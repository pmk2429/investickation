package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.investickation.MainActivity;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.SettingsActivity;
import com.sfsu.investickation.TickGuideMasterActivity;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.utils.AppUtils;


public class Dashboard extends Fragment implements View.OnClickListener {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private final String LOGTAG = "~!@#Dashboard :";
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

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);


        // lot of code for setting up NavDrawer so calling a method inssted for keeping onCreate free from clutter
        // setup the Toolbar for this Fragment.
        setActionBarAndNavDrawer(v);


        final ImageView imageView = (ImageView) v.findViewById(R.id.imageView_dashboardHeader);


        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        // when the use clicks the entire relativelayout, redirect to the appropriate call action
        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_observation);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_activity);
        relativeLayoutDashboard.setOnClickListener(this);

        if (new AppUtils(getActivity()).isLocationEnabled()) {
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
            Log.d(LOGTAG, "Map null");
        }

        return v;
    }

    /**
     * Method to setup ActionBar, NavigationDrawer.
     *
     * @param v
     */

    private void setActionBarAndNavDrawer(View v) {

        toolbarMain = (Toolbar) v.findViewById(R.id.toolbar_dashboard_scrollable);

        if (toolbarMain != null) {
            ((MainActivity) getActivity()).setSupportActionBar(toolbarMain);

            // get the ActionBar and set the Menu icon.
            final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            // initialize the DrawerLayout
            mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

            // set the onClick Listener for the Drawer click event
            toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // initialize the NavigationView and also setup OnClickListener for each of the Items in list
        mNavigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
        }

        // set the Title to CollapsingToolbar
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar_dashboard);
        collapsingToolbar.setTitle("InvesTICKations");
    }

    // method to setup Navigation Drawer item click Listener
    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent;

        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 0;
                    break;

                case R.id.nav_activities:
                    intent = new Intent(mContext, UserActivityMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 1;
                    break;

                case R.id.nav_observations:
                    intent = new Intent(mContext, ObservationMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 2;
                    break;

                case R.id.navigation_ticksData:
                    intent = new Intent(mContext, TickGuideMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 3;
                    break;

                case R.id.nav_settings:
                    intent = new Intent(mContext, SettingsActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 4;
                    break;
            }
        } catch (Exception e) {
            Log.d(LOGTAG, e.getMessage());
        }

        // Hihhlight the selected item and close the drawer
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOGTAG, " inside on items selected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(LOGTAG, " drawer selected");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                Log.i(LOGTAG, " itemId: " + item.getItemId());
        }


        return super.onOptionsItemSelected(item);

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
