package com.sfsu.investickation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.investickation.fragments.Profile;


public class UserProfileActivity extends MainBaseActivity {

    private final String TAG = "~!@#UsrProAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // if Fragment container is present,
        if (findViewById(R.id.profile_fragment_container) != null) {

            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            Profile mProfile = new Profile();
            // add the Fragment to 'guide_fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.profile_fragment_container, mProfile).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_personal_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
