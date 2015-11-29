package com.sfsu.network.handler;

import com.sfsu.network.rest.apiclient.LocationApiClient;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class LocationRequestHandler extends ApiRequestHandler {

    private LocationApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public LocationRequestHandler(Bus bus) {
        super(bus);
        mApiClient = RetrofitApiClient.getApi(RetrofitApiClient.ApiTypes.LOCATION_API);
    }
}
