package com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sfsu.investickation.fragments.Home;
import com.sfsu.investickation.fragments.Login;
import com.sfsu.investickation.fragments.Register;

/**
 * HomeActivity which holds {@link Login} and {@link Register} fragments. This Activity is opened when the user is done
 * navigating the WelcomeScreenActivity for the first time. Also, when the User logs out, then s/he will be redirected to this
 * Activity to allow the User to Login again.
 */
public class HomeActivity extends AppCompatActivity implements Register.IRegisterCallBacks, Home.IHomeCallbacks {

    private final String LOGTAG = "~!@#$HomeActivity :";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // if Fragment container is present
        if (findViewById(R.id.home_fragment_container) != null) {

            // if we are restored from the previous state, just return
            if (savedInstanceState != null) {
                return;
            }

            // else show the ActivityList Fragment in the 'activity_fragment_container'
            Home homeFragment = new Home();

            // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
            homeFragment.setArguments(getIntent().getExtras());

            // add Fragment to 'activity_fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, homeFragment).commit();
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
    public void onRegisterButtonClick() {

    }

    @Override
    public void onLoginButtonClicked() {
        Login loginFragment = new Login();
        switchFragment(loginFragment);
    }

    @Override
    public void onSignUpButtonClicked() {
        Register registerFragment = new Register();
        switchFragment(registerFragment);
    }
}
