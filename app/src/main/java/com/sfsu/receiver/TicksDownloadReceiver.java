package com.sfsu.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pavitra on 5/15/2016.
 */
public class TicksDownloadReceiver extends BroadcastReceiver {
    private static final String TAG = "~!@#$TicksReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ");
        int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);

        if (resultCode == Activity.RESULT_OK) {
            // this means all went well and we have Ticks downloaded and saved to DB
            String resultValue = intent.getStringExtra("resultValue");
            Log.i(TAG, "onReceive: success");
        } else {
            // something went wrong
            Log.i(TAG, "onReceive: failure");
        }
    }
}
