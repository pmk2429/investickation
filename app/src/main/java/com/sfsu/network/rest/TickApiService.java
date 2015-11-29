package com.sfsu.network.rest;


import com.sfsu.entities.Tick;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * The <b>Service</b> interface is used to manage URL calls to the REST Api. In this interface you have to specify the type
 * of the request like POST, GET, PUT, etc.
 * Created by Pavitra on 10/6/2015.
 */

public interface TickApiService {

    /**
     * Returns specific Tick
     *
     * @return
     */
    @GET("/ticks/{id}")
    public Call<Tick> get(@Path("id") String tickId);

    /**
     * Returns list of Tick
     *
     * @return
     */
    @GET("/ticks")
    public Call<List<Tick>> getAll();


    /**
     * Add new Tick.
     *
     * @return
     */
    @POST("/ticks")
    public Call<Tick> add(@Body Tick tick);


    /**
     * Update the Tick for specific Id.
     *
     * @param TickId
     * @param Tick
     * @return
     */
    @GET("/ticks/{id}")
    public Call<Tick> update(@Path("id") String tickId, @Body Tick tick);


    /**
     * Delete the Tick record.
     *
     * @return
     */
    @GET("/ticks/{id}")
    public Call<Tick> delete(@Path("id") String tickId);

}