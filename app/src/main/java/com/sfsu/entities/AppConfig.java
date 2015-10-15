package com.sfsu.entities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <p>
 * <tt>AppConfig</tt> class is the Application Level Configuration Class which contains all the
 * default and static final keywords as well as methods required through out the application.
 * </p>
 * Created by Pavitra on 6/5/2015.
 */
public class AppConfig {

    // Log message for Application Context
    public static final String LOGSTRING = "#INVEST=====> ";
    // Base url to the endpoint
    public static final String BASE_URL = "http://investickation.com";
    // Resource Identifiers
    public static final String USER_RESOURCE = "user";
    public static final String ACTIVITY_RESOURCE = "activities";
    public static final String OBSERVATION_RESOURCE = "observations";
    public static final String TICK_RESOURCE = "ticks";
    // HTTP verbs
    public static final String GET_VERB = "GET";
    public static final String POST_VERB = "POST";
    public static final String PUT_VERB = "PUT";
    public static final String DELETE_VERB = "DELETE";
    // context passed for each activity
    private Context myContext;


    // Constructor
    public AppConfig(Context myContext) {
        this.myContext = myContext;
    }

    // method to check whether there is any network connection or not.
    public boolean isConnectedOnline() {
        ConnectivityManager cmObj = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoObj = cmObj.getActiveNetworkInfo();
        if (networkInfoObj != null && networkInfoObj.isConnected()) {
            return true;
        }
        return false;
    }
}
