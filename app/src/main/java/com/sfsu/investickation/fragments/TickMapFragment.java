package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TickMapFragment extends Fragment {

    private static final String TAG = "`!@#$TickMapFragment";
    @Bind(R.id.mapView_tickmap_main)
    MapView mMapView;
    private GoogleMapController mGoogleMapController;
    private ITickMapCallBack mInterface;
    private Context mContext;

    public TickMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tick_map, container, false);

        ButterKnife.bind(this, v);

        mGoogleMapController = new GoogleMapController(mContext, this);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mMapView.onCreate(mapViewSavedInstanceState);

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mMapView);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final Bundle mapViewSaveState = new Bundle(outState);
        mMapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
        mContext = null;
        mGoogleMapController = null;
        mMapView = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (ITickMapCallBack) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ITickMapCallBack");
        }
    }

    /**
     * Callback interface to handle the onClick in {@link TickMapFragment} Fragment.
     */
    public interface ITickMapCallBack {

    }

}
