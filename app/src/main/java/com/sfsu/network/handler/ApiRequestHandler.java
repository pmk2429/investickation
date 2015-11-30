package com.sfsu.network.handler;

import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.events.LocationEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.events.UserEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Provides a handler to initialize Bus and ApiClient instances.
 * <p>
 * In addition, this class contains the methods to handle the Retrofit network calls for each of Entity present in the
 * InvesTICKations project.
 * </p>
 * Created by Pavitra on 11/27/2015.
 */
public class ApiRequestHandler {
    private Bus mBus;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ApiRequestHandler(Bus bus) {
        mBus = bus;
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.User} is Loaded. Receives the User object and make network calls to
     * Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserEvent(UserEvent.OnLoadingInitialized onLoadingInitialized) {

    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.Activities} is Loaded. Receives the Activity object and make network
     * calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeActivityEvent(ActivityEvent.OnLoadingInitialized onLoadingInitialized) {

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

    /**
     * Subscribes to the event when {@link com.sfsu.entities.Tick} is Loaded. Receives the Tick object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeTickEvent(TickEvent.OnLoadingInitialized onLoadingInitialized) {

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
