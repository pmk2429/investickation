package com.sfsu.network.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.sfsu.investickation.R;

/**
 * SharedPreference to store the OAuth 2.0 Access Token when the user successfully logs in.
 * <p/>
 * Created by Pavitra on 12/5/2015.
 */
public class TokenPreferences {
    private final String KEY_ACCESS_TOKEN = "access_token_key";
    private Context mContext;
    private SharedPreferences tokenSharedPreferences;

    public TokenPreferences(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Sets the AccessToken retrieved by the User after successful Login
     *
     * @param authToken
     * @return
     */
    public boolean setAccessToken(String authToken) {
        tokenSharedPreferences = mContext.getSharedPreferences(mContext.getResources().
                getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = tokenSharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, authToken);
        return editor.commit();
    }

    /**
     * Returns the Access Token stored in the SharedPreferences.
     *
     * @return
     */
    public String getAccessToken() {
        return tokenSharedPreferences.getString(KEY_ACCESS_TOKEN, "invalid-auth-token");
    }

}
