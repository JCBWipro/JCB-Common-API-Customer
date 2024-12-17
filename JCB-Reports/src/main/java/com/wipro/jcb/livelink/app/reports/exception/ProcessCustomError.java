package com.wipro.jcb.livelink.app.reports.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessCustomError extends Exception {
    @Serial
    private static final long serialVersionUID = -5137371098583567742L;
    private ApiError apierror;
    private String errorCode;
    private String details;
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

    public ApiError getApiMessages() {
        return apierror;
    }
}