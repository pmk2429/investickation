package com.sfsu.network.rest.service;


import com.sfsu.entities.Activities;
import com.sfsu.entities.response.ResponseCount;
import com.sfsu.network.api.ApiResources;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * The <b>Service</b> interface to manage http network calls for {@link Activities} related operations to the REST API endpoint.
 * Created by Pavitra on 10/6/2015.
 */

public interface ActivityApiService {

    /**
     * Returns a specific {@link Activities} to corresponding id.
     *
     * @return
     */
//    @GET("activities/{id}")
    @GET(ApiResources.ActivitiesBase + "/" + ApiResources.ID)
    public Call<Activities> get(@Path("id") String activityId);

    /**
     * Returns the list of all the {@link Activities} from the server.
     *
     * @return
     */
//    @GET("accounts/{id}/activities")
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID + "/" + ApiResources.ActivitiesBase)
    public Call<List<Activities>> getAll(@Path("id") String user_id, @Query("filter") String orderFilter);


    /**
     * Returns the list of all the {@link Activities} from the server sorted by the order specified.
     *
     * @return
     */
//    @GET("accounts/{id}/activities")
//    public Call<List<Activities>> getAll(@Path("id") String user_id, @Query("sort") String sortOrder);


    /**
     * Creates new {@link Activities} record on the Server.
     *
     * @return
     */
    @POST(ApiResources.ActivitiesBase)
    public Call<Activities> add(@Body Activities activity);


    /**
     * Updates {@link Activities} record on the server.
     *
     * @param activityId
     * @param activity
     * @return
     */
    @PUT(ApiResources.ActivitiesBase + "/" + ApiResources.ID)
    public Call<Activities> update(@Path("id") String activityId, @Body Activities activity);


    /**
     * Delete and Destroy {@link Activities} from the server.
     *
     * @return
     */
    @DELETE(ApiResources.ActivitiesBase + "/" + ApiResources.ID)
    public Call<ResponseCount> delete(@Path("id") String activityId);


    /**
     * Returns total locations captured for this activity.
     *
     * @param activityId
     * @return
     */
    @GET(ApiResources.ActivitiesBase + "/" + ApiResources.ID + "/" + ApiResources.EntityLocationBase + "/" + "count")
    public Call<ResponseCount> totalLocations(@Path("id") String activityId);


    /**
     * Returns total Activities recorded by the user.
     *
     * @param activityId
     * @return
     */
    @GET(ApiResources.ActivitiesBase + "/" + "count")
    public Call<ResponseCount> count(@Query("where") String whereClause);


    /**
     * Returns 2 recent Activities made by user depending on the Timestamp
     *
     * @param userId
     * @param filter
     * @return
     */
    @GET(ApiResources.AccountBase + "/" + ApiResources.ID + "/" + ApiResources.ActivitiesBase)
    public Call<List<Activities>> getRecentActivities(@Path("id") String userId, @Query("filter") String filter);
}