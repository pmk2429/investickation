package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sfsu.investickation.fragments.HomeFragment;
import com.sfsu.investickation.fragments.LoginFragment;
import com.sfsu.investickation.fragments.LogoutFragment;
import com.sfsu.investickation.fragments.RegisterFragment;
import com.sfsu.session.SessionManager;

/**
 * <p>
 * Container Activity for {@link LoginFragment} and {@link RegisterFragment} Fragments. Opens when the user is done
 * navigating the WelcomeScreenActivity for the first time. Also, when the Account logs out, then s/he will be redirected to this
 * Activity to allow the Account to LoginFragment again.
 * </p>
 * <p>
 * The HomeFragment activity will take care of all user Account related Sessions. If the Account is logged in then s/he will be
 * redirected to the {@link MainActivity} else the Account will be asked to LoginFragment or RegisterFragment.</p>
 */
public class HomeActivity extends AppCompatActivity implements LoginFragment.ILoginCallBack, RegisterFragment.IRegisterCallBacks, HomeFragment
        .IHomeCallbacks, LogoutFragment.ILogoutCallBack {

    public static final String KEY_SIGNIN_SUCCESS = "signin_success";
    private final String TAG = "~!@#$HomeAct";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFragmentManager = getSupportFragmentManager();
        SessionManager mSessionManager = new SessionManager(this);

        // if Fragment container is present
        if (findViewById(R.id.home_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // if the user clicked logout, then open LogoutFragment fragment
            if (getIntent().getIntExtra(MainBaseActivity.KEY_LOGOUT, 0) == 1) {
                LogoutFragment mLogoutFragment = new LogoutFragment();
                performReplaceFragmentTransaction(mLogoutFragment, false, false);
            } else {
                // depending on whether the Session is set for current user or not.
                if (mSessionManager.isLoggedIn()) {
                    displaySplashScreen();
                    //userLoggedIn();
                } else {
                    // use case when the session expires for current user.
                    HomeFragment mHomeFragment = new HomeFragment();
                    performAddFragmentTransaction(mHomeFragment, false);
                }
            }
        }
    }

    /**
     * Method to open {@link SplashScreenActivity} when the user is logged in and opens the application.
     */
    private void displaySplashScreen() {
        startActivity(new Intent(HomeActivity.this, SplashScreenActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count == 0) {
            finish();
        } else if (count > 0) {
            mFragmentManager.popBackStackImmediate();
        }
    }

    private void performAddFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.home_fragment_container, fragment);
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack, boolean animate) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        if (animate)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onLoginClicked() {
        LoginFragment loginFragment = new LoginFragment();
        performReplaceFragmentTransaction(loginFragment, true, false);
    }

    @Override
    public void onSignUpClicked() {
        RegisterFragment registerFragment = new RegisterFragment();
        performReplaceFragmentTransaction(registerFragment, true, false);
    }


    @Override
    public void onUserRegistrationDone() {
        Intent dashboardIntent = new Intent(HomeActivity.this, MainActivity.class);
        dashboardIntent.putExtra(KEY_SIGNIN_SUCCESS, 1);
        startActivity(dashboardIntent);
        finish();
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
//        HomeFragment homeFragment = new HomeFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.home_fragment_container, homeFragment);
//        transaction.commit();

        performReplaceFragmentTransaction(new HomeFragment(), false, false);
    }
}
