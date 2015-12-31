package com.sfsu.investickation.fragments;


import android.content.Context;
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
import com.sfsu.utils.AppUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservationDetail extends Fragment {

    private final String TAG = "~!@#ObservationDetail";
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
    ImageView imageView_tickImage;


    private Bundle args;
    private Observation mObservation;
    private Context mContext;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mContext = mContext;
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_observation_detail);
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

        Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_tickImage);
        textView_activityName.setText("Hiking");
        textView_description.setText("Found while hiking near Golden gate park");
        textView_foundNear.setText(mObservation.getGeoLocation());
        textView_location.setText("35.755815, -121.859291");
        textView_timestamp.setText(AppUtils.getCurrentDateAndTime(mObservation.getTimestamp()));
        return rootView;
    }


}
