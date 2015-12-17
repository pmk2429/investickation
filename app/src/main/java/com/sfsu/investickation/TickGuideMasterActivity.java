package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sfsu.entities.Entity;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.fragments.TickGuideDetail;
import com.sfsu.investickation.fragments.TickGuideList;
import com.sfsu.network.bus.BusProvider;

import java.util.ArrayList;


public class TickGuideMasterActivity extends AppCompatActivity implements TickGuideList.IGuideIndexCallBacks {

    public final static String KEY_TICK_DETAIL = "tick_object_detail";
    private final String LOGTAG = "~!@#$TickGdeMstrAct :";
    private ArrayList<Tick> tickList;
    private ArrayList<Entity> entityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidmaster);

        // if Fragment container is present,
        if (findViewById(R.id.guide_fragment_container) != null) {
            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            TickGuideList guideIndexFragment = new TickGuideList();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.guide_fragment_container, guideIndexFragment);
            transaction.commit();
//            replaceFragment(guideIndexFragment);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(TickGuideMasterActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            Log.i(LOGTAG, "" + count);
            getSupportFragmentManager().popBackStackImmediate();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    @Override
    public void onTickListItemClickListener(Tick mTick) {
        TickGuideDetail guideDetailFragment = TickGuideDetail.newInstance(KEY_TICK_DETAIL, mTick);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.guide_fragment_container, guideDetailFragment);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.guide_fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
