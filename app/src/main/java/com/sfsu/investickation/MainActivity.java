package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.sfsu.investickation.fragments.Dashboard;


public class MainActivity extends AppCompatActivity implements Dashboard.IDashboardCallback {

    public static final String KEY_ADD_ACTIVITY = "add_new_activity_from_dashboard";
    public static final String KEY_ADD_OBSERVATION = "add_new_observation_from_dashboard";
    public static final String KEY_VIEW_ACTIVITY_LIST = "view_activityList_from_dashboard";
    public static final String KEY_VIEW_OBSERVATION_LIST = "view_observationList_from_dashboard";
    private final String LOGTAG = "~!@#$MainActivity :";
    ImageButton btnActivityAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if Fragment container is present,
        if (findViewById(R.id.mainActivity_fragmentContainer) != null) {

            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            // if Intent is called by clicking on the PostObservation button in Dashboard
            if (getIntent().getIntExtra(HomeActivity.KEY_LOGIN_SUCCESS, 0) == 1) {
                Dashboard dashboardFragment = new Dashboard();
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivity_fragmentContainer, dashboardFragment).commit();
            } else {
                Dashboard dashboardFragment = new Dashboard();
                // if activity was started with special instructions from an Intent, then pass Intent's extras
                // to fragments as arguments
                dashboardFragment.setArguments(getIntent().getExtras());
                // add the Fragment to 'mainActivity_fragmentContainer' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.mainActivity_fragmentContainer, dashboardFragment).commit();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onActivityButtonClicked() {
        Intent activityIntent = new Intent(MainActivity.this, UserActivityMasterActivity.class);
        activityIntent.putExtra(KEY_ADD_ACTIVITY, 1);
        startActivity(activityIntent);
        finish();
    }

    @Override
    public void onObservationButtonClicked() {
        Intent observationIntent = new Intent(MainActivity.this, ObservationMasterActivity.class);
        observationIntent.putExtra(KEY_ADD_OBSERVATION, 1);
        startActivity(observationIntent);
        finish();
    }

    @Override
    public void onViewActivitiesClicked() {
        Intent activityIntent = new Intent(MainActivity.this, UserActivityMasterActivity.class);
        activityIntent.putExtra(KEY_VIEW_ACTIVITY_LIST, 2);
        startActivity(activityIntent);
        finish();
    }

    @Override
    public void onViewObservationsClicked() {
        Intent observationIntent = new Intent(MainActivity.this, ObservationMasterActivity.class);
        observationIntent.putExtra(KEY_VIEW_OBSERVATION_LIST, 2);
        startActivity(observationIntent);
        finish();
    }
}

