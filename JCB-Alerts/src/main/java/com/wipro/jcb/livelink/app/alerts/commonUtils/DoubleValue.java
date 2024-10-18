package com.wipro.jcb.livelink.app.alerts.commonUtils;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoubleValue implements Serializable {
    @Serial
    private static final long serialVersionUID = 8445175318692900690L;
    Double val;

}
