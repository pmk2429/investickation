package com.sfsu.application;

import android.app.Application;

import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Bus;

/**
 * Contains all the application level components which are needed to be initialized during the run time of app
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class InvestickationApp extends Application {
    private static ApiRequestHandler mApiRequestHandler;
    private Bus mBus = BusProvider.bus();

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRequestHandler = new ApiRequestHandler(mBus);
        mBus.register(mApiRequestHandler);
    }
}
