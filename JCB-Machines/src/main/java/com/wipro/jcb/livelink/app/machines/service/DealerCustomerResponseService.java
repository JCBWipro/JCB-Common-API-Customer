package com.wipro.jcb.livelink.app.machines.service;

import com.wipro.jcb.livelink.app.machines.dto.DealerCustomers;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * DealerCustomerResponseService is an interface that defines the contract for services related to dealer customer responses.
 */
public interface DealerCustomerResponseService {

    //Retrieves a list of dealer customers based on the provided search criteria, pagination details, and user information.
    DealerCustomers getDealerCustomers(String userName, String search, int pageNumber, int pageSize)
            throws ProcessCustomError;
}
