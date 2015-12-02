package com.sfsu.network.handler;

import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.rest.apiclient.ActivityApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class ActivityRequestHandler extends ApiRequestHandler {

    private ActivityApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ActivityRequestHandler(Bus bus) {
        super(bus);

    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.Activities} is Loaded. Receives the Activity object and make network
     * calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeActivityEvent(ActivityEvent.OnLoadingInitialized onLoadingInitialized) {

        switch (onLoadingInitialized.httpRequestType) {

        }
    }

}
