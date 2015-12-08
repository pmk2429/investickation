package com.sfsu.network.handler;

import com.sfsu.entities.Tick;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.TickApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

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
        Call<Tick> tickCall = null;
        Call<List<Tick>> listTickCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET_METHOD:
                tickCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(tickCall);
                break;
            case GET_ALL_METHOD:
                listTickCall = mApiService.getAll();
                getAllTicksCall(listTickCall);
                break;
            case ADD_METHOD:
                tickCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(tickCall);
                break;
            case DELETE_METHOD:
                tickCall = mApiService.delete(onLoadingInitialized.getResourceId());
                makeCRUDCall(tickCall);
                break;
        }


    }

    /**
     * Makes CRUD type network call to server using Retrofit Api service and posts the response on the event bus.
     *
     * @param tickCall
     */
    public void makeCRUDCall(Call<Tick> tickCall) {
        // makes the Calls to network.
        tickCall.enqueue(new Callback<Tick>() {
            @Override
            public void onResponse(Response<Tick> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Makes a network call for getting List using Retrofit Api interface and posts the response on event bus.
     *
     * @param listTickCall
     */
    public void getAllTicksCall(Call<List<Tick>> listTickCall) {
        // makes the Calls to network.
        listTickCall.enqueue(new Callback<List<Tick>>() {
            @Override
            public void onResponse(Response<List<Tick>> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
