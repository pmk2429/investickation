package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservationFragment;
import com.sfsu.investickation.fragments.ObservationDetailFragment;
import com.sfsu.investickation.fragments.ObservationListFragment;
import com.sfsu.investickation.fragments.ObservationMap;
import com.sfsu.investickation.fragments.PostObservationListFragment;


public class ObservationMasterActivity extends MainBaseActivity
        implements ObservationListFragment.IRemoteObservationCallBacks,
        AddObservationFragment.IAddObservationCallBack,
        ObservationMap.IObservationMapCallBack {

    public static final String KEY_OBSERVATION_DETAIL = "observation_detail";
    public static final String KEY_BACK_TO_ACTIVITY_RUNNING = "back_to_activity_running";
    public static final String KEY_BACK_TO_ACTIVITY_DETAILS = "back_to_activity_details";
    // flag to display status messages depending on how the Observation related Fragments was called
    public static final long FLAG_ACTIVITY_RUNNING = 0x1010;
    public static final long FLAG_ACTIVITY_CREATED = 0x1011;
    public static final long FLAG_ACTIVITIES_OBSERVATIONS = 0x1012;
    // tag
    private final String TAG = "~!@#$ObsMasterAct";
    private FragmentManager fragmentManager;
    // flag to perform fragment transaction depending on the caller and callee
    private boolean FLAG_CALLED_FROM_DASHBOARD;
    private boolean FLAG_CALLED_FROM_ACTIVITY_RUNNING;
    private boolean FLAG_CALLED_FROM_ACTIVITY_DETAILS;
    private boolean FLAG_CALLED_FROM_OBSERVATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_main);

        fragmentManager = getSupportFragmentManager();

        // if Fragment container is present
        if (findViewById(R.id.observation_fragment_container) != null) {
            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }
            // if Intent is called by clicking on the PostObservation button in DashboardFragment
            if (getIntent().getIntExtra(MainActivity.KEY_ADD_OBSERVATION, 0) == 1) {
                // set the dashboard flag
                FLAG_CALLED_FROM_DASHBOARD = true;
                AddObservationFragment mAddObservationFragment = new AddObservationFragment();
                performAddFragmentTransaction(mAddObservationFragment);
            }
            // if the Intent is called from ActivityRunningFragment fragment to by clicking on AddObservationFragment button
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_ACTIVITY_ADD_OBS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_RUNNING = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                // if the intent is called from the UserActivityMasterActivity
                AddObservationFragment mAddObservationFragment = AddObservationFragment.newInstance(UserActivityMasterActivity
                        .KEY_ACTIVITY_ID, activityId);
                performAddFragmentTransaction(mAddObservationFragment);
            }
            // if the Intent is called from DashboardFragment by clicking on View Observations button.
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                FLAG_CALLED_FROM_DASHBOARD = true;
                ObservationListFragment observationListFragment = new ObservationListFragment();
                performAddFragmentTransaction(observationListFragment);
            }
            // if the Intent is called from ActivityDetailFragment by clicking on ViewObservations button.
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_VIEW_OBSERVATIONS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_DETAILS = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                ObservationListFragment mObservationListFragment = ObservationListFragment.newInstance(activityId);
                performAddFragmentTransaction(mObservationListFragment);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                ObservationListFragment mObservationListFragment = new ObservationListFragment();
                performAddFragmentTransaction(mObservationListFragment);
            }
        }
    }

    /*
        * Helper method to perform Fragment Transitions in switching Fragments.
        * @param fragment
        */
    private void performAddFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.observation_fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Helper method to perform Fragment Transitions in switching Fragments.
     *
     * @param fragment
     */
    private void performReplaceFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.observation_fragment_container, fragment);
        transaction.addToBackStack(null);
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
            } else {
                // general case where list is called from NavDrawer
                Intent homeIntent = new Intent(ObservationMasterActivity.this, MainActivity.class);
                startActivity(homeIntent);
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
        AddObservationFragment addObservationFragment = new AddObservationFragment();
        performReplaceFragmentTransaction(addObservationFragment);
    }


    @Override
    public void onObservationListItemClickListener(Observation observation) {
        ObservationDetailFragment observationDetailFragment = ObservationDetailFragment.newInstance(observation);
        performReplaceFragmentTransaction(observationDetailFragment);
    }

    @Override
    public void onUploadListOfObservations() {
        PostObservationListFragment postObservationListFragment = new PostObservationListFragment();
        performReplaceFragmentTransaction(postObservationListFragment);
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
    public void postObservationData(long statusFlag) {
        // dont add to backstack
        ObservationListFragment observationListFragment = ObservationListFragment.newInstance(statusFlag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.observation_fragment_container, observationListFragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
