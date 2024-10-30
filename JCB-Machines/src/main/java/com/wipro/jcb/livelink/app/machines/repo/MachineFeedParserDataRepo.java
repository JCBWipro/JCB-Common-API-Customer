package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.MachineFeedParserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public interface MachineFeedParserDataRepo extends CrudRepository<MachineFeedParserData, String> {
    @Async
    public <S extends MachineFeedParserData> Iterable<S> save(Iterable<S> entities);

    @Override
    public <S extends MachineFeedParserData> S save(S entity);

    @Query(value = "select * from machine_feedparser_data m where m.vin =:vin and m.last_modified_date is not null", nativeQuery = true)
    MachineFeedParserData findByVin(String vin);

}