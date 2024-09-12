package com.wipro.jcb.livelink.app.user.web.exception;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:8/28/2024
 * project: JCB-Common-API-Customer
 */
public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException (String message){
        super(message);
    }

}
