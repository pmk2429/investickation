package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMap extends Fragment implements GoogleMapController.IMarkerClickCallBack {

    private static final String TAG = "`!@#$ActivityMap";
    private static String KEY_OBSERVATIONS_LIST = "activity_id";
    @Bind(R.id.mapView_activitiesMap_main)
    MapView mMapView;
    private GoogleMapController mGoogleMapController;
    private Context mContext;
    private IActivityMapCallBack mListener;
    private String activityId;
    private List<Observation> mObservationList;
    private LinearLayout infowindow_linearLayout;
    private TextView txtView_tickTitle;

    public ActivityMap() {
        // Required empty public constructor
    }


    /**
     * Factory method to create new {@link ActivityMap} instance.
     *
     * @param activityId
     * @return
     */
    public static ActivityMap newInstance(ArrayList<Observation> mObservationList) {
        ActivityMap fragment = new ActivityMap();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_OBSERVATIONS_LIST, mObservationList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getParcelableArrayList(KEY_OBSERVATIONS_LIST) != null) {
                mObservationList = getArguments().getParcelableArrayList(KEY_OBSERVATIONS_LIST);
            }
        }
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

        // once the GoogleMap is setup, add polyline to the Maps.
        LatLng[] latLngs = new LatLng[]{
                new LatLng(40.737102, -73.990318),
                new LatLng(40.749825, -73.987963),
                new LatLng(40.752946, -73.987384),
                new LatLng(40.755823, -73.986397)};

        mGoogleMapController.setUpPolylineOnMap(latLngs);

//        mGoogleMapController.showMarker(mObservationList);


        // inflate the View from infowindow_panel.xml layout
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View info_rootView = layoutInflater.inflate(R.layout.infowindow_panel, null, false);
        infowindow_linearLayout = (LinearLayout) info_rootView.findViewById(R.id.hidden_panel);
        txtView_tickTitle = (TextView) info_rootView.findViewById(R.id.textView_observationTitle);

        return rootView;

    }

    public void slideUpDown() {
        if (!isPanelShown()) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(mContext, R.anim.bottom_up);

            infowindow_linearLayout.startAnimation(bottomUp);
            infowindow_linearLayout.setVisibility(View.VISIBLE);
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(mContext, R.anim.bottom_down);

            infowindow_linearLayout.startAnimation(bottomDown);
            infowindow_linearLayout.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return infowindow_linearLayout.getVisibility() == View.VISIBLE;
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

    @Override
    public void onMarkerClickListener(Marker marker) {
        Log.i(TAG, "inside onMarkerClick");
        txtView_tickTitle.setText(marker.getTitle());
        slideUpDown();
    }

    /**
     * Callback interface to handle the onClick of the button in {@link ActivityMap} Fragment.
     */
    public interface IActivityMapCallBack {

    }
}
