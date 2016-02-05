package com.sfsu.network.rest.service;


import com.sfsu.entities.Observation;
import com.sfsu.network.api.ApiResources;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


/**
 * The <b>Service</b> interface to manage http network calls for {@link Observation} related operations to the REST API endpoint.
 */
public interface FileUploadService {
    @Multipart
    @POST(ApiResources.ObservationBase + "/" + "upload_tick_pic")
    Call<Observation> uploadImage(@PartMap Map<String, RequestBody> params, @Query("id") String observationId);
}