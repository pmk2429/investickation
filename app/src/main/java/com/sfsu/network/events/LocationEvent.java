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

        public String httpRequestType, locationResourceName, locationId;

        public OnLoadingInitialized(String LocationId) {
            super(LocationId);
        }

        public OnLoadingInitialized(EntityLocation entityLocation, String httpRequestType, String locationId) {
            super(entityLocation, httpRequestType);
            this.httpRequestType = httpRequestType;
            this.locationId = locationId;
            this.locationResourceName = entityLocation.getResourceType();
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
