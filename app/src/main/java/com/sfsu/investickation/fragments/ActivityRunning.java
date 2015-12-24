package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.service.LocationService;

/**
 * Provides interface to user to add {@link com.sfsu.entities.Observation} for the current ongoing
 * {@link Activities}. At this point in time, the {@link Activities} is created on the server and the state of Activity is
 * <tt>RUNNING</tt>. So only when the Activity is in RUNNING state, the {@link com.sfsu.entities.User} can make observations
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
public class ActivityRunning extends Fragment {

    public static final String TAG = "~!@#$ActivityRunning :";
    private MapView mapView;
    private Activities ongoingActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private Intent locationIntent;
    private LocationController mLocationController;
    private GoogleMapController mGoogleMapController;
    private CardView btn_addObservation;
    private TextView txtView_activityName;
    private Bundle args;
    private FloatingActionButton stopActivity;
    private boolean FLAG_RUNNING;
    private SharedPreferences activityPref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private LocationController.ILocationCallBacks mLocationListener;
    /**
     * BroadcastReceiver to receive the broadcast send by the FusedLocationService.
     * This receiver receives the EntityLocation every specified interval of time.
     */
    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {

        // simply call the method to collect the location
        @Override
        public void onReceive(Context context, Intent intent) {
            collectLocationData(intent);
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

    /**
     * Method to collect location every specified interval of time.
     *
     * @param locationIntent
     */
    private void collectLocationData(Intent locationIntent) {
        if (locationIntent != null) {
            Bundle bundle = locationIntent.getExtras();
            Location locationVal = (Location) bundle.get("locINFO");
            // TODO: get the EntityLocation object from the BroadcastReceiver
        }
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
        getActivity().setTitle("Ongoing Activity");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getArguments() != null) {
            args = getArguments();
        }
        gson = new Gson();
        activityPref = mContext.getSharedPreferences(UserActivityMasterActivity.PREF_ONGOING_ACTIVITY, Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_running, container, false);

        btn_addObservation = (CardView) v.findViewById(R.id.cardView_activityRunning_addObservation);
        txtView_activityName = (TextView) v.findViewById(R.id.textView_actRun_activityName);

        // initialize the location intent.
        locationIntent = new Intent(mContext, LocationService.class);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityRunning);
        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);

        mLocationController = new LocationController(mContext, this);
        mGoogleMapController = new GoogleMapController(mContext, this);
        // connect to GoogleAPI and setup FusedLocationService to get the Location updates.
        // mLocationController.connectGoogleApi();

        // setup google Map.
        mGoogleMapController.setupGoogleMap(mapView);

        // initialize and set onClickListener for FAB
        stopActivity = (FloatingActionButton) v.findViewById(R.id.fab_actRun_activityStop);

        return v;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        } else { // get data from SharedPref
            String activityJson = activityPref.getString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, "no-data");
            ongoingActivityObj = gson.fromJson(activityJson, Activities.class);
        }

        // check if Activities object is not null.
        if (ongoingActivityObj != null) {
            populateView();
        }

        // perform check for RUNNING state of Activity and if true, register receiver for getting location updates.
        if (FLAG_RUNNING) {
            // start the service to capture Location updates.
            getActivity().startService(locationIntent);

            // register the broadcast receiver to receive the location objects as broadcast data
            getActivity().registerReceiver(locationBroadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
        }

        BusProvider.bus().register(this);
    }


    /**
     * Helper method to populate the View in this Fragment.
     */
    private void populateView() {
        // when the newActivityObject is retrieved from the Intent, create a StringBuilder and set the text to TextView
        StringBuilder textViewData = new StringBuilder();
        textViewData.append(ongoingActivityObj.getActivityName() + " @ " + ongoingActivityObj.getLocation_area());
        txtView_activityName.setText(textViewData.toString());

        // remove the SharedPreferences and set state of the ongoing activity to CREATED.
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onStop button click, change the state of Activity to CREATED.
                ongoingActivityObj.setState(Activities.STATE.CREATED);

                // delete the SharedPref data
                activityPref.edit().remove(UserActivityMasterActivity.PREF_ONGOING_ACTIVITY).apply();

                // pass on the Activities object to the List of activities.
                mListener.onActivityStopButtonClicked();
            }
        });

        // Open the New Observation Fragment in button click and pass the activityId to make Activity related Observations.
        btn_addObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddNewObservationClicked(ongoingActivityObj.getId());
            }
        });

        FLAG_RUNNING = ongoingActivityObj.getState() == Activities.STATE.RUNNING ? true : false;
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

        // stop the service and unregister the broadcast receiver.
        getActivity().unregisterReceiver(locationBroadcastReceiver);
        getActivity().stopService(locationIntent);
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
