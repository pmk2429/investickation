package com.sfsu.controllers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

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
 * <p/>
 * <tt>LocationController</tt> also provides Callback Interface to get the User's current Location and Featured name of the Location if
 * present.
 * Created by Pavitra on 11/14/2015.
 */
public class LocationController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "~!@#LocationCtlr: ";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context mContext;
    private Location mLocation;
    private ILocationCallBacks mInterface;
    private Geocoder mGeocoder;
    private List<Address> addressesList;

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
            // build GoogleApiClient
            buildGoogleApiClient();
        } catch (Exception e) {
        }
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

    /**
     * Method to set up the Google Apis
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
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


            mInterface.setCurrentLocation(location);
            setLocationArea(currentLatitude, currentLongitude);
        } catch (Exception e) {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
    }


    /**
     * Sets the Feature Location name from the <tt>latitude</tt> and <tt>longitude</tt>. Returns <tt>null</tt> if Location is
     * not present
     *
     * @param latitude
     * @param longitude
     */
    private void setLocationArea(double latitude, double longitude) {
        mGeocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            addressesList = mGeocoder.getFromLocation(latitude, longitude, 1);
            String city = addressesList.get(0).getLocality();
//        String state = addressesList.get(0).getAdminArea();
//        String country = addressesList.get(0).getCountryName();
//        String postalCode = addressesList.get(0).getPostalCode();
            String knownName = addressesList.get(0).getFeatureName();

            if (knownName.equals(null)) {
                // if the area/feature name cannot be found, then pass the City name.
                mInterface.setLocationArea(city);
            }
            mInterface.setLocationArea(knownName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Callback interface to get the current Location
     */
    public interface ILocationCallBacks {
        /**
         * Callback method to get the Last known location of User using <tt>FusedLocationApi</tt>. Receives the Location
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
