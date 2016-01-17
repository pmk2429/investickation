package com.sfsu.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.security.Policy;

/**
 * Service to get the periodic EntityLocation updates while the Account performed Activity is still Running. This service runs in tandem
 * with the {@link com.sfsu.investickation.fragments.ActivityRunning} fragment. In other words, when the <tt>ActivityRunning</tt>
 * is created the {@link LocationService } starts and when the <tt>ActivityRunning</tt> is paused/stopped, the
 * {@link LocationService } is stopped too.
 * <p>
 * The LocationService service creates a EntityLocation object and sends it over to the currently running activity which will
 * post the data on the server. This way after every 10 minutes, a EntityLocation will be captured and sent over to the server
 * for getting user's location and finally these locations will be used to depict the probable trajectory of the user.
 */
public class LocationService extends Service implements LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static final String KEY_LOCATION_CHANGED = "location_changes";
    public static final String BROADCAST_ACTION = "com.sfsu.investickation.fragments.LOCATION";
    private static final String TAG = "~!@#$LocServ";
    // EntityLocation updates intervals in sec
    private final static int _SECOND = 1000;
    private final static int _MIN = 60 * _SECOND;
    private final static int UPDATE_INTERVAL = 15 * _SECOND; // 5 mins
    private final static int FASTEST_INTERVAL = 10 * _SECOND; // 5 sec
    private final static int DISPLACEMENT = 10; // 10 meters
    // delay in broadcasting the location
    private final static int DELAY_BROADCAST = 15 * _SECOND;
    // Handler communicate between this Thread and UI thread.
    private final Handler handler = new Handler();
    // getting the LocationObject.
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates;
    // getting the EntityLocation requests
    private LocationRequest mLocationRequest;
    private Intent intent;
    private int counter = 0;
    /**
     *
     */
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            broadcastLocationInfo();
            handler.postDelayed(this, DELAY_BROADCAST); // 5 seconds
        }
    };

    /**
     * send the location broadcast to the Activity
     */
    private void broadcastLocationInfo() {
        Log.i(TAG, "entered broadcast logging info method");
        if (intent != null) {
            Log.i(TAG, "intent not null");
            Log.i(TAG, "" + mLastLocation.getLatitude() + " : " + mLastLocation.getLongitude());
            intent.putExtra(KEY_LOCATION_CHANGED, mLastLocation);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestingLocationUpdates = false;

        /*initialize the Params in the onCreate of the Service. Since building GoogleApiClient and creating LocationRequests
        needs to be done only once,
         */
        intent = new Intent(BROADCAST_ACTION);
        // check availability of play services
        if (isGooglePlayServicesAvailable()) {
            Log.i(TAG, "play services available and so initializing the api, location requests.");
            // Building the GoogleApi client
            buildGoogleApiClient();
            // finally onConnect, handle the LocationUpdates
            mGoogleApiClient.connect();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStart called");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 5000); // 5 second delay
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * util method to check if GooglePlayServicesAvailable or not.
     *
     * @return
     */
    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i(TAG, " Error in resultCode");
            } else {
                Log.i(TAG, " This device is not supported");
            }
            return false;
        }
        return true;
    }

    /**
     * start the Fused location updates
     */
    protected void startLocationUpdates() {
        Log.i(TAG, "starting location updates");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * stop the Fused location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    /**
     * Build the GoogleApi client.
     */
    public synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building the Google API Client.");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * create the LocationRequest object by specifying all the params needed to initialize the object.
     */
    public void createLocationRequest() {
        Log.i(TAG, "Creating EntityLocation Requests.");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }


    // Methods to handle the LocationListener for the
    @Override
    public void onConnected(Bundle bundle) {
        // Once connected with google api, get the location
        Log.i(TAG, "onConnected called");
        if (!mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    // Callback for ConnectionCallbacks interface
    @Override
    public void onConnectionSuspended(int i) {

    }

    // Callbacks for LocationListener interface. This callback will then create a EntityLocation Object out of new EntityLocation values.
    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        Toast.makeText(this, counter++ + ")" + location.getLatitude() + ":" + location.getLongitude(), Toast.LENGTH_LONG).show();
    }


    // call back for OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }


    @Override
    public boolean stopService(Intent name) {
        super.stopService(name);
        stopLocationUpdates();
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        handler.removeCallbacks(sendUpdatesToUI);
    }
}
