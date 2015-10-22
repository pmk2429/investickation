package com.sfsu.utils.controllers;

import android.content.Context;

import com.sfsu.entities.AppConfig;
import com.sfsu.entities.Entity;
import com.sfsu.rest.EntitiesApi;
import com.sfsu.rest.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;

/**
 * The RetrofitController provides a level of abstraction between Retrofit API and the Fragments/Activities.
 * All the business logic, type conversion, data manipulation and security checks are done in this Controller layer.
 * The controller provides the gateway to access the api methods defined in the Retrofit Service Interface.
 * <p/>
 * Another important reason for implementing this layer is to <tt>reduce unnecessary and un required network calls</tt> to the
 * REST API. All the network calls having garbage data will be prevented from calling the API and hence limiting the
 * battery usage.
 * <p/>
 * Created by Pavitra on 10/19/2015.
 */
public class RetrofitController {

    private Context context;
    private EntitiesApi entitiesApi;

    public RetrofitController(Context context) {
        this.context = context;
        entitiesApi = RestClient.createRetrofitService(AppConfig.BASE_URL);
    }

    /**
     * Method provides the Controller layer to GET and POST data using Retrofit. Get the Call from the EntitiesApi and
     * then pass on the parsed object to the Calling Fragment/Activity.
     *
     * @param entityId - entity Id of resource. Converts it to String.
     * @return
     */
    public Call<Entity> get(String resourceType, long entityId) {
        if (entityId != 0 && entityId > 0) {
            String resourceId = String.valueOf(entityId);
            return entitiesApi.get(resourceType, resourceId);
        } else {
            return null;
        }
    }

    /**
     * This method is used to get list of all the entities from the remote API.
     *
     * @param resourceType
     * @return
     */
    public Call<List<Entity>> getAll(String resourceType) {
        if (!resourceType.equals("") && resourceType != null) {
            return entitiesApi.getAll(resourceType);
        } else {
            return null;
        }
    }

    /**
     * Method to add Resource using the Retrofit API. This method receives the Call<Entity> as response by calling the
     * Retrofit API and it parses this response to get the Entity and returns it back to the calling Fragment/Entity method.
     *
     * @param resourceType
     * @param entity
     * @return
     */
    public Entity add(String resourceType, Entity entity) {
        Entity entity1 = null;
        Object[] args = {resourceType, entity};
        if (Collections.frequency(Arrays.asList(args), null) >= 1) {

            entitiesApi.add(resourceType, entity);

            return entity1;
        }
        return entity1;
    }

    //

    /**
     * Method to add Resource using the Retrofit API.
     *
     * @param resourceType
     * @param entity
     * @return
     */
    public void demo(String resourceType, Entity entity, Callback<Entity> callback) {
        entitiesApi.demo(resourceType, entity, callback);
    }

    /**
     * Method to update the entity
     *
     * @param resourceType
     * @param resourceId
     * @param entity
     * @return
     */
    public Call<Entity> update(String resourceType, long entityId, Entity entity) {
        String resourceIdString = String.valueOf(entityId);
        Object[] args = {resourceType, resourceIdString, entity};
        if (Collections.frequency(Arrays.asList(args), null) >= 2) {
            return null;
        } else {
            return entitiesApi.update(resourceType, resourceIdString, entity);
        }
    }


    /**
     * Method to delete the entity resource.
     *
     * @param resourceType
     * @param entityId
     * @return
     */
    public Call<Entity> delete(String resourceType, long entityId) {
        String resourceIdString = String.valueOf(entityId);
        Object[] args = {resourceType, resourceIdString};
        if (Collections.frequency(Arrays.asList(args), null) >= 1) {
            return null;
        } else {
            return entitiesApi.delete(resourceType, resourceIdString);
        }
    }
}
