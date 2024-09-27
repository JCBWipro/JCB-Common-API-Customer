package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineBHLData;
import com.wipro.jcb.livelink.app.machines.service.reports.*;
import com.wipro.jcb.livelink.app.machines.service.response.MachineDutyCycle;
import com.wipro.jcb.livelink.app.machines.service.response.MachineExcavationMode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:13-09-2024
 * project: JCB-Common-API-Customer
 */

@Component
public interface MachineBHLDataRepo extends CrudRepository<MachineBHLData, String> {
    @Async
    public <S extends MachineBHLData> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends MachineBHLData> S save(S entity);

    public MachineBHLData findByDayAndVin(Date day, String vin);

    public List<MachineBHLData> findByVin(String vin);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.DutyCycleBHL(md.day,Coalesce(md.attachment,0),Coalesce(md.idling,0),Coalesce(md.excavation,0),Coalesce(md.loading,0),Coalesce(md.roading,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<DutyCycleBHL> getDutyCycleData(String vin, Date startDay, Date endDay);

    //ExcavationModes
    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.ExcavationModesBHL(md.day,Coalesce(md.economyModeHrs,0),Coalesce(md.powerModeHrs,0),Coalesce(md.activeModeHrs,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<ExcavationModesBHL> getExcavationModesData(String vin, Date startDay, Date endDay);

    //GearTimeSpent(Date day, Double firstGear, Double secoundGear, Double thirdGear, Double forthGear)
    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.GearTimeSpentBHL(md.day,Coalesce(md.gear1Utilization,0),Coalesce(md.gear2Utilization,0),Coalesce(md.gear3Utilization,0),Coalesce(md.gear4Utilization,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<GearTimeSpentBHL> getGearTimeSpentData(String vin, Date startDay, Date endDay);

    //MachineCompassBHL(Date day, Double md.forwardDirection, md.reverseDirection, md.neutralDirection)
    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.MachineCompassBHL(md.day,Coalesce(md.forwardDirection,0), Coalesce(md.reverseDirection,0), Coalesce(md.neutralDirection,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<MachineCompassBHL> getMachineCompassBHLData(String vin, Date startDay, Date endDay);

    @Transactional
    @Modifying
    @Query("delete from MachineBHLData bhl where bhl.day < ?1 and bhl.vin in (select vin from Machine where premiumFeature=false) ")
    public void deletByDay(Date date);

    @Query(value = "select day\\:\\:date from generate_series(?2, ?3, interval '1 day') as day where day not in("
            + "select day from machine_bhl_data where day between ?2 and ?3 and vin = ?1)", nativeQuery = true)
    public List<Date> getDataMissingDates(String vin, Date startDate, Date endDate);

    @Query("select count(*) from MachineBHLData com where com.day = ?1")
    public Long getBhlCountByYesterday(Date startDate);

    @Query(value ="SELECT new com.jcb.livelinkappserver.api.response.MachineExcavationMode (coalesce(sum(m.economyModeHrs),0),coalesce(sum(m.powerModeHrs),0),coalesce(sum(m.activeModeHrs),0))  FROM MachineBHLData m where m.vin= ?1 AND m.day >= ?2 AND m.day <= ?3",nativeQuery=true)
    public MachineExcavationMode getExcavationModes(String vin, Date day, Date day2);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.MachineDutyCycle (coalesce(sum(md.attachment),0),coalesce(sum(md.idling),0),coalesce(sum(md.excavation),0),coalesce(sum(md.loading),0),coalesce(sum(md.roading),0))  FROM MachineBHLData md where md.vin= ?1 AND md.day >= ?2 AND md.day <= ?3",nativeQuery=true)
    public MachineDutyCycle getMachineDutyCycle(String vin, Date day, Date day2);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionBHL(md.day,Coalesce(md.totalFuelUsedInLtrs,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<FuelConsumptionBHL> getFuelConsumptionData(String vin, Date startDate, Date endDate);


    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionBHL(md.day,Coalesce(md.averageFuelConsumption,0)) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<FuelConsumptionBHL> getAverageFuelConsumption(String vin, Date startDate, Date endDate);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs,md.averageFuelConsumption) from MachineBHLData md where md.vin =?1 and  (md.day between ?2 and ?3) and totalFuelUsedInLtrs is not null order by day",nativeQuery=true)
    public List<FuelConsumption> getFuelConsumptionReport(String vin, Date reportStartDate, Date reportEndDate);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumption(md.day,md.totalFuelUsedInLtrs) from MachineBHLData md where md.vin =?1 and md.totalFuelUsedInLtrs>0 and md.day=?2 ",nativeQuery=true)
    public List<FuelConsumption> getFuelConsumptionDetailsReport(String vin, Date date);

    @Query("select md.totalFuelUsedInLtrs from MachineBHLData md where md.vin =?1 and md.day =?2")
    public Double getFuelConsumption(String vin, Date startDate);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionDuty(md.day,md.fuelUsedInExcavation,md.fuelUsedInExcavationPerct,md.fuelUsedAtLoadingMode,md.fuelUsedAtLoadingModePerct,md.fuelUsedAtRoadingMode,md.fuelUsedAtRoadingModePerct,md.fuelUsedInIdle,md.fuelUsedInIdlePerct) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<FuelConsumptionDuty> getFuelConsumptionDuty(String vin, Date startDate, Date endDate);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionExcavation(md.day,md.fuelUsedAtExcavationEcoMode,md.fuelUsedAtExcavationEcoModePerct,md.subidFuelUsedAtExcavationStandardMode,md.subidFuelUsedAtExcavationStandardModePerct,md.subidFuelUsedAtExcavationPlusMode,md.subidFuelUsedAtExcavationPlusModePerct) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<FuelConsumptionExcavation> getFuelConsumptionExcavation(String vin, Date startDate, Date endDate);

    @Query(value = "SELECT new com.jcb.livelinkappserver.api.response.visualization.report.DistanceTraveledRoading(md.day,md.distanceTravelledInRoading) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day",nativeQuery=true)
    public List<DistanceTraveledRoading> getDistanceTraveledRoading(String vin, Date startDate, Date endDate);

    /*@Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.AverageSpeedRoading(md.day,md.averageSpeedInRoading) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<AverageSpeedRoading> getAverageSpeedRoading(String vin, Date startDate, Date endDate);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.DutyCycleBHL(md.day,md.attachment,md.idling,md.excavation,md.loading,md.roading) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<DutyCycleBHL> getDutyCycleDataV3(String vin, Date startDay, Date endDay);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.ExcavationModesBHL(md.day,md.economyModeHrs,md.powerModeHrs,md.activeModeHrs) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<ExcavationModesBHL> getExcavationModesDataV3(String vin, Date startDay, Date endDay);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.GearTimeSpentBHL(md.day,md.gear1Utilization,md.gear2Utilization,md.gear3Utilization,md.gear4Utilization) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<GearTimeSpentBHL> getGearTimeSpentDataV3(String vin, Date startDay, Date endDay);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.MachineCompassBHL(md.day,md.forwardDirection,md.reverseDirection,md.neutralDirection) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<MachineCompassBHL> getMachineCompassBHLDataV3(String vin, Date startDay, Date endDay);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionBHL(md.day,md.totalFuelUsedInLtrs) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<FuelConsumptionBHL> getFuelConsumptionDataV3(String vin, Date startDate, Date endDate);

    @Query("SELECT new com.jcb.livelinkappserver.api.response.visualization.report.FuelConsumptionBHL(md.day,md.averageFuelConsumption) from MachineBHLData md where md.vin =?1 and md.day between ?2 and ?3 order by day")
    public List<FuelConsumptionBHL> getAverageFuelConsumptionV3(String vin, Date startDate, Date endDate);*/

    @Query(value = "select  count(*) from machine_bhl_data  where day=?2  and average_fuel_consumption >=?1", nativeQuery = true)
    public long getBhlAverageCountYesterday(Double range, Date yesterDay);

    @Query(value = "select  count(*) from machine_bhl_data  where day=?2  and total_fuel_used_in_ltrs >=?1", nativeQuery = true)
    public long getBhlFuelCountYesterday(Double range, Date yesterDay);
}
