package com.sfsu.rest;

import com.sfsu.investickation.fragments.Login;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Pavitra on 11/25/2015.
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
    @POST("/users/login")
    Call<Login> login(@Field("email") String email, @Field("password") String password);
}
