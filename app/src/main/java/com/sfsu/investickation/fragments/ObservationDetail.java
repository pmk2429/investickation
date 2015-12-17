package com.sfsu.investickation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservationDetail extends Fragment {

    private final String LOGTAG = "~!@#ObservationDetail :";
    @Bind(R.id.textView_obsDet_activityName)
    TextView textView_activityName;
    @Bind(R.id.textView_obsDet_description)
    TextView textView_description;

    @Bind(R.id.textView_obsDet_foundNear)
    TextView textView_foundNear;

    @Bind(R.id.textView_obsDet_location)
    TextView textView_location;

    @Bind(R.id.textView_obsDet_tickName)
    TextView textView_tickName;

    @Bind(R.id.textView_obsDet_timestamp)
    TextView textView_timestamp;

    @Bind(R.id.imageView_obsDet_tickImage)
    ImageView tickImage;


    private Bundle args;
    private Observation mObservation;

    public ObservationDetail() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link ObservationDetail} Fragment.
     *
     * @param key
     * @param mObservation
     * @return
     */
    public static ObservationDetail newInstance(String key, Observation mObservation) {
        ObservationDetail mObservationDetail = new ObservationDetail();
        Bundle args = new Bundle();
        args.putParcelable(key, mObservation);
        mObservationDetail.setArguments(args);
        return mObservationDetail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Observation Details");
        if (getArguments() != null) {
            args = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_observation_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (args.getParcelable(ObservationMasterActivity.KEY_OBSERVATION_DETAIL) != null) {
            mObservation = args.getParcelable(ObservationMasterActivity.KEY_OBSERVATION_DETAIL);
        }


        return rootView;
    }


}
