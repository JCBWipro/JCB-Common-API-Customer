package com.wipro.jcb.livelink.app.machines.repo;

import com.sun.jdi.DoubleValue;
import com.wipro.jcb.livelink.app.machines.commonUtils.DateValue;
import com.wipro.jcb.livelink.app.machines.entity.MachineFuelHistory;
import com.wipro.jcb.livelink.app.machines.service.response.MachineFuelHistoryData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Component
public interface MachineFuelHistoryDataRepo extends PagingAndSortingRepository<MachineFuelHistory, String> {

	/*@Query("SELECT new com.jcb.livelinkappserver.common.DoubleValue( fueldata.fuelLevel) from MachineFuelHistory fueldata where ?1 = fueldata.vin And fueldata.dateTime >= ?2 order by fueldata.dateTime ASC")
	public List<DoubleValue> getFuelLevelByVin(String vin, Date date);
	@Query("SELECT new com.jcb.livelinkappserver.common.DoubleValue( fueldata.fuelLevel) from MachineFuelHistory fueldata where ?1 = fueldata.vin And fueldata.dateTime between ?2 and ?3 order by fueldata.dateTime ASC")
	public List<DoubleValue> getFuelLevelByVin(String vin, Date startDate, Date endDate);

	@Query("SELECT new com.jcb.livelinkappserver.common.DateValue(fueldata.dateTime) from MachineFuelHistory fueldata where ?1 = fueldata.vin And fueldata.dateTime >= ?2 order by fueldata.dateTime ASC")
	public List<DateValue> getDateByVin(String vin, Date date);
	@Query("SELECT new com.jcb.livelinkappserver.common.DateValue(fueldata.dateTime) from MachineFuelHistory fueldata where ?1 = fueldata.vin And fueldata.dateTime between ?2 and ?3 order by fueldata.dateTime ASC")
	public List<DateValue> getDateByVin(String vin, Date startDate, Date endDate);
	@Transactional
	@Modifying
	@Query("delete from  MachineFuelHistory fueldata where fueldata.dateTime < ?1")
	public void deleteByDate( Date date);
	*/
    //@Query("SELECT new com.jcb.livelinkappserver.api.response.MachineFuelStatus (m.fuelLevel,m.dateTime)  FROM MachineFuelHistory m where m.vin= ?1 order by m.dateTime desc")
    //public MachineFuelStatus getFuelStatusByVin(String vin);
	
	/*@Query(value="select * from machinefuelhistorydata where vin= :vin order by date_time desc limit 1",nativeQuery=true)
	public MachineFuelHistory getFuelStatusByVin(@Param("vin")String vin);
	
	
	@Query(value="select fuel_level from machinefuelhistorydata where vin= :vin and  date_time >= :day1 and date_time <= :day2 order by date_time desc limit 1",nativeQuery=true)
	public DoubleValue getFuelLevelByOneHour(@Param("vin")String vin, @Param("day1") Date startDate, @Param("day2")Date endDate);
	
	@Query(value="select date_time from machinefuelhistorydata where vin= :vin and  date_time >= :day1 and date_time <= :day2 order by date_time desc limit 1",nativeQuery=true)
	public Date getDateLevelByOneHour(@Param("vin")String vin, @Param("day1") Date startDate, @Param("day2")Date endDate);*/

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.MachineFuelHistoryData (fueldata.dateTime,fueldata.fuelLevel) from MachineFuelHistory fueldata where ?1 = fueldata.vin And fueldata.dateTime between ?2 and ?3 order by fueldata.dateTime ASC", nativeQuery = true)
    public List<MachineFuelHistoryData> getFuelDetails(String vin, Date startDate, Date endDate);

    //getFuelLvel
    @Query(value = "SELECT fueldata.fuel_level from machinefuelhistorydata fueldata where fueldata.vin=?1 And fueldata.date_time between ?2 and ?3 order by fueldata.date_time ASC", nativeQuery = true)
    public List<DoubleValue> getFuelLevelByVin(String vin, Date startDate, Date endDate);

    //getDateByVin
    @Query(value = "SELECT fueldata.date_time from machinefuelhistorydata fueldata where fueldata.vin=?1 And fueldata.date_time between ?2 and ?3 order by fueldata.date_time ASC", nativeQuery = true)
    public List<DateValue> getDateByVin(String vin, Date startDate, Date endDate);
}
