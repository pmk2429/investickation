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

import java.util.List;

import investickations.com.sfsu.controllers.ObservationsListAdapter;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.entities.Observation;
import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */

// TODO: channge the name of interface and method
public class RemoteObservations extends Fragment implements View.OnClickListener {

    private IObservationCallBacks mInterface;
    private Context context;

    private List<Observation> observationList;

    public RemoteObservations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remote_observations, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview_remote_observations);
        rv.setHasFixedSize(true);

        if (context != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
        } else {
            Log.d(AppConfig.LOGSTRING, " No Layout manager supplied");
        }

        observationList = Observation.initialieData();

        ObservationsListAdapter mObservationsListAdapter = new ObservationsListAdapter(observationList);
        rv.setAdapter(mObservationsListAdapter);
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
        mInterface.onObservationInteractionListener();
    }


    public interface IObservationCallBacks {
        public void onObservationInteractionListener();
    }

}
