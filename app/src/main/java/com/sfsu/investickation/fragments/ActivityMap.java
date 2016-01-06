package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.MapView;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.investickation.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMap extends Fragment {

    private static final String TAG = "`!@#$ActivityMap";
    @Bind(R.id.mapView_activitiesMap_main)
    MapView mMapView;
    @Bind(R.id.button_actMap_viewList)
    Button btn_viewList;
    private GoogleMapController mGoogleMapController;
    private Context mContext;
    private IActivityMapCallBack mListener;

    public ActivityMap() {
        // Required empty public constructor
    }


    public static ActivityMap newInstance(String param1) {
        ActivityMap fragment = new ActivityMap();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities_map, container, false);

        ButterKnife.bind(this, rootView);

        mGoogleMapController = new GoogleMapController(mContext, this);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mMapView.onCreate(mapViewSavedInstanceState);

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mMapView);

        return rootView;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IActivityMapCallBack) {
            mListener = (IActivityMapCallBack) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IObservationMapCallBack interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContext = null;
        mGoogleMapController = null;
        mMapView = null;
    }

    /**
     * Callback interface to handle the onClick of the button in {@link ActivityMap} Fragment.
     */
    public interface IActivityMapCallBack {

    }
}
