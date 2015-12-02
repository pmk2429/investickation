package com.sfsu.network.handler;

import com.sfsu.network.events.TickEvent;
import com.sfsu.network.rest.apiclient.TickApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class TickRequestHandler extends ApiRequestHandler {

    private TickApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public TickRequestHandler(Bus bus) {
        super(bus);
    }


    /**
     * Subscribes to the event when {@link com.sfsu.entities.Tick} is Loaded. Receives the Tick object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeTickEvent(TickEvent.OnLoadingInitialized onLoadingInitialized) {

    }
}
