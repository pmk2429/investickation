package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

import com.sfsu.entities.Tick;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.TickApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;


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

    private final String TAG = "~!@#$TickReqHdlr";
    private TickApiService mApiService;
    private ErrorResponse mErrorResponse;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     * @param investickationApp
     */
    public TickRequestHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(TickApiService.class, ACCESS_TOKEN);
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
            case GET_ALL_TICKS:
                Log.i(TAG, "get all ticks");
                getAllTicksUsingRx();
                break;
        }


    }

    /**
     * <p>
     * Makes CRUD type network call to server using Retrofit Api service and posts the response on the event bus.
     * </p>
     * <p>
     * When an event is posted for making a network call related to Tick Create, Read or Update then a call is delegated to
     * this method.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param tickCall
     */
    public void makeCRUDCall(Call<Tick> tickCall) {
        // makes the Calls to network.
        tickCall.enqueue(new Callback<Tick>() {
            @Override
            public void onResponse(Call<Tick> call, Response<Tick> response) {
                if (response.isSuccess()) {
                    mBus.post(new TickEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new TickEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(TickEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Tick> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new TickEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(TickEvent.FAILED);
                }
            }
        });
    }

    /**
     * <p>
     * Makes a network call for getting List using Retrofit Api interface and posts the response on event bus.
     * </p>
     * <p>
     * When an event is posted to fetch all the Ticks from the server, then a call is delegated to this method.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param listTickCall
     */
    public void getAllTicksCall(Call<List<Tick>> listTickCall) {
        // makes the Calls to network.
        listTickCall.enqueue(new Callback<List<Tick>>() {
            @Override
            public void onResponse(Call<List<Tick>> call, Response<List<Tick>> response) {
                if (response.isSuccess()) {
                    mBus.post(new TickEvent.OnListLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new TickEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(TickEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Tick>> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new TickEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(TickEvent.FAILED);
                }
            }
        });
    }

    /**
     * Get all Ticks from server using RxJava and posts it back to the Otto event bus
     */
    public void getAllTicksUsingRx() {
        Log.i(TAG, "get all ticks using Rx");
        final TickApiService tickApiService = RetrofitApiClient.createService(TickApiService.class, ACCESS_TOKEN);
        Observable<List<Tick>> listOfTicksObservable = tickApiService.getAllTicks();
        listOfTicksObservable
                .subscribe(new Subscriber<List<Tick>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, e.getMessage());
                        BusProvider.bus().post(new TickEvent.OnLoadingError(e.getMessage(), -1));
                    }

                    @Override
                    public void onNext(List<Tick> ticks) {
                        if (ticks != null)
                            Log.i(TAG, "onNext: " + ticks.size());
                        else
                            Log.i(TAG, "onNext: ticks null");
                        BusProvider.bus().post(new TickEvent.OnListLoaded(ticks));
                    }
                });
    }
}
