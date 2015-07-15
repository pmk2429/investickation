package investickations.com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.investickation.R;

// TODO: Change the name of Interface to something more relevant and appropriate.
// TODO: Change the name of method to make it appropriate to Item click listener

public class ActivityList extends Fragment implements View.OnClickListener {

    private IActivityInteractionListener mInterface;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_list, container, false);
        Button btnTest = (Button) v.findViewById(R.id.btnTest);

        if (btnTest != null) {
            btnTest.setOnClickListener(this);
        } else {
            Log.d(AppConfig.LOGSTRING, "Button not found");
        }
        return v;
    }

    public void onButtonPressed() {
        if (mInterface != null) {
            mInterface.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IActivityInteractionListener) activity;
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
    public void onClick(View v) {
        mInterface.onFragmentInteraction();
    }


    public interface IActivityInteractionListener {
        public void onFragmentInteraction();
    }

}
