package com.sfsu.network.handler;

import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ObservationApiService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Call;

/**
 * Created by Pavitra on 1/1/2016.
 */
public class FileUploadHandler extends ApiRequestHandler {

    private final String TAG = "~!@#FileUploadReqHdlr";
    private ObservationApiService mApiService;
    private ErrorResponse mErrorResponse;

    public FileUploadHandler(Bus bus) {
        super(bus);
        mApiService = RetrofitApiClient.createService(ObservationApiService.class, ACCESS_TOKEN);
    }

    @Subscribe
    public void onInitializeObservationEvent(ObservationEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Observation> observationCall = null;

    }
}
