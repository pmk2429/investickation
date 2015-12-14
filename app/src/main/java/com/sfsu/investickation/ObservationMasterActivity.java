package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservation;
import com.sfsu.investickation.fragments.ObservationDetail;
import com.sfsu.investickation.fragments.ObservationsList;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

public class ObservationMasterActivity extends BaseActivity implements ObservationsList.IRemoteObservationCallBacks, AddObservation.IAddObservationCallBack {

    private final String LOGTAG = "~!@#$ObsMasterAct :";
    private Observation newlyCreatedObs, observationResponseObj;

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
                AddObservation addObservationFragment = AddObservation.newInstance("", "");
                setFragmentTransaction(addObservationFragment);
            } else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_USRACT_ADD_OBS, 0) == 1) {
                String currentActivityUUID = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_UUID);
                // if the intent is called from the UserActivityMasterActivity
                AddObservation addObservationFragment = AddObservation.newInstance(UserActivityMasterActivity
                        .KEY_ACTIVITY_UUID, currentActivityUUID);
                setFragmentTransaction(addObservationFragment);
            }
            // if the Intent is called by clicking on View Observations list in Dashboard
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                ObservationsList observationsList = new ObservationsList();
                setFragmentTransaction(observationsList);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                ObservationsList remoteObservationsFragment = new ObservationsList();
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
        // transaction.addToBackStack(null); NOT REQUIRED SINCE IT WILL BE FIRST FRAGMENT IN STACK
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
        AddObservation addObservationFragment = new AddObservation();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, addObservationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onObservationListItemClickListener() {
        ObservationDetail observationDetailFragment = new ObservationDetail();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.observation_fragment_container, observationDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void postObservationData(Observation newObservation) {
        newlyCreatedObs = newObservation;
        Log.i(LOGTAG, newlyCreatedObs.toString());
        // pass this object to RetrofitController and get response.
        BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(newlyCreatedObs, AppUtils.ADD_METHOD));
    }


    /**
     * Subscriber method to get the Response returned via Retrofit and eventually published by {@link com.squareup.otto.Bus}
     *
     * @param onLoaded - OnLoaded Observation Response Event
     */
    @Subscribe
    public void getObservationResponse(ObservationEvent.OnLoaded onLoaded) {

        // define the ObservationsList Fragment
        ObservationsList mObservationsList = new ObservationsList();

        // once you get the response, simply pass it to RemoteObservations Fragment to display
        Bundle newObservationBundle = new Bundle();

        // TODO: verify the object returned by the Retrofit and send it to RemoteObservationList
        newObservationBundle.putParcelable(AppUtils.OBSERVATION_RESOURCE, onLoaded.getResponse());

        mObservationsList.setArguments(newObservationBundle);
        // begin transaction and commit
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.observation_fragment_container, mObservationsList);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }
}
