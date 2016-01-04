package com.sfsu.network.events;

import java.util.List;

/**
 * The base events class that provides Generic definition of events performed/ carried out/ controller/ occurred in the entire
 * Application. This class provides the base type of Events which contains all the base characteristics exhibited by all types
 * of implementing events.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class BaseNetworkEvent {
    public static final String UNHANDLED_MSG = "Unexpected Error";
    public static final int UNHANDLED_CODE = -1;


    /**
     * Event to initialize the Network request call.
     *
     * @param <Rq>
     * @param <S>
     */
    protected static class OnStart<Rq, S> {
        private Rq mRequest;
        private S resourceId;

        public OnStart(S resourceId) {
            this.resourceId = resourceId;
        }

        public OnStart(Rq mRequest, S resourceId) {
            this.mRequest = mRequest;
            this.resourceId = resourceId;
        }

        public Rq getRequest() {
            return mRequest;
        }

        public S getResourceId() {
            return this.resourceId;
        }
    }

    /**
     * Event which binds the response from the server and posts to the EventBus.
     *
     * @param <Rs>
     */
    protected static class OnDone<Rs> {

        private Rs mResponse;
        private List<Rs> mResponseList;

        public OnDone(Rs response) {
            mResponse = response;
        }

        public OnDone(List<Rs> responseList) {
            mResponseList = responseList;
        }

        public Rs getResponse() {
            return mResponse;
        }

        public List<Rs> getResponseList() {
            return mResponseList;
        }
    }

    /**
     * Event which binds the response from the server and posts to the EventBus.
     *
     * @param <Rs>
     */
    protected static class OnDoneList<Rs> {

        private List<Rs> mResponseList;


        public OnDoneList(List<Rs> responseList) {
            mResponseList = responseList;
        }

        public List<Rs> getResponseList() {
            return mResponseList;
        }
    }

    /**
     * Displays Failed Event
     */
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
