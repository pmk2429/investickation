package com.sfsu.network.handler;

import android.content.Context;
import android.util.Log;

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
    public static final int OBSERVATIONS = 0x3EE;
    public static final int LOCATIONS = 0x3EF;
    // Login Api service method codes
    public static final int LOGIN = 0x3F0;
    // User Api service method codes.
    public static final int TOTAL_ACTIVITIES_COUNT = 0x3F1;
    public static final int TOTAL_OBSERVATIONS_COUNT = 0x3F2;
    private final String TAG = "~!@#$ApiReqHdlr";
    protected String ACCESS_TOKEN;// = "oGATGjerFGwbVsTdxmc3HapSdPX6aY23zJ8yTaDg5pumWnTPexeeLfTQaLV2uCsG";
    protected Bus mBus;
    private Context mContext;

    public ApiRequestHandler(Bus bus) {
        this.mBus = bus;
    }

    /**
     * initialize the access token for the application context.
     *
     * @param mContext
     */
    public void init(Context mContext) {
        this.mContext = mContext;
        ACCESS_TOKEN = new AuthPreferences(mContext).getAccessToken();
        Log.i(TAG, ACCESS_TOKEN);
    }
}
