package com.sfsu.investickation.fragments;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;

/**
 * Displays the detailed information about each Tick in the InvesTICKations project.
 */
public class TickGuideDetail extends Fragment {

    private final String LOGTAG = "~!@#TickDetail :";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Tick Details");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_guide_detail, container, false);

        // identify the Toolbar and remove it.
//        Toolbar parentActivityToolbar = ((TickGuideMasterActivity) getActivity()).mToolbar;
//        parentActivityToolbar.removeAllViews();
//        parentActivityToolbar.removeAllViewsInLayout();


        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarTickDetail);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_guideDetail);
        collapsingToolbar.setTitle("Tick Name");

        return rootView;
    }


}
