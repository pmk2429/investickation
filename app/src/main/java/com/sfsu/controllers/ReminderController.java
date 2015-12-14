package com.sfsu.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.sfsu.receiver.ReminderReceiver;

/**
 * Created by Pavitra on 12/11/2015.
 */
public class ReminderController {

    private Context mContext;
    private long selectedInterval;
    private PendingIntent mPendingIntent;
    private Intent mAlarmIntent;

    /**
     * Initialize the Alarm Manager for the current Context. Initialze the PendingIntent and Intent for current context.
     *
     * @param context
     */
    public ReminderController init(Context context) {
        this.mContext = context;
        mAlarmIntent = new Intent(mContext, ReminderReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, mAlarmIntent, 0);
        return this;
    }

    /**
     * Build the ReminderController for the given context and interval.
     */
    public void schedule() {
        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + selectedInterval,
                selectedInterval, mPendingIntent);
        Toast.makeText(mContext, "Reminder started", Toast.LENGTH_SHORT).show();

    }

    /**
     * Stops the reminder.
     */
    public void cancel() {
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mPendingIntent);
        Toast.makeText(mContext, "Reminder stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * Set the interval to trigger the alarm and also to set the interval for repeating the alarm on specific intervals.
     *
     * @param selectedInterval
     * @return
     */
    public ReminderController interval(long selectedInterval) {
        if (selectedInterval != 0 && selectedInterval != -1)
            this.selectedInterval = selectedInterval == 30000 ? AlarmManager.INTERVAL_HALF_HOUR : AlarmManager.INTERVAL_HOUR;
        return this;
    }

    /**
     * Returns if the Alarm is already set in AlarmManager.
     *
     * @return
     */
    public boolean isSet() {
        return (PendingIntent.getBroadcast(mContext, 0, new Intent("com.sfsu.receiver.ReminderReceiver"),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
