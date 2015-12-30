package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservation;
import com.sfsu.investickation.fragments.ObservationDetail;
import com.sfsu.investickation.fragments.ObservationsList;

public class ObservationMasterActivity extends MainBaseActivity implements ObservationsList.IRemoteObservationCallBacks, AddObservation
        .IAddObservationCallBack {

    public static final String KEY_OBSERVATION_DETAIL = "observation_detail";
    public static final String KEY_BACK_TO_ACTIVITY_RUNNING = "back_to_activity_running";
    public static final String KEY_BACK_TO_ACTIVITY_DETAILS = "back_to_activity_details";
    private final String LOGTAG = "~!@#$ObsMasterAct";
    private boolean FLAG_CALLED_FROM_DASHBOARD;
    private boolean FLAG_CALLED_FROM_ACTIVITY_RUNNING;
    private boolean FLAG_CALLED_FROM_ACTIVITY_DETAILS;
    private boolean FLAG_CALLED_FROM_OBSERVATION;
    private Observation newlyCreatedObs, observationResponseObj;

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
                performFragmentTransaction(mAddObservation);
            }
            // if the Intent is called from ActivityRunning fragment to by clicking on AddObservation button
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_ACTIVITY_ADD_OBS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_RUNNING = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                // if the intent is called from the UserActivityMasterActivity
                AddObservation mAddObservation = AddObservation.newInstance(UserActivityMasterActivity
                        .KEY_ACTIVITY_ID, activityId);
                performFragmentTransaction(mAddObservation);
            }
            // if the Intent is called from Dashboard by clicking on View Observations button.
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                FLAG_CALLED_FROM_DASHBOARD = true;
                ObservationsList observationsList = new ObservationsList();
                performFragmentTransaction(observationsList);
            }
            // if the Intent is called from ActivityDetails by clicking on View Observations button.
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_VIEW_OBSERVATIONS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_DETAILS = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                ObservationsList mObservationsList = ObservationsList.newInstance(UserActivityMasterActivity.KEY_ACTIVITY_ID,
                        activityId);
                performFragmentTransaction(mObservationsList);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                ObservationsList mObservationsList = new ObservationsList();
                performFragmentTransaction(mObservationsList);
            }
        }
    }

    /**
     * Helper method to perform Fragment Transitions in switching Fragments.
     *
     * @param fragment
     */
    private void performFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.observation_fragment_container, fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
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
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onObservationAddListener() {
        FLAG_CALLED_FROM_OBSERVATION = true;
        AddObservation addObservationFragment = new AddObservation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.observation_fragment_container, addObservationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onObservationListItemClickListener(Observation observation) {
        ObservationDetail observationDetailFragment = ObservationDetail.newInstance(KEY_OBSERVATION_DETAIL, observation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.observation_fragment_container, observationDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        newlyCreatedObs = newObservation;
    }

}
