package com.wipro.jcb.livelink.app.alerts.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 *
 */
public enum UserType {
        Customer, Dealer, JCB;
        public static Map<UserType, List<String>> getMapping() {
            Map<UserType, List<String>> mapping = new HashMap<>();
            ArrayList<String> customers = new ArrayList<>();
            customers.add("Customer Care");
            customers.add("Customer Fleet Manager");
            customers.add("Customer");
            customers.add("MA Manager");
            mapping.put(UserType.Customer, customers);
            ArrayList<String> dealers = new ArrayList<>();
            dealers.add("Dealer Admin");
            dealers.add("Dealer");
            mapping.put(UserType.Dealer, dealers);
            ArrayList<String> jcbs = new ArrayList<>();
            jcbs.add("JCB Admin");
            jcbs.add("JCB HO");
            jcbs.add("JCB RO");
            jcbs.add("Super Admin");
            mapping.put(UserType.JCB, jcbs);
            return mapping;
        }
    }
