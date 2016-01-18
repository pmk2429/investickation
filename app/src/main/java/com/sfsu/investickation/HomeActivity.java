package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sfsu.entities.Account;
import com.sfsu.investickation.fragments.Home;
import com.sfsu.investickation.fragments.Login;
import com.sfsu.investickation.fragments.Logout;
import com.sfsu.investickation.fragments.Register;
import com.sfsu.session.SessionManager;

/**
 * <p>
 * Container Activity for {@link Login} and {@link Register} Fragments. Opens when the user is done
 * navigating the WelcomeScreenActivity for the first time. Also, when the Account logs out, then s/he will be redirected to this
 * Activity to allow the Account to Login again.
 * </p>
 * <p>
 * The Home activity will take care of all user Account related Sessions. If the Account is logged in then s/he will be
 * redirected to the {@link MainActivity} else the Account will be asked to Login or Register.</p>
 */
public class HomeActivity extends AppCompatActivity implements Login.ILoginCallBack, Register.IRegisterCallBacks, Home
        .IHomeCallbacks, Logout.ILogoutCallBack {

    public static final String KEY_SIGNIN_SUCCESS = "signin_success";
    private final String TAG = "~!@#$HomeAct";
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSessionManager = new SessionManager(this);

        // if Fragment container is present
        if (findViewById(R.id.home_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // if the user clicked logout, then open Logout fragment
            if (getIntent().getIntExtra(MainBaseActivity.KEY_LOGOUT, 0) == 1) {
                Logout mLogoutFragment = new Logout();
                performFragmentTransaction(mLogoutFragment);
            } else {
                // depending on whether the Session is set for current user or not.
                if (mSessionManager.isLoggedIn()) {
                    userLoggedIn();
                } else {
                    // use case when the session expires for current user.
                    Home mHomeFragment = new Home();
                    getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, mHomeFragment).commit();

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }

    /**
     * Helper method to perform Fragment Transaction.
     *
     * @param fragment
     */

    private void performFragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onLoginClicked() {
        Login loginFragment = new Login();
        performFragmentTransaction(loginFragment);
    }

    @Override
    public void onSignUpClicked() {
        Register registerFragment = new Register();
        performFragmentTransaction(registerFragment);
    }


    @Override
    public void onRegisterButtonClick(Account mUserObj) {
        // pass this user obj to Login and make a call to Login
        Login mLoginFragment = new Login();

        // verify the details and pass the control to Login fragment.
        if (mUserObj.getEmail() != null && mUserObj.getPassword() != null) {
            mLoginFragment.login(mUserObj.getEmail(), mUserObj.getPassword());
        }
    }

    @Override
    public void userLoggedIn() {
        Intent dashboardIntent = new Intent(HomeActivity.this, MainActivity.class);
        dashboardIntent.putExtra(KEY_SIGNIN_SUCCESS, 1);
        startActivity(dashboardIntent);
        finish();
    }


    @Override
    public void userLoggedOut() {
        Home homeFragment = new Home();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, homeFragment);
        transaction.commit();
    }
}
