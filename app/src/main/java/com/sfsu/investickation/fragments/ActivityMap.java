package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.investickation.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMap extends Fragment {

    private static final String TAG = "`!@#$ActivityMap";
    private static String KEY_ARGS_ACTIVITY_ID = "activity_id";
    @Bind(R.id.mapView_activitiesMap_main)
    MapView mMapView;
    private GoogleMapController mGoogleMapController;
    private Context mContext;
    private IActivityMapCallBack mListener;
    private String activityId;

    public ActivityMap() {
        // Required empty public constructor
    }


    /**
     * Factory method to create new {@link ActivityMap} instance.
     *
     * @param activityId
     * @return
     */
    public static ActivityMap newInstance(String activityId) {
        ActivityMap fragment = new ActivityMap();
        Bundle args = new Bundle();
        args.putString(KEY_ARGS_ACTIVITY_ID, activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityId = getArguments().getString(KEY_ARGS_ACTIVITY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities_map, container, false);

        ButterKnife.bind(this, rootView);

        mGoogleMapController = new GoogleMapController(mContext, this);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mMapView.onCreate(mapViewSavedInstanceState);

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mMapView);

        // once the GoogleMap is setup, add polyline to the Maps.
        LatLng[] latLngs = new LatLng[]{new LatLng(40.737102, -73.990318), new LatLng(40.749825, -73.987963), new LatLng(40.752946, -73.987384),
                new LatLng(40.755823, -73.986397)};
        mGoogleMapController.setUpPolylineOnMap(latLngs);

        LatLng mLatLng = mGoogleMapController.getMyCurrentLocation();
        Log.i(TAG, mLatLng.latitude + " : " + mLatLng.longitude);

        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final Bundle mapViewSaveState = new Bundle(outState);
        mMapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IActivityMapCallBack) {
            mListener = (IActivityMapCallBack) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IObservationMapCallBack interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContext = null;
        mGoogleMapController = null;
        mMapView = null;
    }

    /**
     * Callback interface to handle the onClick of the button in {@link ActivityMap} Fragment.
     */
    public interface IActivityMapCallBack {

    }
}