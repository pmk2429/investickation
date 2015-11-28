package com.sfsu.controllers;

import android.content.Context;

import com.sfsu.entities.Entity;
import com.sfsu.network.rest.EntitiesApiService;
import com.sfsu.network.handler.ServiceManager;
import com.sfsu.utils.AppUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

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
    private EntitiesApiService entitiesApi;
    private Entity entityResponseObj;
    private ArrayList<Entity> entityListResponse;

    public RetrofitController(Context context) {
        this.context = context;
        entitiesApi = ServiceManager.createRetrofitService(AppUtils.BASE_URL);
    }

    /**
     * getters and setter for accessing the data from the Response
     **/
    public ArrayList<Entity> getEntityListResponse() {
        return entityListResponse;
    }

    public void setEntityListResponse(ArrayList<Entity> entityListResponse) {
        this.entityListResponse = entityListResponse;
    }

    public Entity getEntityResponseObj() {
        return entityResponseObj;
    }

    public void setEntityResponseObj(Entity entityResponseObj) {
        this.entityResponseObj = entityResponseObj;
    }

    /**
     * Method provides the Controller layer to GET and POST data using Retrofit. Get the Call from the EntitiesApiService and
     * then pass on the parsed object to the Calling Fragment/Activity.
     *
     * @param entityId - entity Id of resource. Converts it to String.
     * @return
     */
    public Entity get(String resourceType, long entityId) {
        Object[] args = {resourceType, entityId};
        final String resourceId = String.valueOf(entityId);
        if (Collections.frequency(Arrays.asList(args), null) >= 1) {
            return null;
        } else {
            Call<Entity> entityCall = entitiesApi.get(resourceType, resourceId);
            entityCall.enqueue(new Callback<Entity>() {
                @Override
                public void onResponse(Response<Entity> response) {
                    Entity entityObj = response.body();
                    setEntityResponseObj(entityObj);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
            return null;
        }
    }

    /**
     * This method is used to get list of all the entities from the remote API.
     *
     * @param resourceType
     * @return
     */
    public ArrayList<Entity> getAll(final String resourceType) {
        Object[] args = {resourceType};

        if (Collections.frequency(Arrays.asList(args), null) >= 0) {
            return null;
        } else {

            Call<ArrayList<Entity>> listCall = entitiesApi.getAll(resourceType);
            listCall.enqueue(new Callback<ArrayList<Entity>>() {
                @Override
                public void onResponse(Response<ArrayList<Entity>> response) {
                    ArrayList<Entity> entityList = response.body();
                    setEntityListResponse(entityList);
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: custom Exception
                }
            });
            return getEntityListResponse();
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
        //final Entity[] demo = new Entity[1];
        Object[] args = {resourceType, entity};
        if (Collections.frequency(Arrays.asList(args), null) >= 1) {
            return null;
        } else {
            Call<Entity> entityCall = entitiesApi.add(resourceType, entity);
            entityCall.enqueue(new Callback<Entity>() {
                @Override
                public void onResponse(Response<Entity> response) {
                    Entity createdEntityResponse = response.body();
                    setEntityResponseObj(createdEntityResponse);
                    //demo[0] = createdEntityResponse;
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: create custom Exceptions
                }
            });
            return getEntityResponseObj();
        }
    }

    /**
     * Method to update the entity
     *
     * @param resourceType
     * @param resourceId
     * @param entity
     * @return
     */
    public Entity update(String resourceType, long entityId, Entity entity) {
        String resourceIdString = String.valueOf(entityId);
        Object[] args = {resourceType, resourceIdString, entity};
        if (Collections.frequency(Arrays.asList(args), null) >= 2) {
            return null;
        } else {
            Call<Entity> entityCall = entitiesApi.update(resourceType, resourceIdString, entity);
            entityCall.enqueue(new Callback<Entity>() {
                @Override
                public void onResponse(Response<Entity> response) {
                    Entity updatedEntityResponse = response.body();
                    setEntityResponseObj(updatedEntityResponse);
                    //demo[0] = createdEntityResponse;
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: custom exception for error
                }
            });
            return getEntityResponseObj();
        }
    }


    /**
     * Method to delete the entity resource.
     *
     * @param resourceType
     * @param entityId
     * @return
     */
    public Entity delete(String resourceType, long entityId) {
        String resourceIdString = String.valueOf(entityId);
        Object[] args = {resourceType, resourceIdString};
        if (Collections.frequency(Arrays.asList(args), null) >= 1) {
            return null;
        } else {

            Call<Entity> entityCall = entitiesApi.delete(resourceType, resourceIdString);
            entityCall.enqueue(new Callback<Entity>() {
                @Override
                public void onResponse(Response<Entity> response) {
                    Entity deletedEntityResponse = response.body();
                    setEntityResponseObj(deletedEntityResponse);
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: custom exception
                }
            });

            return getEntityResponseObj();
        }
    }
}
