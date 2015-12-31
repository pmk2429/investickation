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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ObservationsListAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>
 * Displays the list of {@link Observation}s made by the User. The observations which are retrieved from the Server are
 * combined to that stored on the Local SQLite DB. Depending on the the where the Observation is stored, the RecyclerView item
 * displays an icon representing cloud or database.
 * </p>
 * <p>
 * <p>
 * Also displays the observations made for a specific {@link com.sfsu.entities.Activities}. Using the <tt>activityId</tt>, the
 * network call is made for list of observations corresponding to a specific activity.
 * </p>
 */

public class ObservationsList extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private final String TAG = "~!@#ObsList";
    @Bind(R.id.recyclerview_remote_observations)
    RecyclerView recyclerView_observations;
    @Bind(R.id.relativeLayout_obsList_main)
    RelativeLayout mRelativeLayout;
    @Bind(R.id.textViewStatic_obsList_listInfo)
    TextView txtView_observationList_info;
    @Bind(R.id.fab_observation_add)
    FloatingActionButton addProject;
    private IRemoteObservationCallBacks mInterface;
    private Context mContext;
    private List<Observation> observationList, remoteObservationList, localObservationList;
    private Observation newObservationObject;
    private Bundle args;
    private ObservationsListAdapter mObservationsListAdapter;
    private DatabaseDataController dbController;
    private String activityId;

    public ObservationsList() {
        // Required empty public constructor
    }

    /**
     * Helper method to create instance of an {@link ObservationsList} fragment initialzed with <tt>activityId</tt>
     *
     * @param key
     * @param activityId
     * @return
     */
    public static ObservationsList newInstance(String key, String activityId) {
        ObservationsList mObservationsList = new ObservationsList();
        Bundle args = new Bundle();
        args.putString(key, activityId);
        mObservationsList.setArguments(args);
        return mObservationsList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_observation_list);

        dbController = new DatabaseDataController(mContext, new ObservationsDao());
        // get the ActivityId from the Bundle.
        if (getArguments() != null) {
            args = getArguments();
        }
        if (args != null && args.containsKey(UserActivityMasterActivity.KEY_ACTIVITY_ID)) {
            activityId = args.getString(UserActivityMasterActivity.KEY_ACTIVITY_ID);
        } else {
            activityId = "";
        }

        // call to network depending on the type of call to be made. i.e. all Observations or Observations specific to Activity
        BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_remote_observations, container, false);

        ButterKnife.bind(this, rootView);

        txtView_observationList_info.setVisibility(View.GONE);

        recyclerView_observations.setHasFixedSize(true);

        if (mContext != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No Layout manager supplied");
        }

        // TODO: think of this one.
//        localObservationList = (List<Observation>) dbController.getAll();


        //observationList = new ArrayList<>(remoteObservationList);
        //observationList.addAll(localObservationList);

        //observationList.add(newObservationObject)
//        displayObservationList();

        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onObservationAddListener();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mInterface = (IRemoteObservationCallBacks) activity;
            mContext = activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement IObservationCallBacks");
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
    public void onClick(View v) {
        mInterface.onObservationAddListener();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_observation_list, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

    }

    /*
    This will be fired when the text in the SearchView changes. Accordingly the Incremental search will be performed by the
    name of each element present in the list.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        final List<Observation> filteredModelList = filter(observationList, query);
        mObservationsListAdapter.animateTo(filteredModelList);
        recyclerView_observations.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // don't matter
        return false;
    }

    /**
     * Helper method to filter the List of Observation on search text change in this Fragment.
     *
     * @param observationList
     * @param query
     * @return
     */
    private List<Observation> filter(List<Observation> observationList, String query) {
        query = query.toLowerCase();

        final List<Observation> filteredObservationList = new ArrayList<>();
        for (Observation observation : observationList) {
            // perform the search on TickName since it will be visible to user.
            final String text = observation.getTickName().toLowerCase();
            if (text.contains(query)) {
                filteredObservationList.add(observation);
            }
        }
        return filteredObservationList;
    }


    /**
     * Subscribes to the event of success in loading of all the {@link Observation} from server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationsLoadSuccess(ObservationEvent.OnLoaded onLoaded) {
        remoteObservationList = onLoaded.getResponseList();

        if (remoteObservationList.size() > 0 && remoteObservationList != null) {
            displayObservationList();
        } else if (remoteObservationList.size() == 0) {
            txtView_observationList_info.setVisibility(View.VISIBLE);
            recyclerView_observations.setVisibility(View.GONE);
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        } else {

        }
    }

    /**
     * Subscribes to the failure event of getting all {@link Observation} from server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationsLoadFailure(ObservationEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Helper method to display list of Observations in RecyclerView.
     */
    private void displayObservationList() {
        //pass the observationList to the Adapter
        mObservationsListAdapter = new ObservationsListAdapter(remoteObservationList, mContext);
        recyclerView_observations.setAdapter(mObservationsListAdapter);

        // implement touch event for the item click in RecyclerView
        recyclerView_observations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_observations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                mInterface.onObservationListItemClickListener(remoteObservationList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    /**
     * Callback Interface to handle onClick Listeners in {@link ObservationsList} Fragment.
     */
    public interface IRemoteObservationCallBacks {

        /**
         * Callback method to handle onClick Listener when user clicks on '+' button in {@link ObservationsList} Fragment.
         */
        public void onObservationAddListener();

        /**
         * Callback method to handle the on item click Listener of the Observations List in {@link ObservationsList}
         * Fragment
         *
         * @param observation
         */
        public void onObservationListItemClickListener(Observation mObservation);
    }


}
