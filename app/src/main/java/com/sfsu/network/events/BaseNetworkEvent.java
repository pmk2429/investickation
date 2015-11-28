package com.sfsu.network.events;

/**
 * The base events class that provides Generic definition of events performed/ carried out/ controller/ occurred in the entire
 * Application. This class provides the base type of Events which contains all the base characteristics exhibited by all types
 * of implementing events.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class BaseNetworkEvent {
    public static final String UNHANDLED_MSG = "UNHANDLED_MSG";
    public static final int UNHANDLED_CODE = -1;

    protected static class OnStart<Rq, S> {
        private Rq mRequest;
        private S resourceType;

        public OnStart(S resourceType) {
            this.resourceType = resourceType;
        }

        public OnStart(Rq mRequest, S resource) {
            this.mRequest = mRequest;
            this.resourceType = resource;
        }

        public Rq getRequest() {
            return mRequest;
        }

        public S getResourceType() {
            return this.resourceType;
        }
    }

    protected static class OnDone<Rs> {

        private Rs mResponse;

        public OnDone(Rs response) {
            mResponse = response;
        }

        public Rs getResponse() {
            return mResponse;
        }

    }

    protected static class OnFailed {

        private String mErrorMessage;
        private int mCode;

        public OnFailed(String errorMessage, int code) {
            mErrorMessage = errorMessage;
            mCode = code;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }

        public int getCode() {
            return mCode;
        }

    }
}
