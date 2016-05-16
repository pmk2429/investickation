package com.sfsu.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v4.content.LocalBroadcastManager;
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
 * Created by Pavitra on 5/14/2016.
 */
public class DownloadTickIntentService extends IntentService {

    public static final String ACTION = "com.sfsu.service.DownloadTickIntentService";
    private static final String TAG = "~!@#$DnldTickIntentSer";
    private Context mContext;
    private DatabaseDataController dbTicksController;
    private List<Tick> tickList;
    private long[] resultCodes;
    private int counter;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadTickIntentService() {
        super("DownloadTicksIntent");
        mContext = DownloadTickIntentService.this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        BusProvider.bus().register(this);
        dbTicksController = new DatabaseDataController(this, TickDao.getInstance());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // let the EventBus handle the networking for us!
        BusProvider.bus().post(new TickEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL_TICKS));
    }

    /**
     * Subscribes to event of success in downloading {@link com.sfsu.entities.Tick} from the server
     *
     * @param onLoaded
     */
    @Subscribe
    public void onTicksDownLoadSuccess(TickEvent.OnListLoaded onLoaded) {
        Log.i(TAG, "onTicksDownLoadSuccess: ");
        // if already downloaded, then set to false
        // Had to do this since the EventHandler was getting called twice
        tickList = onLoaded.getResponseList();
        saveTicksToDatabase();

        // finally send the broadcast using local intent
        Intent ticksDownloadedIntent = new Intent(ACTION);
        ticksDownloadedIntent.putExtra("resultCode", Activity.RESULT_OK);
        LocalBroadcastManager.getInstance(this).sendBroadcast(ticksDownloadedIntent);
    }

    /**
     * Save all the downloaded Ticks to the DB for referencing while offline
     */
    private void saveTicksToDatabase() {
        Log.i(TAG, "saveTicksToDatabase: ");
        try {
            if (tickList != null && tickList.size() > 0) {
                // init the array
                resultCodes = new long[tickList.size()];
                // save to db
                for (int i = 0; i < tickList.size(); i++) {
                    // get the tick from DB
                    Tick mTick = tickList.get(i);
                    resultCodes[i] = dbTicksController.save(mTick);
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

    /**
     * Subscribes to event of failure in downloading {@link com.sfsu.entities.Tick} from the server
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onTicksDownLoadFailure(TickEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, "onTicksDownLoadFailure: " + onLoadingError.getErrorMessage());
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        dbTicksController.closeConnection();
        BusProvider.bus().unregister(this);
        stopSelf();
    }
}
