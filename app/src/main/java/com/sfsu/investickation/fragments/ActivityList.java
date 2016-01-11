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
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>
 * Displays list of {@link Activities} created by Account. Each Activity might contain {@link com.sfsu.entities.Observation}
 * depending on user's choice.
 * </p>
 * <p>
 * The Activities diplayed are combination of that stored on the local storage as well as that stored on cloud. Each Activity
 * List item contains an icon which displays whether the Activity is stored on the local SD card or the Cloud.
 * </p>
 * <p>
 * In addition to that, each item displays the brief intro about each activity i.e. number of people in Activity, number of Pets
 * as well as total observations made in each Activity.
 * </p>
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
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private IActivityCallBacks mInterface;
    private Context mContext;
    private List<Activities> responseActivitiesList, mActivitiesList, localActivitiesList;
    private ActivitiesListAdapter mActivitiesListAdapter;
    private boolean loading = true;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseDataController dbController;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_list);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setHasOptionsMenu(true);
        dbController = new DatabaseDataController(mContext, new ActivitiesDao());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_list, container, false);

        ButterKnife.bind(this, rootView);

        if (AppUtils.isConnectedOnline(mContext)) {
            // initialize the Bus to get list of Activities from server.
            // must be cached for frequent accesses.
            BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
        } else {
            // get List of Activities from Database
            localActivitiesList = (List<Activities>) (List<?>) dbController.getAll();

            mActivitiesList = localActivitiesList;

            if (mActivitiesList.size() > 0 && mActivitiesList != null) {
                populateRecyclerView();
            } else if (mActivitiesList.size() == 0) {
                // display text message
                txtView_activityListInfo.setVisibility(View.VISIBLE);
                recyclerView_activity.setVisibility(View.GONE);
                mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
            } else {
                Log.i(TAG, "activity list size < 0");
            }
        }

        mLinearLayoutManager = new LinearLayoutManager(mContext);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // by default the TextView is invisible
        txtView_activityListInfo.setVisibility(View.GONE);

        recyclerView_activity.setHasFixedSize(true);

        if (mContext != null) {
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView_activity.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No layout manager supplied");
        }
    }

    /**
     * Subscribes to event of success in loading list of {@link Activities} from Server. Combines all the Activities on the
     * server as well as those stored locally.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivitiesLoadedSuccess(ActivityEvent.OnListLoaded onLoaded) {
        // get all Response Activities from server
        responseActivitiesList = onLoaded.getResponseList();
        localActivitiesList = (List<Activities>) dbController.getAll();

        for (int i = 0; i < responseActivitiesList.size(); i++) {
            responseActivitiesList.get(i).setIsOnCloud(true);
        }

        for (int i = 0; i < localActivitiesList.size(); i++) {
            localActivitiesList.get(i).setIsOnCloud(false);
        }

//        mActivitiesList = responseActivitiesList;

        responseActivitiesList.addAll(localActivitiesList);

        mActivitiesList = responseActivitiesList;

        if (mActivitiesList.size() > 0 && mActivitiesList != null) {
            populateRecyclerView();
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
     * Helper method to initialize and display list of activities in RecyclerView. In addition, this method provides lazy loading
     * of data for better performance and interface.
     */
    private void populateRecyclerView() {
        // set the List of Activities to Adapter.
        mActivitiesListAdapter = new ActivitiesListAdapter(mActivitiesList, mContext);

        if (recyclerView_activity != null) {
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


            // lazy loading of recycler view.
            recyclerView_activity.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                Log.i("...", "Last Item Wow !");
                                //TODO Do pagination.. i.e. fetch new data
                            }
                        }
                    }
                }
            });
        }
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
    public void onDestroy() {
        super.onDestroy();
        mInterface = null;
        mContext = null;
        mActivitiesListAdapter = null;
        mActivitiesList = null;
        responseActivitiesList = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
