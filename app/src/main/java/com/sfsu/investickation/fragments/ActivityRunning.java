package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.service.LocationService;
import com.sfsu.utils.AppUtils;

/**
 * This fragment provides interface to user to add {@link com.sfsu.entities.Observation} for the current ongoing
 * {@link Activities} or to just make an observation without registering for an activity. This fragment contains the action
 * callback to start new <tt>Observation</tt>. Once the Add Observation button is clicked, the user will be redirected to add
 * Observation for the current ongoing activity.
 * <p>
 * The object passed by the UserActivityMasterActivity from ActivityNew fragment will be in RUNNING state. Hence, in Running
 * state, the Activity performs various operations such as getting Location updates, getting the updates from the
 * BroadcastReceiver, recording the observations etc.
 * <p>
 * All these operations will be carried out when the Activity is in RUNNING state ONLY.
 * Observation,
 */
public class ActivityRunning extends Fragment {

    private final String LOGTAG = "~!@#$ActivityRunning :";
    private MapView mapView;
    private Activities currentActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private Intent locationIntent;
    private LocationController mLocationController;
    private GoogleMapController mGoogleMapController;
    private CardView btn_addObservation;
    private TextView txtView_activityName;
    private AuthPreferences mAuthPreferences;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_running, container, false);

        btn_addObservation = (CardView) v.findViewById(R.id.cardView_activityRunning_addObservation);
        txtView_activityName = (TextView) v.findViewById(R.id.textView_actRun_activityName);

        // initialize the location intent.
        locationIntent = new Intent(mContext, LocationService.class);

        /* retrieve all the data passed from the ActivityRunning fragment.
        The retrieved object has the running state set. So perform all the operations only when state is RUNNING.
         */
        currentActivityObj = getArguments().getParcelable(AppUtils.ACTIVITY_RESOURCE);
        Log.i(LOGTAG, currentActivityObj.toString());
        Log.i(LOGTAG, currentActivityObj.getState() + "");

        mAuthPreferences = new AuthPreferences(mContext);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityRunning);
        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);

        mLocationController = new LocationController(mContext, this);
        mGoogleMapController = new GoogleMapController(mContext, this);
        // connect to GoogleAPI and setup FusedLocationService to get the Location updates.
//        mLocationController.connectGoogleApi();

        // setup google Map.
        mGoogleMapController.setupGoogleMap(mapView);

        // when the newActivityObject is retrieved from the Intent, create a StringBuilder and set the text to TextView
        StringBuilder textViewData = new StringBuilder();
        textViewData.append(currentActivityObj.getActivityName() + " @ " + currentActivityObj.getLocation_area());
        txtView_activityName.setText(textViewData.toString());

        // initialize and set onClickListener for FAB
        final FloatingActionButton stopActivity = (FloatingActionButton) v.findViewById(R.id.fab_actRun_activityStop);
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // onStop button clics, change the state of Activity to CREATED.
                currentActivityObj.setState(Activities.STATE.CREATED);

                /* currentActivityObj will be passed on to Retrofit Controller pass the currentActivityObj to the callback method
                and lets the Activity handle the data processing.
                 */
                mListener.onActivityStopButtonClicked(currentActivityObj);
            }
        });

        // Open the New Observation Fragment in button click.
        btn_addObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddNewObservationButtonClicked(currentActivityObj.getUUID());
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* start the Service on onResume and register for broadcast receiver too only if the Activity state is RUNNING.
    So check for the state of the ActivtyObjet and then perform startService as well as register for Receiver.
    */

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

        // perform check for RUNNING state of Activity
        if (currentActivityObj.getState() == Activities.STATE.RUNNING) {

            // start the service to capture Location updates.
            getActivity().startService(locationIntent);

            // register the broadcast receiver to receive the location objects as broadcast data
            getActivity().registerReceiver(locationBroadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
        }

        BusProvider.bus().register(this);
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
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Callback Interface for defining the callback methods to the UserActivityMasterActivity Activity.
     * 1) Unregister the broadcast receiver in the onPause.
     * 2)
     */
    @Override
    public void onPause() {
        super.onPause();
        // stop the service and unregister the broadcast receiver.
        getActivity().unregisterReceiver(locationBroadcastReceiver);
        getActivity().stopService(locationIntent);
        BusProvider.bus().unregister(this);
    }

    /**
     * Callback Interface for handling onClick Listeners in <tt>ActivityRunning</tt> Fragment.
     */
    public interface IActivityRunningCallBacks {

        /**
         * Callback method when the user clicks on the Stop button in <tt>ActivityRunning</tt> Fragment. The newly created
         * Activity object is passed  to the UserActivityMasterActivity where it is sent over to Retrofit for storing on the server.
         *
         * @param currentActivityObj Current Ongoing Activity Object passed from the ActivityNew
         */
        public void onActivityStopButtonClicked(Activities currentActivityObj);


        /**
         * Callback method to handle the new AddObservation button click in <tt>ActivityNew</tt> Fragment. This method takes
         * in the unique UUID of the Activity currently ongoing to determing that the Observation is under a specific activity.
         *
         * @param currentActivityUUID
         */
        public void onAddNewObservationButtonClicked(String currentActivityUUID);
    }

}
