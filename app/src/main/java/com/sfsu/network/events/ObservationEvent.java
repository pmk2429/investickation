package com.sfsu.network.events;

import com.sfsu.entities.Observation;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class ObservationEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Observation Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Observation, String> {

        public String httpRequestType, observationResourceName, observationId;

        public OnLoadingInitialized(String observationId) {
            super(observationId);
        }

        public OnLoadingInitialized(Observation observation, String httpRequestType, String observationId) {
            super(observation, httpRequestType);
            this.httpRequestType = httpRequestType;
            this.observationId = observationId;
            this.observationResourceName = observation.getResourceType();
        }
    }

    /**
     * Event Handler when the Observation Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Observation> {
        public OnLoaded(Observation entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of Observation Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
