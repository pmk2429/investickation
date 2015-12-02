package com.sfsu.network.handler;

import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.rest.apiclient.ObservationApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class ObservationRequestHandler extends ApiRequestHandler {

    private ObservationApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ObservationRequestHandler(Bus bus) {
        super(bus);
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.Observation} is Loaded. Receives the Observation object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeObservationEvent(ObservationEvent.OnLoadingInitialized onLoadingInitialized) {
        
    }

}
