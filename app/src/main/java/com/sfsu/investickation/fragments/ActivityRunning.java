package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.entities.Account;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.map.StaticMap;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.service.LocationService;
import com.sfsu.service.PeriodicAlarm;
import com.sfsu.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Provides interface to user to add {@link com.sfsu.entities.Observation} for the current ongoing
 * {@link Activities}. At this point in time, the {@link Activities} is created on the server and the state of Activity is
 * <tt>RUNNING</tt>. So only when the Activity is in RUNNING state, the {@link Account} can make observations
 * or receive location updates. This fragment the action callback to start new <tt>Observation</tt>.
 * Once the Add Observation button is clicked, the user will be redirected to add Observation for the current ongoing activity.
 * <p>
 * The object passed by the UserActivityMasterActivity from ActivityNew fragment will be in RUNNING state. Hence, in Running
 * state, the Activity performs various operations such as getting Location updates, getting the updates from the
 * BroadcastReceiver, recording the observations etc.
 * </p>
 * <p>
 * All these operations will be carried out when the Activity is in RUNNING state ONLY.
 * </p>
 * <p>Once the user clicks on <tt>'Add Observation'</tt> button, the {@link AddObservation} Fragment is opened and
 * the control is redirected to new Fragment inside {@link com.sfsu.investickation.ObservationMasterActivity}. When this happens,
 * the current ongoing activity is stored in SharedPreferences and retrieved when the user returns back to the same Fragment.</p>
 * Observation,
 */
public class ActivityRunning extends Fragment implements LocationController.ILocationCallBacks, View.OnClickListener {

    public static final String TAG = "~!@#$ActivityRunning";
    // FAB
    @Bind(R.id.fab_actRun_activityStop)
    FloatingActionButton stopActivity;
    // Map
    @Bind(R.id.mapView_activityRunning)
    MapView mapView;
    // CardView
    @Bind(R.id.cardView_activityRunning_addObservation)
    CardView btn_addObservation;
    // TextView
    @Bind(R.id.textView_actRun_activityName)
    TextView txtView_activityName;
    LatLng[] mLatLngs;
    private Activities ongoingActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private LocationController mLocationController;
    private GoogleMapController mGoogleMapController;
    private Bundle args;
    private boolean FLAG_RUNNING;
    private SharedPreferences activityPref;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<LatLng> mLatLngList = new ArrayList<>();
    private Intent locationIntent;

    /**
     * BroadcastReceiver to receive the updates when Location is changed.
     */
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUi(intent);
        }
    };

    /**
     * BroadcastReceiver to receive the updates when the Alarm goes off
     */
    private BroadcastReceiver alarmBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "alarm ticked");
            PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WAKING CLOCK");
            //Acquire the lock
            mWakeLock.acquire();

            Bundle intentExtras = intent.getExtras();

            if (intentExtras != null && intentExtras.getBoolean(PeriodicAlarm.ONE_TIME, Boolean.FALSE)) {
                // timer for one time
            }

            // open alert dialog
            openAlarmDialog();

            //Release the lock
            mWakeLock.release();
        }
    };

    public ActivityRunning() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link ActivityRunning} fragment with bundle.
     *
     * @param key
     * @param mActivity
     * @return
     */
    public static ActivityRunning newInstance(String key, Activities mActivity) {
        ActivityRunning mActivityRunning = new ActivityRunning();
        Bundle args = new Bundle();
        args.putParcelable(key, mActivity);
        mActivityRunning.setArguments(args);
        return mActivityRunning;
    }

    private void updateUi(Intent intent) {
        Location mLocation = intent.getParcelableExtra(LocationService.KEY_LOCATION_CHANGED);
        if (mLocation != null) {
            LatLng mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            mLatLngList.add(mLatLng);
        }
    }

    /**
     * Collects the location every specified interval of time.
     *
     * @param locationIntent
     */
    private void collectLocationData(Intent locationIntent) {
        if (locationIntent != null) {
            Location location = (Location) locationIntent.getExtras().get(LocationService.KEY_LOCATION_CHANGED);
        }
    }

    /**
     * Open AlertDialog when the Alarm goes off
     */
    private void openAlarmDialog() {

        AlertDialog.Builder alarmReminderDialog = new AlertDialog.Builder(mContext);
        alarmReminderDialog.setTitle("Found any Tick?");
        alarmReminderDialog.setMessage("Dont forget to add Observation for Tick");

        alarmReminderDialog.setPositiveButton("Open Observation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("~!@#$NotAct", "Open Observation");
            }
        });

        alarmReminderDialog.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("~!@#$NotAct", "Continue");
            }
        });

        alarmReminderDialog.setNeutralButton("Cancel Timer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("~!@#$NotAct", "Cancel Timer");
            }
        });

        alarmReminderDialog.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityRunningCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityRunningCallBacks interface");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_running);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_running, container, false);

        // bind the views
        ButterKnife.bind(this, rootView);

        // initialize the location intent.
        mLocationController = new LocationController(mContext, this);
        mGoogleMapController = new GoogleMapController(mContext, this);

        try {
            if (getArguments() != null) {
                args = getArguments();
            }
            gson = new Gson();
            activityPref = mContext.getSharedPreferences(UserActivityMasterActivity.PREF_ACTIVITY_DATA, Context.MODE_PRIVATE);

            // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
            final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
            mapView.onCreate(mapViewSavedInstanceState);


            // connect to GoogleAPI and setup FusedLocationService to get the Location updates.
            if (AppUtils.isConnectedOnline(mContext)) {
                mLocationController.connectGoogleApi();
            }

            mLocationController.startLocationUpdates();


            // setup google Map.
            mGoogleMapController.setupGoogleMap(mapView);
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return rootView;
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

        // retrieve the data from Arguments if the Fragment is opened for first time, or from the SharedPreferences.
        if (args != null) {
            if (args.getParcelable(UserActivityMasterActivity.KEY_RUNNING_ACTIVITY) != null) {
                ongoingActivityObj = args.getParcelable(UserActivityMasterActivity.KEY_RUNNING_ACTIVITY);
                Log.i(TAG, ongoingActivityObj.toString());
            }
        } else {
            // get data from SharedPref
            String activityJson = activityPref.getString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, "no-data");
            ongoingActivityObj = gson.fromJson(activityJson, Activities.class);
        }

        // check if Activities object is not null.
        if (ongoingActivityObj != null) {
            populateView();
        }

        // perform check for RUNNING state of Activity and if true, register receiver for getting location updates.
        if (FLAG_RUNNING) {
            locationIntent = new Intent(mContext, LocationService.class);
            // start the service to capture Location updates.
            mContext.startService(locationIntent);

            // register for BroadcastReceiver
            mContext.registerReceiver(locationReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));

        }

        // register AlarmReceiver
        mContext.registerReceiver(alarmBroadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));

        // register for Event Bus
        BusProvider.bus().register(this);
    }


    /**
     * Helper method to populate the View in this Fragment.
     */
    private void populateView() {
        // when the newActivityObject is retrieved from the Intent, create a StringBuilder and set the text to TextView
        StringBuilder textViewData = new StringBuilder();
        if (AppUtils.isConnectedOnline(mContext)) {
            textViewData.append(ongoingActivityObj.getActivityName() + " @ " + ongoingActivityObj.getLocation_area());
        } else {
            textViewData.append(ongoingActivityObj.getActivityName());
        }
        txtView_activityName.setText(textViewData.toString());

        // Open the New Observation Fragment in button click and pass the activityId to make Activity related Observations.
        btn_addObservation.setOnClickListener(this);

        // remove the SharedPreferences and set state of the ongoing activity to CREATED.
        stopActivity.setOnClickListener(this);

        FLAG_RUNNING = ongoingActivityObj.getState() == Activities.STATE.RUNNING ? true : false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // store the information on map.
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();
        // save the Currently running object in SharedPref to retrieve it later using editor.
        editor = activityPref.edit();
        String activityJson = gson.toJson(ongoingActivityObj);
        editor.putString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, activityJson);
        editor.apply();
        mContext.unregisterReceiver(alarmBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationController.stopLocationUpdates();
        mGoogleMapController.clear();
        mLatLngList = null;
        mContext.unregisterReceiver(locationReceiver);
        mContext.stopService(locationIntent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setCurrentLocation(Location mLocation) {

    }

    @Override
    public void setLatLng(LatLng mLatLng) {
        if (mLatLng != null) {
            mLatLngList.add(mLatLng);
        }

    }

    @Override
    public void setLocationArea(String locationArea) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView_activityRunning_addObservation:
                mListener.onAddNewObservationClicked(ongoingActivityObj.getId());
                break;

            case R.id.fab_actRun_activityStop:
                updateRunningActivity();
                break;
        }
    }

    /**
     * Helper method to UPDATE the ongoing Activity for Location updates and adding LatLng.
     */
    private void updateRunningActivity() {

        try {
            // onStop button click, change the state of Activity to CREATED.
            ongoingActivityObj.setState(Activities.STATE.CREATED);

            if (mLatLngList != null) {
                mLatLngs = mLatLngList.toArray(new LatLng[mLatLngList.size()]);
            }

            // build the imageUrl
            String imageUrl = new StaticMap
                    .UrlBuilder()
                    .init()
                    .zoom(14)
                    .size(650, 350)
                    .path(mLatLngs)
                    .build();

            ongoingActivityObj.setImage_url(imageUrl);

            Log.i(TAG, ongoingActivityObj.toString());

            // delete the SharedPref data
            activityPref.edit().remove(UserActivityMasterActivity.PREF_ACTIVITY_DATA).apply();

            mContext.stopService(locationIntent);

            // pass on the Activities object to the List of activities.
            //mListener.onActivityStopButtonClicked();
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }


    /**
     * Callback Interface for handling onClick Listeners in <tt>ActivityRunning</tt> Fragment.
     */
    public interface IActivityRunningCallBacks {
        /**
         * Callback method when the user clicks on the Stop button in {@link ActivityRunning} Fragment. OnClick of this button
         * will remove the SharedPreferences file which contains the Activities Object.
         * <p>The state of {@link Activities} is changed from <tt>RUNNING</tt> to <tt>CREATED</tt> and as a result the the
         * LocationController is stopped</p>
         *
         * @param currentActivityObj Current Ongoing Activity Object passed from the ActivityNew
         */
        public void onActivityStopButtonClicked();


        /**
         * Callback method to handle the new AddObservation button click in <tt>ActivityNew</tt> Fragment. This method takes
         * in the unique UUID of the Activity currently ongoing to determing that the Observation is under a specific activity.
         *
         * @param activityId
         */
        public void onAddNewObservationClicked(String activityId);
    }

}
