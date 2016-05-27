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
import android.widget.Button;
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
 * ActivityNewFragment Fragment provides Account the capability to add new Activity. The ActivityNewFragment fragment passes the newly created
 * Activities object to the ActivityRunningFragment fragment.
 */
public class ActivityNewFragment extends Fragment implements
        View.OnClickListener,
        LocationController.ILocationCallBacks,
        ITextValidate {

    public final String TAG = "~!@#$ActivityNewFragment";
    private final long REMINDER_INTERVAL = 30;
    @Bind(R.id.editText_actNew_ActivityName)
    EditText et_activityName;
    @Bind(R.id.editText_actNew_numOfPeople)
    EditText et_totalPeople;
    @Bind(R.id.editText_actNew_totalPets)
    EditText et_totalPets;
    @Bind(R.id.textView_actNew_reminder)
    TextView txtView_setReminder;
    // Views and Attribs
    private EditText et_manualInput;
    private Button btnHalfHour, btnHour;
    private GoogleMap googleMap;
    private MapView mapView;
    private SupportMapFragment fragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private Activities newActivityObj;
    private String reminderText;
    private boolean isHalfHourButtonClicked, isHourButtonClicked, isManualInputSet;
    private boolean isTotalPetsValid, isTotalPeopleValid, isActivityNameValid;
    private LocationController locationController;
    private GoogleMapController mGoogleMapController;
    private LocationController.ILocationCallBacks mLocationListener;
    private String locationArea;
    private boolean isGrownAnim = false;
    private DatabaseDataController dbController;
    private Bundle activityBundle;
    private ProgressDialog mProgressDialog;

    public ActivityNewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_new);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_new, container, false);

        ButterKnife.bind(this, rootView);

        // reference for setting up the Activities Object.
        newActivityObj = new Activities();

        et_activityName.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_activityName));
        et_totalPeople.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_totalPeople));
        et_totalPets.addTextChangedListener(new TextValidator(mContext, ActivityNewFragment.this, et_totalPets));

        // Get the MapView from the XML layout and inflate it
        mapView = (MapView) rootView.findViewById(R.id.mapView_activityMap);


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
            locationController = new LocationController(mContext, this);
            mGoogleMapController = new GoogleMapController(mContext);
            mGoogleMapController.setupGoogleMap(mapView);
            locationController.connectGoogleApi();
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
        dbController.closeConnection();
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
                isGrownAnim = true;
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
            newActivityObj = new Activities(activityName, totalPeople, totalPets, AppUtils.getCurrentTimeStamp(), userId);

            // set the state of Currently running activity to Running.
            newActivityObj.setState(Activities.STATE.RUNNING);

            // build on the same newActivity Object for geo location
            locationArea = locationArea != null ? locationArea : "";

            newActivityObj.setLocation_area(locationArea);

            // once the play button is clicked, make a network call and create new Activities on the server
            if (AppUtils.isConnectedOnline(mContext)) {
                BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized(newActivityObj, ApiRequestHandler.ADD));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Creating Activity...");
                mProgressDialog.show();
            } else {
                // create Unique ID for the Running activity of length 32.
                String activityUUID = RandomStringUtils.randomAlphanumeric(Activities.ID_LENGTH);

                newActivityObj.setId(activityUUID);

                // save the Activity on local database
                long resultCode = dbController.save(newActivityObj);

                // finally when the result Code is not -1, open Activity running Fragment.
                if (resultCode != -1) {
                    activityBundle = new Bundle();
                    activityBundle.putParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT, newActivityObj);
                    activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);

                    // click the play button
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    startAlarmForReminder();

                    mInterface.onPlayButtonClick(activityBundle);
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
            this.locationArea = locationArea;
        } else {
            this.locationArea = "";
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

        activityBundle = new Bundle();
        activityBundle.putParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT, createdActivity);
        // set reminder values
        activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);
        activityBundle.putLong(UserActivityMasterActivity.KEY_REMINDER_INTERVAL, REMINDER_INTERVAL);

        // start reminder
        startAlarmForReminder();

        // click the play button
        mInterface.onPlayButtonClick(activityBundle);
    }

    /**
     * Subscribes to the event of failure in creating {@link Activities} on the server.
     *
     * @param onLoaded
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
            case R.id.editText_actNew_ActivityName:
                isActivityNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_actNew_totalPets:
                isTotalPetsValid = ValidationUtil.validateNumber(mEditText, text);
                break;
            case R.id.editText_actNew_numOfPeople:
                isTotalPeopleValid = ValidationUtil.validateNumber(mEditText, text);
                break;
        }
    }


    /**
     * Interface Callback to define the callback methods for ActivityNewFragment fragment
     */
    public interface IActivityNewCallBack {
        /**
         * Callback method when Account clicks on the Play Button defined in the {@link ActivityNewFragment} Fragment. On clicking the
         * play button, the {@link Activities} is created on the server and using the created ActivityId, observations are made.
         *
         * @param newActivityDetails
         */
        public void onPlayButtonClick(Bundle activityBundle);
    }

}
