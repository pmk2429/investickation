package com.sfsu.network.handler;

import android.util.Log;

import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.ObservationApiService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public void onInitializeFileUploadEvent(FileUploadEvent.OnLoadingInitialized onLoadingInitialized) {
        Call<Observation> imageUploadCall = null;
        Log.i(TAG, "3) reached desired point");

        // separate the Method logic
        switch (onLoadingInitialized.apiRequestMethod) {
            case UPLOAD_TICK_IMAGE:

                // build a map of RequestBody.

                Map<String, RequestBody> requestBodyMap = new HashMap<>();
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"), onLoadingInitialized.observationId);
                requestBodyMap.put("id", id);

                String fileName = "file\"; filename=\"" + onLoadingInitialized.getRequest().getImage_name();

                requestBodyMap.put(fileName, onLoadingInitialized.getRequest().getRequestBody());
                imageUploadCall = mApiService.upload(requestBodyMap);

                imageUploadCall.enqueue(new Callback<Observation>() {
                    @Override
                    public void onResponse(Response<Observation> response) {
                        if (response.isSuccess()) {
                            Log.i(TAG, "4a) Response success");
                            mBus.post(new FileUploadEvent.OnLoaded(response.body()));
                        } else {
                            Log.i(TAG, "4b) response failure");
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
                        Log.i(TAG, "4c) FAILURE");
                        if (t != null && t.getMessage() != null) {
                            Log.i(TAG, "4ci) message not null");
                            mBus.post(new FileUploadEvent.OnLoadingError(t.getMessage(), -1));
                        } else {
                            Log.i(TAG, "4cii) message null");
                            mBus.post(FileUploadEvent.FAILED);
                        }
                    }
                });

        }
    }
}
