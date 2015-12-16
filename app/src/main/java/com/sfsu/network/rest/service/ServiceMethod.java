package com.sfsu.network.rest.service;

/**
 * Created by Pavitra on 12/15/2015.
 */
public interface ServiceMethod {


    // common static method codes for each Retrofit API service interface.
    public static final String GET = "get";
    public static final String GET_ALL = "getAll";
    public static final String ADD = "add";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String TOTAL_LOCATIONS_COUNT = "totalLocations";

    // Activity Api Service related method codes.
    public static final String OBSERVATIONS = "observations";
    public static final String LOCATIONS = "locations";

    // Login Api service method codes
    public static final String LOGIN = "login";

    // User Api service method codes.
    public static final String TOTAL_ACTIVITIES_COUNT = "totalActivities";
    public static final String TOTAL_OBSERVATIONS_COUNT = "totalObservations";


}
