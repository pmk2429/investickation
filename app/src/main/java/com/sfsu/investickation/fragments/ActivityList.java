package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ActivitiesListAdapter;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays list of {@link Activities} created by User. Each Activity might contains {@link com.sfsu.entities.Observation} depending on
 * user's choice.
 */
public class ActivityList extends Fragment implements SearchView.OnQueryTextListener {

    public final String TAG = "~!@#ActivityList";
    @Bind(R.id.recyclerview_activity_list)
    RecyclerView recyclerView_activity;
    @Bind(R.id.fab_activity_add)
    FloatingActionButton addActivity;
    @Bind(R.id.relativeLayout_actList_main)
    RelativeLayout mRelativeLayout;
    @Bind(R.id.textViewStatic_actList_listInfo)
    TextView txtView_activityListInfo;
    int count = 0;
    private IActivityCallBacks mInterface;
    private Context mContext;
    private List<Activities> responseActivitiesList, mActivitiesList;
    private ActivitiesListAdapter mActivitiesListAdapter;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_list);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setHasOptionsMenu(true);
        // initialize the Bus to get list of Activities from server.
        // must be cached for frequent accesses.
        Log.i(TAG, "on create called");
        BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_list, container, false);

        ButterKnife.bind(this, rootView);

        // by default the TextView is invisible
        txtView_activityListInfo.setVisibility(View.GONE);

        recyclerView_activity.setHasFixedSize(true);

        if (mContext != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_activity.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No layout manager supplied");
        }

        return rootView;
    }

    /**
     * Subscribes to event of success in loading list of {@link Activities} from Server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivitiesLoadedSuccess(ActivityEvent.OnListLoaded onLoaded) {
        responseActivitiesList = onLoaded.getResponseList();

        mActivitiesList = responseActivitiesList;

        if (mActivitiesList.size() > 0 && mActivitiesList != null) {
//            Log.i(TAG, responseActivitiesList.size() + "");
            Log.i(TAG, mActivitiesList.size() + "");
            displayActivitiesList();
        } else if (mActivitiesList.size() == 0) {
            // display text message
            txtView_activityListInfo.setVisibility(View.VISIBLE);
            recyclerView_activity.setVisibility(View.GONE);
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        } else {
            Log.i(TAG, "activity list size < 0");
        }


    }

    /**
     * Subscribes to event of failure of loading list of {@link Activities} from server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onActivitiesLoadedFailure(ActivityEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Helper method to display list of activities in RecyclerView.
     */
    private void displayActivitiesList() {
        // set the List of Activities to Adapter.
        mActivitiesListAdapter = new ActivitiesListAdapter(mActivitiesList);
        recyclerView_activity.setAdapter(mActivitiesListAdapter);

        // touch listener when the user clicks on the Activity in the List.
        recyclerView_activity.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView_activity,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // call the interface callback to listen to the item click event
                        mInterface.onActivitiesListItemClickListener(mActivitiesList.get(position));
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));

        // Add new Activity button.
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onActivityAddListener();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IActivityCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityInteractionListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_list, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        } else {
            Log.i(TAG, "search is null");
        }
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Helper method to filter the List of Activities on search text change in this Fragment.
     *
     * @param serverActivitiesList
     * @param query
     * @return
     */
    private List<Activities> filter(List<Activities> activitiesList, String query) {
        query = query.toLowerCase();

        final List<Activities> filteredActivitiesList = new ArrayList<>();
        for (Activities activity : activitiesList) {
            // perform the search on Activity name
            final String text = activity.getActivityName().toLowerCase();
            if (text.contains(query)) {
                filteredActivitiesList.add(activity);
            }
        }
        return filteredActivitiesList;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Activities> filteredActivitiesList = filter(mActivitiesList, query);
        mActivitiesListAdapter.animateTo(filteredActivitiesList);
        recyclerView_activity.scrollToPosition(0);
        return true;
    }


    @Override
    public void onStop() {
        super.onStop();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.bus().register(this);
    }

    /**
     * Callback interface to handle callback methods for the ActivityList fragment.
     */
    public interface IActivityCallBacks {
        /**
         * Callback method to handle the Item click event of activities list in {@link ActivityList} Fragment.
         */
        public void onActivitiesListItemClickListener(Activities mActivity);

        /**
         * Callback method to handle the click event of the Add Button in {@link ActivityList} Fragment.
         */
        public void onActivityAddListener();
    }

}
