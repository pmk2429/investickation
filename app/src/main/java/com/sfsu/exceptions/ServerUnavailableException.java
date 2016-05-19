package com.sfsu.exceptions;

/**
 * Defines checked exception when the Server is not available or connection cant be established to the server.
 * <p>
 * Created by Pavitra on 10/21/2015.
 */
public class ServerUnavailableException extends Exception {
    public ServerUnavailableException() {
        super();
    }

    public ServerUnavailableException(String message) {
        super(message);
    }

    public ServerUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerUnavailableException(Throwable cause) {
        super(cause);
    }
}
