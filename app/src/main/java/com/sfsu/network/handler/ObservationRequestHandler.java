package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

import com.sfsu.entities.Observation;
import com.sfsu.entities.response.ObservationResponse;
import com.sfsu.entities.response.ResponseCount;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ObservationApiService;
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

    private final String TAG = "~!@#$ObsReqHdlr";
    private ObservationApiService mApiService;
    private ErrorResponse mErrorResponse;
    private List<Observation> massUploadResponseList;

    /**
     * Constructor overloading to initialize the Bus to be used for this Request Handling.
     *
     * @param bus
     */
    public ObservationRequestHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(ObservationApiService.class, ACCESS_TOKEN);
    }

    /**
     * <p>
     * Subscribes to the event when {@link Observation} is Loaded. Receives the Observation object and make
     * network calls to Retrofit depending on the type of request made.
     * </p>
     * <p>
     * This method is called when an event is posted from Observation based fragments for CRUD type of Observation calls. These
     * events are posted from {@link com.sfsu.investickation.fragments.AddObservationFragment}, {@link com.sfsu.investickation.fragments.ObservationListFragment}, {@link com.sfsu.investickation.fragments.ObservationDetailFragment} etc. Depending on each event,
     * the call is delegated to the corresponding method.
     * </p>
     * <p>
     * This method helps to avoid writing complex logic in Fragments and separate Business logic from the network calls.
     * </p>
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeObservationEvent(ObservationEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Observation> observationCall = null;
        Call<ResponseCount> deleteObservation = null;
        Call<ResponseCount> countCall = null;
        Call<List<Observation>> listObservationCall = null;
        Call<List<ObservationResponse>> listObservationResponseCall = null;
        Call<ObservationResponse> observationResponseCall = null;

        final String observationWrapperFilter = "{\"include\":[\"activity\", \"tick\"]}";
        final String countWhereClause = "{\"user_id\":\"" + USER_ID + "\"}";
        final String locationIncludeFilter = "{\"include\":\"location\"}";
        final String orderFilter = "{\"order\": \"timestamp DESC\"}";

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                observationCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUCall(observationCall);
                break;
            case GET_ALL:
                listObservationCall = mApiService.getAll(USER_ID, orderFilter);
                getAllObservationsCall(listObservationCall);
                break;
            case ADD:
                observationCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUCall(observationCall);
                break;
            case DELETE:
                deleteObservation = mApiService.delete(onLoadingInitialized.getResourceId());
                makeDeleteCall(deleteObservation);
                break;
            case ACT_OBSERVATIONS:
                listObservationCall = mApiService.observationsOfActivity(onLoadingInitialized.activityId, locationIncludeFilter);
                get_Activity_Observations(listObservationCall);
                break;
            case GET_OBSERVATION_WRAPPER:
                observationResponseCall = mApiService.getObservationWrapper(onLoadingInitialized.getResourceId(), observationWrapperFilter);
                getObservationWrapperResponse(observationResponseCall);
                break;
            case TOTAL_OBSERVATIONS_COUNT:
                countCall = mApiService.count(countWhereClause);
                getCount(countCall);

        }
    }

    /**
     * <p>
     * Returns the count of Observations made by this user from the server.
     * </p>
     * <p>
     * When an event is posted on Bus for getting counts of Observation from the server, a call is delegated to this method.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param countCall
     */
    public void getCount(Call<ResponseCount> countCall) {
        countCall.enqueue(new Callback<ResponseCount>() {
            @Override
            public void onResponse(Call<ResponseCount> call, Response<ResponseCount> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnLoadedCount(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCount> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }

    /**
     * <p>
     * Subscribes to the event of loading list of Observations to store on server
     * </p>
     * <p>
     * When an event is posted on Bus for uploading a list of Observations stored locally to server, a call is delegated to
     * this method. Each Observation is uploaded in serial manner from the list of Observations. This method makes sure that
     * for each Observation being uploaded, the corresponding image is uploaded to the server as well in the consecutive call.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param onListLoadingInitialized
     */
    @Subscribe
    public void onPostObservationsLoadingInitialized(ObservationEvent.OnListLoadingInitialized onListLoadingInitialized) {
        List<Observation> observationsList = onListLoadingInitialized.getRequest();
        massUploadResponseList = new ArrayList<>();

        Call<Observation> observationCall = null;
        // for each Observations in the list, make a call and push data on the server
        for (int i = 0; i < observationsList.size(); i++) {
            observationCall = mApiService.add(onListLoadingInitialized.getRequest().get(i));
            // makes the Calls to network.
            observationCall.enqueue(new Callback<Observation>() {
                @Override
                public void onResponse(Call<Observation> call, Response<Observation> response) {
                    if (response.isSuccess()) {
                        massUploadResponseList.add(response.body());
                    } else {
                        int statusCode = response.code();
                        ResponseBody errorBody = response.errorBody();
                        try {
                            mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                            Log.i(TAG, mErrorResponse.getApiError().getMessage());
                            mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                        } catch (IOException e) {
                            mBus.post(ObservationEvent.FAILED);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Observation> call, Throwable t) {
                    if (t != null && t.getMessage() != null) {
                        mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                    } else {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            });
        }
        if (massUploadResponseList != null) {
            // finally post response list
            mBus.post(new ObservationEvent.OnListLoaded(massUploadResponseList));

        }
    }


    /**
     * <p>
     * Makes CREATE, READ, UPDATE type network call to server using Retrofit Api service and posts the response on the event bus.
     * </p>
     * <p>
     * When an event is posted for the Observation Create, Update or Read then the call is delegated to this method. This logic
     * provides a modular approach for implementing the CRUD operations for Observation model on server.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param observationCall
     */
    public void makeCRUCall(Call<Observation> observationCall) {
        // makes the Calls to network.
        observationCall.enqueue(new Callback<Observation>() {
            @Override
            public void onResponse(Call<Observation> call, Response<Observation> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Observation> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }


    /**
     * <p>
     * Makes DELETE type network call to server using Retrofit Api service and posts the response on the event bus.
     * </p>
     * <p>
     * When an event is posted in Bus for deleting the Observation then the call is delegated to this method.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param observationCall
     */
    public void makeDeleteCall(Call<ResponseCount> deleteObservationCall) {
        // makes the Calls to network.
        deleteObservationCall.enqueue(new Callback<ResponseCount>() {
            @Override
            public void onResponse(Call<ResponseCount> call, Response<ResponseCount> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnLoadedCount(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCount> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }

    /**
     * Returns list of {@link Observation} for current User.
     * <p>
     * When an event is posted in Bus for fetching all the Observations from the server, the call is delegated to this method.
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param listObservationCall
     */
    public void getAllObservationsCall(Call<List<Observation>> listObservationCall) {
        // makes the Calls to network.
        listObservationCall.enqueue(new Callback<List<Observation>>() {
            @Override
            public void onResponse(Call<List<Observation>> call, Response<List<Observation>> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnListLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Observation>> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }


    /**
     * <p>
     * Returns a list of {@link Observation} for specific {@link com.sfsu.entities.Activities} which includes Locations for
     * each Observations as well
     * </p>
     * <p>
     * When an event is posted to retrieve list of {@link com.sfsu.entities.EntityLocation} for a specific Activity, then a call
     * is delegated to this method
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param locationsCall
     */
    public void get_Activity_Observations(Call<List<Observation>> observationCall) {
        observationCall.enqueue(new Callback<List<Observation>>() {
            @Override
            public void onResponse(Call<List<Observation>> call, Response<List<Observation>> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnListLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Observation>> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }


    /**
     * <p>
     * Returns a custom ObservationResponse containing the Observation, Activity and Tick
     * </p>
     * <p>
     * When an event is posted to get the {@link ObservationResponse} (which is Observation and its {@link com.sfsu.entities
     * .EntityLocation}) from the server, then the call is delegated to this method
     * </p>
     * <p>
     * When the response is received from the server, the response is posted back on the event Bus which is later subscribed to
     * fetch the response from the Event Bus. This way the whole logic for making network calls to getting ans passing the data
     * is made simpler and readable.
     * </p>
     *
     * @param observationResponseCall
     */
    public void getObservationWrapperResponse(Call<ObservationResponse> observationResponseCall) {
        observationResponseCall.enqueue(new Callback<ObservationResponse>() {
            @Override
            public void onResponse(Call<ObservationResponse> call, Response<ObservationResponse> response) {
                if (response.isSuccess()) {
                    mBus.post(new ObservationEvent.OnObservationWrapperLoaded(response.body()));
                } else {
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                        mBus.post(new ObservationEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                    } catch (IOException e) {
                        mBus.post(ObservationEvent.FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ObservationResponse> call, Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }
}
