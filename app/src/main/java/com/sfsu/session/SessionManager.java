package com.sfsu.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sfsu.entities.Account;
import com.sfsu.investickation.R;
import com.sfsu.investickation.fragments.DashboardFragment;
import com.sfsu.investickation.fragments.LoginFragment;

/**
 * <p>
 * Manages the {@link Account} session in the application. When the use logs in the application, the LoginFragment
 * Credentials are stored in {@link com.sfsu.network.auth.AuthPreferences} while whether the flag are stored this class.
 * </p>
 * <p>The SessionManager will store the boolean value depending on whether the Account is logged in or not. When the Account
 * opens the application, the HomeActivity will check for the boolean value in SessionManager and will redirect the user to
 * the {@link DashboardFragment} or {@link LoginFragment} screen
 * respectively
 * </p>
 * <p/>
 * Created by Pavitra on 11/24/2015.
 */
public class SessionManager {
    // Shared preferences file name
    private static final String KEY_PREF_NAME = "InvesTICKationsSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_LOGGEDOUT = "isLoggedOut";
    // LogCat tag
    private static final String TAG = "~!@#$SessionMgr";
    // Shared Preferences
    private SharedPreferences prefSession;
    private SharedPreferences.Editor editor;
    private Context mContext;

    @SuppressLint("CommitPrefEdits")
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
        Log.d(TAG, "Account LoginFragment session modified!");
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
