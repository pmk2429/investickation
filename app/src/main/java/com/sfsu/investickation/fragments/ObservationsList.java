package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ObservationsListAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays List of {@link Observation}s made by the User. The Observation are combined to that store on the Local SQLite DB
 * and also those which are retrieved from the Server. Depending on the the where the Observation is stored, the RecyclerView
 * item displays an icon
 */

public class ObservationsList extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private final String TAG = "~!@#RemoteObs :";
    private IRemoteObservationCallBacks mInterface;
    private Context mContext;
    private List<Observation> observationList, remoteObservationList, localObservationList;
    private Observation newObservationObject;
    private RecyclerView recyclerView_observations;
    private Bundle args;
    private ObservationsListAdapter mObservationsListAdapter;
    private DatabaseDataController dbController;

    public ObservationsList() {
        // Required empty public constructor
    }


    public static ObservationsList newInstance() {
        return new ObservationsList();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Observations");
        args = getArguments();
        dbController = new DatabaseDataController(mContext, new ObservationsDao());
        BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remote_observations, container, false);

        recyclerView_observations = (RecyclerView) v.findViewById(R.id.recyclerview_remote_observations);
        recyclerView_observations.setHasFixedSize(true);


        if (mContext != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No Layout manager supplied");
        }

        // retrieve the Observation Response Object from the Bundle. This object will be the one returned as Response by Retrofit
        if (args != null) {
            newObservationObject = getArguments().getParcelable(AppUtils.OBSERVATION_RESOURCE);
            if (newObservationObject != null) {
                Log.i(TAG, newObservationObject.toString());
            }
        }

        // TODO: think of this one.
//        localObservationList = (List<Observation>) dbController.getAll();


        //observationList = new ArrayList<>(remoteObservationList);
        //observationList.addAll(localObservationList);

        //observationList.add(newObservationObject)
        displayObservationList();

        final FloatingActionButton addProject = (FloatingActionButton) v.findViewById(R.id.fab_observation_add);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onObservationAddListener();
            }
        });

        return v;
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
     * Subscribes to the success loading of all the {@link Observation} from server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationsLoadSuccess(ObservationEvent.OnLoaded onLoaded) {
        remoteObservationList = onLoaded.getResponseList();

        if (remoteObservationList.size() > 0 && remoteObservationList != null) {
            displayObservationList();
        } else {
            // TODO: display message that no observations in the List.
        }
    }

    /**
     * Subscribes to the failure event of getting all {@link Observation} from server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationsLoadFailure(ObservationEvent.OnLoadingError onLoadingError) {

    }

    /**
     * Helper method to display list of Observations in RecyclerView.
     */
    private void displayObservationList() {
        //pass the observationList to the Adapter
        mObservationsListAdapter = new ObservationsListAdapter(remoteObservationList);
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
