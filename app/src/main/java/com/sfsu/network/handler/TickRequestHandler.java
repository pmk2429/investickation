package com.sfsu.network.handler;

import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.apiclient.TickApiClient;
import com.squareup.otto.Bus;

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
        mApiClient = RetrofitApiClient.getApi(RetrofitApiClient.ApiTypes.TICK_API);
    }
}
