package com.sfsu.network.handler;

import com.sfsu.entities.Tick;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.TickApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * <p>
 * RequestHandler to handle network requests for {@link Tick}. It Subscribes to the Tick events published by the all Tick related
 * operations through out the application and makes successive network calls depending on the type of request made such as get,
 * add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p/>
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class TickRequestHandler extends ApiRequestHandler {

    private TickApiService mApiService;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public TickRequestHandler(Bus bus) {
        super(bus);
        mApiService = RetrofitApiClient.createService(TickApiService.class);
    }


    /**
     * Subscribes to the event when {@link Tick} is Loaded. Receives the Tick object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeTickEvent(TickEvent.OnLoadingInitialized onLoadingInitialized) {
        Tick tick;
    }
}
