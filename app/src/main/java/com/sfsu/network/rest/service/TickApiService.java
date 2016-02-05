package com.sfsu.network.rest.service;


import com.sfsu.entities.Tick;
import com.sfsu.network.api.ApiResources;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * The <b>Service</b> interface to manage http network calls for {@link Tick} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface TickApiService {

    /**
     * Returns specific Tick
     *
     * @return
     */
    @GET(ApiResources.TickBase + "/" + ApiResources.ID)
    public Call<Tick> get(@Path("id") String tickId);

    /**
     * Returns list of Tick
     *
     * @return
     */
    @GET(ApiResources.TickBase)
    public Call<List<Tick>> getAll();

}