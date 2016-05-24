package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.investickation.fragments.ProfileFragment;


/**
 * Container Activity for displaying {@link User} related data. Holds references to {@link ProfileFragment} Fragment.
 */
public class UserProfileActivity extends MainBaseActivity {

    private final String TAG = "~!@#UsrProAct";
    private ProfileFragment mProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // if Fragment container is present,
        if (findViewById(R.id.profile_fragment_container) != null) {

            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                mProfileFragment = (ProfileFragment) getSupportFragmentManager().getFragment(savedInstanceState, "profileFragment");
            }

            mProfileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.profile_fragment_container, mProfileFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "profileFragment", mProfileFragment);
    }
}
