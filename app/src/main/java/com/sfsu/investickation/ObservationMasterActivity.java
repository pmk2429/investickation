package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.controllers.RetrofitController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservation;
import com.sfsu.investickation.fragments.ObservationDetail;
import com.sfsu.investickation.fragments.RemoteObservationsList;
import com.sfsu.utils.AppUtils;

public class ObservationMasterActivity extends BaseActivity implements RemoteObservationsList.IRemoteObservationCallBacks, AddObservation.IAddObservationCallBack {

    private final String LOGTAG = "~!@#$ObsMasterAct :";
    private Observation newlyCreatedObs, observationResponseObj;
    private RetrofitController retrofitController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_main);

        // if Fragment container is present,
        if (findViewById(R.id.observation_fragment_container) != null) {

            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }
            // if Intent is called by clicking on the PostObservation button in Dashboard
            if (getIntent().getIntExtra(MainActivity.KEY_ADD_OBSERVATION, 0) == 1) {
                AddObservation addObservationFragment = new AddObservation();
                setFragmentTransaction(addObservationFragment);
            } else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_USRACT_ADD_OBS, 0) == 1) {
                // if the intent is called from the UserActivityMasterActivity
                AddObservation addObservationFragment = new AddObservation();
                // get the UUID of the current activity
                String currentActivityUUID = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_UUID);
                // create a Bundle and pass it to the New Observation Fragment.
                Bundle newObservationBundle = new Bundle();
                newObservationBundle.putString(UserActivityMasterActivity.KEY_ACTIVITY_UUID, currentActivityUUID);
                addObservationFragment.setArguments(newObservationBundle);
                setFragmentTransaction(addObservationFragment);
            }
            // if the Intent is called by clicking on View Observations list in Dashboard
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                RemoteObservationsList observationsList = new RemoteObservationsList();
                setFragmentTransaction(observationsList);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                RemoteObservationsList remoteObservationsFragment = new RemoteObservationsList();
                // if activity was started with special instructions from an Intent, then pass Intent's extras
                remoteObservationsFragment.setArguments(getIntent().getExtras());
                setFragmentTransaction(remoteObservationsFragment);
            }
        }
    }

    /**
     * Helper method to perform Fragment Transitions in switching Fragments.
     *
     * @param fragment
     */
    private void setFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, fragment);
//                transaction.addToBackStack(null); NOT REQUIRED SINCE IT WILL BE FIRST FRAGMENT IN STACK
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(ObservationMasterActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_observation_main, menu);
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

    /**
     * Call back method when user clicks on the plus button in RemoteObservationsList Fragment to add Observation
     */
    @Override
    public void onObservationAddListener() {
        AddObservation addObservationFragment = new AddObservation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, addObservationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Callback for item click in RemoteObservationsList.
     */
    @Override
    public void onItemClickListener() {
        ObservationDetail observationDetailFragment = new ObservationDetail();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, observationDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * This method provides the Call back interface to the AddObservation Fragment when user click Post Observation button.
     *
     * @param newObservation
     */
    @Override
    public void postObservationData(Observation newObservation) {

        newlyCreatedObs = newObservation;

        // pass this object to RetrofitController and get response.
        observationResponseObj = (Observation) retrofitController.add(AppUtils.OBSERVATION_RESOURCE, newlyCreatedObs);

        // define the RemoteObservationsList Fragment
        RemoteObservationsList mRemoteObservationsList = new RemoteObservationsList();

        // once you get the response, simply pass it to RemoteObservations Fragment to display
        Bundle newObservationBundle = new Bundle();
        newObservationBundle.putParcelable(AppUtils.OBSERVATION_RESOURCE, observationResponseObj);

        mRemoteObservationsList.setArguments(newObservationBundle);
        // begin transaction and commit
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.observation_fragment_container, mRemoteObservationsList);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }
}
