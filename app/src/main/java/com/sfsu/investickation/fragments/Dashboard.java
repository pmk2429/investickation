package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.entities.AppConfig;
import com.sfsu.investickation.R;


public class Dashboard extends Fragment implements View.OnClickListener {

    private IDashboardCallback mListener;
    private CardView btn_action;
    private RelativeLayout relativeLayoutDashboard;
    private MapView mapView;
    private GoogleMap googleMap;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_observation);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_activity);
        relativeLayoutDashboard.setOnClickListener(this);

        if (new AppConfig(getActivity()).isLocationEnabled()) {
        }

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityDashboard);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization of googleMap Object.
        googleMap = mapView.getMap();

        if (googleMap != null) {
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.773972, -122.431297), 10);
            googleMap.animateCamera(cameraUpdate);
        } else {
            Log.d(AppConfig.LOGSTRING, "Map null");
        }

        return v;
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IDashboardCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDashboardCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_activity_start) {
            mListener.onActivityButtonClicked();
        } else if (v.getId() == R.id.btn_observation_post) {
            mListener.onObservationButtonClicked();
        } else if (v.getId() == R.id.relativeLayout_observation) {
            mListener.onViewObservationsClicked();
        } else if (v.getId() == R.id.relativeLayout_activity) {
            mListener.onViewActivitiesClicked();
        }
    }


    public interface IDashboardCallback {
        public void onDashboardInteraction(Uri uri);

        public void onActivityButtonClicked();

        public void onObservationButtonClicked();

        public void onViewActivitiesClicked();

        public void onViewObservationsClicked();
    }

}
