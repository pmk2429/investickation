package com.sfsu.investickation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sfsu.adapters.DrawerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pavitra on 7/8/2015.
 */
public class BaseActivity extends ActionBarActivity {

    private final String TAG = "~!@#$BaseActivity";
    // InjectView is used to inject the UI controls using ButterKnife library.
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    RecyclerView drawer_recyclerView;

    //Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar_top_base);
        drawer_recyclerView = (RecyclerView) findViewById(R.id.drawer_recyclerView);

        // set the toolbar_master injected using ButterKnife library.
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        int ICONS[] = {R.mipmap.ic_home_black_36dp, R.mipmap.ic_walk_black_36dp, R.mipmap.ic_observations_black_36dp, R.mipmap.ic_bug_report_black_36dp, R.mipmap.ic_settings_black_36dp, R.mipmap.ic_edit};


        List<String> rows = new ArrayList<String>();
        String[] navDrawer = getResources().getStringArray(R.array.navdrawer);
        rows.addAll(Arrays.asList(navDrawer));

        DrawerAdapter drawerAdapter = new DrawerAdapter(rows, ICONS, this);

        drawer_recyclerView.setAdapter(drawerAdapter);
        // performance improvement
        drawer_recyclerView.setHasFixedSize(true);

        drawer_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // main implementation of the touch event
        drawer_recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getParent(), drawer_recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


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


