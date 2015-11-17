package com.sfsu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * <tt>AppUtils</tt> class is the Application Level Configuration Class which contains all the default and static final keywords
 * as well as methods required through out the application.
 * </p>
 * <p>
 * The main reason for defining these resources in AppUtils is to make these resources independent of Activity and Fragment
 * Lifecycle events hence making it easy to use.
 * </p>
 * <p>
 * AppUtils also define the application level methods that might be required for the Activities/Fragments to carry out specific
 * operations such as network connections checks, getting current timestamps etc.
 * </p>
 * Created by Pavitra on 6/5/2015.
 */
public class AppUtils {

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
    public AppUtils(Context myContext) {
        this.myContext = myContext;
    }

    /**
     * Helper method to get the current system time stamp
     *
     * @return
     */
    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * Helper method to get the Current date and time from timestamp
     *
     * @return
     */
    public static String getCurrentDateAndTime(long timestamp) {
        Date date = new Date(timestamp);
        Format format = new SimpleDateFormat("MM-dd-yy HH:mm:ss");
        String dateTime = format.format(date);
        return dateTime;
    }

    /**
     * Returns <tt>true</tt> if input String is not empty and Numeric.
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return !TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str);
    }

    // method to check whether there is any network connection or not.

    // Method to check if EntityLocation is enabled or disabled.
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

    /**
     * Returns <tt>true</tt> if the Connections is available. Else returns <tt>false</tt>.
     *
     * @return
     */
    public boolean isConnectedOnline() {
        ConnectivityManager cmObj = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoObj = cmObj.getActiveNetworkInfo();
        if (networkInfoObj != null && networkInfoObj.isConnected()) {
            return true;
        }
        return false;
    }
}
