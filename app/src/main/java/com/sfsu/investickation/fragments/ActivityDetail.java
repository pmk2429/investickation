package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sfsu.entities.Account;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the details of a specific {@link Activities}. Allows {@link Account} to see all the observations
 * that belongs to this specific activity.
 */
public class ActivityDetail extends Fragment {

    public final String TAG = "~!@#ActivityDet";
    @Bind(R.id.textView_actDet_activityName)
    TextView txtView_name;
    @Bind(R.id.textView_actDet_observationCount)
    TextView txtView_observationCount;
    @Bind(R.id.textView_actDet_totalLocations)
    TextView txtView_totalLocation;
    @Bind(R.id.textView_actDet_totalPeople)
    TextView txtView_totalPeople;
    @Bind(R.id.textView_actDet_totalPets)
    TextView txtView_totalPets;
    @Bind(R.id.textView_actDet_totalDistance)
    TextView txtView_totalDistance;

    @Bind(R.id.button_actDet_viewAllObservation)
    Button button_viewObservations;

    @Bind(R.id.imageView_actDet_staticMap)
    ImageView imageView_staticMap;

    @Bind(R.id.icon_actDet_openMap)
    ImageView icon_openMap;

    private IActivityDetailsCallBacks mListener;
    private Context mContext;
    private Bundle args;
    private Activities mActivity;

    private SharedPreferences activityPref;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public ActivityDetail() {
        // Required empty public constructor
    }

    /**
     * Method to create {@link ActivityDetail} instance.
     *
     * @param key
     * @param mActivity
     * @return
     */
    public static ActivityDetail newInstance(String key, Activities mActivity) {
        ActivityDetail fragment = new ActivityDetail();
        Bundle args = new Bundle();
        args.putParcelable(key, mActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_activity_detail);
        if (getArguments() != null) {
            args = getArguments();
        }
        gson = new Gson();
        activityPref = mContext.getSharedPreferences(UserActivityMasterActivity.PREF_ACTIVITY_DATA, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_details, container, false);
        ButterKnife.bind(this, rootView);

        button_viewObservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onViewAllObservationsClicked(mActivity.getId());
            }
        });

        icon_openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onOpenActivitiesMapClicked(mActivity.getId());
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityDetailsCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityDetailsCallBacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save the Currently running object in SharedPref to retrieve it later using editor.
        editor = activityPref.edit();
        String activityJson = gson.toJson(mActivity);
        editor.putString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, activityJson);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        // depending on the args, populate view based on the Activity restored.
        if (args != null) {
            // if args not null, retrieve the Activities object.
            if (args.getParcelable(UserActivityMasterActivity.KEY_ACTIVITY_DETAILS) != null) {
                mActivity = args.getParcelable(UserActivityMasterActivity.KEY_ACTIVITY_DETAILS);
            }
        } else {
            // get data from SharedPref
            String activityJson = activityPref.getString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, "no-data");
            mActivity = gson.fromJson(activityJson, Activities.class);
        }

        if (mActivity != null) {
            populateView();
        }
    }

    /**
     * Helper method to populate View in ActivityDetail.
     */
    private void populateView() {
        // once the object is collected, display it in the respective controls.
        String activityName = mActivity.getActivityName() + " @ " + mActivity.getLocation_area();
        txtView_name.setText(activityName);

        String observationCount = mActivity.getNum_of_ticks() + " Obs.";
        txtView_observationCount.setText(observationCount);

        // TODO: think about this one
        txtView_totalLocation.setText("00");

        String people = mActivity.getNum_of_people() + " people";
        txtView_totalPeople.setText(people);

        String pets = mActivity.getNum_of_pets() + " pets";
        txtView_totalPets.setText(pets);

        // TODO: think about this one.
        txtView_totalDistance.setText("00");

        if (mActivity.getImage_url().equals("") || mActivity.getImage_url().equals(null)) {
            imageView_staticMap.setImageResource(R.mipmap.placeholder_activity);
        } else {
            Picasso.with(mContext).load(mActivity.getImage_url()).into(imageView_staticMap);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // delete the SharedPref data
        activityPref.edit().remove(UserActivityMasterActivity.PREF_ACTIVITY_DATA).apply();
    }

    /**
     * Interface callback for handling onClick Listeners in {@link ActivityDetail} Fragment.
     */
    public interface IActivityDetailsCallBacks {
        /**
         * Callback method to handle the click event when user clicks <tt>View Observation</tt> button in {@link
         * ActivityDetail} fragment.
         */
        public void onViewAllObservationsClicked(String activityId);

        /**
         * Callback method to handle the onclick event of the button in {@link ActivityDetail} fragment to open up the {@link
         * ObservationMap} fragment.
         */
        public void onOpenActivitiesMapClicked(String activityId);
    }

}
