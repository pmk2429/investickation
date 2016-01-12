package com.sfsu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * <p>
 * PeriodicService to check for the <tt>isOnCloud</tt> flags for each {@link com.sfsu.entities.Activities} and {@link com.sfsu
 * .entities.Observation} and depending on the flags value, makes a network request if network is available via a Retrofit's
 * OkHttp Client. This ensures the total uploading state of all the user captured data on the server.
 * </p>
 * <p>
 * This service also checks for the network connection and updates the adapters.
 * </p>
 * <p>
 * In addition, the services uploads a large chunk of data altogether instead of several small uploads for efficiency and
 * better battery life.
 * <p>
 * Created by Pavitra on 12/11/2015.
 */
public class PeriodicDataUploadService extends Service {
    private static final long TIME_INTERVAL = 10000 * 60;// 10 minutes

    public static String LOG = "Log";

    private final Context mContext;

    public PeriodicDataUploadService(Context context) {
        this.mContext = context;
    }

    public PeriodicDataUploadService() {
        super();
        mContext = PeriodicDataUploadService.this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i(LOG, "Service started");
        // save in sqlite after 10 mints

        // upload data to server

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent ishintent = new Intent(this, PeriodicDataUploadService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIME_INTERVAL, pintent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent uploadIntent = new Intent(this, PeriodicDataUploadService.class);
        PendingIntent mPendingIntent = PendingIntent.getService(this, 0, uploadIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mPendingIntent);
        Toast.makeText(this, "service stoped", Toast.LENGTH_SHORT).show();
    }
}

