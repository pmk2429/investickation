package com.sfsu.network.events;

import com.sfsu.entities.EntityLocation;

import java.util.List;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class LocationEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Location Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<EntityLocation, String> {

        public int apiRequestMethod;

        public OnLoadingInitialized(String locationId, int apiRequestMethod) {
            super(locationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(EntityLocation entityLocation, String locationId, int apiRequestMethod) {
            super(entityLocation, locationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(EntityLocation entityLocation, int apiRequestMethod) {
            super(entityLocation, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public int getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Location Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Object> {
        public OnLoaded(EntityLocation entity) {
            super(entity);
        }

        public OnLoaded(List<EntityLocation> locationList) {
            super(locationList);
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
