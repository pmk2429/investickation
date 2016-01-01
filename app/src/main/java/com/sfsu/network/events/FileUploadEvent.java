package com.sfsu.network.events;

import com.sfsu.entities.Observation;
import com.squareup.okhttp.RequestBody;

import java.util.List;

/**
 * Event handler for uploading Tick related Observation Images to the server.
 * <p>
 * Created by Pavitra on 11/27/2015.
 */
public class FileUploadEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Observation Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<RequestBody, String> {

        public int apiRequestMethod;

        /**
         * Constructor overloading for HTTP POST calls.
         *
         * @param observation
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(RequestBody tickRequestBody, int apiRequestMethod) {
            super(tickRequestBody, "");
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
