package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;

import com.sfsu.investickation.fragments.TutorialFragment;


/**
 * Tutorial for the InvesTICKations applications. Provides a basic tutorial guide for the user to use the application. The
 * tutorial contains all the features that are built into this app displayed using a small graphics and annotated text
 * describing how to use that feature.
 */
public class TutorialActivity extends MainBaseActivity {

    private final String TAG = "~!@#Tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // if Fragment container is present,
        if (findViewById(R.id.tutorial_fragment_container) != null) {

            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.tutorial_fragment_container, new TutorialFragment())
                    .commit();

        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(TutorialActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

}
