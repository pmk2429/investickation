package com.sfsu.controllers;

import android.app.Activity;
import android.content.Context;
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

/**
 * Controller to perform all the Google Maps related operations including setting up GoogleMaps in MapView, setting the
 * InfoWindow on the location etc.
 * <p>
 * A GoogleMaps Controller to setup and initialize all the Google Map related operations and processes. LocationController
 * provides methods to setup Google Maps, display and render, verify the API KEY registered in the Google Dev Console and so on.
 * <p>
 * Created by Pavitra on 11/16/2015.
 */
public class GoogleMapController {
    private Context mContext;
    private String TAG = "~!@#$GMapCtrl :";
    private GoogleMap mGoogleMap;

    /**
     * Setting the Location change listener for the Maps
     */
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("Right Now")
                    .snippet("Population: 20,000"));

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
                // Updates the location and zoom of the MapView
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mUserLocation.getLatitude(),
//                        mUserLocation.getLongitude()), 10);
//                mGoogleMap.animateCamera(cameraUpdate);
                mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);

            } else {
                Log.d(TAG, "MapView is NULL");
            }
        }
    }
}
