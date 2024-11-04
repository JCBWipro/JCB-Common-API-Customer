package com.wipro.jcb.livelink.app.machines.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wipro.jcb.livelink.app.machines.entity.DashboardData;

@Repository
public interface DashboardDataRepository extends JpaRepository<DashboardData, Integer> {
	
	@Query(value="SELECT * from dashboard_data where type=:type", nativeQuery = true)
	DashboardData getDashboardData(String type);

}
