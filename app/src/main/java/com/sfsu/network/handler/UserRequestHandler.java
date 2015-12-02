package com.sfsu.network.handler;

import com.sfsu.entities.User;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.rest.apiclient.UserApiClient;
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
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class UserRequestHandler extends ApiRequestHandler {

    private UserApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public UserRequestHandler(Bus bus) {
        super(bus);
    }

    /**
     * Subscribes to the event when {@link com.sfsu.entities.User} is Loaded. Receives the User object and make network calls to
     * Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeUserEvent(UserEvent.OnLoadingInitialized onLoadingInitialized) {
        User user = new User();

        BusProvider.bus().post(new UserEvent.OnLoadingInitialized(user, ADD_METHOD));
        UserApiClient apiClient = new UserApiClient();

        switch (onLoadingInitialized.apiRequestMethod) {
            case GET_METHOD:
                apiClient.getService().add(onLoadingInitialized.getRequest());

        }


    }
}
