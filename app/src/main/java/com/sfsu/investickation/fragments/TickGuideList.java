package com.sfsu.investickation.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sfsu.adapters.TicksListAdapter;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.HomeActivity;
import com.sfsu.investickation.MainActivity;
import com.sfsu.investickation.MainBaseActivity;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.investickation.SettingsActivity;
import com.sfsu.investickation.TickGuideMasterActivity;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.investickation.UserProfileActivity;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.TickEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Displays list of {@link Tick} present in the InvesTICKations application. Fetches the list from server when the user first
 * opens the application. Then after it stores the Tick list in the Local DB for further references.
 * </p>
 */
public class TickGuideList extends Fragment implements SearchView.OnQueryTextListener {

    private final String TAG = "~!@#$TickGuideList";
    private IGuideIndexCallBacks mInterface;
    private Context mContext;
    private List<Tick> mTickList, responseTickList;
    private Toolbar toolbarMain;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private int mCurrentSelectedPosition;
    private TicksListAdapter ticksListAdapter;
    private RecyclerView recyclerView_tickList;

    public TickGuideList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_tick_list);
        BusProvider.bus().post(new TickEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_index, container, false);

        setActionBarAndNavDrawer(v);

        recyclerView_tickList = (RecyclerView) v.findViewById(R.id.recyclerview_tickGuide);
        recyclerView_tickList.setHasFixedSize(true);

        if (mContext != null) {
            recyclerView_tickList.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            Log.d(TAG, "Failed to load layout manager");
        }

        return v;
    }


    /**
     * Helper method to set up Navigation drawer using the View of the Fragment.
     *
     * @param v
     */
    private void setActionBarAndNavDrawer(View v) {
        toolbarMain = (Toolbar) v.findViewById(R.id.toolbar_guideList);

        if (toolbarMain != null) {
            ((TickGuideMasterActivity) getActivity()).setSupportActionBar(toolbarMain);

            // get the ActionBar and set the Menu icon.
            final ActionBar actionBar = ((TickGuideMasterActivity) getActivity()).getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            // initialize the DrawerLayout
            mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

            // set the onClick Listener for the Drawer click event
            toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // initialize the NavigationView and also setup OnClickListener for each of the Items in list
        mNavigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
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

    private void selectDrawerItem(MenuItem menuItem) {
        Intent intent;

        try {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 0;
                    break;

                case R.id.nav_activities:
                    intent = new Intent(mContext, UserActivityMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 1;
                    break;

                case R.id.nav_observations:
                    intent = new Intent(mContext, ObservationMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 2;
                    break;

                case R.id.nav_tickGuide:
                    intent = new Intent(mContext, TickGuideMasterActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 3;
                    break;

                case R.id.nav_settings:
                    intent = new Intent(mContext, SettingsActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 4;
                    break;

                case R.id.nav_profile:
                    intent = new Intent(mContext, UserProfileActivity.class);
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 5;
                    break;


                case R.id.nav_logout:
                    intent = new Intent(mContext, HomeActivity.class);
                    intent.putExtra(MainBaseActivity.KEY_LOGOUT, 1);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                    startActivity(intent);
                    ((Activity) mContext).finish();
                    mCurrentSelectedPosition = 6;
                    break;
            }
        } catch (Exception e) {
        }

        // Highlight the selected item and close the drawer
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IGuideIndexCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGuideIndexCallBacks");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tick_list, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        } else {
            Log.i(TAG, "search is null");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        final List<Tick> filteredModelList = filter(mTickList, query);
        ticksListAdapter.animateTo(filteredModelList);
        recyclerView_tickList.scrollToPosition(0);
        return true;
    }

    /**
     * Helper method to filter the List of Observation on search text change in this Fragment.
     *
     * @param tickList
     * @param query
     * @return
     */
    private List<Tick> filter(List<Tick> tickList, String query) {
        query = query.toLowerCase();

        final List<Tick> filteredTickList = new ArrayList<>();
        for (Tick tick : tickList) {
            // perform the search on TickName since it will be visible to user.
            final String text = tick.getTickName().toLowerCase();
            if (text.contains(query)) {
                filteredTickList.add(tick);
            }
        }
        return filteredTickList;
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

    /**
     * Subscribes to the event of successful loading of {@link Tick} from server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onTicksLoadSuccess(TickEvent.OnLoaded onLoaded) {
        if (onLoaded.getResponseList() != null) {
            responseTickList = onLoaded.getResponseList();
            mTickList = responseTickList;
            // display the ticks in list
            displayTickList();
        } else {
        }
    }

    /**
     * Subscribes to the event of failure in loading of {@link Tick} from server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onTicksLoadSuccess(TickEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Helper method to display list of Ticks
     */
    private void displayTickList() {

        ticksListAdapter = new TicksListAdapter(mTickList, mContext);
        recyclerView_tickList.setAdapter(ticksListAdapter);

        // set on click listener for the item click of recyclerView
        recyclerView_tickList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView_tickList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mInterface.onTickListItemClickListener(mTickList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                
            }
        }));
    }


    /**
     * Callback Interface to handle onClick Listeners in {@link TickGuideList} Fragment.
     */
    public interface IGuideIndexCallBacks {

        /**
         * Callback method to provide an interface to listen to data sent or button clicked in {@link TickGuideList} Fragment
         *
         * @param tick
         */
        public void onTickListItemClickListener(Tick mTick);


    }

}
