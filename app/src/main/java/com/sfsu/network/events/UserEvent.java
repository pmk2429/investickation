package com.sfsu.network.events;

import com.sfsu.entities.User;

/**
 * Defines all the User events performed under {@link User} Entity such as CRUD, searching user etc.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class UserEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the User Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<User, String> {

        public String httpRequestType, userResourceName, userId;

        public OnLoadingInitialized(String userId) {
            super(userId);
        }

        public OnLoadingInitialized(User user, String httpRequestType, String userId) {
            super(user, httpRequestType);
            this.httpRequestType = httpRequestType;
            this.userId = userId;
            this.userResourceName = user.getResourceType();
        }
    }

    /**
     * Event Handler when the User Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<User> {
        public OnLoaded(User entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of User Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
