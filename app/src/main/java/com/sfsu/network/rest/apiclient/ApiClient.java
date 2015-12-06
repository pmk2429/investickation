package com.sfsu.network.rest.apiclient;

/**
 * The singleton ApiClient that declares and initializes the Retrofit instance to build the instance and create the Retrofit
 * Service Interface.
 * <p>
 * The class enforces singleton design pattern in order to avoid creating other instances and hence avoiding
 * possible network leaks and errors.
 * </p>
 * Created by Pavitra on 11/27/2015.
 */
@Deprecated
public class ApiClient {

    private static volatile ApiClient instance;

    private ApiClient() {

    }

    public static ApiClient getInstance() {
        ApiClient localInstance = instance;
        if (localInstance == null) {
            synchronized (ApiClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ApiClient();
                }
            }
        }
        return localInstance;

    }


}
