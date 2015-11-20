package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ObservationsListAdapter;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.utils.AppUtils;

import java.util.List;

/**
 * A simple {@link Observation} Fragment which is used to display all the observations made by the user
 * and are posted on the server. This fragment is attached to {@link ObservationMasterActivity} and
 * the callbacks of this fragment are implemented in that activity.
 */

public class RemoteObservationsList extends Fragment implements View.OnClickListener {

    private final String LOGTAG = "~!@#RemoteObs :";
    private IRemoteObservationCallBacks mInterface;
    private Context context;
    private List<Observation> observationList;
    private Observation newObservationObject;
    private RecyclerView recyclerView_observations;
    private Bundle args;

    public RemoteObservationsList() {
        // Required empty public constructor
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
        }


        recyclerView_observations = (RecyclerView) v.findViewById(R.id.recyclerview_remote_observations);
        recyclerView_observations.setHasFixedSize(true);

        if (context != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(LOGTAG, " No Layout manager supplied");
        }

        observationList = Observation.initializeData();

        //observationList.add(newObservationObject);

        ObservationsListAdapter mObservationsListAdapter = new ObservationsListAdapter(observationList);
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
            context = activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement IObservationCallBacks");
        }
    }

    @Override
    public void onClick(View v) {
        mInterface.onObservationAddListener();
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
