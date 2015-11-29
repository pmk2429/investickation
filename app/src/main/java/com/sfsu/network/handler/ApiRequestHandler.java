package com.sfsu.network.handler;

import com.squareup.otto.Bus;

/**
 * Provides a handler to initialize Bus and ApiClient instances.
 * <p/>
 * In addition, this class contains the methods to handle the Retrofit network calls for each of Entity present in the
 * InvesTICKations project.
 * <p/>
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

}
