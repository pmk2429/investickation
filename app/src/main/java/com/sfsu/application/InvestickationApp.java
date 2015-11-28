package com.sfsu.application;

import android.app.Application;

import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Bus;

/**
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
