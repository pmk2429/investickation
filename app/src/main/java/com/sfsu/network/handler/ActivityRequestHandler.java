package com.sfsu.network.handler;

import com.sfsu.entities.Activities;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ActivityApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
 * <p>
 * Created by Pavitra on 11/28/2015.
 */
public class ActivityRequestHandler extends ApiRequestHandler {

    private ActivityApiService mApiService;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ActivityRequestHandler(Bus bus) {
       
        mApiService = RetrofitApiClient.createService(ActivityApiService.class);
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
                listActivitiesCall = mApiService.getAll();
                getAllActivitieCalls(listActivitiesCall);
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
        // makes the Calls to network.
        activitiesCall.enqueue(new Callback<Activities>() {
            @Override
            public void onResponse(Response<Activities> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Makes a network call for getting List using Retrofit Api interface and posts the response on event bus.
     *
     * @param listActivitiesCall
     */
    public void getAllActivitieCalls(Call<List<Activities>> listActivitiesCall) {
        // makes the Calls to network.
        listActivitiesCall.enqueue(new Callback<List<Activities>>() {
            @Override
            public void onResponse(Response<List<Activities>> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
