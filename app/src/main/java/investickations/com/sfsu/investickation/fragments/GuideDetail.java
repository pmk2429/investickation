package investickations.com.sfsu.investickation.fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideDetail extends Fragment {


    public GuideDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_detail, container, false);
    }


}
