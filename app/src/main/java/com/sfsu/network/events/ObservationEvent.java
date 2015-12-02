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

        public String apiRequestMethod;

        /**
         * Constructor overloading for HTTP UPDATE calls.
         *
         * @param observation
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Observation observation, String observationId, String apiRequestMethod) {
            super(observation, observationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overlaoding for HTTP GET, DELETE calls.
         *
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String observationId, String apiRequestMethod) {
            super(observationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overloading for HTTP POST calls.
         *
         * @param observation
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Observation observation, String apiRequestMethod) {
            super(observation, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public String getApiRequestMethod() {
            return apiRequestMethod;
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
