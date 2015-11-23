package com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.sfsu.investickation.fragments.Login;
import com.sfsu.investickation.fragments.Register;

/**
 * HomeActivity which holds {@link Login} and {@link Register} fragments. This Activity is opened when the user is done
 * navigating the WelcomeScreenActivity for the first time. Also, when the User logs out, then s/he will be redirected to this
 * Activity to allow the User to Login again.
 */
public class HomeActivity extends BaseActivity implements Login.ILoginCallBack, Register.IRegisterCallBacks {

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
            Login loginFragment = new Login();

            // if activity was started with special instructions from an Intent, pass Intent's extras to fragments as Args
            loginFragment.setArguments(getIntent().getExtras());

            // add Fragment to 'activity_fragment_container'
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, loginFragment).commit();
        }
    }


    @Override
    public void onFragmentInteraction() {
        Register registerFragment = new Register();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onRegisterItemListener() {

    }
}
