package com.sfsu.network.rest.service;

import com.sfsu.session.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The <b>Service</b> interface to manage http network calls for Login/Logout of Account Session.
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
    @POST("accounts/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);
}
