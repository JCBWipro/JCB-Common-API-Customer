package com.wipro.jcb.livelink.app.machines.repo;

import com.wipro.jcb.livelink.app.machines.entity.UsersFeedbackData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on UsersFeedbackData entities
 */
@Repository
public interface UsersFeedbackDataRepo extends CrudRepository<UsersFeedbackData, String> {
    @Override
    <S extends UsersFeedbackData> S save(S entity);
}
