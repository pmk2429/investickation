package com.sfsu.network.handler;

import android.util.Log;

import com.sfsu.entities.Tick;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.TickApiService;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
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
 * <p>
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class TickRequestHandler extends ApiRequestHandler {

    private final String LOGTAG = "~!@#$TickReqHdlr: ";
    private TickApiService mApiService;
    private Bus mBus;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public TickRequestHandler(Bus bus) {
        mApiService = RetrofitApiClient.createService(TickApiService.class, "PhcKAKLu0pdGHhsCVVWGz0mmF4FDylvuhgK80YvzhxHiziaznmc7uSL9zSCcUHFU");
        this.mBus = bus;
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
            case GET:
                tickCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(tickCall);
                break;
            case GET_ALL:
                listTickCall = mApiService.getAll();
                getAllTicksCall(listTickCall);
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
                Log.i(LOGTAG, "inside onResponse");
                if (response.isSuccess()) {
                    Log.i(LOGTAG, "Response Success");
                    mBus.post(new TickEvent.OnLoaded(response.body()));
                } else {
                    Log.i(LOGTAG, "Response Failure");
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new TickEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(TickEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(LOGTAG, "inside onFailure");
                if (t != null && t.getMessage() != null) {
                    mBus.post(new TickEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(TickEvent.FAILED);
                }
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
                Log.i(LOGTAG, "inside onResponse");
                if (response.isSuccess()) {
                    Log.i(LOGTAG, "Response Success");
                    mBus.post(new TickEvent.OnLoaded(response.body()));
                } else {
                    Log.i(LOGTAG, "Response Failure");
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new TickEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(TickEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(LOGTAG, "inside onFailure");
                if (t != null && t.getMessage() != null) {
                    mBus.post(new TickEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(TickEvent.FAILED);
                }
            }
        });
    }
}
