package com.sfsu.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * A session manager to manage the User's session in the application. The User's details such as whether s/he is logged in or
 * not is stored in a SharedPreference manager.
 * Created by Pavitra on 11/24/2015.
 */
public class SessionManager {
    // Shared preferences file name
    private static final String KEY_PREF_NAME = "InvesTICKationsLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    // LogCat tag
    private static final String LOGTAG = "~!@#$" + SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences prefSession;

    SharedPreferences.Editor editor;
    Context mContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.mContext = context;
        prefSession = mContext.getSharedPreferences(KEY_PREF_NAME, PRIVATE_MODE);
        editor = prefSession.edit();
    }

    /**
     * Sets a boolean value to the preference and commits.
     *
     * @param isLoggedIn
     */
    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
        Log.d(LOGTAG, "User Login session modified!");
    }

    /**
     * Returns whether the user is logged in or not.
     *
     * @return
     */
    public boolean isLoggedIn() {
        return prefSession.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
