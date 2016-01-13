package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservation;
import com.sfsu.investickation.fragments.ObservationDetail;
import com.sfsu.investickation.fragments.ObservationList;
import com.sfsu.investickation.fragments.ObservationMap;


public class ObservationMasterActivity extends MainBaseActivity
        implements ObservationList.IRemoteObservationCallBacks,
        AddObservation.IAddObservationCallBack,
        ObservationMap.IObservationMapCallBack {

    public static final String KEY_OBSERVATION_DETAIL = "observation_detail";
    public static final String KEY_BACK_TO_ACTIVITY_RUNNING = "back_to_activity_running";
    public static final String KEY_BACK_TO_ACTIVITY_DETAILS = "back_to_activity_details";
    private final String TAG = "~!@#$ObsMasterAct";

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private boolean FLAG_CALLED_FROM_DASHBOARD;
    private boolean FLAG_CALLED_FROM_ACTIVITY_RUNNING;
    private boolean FLAG_CALLED_FROM_ACTIVITY_DETAILS;
    private boolean FLAG_CALLED_FROM_OBSERVATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_main);

        // if Fragment container is present
        if (findViewById(R.id.observation_fragment_container) != null) {
            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }
            // if Intent is called by clicking on the PostObservation button in Dashboard
            if (getIntent().getIntExtra(MainActivity.KEY_ADD_OBSERVATION, 0) == 1) {
                // set the dashboard flag
                FLAG_CALLED_FROM_DASHBOARD = true;
                AddObservation mAddObservation = new AddObservation();
                performReplaceFragmentTransaction(mAddObservation);
            }
            // if the Intent is called from ActivityRunning fragment to by clicking on AddObservation button
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_ACTIVITY_ADD_OBS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_RUNNING = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                // if the intent is called from the UserActivityMasterActivity
                AddObservation mAddObservation = AddObservation.newInstance(UserActivityMasterActivity
                        .KEY_ACTIVITY_ID, activityId);
                performReplaceFragmentTransaction(mAddObservation);
            }
            // if the Intent is called from Dashboard by clicking on View Observations button.
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                FLAG_CALLED_FROM_DASHBOARD = true;
                ObservationList observationList = new ObservationList();
                performReplaceFragmentTransaction(observationList);
            }
            // if the Intent is called from ActivityDetail by clicking on View Observations button.
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_VIEW_OBSERVATIONS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_DETAILS = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                ObservationList mObservationList = ObservationList.newInstance(activityId);
                performReplaceFragmentTransaction(mObservationList);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                ObservationList mObservationList = new ObservationList();
                performReplaceFragmentTransaction(mObservationList);
            }
        }
    }

    /*
        * Helper method to perform Fragment Transitions in switching Fragments.
        * @param fragment
        */
    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.observation_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Helper method to perform Fragment Transitions in switching Fragments.
     *
     * @param fragment
     */
    private void performReplaceFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.observation_fragment_container, fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            if (FLAG_CALLED_FROM_DASHBOARD || FLAG_CALLED_FROM_OBSERVATION) {
                Intent homeIntent = new Intent(ObservationMasterActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            } else if (FLAG_CALLED_FROM_ACTIVITY_RUNNING) {
                Intent activityIntent = new Intent(ObservationMasterActivity.this, UserActivityMasterActivity.class);
                activityIntent.putExtra(KEY_BACK_TO_ACTIVITY_RUNNING, 11);
                startActivity(activityIntent);
                finish();
            } else if (FLAG_CALLED_FROM_ACTIVITY_DETAILS) {
                Intent activityIntent = new Intent(ObservationMasterActivity.this, UserActivityMasterActivity.class);
                activityIntent.putExtra(KEY_BACK_TO_ACTIVITY_DETAILS, 11);
                startActivity(activityIntent);
                finish();
            }
            super.onBackPressed();
        } else if (count > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }


    @Override
    public void onObservationAddListener() {
        FLAG_CALLED_FROM_OBSERVATION = true;
        AddObservation addObservationFragment = new AddObservation();
        performAddFragmentTransaction(addObservationFragment);
    }


    @Override
    public void onObservationListItemClickListener(Observation observation) {
        ObservationDetail observationDetailFragment = ObservationDetail.newInstance(observation);
        performAddFragmentTransaction(observationDetailFragment);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void postObservationData(Observation newObservation) {
        ObservationList observationListFragment = new ObservationList();
        performReplaceFragmentTransaction(observationListFragment);
    }

}
