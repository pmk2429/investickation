package com.sfsu.investickation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Pavitra on 11/20/2015.
 */
public class MainBaseActivity extends AppCompatActivity {

    private final String LOGTAG = "~!@#$BaseActivity:";
    // InjectView is used to inject the UI controls using ButterKnife library.
    DrawerLayout mDrawerLayout;
    Toolbar toolbarMain;
    NavigationView mNavigationView;
    private int mCurrentSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //toolbarMain = (Toolbar) findViewById(R.id.toolbar_dashboard_scrollable);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);


        if (toolbarMain != null) {
            setSupportActionBar(toolbarMain);

            // get the ActionBar and set the Menu icon.
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            // set the onClick Listener for the Drawer click event
            toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // initialize the NavigationView and also setup OnClickListener for each of the Items in list
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    selectDrawerItem(menuItem);
                    return true;
                }
            });
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


//        // set the Title to CollapsingToolbar
//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_dashboard);
//        collapsingToolbar.setTitle("InvesTICKations");
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent;

        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 0;
                    break;

                case R.id.nav_activities:
                    intent = new Intent(this, UserActivityMasterActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 1;
                    break;

                case R.id.nav_observations:
                    intent = new Intent(this, ObservationMasterActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 2;
                    break;

                case R.id.navigation_ticksData:
                    intent = new Intent(this, TickGuideMasterActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 3;
                    break;

                case R.id.nav_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    mCurrentSelectedPosition = 4;
                    break;
            }
        } catch (Exception e) {
            Log.d(LOGTAG, e.getMessage());
        }

        // Hihhlight the selected item and close the drawer
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
