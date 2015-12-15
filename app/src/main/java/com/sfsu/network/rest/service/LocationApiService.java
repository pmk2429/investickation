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
     * Returns a specific {@link EntityLocation} matching the id.
     *
     * @return
     */
    @GET("/locations/{id}")
    public Call<EntityLocation> get(@Path("id") String locationId);

    /**
     * Returns the list of {@link EntityLocation} in resource based.
     *
     * @return
     */
    @GET("/locations")
    public Call<List<EntityLocation>> getAll();


    /**
     * Adds a {@link EntityLocation} to the server.
     *
     * @return
     */
    @POST("/locations")
    public Call<EntityLocation> add(@Body EntityLocation entityLocation);


    /**
     * Updates a specific {@link EntityLocation} in the server.
     *
     * @param locationId
     * @param entityLocation
     * @return
     */
    @GET("/locations/{id}")
    public Call<EntityLocation> update(@Path("id") String locationId, @Body EntityLocation entityLocation);


    /**
     * <p>
     * Deletes {@link EntityLocation} from the server.
     * </p>
     *
     * @return
     */
    @GET("/locations/{id}")
    public Call<EntityLocation> delete(@Path("id") String locationId);

}