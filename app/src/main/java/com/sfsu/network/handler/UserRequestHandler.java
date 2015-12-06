package com.sfsu.network.handler;

import com.sfsu.entities.User;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.UserApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * <p>
 * RequestHandler to handle network requests for {@link User}. It Subscribes to the User events published by the all User related
 * operations through out the application and makes successive network calls depending on the type of request made such as get,
 * add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class UserRequestHandler extends ApiRequestHandler {

    private UserApiService mApiService;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public UserRequestHandler(Bus bus) {
        super(bus);
        mApiService = RetrofitApiClient.createService(UserApiService.class);
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.User} is Loaded. Receives the User object and make network calls to
     * Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserEvent(UserEvent.OnLoadingInitialized onLoadingInitialized) {

        UserApiService apiService = RetrofitApiClient.createService(UserApiService.class);

        switch (onLoadingInitialized.apiRequestMethod) {
            case GET_METHOD:
                mApiService.add(onLoadingInitialized.getRequest());

        }


    }
}
