package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.entities.Account;
import com.sfsu.entities.Activities;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the details of a specific {@link Activities}. Allows {@link Account} to see all the observations
 * that belongs to this specific activity.
 */
public class ActivityDetailFragment extends Fragment implements View.OnClickListener {

    private static String KEY_ARGS = "activity_object";
    public final String TAG = "~!@#ActivityDet";
    @Bind(R.id.textView_actDet_activityName)
    TextView txtView_name;
    @Bind(R.id.textView_actDet_observationCount)
    TextView txtView_observationCount;
    @Bind(R.id.textView_actDet_date)
    TextView txtView_date;
    @Bind(R.id.textView_actDet_totalPeople)
    TextView txtView_totalPeople;
    @Bind(R.id.textView_actDet_totalPets)
    TextView txtView_totalPets;
    @Bind(R.id.textView_actDet_time)
    TextView txtView_time;
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

    private DatabaseDataController dbController;
    private List<Observation> mObservationList;

    public ActivityDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Method to create {@link ActivityDetailFragment} instance.
     *
     * @param key
     * @param mActivity
     * @return
     */
    public static ActivityDetailFragment newInstance(Activities mActivity) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ARGS, mActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_details, container, false);
        ButterKnife.bind(this, rootView);


        if (getArguments() != null) {
            args = getArguments();
        }
        gson = new Gson();
        activityPref = mContext.getSharedPreferences(UserActivityMasterActivity.PREF_ACTIVITY_DATA, Context.MODE_PRIVATE);
        dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());

        button_viewObservations.setOnClickListener(this);
        icon_openMap.setOnClickListener(this);

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
    public void onPause() {
        super.onPause();
        // save the Currently running object in SharedPref to retrieve it later using editor.
        editor = activityPref.edit();
        String activityJson = gson.toJson(mActivity);
        editor.putString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, activityJson);
        editor.apply();

        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_activity_detail);

        BusProvider.bus().register(this);

        // depending on the args, populate view based on the Activity restored.
        if (args != null) {
            // if args not null, retrieve the Activities object.
            if (args.getParcelable(KEY_ARGS) != null) {
                mActivity = args.getParcelable(KEY_ARGS);
            }
        } else {
            // get data from SharedPref
            String activityJson = activityPref.getString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, "no-data");
            mActivity = gson.fromJson(activityJson, Activities.class);
        }

        if (mActivity != null) {
            populateView();
        }

        if (mActivity.getId() != null) {
            // since we have the activityId, get all Observations for this Activity and display it on Maps as well as view it in List.
            BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", mActivity.getId(), ApiRequestHandler.ACT_OBSERVATIONS));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dbController.closeConnection();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Helper method to populate View in ActivityDetailFragment.
     */
    private void populateView() {
        try {
            // once the object is collected, display it in the respective controls.
            String activityName = mActivity.getActivityName() + " @ " + mActivity.getLocation_area();
            txtView_name.setText(activityName);

            String observationCount = mActivity.getNum_of_ticks() + " Obs.";
            txtView_observationCount.setText(observationCount);
            String[] dateAndTime = AppUtils.getDateAndTimeSeparate(mActivity.getTimestamp());
            txtView_date.setText(dateAndTime[0]);
            txtView_time.setText(dateAndTime[1]);
            String people = mActivity.getNum_of_people() + " people";
            txtView_totalPeople.setText(people);
            String pets = mActivity.getNum_of_pets() + " pets";
            txtView_totalPets.setText(pets);
            String image_url = mActivity.getImage_url();
            // imageFile
            File imgFile = new File(mActivity.getImage_url());

            // depending on the image url, display the activity image
            if (image_url == null || image_url.equals("")) {
                imageView_staticMap.setImageResource(R.mipmap.placeholder_activity);
            } else {
                if (AppUtils.isConnectedOnline(mContext)) {
                    if (mActivity.getImage_url().startsWith("http")) {
                        Picasso.with(mContext).load(mActivity.getImage_url()).into(imageView_staticMap);
                    } else {
                        if (imgFile.exists()) {
                            Bitmap tickBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imageView_staticMap.setImageBitmap(tickBitmap);
                        }
                    }
                } else {
                    if (imgFile.exists()) {
                        Bitmap tickBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageView_staticMap.setImageBitmap(tickBitmap);
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // delete the SharedPref data
        activityPref.edit().remove(UserActivityMasterActivity.PREF_ACTIVITY_DATA).apply();

        // free up all the resources.
        mActivity = null;
        mContext = null;
        gson = null;
        dbController = null;
        mObservationList = null;
    }


    /**
     * Subscribes to the event of success loading of {@link com.sfsu.entities.Observation} related to this Activity.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationsLoadSuccess(ObservationEvent.OnListLoaded onLoaded) {
        try {
            mObservationList = onLoaded.getResponseList();
            // only if the observations can be retrieved for current Activity, open the List of Observation.
            if (mObservationList == null || mObservationList.size() <= 0) {
                button_viewObservations.setEnabled(false);
                button_viewObservations.setText(R.string.actDet_noObservationRecorded);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Subscribes to the event of failure in deletion of {@link Activities}.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onDeleteActivityFailure(ActivityEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_actDet_viewAllObservation:
                mListener.onViewAllObservationsClicked(mActivity.getId());
                break;
            case R.id.icon_actDet_openMap:
                try {
                    ArrayList<Observation> mObservationArrayList = new ArrayList<Observation>(mObservationList);
                    mListener.onOpenActivitiesMapClicked(mObservationArrayList);
                } catch (Exception e) {
                    if (BuildConfig.DEBUG)
                        Log.e(TAG, "onClick: ", e);
                }
                break;
        }
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
                deleteActivity();
                return true;
        }
        return false;
    }

    /**
     * Delete the {@link Activities} from Local Storage or Cloud Server depending on where its stored.
     */
    private void deleteActivity() {
        if (mActivity.isOnCloud()) {
            AlertDialog.Builder deleteActivityDialog = new AlertDialog.Builder(mContext);
            String deleteTitle = "Delete '" + mActivity.getActivityName() + "' Activity?";
            deleteActivityDialog.setTitle(deleteTitle);
            deleteActivityDialog.setMessage(R.string.alertDialog_delete_activity_warning);
            deleteActivityDialog.setIcon(R.mipmap.ic_delete_black_24dp);

            deleteActivityDialog.setPositiveButton(R.string.alertDialog_YES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized(mActivity.getId(), ApiRequestHandler.DELETE));
                }
            });

            deleteActivityDialog.setNegativeButton(R.string.alertDialog_NO, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            deleteActivityDialog.show();
        } else {
            dbController.delete(mActivity.getId());
            // open ActivityListFragment after deleting the Observation
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Subscribes to the event of successful deletion of {@link Activities} from the server
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivityDeleteSuccess(ActivityEvent.OnLoadedCount onLoaded) {
        if (onLoaded.getResponse().getCount() == 1) {
            Toast.makeText(mContext, "Activity deleted", Toast.LENGTH_LONG).show();
            // open ActivityListFragment after the current Activity is successfully deleted.
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Subscribes to the event of failure in deleting {@link Activities} from the server
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onActivityDeleteFailure(ActivityEvent.OnLoadingError onLoadingError) {
        //Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Interface callback for handling onClick Listeners in {@link ActivityDetailFragment} Fragment.
     */
    public interface IActivityDetailsCallBacks {
        /**
         * Callback method to handle the click event when user clicks <tt>View Observation</tt> button in {@link
         * ActivityDetailFragment} fragment.
         */
        public void onViewAllObservationsClicked(String activityId);

        /**
         * Callback method to handle the onclick event of the button in {@link ActivityDetailFragment} fragment to open up the {@link
         * ObservationMap} fragment.
         */
        public void onOpenActivitiesMapClicked(ArrayList<Observation> mObservationList);
    }

}
