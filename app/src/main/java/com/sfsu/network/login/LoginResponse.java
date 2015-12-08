package com.sfsu.network.login;

import com.google.gson.annotations.SerializedName;

/**
 * Login Response when the User successfully logs in the system. Holds reference to userId of the User and the Access Token to
 * make successive calls to the server.
 * <p/>
 * Created by Pavitra on 12/7/2015.
 */
public class LoginResponse {
    @SerializedName("id")
    final String accessToken;
    @SerializedName("userId")
    final String user_id;
    final long ttl;

    public LoginResponse(String accessToken, String user_id, long ttl) {
        this.accessToken = accessToken;
        this.user_id = user_id;
        this.ttl = ttl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUser_id() {
        return user_id;
    }

    public long getTtl() {
        return ttl;
    }
}
