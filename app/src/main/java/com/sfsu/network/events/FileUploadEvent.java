package com.sfsu.network.events;

import com.sfsu.entities.ImageData;
import com.sfsu.entities.Observation;

/**
 * Event handler for uploading Tick related Observation Images to the server.
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class FileUploadEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the FileUpload Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<ImageData, String> {

        public int apiRequestMethod;
        public String observationId;

        /**
         * Constructor overloading for HTTP POST calls.
         *
         * @param observation
         * @param apiRequestMethod
         */
        public OnLoadingInitialized(ImageData mImageData, String observationId, int apiRequestMethod) {
            super(mImageData, "");
            this.apiRequestMethod = apiRequestMethod;
            this.observationId = observationId;
        }
    }

    /**
     * Event Handler when the FileUpload Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Observation> {
        public OnLoaded(Observation entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of FileUpload Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
