package com.sfsu.network.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.sfsu.investickation.R;

/**
 * Stores the OAuth 2.0 Access Token when the user successfully logs in, in the SharedPreferences. This class handles all the
 * user login related session details such as <tt>user_id</tt> and <tt>accessToken</tt> when the User creates a session after
 * signing in.
 * <p/>
 * Created by Pavitra on 12/5/2015.
 */
public class AuthPreferences {
    private final String KEY_ACCESS_TOKEN = "key_access_token";
    private final String KEY_USER_ID = "key_user_id";

    private Context mContext;
    private SharedPreferences tokenSharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthPreferences(Context mContext) {
        this.mContext = mContext;
        tokenSharedPreferences = mContext.getSharedPreferences(mContext.getResources().
                getString(R.string.auth_pref_file_name), Context.MODE_PRIVATE);
        editor = tokenSharedPreferences.edit();
    }

    /**
     * Allows to store <tt>user_id</tt> and <tt>accessToken</tt> for the current logged in {@link com.sfsu.entities.User}.
     *
     * @param authToken
     * @param userId
     * @return
     */
    public boolean setCredentials(String authToken, String userId) {
        editor.putString(KEY_ACCESS_TOKEN, authToken);
        editor.putString(KEY_USER_ID, userId);
        return editor.commit();
    }

    /**
     * Returns the Access Token retrieved from successful login and stored in the SharedPreferences.
     *
     * @return
     */
    public String getAccessToken() {
        return tokenSharedPreferences != null ? tokenSharedPreferences.getString(KEY_ACCESS_TOKEN, "invalid-auth-token")
                : "invalid-auth-token";
    }

    /**
     * Returns the user_id retrieved from successful login and stored in SharedPreferences
     *
     * @return
     */
    public String getUser_id() {
        return tokenSharedPreferences != null ? tokenSharedPreferences.getString(KEY_USER_ID, "no-user-id") : "no-user-id";
    }

    /**
     * Clears the <tt>user_id</tt> and the <tt>accessToken</tt> from SharedPreferences.
     *
     * @return
     */
    public boolean clearCredentials() {
        editor.clear();
        //editor.apply();

        return editor.commit();
    }

}
