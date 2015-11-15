package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.MapView;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.service.LocationService;
import com.sfsu.utils.AppUtils;

/**
 * This fragment provides interface to user to add {@link com.sfsu.entities.Observation} for the current ongoing
 * {@link Activities} or to just make an observation without registering for an activity. This fragment contains the action
 * callback to start new <tt>Observation</tt>. Once the Add Observation button is clicked, the user will be redirected to add
 * Observation for the current ongoing activity.
 * <p/>
 * The object passed by the UserActivityMasterActivity from ActivityNew fragment will be in RUNNING state. Hence, in Running
 * state, the Activity performs various operations such as getting Location updates, getting the updates from the
 * BroadcastReceiver, recording the observations etc.
 * <p/>
 * All these operations will be carried out when the Activity is in RUNNING state ONLY.
 * Observation,
 */
public class ActivityRunning extends Fragment {

    private MapView mapView;
    private Activities newActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private Intent locationIntent;
    private GoogleMapController mGoogleMapController;
    /**
     * BroadcastReceiver to receive the broadcast send by the FusedLocationService.
     * This receiver receives the UserLocation every specified interval of time.
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
            Log.i("~!@#$", "intent not null");
            Bundle bundle = locationIntent.getExtras();
            Location locationVal = (Location) bundle.get("locINFO");
            // TODO: get the UserLocation object from the BroadcastReceiver
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_running, container, false);

        // initialize the location intent.
        locationIntent = new Intent(mContext, LocationService.class);

        /* retrieve all the data passed from the ActivityRunning fragment.
        The retrieved object has the running state set. So perform all the operations only when state is RUNNING.
         */
        newActivityObj = getArguments().getParcelable(AppUtils.ACTIVITY_RESOURCE);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityRunning);
        mapView.onCreate(savedInstanceState);

        mGoogleMapController = new GoogleMapController(mContext);

        // setup google Map.
        mGoogleMapController.setupGoogleMap(mapView);


        // initialize the FAB
        final FloatingActionButton stopActivity = (FloatingActionButton) v.findViewById(R.id.fab_activity_stop);
        stopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // on stop button click the newActivityObj will be passed on to Retrofit Controller
                // pass the newActivityObj to the callback method and let Activity handle the data processing
                mListener.onActivityStopButtonClicked(newActivityObj);
            }
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // start the Service on onResume and register for broadcast receiver too.
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

        // start the service onResume of the Fragment.
        getActivity().startService(locationIntent);

        // register the broadcast receiver to receive the broadcast data
        getActivity().registerReceiver(locationBroadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));
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

    // unregister the broadcast receiver in the onPause.
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(locationBroadcastReceiver);
    }

    /**
     * Callback Interface for defining the callback methods to the UserActivityMasterActivity Activity.
     */
    public interface IActivityRunningCallBacks {

        /**
         * Call back method when the user clicks on the Stop button in this Fragment. The newly created Activity object
         * is passed  to the UserActivityMasterActivity where it is sent over to Retrofit for storing on the server.
         *
         * @param mNewActivityObj
         */
        public void onActivityStopButtonClicked(Activities mNewActivityObj);
    }

}
