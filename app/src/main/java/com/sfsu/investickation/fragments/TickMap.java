package com.sfsu.investickation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.sfsu.investickation.R;


public class TickMap extends Fragment {

    private static final String TAG = "`!@#$TickMap";
    GoogleMap googleMap;

    public TickMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tick_map, container, false);

        /**
         * Catch the null pointer exception that may be thrown when initialising the map
         */
        try {
            if (null == googleMap) {
                googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapView_tick)).getMap();
            }
        } catch (NullPointerException exception) {
        }

        return v;
    }


}
