package com.wipro.jcb.livelink.app.machines.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */
/*
 * Error object with code and message. code is bound to status code, but not always same to standard
 * HTTP status text. For example, some 409 error may have code "Invalid Argument" instead of "Conflict".
 * So, Client should read message property to know what happend exactly, when an error is returned
 * from server. And, some 500 errors can have system errno instead of useless "internal". Like other
 * errors, details are hidden in message.
 */
@Setter
@Getter
public class ApiError implements Serializable {
    @Serial
    private static final long serialVersionUID = -1122688221544728777L;
    /*
     * optional stack trace for this error. Server should not include stack trace in
     * production and client should not print or show this stack to user. This
     * property should be used in 'developer mode' only, for debugging. Example :
     * "string"
     */
    @Schema(description = "Code representing HTTP response error code", example = "500", required = true)
    private int code;
    /*
     * optional Example : "string" string
     */
    @JsonIgnore
    @Schema(description = "error no is Internal errorno or error description for server", example = "-", required = false)
    private String errno;
    /*
     * optional errno code for some internal errors in server. Since service
     * implementation can use many different platform api & runtime, client should
     * avoid relying on errno code. If server provides errno code, it should be
     * translated into human readable string like ENOENT or ENOMEM, not pure integer
     * value. Example : "string"
     */
    @Schema(description = "Description about error", example = "-", required = false)
    private String message;
    /*
     * required Example : "string" string
     */
    @JsonIgnore
    @Schema(description = "Stacktrace of error", example = "-", required = false)
    private String stack;

    public ApiError(HttpStatus httpStatus, String message) {
    }

    @Override
    public String toString() {
        return "ApiError [code=" + code + ", errno=" + errno + ", message=" + message + ", stack=" + stack + "]";
    }

    public String toJsonString() {
        final JSONObject jobj = new JSONObject();
        jobj.put("code", this.code);
        jobj.put("message", this.message);
        jobj.put("stack", this.stack);
        jobj.put("errno", this.errno);
        return jobj.toString();
    }

    public ApiError() {
        super();
    }

    public ApiError(String code, String errno, String message, String stack) {
        super();
        this.code = Integer.parseInt(code);
        this.errno = errno;
        this.message = message;
        this.stack = stack;
    }

    public ApiError(HttpStatus code, String errno, String message, String stack) {
        this.code = Integer.parseInt(String.valueOf(code));
        this.errno = errno;
        this.message = message;
        this.stack = stack;
    }

    public ApiError(int code, String errno, String message, String stack) {
        this.code = code;
        this.errno = errno;
        this.message = message;
        this.stack = stack;
    }

}

