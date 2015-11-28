package com.sfsu.network.handler;

import com.sfsu.network.rest.ApiClient;
import com.squareup.otto.Bus;

/**
 * Created by Pavitra on 11/27/2015.
 */
public class ApiRequestHandler {
    private Bus mBus;
    private ApiClient mApiClient;

    public ApiRequestHandler(Bus bus) {
        mBus = bus;
        mApiClient = ApiClient.getInstance();
    }

}
