package investickations.com.sfsu.investickation.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */

// TODO: channge the name of interface and method
public class RemoteObservations extends Fragment implements View.OnClickListener {

    private IObservationsInteractionListener mInterface;

    public RemoteObservations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remote_observations, container, false);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mInterface = (IObservationsInteractionListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement IObservationsListListener");
        }
    }

    @Override
    public void onClick(View v) {
        mInterface.onObservationInteractionListener();
    }


    public interface IObservationsInteractionListener {
        public void onObservationInteractionListener();
    }

}
