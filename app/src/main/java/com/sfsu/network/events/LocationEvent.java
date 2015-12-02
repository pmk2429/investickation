package com.sfsu.network.events;

import com.sfsu.entities.EntityLocation;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class LocationEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Location Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<EntityLocation, String> {

        public String apiRequestMethod;

        public OnLoadingInitialized(String locationId, String apiRequestMethod) {
            super(locationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(EntityLocation entityLocation, String locationId, String apiRequestMethod) {
            super(entityLocation, locationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(EntityLocation entityLocation, String apiRequestMethod) {
            super(entityLocation, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public String getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Location Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<EntityLocation> {
        public OnLoaded(EntityLocation entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of Location Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
