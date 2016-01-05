package com.sfsu.network.handler;

import android.content.Context;

import com.sfsu.entities.EntityLocation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.LocationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.LocationApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * <p>
 * RequestHandler to handle network requests for {@link EntityLocation}. It Subscribes to EntityLocation events published
 * by the all EntityLocation related operations through out the application and makes successive network calls depending on the
 * type of request made such as get, add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p/>
 * Created by Pavitra on 11/28/2015.
 */
public class LocationRequestHandler extends ApiRequestHandler {

    private LocationApiService mApiService;
    private ErrorResponse mErrorResponse;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public LocationRequestHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(LocationApiService.class, ACCESS_TOKEN);
    }

    /**
     * Subscribes to the event when {@link EntityLocation} is Loaded. Receives the EntityLocation object
     * and make network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeLocationEvent(LocationEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<EntityLocation> locationCall = null;
        Call<List<EntityLocation>> listLocationCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                locationCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(locationCall);
                break;
            case GET_ALL:
                listLocationCall = mApiService.getAll();
                getAllLocationsCall(listLocationCall);
                break;
            case ADD:
                locationCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(locationCall);
                break;
            case DELETE:
                locationCall = mApiService.delete(onLoadingInitialized.getResourceId());
                makeCRUDCall(locationCall);
                break;
        }


    }

    /**
     * Makes CRUD type network call to server using Retrofit Api service and posts the response on the event bus.
     *
     * @param locationCall
     */
    public void makeCRUDCall(Call<EntityLocation> locationCall) {
        // makes the Calls to network.
        locationCall.enqueue(new Callback<EntityLocation>() {
            @Override
            public void onResponse(Response<EntityLocation> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Makes a network call for getting List using Retrofit Api interface and posts the response on event bus.
     *
     * @param listLocationCall
     */
    public void getAllLocationsCall(Call<List<EntityLocation>> listLocationCall) {
        // makes the Calls to network.
        listLocationCall.enqueue(new Callback<List<EntityLocation>>() {
            @Override
            public void onResponse(Response<List<EntityLocation>> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
