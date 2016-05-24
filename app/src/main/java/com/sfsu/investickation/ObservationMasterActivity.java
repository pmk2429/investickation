package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.fragments.AddObservationFragment;
import com.sfsu.investickation.fragments.EditObservationFragment;
import com.sfsu.investickation.fragments.ObservationDetailFragment;
import com.sfsu.investickation.fragments.ObservationListFragment;
import com.sfsu.investickation.fragments.ObservationMap;
import com.sfsu.investickation.fragments.PostObservationFragment;
import com.sfsu.investickation.fragments.PostObservationListFragment;

/**
 * Container Activity for all the Observation related Fragments. Holds strong references of each Fragment for saving/restoring
 * the state. Provides a navigation mechanism for handling backstacks and traversing between Activities/Fragments
 */
public class ObservationMasterActivity extends MainBaseActivity implements
        ObservationListFragment.IRemoteObservationCallBacks,
        AddObservationFragment.IAddObservationCallBack,
        ObservationMap.IObservationMapCallBack,
        ObservationDetailFragment.IObservationDetailCallbacks,
        EditObservationFragment.IEditObservationCallbacks {

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
    // fragment references
    private ObservationDetailFragment mObservationDetailFragment;
    private AddObservationFragment mAddObservationFragment;
    private EditObservationFragment mEditObservationFragment;
    private ObservationListFragment mObservationListFragment;


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
                mAddObservationFragment = (AddObservationFragment) fragmentManager.getFragment(savedInstanceState,
                        "addObservation");
                mObservationDetailFragment = (ObservationDetailFragment) fragmentManager.getFragment(savedInstanceState,
                        "observationDetail");
                mEditObservationFragment = (EditObservationFragment) fragmentManager.getFragment(savedInstanceState,
                        "editObservation");
                mObservationListFragment = (ObservationListFragment) fragmentManager.getFragment(savedInstanceState,
                        "observationList");

            }
            // if Intent is called by clicking on the PostObservation button in DashboardFragment
            if (getIntent().getIntExtra(MainActivity.KEY_ADD_OBSERVATION, 0) == 1) {
                // set the dashboard flag
                FLAG_CALLED_FROM_DASHBOARD = true;
                if (mAddObservationFragment == null)
                    mAddObservationFragment = new AddObservationFragment();
                performAddFragmentTransaction(mAddObservationFragment, false);
            }
            // if the Intent is called from ActivityRunningFragment fragment to by clicking on AddObservationFragment button
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_ACTIVITY_ADD_OBS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_RUNNING = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                // if the intent is called from the UserActivityMasterActivity
                mAddObservationFragment = AddObservationFragment.newInstance(UserActivityMasterActivity
                        .KEY_ACTIVITY_ID, activityId);
                performAddFragmentTransaction(mAddObservationFragment, false);
            }
            // if the Intent is called from DashboardFragment by clicking on View Observations button.
            else if (getIntent().getIntExtra(MainActivity.KEY_VIEW_OBSERVATION_LIST, 0) == 2) {
                FLAG_CALLED_FROM_DASHBOARD = true;
                if (mObservationListFragment == null)
                    mObservationListFragment = new ObservationListFragment();
                performAddFragmentTransaction(mObservationListFragment, false);
            }
            // if the Intent is called from ActivityDetailFragment by clicking on ViewObservations button.
            else if (getIntent().getIntExtra(UserActivityMasterActivity.KEY_VIEW_OBSERVATIONS, 0) == 1) {
                FLAG_CALLED_FROM_ACTIVITY_DETAILS = true;
                String activityId = getIntent().getStringExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID);
                mObservationListFragment = ObservationListFragment.newInstance(activityId);
                performAddFragmentTransaction(mObservationListFragment, false);
            }
            // else if the Observations is clicked in the NavDrawer
            else {
                if (mObservationListFragment == null) mObservationListFragment = new ObservationListFragment();
                performAddFragmentTransaction(mObservationListFragment, false);
            }
        }
    }

    /*
        * Helper method to perform Fragment Transitions in switching Fragments.
        * @param fragment
        */
    private void performAddFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.observation_fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Helper method to perform Fragment Transitions in switching Fragments.
     *
     * @param fragment
     */
    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.observation_fragment_container, fragment);
        if (addToBackStack)
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
        mAddObservationFragment = new AddObservationFragment();
        performReplaceFragmentTransaction(mAddObservationFragment, true);
    }


    @Override
    public void onObservationListItemClickListener(Observation observation) {
        mObservationDetailFragment = ObservationDetailFragment.newInstance(observation);
        performReplaceFragmentTransaction(mObservationDetailFragment, true);
    }

    @Override
    public void onUploadListOfObservations() {
        performReplaceFragmentTransaction(new PostObservationListFragment(), true);
    }

    @Override
    public void postObservationData(long statusFlag) {
        // dont add to backstack
        mObservationListFragment = ObservationListFragment.newInstance(statusFlag);
        performReplaceFragmentTransaction(mObservationListFragment, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onEditObservationClick(Observation mObservation) {
        mEditObservationFragment = EditObservationFragment.newInstance(mObservation);
        performReplaceFragmentTransaction(mEditObservationFragment, true);
    }

    @Override
    public void onUploadObservationStoredLocally(Observation mObservation) {
        performReplaceFragmentTransaction(PostObservationFragment.createNewInstance(mObservation), true);
    }

    @Override
    public void displayObservationDetails(Observation mObservation) {
        mObservationDetailFragment = ObservationDetailFragment.newInstance(mObservation);
        performReplaceFragmentTransaction(mObservationDetailFragment, true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentManager.putFragment(outState, "addObservation", mAddObservationFragment);
        fragmentManager.putFragment(outState, "editObservation", mEditObservationFragment);
        fragmentManager.putFragment(outState, "observationDetail", mObservationDetailFragment);
        fragmentManager.putFragment(outState, "observationList", mObservationListFragment);
    }
}
