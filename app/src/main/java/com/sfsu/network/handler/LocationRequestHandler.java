package com.sfsu.network.handler;

import com.sfsu.entities.EntityLocation;
import com.sfsu.network.events.LocationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * <p>
 * RequestHandler to handle network requests for {@link EntityLocation}. It Subscribes to EntityLocation events published
 * by the all EntityLocation related operations through out the application and makes successive network calls depending on the
 * type of request made such as get, add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class LocationRequestHandler extends ApiRequestHandler {

    // private LocationApiClient mApiClient;
    private RetrofitApiClient mApiClient;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public LocationRequestHandler(Bus bus) {
        super(bus);
    }

    /**
     * Subscribes to the event when {@link EntityLocation} is Loaded. Receives the EntityLocation object
     * and make network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeLocationEvent(LocationEvent.OnLoadingInitialized onLoadingInitialized) {
        EntityLocation entityLocation;
    }
}
