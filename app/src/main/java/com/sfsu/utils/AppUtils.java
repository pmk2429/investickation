package com.sfsu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.sfsu.network.api.ApiResources;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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
     * Helper method to get the date and time from timestamp. Converts the Timestamp in Milliseconds to Date and Time and then
     * formats the Date object with {@link Format} and returns the String of Date and Time.
     *
     * @return
     */
    public static String getDateAndTime(long timestamp) {
        Date date = new Date(timestamp);
        Format dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
        String dateTime = dateFormat.format(date);
        return dateTime;
    }


    /**
     * Helper method to get the date and time from timestamp. Converts the Timestamp in Milliseconds to Date and Time and then
     * formats the Date object with {@link Format} and returns the String of Date and Time separately respectively
     *
     * @return String[] containing Date and Time
     */
    public static String[] getDateAndTimeSeparate(long timestamp) {
        Date date = new Date(timestamp);
        Format dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        String dateTime = dateFormat.format(date);
        String[] timeAndDate = dateTime.split(" ");
        return timeAndDate;
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

    /**
     * Returns <tt>true</tt> if the Connections is available. Else returns <tt>false</tt>.
     *
     * @return
     */
    public static boolean isConnectedOnline(Context myContext) {
        ConnectivityManager cmObj = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoObj = cmObj.getActiveNetworkInfo();
        if (networkInfoObj != null && networkInfoObj.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Returns an Array of US States
     */
    public static ArrayList<String> getStates() {
        String[] states = new String[]{"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI",
                "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", "MI", "MN", "MO", "MS",
                "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "PW",
                "RI", "SC", "SD", "TN", "TX", "UT", "VA", "VI", "VT", "WA", "WI", "WV", "WY"};

        return new ArrayList<>(Arrays.asList(states));
    }

    /**
     * Returns the result of ping to server for successful availability and connection establishment
     */
    public static boolean isServerReachable(Context myContext) {
        ConnectivityManager cm = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL(ApiResources.SERVER_API_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10 * 1000);          // 10 s.
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns whether the location is enabled or not
     *
     * @return
     */
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

}
