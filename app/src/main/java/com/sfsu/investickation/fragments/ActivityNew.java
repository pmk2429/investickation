package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
    private String reminderTimeValue;

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

                setupReminderDialog(inflater, rootView);
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
    private void setupReminderDialog(LayoutInflater inflater, View fragmentView) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        View convertView = inflater.inflate(R.layout.alertdialog_reminder, null);
        alertDialog.setTitle("Set Reminder");
        // set the onclick Listener for each buttons defined in the custom layout.
        final Button btnHalfHour = (Button) convertView.findViewById(R.id.button_alertDialog_30);
        btnHalfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderTimeValue = "30";
                toggleBackground(btnHalfHour);
            }
        });
        final Button btnHour = (Button) convertView.findViewById(R.id.button_alertDialog_60);
        btnHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderTimeValue = "60";
                toggleBackground(btnHour);
            }
        });
        EditText et_manualInput = (EditText) convertView.findViewById(R.id.editText_alertDialog_manualInput);

        // on click of the Positive Button, set the textView_Reminder value for selected option.
        alertDialog.setPositiveButton(R.string.alertDialog_apply, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

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
        // collect all the data for the New Activity posted by user.
        EditText et_activityName = (EditText) v.findViewById(R.id.editText_ActivityName);
        EditText et_totalPeople = (EditText) v.findViewById(R.id.editText_numOfPeople);
        EditText et_totalPets = (EditText) v.findViewById(R.id.editText_pets);

        // get the Values entered by the User.
        String activityName = et_activityName.getText().toString();
        int totalPeople = Integer.parseInt(et_totalPeople.getText().toString());
        int totalPets = Integer.parseInt(et_totalPets.getText().toString());

        // create a new Activites Object (Model).
        newActivityObj = new Activities(activityName, totalPeople, totalPets, AppUtils.getCurrentTimeStamp());

        // set the state of Currently running activity to Running.
        newActivityObj.setState(Activities.STATE.RUNNING);

        // TODO: collect information about Reminder
        mInterface.onPlayButtonClick(newActivityObj);
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
}
