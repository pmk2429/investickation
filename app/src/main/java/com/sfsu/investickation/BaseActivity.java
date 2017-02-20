package com.sfsu.investickation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sfsu.adapters.DrawerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private final String TAG = "~!@#$BaseActivity";
    // InjectView is used to inject the UI controls using ButterKnife library.
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    RecyclerView drawer_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_top_base);
        drawer_recyclerView = (RecyclerView) findViewById(R.id.drawer_recycler_view);

        // set the toolbar_master injected using ButterKnife library.
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        int ICONS[] = {};


        List<String> rows = new ArrayList<String>();
        String[] navDrawer = getResources().getStringArray(R.array.navdrawer);
        rows.addAll(Arrays.asList(navDrawer));

        DrawerAdapter drawerAdapter = new DrawerAdapter(rows, ICONS, this);

        drawer_recyclerView.setAdapter(drawerAdapter);
        // performance improvement
        drawer_recyclerView.setHasFixedSize(true);

        drawer_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // main implementation of the touch event
        drawer_recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getParent(),
                drawer_recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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


