package com.sfsu.controllers;

import android.app.Application;

/**
 * AppController is an application level Controller which extends {@link Application} and is executed on the App launch to
 * initialize all the necessary application resources such as Retrofit Controller, Location Objects etc.
 * <p/>
 * Created by Pavitra on 11/24/2015.
 */
public class AppController extends Application {
    public static final String LOGTAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
