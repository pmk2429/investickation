package com.sfsu.investickation.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sfsu.adapters.RecentActivitiesAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.TickDao;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.PermissionUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * The dashboard of the application. Displays total number of {@link com.sfsu.entities.Activities} and {@link com.sfsu.entities
 * .Observation}. Also, provides a starting point for the user to post an Observation or to start an Activity.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NETWORK_LOCATION_BASED_PERMISSIONS = 24;
    public final String TAG = "~!@#DashboardFragment";
    private IDashboardCallback mListener;
    private CardView btn_action;
    private RelativeLayout relativeLayoutDashboard;
    private LinearLayout linearLayoutRecentActivities;
    private DrawerLayout mDrawerLayout;
    private TextView txtView_activitiesCount, txtView_observationCount;
    private Context mContext;
    private List<Activities> mActivitiesList;
    private RecentActivitiesAdapter mActivitiesAdapter;
    private ListView mListViewActivities;
    private DatabaseDataController dbTickController;
    private PermissionUtils mPermissionUtils;
    private boolean FLAG_PERMISSION;
    private SharedPreferences settingsPref;
    // TODO: change this to get the count from Settings SharedPref
    private int activitiesCount = 2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbTickController = new DatabaseDataController(mContext, TickDao.getInstance());
        mPermissionUtils = new PermissionUtils(mContext);
        if (mPermissionUtils.isCoarseLocationPermissionApproved() && mPermissionUtils.isFineLocationPermissionApproved()) {
            // fire event to get the total count of Activities and Observations made by the user
            BusProvider.bus().post(new UserEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ACT_OBS_COUNT));
        } else {
            askForPermission();
        }

        settingsPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        activitiesCount = Integer.parseInt(settingsPref.getString(mContext.getString(R.string.KEY_PREF_ACTIVITIES_COUNT), "2"));
    }

    /**
     * Prompts for all the network and location related permissions
     */

    private void askForPermission() {
        int hasInternetPermission = 0;
        int hasAccessLocationPermission = 0;
        int hasFineLocationPermission = 0;
        int hasWiFiPermission = 0;
        int hasNetworkPermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasAccessLocationPermission = mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            hasFineLocationPermission = mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            hasWiFiPermission = mContext.checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE);
            hasInternetPermission = mContext.checkSelfPermission(Manifest.permission.INTERNET);
            hasNetworkPermission = mContext.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

            List<String> permissions = new ArrayList<>();
            if (hasAccessLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (hasWiFiPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            }

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }

            if (hasNetworkPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }


            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), NETWORK_LOCATION_BASED_PERMISSIONS);
            }
        } else {
            Log.i(TAG, "askForPermission: not working");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<Integer> permissionsApprovalList = new ArrayList<>();
        switch (requestCode) {
            case NETWORK_LOCATION_BASED_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissionUtils.setPermission(PermissionUtils.COARSE_LOCATION);
                        mPermissionUtils.setPermission(PermissionUtils.FINE_LOCATION);
                        mPermissionUtils.setPermission(PermissionUtils.INTERNET);
                        mPermissionUtils.setPermission(PermissionUtils.WIFI);
                        mPermissionUtils.setPermission(PermissionUtils.NETWORK);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_RECENT_ACTIVITIES));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mListViewActivities = (ListView) v.findViewById(R.id.listView_dashboard_recentActivities);

        // set the button in DashboardFragment to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        // set the button in DashboardFragment to the corresponding action
        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        // when the use clicks the entire relativeLayout, redirect to the appropriate call action
        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_observationCount);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_dashboard_activityCount);
        relativeLayoutDashboard.setOnClickListener(this);


        txtView_activitiesCount = (TextView) v.findViewById(R.id.textView_dashboard_activityCount);
        txtView_observationCount = (TextView) v.findViewById(R.id.textView_dashboard_observationCount);

        linearLayoutRecentActivities = (LinearLayout) v.findViewById(R.id.linearLayout_dashboard_recentActivitiesl);
        // make the linear layout invisible - DEFAULT
        linearLayoutRecentActivities.setVisibility(View.GONE);
        // enable Location at the start
        //enableLocation();

//        // Instantiate a ViewPager and a PagerAdapter.
//        mPager = (ViewPager) v.findViewById(R.id.viewPager_dashboard_info);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
//        mPager.setAdapter(mPagerAdapter);

        return v;
    }

    /**
     * Helper method to enable GPS at the start of the DashboardFragment.
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Subscribes to the event of successful loading of Activities. Initializes Adapter and sets the data to Adapter.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivitiesLoadSuccess(ActivityEvent.OnListLoaded onLoaded) {
        mActivitiesList = onLoaded.getResponseList();

        if (mActivitiesList != null) {
            if (mActivitiesList.size() > 0) {
                // display the recent activities
                linearLayoutRecentActivities.setVisibility(View.VISIBLE);

                mActivitiesAdapter = new RecentActivitiesAdapter(mContext, mActivitiesList);

                mListViewActivities.setAdapter(mActivitiesAdapter);
                mActivitiesAdapter.setNotifyOnChange(true);
                mActivitiesAdapter.notifyDataSetChanged();

                // get the selected Activity and open ActivityDetailFragment page.
                mListViewActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Activities mActivity = (Activities) mListViewActivities.getItemAtPosition(position);
                        mListener.onActivityItemClicked(mActivity);
                    }
                });
            } else {
                // in case if User has not created any Activity on Server
                linearLayoutRecentActivities.setVisibility(View.GONE);
            }
        }
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

    @Subscribe
    public void onActivitiesAndObservationsCountsLoadedSuccess(UserEvent.OnCountsLoaded onCountsLoaded) {
        txtView_activitiesCount.setText(String.valueOf(onCountsLoaded.getResponse().activitiesCount));
        txtView_observationCount.setText(String.valueOf(onCountsLoaded.getResponse().observationsCount));
    }

    @Subscribe
    public void onActivitiesAndObservationsCountsLoadedFailure(UserEvent.OnLoadingError onLoadingError) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, onLoadingError.getErrorMessage());
    }

    /**
     * Callback Interface to get the callbacks from the DashboardFragment fragment.
     */
    public interface IDashboardCallback {

        /**
         * Callback method when the <tt>'START ACTIVITY'</tt> button is clicked in {@link DashboardFragment}.
         */
        public void onActivityButtonClicked();

        /**
         * Callback method when the <tt>'POST OBSERVATION'</tt> button is clicked in {@link DashboardFragment}. Depending on User's
         * choice, start an {@link ActivityNewFragment} fragment or directly {@link AddObservationFragment} fragment.
         */
        public void onObservationButtonClicked();

        /**
         * Callback method when the <tt>'VIEW ACTIVITIES'</tt> button is clicked in {@link DashboardFragment}.
         */
        public void onViewActivitiesClicked();

        /**
         * Callback method when the <tt>'VIEW OBSERVATIONS'</tt> button is clicked in {@link DashboardFragment}.
         */
        public void onViewObservationsClicked();

        /**
         * Callback method when the user clicks item in Activities ListView in {@link DashboardFragment}.
         *
         * @param mActivity
         */
        public void onActivityItemClicked(Activities mActivity);
    }

    /**
     * FIXME: NOT USED
     * Pager adapter that represents 4 {@link ScreenSlidePageFragment} objects, in sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}
