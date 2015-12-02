package com.sfsu.network.handler;

import com.sfsu.network.events.LocationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class LocationRequestHandler extends ApiRequestHandler {

    // private LocationApiClient mApiClient;
    private RetrofitApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public LocationRequestHandler(Bus bus) {
        super(bus);
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.EntityLocation} is Loaded. Receives the EntityLocation object
     * and make network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeLocationEvent(LocationEvent.OnLoadingInitialized onLoadingInitialized) {

    }
}
