package com.sfsu.application;

import android.app.Application;

import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.handler.ActivityRequestHandler;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.network.handler.FileUploadHandler;
import com.sfsu.network.handler.LocationRequestHandler;
import com.sfsu.network.handler.ObservationRequestHandler;
import com.sfsu.network.handler.TickRequestHandler;
import com.sfsu.network.handler.UserRequestHandler;
import com.squareup.otto.Bus;

/**
 * Contains all the application level components which are needed to be initialized during the run time of app
 * <p>
 * Created by Pavitra on 11/27/2015.
 */
public class InvestickationApp extends Application {
    public static final String TAG = "~!@#$" + InvestickationApp.class.getSimpleName();

    // single object of application
    private static InvestickationApp mInstance;

    private static ApiRequestHandler mApiRequestHandler;
    private static UserRequestHandler mUserRequestHandler;
    private static ActivityRequestHandler mActivityRequestHandler;
    private static ObservationRequestHandler mObservationRequestHandler;
    private static LocationRequestHandler mLocationRequestHandler;
    private static TickRequestHandler mTickRequestHandler;
    private static FileUploadHandler mFileUploadHandler;

    // single creation of Event Bus.
    private Bus mBus = BusProvider.bus();

    /**
     * Returns the singleton instance of the Application context.
     *
     * @return
     */
    public static synchronized InvestickationApp getInstance() {
        return mInstance;
    }

    /*
    1) Register the Otto EventBus,
    2) Initialize the ApiRequestHandler and Entity specific RequestHandlers
    3) Initialize the InvestickationApp instance to enforce singleton pattern.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // initialize the ApiRequestHandler to get access token.
        mApiRequestHandler = new ApiRequestHandler(mBus);
        mApiRequestHandler.init(this);

        mUserRequestHandler = new UserRequestHandler(mBus);
        mBus.register(mUserRequestHandler);

        mActivityRequestHandler = new ActivityRequestHandler(mBus);
        mBus.register(mActivityRequestHandler);

        mObservationRequestHandler = new ObservationRequestHandler(mBus);
        mBus.register(mObservationRequestHandler);

        mLocationRequestHandler = new LocationRequestHandler(mBus);
        mBus.register(mLocationRequestHandler);

        mTickRequestHandler = new TickRequestHandler(mBus);
        mBus.register(mTickRequestHandler);

        mFileUploadHandler = new FileUploadHandler(mBus);
        mBus.register(mFileUploadHandler);
    }
}
