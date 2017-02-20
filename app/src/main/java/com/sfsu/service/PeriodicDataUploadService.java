package com.sfsu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;

import java.util.List;

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
    public static final String TAG = "~!@#$PeriodicService";
    private static final long TIME_INTERVAL = 1000 * 3 * 60;// 30
    private final Context mContext;
    private DatabaseDataController dbControllerActivities, dbControllerObservations;

    public PeriodicDataUploadService(Context context) {
        this.mContext = context;
        dbControllerActivities = new DatabaseDataController(mContext, ActivitiesDao.getInstance());
        dbControllerObservations = new DatabaseDataController(mContext, ObservationsDao.getInstance());
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
        Log.i(TAG, "Service started");
        // save in sqlite after 10 mints
        List<Activities> activitiesList = (List<Activities>) dbControllerActivities.getAll();
        List<Observation> observationList = (List<Observation>) dbControllerObservations.getAll();

        // check for activitiesList
        if (activitiesList != null && activitiesList.size() > 0) {
            // TODO upload Activities to server
        } else {
            Log.i(TAG, "no activities");
        }

        // check for observationList
        if (observationList != null || observationList.size() > 0) {
            // TODO upload Activities to server
        } else {
            Log.i(TAG, "no observations");
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent dataCheckIntent = new Intent(this, PeriodicDataUploadService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, dataCheckIntent, 0);
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
        dbControllerObservations.closeConnection();
        dbControllerActivities = null;
        dbControllerObservations = null;
        Toast.makeText(this, "service stoped", Toast.LENGTH_SHORT).show();
    }
}

