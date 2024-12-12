package com.wipro.jcb.livelink.app.dataprocess.repo;

import com.wipro.jcb.livelink.app.dataprocess.entity.FeedParserStatistics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */

@Repository
public interface FeedParserStatisticsRepository extends CrudRepository<FeedParserStatistics, Date> {
}
