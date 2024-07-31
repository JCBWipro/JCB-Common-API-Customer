package com.wipro.jcb.livelink.app.auth.exception;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:30-07-2024
 * project: JCB-Common-API-Customer
 */
public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
