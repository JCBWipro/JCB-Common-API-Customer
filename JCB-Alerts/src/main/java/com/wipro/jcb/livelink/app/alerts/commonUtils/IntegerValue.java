package com.wipro.jcb.livelink.app.alerts.commonUtils;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:16-09-2024
 *
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IntegerValue implements Serializable {

    @Serial
    private static final long serialVersionUID = -6313377994788070591L;

    Integer val;
}

