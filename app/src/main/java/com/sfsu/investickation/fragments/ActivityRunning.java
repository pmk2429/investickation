package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfsu.investickation.R;

/**
 * A simple fragment to make the user Add observations for the current ongoing {@link Activities}. This fragment
 * contains the action callback to start new <tt>Observation</tt>. Once the Add Observation button is clicked, the
 * user will be redirected to Add Observation for the current ongoing activity.
 */
public class ActivityRunning extends Fragment {

    private IActivityRunningCallBacks mListener;


    public ActivityRunning() {
        // Required empty public constructor
    }

    public static ActivityRunning newInstance(String param1, String param2) {
        ActivityRunning fragment = new ActivityRunning();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_running, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onActivityRunningItemClickListener();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityRunningCallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface IActivityRunningCallBacks {

        public void onActivityRunningItemClickListener();
    }

}
