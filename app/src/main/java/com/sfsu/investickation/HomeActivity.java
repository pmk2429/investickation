package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.entities.User;
import com.sfsu.investickation.fragments.Home;
import com.sfsu.investickation.fragments.Login;
import com.sfsu.investickation.fragments.Register;
import com.sfsu.network.login.SessionManager;

/**
 * <p>
 * Container Activity for {@link Login} and {@link Register} fragments. Opens when the user is done
 * navigating the WelcomeScreenActivity for the first time. Also, when the User logs out, then s/he will be redirected to this
 * Activity to allow the User to Login again.
 * </p>
 * <p>
 * The Home activity will take care of all user User related Sessions. If the User is logged in then s/he will be
 * redirected to the {@link MainActivity} else the User will be asked to Login or Register.</p>
 */
public class HomeActivity extends AppCompatActivity implements Login.ILoginCallBack, Register.IRegisterCallBacks, Home
        .IHomeCallbacks {

    public static final String KEY_SIGNIN_SUCCESS = "signin_success";
    private final String TAG = "~!@#$HomeActivity :";
    private DatabaseDataController dbController;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbController = new DatabaseDataController(this, new UsersDao());
        mSessionManager = new SessionManager(this);

        // if Fragment container is present
        if (findViewById(R.id.home_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // depending on whether the Session is set for current user or not, open Login or Dashboard respectively.

            if (mSessionManager.isLoggedIn()) {
                userLoggedIn();
                Log.i(TAG, " user logged in");
            } else {
                Log.i(TAG, " user not logged in");
                // else show the ActivityList Fragment in the 'activity_fragment_container'
                Home homeFragment = new Home();
                // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
                homeFragment.setArguments(getIntent().getExtras());
                // add Fragment to 'activity_fragment_container'
                getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, homeFragment).commit();
            }
        }
    }

    /**
     * Helper method to provide Fragment Transaction.
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onLoginClicked() {
        Login loginFragment = new Login();
        switchFragment(loginFragment);
    }

    @Override
    public void onSignUpClicked() {
        Register registerFragment = new Register();
        switchFragment(registerFragment);
    }


    @Override
    public void onRegisterButtonClick(User mUserObj) {
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


}
