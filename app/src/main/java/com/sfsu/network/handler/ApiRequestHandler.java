package com.sfsu.network.handler;

import com.squareup.otto.Bus;

/**
 * <p>
 * Parent class for all RequestHandlers in the application. It contains the api method identifiers and a constructor to handle
 * the Retrofit network calls for each of {@link com.sfsu.entities.Entity} present in the InvesTICKations project.
 * </p>
 * Created by Pavitra on 11/27/2015.
 */
public class ApiRequestHandler {

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
    public static final int GET_METHOD = 0x03E8;

    protected static final String ACCESS_TOKEN = "rqBGCbJJ7q0pxbARViZDKmaJJphgR5xFxftwR5qIQYelrfTvfACgq6AiDEUGNoJj";

    protected Bus mBus;

    public ApiRequestHandler(Bus bus) {
        this.mBus = bus;
    }
}
