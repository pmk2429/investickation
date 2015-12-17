package com.sfsu.network.events;

import com.sfsu.entities.User;

/**
 * Defines all the User events performed under {@link User} Entity such as CRUD, searching user etc.
 * <p>
 * Created by Pavitra on 11/27/2015.
 */
public class UserEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the User Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<User, String> {

        public int apiRequestMethod;

        public OnLoadingInitialized(String userId, int apiRequestMethod) {
            super(userId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(User user, String userId, int apiRequestMethod) {
            super(user, userId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(User user, int apiRequestMethod) {
            super(user, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public int getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the User Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<User> {
        public Integer count;

        public OnLoaded(User entity) {
            super(entity);
        }

        public OnLoaded(Integer count) {
            super(new User());
            this.count = count;
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
