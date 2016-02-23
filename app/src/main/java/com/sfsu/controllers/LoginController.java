package com.sfsu.controllers;

import android.content.Context;

/**
 * Created by Pavitra on 11/25/2015.
 */
public class LoginController {

    private Context mContext;

    public LoginController(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * <p>
     * Verifies the LoginFragment details and delegates the call to Retrofit. This Controller provides an extra level of modularity to
     * avoid Retrofit calls getting dropped when the Configuration changes occurs in Fragment/Activity Lifecycle making
     * Retrofit object corrupt.
     * </p>
     * The main network call to retrofit will be handled by and delegated by the {@link LoginController}
     * and not {@link Login}Fragment.
     *
     * @param email
     * @param password
     */
    public boolean checkLogin(final String email, final String password) {

        return false;
    }

}
