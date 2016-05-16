package com.sfsu.network.handler;

import android.content.Context;

import com.google.gson.Gson;
import com.sfsu.network.auth.AuthPreferences;
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
    public static final int ACT_OBSERVATIONS = 0x3EE;
    public static final int ACT_LOCATIONS = 0x3EF;

    // LoginFragment Api service method codes
    public static final int LOGIN = 0x3F0;
    // Account Api service method codes.
    public static final int TOTAL_ACTIVITIES_COUNT = 0x3F1;
    public static final int TOTAL_OBSERVATIONS_COUNT = 0x3F2;
    // File upload
    public static final int UPLOAD_TICK_IMAGE = 0x3F3;
    // post list of activities and observations
    public static final int POST_ACTIVITIES_LIST = 0x3F4;
    public static final int POST_OBSERVATION_LIST = 0x3F5;
    // wrapper class
    public static final int GET_OBSERVATION_WRAPPER = 0x2F2;
    // get recent activities
    public static final int GET_RECENT_ACTIVITIES = 0x2F3;
    // get all counts
    public static final int GET_ACT_OBS_COUNT = 0x2F4;
    // get all Ticks using Observable
    public static final int GET_ALL_TICKS = 0x2F5;

    private final String TAG = "~!@#$ApiReqHdlr";
    protected String ACCESS_TOKEN;
    protected String USER_ID;
    protected Bus mBus;
    protected Gson mGson;
    private Context mContext;
    private AuthPreferences mAuthPreferences;

    public ApiRequestHandler(Bus bus, Context mContext) {
        this.mBus = bus;
        mGson = new Gson();
        this.mContext = mContext;
        mAuthPreferences = new AuthPreferences(mContext);
        ACCESS_TOKEN = mAuthPreferences.getAccessToken();
        USER_ID = mAuthPreferences.getUser_id();
    }

}
