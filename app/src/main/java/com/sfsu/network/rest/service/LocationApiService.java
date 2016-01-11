package com.sfsu.network.rest.service;


import com.sfsu.entities.EntityLocation;
import com.sfsu.network.api.ApiResources;

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
    @GET(ApiResources.EntityLocationBase + "/" + ApiResources.ID)
    public Call<EntityLocation> get(@Path("id") String locationId);

    /**
     * Returns the list of {@link EntityLocation} in resource based.
     *
     * @return
     */
    @GET(ApiResources.EntityLocationBase)
    public Call<List<EntityLocation>> getAll();


    /**
     * Returns the list of all {@link EntityLocation} for a specific {@link com.sfsu.entities.Activities}.
     *
     * @return
     */
    // GET /activities/{id}/locations
    @GET(ApiResources.ActivitiesBase + "/" + ApiResources.ID + "/" + ApiResources.EntityLocationBase)
    public Call<List<EntityLocation>> get_Activities_Locations();


    /**
     * Adds a {@link EntityLocation} to the server.
     *
     * @return
     */
    @POST(ApiResources.EntityLocationBase)
    public Call<EntityLocation> add(@Body EntityLocation entityLocation);


    /**
     * Updates a specific {@link EntityLocation} in the server.
     *
     * @param locationId     {@link EntityLocation} ID.
     * @param entityLocation {@link EntityLocation}
     * @return
     */
    @GET(ApiResources.EntityLocationBase + "/" + ApiResources.ID)
    public Call<EntityLocation> update(@Path("id") String locationId, @Body EntityLocation entityLocation);


    /**
     * Deletes {@link EntityLocation} from the server.
     *
     * @return
     */
    @GET(ApiResources.EntityLocationBase + "/" + ApiResources.ID)
    public Call<Integer> delete(@Path("id") String locationId);

}