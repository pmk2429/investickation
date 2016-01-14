package com.sfsu.controllers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Controller used to handle all the Location related operations and tasks such as finding the Last Know Location, getting
 * Location updates etc. The LocationController uses FusedLocation service provided by Google to get the Location updates.
 * <p>
 * <tt>LocationController</tt> also provides Callback Interface to get the Account's current Location and Featured name of the Location if
 * present.
 * Created by Pavitra on 11/14/2015.
 */
public class LocationController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "~!@#LocationCtlr: ";
    private static final long CONNECTION_FAILURE_RESOLUTION_REQUEST = 5000;
    private static final long UPDATE_INTERVAL = 10000; // 10 sec
    private static final long FASTEST_INTERVAL = 5000; // 5 sec
    private static final long DISPLACEMENT = 10; // 10 meters
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context mContext;
    private Location mLocation;
    private ILocationCallBacks mInterface;
    private Geocoder mGeocoder;
    private List<Address> addressesList;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private boolean mRequestingLocationUpdates;

    /**
     * Constructor overloading for setting up LocationController in Fragment.
     *
     * @param mContext
     * @param fragment
     */
    public LocationController(Context mContext, Fragment fragment) {
        try {
            this.mContext = mContext;
            mInterface = (ILocationCallBacks) fragment;
            mRequestingLocationUpdates = false;
            mLastUpdateTime = "";
            // build GoogleApiClient
            buildGoogleApiClient();
        } catch (Exception e) {
        }
    }


    /**
     * Constructor overloading for using LocationController in Activity
     *
     * @param mContext
     * @param activity
     */
    public LocationController(Context mContext, Activity activity) {
        try {
            this.mContext = mContext;
            mInterface = (ILocationCallBacks) activity;
            mRequestingLocationUpdates = false;
            mLastUpdateTime = "";
            // build GoogleApiClient
            buildGoogleApiClient();
        } catch (Exception e) {
        }
    }


    /**
     * Method to set up the Google Apis for {@link GoogleApiClient} and {@link LocationRequest}
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    /**
     * setup Location Requests
     */
    private synchronized void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Connect to the GoogleApiClient to start receiving Location updates
     */
    public void connectGoogleApi() {
        mGoogleApiClient.connect();
    }

    /**
     * Disconnect from GoogleApi
     */
    public void disconnectGoogleApi() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // get last known location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            handleNewLocation(location);
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true. Here, we check the value of mRequestingLocationUpdates and if it is true, we
        // start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Helper method to start Location updates.
     */
    public void startLocationUpdates() {
        Log.i(TAG, "starting location updates");
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Helper method to stop Location updates.
     */
    public void stopLocationUpdates() {
        Log.i(TAG, "stopping location updates");
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Private helper method to handle new Location and call the methods defined in the Interface.
     *
     * @param location
     */
    private void handleNewLocation(Location location) {

        try {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            // when the handleNewLocation method is handled, call the setCurrentLocation and setLocationArea.
            LatLng latLng = new LatLng(currentLatitude, currentLongitude);

            // pass the LatLng to interface
            mInterface.setLatLng(latLng);

            mInterface.setCurrentLocation(location);

            // handle the Geo Location.
            setLocationArea(currentLatitude, currentLongitude);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    /*
      IMP : Will be called when the Location is changed for the user depending on the update time.
     */
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(mContext, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * <p>
     * Sets the Feature Location name from the <tt>latitude</tt> and <tt>longitude</tt>. Returns <tt>null</tt> if Location is
     * not present.
     * </p>
     * <b>Requires network connection</b>
     *
     * @param latitude
     * @param longitude
     */
    private void setLocationArea(double latitude, double longitude) {
        mGeocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressesList = mGeocoder.getFromLocation(latitude, longitude, 1);
            String neighborhood = addressesList.get(0).getSubLocality();
            String locality = addressesList.get(0).getSubLocality();
            String knownName = addressesList.get(0).getFeatureName();

            StringBuilder sb = new StringBuilder();
            if (knownName != null) {
                sb.append(knownName);
            }
            if (neighborhood != null) {
                if (sb.length() > 0)
                    sb.append(", " + neighborhood);
                else
                    sb.append(neighborhood);
            }

            // finally pass it to the callback interface.
            mInterface.setLocationArea(sb.toString());
        } catch (IOException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Callback interface to get the current {@link Location}, {@link LatLng} and the {@link Geocoder} feature name/ Locality
     */
    public interface ILocationCallBacks {
        /**
         * Callback method to get the Last known location of Account using <tt>FusedLocationApi</tt>. Receives the Location
         * periodically.
         *
         * @param mLocation
         */
        public void setCurrentLocation(Location mLocation);


        /**
         * Callback to receive the {@link LatLng} object.
         *
         * @param mLatLng
         */
        public void setLatLng(LatLng mLatLng);

        /**
         * Callback method to get the Location Area depending on the Current Location.
         *
         * @param locationArea
         */
        public void setLocationArea(String locationArea);

    }
}
