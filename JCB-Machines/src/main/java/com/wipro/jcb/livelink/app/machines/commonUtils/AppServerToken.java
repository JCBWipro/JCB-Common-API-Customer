package com.wipro.jcb.livelink.app.machines.commonUtils;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@Data
@XmlRootElement
public class AppServerToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String liveLinkToken;
    private String userName;


    @Override
    public String toString() {
        return "AppServerToken [liveLinkToken=" + liveLinkToken + ", userName=" + userName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((liveLinkToken == null) ? 0 : liveLinkToken.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppServerToken other = (AppServerToken) obj;
        if (liveLinkToken == null) {
            if (other.liveLinkToken != null)
                return false;
        } else if (!liveLinkToken.equals(other.liveLinkToken))
            return false;
        return true;
    }
}
