package com.sfsu.network.handler;

import com.sfsu.network.rest.apiclient.ObservationApiClient;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;

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
        mApiClient = RetrofitApiClient.getApi(RetrofitApiClient.ApiTypes.OBSERVATION_API);
    }
}
