package com.sfsu.network.rest.service;

import com.sfsu.network.login.LoginResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * The <b>Service</b> interface to manage http network calls for Login/Logout of User Session.
 */
public interface LoginService {

    /**
     * Method to Log in the user and get Access Token in Response.
     *
     * @param email
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);
}
