package com.wipro.jcb.livelink.app.dataprocess.constants;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
public enum UserType {
    CUSTOMER ("Customer"),
    DEALER("Dealer"),
    JCB("JCB");
    final String roleName;
    private UserType(String roleName) {
        this.roleName= roleName;
    }
    public String getName() {
        return this.roleName;
    }
}
