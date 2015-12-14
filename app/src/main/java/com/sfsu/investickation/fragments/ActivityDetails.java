package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;

/**
 */
public class ActivityDetails extends Fragment {

    private final String LOGTAG = "~!@#ActivityDetails :";
    private IActivityDetailsCallBacks mListener;
    private Context mContext;

    public ActivityDetails() {
        // Required empty public constructor
    }

    /**
     * Method to create Fragment instance.
     *
     * @param key
     * @param mActivity
     * @return
     */
    public static ActivityDetails newInstance(String key, Activities mActivity) {
        ActivityDetails fragment = new ActivityDetails();
        Bundle args = new Bundle();
        args.putParcelable(key, mActivity);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_details, container, false);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityDetailsCallBacks) activity;
            mContext = activity;
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


    /**
     * Interface callback for handling onClick Listeners in {@link ActivityDetails} Fragment.
     */
    public interface IActivityDetailsCallBacks {
        /**
         * Callback method to handle the click event
         */
        public void onActivityDetailsClick();
    }

}
