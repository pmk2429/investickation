package investickations.com.sfsu.investickation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import investickations.com.sfsu.investickation.fragments.Login;
import investickations.com.sfsu.investickation.fragments.Register;

public class HomeActivity extends BaseActivity implements Login.ILoginCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);


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

        // if user clicked the Add Button, replace with AddObservation Fragment
        Register registerFragment = new Register();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment_container, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
