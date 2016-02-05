package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.response.ResponseCount;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.events.LocationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ActivityApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    private List<Activities> massUploadResponseList;
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
        Call<ResponseCount> deleteActivityCall = null;
        Call<List<Activities>> listActivitiesCall = null;
        Call<ResponseCount> countCall = null;
        final String recentActivitiesFilter = "{\"order\": \"timestamp DESC\", \"limit\": 2}";
        final String orderFilter = "{\"order\": \"timestamp DESC\"}";
        final String countWhereClause = "{\"user_id\":\"" + USER_ID + "\"}";

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                activitiesCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUCall(activitiesCall);
                break;
            case GET_ALL:
                listActivitiesCall = mApiService.getAll(USER_ID, orderFilter);
                getAllActivitiesCalls(listActivitiesCall);
                break;
            case ADD:
                activitiesCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUCall(activitiesCall);
                break;
            case UPDATE:
                activitiesCall = mApiService.update(onLoadingInitialized.getResourceId(), onLoadingInitialized.getRequest());
                makeCRUCall(activitiesCall);
                break;
            case DELETE:
                deleteActivityCall = mApiService.delete(onLoadingInitialized.getResourceId());
                makeDeleteCall(deleteActivityCall);
                break;
            case TOTAL_LOCATIONS_COUNT:
                countCall = mApiService.totalLocations(onLoadingInitialized.getResourceId());
                getCount(countCall);
                break;
            case GET_RECENT_ACTIVITIES:
                listActivitiesCall = mApiService.getRecentActivities(USER_ID, recentActivitiesFilter);
                getAllActivitiesCalls(listActivitiesCall);
                break;
            case TOTAL_ACTIVITIES_COUNT:
                Log.i(TAG, "act - yeah");
                countCall = mApiService.count(countWhereClause);
                getCount(countCall);
        }

    }

    /**
     * Subscribes to the event of initializing list of Activities to store on server
     *
     * @param onListLoadingInitialized
     */
    @Subscribe
    public void onInitializeListActivityEvent(ActivityEvent.OnListLoadingInitialized onListLoadingInitialized) {
        List<Activities> activitiesList = onListLoadingInitialized.getRequest();
        massUploadResponseList = new ArrayList<>();

        Call<Activities> activitiesCall = null;
        // for each Activities in the list, make a call and push data on the server
        for (int i = 0; i < activitiesList.size(); i++) {
            Log.i(TAG, "inside forloop");
            activitiesCall = mApiService.add(onListLoadingInitialized.getRequest().get(i));
            Log.i(TAG, "making activitiesCall");
            // makes the Calls to network.
            activitiesCall.enqueue(new Callback<Activities>() {
                @Override
                public void onResponse(Call<Activities> call, Response<Activities> response) {
                    if (response.isSuccess()) {
                        massUploadResponseList.add(response.body());
                    } else {
                        int statusCode = response.code();
                        ResponseBody errorBody = response.errorBody();
                        try {
                            mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                            Log.i(TAG, mErrorResponse.getApiError().getMessage());
                            mBus.post(new ActivityEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                        } catch (IOException e) {
                            mBus.post(ActivityEvent.FAILED);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Activities> call, Throwable t) {
                    if (t != null && t.getMessage() != null) {
                        mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                    } else {
                        mBus.post(ActivityEvent.FAILED);
                    }
                }
            });
        }
        Log.i(TAG, "reached-3");
        if (massUploadResponseList != null) {
            Log.i(TAG, "responseList not empty");
            // finally post response list
            mBus.post(new ActivityEvent.OnMassUploadListLoaded(massUploadResponseList));

        }
    }


    /**
     * Makes CREATE, READ, UPDATE type network call to server using Retrofit Api service and posts the response on the event
     * bus.
     *
     * @param activitiesCall
     */
    public void makeCRUCall(Call<Activities> activitiesCall) {
        // makes the Calls to network.
        activitiesCall.enqueue(new Callback<Activities>() {
            @Override
            public void onResponse(Call<Activities> call, Response<Activities> response) {
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
            public void onFailure(Call<Activities> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ActivityEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ActivityEvent.FAILED);
                }
            }
        });
    }

    /**
     * Makes Delete type network call to server using Retrofit Api service and posts the response on the event bus.
     *
     * @param activitiesCall
     */
    public void makeDeleteCall(Call<ResponseCount> deleteActivitiesCall) {
        // makes the Calls to network.
        deleteActivitiesCall.enqueue(new Callback<ResponseCount>() {
            @Override
            public void onResponse(Call<ResponseCount> call, Response<ResponseCount> response) {
                if (response.isSuccess()) {
                    mBus.post(new ActivityEvent.OnLoadedCount(response.body()));
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
            public void onFailure(Call<ResponseCount> call, Throwable t) {
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
            public void onResponse(Call<List<Activities>> call, Response<List<Activities>> response) {
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
            public void onFailure(Call<List<Activities>> call, Throwable t) {
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
    public void getCount(Call<ResponseCount> countCall) {
        Log.i(TAG, "act - ok");
        countCall.enqueue(new Callback<ResponseCount>() {
            @Override
            public void onResponse(Call<ResponseCount> call, Response<ResponseCount> response) {
                if (response.isSuccess()) {
                    mBus.post(new ActivityEvent.OnLoadedCount(response.body()));
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
            public void onFailure(Call<ResponseCount> call, Throwable t) {
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
            public void onResponse(Call<List<EntityLocation>> call, Response<List<EntityLocation>> response) {
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
            public void onFailure(Call<List<EntityLocation>> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new LocationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(LocationEvent.FAILED);
                }
            }
        });
    }

}
