package com.sfsu.investickation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sfsu.entities.Entity;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.fragments.TickGuideDetailFragment;
import com.sfsu.investickation.fragments.TickGuideListFragment;
import com.sfsu.investickation.fragments.TickMap;
import com.sfsu.network.bus.BusProvider;

import java.util.ArrayList;


public class TickGuideMasterActivity extends AppCompatActivity implements TickGuideListFragment.IGuideIndexCallBacks, TickMap.ITickMapCallBack {

    public final static String KEY_TICK_DETAIL = "tick_object_detail";
    public final static String KEY_TICK_MAP = "tick_map";
    private final String TAG = "~!@#$TickGuideMstrAct";
    private ArrayList<Tick> tickList;
    private ArrayList<Entity> entityList;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidmaster);

        mFragmentManager = getSupportFragmentManager();

        // if Fragment container is present,
        if (findViewById(R.id.guide_fragment_container) != null) {
            // if we are being restored from previous state, then just RETURN or else we could have
            // over lapping fragments
            if (savedInstanceState != null) {
                return;
            }

            if (getIntent().getIntExtra(KEY_TICK_MAP, 0) == 1) {
                TickMap tickMap = new TickMap();
                performAddFragmentTransaction(tickMap, false);
            } else {
                TickGuideListFragment mTikTickGuideListFragment = new TickGuideListFragment();
                performAddFragmentTransaction(mTikTickGuideListFragment, false);
            }
        }
    }

    private void performAddFragmentTransaction(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.guide_fragment_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void performReplaceFragmentTransaction(Fragment fragment, boolean addToBackStack, boolean animate) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.guide_fragment_container, fragment);
        if (animate)
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
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
            getSupportFragmentManager().popBackStack();
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
    public void onTickListItemClickListener(Tick mTick) {
        TickGuideDetailFragment guideDetailFragment = TickGuideDetailFragment.newInstance(KEY_TICK_DETAIL, mTick);
        performReplaceFragmentTransaction(guideDetailFragment, true, true);
    }

}
