package com.wipro.jcb.livelink.app.alerts.commonUtils;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:23-10-2024
 */
@Setter
@Getter
@Data
@ToString
@XmlRootElement
public class AppServerToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String liveLinkToken;
    private String userName;

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
            return other.liveLinkToken == null;
        } else return liveLinkToken.equals(other.liveLinkToken);
    }
}
