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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMap extends Fragment implements GoogleMapController.IMarkerClickCallBack {

    private static final String TAG = "`!@#$ActivityMap";
    private static String KEY_OBSERVATIONS_LIST = "activity_id";
    //
    @Bind(R.id.mapView_activitiesMap_main)
    MapView mMapView;
    //
    @Bind(R.id.slidingLayout_infoWindow)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    // TextView
    @Bind(R.id.textView_infoWindow_obsName)
    TextView txtView_obsName;
    @Bind(R.id.textView_infoWindow_location)
    TextView txtView_geoLocation;
    //ImageView
    @Bind(R.id.imageView_infoWindow_tickImage)
    ImageView imageView_obsImage;
    // properties
    private GoogleMapController mGoogleMapController;
    private Context mContext;
    private IActivityMapCallBack mListener;
    private String activityId;
    private ArrayList<Observation> mObservationList;
    private LinearLayout infowindow_linearLayout;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activities_map, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            if (getArguments().getParcelableArrayList(KEY_OBSERVATIONS_LIST) != null) {
                mObservationList = getArguments().getParcelableArrayList(KEY_OBSERVATIONS_LIST);
            }
        }

        mGoogleMapController = new GoogleMapController(mContext, this);

        // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mMapView.onCreate(mapViewSavedInstanceState);

        // setup google Map using the GoogleMapController.
        mGoogleMapController.setupGoogleMap(mMapView);


        if (mObservationList != null) {
            mGoogleMapController.setUpPolylineOnMap(mObservationList);
        }

//        mGoogleMapController.showMarker(mObservationList);


//        // inflate the View from infowindow_panel.xml layout
//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//        View info_rootView = layoutInflater.inflate(R.layout.infowindow_panel, null, false);
//        infowindow_linearLayout = (LinearLayout) info_rootView.findViewById(R.id.hidden_panel);
//        txtView_tickTitle = (TextView) info_rootView.findViewById(R.id.textView_observationTitle);

        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        mSlidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                Log.i(TAG, "onPanelCollapsed");
            }

            @Override
            public void onPanelExpanded(View panel) {
                Log.i(TAG, "onPanelExpanded");
            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

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
        getActivity().setTitle(R.string.title_fragment_activity_map);
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
    }

    @Override
    public void onMarkerClickObservationListener(Observation mObservation) {

        try {
            if (mObservation != null) {
                Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_obsImage);
                txtView_obsName.setText(mObservation.getTickName());
                txtView_geoLocation.setText(mObservation.getGeoLocation());
                // toggle state of Panel
                toggleSlidingPanelLayout();
            } else {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Toggle to show-hide the SlidingPanelLayout
     */
    private void toggleSlidingPanelLayout() {
        if (mSlidingUpPanelLayout != null) {
            if (mSlidingUpPanelLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            } else {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }
    }

    /**
     * Callback interface to handle the onClick of the button in {@link ActivityMap} Fragment.
     */
    public interface IActivityMapCallBack {

    }
}
