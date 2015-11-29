package com.sfsu.network.handler;

import com.sfsu.network.rest.apiclient.ActivityApiClient;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;

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
        mApiClient = RetrofitApiClient.getApi(RetrofitApiClient.ApiTypes.ACTIVITY_API);
    }
}
