package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.bus.BusProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class ActivityDetails extends Fragment {

    private final String LOGTAG = "~!@#ActivityDetails :";
    @Bind(R.id.textView_actDet_activityName)
    TextView txtView_name;
    @Bind(R.id.textView_actDet_observationCount)
    TextView txtView_observationCount;
    @Bind(R.id.textView_actDet_totalLocations)
    TextView txtView_totalLocation;
    @Bind(R.id.textView_actDet_totalPeople)
    TextView txtView_totalPeople;
    @Bind(R.id.textView_actDet_totalPets)
    TextView txtView_totalPets;

    private IActivityDetailsCallBacks mListener;
    private Context mContext;
    private Bundle args;
    private Activities mActivity;
    private ImageView imageView_map;

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
        if (getArguments() != null) {
            args = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_details, container, false);
        ButterKnife.bind(this, rootView);
        // if args not null, retrieve the Activities object.
        if (args.getParcelable(UserActivityMasterActivity.KEY_ACTIVITY_DETAILS) != null) {
            mActivity = args.getParcelable(UserActivityMasterActivity.KEY_ACTIVITY_DETAILS);
        }


        return rootView;
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
