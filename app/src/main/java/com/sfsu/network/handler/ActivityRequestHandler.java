package com.sfsu.network.handler;

import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ActivityApiService;
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
 * RequestHandler to handle network requests for {@link Activities}. It Subscribes to the Activity events published by the all
 * Activity related operations through out the application and makes successive network calls depending on the type of request
 * made such as get, add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class ActivityRequestHandler extends ApiRequestHandler {

    private ActivityApiService mApiService;
    private String LOGTAG = "~!@#$ActReqHdlr: ";
    private Bus mBus;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ActivityRequestHandler(Bus bus) {
        this.mBus = bus;
        mApiService = RetrofitApiClient.createService(ActivityApiService.class, "vEr7lrb9Q56egAc9LRgI11cY3VQ5lXbmCinRTZCdLS21XkAZhYnpqWFY1m4nuemI");
    }

    /**
     * Subscribes to the event when {@link Activities} is Loaded. Receives the Activity object and make network
     * calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeActivityEvent(ActivityEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Activities> activitiesCall = null;
        Call<List<Activities>> listActivitiesCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET_METHOD:
                activitiesCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(activitiesCall);
                break;
            case GET_ALL_METHOD:
                Log.i(LOGTAG, "get all called");
                listActivitiesCall = mApiService.getAll();
                getAllActivitiesCalls(listActivitiesCall);
                break;
            case ADD_METHOD:
                activitiesCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(activitiesCall);
                break;
            case DELETE_METHOD:
                activitiesCall = mApiService.delete(onLoadingInitialized.getResourceId());
                makeCRUDCall(activitiesCall);
                break;
        }


    }

    /**
     * Makes CRUD type network call to server using Retrofit Api service and posts the response on the event bus.
     *
     * @param activitiesCall
     */
    public void makeCRUDCall(Call<Activities> activitiesCall) {
        Log.i(LOGTAG, "making CRUD call");
        // makes the Calls to network.
        activitiesCall.enqueue(new Callback<Activities>() {
            @Override
            public void onResponse(Response<Activities> response) {
                Log.i(LOGTAG, "inside onResponse");
                if (response.isSuccess()) {
                    Log.i(LOGTAG, "Response Success");
                    mBus.post(new ActivityEvent.OnLoaded(response.body()));
                } else {
                    Log.i(LOGTAG, "Response Failure");
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new ActivityEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(LOGTAG, "inside onFailure");
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ActivityEvent.FAILED);
                }
            }
        });
    }

    /**
     * Makes a network call for getting List using Retrofit Api interface and posts the response on event bus.
     *
     * @param listActivitiesCall
     */
    public void getAllActivitiesCalls(Call<List<Activities>> listActivitiesCall) {
        Log.i(LOGTAG, "yup. making get all Activities call");
        // makes the Calls to network.
        listActivitiesCall.enqueue(new Callback<List<Activities>>() {
            @Override
            public void onResponse(Response<List<Activities>> response) {
                Log.i(LOGTAG, "inside onResponse");
                if (response.isSuccess()) {
                    Log.i(LOGTAG, "Response Success");
                    mBus.post(new ActivityEvent.OnLoaded(response.body()));
                } else {
                    Log.i(LOGTAG, "Response Failure");
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mBus.post(new ActivityEvent.OnLoadingError(errorBody.string(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(LOGTAG, "inside onFailure");
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ActivityEvent.FAILED);
                }
            }
        });
    }

}
