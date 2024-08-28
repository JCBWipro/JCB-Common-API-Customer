package com.wipro.jcb.livelink.app.user.exception;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:8/28/2024
 * project: JCB-Common-API-Customer
 */
public class NoMachineGroupFoundException extends RuntimeException{
    public NoMachineGroupFoundException(String message){
        super(message);
    }
}
