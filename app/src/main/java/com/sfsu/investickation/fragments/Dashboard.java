package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sfsu.adapters.RecentActivitiesAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.TickDao;
import com.sfsu.entities.Activities;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * The dashboard of the application. Displays total number of {@link com.sfsu.entities.Activities} and {@link com.sfsu.entities
 * .Observation}. Also, provides a starting point for the user to post an Observation or to start an Activity.
 */
public class Dashboard extends Fragment implements View.OnClickListener {

    public final String TAG = "~!@#Dashboard";
    private IDashboardCallback mListener;
    private CardView btn_action;
    private RelativeLayout relativeLayoutDashboard;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbarMain;
    private int mCurrentSelectedPosition;
    private NavigationView mNavigationView;
    private Context mContext;
    private List<Activities> mActivitiesList;
    private RecentActivitiesAdapter mActivitiesAdapter;
    private ListView mListViewActivities;
    private DatabaseDataController dbTickController;

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: make a network call and get Activity as well as Observation count
        BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
        dbTickController = new DatabaseDataController(mContext, TickDao.getInstance());
        //BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
        //BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mListViewActivities = (ListView) v.findViewById(R.id.listView_dashboard_recentActivities);

        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        // set the button in Dashboard to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        // when the use clicks the entire relativeLayout, redirect to the appropriate call action
        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_observationCount);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_activityCount);
        relativeLayoutDashboard.setOnClickListener(this);

        // enable Location at the start
        //enableLocation();


        return v;
    }

    /**
     * Helper method to enable GPS at the start of the Dashboard.
     */
    private void enableLocation() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setMessage(mContext.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(mContext.getResources().getString(R.string.open_location_settings),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(myIntent);
                        }
                    });
            dialog.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }
    }


    /**
     * Subscribes to the event of successful loading of Activities. Initializes Adapter and sets the data to Adapter.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivitiesLoadSuccess(ActivityEvent.OnListLoaded onLoaded) {
        mActivitiesList = onLoaded.getResponseList();

        mActivitiesAdapter = new RecentActivitiesAdapter(mContext, mActivitiesList);

        mListViewActivities.setAdapter(mActivitiesAdapter);
        mActivitiesAdapter.setNotifyOnChange(true);
        mActivitiesAdapter.notifyDataSetChanged();


        // get the selected Activity and open ActivityDetail page.
        mListViewActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activities mActivity = (Activities) mListViewActivities.getItemAtPosition(position);
                mListener.onActivityItemClicked(mActivity);
            }
        });

    }

    /**
     * Subscribes to event of failure in loading Activities.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onActivitiesLoadFailure(ActivityEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //This MUST be done before saving any of your own or your base class's variables
        final Bundle mapViewSaveState = new Bundle(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onResume() {
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
         * Callback method when the <tt>'POST OBSERVATION'</tt> button is clicked in {@link Dashboard}. Depending on User's
         * choice, start an {@link ActivityNew} fragment or directly {@link AddObservation} fragment.
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

        /**
         * Callback method when the user clicks item in Activities ListView in {@link Dashboard}.
         *
         * @param mActivity
         */
        public void onActivityItemClicked(Activities mActivity);
    }

}
