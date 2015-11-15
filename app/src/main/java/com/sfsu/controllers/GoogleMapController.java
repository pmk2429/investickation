package com.sfsu.controllers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.utils.AppUtils;

/**
 * A GoogleMaps Controller to setup and initialize all the Google Map related operations and processes. GoogleMapController
 * provides methods to setup Google Maps, display and render, verify the API KEY registered in the Dev Console and so on.
 * <p/>
 * GoogleMapController also consists of a Callback Interface to
 * Created by Pavitra on 11/14/2015.
 */
public class GoogleMapController {

    private GoogleMap mGoogleMap;
    private Context mContext;

    public GoogleMapController(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Setups the Google Map and locates the current User Location.
     */
    public void setupGoogleMap(MapView mapView) {
        // Gets to GoogleMap from the MapView and does initialization stuff
        mGoogleMap = mapView.getMap();

        if (mGoogleMap != null) {
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.setMyLocationEnabled(true);
            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.773972, -122.431297), 10);
            mGoogleMap.animateCamera(cameraUpdate);
        } else {
            Log.d(AppUtils.LOGTAG, "Map null");
        }
    }

    /**
     * Returns the current location of the User.
     *
     * @return
     */
    public Location getCurrentLocation() {
        return null;
    }
}
