package com.sfsu.network.events;

import com.sfsu.entities.Tick;

import java.util.List;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class TickEvent extends BaseNetworkEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    /**
     * Event Handler When the Loading for the Tick Events is initialized and Request is initiated.
     */
    public static class OnLoadingInitialized extends OnStart<Tick, String> {

        public int apiRequestMethod;

        public OnLoadingInitialized(String tickId, int apiRequestMethod) {
            super(tickId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(Tick tick, String tickId, int apiRequestMethod) {
            super(tick, tickId);
            this.apiRequestMethod = apiRequestMethod;
        }

        public OnLoadingInitialized(Tick tick, int apiRequestMethod) {
            super(tick, "");
            this.apiRequestMethod = apiRequestMethod;
        }

        public int getApiRequestMethod() {
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

        public OnLoaded(List<Tick> tickList) {
            super(tickList);
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
