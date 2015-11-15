package com.sfsu.investickation;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sfsu.controllers.RetrofitController;
import com.sfsu.entities.Activities;
import com.sfsu.exceptions.NetworkErrorException;
import com.sfsu.investickation.fragments.ActivityDetails;
import com.sfsu.investickation.fragments.ActivityList;
import com.sfsu.investickation.fragments.ActivityNew;
import com.sfsu.investickation.fragments.ActivityRunning;
import com.sfsu.utils.AppUtils;

import java.util.ArrayList;

/**
 * <tt>UserActivityMasterActivity</tt> is the parent activity and the holding container for all the Activity related fragments.
 * This activity provides the DB access calls, network calls, initializing the controllers, passing the data to the Fragments and so
 * on. All the Activity related operations are carried out in UserActivityMasterActivity.
 * <p/>
 * This Activity implements the ConnectionCallbacks for its child Fragments which provides listener methods to these Fragments.
 */
public class UserActivityMasterActivity extends BaseActivity implements ActivityList.IActivityCallBacks, ActivityNew.IActivityNewCallBack, ActivityRunning.IActivityRunningCallBacks, ActivityDetails.IActivityDetailsCallBacks, View.OnClickListener {

    private final String LOGTAG = "~!@#$UserActivity :";
    private ArrayList<Activities> listSavedActivities;
    private RetrofitController retrofitController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);


        // initialize the RetrofitController.
        retrofitController = new RetrofitController(this);

        try {
            // get the List of Activities from server.
            listSavedActivities = getListOfSavedActivities();
        } catch (NetworkErrorException e) {
            Log.i(LOGTAG, e.getMessage());
        } catch (Exception e) {
            Log.i(LOGTAG, e.getMessage());
        }

        // if Fragment container is present
        if (findViewById(R.id.activity_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // is the ActivityNew is being called
            if (getIntent().getIntExtra("ActivityNew", 0) == 1) { // if user clicks on Start Activity
                ActivityNew activityNewFragment = new ActivityNew();
                FragmentTransaction activityNew = getSupportFragmentManager().beginTransaction();
                activityNew.replace(R.id.activity_fragment_container, activityNewFragment);
                activityNew.addToBackStack(null);
                activityNew.commit();
            }
            // if ActivityList is being called.
            else if (getIntent().getIntExtra("ActivityList", 0) == 2) { // if user clicks on ActivityList
                ActivityList activityList = new ActivityList();
                FragmentTransaction activityListFragment = getSupportFragmentManager().beginTransaction();
                activityListFragment.replace(R.id.activity_fragment_container, activityList);
                activityListFragment.addToBackStack(null);
                activityListFragment.commit();
            }
            /* if no press, open ActivityList. The first time the UserActivityMasterActivity is opened, the default Fragment
            to be displayed it the ActivityList Fragment.

            Since this Fragment displays the List of Activities stored in the Server, we have to pass the List of Activities
            in the Bundle to this Fragment.
             */

            else {
                ActivityList activityListFragment = new ActivityList();
                Bundle bundleListOfActivities = new Bundle();
                bundleListOfActivities.putParcelableArrayList(AppUtils.ACTIVITY_KEY, listSavedActivities);
                // finally add the Bundle to the activityList Fragment instance.
                activityListFragment.setArguments(bundleListOfActivities);
                // add Fragment to 'activity_fragment_container'
                getSupportFragmentManager().beginTransaction().replace(R.id.activity_fragment_container, activityListFragment).commit();
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent homeIntent = new Intent(UserActMainActivity.this, MainActivity.class);
//        startActivity(homeIntent);
//        finish();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_activity_main, menu);
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

    // callback for item click on RecyclerView.
    @Override
    public void onItemClickListener() {
        ActivityDetails mActivityDetailsFragment = new ActivityDetails();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_container, mActivityDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // callback for Adding New Activity
    @Override
    public void onActivityAddListener() {
        // if user clicked the Add Button, replace with AddObservation Fragment
        ActivityNew addActivityFragment = new ActivityNew();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_container, addActivityFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
    }

    /*
     * This method will be called when the user clicks on the Play Button in ActivityNew Fragment. All the Details of the
     * Activity started by the user will be collected and passed to the ActivityRunning fragment.
     */
    @Override
    public void onPlayButtonClick(Activities newActivityDetailsObject) {

        ActivityRunning mActivityRunningFragment = new ActivityRunning();
        // set the data to Bundle to pass it to ActivityRunning Fragment
        Bundle newActivityBundle = new Bundle();
        if (newActivityDetailsObject != null) {
            newActivityBundle.putParcelable(AppUtils.ACTIVITY_RESOURCE, newActivityDetailsObject);
            mActivityRunningFragment.setArguments(newActivityBundle);
        }
        // initialize the transaction.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_fragment_container, mActivityRunningFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onActivityDetailsClick() {

    }


    /* The newly created Activity object is passed over to Retrofit. The Activity model references the current user who is
     logged in and hence it is mandatory to pass the current user id along with the Activity Object. */
    @Override
    public void onActivityStopButtonClicked(Activities mNewActivityObj) {
        // get the current User Id.
        retrofitController.add(AppUtils.ACTIVITY_RESOURCE, mNewActivityObj);
    }

    /**
     * Method to call the RetrofitController and get List of all Activities stored in Server.
     *
     * @return
     */
    public ArrayList<Activities> getListOfSavedActivities() throws NetworkErrorException {
        // casting and converting the ArrayList of Entities to ArrayList of Activities.
        ArrayList<Activities> activitiesList = (ArrayList<Activities>) (ArrayList<?>) retrofitController.getAll(AppUtils
                .ACTIVITY_RESOURCE);
        return activitiesList;

    }
}
