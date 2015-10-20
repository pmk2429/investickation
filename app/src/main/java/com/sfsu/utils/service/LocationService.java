package com.sfsu.utils.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {
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
}
