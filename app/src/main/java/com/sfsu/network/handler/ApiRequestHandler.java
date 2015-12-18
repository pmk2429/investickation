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
    public static final int GET = 0x3E8;
    public static final int GET_ALL = 0x3E9;
    public static final int ADD = 0x3EA;
    public static final int UPDATE = 0x3EB;
    public static final int DELETE = 0x3EC;
    public static final int TOTAL_LOCATIONS_COUNT = 0x3ED;
    // Activity Api Service related method codes.
    public static final int OBSERVATIONS = 0x3EE;
    public static final int LOCATIONS = 0x3EF;
    // Login Api service method codes
    public static final int LOGIN = 0x3F0;
    // User Api service method codes.
    public static final int TOTAL_ACTIVITIES_COUNT = 0x3F1;
    public static final int TOTAL_OBSERVATIONS_COUNT = 0x3F2;

    protected static final String ACCESS_TOKEN = "bJnpp5FuHj7xMhFbbi8zXG87n8pfS81cUKWtUAmKrOtlPHiE3U8hQtYHSBpdNvPz";

    protected Bus mBus;

    public ApiRequestHandler(Bus bus) {
        this.mBus = bus;
    }
}
