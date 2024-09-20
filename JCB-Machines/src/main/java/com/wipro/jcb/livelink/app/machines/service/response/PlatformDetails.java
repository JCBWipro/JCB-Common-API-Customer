package com.wipro.jcb.livelink.app.machines.service.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:15-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDetails {
    @ApiModelProperty(value = "Machine Platform", example = "Backhoe Loader", required = true)
    private String platform;
    @ApiModelProperty(value = "Number of machine belong to this platform", example = "3", required = true)
    private Long count;

    @Override
    public String toString() {
        return "PlatformDetails [platform=" + platform + ", count=" + count + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((platform == null) ? 0 : platform.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PlatformDetails other = (PlatformDetails) obj;
        if (platform == null) {
            return other.platform == null;
        } else return platform.equals(other.platform);
    }
}
