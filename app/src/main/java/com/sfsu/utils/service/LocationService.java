package com.sfsu.utils.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

// TODO: implement the LocationService to get LocationUpdates using FusedLocation and also set the BroadcastReceiver to send
// the data to Activity once the service gets the Location
public class LocationService extends Service implements LocationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static final String BROADCAST_ACTION = "com.sfsu.investickation";
    private static final String TAG = "LocationService";
    private final Handler handlerObj = new Handler();
    int counter = 0;
    private Intent locationIntent;

    // Deafault Constructor
    public LocationService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Methods to handle the LocationListener for the
    @Override
    public void onConnected(Bundle bundle) {

    }

    // Callback for ConnectionCallbacks interface
    @Override
    public void onConnectionSuspended(int i) {

    }

    // Callbacks for LocationListener interface.
    @Override
    public void onLocationChanged(Location location) {

    }

    // call back for OnConnectionFailedListener
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
