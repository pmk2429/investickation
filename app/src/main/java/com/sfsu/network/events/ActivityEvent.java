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

        public String httpRequestType, activitiesResourceName, activitiesId;

        public OnLoadingInitialized(String ActivitiesId) {
            super(ActivitiesId);
        }

        public OnLoadingInitialized(Activities activity, String httpRequestType, String activitiesId) {
            super(activity, httpRequestType);
            this.httpRequestType = httpRequestType;
            this.activitiesId = activitiesId;
            this.activitiesResourceName = activity.getResourceType();
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
