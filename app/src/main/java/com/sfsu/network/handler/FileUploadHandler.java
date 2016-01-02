package com.sfsu.network.handler;

import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ObservationApiService;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

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
    public void onInitializeObservationEvent(FileUploadEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Observation> imageUploadCall = null;

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case UPLOAD_TICK_IMAGE:
                imageUploadCall = mApiService.upload(onLoadingInitialized.getRequest());

                imageUploadCall.enqueue(new Callback<Observation>() {
                    @Override
                    public void onResponse(Response<Observation> response) {
                        if (response.isSuccess()) {
                            mBus.post(new FileUploadEvent.OnLoaded(response.body()));
                        } else {
                            int statusCode = response.code();
                            ResponseBody errorBody = response.errorBody();
                            try {
                                mErrorResponse = mGson.fromJson(errorBody.string(), ErrorResponse.class);
                                mBus.post(new FileUploadEvent.OnLoadingError(mErrorResponse.getApiError().getMessage(), statusCode));
                            } catch (IOException e) {
                                mBus.post(FileUploadEvent.FAILED);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t != null && t.getMessage() != null) {
                            mBus.post(new FileUploadEvent.OnLoadingError(t.getMessage(), -1));
                        } else {
                            mBus.post(FileUploadEvent.FAILED);
                        }
                    }
                });

        }
    }
}
