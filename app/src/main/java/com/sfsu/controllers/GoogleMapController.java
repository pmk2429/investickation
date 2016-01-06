package com.sfsu.controllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Controller to perform all the Google Maps related operations including setting up GoogleMaps in MapView, setting the
 * InfoWindow on the location etc.
 * <p/>
 * A GoogleMaps Controller to setup and initialize all the Google Map related operations and processes. LocationController
 * provides methods to setup Google Maps, display and render, verify the API KEY registered in the Google Dev Console and so on.
 * <p/>
 * Created by Pavitra on 11/16/2015.
 */
public class GoogleMapController {
    private Context mContext;
    private String TAG = "~!@#$GMapCtrl :";
    private GoogleMap mGoogleMap;
    private LatLng mCurrentLatLng;
    private Location mLocation;

    /**
     * Setting the Location change listener for the Maps
     */
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLocation = location;
            mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            moveCameraToPosition(mCurrentLatLng);
        }
    };

    /**
     * Constructor overloading for setting up LocationController in Fragment.
     *
     * @param mContext
     * @param fragment
     */
    public GoogleMapController(Context mContext, Fragment fragment) {
        try {
            this.mContext = mContext;
            // build GoogleApiClient
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }


    /**
     * Constructor overloading for using LocationController in Activity
     *
     * @param mContext
     * @param activity
     */
    public GoogleMapController(Context mContext, Activity activity) {
        try {
            this.mContext = mContext;
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Sets up the Google Map and locates the current Account Location.
     */
    public void setupGoogleMap(MapView mapView) {
        if (mapView != null) {
            // Gets to GoogleMap from the MapView and does initialization stuff
            mGoogleMap = mapView.getMap();
            if (mGoogleMap != null) {

                // enabled all the settings
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setCompassEnabled(true);

                mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
                mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
                mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
                mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
            } else {
                Log.d(TAG, "MapView is NULL");
            }
        }
    }

    /**
     * Instantiates a new Polyline object on {@link GoogleMap} and adds points to define a rectangle.
     *
     * @param latLngs Array of {@link LatLng}
     */
    public void setUpPolylineOnMap(LatLng[] latLngs) {
        moveCameraToPosition(latLngs[2]);
        PolylineOptions drawOptions = new PolylineOptions().width(7).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < latLngs.length; i++) {
            drawOptions.add(latLngs[i]);
        }
        // Get back the mutable Polyline
        mGoogleMap.addPolyline(drawOptions);
        showMarker(latLngs);
    }

    /**
     * Method to display marker on GoogleMaps for the {@link LatLng} specified.
     */
    public void showMarker(LatLng[] latLngs) {
        MarkerOptions mMarkerOptions = new MarkerOptions();
        if (latLngs != null || latLngs.length > 0) {
            for (int i = 0; i < latLngs.length; i++) {
                mMarkerOptions.position(latLngs[i]);
                // once the Markers are all set, display the title and the snippet.
                mMarkerOptions.title("Right Now")
                        .snippet("Population: 20,000");

                Marker mMarker = mGoogleMap.addMarker(mMarkerOptions);
            }
        } else {
            mMarkerOptions.position(mCurrentLatLng);
            // once the Markers are all set, display the title and the snippet.
            mMarkerOptions.title("Right Now")
                    .snippet("Population: 20,000");

            Marker mMarker = mGoogleMap.addMarker(mMarkerOptions);
        }

    }

    /**
     * Helper method to move the camera to the position passed as param. When this method is called, OnMyLocationChangeListener
     * is set to null.
     *
     * @param mLatLng
     */
    private void moveCameraToPosition(LatLng mLatLng) {
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.setOnMyLocationChangeListener(null);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 14.0f));
    }

    /**
     * Helper method to clear all the resources; GoogleMap, GoogleMapController and everything.
     */
    public void clear() {
        this.mGoogleMap = null;
        this.mContext = null;
        this.myLocationChangeListener = null;
    }


    /**
     * Returns the current {@link LatLng} based on the current Location.
     *
     * @return
     */
    public LatLng getMyCurrentLocation() {
        if (mCurrentLatLng == null) {
            return new LatLng(1.2, 3.4);
        } else {
            return mCurrentLatLng;
        }
    }
}
