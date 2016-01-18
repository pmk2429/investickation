package com.sfsu.investickation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ObservationDetail extends Fragment {

    private static final String KEY_OBSERVATION = "observation_object_detail";
    private final String TAG = "~!@#ObservationDetail";
    @Bind(R.id.textView_obsDet_activityName)
    TextView textView_activityName;
    @Bind(R.id.textView_obsDet_description)
    TextView textView_description;

    @Bind(R.id.textView_obsDet_geoLocation)
    TextView textView_geoLocation;

    @Bind(R.id.textView_obsDet_latLng)
    TextView textView_latLng;

    @Bind(R.id.textView_obsDet_tickName)
    TextView textView_tickName;

    @Bind(R.id.textView_obsDet_timestamp)
    TextView textView_timestamp;

    @Bind(R.id.imageView_obsDet_tickImage)
    ImageView imageView_tickImage;


    private Bundle args;
    private Observation mObservation;
    private Context mContext;
    private DatabaseDataController dbController;

    public ObservationDetail() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link ObservationDetail} Fragment.
     *
     * @param key
     * @param mObservation
     * @return
     */
    public static ObservationDetail newInstance(Observation mObservation) {
        ObservationDetail mObservationDetail = new ObservationDetail();
        Bundle args = new Bundle();
        args.putParcelable(KEY_OBSERVATION, mObservation);
        mObservationDetail.setArguments(args);
        return mObservationDetail;
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
        getActivity().setTitle(R.string.title_fragment_observation_detail);
        dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            args = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_observation_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (args.getParcelable(KEY_OBSERVATION) != null) {
            mObservation = args.getParcelable(KEY_OBSERVATION);
        }

        Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_tickImage);
        // set all the details
        textView_activityName.setText("Hiking");
        textView_description.setText("Found while hiking near Golden gate park");
        textView_geoLocation.setText(mObservation.getGeoLocation());
        textView_latLng.setText("35.755815, -121.859291");
        long now = System.currentTimeMillis();
        CharSequence charSequence = DateUtils.getRelativeTimeSpanString(mObservation.getTimestamp(), now, DateUtils
                .DAY_IN_MILLIS);
//        textView_timestamp.setText(AppUtils.getDateAndTime(mObservation.getTimestamp()));
        textView_timestamp.setText(charSequence);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_detail, menu);
        final MenuItem item = menu.findItem(R.id.action_delete);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteObservation();
                return true;
        }

        return false;
    }

    /**
     * Helper method to delete the current {@link Observation} from the server/database.
     */
    private void deleteObservation() {
        if (mObservation.isOnCloud()) {
            BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(mObservation.getId(), ApiRequestHandler.DELETE));
        } else {
            dbController.delete(mObservation.getId());
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
