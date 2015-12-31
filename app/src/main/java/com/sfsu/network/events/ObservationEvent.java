package com.sfsu.network.events;

import com.sfsu.entities.Observation;

import java.util.List;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class ObservationEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Observation Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Observation, String> {

        public int apiRequestMethod;
        public String activityId;

        /**
         * Constructor overloading for HTTP UPDATE calls.
         *
         * @param observation
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Observation observation, String observationId, int apiRequestMethod) {
            super(observation, observationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overlaoding for HTTP GET, DELETE calls.
         *
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String observationId, int apiRequestMethod) {
            super(observationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overlaoding for HTTP GET, DELETE calls.
         *
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String observationId, String activityId, int apiRequestMethod) {
            super(observationId);
            this.apiRequestMethod = apiRequestMethod;
            this.activityId = activityId;
        }


        /**
         * Constructor overloading for HTTP POST calls.
         *
         * @param observation
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Observation observation, int apiRequestMethod) {
            super(observation, "");
            this.apiRequestMethod = apiRequestMethod;
        }


        public int getApiRequestMethod() {
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

        public OnLoaded(List<Observation> observationList) {
            super(observationList);
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
