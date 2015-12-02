package com.sfsu.network.handler;

import com.sfsu.entities.Observation;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.rest.apiclient.ObservationApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * <p>
 * RequestHandler to handle network requests for {@link Observation}. It Subscribes to the Observation events published by
 * all Observation related operations through out the application and makes successive network calls depending on the type of
 * request made such as get, add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p>
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
    }

    /**
     * Subscribes to the event when {@link Observation} is Loaded. Receives the Observation object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeObservationEvent(ObservationEvent.OnLoadingInitialized onLoadingInitialized) {
        Observation observation;
    }

}
