package com.wipro.jcb.livelink.app.machines.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
public class ProcessCustomError extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -5137371098583567742L;
    private ApiError apierror;
    @Setter
    @Getter
    private HttpStatus status;

    public ProcessCustomError(String msg, HttpStatus status) {
        apierror = new ApiError();
        apierror.setMessage(msg);
        apierror.setCode(Integer.parseInt(String.valueOf(status)));
        this.status = status;
    }

    public ProcessCustomError(String msg, String error, HttpStatus status) {
        apierror = new ApiError();
        apierror.setErrno(error);
        apierror.setMessage(msg);
        apierror.setCode(Integer.parseInt(String.valueOf(status)));
        this.status = status;
    }

    public ProcessCustomError() {
    }

    public ApiError getApiMessages() {
        return apierror;
    }
}
