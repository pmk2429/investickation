package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.session.SessionManager;

/**
 * Logs the user out of the application. Clears all the SharedPreferences from {@link AuthPreferences} and updates the
 * <tt>login</tt> flag in the {@link SessionManager}.
 * <p/>
 * Created by Pavitra on 12/24/2015.
 */
public class Logout extends Fragment {

    private Context mContext;
    private AuthPreferences mAuthPreferences;
    private SessionManager mSessionManager;
    private ILogoutCallBack mInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mContext = context;
            mInterface = (ILogoutCallBack) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("must implement ILogoutCallBack interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // clear the AuthPreferences SharedPreferences
        mAuthPreferences = new AuthPreferences(mContext);
        mSessionManager = new SessionManager(mContext);

        boolean isLoggedOut = mAuthPreferences.clearCredentials();
        if (isLoggedOut) {
            mSessionManager.setLogin(false);
        }

        mInterface.userLoggedOut();

    }

    /**
     * Callback interface to logout the user and display login screen.
     */
    public interface ILogoutCallBack {
        /**
         * Callback method to logout the User and display {@link Login} Fragment.
         */
        public void userLoggedOut();
    }

}
