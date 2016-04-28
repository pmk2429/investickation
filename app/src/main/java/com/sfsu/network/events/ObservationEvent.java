package com.sfsu.network.events;

import com.sfsu.entities.Observation;
import com.sfsu.entities.response.ObservationResponse;
import com.sfsu.entities.response.ResponseCount;

import java.util.List;

/**
 * Handles all the {@link Observation} type of events such as loading list, creating new Observation, Deleting observation etc.
 * The ObservationEvent is used to post on EventBus regarding any events related to Observation
 * <p>
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
         * Constructor overloading for HTTP GET, DELETE calls.
         *
         * @param observationId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String observationId, int apiRequestMethod) {
            super(observationId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overloading for HTTP GET, DELETE calls.
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
     * Event Handler When the Loading for the Activities Events is initialized and Request is initiated.
     */
    public static class OnListLoadingInitialized extends OnStart<List<Observation>, String> {

        public int apiRequestMethod;

        public OnListLoadingInitialized(List<Observation> observationList, int apiRequestMethod) {
            super(observationList, "");
            this.apiRequestMethod = apiRequestMethod;
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
     * Event handler when the response from server is Integer
     */
    public static class OnLoadedCount extends OnDone<ResponseCount> {
        public OnLoadedCount(ResponseCount count) {
            super(count);
        }
    }

    /**
     * Event Handler for loading List of Activities all together from the Server.
     * <b>Has to be separate to properly unregister the event from ActivityListFragment</b>
     */
    public static class OnListLoaded extends OnDone<Observation> {
        public OnListLoaded(List<Observation> observationList) {
            super(observationList);
        }
    }

    public static class OnMassUploadListLoaded extends OnDone<Observation> {
        public OnMassUploadListLoaded(List<Observation> observationList) {
            super(observationList);
        }
    }

    /**
     * OnDone event for dispatching {@link ObservationResponse} type object
     */
    public static class OnObservationWrapperLoaded extends OnDone<ObservationResponse> {

        public OnObservationWrapperLoaded(ObservationResponse response) {
            super(response);
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
