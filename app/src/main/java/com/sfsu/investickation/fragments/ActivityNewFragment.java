package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.service.PeriodicAlarm;
import com.sfsu.utils.AppUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.RandomStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ActivityNewFragment Fragment provides Account the capability to add new Activity. The ActivityNewFragment mMapFragment passes the newly created
 * Activities object to the ActivityRunningFragment mMapFragment.
 */
public class ActivityNewFragment extends Fragment implements
        View.OnClickListener,
        LocationController.ILocationCallBacks,
        ITextValidate {

    public final String TAG = "~!@#$ActivityNewFragment";
    private final long REMINDER_INTERVAL = 30;
    @Bind(R.id.edittext_activity_new_name)
    EditText et_activityName;
    @Bind(R.id.edittext_activity_new_num_of_people)
    EditText et_totalPeople;
    @Bind(R.id.edittext_act_new_total_pets)
    EditText et_totalPets;
    @Bind(R.id.textview_act_new_reminder)
    TextView txtView_setReminder;
    // Views and Attributes
    private GoogleMap googleMap;
    private MapView mapView;
    private SupportMapFragment mMapFragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private Activities mNewActivity;
    private boolean isTotalPetsValid;
    private boolean isTotalPeopleValid;
    private boolean isActivityNameValid;
    private LocationController mLocationController;
    private GoogleMapController mGoogleMapController;
    private LocationController.ILocationCallBacks mLocationListener;
    private String mLocationArea;
    private DatabaseDataController mDbController;
    private Bundle mActivityBundle;
    private ProgressDialog mProgressDialog;

    public ActivityNewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_new);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mDbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this mMapFragment
        View rootView = inflater.inflate(R.layout.fragment_activity_new, container, false);

        ButterKnife.bind(this, rootView);

        // reference for setting up the Activities Object.
        mNewActivity = new Activities();

        et_activityName.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_activityName));
        et_totalPeople.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_totalPeople));
        et_totalPets.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_totalPets));

        // Get the MapView from the XML layout and inflate it
        mapView = (MapView) rootView.findViewById(R.id.mapview_activity_map);


        // start the activity
        final FloatingActionButton addProject = (FloatingActionButton) rootView.findViewById(R.id.fab_activity_start);
        addProject.setOnClickListener(this);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);

        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        BusProvider.bus().register(this);
        mProgressDialog = new ProgressDialog(mContext);

        // setup google Map using the GoogleMapController.
        if (AppUtils.isConnectedOnline(mContext)) {
            mLocationController = new LocationController(mContext, this);
            mGoogleMapController = new GoogleMapController(mContext);
            mGoogleMapController.setupGoogleMap(mapView);
            mLocationController.connectGoogleApi();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDbController.closeConnection();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IActivityNewCallBack) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityNewCallBack");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mInterface = null;
        mapView = null;
        mGoogleMapController = null;
        mLocationListener = null;
        googleMap = null;
    }


    // collect all the details of an Activity and pass it on to Parent Activity
    @Override
    public void onClick(View v) {

        final Animation animGrow = AnimationUtils.loadAnimation(mContext, R.anim.grow_color);

        animGrow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        /*
        Validate all the input Strings and once the validation passes, create the activity object and pass it to parent Activity
         */
        if (isActivityNameValid && isTotalPeopleValid && isTotalPetsValid) {
            String activityName = et_activityName.getText().toString();
            int totalPeople = Integer.parseInt(et_totalPeople.getText().toString());
            int totalPets = Integer.parseInt(et_totalPets.getText().toString());

            String userId = new AuthPreferences(mContext).getUser_id();

            // create a new Activities Object (Model).
            mNewActivity = new Activities(activityName, totalPeople, totalPets, AppUtils.getCurrentTimeStamp(), userId);

            // set the state of Currently running activity to Running.
            mNewActivity.setState(Activities.STATE.RUNNING);

            // build on the same newActivity Object for geo location
            mLocationArea = mLocationArea != null ? mLocationArea : "";

            mNewActivity.setLocation_area(mLocationArea);

            // once the play button is clicked, make a network call and create new Activities on the server
            if (AppUtils.isConnectedOnline(mContext)) {
                BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized(mNewActivity, ApiRequestHandler.ADD));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Creating Activity...");
                mProgressDialog.show();
            } else {
                // create Unique ID for the Running activity of length 32.
                String activityUUID = RandomStringUtils.randomAlphanumeric(Activities.ID_LENGTH);

                mNewActivity.setId(activityUUID);

                // save the Activity on local database
                long resultCode = mDbController.save(mNewActivity);

                // finally when the result Code is not -1, open Activity running Fragment.
                if (resultCode != -1) {
                    mActivityBundle = new Bundle();
                    mActivityBundle.putParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT, mNewActivity);
                    mActivityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);

                    // click the play button
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    startAlarmForReminder();

                    mInterface.onPlayButtonClick(mActivityBundle);
                }
            }
        } else {
            return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCurrentLocation(Location mLocation) {

    }

    @Override
    public void setLatLng(LatLng mLatLng) {

    }

    @Override
    public void setLocationArea(String locationArea) {
        if (locationArea != null || !locationArea.equals("")) {
            this.mLocationArea = locationArea;
        } else {
            this.mLocationArea = "";
        }
    }


    /**
     * Subscribes to the event of successful in creating {@link Activities} on the server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onCreateActivitiesSuccess(ActivityEvent.OnLoaded onLoaded) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        Activities createdActivity = onLoaded.getResponse();
        createdActivity.setState(Activities.STATE.RUNNING);

        mActivityBundle = new Bundle();
        mActivityBundle.putParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT, createdActivity);
        // set reminder values
        mActivityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);
        mActivityBundle.putLong(UserActivityMasterActivity.KEY_REMINDER_INTERVAL, REMINDER_INTERVAL);

        // start reminder
        startAlarmForReminder();

        // click the play button
        mInterface.onPlayButtonClick(mActivityBundle);
    }

    /**
     * Subscribes to the event of failure in creating {@link Activities} on the server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onCreateActivitiesFailure(ActivityEvent.OnLoadingError onLoadingError) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        //Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Helper method to set the reminder depending on User's choice
     */
    private void startAlarmForReminder() {
        new PeriodicAlarm(mContext).setAlarm(REMINDER_INTERVAL);
    }

    @Override
    public void validate(View mView, String text) {
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.edittext_activity_new_name:
                isActivityNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.edittext_act_new_total_pets:
                isTotalPetsValid = ValidationUtil.validateNumber(mEditText, text);
                break;
            case R.id.edittext_activity_new_num_of_people:
                isTotalPeopleValid = ValidationUtil.validateNumber(mEditText, text);
                break;
        }
    }


    /**
     * Interface Callback to define the callback methods for ActivityNewFragment mMapFragment
     */
    public interface IActivityNewCallBack {
        /**
         * Callback method when Account clicks on the Play Button defined in the {@link ActivityNewFragment} Fragment. On clicking the
         * play button, the {@link Activities} is created on the server and using the created ActivityId, observations are made.
         *
         * @param activityBundle
         */
        public void onPlayButtonClick(Bundle activityBundle);
    }

}
