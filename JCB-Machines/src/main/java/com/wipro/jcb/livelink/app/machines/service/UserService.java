package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.service.response.Filter;
import com.wipro.jcb.livelink.app.machines.service.response.UserProfile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserService deals with all the interaction with UserRepository
 * <p>
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:9/30/2024
 * project: JCB-Common-API-Customer
 */
@Component
public interface UserService {
    /**
     * Get userprofile based on token
     *
     * @param version
     * @return UserProfile is the instance of UserProfile
     * @throws ProcessCustomError
     */
    UserProfile getUserProfile(String userName, String version) throws ProcessCustomError;

    Boolean getuserMapping(String userName);

    List<String> getSuggestions(String word, String userName);

    List<Filter> getFiltersCustomer(String userName);

}
