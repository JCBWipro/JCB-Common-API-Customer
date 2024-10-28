package com.wipro.jcb.livelink.app.reports.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wipro.jcb.livelink.app.reports.entity.PdfAnalyticsData;

@Repository
public interface PdfAnalyticsDataRepo extends CrudRepository<PdfAnalyticsData, Date> {

	@Query(value = "select * from pdf_analytics_data where vin=?1 and day=?2", nativeQuery = true)
	PdfAnalyticsData findByVin(String firmware, Date day);

	@Query(value = "select count(*) from pdf_analytics_data where vin=?1 and day=?2", nativeQuery = true)
	Long countByVinAndDay(String firmware, Date day);

	@Query(value = "select distinct vin from pdf_analytics_data", nativeQuery = true)
	List<String> getPdfFile();
	
	@Modifying
	@Transactional
	@Query(value = "update pdf_analytics_data set count=:count where vin=:vin and day=:day", nativeQuery = true)
	void updateCountByVinAndDate(Long count, String vin, Date day);
	
}
