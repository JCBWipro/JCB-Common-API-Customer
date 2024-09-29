package com.wipro.jcb.livelink.app.user.web.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.jcb.livelink.app.user.web.entity.AssetControlUnitEntity;
import com.wipro.jcb.livelink.app.user.web.reponse.AssetAndProductResponse;

public interface AssetControlUnitRepo extends JpaRepository<AssetControlUnitEntity, String>{
	
	@Query(nativeQuery = true, value = "select serial_number from asset where Machine_Number=:machineNumber")
	public String getSerialNumberFromAsset(@Param("machineNumber") String machineNumber);
	
	@Query(nativeQuery = true, value = "SELECT Serial_Number FROM wise.asset_control_unit where Serial_Number=:serialNumber")
	public String getSerialNumber(@Param("serialNumber") String serialNumber);
	
	@Query(nativeQuery = true, value = "select aa.*,product.* from ( select ams.Serial_Number, ams.TxnData, a.Engine_Number,a.Product_ID from asset_monitoring_snapshot ams, asset a  where a.Serial_Number=ams.Serial_Number and ams.Serial_Number=:serialNumber) aa left outer join (SELECT ag.Asseet_Group_Name, ap.operatingStartTime, ap.operatingEndTime,p.Product_ID FROM  asset_group ag, products p, asset_profile ap WHERE p.Asset_Group_ID=ag.Asset_Group_ID and ap.serialNumber = :serialNumber ) product ON aa.Product_ID=product.Product_ID")
	public AssetAndProductResponse getDetailsIfMachineEmpty(@Param("serialNumber") String serialNumber);
	
	@Query(nativeQuery = true, value = "select aa.*,product.* from (select ams.Serial_Number, ams.TxnData, a.Engine_Number,a.Product_ID from asset_monitoring_snapshot ams, asset a,custom_asset_group_snapshot cags  where a.Serial_Number=ams.Serial_Number and ams.Serial_Number = cags.Asset_Id and ams.Serial_Number=:serialNumber and cags.Group_ID in :customMachineGroupIdList) aa left outer join (SELECT ag.Asseet_Group_Name, ap.operatingStartTime, ap.operatingEndTime,p.Product_ID FROM  asset_group ag, products p, asset_profile ap WHERE p.Asset_Group_ID=ag.Asset_Group_ID and ap.serialNumber = :serialNumber) product ON aa.Product_ID=product.Product_ID")
	public AssetAndProductResponse getDetailsIfMachineNotEmpty(@Param("serialNumber") String serialNumber);
	
	@Query(nativeQuery = true, value = "select aa.*,product.* from (select ams.Serial_Number, ams.TxnData, a.Engine_Number,a.Product_ID from asset_monitoring_snapshot ams, asset a,custom_asset_group_snapshot cags  where a.Serial_Number=ams.Serial_Number and ams.Serial_Number = cags.Asset_Id and ams.Serial_Number=:serialNumber) aa left outer join (SELECT ag.Asseet_Group_Name, ap.operatingStartTime, ap.operatingEndTime,p.Product_ID FROM  asset_group ag, products p, asset_profile ap WHERE p.Asset_Group_ID=ag.Asset_Group_ID and ap.serialNumber = :serialNumber ) product ON aa.Product_ID=product.Product_ID")
	public AssetAndProductResponse getDetails(@Param("serialNumber") String serialNumber);
}
