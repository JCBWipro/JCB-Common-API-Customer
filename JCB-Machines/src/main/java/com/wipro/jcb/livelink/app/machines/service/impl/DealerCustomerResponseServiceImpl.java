package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.constants.MessagesList;
import com.wipro.jcb.livelink.app.machines.dto.CustomerInfo;
import com.wipro.jcb.livelink.app.machines.dto.DealerCustomers;
import com.wipro.jcb.livelink.app.machines.entity.StakeHolder;
import com.wipro.jcb.livelink.app.machines.entity.User;
import com.wipro.jcb.livelink.app.machines.exception.ProcessCustomError;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.repo.UserRepository;
import com.wipro.jcb.livelink.app.machines.service.DealerCustomerResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/28/2024
 */

/**
 * DealerCustomerResponseServiceImpl is a service implementation class that handles the retrieval of dealer customers.
 */
@Slf4j
@Service
public class DealerCustomerResponseServiceImpl implements DealerCustomerResponseService {
    @Autowired
    MachineRepository machineRepo;
    @Autowired
    UserRepository userRepository;
    @Override
    public DealerCustomers getDealerCustomers(String userName, String search, int pageNumber, int pageSize)
            throws ProcessCustomError {
        final DealerCustomers allCustomers = new DealerCustomers();
        final List<CustomerInfo> customers = new ArrayList<>();
        List<StakeHolder> allCustomersList;

        try {
            long start = System.currentTimeMillis();
            if ("optional".equals(search)) {
                allCustomersList = machineRepo.getCustomersByUsersUserNameWithAllMachines(userName,
                        PageRequest.of(pageNumber, pageSize));

            } else {
                allCustomersList = machineRepo.getCustomersByUsersUserNameWithAllMachinesWithSearch(userName, search,
                        PageRequest.of(pageNumber, pageSize));
            }
            for (final StakeHolder customer : allCustomersList) {
                final User customerUser = userRepository.findByUserName(customer.getCustomer().getId());
                if (customerUser != null) {
                    customers.add(new CustomerInfo(customer.getCustomer().getId(), customerUser.getThumbnail(),
                            customer.getCustomer().getName(), customer.getCustomer().getPhonenumber(),
                            customer.getCustomer().getAddress(), customer.getCustomer().getCountry(),
                            customer.getCount()));
                } else {
                    customers.add(new CustomerInfo(customer.getCustomer().getId(), "", customer.getCustomer().getName(),
                            customer.getCustomer().getPhonenumber(), customer.getCustomer().getAddress(),
                            customer.getCustomer().getCountry(), customer.getCount()));
                }
            }
            log.info("getDealerCustomers: end of request for user{}", userName);
            long end = System.currentTimeMillis();
            long elapsedTime = end - start;
            log.info("DashboardCustomers API Duration :{}-{}-{}", elapsedTime, userName, search);
            allCustomers.setCustomers(customers);
        } catch (final Exception ex) {
            log.error("getDealerCustomers: Failed to retrieve all customer list {}", ex.getMessage());
            throw new ProcessCustomError(MessagesList.APP_REQUEST_PROCESSING_FAILED, ex.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
        return allCustomers;
    }
}
