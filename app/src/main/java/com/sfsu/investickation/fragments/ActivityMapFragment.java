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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.investickation.customview.MySlidingPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays User's trajectory on Google Maps. In addition, all the locations where the Observations are made, are displayed as
 * Markers on the maps. Upon clicking the marker, a sliding panel is opened which displays the Observation details
 */
public class ActivityMapFragment extends Fragment implements GoogleMapController.IMarkerClickCallBack {

    private static final String TAG = "`!@#$ActMapFrag";
    private static String KEY_OBSERVATIONS_LIST = "activity_id";
    @Bind(R.id.map_view_activities_map)
    MapView mMapView;
    @Bind(R.id.sliding_layout_info_window)
    MySlidingPanelLayout mSlidingUpPanelLayout;
    @Bind(R.id.textview_info_window_observation_name)
    TextView txtView_obsName;
    @Bind(R.id.textview_info_window_location)
    TextView txtView_geoLocation;
    @Bind(R.id.imageview_info_window_tick_image)
    ImageView imageView_obsImage;
    @Bind(R.id.container_content_view_activities)
    RelativeLayout mRelativeLayout_contentView;
    // properties
    private GoogleMapController mGoogleMapController;
    private Context mContext;
    private IActivityMapCallBack mListener;
    private String activityId;
    private ArrayList<Observation> mObservationList;
    private LinearLayout infowindow_linearLayout;

    public ActivityMapFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create new {@link ActivityMapFragment} instance.
     *
     * @param activityId
     * @return
     */
    public static ActivityMapFragment newInstance(ArrayList<Observation> mObservationList) {
        ActivityMapFragment fragment = new ActivityMapFragment();
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

        if (mObservationList != null) {
            mGoogleMapController.setUpPolylineOnMap(mObservationList);
        }

        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

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
     * Callback interface to handle the onClick of the button in {@link ActivityMapFragment} Fragment.
     */
    public interface IActivityMapCallBack {

    }
}
