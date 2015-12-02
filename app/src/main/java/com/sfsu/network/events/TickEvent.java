package com.sfsu.network.events;

import com.sfsu.entities.Tick;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class TickEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Tick Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Tick, String> {

        public String apiRequestMethod;

        public OnLoadingInitialized(String tickId, String apiRequestMethod) {
            super(tickId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(Tick tick, String tickId, String apiRequestMethod) {
            super(tick, tickId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(Tick tick, String apiRequestMethod) {
            super(tick, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public String getApiRequestMethod() {
            return apiRequestMethod;
        }
    }

    /**
     * Event Handler when the Tick Events are successfully executed and Response is generated.
     */
    public static class OnLoaded extends OnDone<Tick> {
        public OnLoaded(Tick entity) {
            super(entity);
        }
    }

    /**
     * Event Handler When loading of Tick Events throws errors.
     */
    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
