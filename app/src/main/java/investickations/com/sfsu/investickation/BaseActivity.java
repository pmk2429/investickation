package investickations.com.sfsu.investickation;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import investickations.com.sfsu.entities.AppConfig;

/**
 * Created by Pavitra on 7/8/2015.
 */
public class BaseActivity extends ActionBarActivity {

    // InjectView is used to inject the UI controls using ButterKnife library.
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_recyclerView)
    RecyclerView drawer_recyclerView;

    //Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        // set the toolbar injected using ButterKnife library.
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        int ICONS[] = {R.mipmap.ic_home, R.mipmap.ic_activity, R.mipmap.ic_observations, R.mipmap.ic_bug, R.mipmap.ic_settings, R.mipmap.ic_edit};


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

                Toast.makeText(BaseActivity.this, "Item clicked: " + position, Toast.LENGTH_SHORT);
                Log.d(AppConfig.LOGSTRING, "item clicked: " + position);

                // call the method to open the corresponding activity
                //goToNavDrawerItem(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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

// itemTouchListener implementation for RecyclerView
class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView, recyclerView.getChildPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onItemLongClick(View view, int position);
    }
}


