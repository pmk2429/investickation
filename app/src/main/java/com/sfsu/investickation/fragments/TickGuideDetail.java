package com.sfsu.investickation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TickGuideDetail extends Fragment {

    private final String LOGTAG = "~!@#TickDetail :";

    public TickGuideDetail() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_guide_detail, container, false);
    }


}
