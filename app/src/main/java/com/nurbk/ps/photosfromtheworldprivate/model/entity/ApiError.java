package com.nurbk.ps.photosfromtheworldprivate.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * General class that represent all kind of network and http errors
 */
public class ApiError {

    /**
     * Http error code
     */
    @SerializedName("code")
    @Expose
    private int code;

    /**
     * Error message
     */
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * Boolean value to indicate if this error can be solve by re-executing the request
     */
    @Expose(serialize = false, deserialize = false)
    private boolean isRecoverable;

    /**
     * Constructor to create ApiError instance that represent HttpErrors
     *
     * @param code    http error code
     * @param message error message
     */
    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Constructor to create ApiError instance that represent Network errors and exceptions
     *
     * @param message error message
     */
    public ApiError(String message) {
        this.message = message;
        this.isRecoverable = true;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRecoverable() {
        return isRecoverable;
    }

    public void setRecoverable(boolean recoverable) {
        isRecoverable = recoverable;
    }

}