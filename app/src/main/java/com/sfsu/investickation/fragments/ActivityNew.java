package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.dialogs.AlertDialogMaster;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.service.PeriodicAlarm;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.RandomStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ActivityNew Fragment provides Account the capability to add new Activity. The ActivityNew fragment passes the newly created
 * Activities object to the ActivityRunning fragment.
 */
public class ActivityNew extends Fragment implements View.OnClickListener, LocationController.ILocationCallBacks,
        AlertDialogMaster.IReminderCallback {

    public final String TAG = "~!@#$ActivityNew";

    @Bind(R.id.editText_actNew_ActivityName)
    EditText et_activityName;
    @Bind(R.id.editText_actNew_numOfPeople)
    EditText et_totalPeople;
    @Bind(R.id.editText_actNew_totalPets)
    EditText et_totalPets;
    @Bind(R.id.textView_actNew_reminder)
    TextView txtView_setReminder;

    private EditText et_manualInput;
    private Button btnHalfHour, btnHour;
    private GoogleMap googleMap;
    private MapView mapView;
    private SupportMapFragment fragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private Activities newActivityObj;
    private String reminderText;
    private long REMINDER_INTERVAL;
    private boolean isHalfHourButtonClicked, isHourButtonClicked, isManualInputSet;
    private LocationController locationController;
    private GoogleMapController mGoogleMapController;
    private LocationController.ILocationCallBacks mLocationListener;
    private String locationArea;
    private boolean isGrownAnim = false;
    private DatabaseDataController dbController;
    private Bundle activityBundle;

    public ActivityNew() {
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

        et_activityName.addTextChangedListener(new TextValidator(et_activityName));
        et_totalPeople.addTextChangedListener(new TextValidator(et_totalPeople));
        et_totalPets.addTextChangedListener(new TextValidator(et_totalPets));

        // Get the MapView from the XML layout and inflate it
        mapView = (MapView) rootView.findViewById(R.id.mapView_activityMap);
        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);

        // setOnclickListener for the TextView.
        txtView_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupReminderDialog();
            }
        });


        // start the activity
        final FloatingActionButton addProject = (FloatingActionButton) rootView.findViewById(R.id.fab_activity_start);
        addProject.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationController = new LocationController(mContext, this);
        mGoogleMapController = new GoogleMapController(mContext);
        // Database controller.

        locationController.connectGoogleApi();

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mapView);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

        BusProvider.bus().register(this);
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
        if (validateString() && validateNumber(1) && validateNumber(2)) {
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

                    if (REMINDER_INTERVAL != 0 && REMINDER_INTERVAL != -123)
                        activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);
                    else
                        activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.FALSE);

                    // click the play button
                    mInterface.onPlayButtonClick(activityBundle);
                }
            }
        } else {
            return;
        }
    }

    // validate the input String be it first name, last name, address etc
    private boolean validateString() {
        if (et_activityName.getText().toString().trim().isEmpty()) {
            et_activityName.setError(getString(R.string.error_invalid_string));
            et_activityName.requestFocus();
            return false;
        } else {
            et_activityName.setError(null);
        }
        return true;
    }

    // validate the input String for valid Numeric value
    private boolean validateNumber(int checkCode) {
        if (checkCode == 1) {
            String numOfPeople = et_totalPeople.getText().toString().trim();
            if (numOfPeople.isEmpty() || !AppUtils.isNumeric(numOfPeople)) {
                et_totalPeople.setError(getString(R.string.error_NAN));
                et_totalPeople.requestFocus();
                return false;
            } else {
                et_totalPeople.setError(null);
            }
        } else if (checkCode == 2) {
            String totalPets = et_totalPets.getText().toString().trim();
            if (totalPets.isEmpty() || !AppUtils.isNumeric(totalPets)) {
                et_totalPets.setError(getString(R.string.error_NAN));
                et_totalPets.requestFocus();
                return false;
            } else {
                et_totalPets.setError(null);
            }
        }
        return true;
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
        Activities createdActivity = onLoaded.getResponse();
        Log.i(TAG, createdActivity.toString());

        createdActivity.setState(Activities.STATE.RUNNING);

        activityBundle = new Bundle();
        activityBundle.putParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT, createdActivity);

        if (REMINDER_INTERVAL != 0 && REMINDER_INTERVAL != -123) {
            activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.TRUE);
            activityBundle.putLong(UserActivityMasterActivity.KEY_REMINDER_INTERVAL, REMINDER_INTERVAL);
        } else {
            activityBundle.putBoolean(UserActivityMasterActivity.KEY_REMINDER_SET, Boolean.FALSE);
            activityBundle.putLong(UserActivityMasterActivity.KEY_REMINDER_INTERVAL, 0);
        }

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
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Helper method to setup Reminder Alert dialog
     */
    private void setupReminderDialog() {
        AlertDialogMaster alertDialogMaster = new AlertDialogMaster(mContext, this);
        alertDialogMaster.setupNewReminderDialog();
    }


    @Override
    public void setReminderValue(long reminderValue) {
        try {
            REMINDER_INTERVAL = reminderValue;

            if (REMINDER_INTERVAL != 0) {
                reminderText = "Reminder set for " + REMINDER_INTERVAL + " minutes";
//                Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
//                spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, mContext.getColor(R.color
//                        .colorPrimary))), 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                txtView_setReminder.setText(reminderText);

                // start the Alarm Reminder.
                startAlarmForReminder();
            }

        } catch (NullPointerException ne) {

        }
    }


    /**
     * Helper method to set the reminder depending on User's choice
     */
    private void startAlarmForReminder() {
        if (REMINDER_INTERVAL != 0 && REMINDER_INTERVAL != -123) {
            Log.d(TAG, "interval: " + REMINDER_INTERVAL);
            new PeriodicAlarm(mContext).setAlarm(REMINDER_INTERVAL);
        }

    }


    /**
     * Interface Callback to define the callback methods for ActivityNew fragment
     */
    public interface IActivityNewCallBack {
        /**
         * Callback method when Account clicks on the Play Button defined in the {@link ActivityNew} Fragment. On clicking the
         * play button, the {@link Activities} is created on the server and using the created ActivityId, observations are made.
         *
         * @param newActivityDetails
         */
        public void onPlayButtonClick(Bundle activityBundle);
    }

    /**
     * TextValidator class to validate the input Text from the user.
     */
    private class TextValidator implements TextWatcher {

        private View view;

        public TextValidator(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.editText_actNew_ActivityName:
                    validateString();
                    break;
                case R.id.editText_actNew_numOfPeople:
                    validateNumber(1);
                    break;
                case R.id.editText_actNew_totalPets:
                    validateNumber(2);
                    break;
            }
        }
    }

}
