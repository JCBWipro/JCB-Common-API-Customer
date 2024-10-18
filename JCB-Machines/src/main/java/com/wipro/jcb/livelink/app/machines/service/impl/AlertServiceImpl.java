package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.repo.AlertRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.service.AlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-10-2024
 */
@Slf4j
@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    AlertRepository alertRepository;
    @Autowired
    MachineRepository machineRepository;

    @Override
    public List<String> getSuggestions(String word, String userName) {
        log.debug("Creating suggestion list for alerts ");
        List<String> suggestions = new ArrayList<>();
        log.debug("Creating suggestion list for alerts of Customer {}", userName);
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionTag(userName, word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionVin(userName, word));
        suggestions.addAll(alertRepository.getByMachinesAndSuggestionEventName(
                machineRepository.getDistinctUsersUserNameAndSuggestionVin(userName), word));
        suggestions.addAll(alertRepository.getByMachinesAndSuggestionEventLevel(
                machineRepository.getDistinctUsersUserNameAndSuggestionVin(userName), word));
        suggestions.addAll(alertRepository.getByMachinesAndSuggestionLocation(
                machineRepository.getDistinctUsersUserNameAndSuggestionVin(userName), word));
        suggestions.addAll(machineRepository.getByUsersUserNameAndSuggestionSite(userName, word));
        return suggestions;
    }
}
