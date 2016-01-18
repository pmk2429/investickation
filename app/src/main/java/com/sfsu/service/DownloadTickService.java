package com.sfsu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.TickDao;
import com.sfsu.entities.Tick;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Downloads all the {@link com.sfsu.entities.Tick} from server and stores it in local db for the first time user registers
 */
public class DownloadTickService extends Service {

    public static final String TAG = "~!@#DownloadTicks";
    private final Context mContext;
    private DatabaseDataController dbTicksController;
    private List<Tick> tickList;
    private long[] resultCodes;
    private int counter;
    private boolean FLAG_DOWNLOAD;

    public DownloadTickService() {
        super();
        mContext = DownloadTickService.this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BusProvider.bus().register(this);
        dbTicksController = new DatabaseDataController(this, TickDao.getInstance());
        // make a network call and download all the Ticks from the server
        Log.i(TAG, "onCreate");
        FLAG_DOWNLOAD = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        BusProvider.bus().post(new TickEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
        return START_NOT_STICKY;

    }

    /**
     * Subscribes to event of success in downloading {@link com.sfsu.entities.Tick} from the server
     *
     * @param onLoaded
     */
    @Subscribe
    public void onTicksDownLoadSuccess(TickEvent.OnListLoaded onLoaded) {
        // if already downloaded, then set to false
        // Had to do this since the EventHandler was getting called twice
        if (FLAG_DOWNLOAD) {
            Log.i(TAG, FLAG_DOWNLOAD + " already downloaded");
            FLAG_DOWNLOAD = false;
        } else {
            tickList = onLoaded.getResponseList();
            Log.i(TAG, FLAG_DOWNLOAD + " not downloaded");
            FLAG_DOWNLOAD = true;
            saveTicksToDatabase();
        }
    }

    /**
     * Subscribes to event of failure in downloading {@link com.sfsu.entities.Tick} from the server
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onTicksDownLoadFailure(TickEvent.OnLoadingError onLoadingError) {
        stopSelf();
        Log.i(TAG, "ticks download fail");
    }

    private void saveTicksToDatabase() {
        Log.i(TAG, counter++ + " ticks download success");
        try {
            if (FLAG_DOWNLOAD) {
                Log.i(TAG, "saveToDB: " + FLAG_DOWNLOAD);
                if (tickList != null && tickList.size() > 0) {
                    Log.i(TAG, "ticksList not null");
                    // init the array
                    resultCodes = new long[tickList.size()];
                    // save to db
                    for (int i = 0; i < tickList.size(); i++) {
                        Log.i(TAG, "-> " + i);
                        // get the tick from DB
                        Tick mTick = tickList.get(i);
                        resultCodes[i] = dbTicksController.save(mTick);
                        Log.i(TAG, "result: " + resultCodes[i]);
                    }
                } else {
                    Log.i(TAG, "tickList null");
                }
            }
        } catch (NullPointerException ne) {
            stopSelf();
        } catch (SQLiteException se) {
            stopSelf();
        } catch (Exception e) {
            stopSelf();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        stopSelf();
        BusProvider.bus().unregister(this);
    }
}
