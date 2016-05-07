package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

import com.sfsu.entities.Observation;
import com.sfsu.network.error.ErrorResponse;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.rest.apiclient.RetrofitApiClient;
import com.sfsu.network.rest.service.FileUploadService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Pavitra on 1/1/2016.
 */
public class FileUploadHandler extends ApiRequestHandler {

    private final String TAG = "~!@#FileUploadReqHdlr";
    private FileUploadService mApiService;
    private ErrorResponse mErrorResponse;

    public FileUploadHandler(Bus bus, Context mContext) {
        super(bus, mContext);
        mApiService = RetrofitApiClient.createService(FileUploadService.class, ACCESS_TOKEN);
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

                // add file name param and RequestBody to Map
                requestBodyMap.put(onLoadingInitialized.getRequest().getImage_name(), onLoadingInitialized.getRequest()
                        .getRequestBody());

                // finally make a call to ApiService
                imageUploadCall = mApiService.uploadImage(requestBodyMap, onLoadingInitialized.observationId);

                imageUploadCall.enqueue(new Callback<Observation>() {
                    @Override
                    public void onResponse(Call<Observation> call, Response<Observation> response) {
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
                    public void onFailure(Call<Observation> call, Throwable t) {
                        Log.i(TAG, "4c) FAILURE");
                        if (t != null && t.getMessage() != null) {
                            Log.i(TAG, "4ci) message not null");
                            Log.i(TAG, t.getMessage());
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
