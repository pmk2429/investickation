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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.service.LocationService;
import com.sfsu.utils.AppUtils;

/**
 * A simple fragment to make the user Add observations for the current ongoing {@link Activities}. This fragment
 * contains the action callback to start new <tt>Observation</tt>. Once the Add Observation button is clicked, the
 * user will be redirected to Add Observation for the current ongoing activity.
 * In addition to the current
 */
public class ActivityRunning extends Fragment {

    MapView mapView;
    GoogleMap googleMap;
    private Activities newActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private Intent locationIntent;
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

    public static ActivityRunning newInstance(String param1, String param2) {
        ActivityRunning fragment = new ActivityRunning();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        // retrieve all the data passed from the ActivityRunning fragment.
        newActivityObj = getArguments().getParcelable(AppUtils.ACTIVITY_RESOURCE);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView_activityRunning);
        mapView.onCreate(savedInstanceState);

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
            Log.d(AppUtils.LOGTAG, "Map is null");
        }

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
         * is passed to the UserActivityMasterActivity where it is sent over to Retrofit for storing on the server.
         *
         * @param mNewActivityObj
         */
        public void onActivityStopButtonClicked(Activities mNewActivityObj);
    }

}
