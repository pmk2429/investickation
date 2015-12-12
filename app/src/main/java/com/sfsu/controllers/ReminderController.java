package com.sfsu.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.sfsu.receiver.ReminderReceiver;

/**
 * Created by Pavitra on 12/11/2015.
 */
public class ReminderController {

    private Context mContext;
    private long selectedInterval;

    /**
     * Initialize the Alarm Manager for the current Context.
     *
     * @param context
     */
    public ReminderController init(Context context) {
        this.mContext = context;
        return this;
    }

    /**
     * Build the ReminderController for the given context and interval.
     */
    public void schedule() {
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(mContext, ReminderReceiver.class);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, 0, alarmIntent, 0);

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + selectedInterval,
                selectedInterval, mPendingIntent);

    }

    /**
     * Set the interval to trigger the alarm and also to set the interval for repeating the alarm on specific intervals.
     *
     * @param selectedInterval
     * @return
     */
    public ReminderController interval(long selectedInterval) {
        this.selectedInterval = selectedInterval;
        return this;
    }
}
