package com.wipro.jcb.livelink.app.machines.service;

import java.util.List;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:18-10-2024
 */
public interface AlertService {

    List<String> getSuggestions(String word, String userName);
}
