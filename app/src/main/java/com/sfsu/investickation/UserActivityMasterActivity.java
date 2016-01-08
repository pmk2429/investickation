package com.sfsu.investickation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.ActivityDetail;
import com.sfsu.investickation.fragments.ActivityList;
import com.sfsu.investickation.fragments.ActivityMap;
import com.sfsu.investickation.fragments.ActivityNew;
import com.sfsu.investickation.fragments.ActivityRunning;
import com.sfsu.network.bus.BusProvider;

import java.util.ArrayList;

/**
 * <tt>UserActivityMasterActivity</tt> is the parent activity and the holding container for all the Activity related fragments.
 * This activity provides the DB access calls, network calls, initializing the controllers, passing the data to the Fragments
 * and so on. All the Activity related operations are carried out in UserActivityMasterActivity.
 * <p/>
 * This Activity implements the ConnectionCallbacks for its child Fragments which provides listener methods to these Fragments.
 */
public class UserActivityMasterActivity extends MainBaseActivity implements ActivityList.IActivityCallBacks, ActivityDetail.IActivityDetailsCallBacks, ActivityNew.IActivityNewCallBack, ActivityRunning.IActivityRunningCallBacks, ActivityMap.IActivityMapCallBack {

    public static final String KEY_ACTIVITY_ADD_OBS = "add_new_observation_from_activity";
    public static final String KEY_ACTIVITY_ID = "ongoing_activity_id";
    public static final String KEY_ACTIVITY_DETAILS = "selected_activity";
    public static final String KEY_RUNNING_ACTIVITY = "ongoing_activity";
    public static final String EDITOR_ONGOING_ACTIVITY = "editor_ongoing_activity";
    public static final String EDITOR_ACTIVITY_DETAILS = "editor_activity_details";
    public static final String PREF_ACTIVITY_DATA = "pref_ongoing_activity";
    //
    public static final String KEY_VIEW_OBSERVATIONS = "view_all_activity_observations";
    private final String TAG = "~!@#$UserActivity";
    private ActivityRunning mActivityRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        // if Fragment container is present
        if (findViewById(R.id.activity_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            } else {
                // if user clicks on Start Activity
                if (getIntent().getIntExtra(MainActivity.KEY_ADD_ACTIVITY, 0) == 1) {
                    ActivityNew activityNewFragment = new ActivityNew();
                    performReplaceFragmentTransaction(activityNewFragment);
                }
                // if user clicks on ActivityList
                else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_ACTIVITY_LIST, 0) == 2) {
                    ActivityList activityListFragment = new ActivityList();
                    performAddFragmentTransaction(activityListFragment);
                }
                // if user navigates back to ActivityRunning fragment.
                else if (getIntent().getIntExtra(ObservationMasterActivity.KEY_BACK_TO_ACTIVITY_RUNNING, 0) == 11) {
                    mActivityRunning = new ActivityRunning();
                    performReplaceFragmentTransaction(mActivityRunning);
                }
                // if user navigates back to ActivityDetail fragment.
                else if (getIntent().getIntExtra(ObservationMasterActivity.KEY_BACK_TO_ACTIVITY_DETAILS, 0) == 11) {
                    ActivityDetail mActivityDetail = new ActivityDetail();
                    performAddFragmentTransaction(mActivityDetail);
                }
                // if user opens Activity by clicking on the ListView item from Dashboard.
                else if (getIntent().getIntExtra(MainActivity.KEY_OPEN_SELECTED_ACTIVITY, 0) == 1) {
                    Activities mActivities = getIntent().getParcelableExtra(MainActivity.KEY_VIEW_ACTIVITY);
                    ActivityDetail mActivityDetail = ActivityDetail.newInstance(mActivities);
                    performAddFragmentTransaction(mActivityDetail);
                }
                // open List of Activities by default.
                else {
                    ActivityList activityListFragment = new ActivityList();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.activity_fragment_container, activityListFragment);
                    transaction.commit();
                }
            }
        }
    }

    /**
     * Helper method to set the Fragment transaction for current fragment.
     *
     * @param mFragment
     */
    private void performReplaceFragmentTransaction(Fragment mFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_fragment_container, mFragment);
        transaction.addToBackStack(null);
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
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(UserActivityMasterActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
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
        ActivityDetail mActivityDetailFragment = ActivityDetail.newInstance(mActivity);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_fragment_container, mActivityDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onActivityAddListener() {
        BusProvider.bus().unregister(ActivityList.class);
        // if user clicked the Add Button, replace with AddObservation Fragment
        ActivityNew addActivityFragment = new ActivityNew();
        performReplaceFragmentTransaction(addActivityFragment);
    }

    @Override
    public void onPlayButtonClick(Activities mActivity) {
        // passes the Newly created object to the ActivityRunning fragment.
        ActivityRunning mActivityRunning = ActivityRunning.newInstance(KEY_RUNNING_ACTIVITY, mActivity);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.activity_fragment_container, mActivityRunning);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onActivityStopButtonClicked() {
        ActivityList mActivityList = new ActivityList();
        performReplaceFragmentTransaction(mActivityList);
    }

    /*
     * The passed activityId will be used to make Observations for current Activity.
     */
    @Override
    public void onAddNewObservationClicked(String activityId) {
        try {
            // create and intent and open the AddObservation fragment to add Observation.
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
            // open ObservationList Fragment.
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
            ActivityMap mActivityMap = ActivityMap.newInstance(mObservationList);
            performAddFragmentTransaction(mActivityMap);
        } catch (Exception e) {
        }
    }

}
