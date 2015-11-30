package com.sfsu.network.rest.service;


import com.sfsu.entities.EntityLocation;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface used to manage http network calls for {@link EntityLocation} to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface LocationApiService {

    /**
     * This method gets a specific location by using ID of that resource.
     *
     * @return
     */
    @GET("/locations/{id}")
    public Call<EntityLocation> get(@Path("id") String locationId);

    /**
     * This method returns the list of all the Entities in resource based on the resourceIdentifier.
     *
     * @return
     */
    @GET("/locations")
    public Call<List<EntityLocation>> getAll();


    /**
     * This method is used to create the specified resource and store in database.
     *
     * @return
     */
    @POST("/locations")
    public Call<EntityLocation> add(@Body EntityLocation entityLocation);


    @GET("/locations/{id}")
    public Call<EntityLocation> update(@Path("id") String locationId, @Body EntityLocation entityLocation);


    /**
     * Deletes the record from database.
     *
     * @return
     */
    @GET("/locations/{id}")
    public Call<EntityLocation> delete(@Path("id") String locationId);

}