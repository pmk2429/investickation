package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.utils.AppUtils;

/**
 * ActivityNew Fragment provides User the capability to add new Activity. The ActivityNew fragment passes the newly created
 * Activities object to the ActivityRunning fragment.
 */
public class ActivityNew extends Fragment implements View.OnClickListener {
    private GoogleMap googleMap;
    private MapView mapView;
    private SupportMapFragment fragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private TextView txtView_setReminder;
    private Activities newActivityObj;
    private EditText et_activityName, et_totalPeople, et_totalPets, et_manualInput;
    private String reminderTimeValue;
    private boolean isHalfHourButtonClicked, isHourButtonClicked, isManualInputSet;
    private Button btnHour, btnHalfHour;

    public ActivityNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Start New Activity");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_activity_new, container, false);

        // set the Timer Reminder.
        txtView_setReminder = (TextView) rootView.findViewById(R.id.spinner_reminder);

        // collect all the data for the New Activity posted by user.
        et_activityName = (EditText) rootView.findViewById(R.id.editText_ActivityName);
        et_totalPeople = (EditText) rootView.findViewById(R.id.editText_numOfPeople);
        et_totalPets = (EditText) rootView.findViewById(R.id.editText_totalPets);

        et_activityName.addTextChangedListener(new TextValidator(et_activityName));
        et_totalPeople.addTextChangedListener(new TextValidator(et_totalPeople));
        et_totalPets.addTextChangedListener(new TextValidator(et_totalPets));

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) rootView.findViewById(R.id.mapView_activityMap);
        mapView.onCreate(savedInstanceState);

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

        // setup google Map.
        setupGoogleMap();

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

        btnHalfHour = (Button) convertView.findViewById(R.id.button_alertDialog_30);
        btnHour = (Button) convertView.findViewById(R.id.button_alertDialog_60);
        et_manualInput = (EditText) convertView.findViewById(R.id.editText_alertDialog_manualInput);

        btnHalfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView(btnHalfHour);
            }
        });

        btnHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView(btnHour);
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
                } else if (isHourButtonClicked) {
                    txtView_setReminder.setText(getActivity().getResources().getString(R.string.alertDialog_reminder_60));
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

    private void clearFocusView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                btnHalfHour.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.button_alertDialog_60:
                btnHour.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.editText_alertDialog_manualInput:
                et_manualInput.clearFocus();
                break;
        }
    }

    private void enableView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                isHalfHourButtonClicked = true;
                isHourButtonClicked = false;
                isManualInputSet = false;
                toggleBackground(btnHalfHour);
                clearFocusView(btnHour);
                clearFocusView(et_manualInput);
//                Toast.makeText(getActivity(), isHalfHourButtonClicked + " : " + isHourButtonClicked + " : " + isManualInputSet, Toast
//                        .LENGTH_SHORT).show();
                break;

            case R.id.button_alertDialog_60:
                isHourButtonClicked = true;
                isHalfHourButtonClicked = false;
                isManualInputSet = false;
                toggleBackground(btnHour);
                clearFocusView(btnHalfHour);
                clearFocusView(et_manualInput);
//                Toast.makeText(getActivity(), isHalfHourButtonClicked + " : " + isHourButtonClicked + " : " + isManualInputSet, Toast
//                        .LENGTH_SHORT).show();
                break;

            case R.id.editText_alertDialog_manualInput:
                isManualInputSet = true;
                isHalfHourButtonClicked = false;
                isHourButtonClicked = false;
                clearFocusView(btnHour);
                clearFocusView(btnHalfHour);
                break;
//                Toast.makeText(getActivity(), isHalfHourButtonClicked + " : " + isHourButtonClicked + " : " + isManualInputSet, Toast
//                        .LENGTH_SHORT).show();
        }
    }


    /**
     * setUp Google Map and its corresponding attributes.
     */
    private void setupGoogleMap() {
        // Gets to GoogleMap from the MapView and does initialization stuff
        googleMap = mapView.getMap();

        if (googleMap != null) {
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Updates the location and zoom of the MapView
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.773972, -122.431297), 10);
            googleMap.animateCamera(cameraUpdate);
        } else {
            Log.d(AppUtils.LOGTAG, "Map null");
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    // collect all the details of an Activity and pass it on to Parent Activity
    @Override
    public void onClick(View v) {

        /*
        Validate all the input Strings and once the validation passes, create the activity object and pass it to parent Activity
         */
        if (validateString(0) && validateNumber(1) && validateNumber(2)) {
            String activityName = et_activityName.getText().toString();
            int totalPeople = Integer.parseInt(et_totalPeople.getText().toString());
            int totalPets = Integer.parseInt(et_totalPets.getText().toString());

            // create a new Activites Object (Model).
            newActivityObj = new Activities(activityName, totalPeople, totalPets, AppUtils.getCurrentTimeStamp());

            // set the state of Currently running activity to Running.
            newActivityObj.setState(Activities.STATE.RUNNING);

            mInterface.onPlayButtonClick(newActivityObj);
        } else {
            return;
        }
    }

    // validate the input String be it first name, last name, address etc
    private boolean validateString(int checkCode) {
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


    /**
     * Interface Callback to define the callback methods for ActivityNew fragment
     */
    public interface IActivityNewCallBack {
        /**
         * Callback method when User clicks on the Play Button defined in the ActivityNew Fragment.
         *
         * @param newActivityDetails
         */
        public void onPlayButtonClick(Activities newActivityDetails);
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
                    validateString(1);
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
