package investickations.com.sfsu.investickation.fragments;

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

import java.util.List;

import investickations.com.sfsu.utils.adapters.ObservationsListAdapter;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.entities.Observation;
import investickations.com.sfsu.investickation.R;
import investickations.com.sfsu.investickation.RecyclerItemClickListener;

/**
 * A simple {@link Observation} Fragment which is used to display all the observations made by the user
 * and are posted on the server. This fragment is attached to {@link investickations.com.sfsu.investickation.ObservationMainActivity} and
 * the callbacks of this fragment are implemented in that activity.
 */

public class RemoteObservationsList extends Fragment implements View.OnClickListener {

    private IObservationCallBacks mInterface;
    private Context context;

    private List<Observation> observationList;

    public RemoteObservationsList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Observations");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remote_observations, container, false);
        RecyclerView recyclerView_observations = (RecyclerView) v.findViewById(R.id.recyclerview_remote_observations);

        recyclerView_observations.setHasFixedSize(true);

        if (context != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(llm);
        } else {
            Log.d(AppConfig.LOGSTRING, " No Layout manager supplied");
        }

        observationList = Observation.initialieData();

        ObservationsListAdapter mObservationsListAdapter = new ObservationsListAdapter(observationList);
        recyclerView_observations.setAdapter(mObservationsListAdapter);

        // implement touch event for the item click in RecyclerView
        recyclerView_observations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_observations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                mInterface.onItemClickListener();
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
            mInterface = (IObservationCallBacks) activity;
            context = activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement IObservationCallBacks");
        }
    }

    @Override
    public void onClick(View v) {
        mInterface.onObservationAddListener();
    }


    public interface IObservationCallBacks {
        public void onObservationAddListener();

        public void onItemClickListener();
    }


}
