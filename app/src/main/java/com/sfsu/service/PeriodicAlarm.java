package com.sfsu.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pavitra on 1/15/2016.
 */
public class PeriodicAlarm {
    public final static String ONE_TIME = "onetime";
    public static final String BROADCAST_ACTION = "com.sfsu.investickation.fragments.ALARM";
    private Context mContext;

    public PeriodicAlarm(Context mContext) {
        this.mContext = mContext;
    }

    public void setAlarm(long interval) {
        Log.i("~!@#$NotAct", "alarm starting");
        Intent alarmIntent = new Intent(BROADCAST_ACTION);
        alarmIntent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, 0);
        //After 30 seconds
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, mPendingIntent);
    }

    public void cancelAlarm() {
        Log.i("~!@#$NotAct", "alarm stopping");
        Intent intent = new Intent(BROADCAST_ACTION);
        PendingIntent mSenderPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(mSenderPendingIntent);
    }
}
