package com.sfsu.network.error;

/**
 * Wrapper for the Error status and message sent from the server
 * Created by Pavitra on 12/27/2015.
 */
public class ApiError {
    private int statusCode;
    private String message;

    public ApiError() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
