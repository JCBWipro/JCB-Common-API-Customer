package com.wipro.jcb.livelink.app.machines.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/2/2024
 * project: JCB-Common-API-Customer
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserVinList {
    List<String> vin;
}
