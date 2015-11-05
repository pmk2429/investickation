package com.sfsu.entities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * <p>
 * <tt>AppConfig</tt> class is the Application Level Configuration Class which contains all the
 * default and static final keywords as well as methods required through out the application.
 * </p>
 * Created by Pavitra on 6/5/2015.
 */
public class AppConfig {

    // Log message for Application Context
    public static final String LOGTAG = "~!@#$%";
    // Base url to the endpoint
    public static final String BASE_URL = "http://investickation.com";
    // Resource Identifiers
    public static final String USER_RESOURCE = "users";
    public static final String ACTIVITY_RESOURCE = "activities";
    public static final String OBSERVATION_RESOURCE = "observations";
    public static final String TICK_RESOURCE = "ticks";
    public static final String LOCATION_RESOURCE = "locations";
    // HTTP verbs
    public static final String GET_VERB = "GET";
    public static final String POST_VERB = "POST";
    public static final String PUT_VERB = "PUT";
    public static final String DELETE_VERB = "DELETE";
    // Unique Key
    public static final String USER_KEY = "key_user";
    public static final String ACTIVITY_KEY = "key_activities";
    public static final String OBSERVATION_KEY = "key_observation";
    public static final String LOCATION_KEY = "key_location";
    public static final String TICK_KEY = "key_tick";
    // list keys
    public static final String USER_LIST_KEY = "key_user_list";
    public static final String ACTIVITY_LIST_KEY = "key_activities_list";
    public static final String OBSERVATION_LIST_KEY = "key_observation_list";
    public static final String LOCATION_LIST_KEY = "key_location_list";
    public static final String TICK_LIST_KEY = "key_tick_list";


    // context passed for each activity
    private Context myContext;

    // Constructor
    public AppConfig(Context myContext) {
        this.myContext = myContext;
    }

    // Method to check if Location is enabled or disabled.
    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        /**
         * @return true when the caller API version is at least Kitkat 19
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(myContext.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
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
