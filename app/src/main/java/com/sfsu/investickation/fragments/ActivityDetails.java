package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;

/**
 */
public class ActivityDetails extends Fragment {

    private final String LOGTAG = "~!@#ActivityDetails :";
    private IActivityDetailsCallBacks mListener;

    public ActivityDetails() {
        // Required empty public constructor
    }


    public static ActivityDetails newInstance() {
        ActivityDetails fragment = new ActivityDetails();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Activity Details");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_activity_details, container, false);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityDetailsCallBacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityDetailsCallBacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


    public interface IActivityDetailsCallBacks {
        public void onActivityDetailsClick();
    }

}
