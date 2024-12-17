package com.wipro.jcb.livelink.app.machines.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:17-10-2024
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiOK implements Serializable {
    @Serial
    private static final long serialVersionUID = -7633140890278362102L;
    /*
     * additional text from server. Clients may use this value to debugging or
     * logging, but should not rely on the value of this property, for server can
     * omit to set message for performance. Example : "string"
     */
    private String message;

}
