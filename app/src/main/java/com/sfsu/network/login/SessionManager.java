package com.sfsu.network.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sfsu.investickation.R;

/**
 * <p>
 * Manages the {@link com.sfsu.entities.User} session in the application. When the use logs in the application, the Login
 * Credentials are stored in {@link com.sfsu.network.auth.AuthPreferences} while whether the flag are stored this class.
 * </p>
 * <p>The SessionManager will store the boolean value depending on whether the User is logged in or not. When the User
 * opens the application, the HomeActivity will check for the boolean value in SessionManager and will redirect the user to
 * the {@link com.sfsu.investickation.fragments.Dashboard} or {@link com.sfsu.investickation.fragments.Login} screen
 * respectively</p>
 * Created by Pavitra on 11/24/2015.
 */
public class SessionManager {
    // Shared preferences file name
    private static final String KEY_PREF_NAME = "InvesTICKationsLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    // LogCat tag
    private static final String TAG = "~!@#$" + SessionManager.class.getSimpleName();
    // Shared Preferences
    private SharedPreferences prefSession;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SessionManager(Context context) {
        this.mContext = context;
        prefSession = mContext.getSharedPreferences(mContext.getResources().getString(R.string.session_pref_file_name), Context.MODE_PRIVATE);
        editor = prefSession.edit();
    }

    /**
     * Sets a boolean value to the preference and commits.
     *
     * @param isLoggedIn
     */
    public void setLogin(boolean isLoggedIn) {
        // open the Session preference file.
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.apply();
        Log.d(TAG, "User Login session modified!");
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
