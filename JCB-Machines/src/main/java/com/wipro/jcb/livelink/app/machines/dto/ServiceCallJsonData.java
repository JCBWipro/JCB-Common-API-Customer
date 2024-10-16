package com.wipro.jcb.livelink.app.machines.dto;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 * project: JCB-Common-API-Customer
 */
public interface ServiceCallJsonData {
    String getlabel();

    String getfield();

    List<String> getvalue();

    String gettype();

    Boolean getrequired();

    String getfield_name();
}
