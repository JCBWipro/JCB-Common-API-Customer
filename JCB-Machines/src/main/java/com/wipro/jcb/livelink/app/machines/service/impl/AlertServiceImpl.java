package com.wipro.jcb.livelink.app.machines.service.impl;

import com.wipro.jcb.livelink.app.machines.repo.AlertRepository;
import com.wipro.jcb.livelink.app.machines.repo.MachineRepository;
import com.wipro.jcb.livelink.app.machines.service.AlertService;
import com.wipro.jcb.livelink.app.machines.service.response.Filter;
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

    @Override
    public List<Filter> getFilters(String userName) {
        log.debug("Fetching distinct models for user: {}", userName);
        List<String> models = machineRepository.findDistinctModelForUsers(userName);
        log.debug("Found {} distinct models for user: {}", models.size(), userName);

        List<Filter> filters = new ArrayList<>();
        for (String model : models) {
            Filter f = new Filter(model);
            filters.add(f);
        }

        log.debug("Fetching distinct platforms for user: {}", userName);
        List<String> platforms = machineRepository.findDistinctPlatformForUsers(userName);
        log.debug("Found {} distinct platforms for user: {}", platforms.size(), userName);

        for (String platform : platforms) {
            Filter f = new Filter(platform);
            filters.add(f);
        }

        log.debug("Returning {} filters for user: {}", filters.size(), userName);
        return filters;
    }

}
