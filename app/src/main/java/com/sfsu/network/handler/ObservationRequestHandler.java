package com.sfsu.network.handler;

import android.content.Context;

import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ObservationApiService;
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
     * Subscribes to the event when {@link Observation} is Loaded. Receives the Observation object and make
     * network calls to Retrofit depending on the type of request made.
     *
     * @param onLoadingInitialized
     */
    @Subscribe
    public void onInitializeObservationEvent(ObservationEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Observation> observationCall = null;
        Call<Integer> deleteObservation = null;
        Call<List<Observation>> listObservationCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case GET:
                observationCall = mApiService.get(onLoadingInitialized.getResourceId());
                makeCRUDCall(observationCall);
                break;
            case GET_ALL:
                listObservationCall = mApiService.getAll(USER_ID);
                getAllObservationsCall(listObservationCall);
                break;
            case ADD:
                observationCall = mApiService.add(onLoadingInitialized.getRequest());
                makeCRUDCall(observationCall);
                break;
            case DELETE:
                deleteObservation = mApiService.delete(onLoadingInitialized.getResourceId());
                makeCRUDCall(observationCall);
                break;
            case ACT_OBSERVATIONS:
                listObservationCall = mApiService.observationsOfActivity(onLoadingInitialized.activityId);
                get_Activity_Observations(listObservationCall);
        }
    }

    /**
     * Makes CRUD type network call to server using Retrofit Api service and posts the response on the event bus.
     *
     * @param observationCall
     */
    public void makeCRUDCall(Call<Observation> observationCall) {
        // makes the Calls to network.
        observationCall.enqueue(new Callback<Observation>() {
            @Override
            public void onResponse(Response<Observation> response) {
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
            public void onFailure(Throwable t) {
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
     *
     * @param listObservationCall
     */
    public void getAllObservationsCall(Call<List<Observation>> listObservationCall) {
        // makes the Calls to network.
        listObservationCall.enqueue(new Callback<List<Observation>>() {
            @Override
            public void onResponse(Response<List<Observation>> response) {
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
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }


    /**
     * Returns a list of {@link Observation} for specific {@link com.sfsu.entities.Activities}.
     *
     * @param locationsCall
     */
    public void get_Activity_Observations(Call<List<Observation>> observationCall) {
        observationCall.enqueue(new Callback<List<Observation>>() {
            @Override
            public void onResponse(Response<List<Observation>> response) {
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
            public void onFailure(Throwable t) {
                if (t != null && t.getMessage() != null) {
                    mBus.post(new ObservationEvent.OnLoadingError(t.getMessage(), -1));
                } else {
                    mBus.post(ObservationEvent.FAILED);
                }
            }
        });
    }
}
