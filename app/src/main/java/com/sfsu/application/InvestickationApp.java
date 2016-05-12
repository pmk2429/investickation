package com.sfsu.application;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.handler.ActivityRequestHandler;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.network.handler.FileUploadHandler;
import com.sfsu.network.handler.LocationRequestHandler;
import com.sfsu.network.handler.ObservationRequestHandler;
import com.sfsu.network.handler.TickRequestHandler;
import com.sfsu.network.handler.UserRequestHandler;
import com.squareup.otto.Bus;

import net.danlew.android.joda.JodaTimeAndroid;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * <p>
 * Contains all the application level components which are needed to be initialized during the run time of app. In addition,
 * when the application opens, a {@link com.sfsu.service.PeriodicDataUploadService} will start which will collect all the data
 * form the Database and notify user for any data stored in the {@link android.database.sqlite.SQLiteDatabase}.
 * </p>
 * <p>
 * The application onCreate will initialize
 * </p>
 * <p>
 * Created by Pavitra on 11/27/2015.
 */

/**
 * Sends Crash report to the Cloudant server configured to generate reports.
 */
@ReportsCrashes(
        formUri = "https://tick2429.cloudant.com/acra-investmain/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "nguelyingplairropedsheye",
        formUriBasicAuthPassword = "55cb103d3c92483379c1c2fbca2665f5ea7246d1",
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        }
)

public class InvestickationApp extends Application {
    public static final String TAG = "~!@#$" + InvestickationApp.class.getSimpleName();

    // single object of application
    private static InvestickationApp mInstance;
    // define request handlers
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
    1) RegisterFragment the Otto EventBus,
    2) Initialize the ApiRequestHandler and Entity specific RequestHandlers
    3) Initialize the InvestickationApp instance to enforce singleton pattern.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        // initialize this instance
        mInstance = this;

        initResources();

        initDatabaseResources();

        // init SharedPreferences Settings
        PreferenceManager.setDefaultValues(this, R.xml.pref_user_settings, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

    /**
     * Initializes all the resources for the application with Application Context.
     */
    public void initResources() {

        // initialize the ApiRequestHandler to get access token.
        mApiRequestHandler = new ApiRequestHandler(mBus, this);

        mUserRequestHandler = new UserRequestHandler(mBus, this);
        mBus.register(mUserRequestHandler);

        mActivityRequestHandler = new ActivityRequestHandler(mBus, this);
        mBus.register(mActivityRequestHandler);

        mObservationRequestHandler = new ObservationRequestHandler(mBus, this);
        mBus.register(mObservationRequestHandler);

        mLocationRequestHandler = new LocationRequestHandler(mBus, this);
        mBus.register(mLocationRequestHandler);

        mTickRequestHandler = new TickRequestHandler(mBus, this);
        mBus.register(mTickRequestHandler);

        mFileUploadHandler = new FileUploadHandler(mBus, this);
        mBus.register(mFileUploadHandler);

        JodaTimeAndroid.init(this);
    }

    // init database resources
    private void initDatabaseResources() {

    }

}
