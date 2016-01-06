package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.RandomStringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ActivityNew Fragment provides Account the capability to add new Activity. The ActivityNew fragment passes the newly created
 * Activities object to the ActivityRunning fragment.
 */
public class ActivityNew extends Fragment implements View.OnClickListener, LocationController.ILocationCallBacks {

    private static final int ID_LENGTH = 16;
    public final String TAG = "~!@#$ActivityNew";

    @Bind(R.id.editText_ActivityName)
    EditText et_activityName;
    @Bind(R.id.editText_numOfPeople)
    EditText et_totalPeople;
    @Bind(R.id.editText_totalPets)
    EditText et_totalPets;
    @Bind(R.id.textView_reminder)
    TextView txtView_setReminder;

    private EditText et_manualInput;
    private Button btnHalfHour, btnHour;
    private GoogleMap googleMap;
    private MapView mapView;
    private SupportMapFragment fragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private Activities newActivityObj;
    private String reminderTimeValue;
    private boolean isHalfHourButtonClicked, isHourButtonClicked, isManualInputSet;
    private LocationController locationController;
    private GoogleMapController mGoogleMapController;
    private LocationController.ILocationCallBacks mLocationListener;
    private String locationArea;
    private boolean isGrownAnim = false;

    public ActivityNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_new);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        BusProvider.bus().unregister(ActivityList.class);
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

        locationController = new LocationController(mContext, this);
        mGoogleMapController = new GoogleMapController(mContext, this);

        // connect to GoogleAPI and setup FusedLocationService to get the Location updates.
        locationController.connectGoogleApi();

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mapView);

        // setOnclickListener for the TextView.
        txtView_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        txtView_setReminder.setText(selectedHour + ":" + selectedMinute);
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();

                setupReminderDialog(inflater);
            }
        });


        // start the activity
        final FloatingActionButton addProject = (FloatingActionButton) rootView.findViewById(R.id.fab_activity_start);
        addProject.setOnClickListener(this);

        return rootView;
    }


    /**
     * Method to set up the custom Reminder Alert Dialog to notify user for Tick Checks periodically as per time specified.
     *
     * @param inflater
     * @param fragmentView
     */
    private void setupReminderDialog(LayoutInflater inflater) {
        // initialize the value of each flags.
        isHalfHourButtonClicked = isHourButtonClicked = isManualInputSet = false;
        // setup Custom AlertDialog builder.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View convertView = inflater.inflate(R.layout.alertdialog_reminder, null);
        alertDialog.setTitle("Set Reminder");

        // set the onClickListener for each buttons defined in the custom layout.
        et_manualInput = (EditText) convertView.findViewById(R.id.editText_alertDialog_manualInput);
        btnHalfHour = (Button) convertView.findViewById(R.id.button_alertDialog_30);
        //btnHour = (Button) convertView.findViewById(R.id.button_alertDialog_60);

        btnHalfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView(btnHalfHour);
            }
        });

        // initialize and set et_manualInput.
        et_manualInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_manualInput.getText().toString().equals("") && !et_manualInput.getText().toString().equals(null)) {
                    enableView(et_manualInput);
                }
            }
        });

        // on click of the Positive Button, set the textView_Reminder value for selected option.
        alertDialog.setPositiveButton(R.string.alertDialog_apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // when the flags are set,
                if (isHalfHourButtonClicked) {
                    txtView_setReminder.setText(getActivity().getResources().getString(R.string.alertDialog_reminder_30));
                } else if (isManualInputSet) {
                    txtView_setReminder.setText(et_manualInput.getText().toString());
                }
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // finally display the alert dialog.
        alertDialog.setView(convertView);
        alertDialog.show();
    }

    /**
     * Method to toggle the color of the clicked button in Alert Dialog.
     *
     * @param mButton
     */
    private void toggleBackground(Button mButton) {
        int colorPrimary = getActivity().getResources().getColor(R.color.colorPrimary);
        int colorSecondary = getActivity().getResources().getColor(R.color.colorSecondary);

        ColorDrawable viewColor = (ColorDrawable) mButton.getBackground();
        int currentColor = viewColor.getColor();

        if (currentColor == colorPrimary) {
            mButton.setBackgroundColor(colorSecondary);
        } else {
            mButton.setBackgroundColor(colorPrimary);
        }
    }

    /**
     * Helper method to clear the focus of the View.
     *
     * @param v
     */
    private void clearFocusView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                btnHalfHour.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                break;

//            case R.id.button_alertDialog_60:
//                btnHour.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
//                break;

            case R.id.editText_alertDialog_manualInput:
                et_manualInput.clearFocus();
                break;
        }
    }

    /**
     * Helper method to enable the view for reminder option
     *
     * @param v
     */
    private void enableView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                isHalfHourButtonClicked = true;
                //isHourButtonClicked = false;
                isManualInputSet = false;
                toggleBackground(btnHalfHour);
                //clearFocusView(btnHour);
                clearFocusView(et_manualInput);
                break;

//            case R.id.button_alertDialog_60:
//                isHourButtonClicked = true;
//                isHalfHourButtonClicked = false;
//                isManualInputSet = false;
//                toggleBackground(btnHour);
//                clearFocusView(btnHalfHour);
//                clearFocusView(et_manualInput);
//                break;

            case R.id.editText_alertDialog_manualInput:
                isManualInputSet = true;
                isHalfHourButtonClicked = false;
                //isHourButtonClicked = false;
                //clearFocusView(btnHour);
                clearFocusView(btnHalfHour);
                break;
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onStop() {
        super.onStop();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.bus().register(this);
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

            // create Unique ID for the Running activity.
            String activityUUID = RandomStringUtils.randomAlphanumeric(ID_LENGTH);

            String userId = new AuthPreferences(mContext).getUser_id();

            // create a new Activities Object (Model).
            newActivityObj = new Activities(activityName, totalPeople, totalPets, AppUtils.getCurrentTimeStamp(), userId);

            // set the state of Currently running activity to Running.
            newActivityObj.setState(Activities.STATE.RUNNING);

            // build on the same newActivity Object.
            newActivityObj.setLocation_area(locationArea);

            // once the play button is clicked, make a network call and create new Activities on the server
            BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized(newActivityObj, ApiRequestHandler.ADD));
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
            newActivityObj.setLocation_area(".");
        }
    }


    /**
     * Subscribes to the event of successful in creating {@link Activities} on the server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onCreateActivitiesSuccess(ActivityEvent.OnLoaded onLoaded) {
        Log.i(TAG, "Activity created success");
        Activities createdActivity = onLoaded.getResponse();
        mInterface.onPlayButtonClick(createdActivity);
    }

    /**
     * Subscribes to the event of failure in creating {@link Activities} on the server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onCreateActivitiesFailure(ActivityEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, "failed to create Activity");
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
        public void onPlayButtonClick(Activities mActivity);
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
                case R.id.editText_ActivityName:
                    validateString();
                    break;
                case R.id.editText_numOfPeople:
                    validateNumber(1);
                    break;
                case R.id.editText_totalPets:
                    validateNumber(2);
                    break;
            }
        }
    }

}
