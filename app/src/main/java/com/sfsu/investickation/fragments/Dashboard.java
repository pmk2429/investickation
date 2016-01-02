package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;

/**
 * The dashboard of the application. Displays total number of {@link com.sfsu.entities.Activities} and {@link com.sfsu.entities
 * .Observation}. Also, provides a starting point for the user to post an Observation or to start an Activity.
 */
public class Dashboard extends Fragment implements View.OnClickListener {

    public final String TAG = "~!@#Dashboard";
    private IDashboardCallback mListener;
    private CardView btn_action;
    private RelativeLayout relativeLayoutDashboard;
    private MapView mapView;
    private GoogleMap googleMap;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbarMain;
    private int mCurrentSelectedPosition;
    private NavigationView mNavigationView;
    private Context mContext;
    private GoogleMapController mGoogleMapController;

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: make a network call and get Activity as well as Observation count

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        mGoogleMapController = new GoogleMapController(mContext, this);

        // when the use clicks the entire relativeLayout, redirect to the appropriate call action
        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_observationCount);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_activityCount);
        relativeLayoutDashboard.setOnClickListener(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityDashboard);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);

        // connect to GoogleAPI and setup FusedLocationService to get the Location updates.

        // setup the Google Maps in MapView.
        mGoogleMapController.setupGoogleMap(mapView);

        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //This MUST be done before saving any of your own or your base class's variables
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
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
            mContext = activity;
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
        } else if (v.getId() == R.id.relativeLayout_dashboard_observationCount) {
            mListener.onViewObservationsClicked();
        } else if (v.getId() == R.id.relativeLayout_dashboard_activityCount) {
            mListener.onViewActivitiesClicked();
        }
    }


    /**
     * Callback Interface to get the callbacks from the Dashboard fragment.
     */
    public interface IDashboardCallback {

        /**
         * Callback method when the <tt>'START ACTIVITY'</tt> button is clicked in {@link Dashboard}.
         */
        public void onActivityButtonClicked();

        /**
         * Callback method when the <tt>'POST OBSERVATION'</tt> button is clicked in {@link Dashboard}.
         */
        public void onObservationButtonClicked();

        /**
         * Callback method when the <tt>'VIEW ACTIVITIES'</tt> button is clicked in {@link Dashboard}.
         */
        public void onViewActivitiesClicked();

        /**
         * Callback method when the <tt>'VIEW OBSERVATIONS'</tt> button is clicked in {@link Dashboard}.
         */
        public void onViewObservationsClicked();
    }

}
