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
import com.sfsu.entities.Observation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller to perform all the Google Maps related operations including setting up GoogleMaps in MapView, setting the
 * InfoWindow on the location etc.
 * <p/>
 * A GoogleMaps Controller to setup and initialize all the Google Map related operations and processes. LocationController
 * provides methods to setup Google Maps, display and render, verify the API KEY registered in the Google Dev Console and so on.
 * <p/>
 * Created by Pavitra on 11/16/2015.
 */
public class GoogleMapController implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    Map<Marker, Observation> markerObservationMap;
    private Context mContext;
    private String TAG = "~!@#$GMapCtrl :";
    private GoogleMap mGoogleMap;
    private LatLng mCurrentLatLng;
    private Location mLocation;
    private IMarkerClickCallBack mInterface;
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
            mInterface = (IMarkerClickCallBack) fragment;
            // build GoogleApiClient
            markerObservationMap = new HashMap<>();
        } catch (Exception e) {
        }
    }

    /**
     * Default constructor
     *
     * @param mContext
     */
    public GoogleMapController(Context mContext) {
        try {
            this.mContext = mContext;
            markerObservationMap = new HashMap<>();
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
            markerObservationMap = new HashMap<>();
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
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                mGoogleMap.getUiSettings().setCompassEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
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
                Log.i(TAG, "MapView is NULL");
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
     * Instantiates a new Polyline object on {@link GoogleMap} and adds points to define a rectangle.
     *
     * @param latLngs Array of {@link LatLng}
     */
    public void setUpPolylineOnMap(ArrayList<Observation> mObservationsList) {
        try {
            // build LatLngs array from ObservationListFragment
            LatLng[] mLatLngs = getLatLngArray(mObservationsList);
            moveCameraToPosition(mLatLngs[0]);

            PolylineOptions drawOptions = new PolylineOptions().width(7).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < mLatLngs.length; i++) {
                drawOptions.add(mLatLngs[i]);
            }
            // Get back the mutable Polyline
            mGoogleMap.addPolyline(drawOptions);
            // finally show marker
            showMarker(mObservationsList);
        } catch (Exception e) {

        }
    }


    /**
     * Get LatLng Array from ArrayList of Observations
     *
     * @param mObservationsList
     * @return
     */
    private LatLng[] getLatLngArray(ArrayList<Observation> mObservationsList) {
        LatLng[] mLatLngs = new LatLng[mObservationsList.size()];
        for (int i = 0; i < mObservationsList.size(); i++) {
            mLatLngs[i] = new LatLng(mObservationsList.get(i).getLocation().getLatitude(), mObservationsList.get(i)
                    .getLocation().getLongitude());
        }
        return mLatLngs;
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
                mMarkerOptions.title(i + "Random Text").snippet("");

                Marker mMarker = mGoogleMap.addMarker(mMarkerOptions);
            }
        } else {
            mMarkerOptions.position(mCurrentLatLng);
            // once the Markers are all set, display the title and the snippet.
            mMarkerOptions.title("It works")
                    .snippet("Population: 20,000");

            Marker mMarker = mGoogleMap.addMarker(mMarkerOptions);
        }

    }


    /**
     * Method to display marker on GoogleMaps for all the {@link Observation}. It is used to display all the
     * {@link com.sfsu.entities.Observation} of an {@link com.sfsu.entities.Activities}.
     */
    public void showMarker(List<Observation> mObservationsList) {
        MarkerOptions mMarkerOptions = new MarkerOptions();
        if (mObservationsList != null || mObservationsList.size() > 0) {

            for (int i = 0; i < mObservationsList.size(); i++) {
                // get the Observation
                Observation mObservation = mObservationsList.get(i);

                LatLng mLatLng = new LatLng(mObservation.getLocation().getLatitude(), mObservation.getLocation().getLongitude());

                mMarkerOptions.position(mLatLng);

                // once the Markers are all set, display the title and the snippet.
                mMarkerOptions.title(mObservation.getTickName()).snippet(mObservation.getGeoLocation());

                Marker mMarker = mGoogleMap.addMarker(mMarkerOptions);

                // add Marker and Observation to HashMap
                markerObservationMap.put(mMarker, mObservation);
                Log.i(TAG, markerObservationMap.size() + "");

                //mGoogleMap.setOnInfoWindowClickListener(this);
                mGoogleMap.setOnMarkerClickListener(this);
            }
        } else {
            mMarkerOptions.position(mCurrentLatLng);
            // once the Markers are all set, display the title and the snippet.
            mMarkerOptions.title("You are here").snippet("No data");

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


    @Override
    public boolean onMarkerClick(Marker marker) {

        try {
            // when the user clicks the Marker, get the Observation and pass it on to Callback
            //mInterface.onMarkerClickListener(marker);
            Observation mObservation = markerObservationMap.get(marker);
            mInterface.onMarkerClickObservationListener(mObservation);
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }

        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /**
     * Callback interface for {@link Marker} onClick Listener.
     */
    public interface IMarkerClickCallBack {
        void onMarkerClickListener(Marker marker);

        void onMarkerClickObservationListener(Observation mObservation);
    }
}
