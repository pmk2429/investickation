package com.sfsu.network.events;

import com.sfsu.session.LoginResponse;

/**
 * LoginEvent when the user enters email and password and clicks on Login button.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class LoginEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Account Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<String, String> {

        public int apiRequestMethod;
        public String email;
        public String password;

        public OnLoadingInitialized(String email, String password) {
            super(email);
            this.email = email;
            this.password = password;
        }

        public int getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Account Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<LoginResponse> {
        public OnLoaded(LoginResponse responseBody) {
            super(responseBody);
        }
    }

    /**
     * Event Handler When loading of Account Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
