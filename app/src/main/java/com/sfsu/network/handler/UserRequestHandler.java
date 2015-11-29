package com.sfsu.network.handler;

import com.sfsu.network.rest.apiclient.ApiClient;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;

/**
 * Created by Pavitra on 11/28/2015.
 */
public class UserRequestHandler extends ApiRequestHandler {

    private ApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public UserRequestHandler(Bus bus) {
        super(bus);
        mApiClient = ApiClient.getInstance(RetrofitApiClient.ApiTypes.USER_API);
    }
}
