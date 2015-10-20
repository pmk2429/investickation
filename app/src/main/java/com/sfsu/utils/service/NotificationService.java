package com.sfsu.utils.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sfsu.investickation.R;
import com.sfsu.investickation.fragments.ActivityNew;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.notification_template_icon_bg, "Notify Alarm strart", System.currentTimeMillis());
        Intent myIntent = new Intent(this, ActivityNew.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
        notification.setLatestEventInfo(this, "Notify label", "Notify text", contentIntent);
        mNM.notify(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
