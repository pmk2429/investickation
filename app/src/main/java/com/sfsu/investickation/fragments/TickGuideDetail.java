package com.sfsu.investickation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.investickation.TickGuideMasterActivity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the detailed information about each Tick in the InvesTICKations project.
 */
public class TickGuideDetail extends Fragment {

    private final String TAG = "~!@#TickDetail :";
    @Bind(R.id.textViewStatic_tickDet_knownFor)
    TextView txtView_knownFor;
    @Bind(R.id.textView_tickDet_tickFormalName)
    TextView txtView_tickFormalName;
    @Bind(R.id.textView_tickDet_tickLocation)
    TextView txtView_tickLocation;
    @Bind(R.id.textView_tickDet_tickName)
    TextView txtView_tickName;
    @Bind(R.id.textView_tickDet_tickSpecies)
    TextView txtView_tickSpecies;
    @Bind(R.id.textView_tickDet_tickDetails)
    TextView txtView_description;

    @Bind(R.id.image_tickGuideDetail)
    ImageView imageView_tickImage;

    private Bundle args;
    private Tick mTick;
    private Context mContext;

    public TickGuideDetail() {
        // Required empty public constructor
    }

    /**
     * Method to create {@link TickGuideDetail} instance.
     *
     * @param key
     * @param tickObj
     * @return
     */
    public static TickGuideDetail newInstance(String key, Tick tickObj) {
        TickGuideDetail tickGuideDetailFragment = new TickGuideDetail();
        Bundle args = new Bundle();
        args.putParcelable(key, tickObj);
        tickGuideDetailFragment.setArguments(args);
        return tickGuideDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_tick_detail);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            args = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tick_guide_detail, container, false);

        ButterKnife.bind(this, rootView);

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarTickDetail);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id
                .collapsing_toolbar_guideDetail);
        collapsingToolbar.setTitle("Tick Details");
        String title = getResources().getString(R.string.tickDetails_toolbar_title);


        if (args.getParcelable(TickGuideMasterActivity.KEY_TICK_DETAIL) != null) {
            mTick = args.getParcelable(TickGuideMasterActivity.KEY_TICK_DETAIL);
        }

        txtView_tickName.setText(mTick.getTickName());
        txtView_tickSpecies.setText(mTick.getSpecies());
        txtView_knownFor.setText(mTick.getKnown_for());
        txtView_tickFormalName.setText(mTick.getScientific_name());
        txtView_description.setText(mTick.getDescription());
        txtView_tickLocation.setText(mTick.getFound_near_habitat());

        Picasso.with(mContext).load(mTick.getImageUrl()).into(imageView_tickImage);

        return rootView;
    }
}
