package com.wipro.jcb.livelink.app.user.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wipro.jcb.livelink.app.user.web.dto.MapImpl;
import com.wipro.jcb.livelink.app.user.web.dto.MapReqContract;
import com.wipro.jcb.livelink.app.user.web.dto.MapRespContract;
import com.wipro.jcb.livelink.app.user.web.exception.MachineVinException;
import com.wipro.jcb.livelink.app.user.web.repo.AssetControlUnitRepo;
import com.wipro.jcb.livelink.app.user.web.repo.TenancyRepo;
import com.wipro.jcb.livelink.app.user.web.reponse.AssetAndProductResponse;

@Service
public class SearchByVinService {
	
	private static final Logger log = LoggerFactory.getLogger(SearchByVinService.class);
	
	@Autowired
	private TenancyRepo tenancyRepo;
	
	@Autowired
	private AssetControlUnitRepo assetControlUnitRepo;
	
	public List<MapRespContract> getMap(String userName, String vin){
		log.info("In SearchByVinService::getMap() userName:{} and vin:{}", userName, vin);
		
		MapReqContract mapReqContract = setMapReqContractDetails(userName, vin);
		
		int mapMarkerKey = 0;
		String countryCode=null;
		List<Integer> linkedTenancyList=new ArrayList<Integer>();
		List<MapRespContract> respObj=new ArrayList<MapRespContract>();
		
		if(mapReqContract.getSerialNumberList()==null || mapReqContract.getSerialNumberList().size() == 0){
			if(mapReqContract.getLoginId().split("\\|").length > 1){
				if(mapReqContract.getLoginId().split("\\|")[1] != null && !mapReqContract.getLoginId().split("\\|")[1].equalsIgnoreCase("null")){
					mapMarkerKey = Integer.parseInt(mapReqContract.getLoginId().split("\\|")[1]);
				}
			}
			if(mapReqContract.getLoginId().split("\\|").length > 2){
				countryCode=mapReqContract.getLoginId().split("\\|")[2];
			}
			mapReqContract.setLoginId(mapReqContract.getLoginId().split("\\|")[0]);
		}
		
		String serialNumber = validateVIN(mapReqContract.getLoginUserTenancyList().get(0), mapReqContract.getSerialNumberList().get(0));
		if(serialNumber == null){
			throw new MachineVinException("MACHINE WITH VIN NUMBER "+mapReqContract.getSerialNumberList().get(0)+" NOT FOUND");
		}
		if(mapReqContract.getLoginUserTenancyList()!=null && mapReqContract.getLoginUserTenancyList().size()>0){
			mapReqContract.setLoginUserTenancyList(getLinkedTenancyListForTheTenancy(mapReqContract.getLoginUserTenancyList()));
		}
		if(mapReqContract.getTenancy_ID()!=null && mapReqContract.getTenancy_ID().size()>0){
			linkedTenancyList=getLinkedTenancyListForTheTenancy(mapReqContract.getTenancy_ID());
			if(linkedTenancyList!=null && linkedTenancyList.size()>0)
				mapReqContract.setTenancy_ID(linkedTenancyList);
			else
				return respObj;
		}
		return getNewMapDetails(mapReqContract, mapMarkerKey,countryCode);
	}
	
	public MapReqContract setMapReqContractDetails(String userName, String vin) {
		log.info("In SearchByVinService::setMapReqContractDetails() userName:{} and vin:{}", userName, vin);
		MapReqContract mapReqContract = new MapReqContract();
    	List<String> serialNumberList = new ArrayList<String>();
		serialNumberList.add(vin);
		mapReqContract.setSerialNumberList(serialNumberList);
		mapReqContract.setLoginId(userName);
		List<Integer> tenIdList = tenancyRepo.getTenancyIdByUserName(userName);
		mapReqContract.setTenancy_ID(tenIdList);
		mapReqContract.setLoginUserTenancyList(tenIdList);
		return mapReqContract;
	}
	
	public String validateVIN(int loginTenancyId, String serialNumber) {
		log.info("In SearchByVinService::validateVIN() loginTenancyId:{} and serialNumber:{}", loginTenancyId, serialNumber);

		if (serialNumber != null && (serialNumber.trim().length() == 7 || serialNumber.trim().length() == 17)) {
			if (serialNumber.trim().length() == 7) {
				serialNumber = tenancyRepo.getMachineNumber(serialNumber);
				if (serialNumber == null) {
					log.info("In SearchByVinService::validateVIN() Machine serialNumber:{} does not exist !!!", serialNumber);
					return null;
				}
			}
		}
		return tenancyRepo.getSerialNumberByTenancyId(loginTenancyId, serialNumber);
	}
	
	public List<Integer> getLinkedTenancyListForTheTenancy(List<Integer> tenancyIdList){
		log.info("In SearchByVinService::getLinkedTenancyListForTheTenancy() tenancyIdList:{}", tenancyIdList);
		List<Integer> updatedTenancyIdList = new LinkedList<Integer>();
		List<Integer> tenancyId = tenancyRepo.getLinkedTenancyListForTheTenancy(tenancyIdList);
		List<Integer> tenancyIds =  tenancyRepo.getTenancyDetailsByTenancyId(tenancyId);
		for(Integer result : tenancyIds) {
			updatedTenancyIdList.add(result);
		}
		return updatedTenancyIdList;
	}
	
	public List<MapRespContract> getNewMapDetails(MapReqContract mapReq,int mapMarkerKey,String countryCode){
		log.info("In SearchByVinService::getNewMapDetails() mapReq:{}, mapMarkerKey:{} and countryCode:{}", mapReq, mapMarkerKey,countryCode);
		List<MapImpl> mapImpl=null;
		List<MapRespContract> respList = new LinkedList<MapRespContract>();
		if(mapReq.getSerialNumberList()!=null && mapReq.getSerialNumberList().size()>0)
		{
			MapImpl implObj = getFleetMapDetails(mapReq.getSerialNumberList().get(0),mapReq.getLoginId(),mapReq.getMachineGroupIdList());
			mapImpl = new LinkedList<MapImpl>();
			mapImpl.add(implObj);
		} else {
			//Else Block is for Overview Menu. Code Will be Implemented Later For Overview Menu Section.
		}
		
		for (int i = 0; i < mapImpl.size(); i++) {
			MapRespContract respContractObj = new MapRespContract();
			respContractObj.setNickname(mapImpl.get(i).getNickname());
			if (mapImpl.get(i).getOperatingStartTime() != null) {
				respContractObj.setOperatingStartTime(mapImpl.get(i).getOperatingStartTime().toString());
			} else {
				respContractObj.setOperatingStartTime("");
			}

			if (mapImpl.get(i).getOperatingEndTime() != null) {
				respContractObj.setOperatingEndTime(mapImpl.get(i).getOperatingEndTime().toString());
			} else {
				respContractObj.setOperatingEndTime("");
			}

			respContractObj.setTotalMachineHours(mapImpl.get(i).getTotalMachineHours());
			respContractObj.setLatitude(mapImpl.get(i).getLatitude());
			respContractObj.setLongitude(mapImpl.get(i).getLongitude());
			respContractObj.setEngineStatus(mapImpl.get(i).getEngineStatus());
			respContractObj.setProfileName(mapImpl.get(i).getProfileName());
			respContractObj.setSeverity(mapImpl.get(i).getSeverity());
			respContractObj.setSerialNumber(mapImpl.get(i).getSerialNumber());
			respList.add(respContractObj);
		}
		return respList;
	}
	
	public MapImpl getFleetMapDetails(String SerialNumber,String loginId, List<Integer> customMachineGroupIdList) {
		HashMap<String,String> txnDataMap=new HashMap<String, String>();
		MapImpl implObj = new MapImpl();
		String enginestatus=null;
		
		if(SerialNumber!=null) {
			if(SerialNumber.trim().length()==7) {
				String machineNumber = SerialNumber;
				SerialNumber = assetControlUnitRepo.getSerialNumberFromAsset(machineNumber);
				if(SerialNumber!=null) {
					SerialNumber = assetControlUnitRepo.getSerialNumber(SerialNumber);
				}
				if(SerialNumber==null)
				{
					log.info("In SearchByVinService::validateVIN() Machine Number machineNumber:{} does not exist !!!", machineNumber);
					return implObj;
				}
			}
		}
		AssetAndProductResponse response = null;
		if(customMachineGroupIdList==null || (customMachineGroupIdList!=null && customMachineGroupIdList.isEmpty())){
			response = assetControlUnitRepo.getDetailsIfMachineEmpty(SerialNumber);
		} else if (customMachineGroupIdList != null && !(customMachineGroupIdList.isEmpty())) {
			response = assetControlUnitRepo.getDetailsIfMachineNotEmpty(SerialNumber);
		} else {
			response = assetControlUnitRepo.getDetails(SerialNumber);
		}
		
		implObj.setSerialNumber(response.getSerial_Number());
		implObj.setNickname(response.getEngine_Number());
		implObj.setProfileName(response.getAsseet_Group_Name());
		implObj.setOperatingStartTime(response.getoperatingStartTime());
		implObj.setOperatingEndTime(response.getoperatingEndTime());
		
		txnDataMap = new Gson().fromJson(response.getTxnData(), new TypeToken<HashMap<String, Object>>() {}.getType());
		implObj.setLatitude(txnDataMap.get("LAT"));
		implObj.setLongitude(txnDataMap.get("LONG"));
		
		enginestatus=txnDataMap.get("ENG_STATUS");
		
		if(enginestatus==null){
			enginestatus=txnDataMap.get("EVT_ENG");
		}
		if(enginestatus==null){
			enginestatus="0";
		}
		implObj.setEngineStatus(enginestatus);
		implObj.setTotalMachineHours(txnDataMap.get("CMH"));
		
		return implObj;
	}

}
