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
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ObservationsListAdapter;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Observation} Fragment which is used to display all the observations made by the user
 * and are posted on the server. This fragment is attached to {@link ObservationMasterActivity} and
 * the callbacks of this fragment are implemented in that activity.
 */

public class RemoteObservationsList extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private final String LOGTAG = "~!@#RemoteObs :";
    private IRemoteObservationCallBacks mInterface;
    private Context mContext;
    private List<Observation> observationList;
    private Observation newObservationObject;
    private RecyclerView recyclerView_observations;
    private Bundle args;
    private ObservationsListAdapter mObservationsListAdapter;

    public RemoteObservationsList() {
        // Required empty public constructor
    }


    public static RemoteObservationsList newInstance() {
        return new RemoteObservationsList();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Observations");
        args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remote_observations, container, false);

        // retrieve the Observation Response Object from the Bundle. This object will be the one returned as Response by Retrofit
        if (args != null) {
            newObservationObject = getArguments().getParcelable(AppUtils.OBSERVATION_RESOURCE);
            if (newObservationObject != null) {
                Log.i(LOGTAG, newObservationObject.toString());
            }
        }

        recyclerView_observations = (RecyclerView) v.findViewById(R.id.recyclerview_remote_observations);
        recyclerView_observations.setHasFixedSize(true);

        if (mContext != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(LOGTAG, " No Layout manager supplied");
        }

        observationList = Observation.initializeData();
        // make a network call and get all the Observations
        BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", AppUtils.GET_ALL_METHOD));

        //observationList.add(newObservationObject);

        //pass the observationList to the Adapter
        mObservationsListAdapter = new ObservationsListAdapter(observationList);
        recyclerView_observations.setAdapter(mObservationsListAdapter);

        // implement touch event for the item click in RecyclerView
        recyclerView_observations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_observations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                mInterface.onObservationListItemClickListener();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

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


    /**
     * Subscribes to get list of Observations present on Server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void getObservationList(ObservationEvent.OnLoaded onLoaded) {
        observationList = onLoaded.getResponseList();
    }

    @Subscribe
    public void onReposLoadingFailed(ObservationEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
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
     * Callback Interface to handle onClick Listeners in {@link RemoteObservationsList} Fragment.
     */
    public interface IRemoteObservationCallBacks {

        /**
         * Callback method to handle onClick Listener when user clicks on '+' button in {@link RemoteObservationsList} Fragment.
         */
        public void onObservationAddListener();


        /**
         * Callback method to handle the on item click Listener of the Observations List in {@link RemoteObservationsList}
         * Fragment
         */
        public void onObservationListItemClickListener();
    }


}
