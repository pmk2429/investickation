package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.events.LocationEvent;
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
import retrofit.Retrofit;

/**
 * <p>
 * RequestHandler to handle network requests for {@link Activities}. It Subscribes to the Activity events published by the all
 * Activity related operations through out the application and makes successive network calls depending on the type of request
 * made such as get, add, delete etc.
 * </p>
 * The successive request call receives the JSON response from the API via a {@link retrofit.Call} and then adds
 * the Response to the {@link Bus}.
 * <p>
 * Each handler delegates a network call and depending on the status code of response, raises an error/success flag.
 * </p>
 * Created by Pavitra on 11/28/2015.
 */
public class ActivityRequestHandler extends ApiRequestHandler {

    private ActivityApiService mApiService;
    private String TAG = "~!@#$ActReqHdlr";
    private ErrorResponse mErrorResponse;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ActivityRequestHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(ActivityApiService.class, ACCESS_TOKEN);
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
        Call<List<Observation>> observationCall = null;
        Call<List<EntityLocation>> locationCall = null;
        Call<List<Activities>> listActivitiesCall = null;
        Call<Integer> countCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                activitiesCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(activitiesCall);
                break;
            case GET_ALL:
                listActivitiesCall = mApiService.getAll(USER_ID);
                getAllActivitiesCalls(listActivitiesCall);
                break;
            case ADD:
                activitiesCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(activitiesCall);
                break;
            case DELETE:
                activitiesCall = mApiService.delete(onLoadingInitialized.getResourceId());
                makeCRUDCall(activitiesCall);
                break;
            case TOTAL_LOCATIONS_COUNT:
                countCall = mApiService.totalLocations(onLoadingInitialized.getResourceId());
                getCount(countCall);
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
                if (response.isSuccess()) {
                    mBus.post(new ActivityEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ActivityEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
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
        listActivitiesCall.enqueue(new Callback<List<Activities>>() {
            @Override
            public void onResponse(Response<List<Activities>> response) {
                if (response.isSuccess()) {
                    mBus.post(new ActivityEvent.OnListLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ActivityEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ActivityEvent.FAILED);
                }
            }
        });
    }

    /**
     * Calculates the count of {@link Activities}
     *
     * @param countCall
     */
    public void getCount(Call<Integer> countCall) {
        countCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response) {
                if (response.isSuccess()) {
                    mBus.post(new ActivityEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ActivityEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ActivityEvent.FAILED);
                }
            }
        });
    }

    /**
     * Returns a list of Locations for specific Activities.
     *
     * @param locationsCall
     */
    public void getAllLocations(Call<List<EntityLocation>> locationsCall) {
        locationsCall.enqueue(new Callback<List<EntityLocation>>() {
            @Override
            public void onResponse(Response<List<EntityLocation>> response) {
                if (response.isSuccess()) {
                    mBus.post(new LocationEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new LocationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(LocationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new LocationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(LocationEvent.FAILED);
                }
            }
        });
    }

}
