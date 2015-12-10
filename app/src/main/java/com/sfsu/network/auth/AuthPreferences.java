package com.sfsu.network.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.sfsu.investickation.R;

/**
 * SharedPreference to store the OAuth 2.0 Access Token when the user successfully logs in.
 * <p>
 * Created by Pavitra on 12/5/2015.
 */
public class AuthPreferences {
    private final String KEY_ACCESS_TOKEN = "access_token_key";
    private final String KEY_USER_ID = "user_id";

    private Context mContext;
    private SharedPreferences tokenSharedPreferences;

    public AuthPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public boolean setCredentials(String authToken, String userId) {
        tokenSharedPreferences = mContext.getSharedPreferences(mContext.getResources().
                getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenSharedPreferences.edit();
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

}
