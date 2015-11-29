package com.sfsu.application;

import android.app.Application;

import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.network.handler.UserRequestHandler;
import com.squareup.otto.Bus;

/**
 * Contains all the application level components which are needed to be initialized during the run time of app
 * <p/>
 * Created by Pavitra on 11/27/2015.
 */
public class InvestickationApp extends Application {
    public static final String LOGTAG = InvestickationApp.class.getSimpleName();

    private static ApiRequestHandler mApiRequestHandler;
    private static UserRequestHandler mUserRequestHandler;

    private static InvestickationApp mInstance;

    private Bus mBus = BusProvider.bus();

    public static synchronized InvestickationApp getInstance() {
        return mInstance;
    }

    /*
    Register the Bus,
    initialize the ApiRequestHandler and
    initialize the InvestickationApp instance to enforce singleton pattern.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        mApiRequestHandler = new ApiRequestHandler(mBus);
        mUserRequestHandler = new UserRequestHandler(mBus);
        mBus.register(mApiRequestHandler);
    }
}
