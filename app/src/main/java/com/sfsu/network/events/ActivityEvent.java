package com.sfsu.network.events;

import com.sfsu.entities.Activities;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class ActivityEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Activities Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Activities, String> {

        public String apiRequestMethod;

        /**
         * Constructor overloading for HTTP GET, DELETE calls.
         *
         * @param activityId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String activityId, String apiRequestMethod) {
            super(activityId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overloading for UPDATE calls.
         *
         * @param activity
         * @param activityId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Activities activity, String activityId, String apiRequestMethod) {
            super(activity, activityId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overloading for POST calls
         *
         * @param activity
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Activities activity, String apiRequestMethod) {
            super(activity, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public String getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Activities Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Activities> {
        public OnLoaded(Activities entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of Activities Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
