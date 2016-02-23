package com.sfsu.investickation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.ActivityDetailFragment;
import com.sfsu.investickation.fragments.ActivityListFragment;
import com.sfsu.investickation.fragments.ActivityMapFragment;
import com.sfsu.investickation.fragments.ActivityNewFragment;
import com.sfsu.investickation.fragments.ActivityRunningFragment;
import com.sfsu.investickation.fragments.PostActivitiesListFragment;
import com.sfsu.network.bus.BusProvider;

import java.util.ArrayList;

/**
 * <tt>UserActivityMasterActivity</tt> is the parent activity and the holding container for all the Activity related fragments.
 * This activity provides the DB access calls, network calls, initializing the controllers, passing the data to the Fragments
 * and so on. All the Activity related operations are carried out in UserActivityMasterActivity.
 * <p/>
 * This Activity implements the ConnectionCallbacks for its child Fragments which provides listener methods to these Fragments.
 */
public class UserActivityMasterActivity extends MainBaseActivity implements ActivityListFragment.IActivityCallBacks,
        ActivityDetailFragment.IActivityDetailsCallBacks,
        ActivityNewFragment.IActivityNewCallBack,
        ActivityRunningFragment.IActivityRunningCallBacks,
        ActivityMapFragment.IActivityMapCallBack {

    public static final String KEY_ACTIVITY_ADD_OBS = "add_new_observation_from_activity";
    public static final String KEY_ACTIVITY_ID = "ongoing_activity_id";
    public static final String KEY_ACTIVITY_DETAILS = "selected_activity";
    public static final String EDITOR_ONGOING_ACTIVITY = "editor_ongoing_activity";
    public static final String EDITOR_ACTIVITY_DETAILS = "editor_activity_details";
    public static final String PREF_ACTIVITY_DATA = "pref_ongoing_activity";
    //
    public static final String KEY_VIEW_OBSERVATIONS = "view_all_activity_observations";

    public static final String KEY_NEW_ACTIVITY_OBJECT = "new_activity_object";
    public static final String KEY_REMINDER_SET = "tick_reminder_set";
    public static final String KEY_REMINDER_INTERVAL = "reminder_interval";

    // count to maintain the Stack in the UserActivityMasterActivity for all the Fragments.
    private final String TAG = "~!@#$UserActivity";
    private FragmentManager fragmentManager;
    // for performing better navigation on back press.
    private ActivityRunningFragment mActivityRunningFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        try {
            fragmentManager = getSupportFragmentManager();

            // if Fragment container is present
            if (findViewById(R.id.activity_fragment_container) != null) {

                // if we are restored from the previous state, just return
                if (savedInstanceState != null) {
                    return;
                } else {
                    // if user clicks on Start Activity in DashboardFragment
                    if (getIntent().getIntExtra(MainActivity.KEY_ADD_ACTIVITY, 0) == 1) {
                        ActivityNewFragment activityNewFragment = new ActivityNewFragment();
                        performAddFragmentTransaction(activityNewFragment);
                    }
                    // if user clicks on ActivityListFragment in DashboardFragment
                    else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_ACTIVITY_LIST, 0) == 2) {
                        ActivityListFragment activityListFragment = new ActivityListFragment();
                        performAddFragmentTransaction(activityListFragment);
                    }
                    // if user navigates back to ActivityRunningFragment fragment.
                    else if (getIntent().getIntExtra(ObservationMasterActivity.KEY_BACK_TO_ACTIVITY_RUNNING, 0) == 11) {
                        mActivityRunningFragment = new ActivityRunningFragment();
                        performReplaceFragmentTransaction(mActivityRunningFragment);
                    }
                    // if user navigates back to ActivityDetailFragment fragment.
                    else if (getIntent().getIntExtra(ObservationMasterActivity.KEY_BACK_TO_ACTIVITY_DETAILS, 0) == 11) {
                        ActivityDetailFragment mActivityDetailFragment = new ActivityDetailFragment();
                        performAddFragmentTransaction(mActivityDetailFragment);
                    }
                    // if user opens Activity by clicking on the ListView item from DashboardFragment.
                    else if (getIntent().getIntExtra(MainActivity.KEY_OPEN_SELECTED_ACTIVITY, 0) == 24) {
                        Activities mActivities = getIntent().getParcelableExtra(MainActivity.KEY_VIEW_ACTIVITY);
                        ActivityDetailFragment mActivityDetailFragment = ActivityDetailFragment.newInstance(mActivities);
                        performAddFragmentTransaction(mActivityDetailFragment);
                    }
                    // open List of Activities by default.
                    else {
                        ActivityListFragment activityListFragment = new ActivityListFragment();
                        performAddFragmentTransaction(activityListFragment);
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Helper method to set the Fragment transaction for current fragment.
     *
     * @param mFragment
     */
    private void performReplaceFragmentTransaction(Fragment mFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.activity_fragment_container, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Helper method to set the Fragment transaction for current fragment.
     *
     * @param mFragment
     */
    private void performAddFragmentTransaction(Fragment mFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.activity_fragment_container, mFragment);
        transaction.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle args = new Bundle();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        Fragment mFragment = fragmentManager.findFragmentById(R.id.activity_fragment_container);

        if (mFragment instanceof ActivityRunningFragment) {
            // disable back press for ActivityRunningFragment fragment
        } else {
            if (count == 0) {
                Intent homeIntent = new Intent(UserActivityMasterActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            } else if (count > 0) {
                super.onBackPressed();
                fragmentManager.popBackStackImmediate();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onActivitiesListItemClickListener(Activities mActivity) {
        ActivityDetailFragment mActivityDetailFragment = ActivityDetailFragment.newInstance(mActivity);
        performReplaceFragmentTransaction(mActivityDetailFragment);
    }


    @Override
    public void onActivityAddListener() {
        // if user clicked the Add Button, replace with ActivityNewFragment Fragment
        ActivityNewFragment addActivityFragment = new ActivityNewFragment();
        performReplaceFragmentTransaction(addActivityFragment);
    }

    @Override
    public void onUploadListOfActivities() {
        PostActivitiesListFragment postActivitiesListFragment = new PostActivitiesListFragment();
        performReplaceFragmentTransaction(postActivitiesListFragment);
    }

    @Override
    public void onPlayButtonClick(Bundle activityBundle) {

        if (activityBundle != null) {
            // passes the Newly created object to the ActivityRunningFragment fragment.
            ActivityRunningFragment mActivityRunningFragment = ActivityRunningFragment.newInstance(activityBundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            // dont add to backstack
            transaction.replace(R.id.activity_fragment_container, mActivityRunningFragment);
            transaction.commit();
        }
    }

    @Override
    public void onActivityStopButtonClicked() {
        ActivityListFragment mActivityListFragment = new ActivityListFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // dont add to backstack
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.activity_fragment_container, mActivityListFragment);
        transaction.commit();
    }

    /*
     * The passed activityId will be used to make Observations for current Activity.
     */
    @Override
    public void onAddNewObservationClicked(String activityId) {
        try {
            // create and intent and open the AddObservationFragment fragment to add Observation.
            Intent addObservationIntent = new Intent(UserActivityMasterActivity.this, ObservationMasterActivity.class);
            // put the extras in addObservationIntent to perform fragment Transaction efficiently.
            addObservationIntent.putExtra(KEY_ACTIVITY_ADD_OBS, 1);
            addObservationIntent.putExtra(KEY_ACTIVITY_ID, activityId);
            startActivity(addObservationIntent);
            finish();
        } catch (Exception e) {
        }
    }


    @Override
    public void onViewAllObservationsClicked(String activityId) {
        try {
            // open ObservationListFragment Fragment.
            Log.i(TAG, "onViewAllObservationsClicked: ");
            Intent observationListIntent = new Intent(UserActivityMasterActivity.this, ObservationMasterActivity.class);
            observationListIntent.putExtra(KEY_VIEW_OBSERVATIONS, 1);
            observationListIntent.putExtra(KEY_ACTIVITY_ID, activityId);
            startActivity(observationListIntent);
            finish();
        } catch (Exception e) {
        }
    }

    @Override
    public void onOpenActivitiesMapClicked(ArrayList<Observation> mObservationList) {
        try {
            ActivityMapFragment mActivityMapFragment = ActivityMapFragment.newInstance(mObservationList);
            performReplaceFragmentTransaction(mActivityMapFragment);
        } catch (Exception e) {
        }
    }
}
