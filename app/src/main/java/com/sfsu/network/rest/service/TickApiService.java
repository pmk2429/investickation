package com.sfsu.network.rest.service;


import com.sfsu.entities.Tick;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

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
    @GET("ticks/{id}")
    public Call<Tick> get(@Path("id") String tickId);

    /**
     * Returns list of Tick
     *
     * @return
     */
    @GET("ticks")
    public Call<List<Tick>> getAll();

}