package com.sfsu.network.events;

import com.sfsu.entities.Activities;

import java.util.List;

/**
 * Event publisher for {@link Activities}.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class ActivityEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Activities Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Activities, String> {

        public int apiRequestMethod;

        /**
         * Constructor overloading for HTTP GET, DELETE calls.
         *
         * @param activityId
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(String activityId, int apiRequestMethod) {
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
        public OnLoadingInitialized(Activities activity, String activityId, int apiRequestMethod) {
            super(activity, activityId);
            this.apiRequestMethod = apiRequestMethod;
        }

        /**
         * Constructor overloading for POST calls
         *
         * @param activity
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(Activities activity, int apiRequestMethod) {
            super(activity, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public int getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Activities Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Activities> {

        public Integer count;

        public OnLoaded(Activities entity) {
            super(entity);
        }

        public OnLoaded(Integer count) {
            super(new Activities());
            this.count = count;
        }
    }

    /**
     * Event Handler for loading List of Activities all together from the Server.
     * <b>Has to be separate to properly unregister the event from ActivityList</b>
     */
    public static class OnListLoaded extends OnDone<Activities> {
        public OnListLoaded(List<Activities> activitiesList) {
            super(activitiesList);
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
