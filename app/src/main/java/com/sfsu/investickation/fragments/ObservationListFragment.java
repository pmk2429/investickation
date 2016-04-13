package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ObservationsListAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.dialogs.UploadAlertDialog;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.sfsu.utils.MyRecyclerScroll;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>
 * Displays the list of {@link Observation}s made by the Account. The observations which are retrieved from the Server are
 * combined to that stored on the Local SQLite DB. Depending on the the where the Observation is stored, the RecyclerView item
 * displays an icon representing cloud or database.
 * </p>
 * <p>
 * The list will also be displayed when the User will add observation in the middle of the Activity Running. This will set the
 * flag and will display a status message that Activity is Running
 * </p>
 * <p>
 * Also displays the observations made for a specific {@link com.sfsu.entities.Activities}. Using the <tt>activityId</tt>, the
 * network call is made for list of observations corresponding to a specific activity.
 * </p>
 */

public class ObservationListFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener,
        UploadAlertDialog.IUploadDataCallback {

    private static final String KEY_ACTIVITY_ID = "activity_id";
    private static final String KEY_STATUS_FLAG = "observation_caller_flag";
    private final String TAG = "~!@#ObsList";
    @Bind(R.id.recyclerview_remote_observations)
    RecyclerView recyclerView_observations;

    @Bind(R.id.relativeLayout_obsList_main)
    RelativeLayout mRelativeLayout;

    @Bind(R.id.textViewStatic_obsList_listInfo)
    TextView txtView_observationList_info;

    @Bind(R.id.fab_observation_add)
    FloatingActionButton fab_addObservation;

    private IRemoteObservationCallBacks mInterface;
    private Context mContext;
    private List<Observation> mObservationList, responseObservationList, localObservationList;
    private Observation newObservationObject;
    private Bundle args;
    private ObservationsListAdapter mObservationsListAdapter;
    private DatabaseDataController dbController;
    private String activityId;
    private boolean FLAG_GET_ACTIVITY_OBSERVATIONS, FLAG_ACTIVITY_RUNNING;
    private int fabMargin;
    private Animation animation;
    private UploadAlertDialog mUploadAlertDialog;
    private ProgressDialog mProgressDialog;

    public ObservationListFragment() {
        // Required empty public constructor
    }

    /**
     * Creates instance of an {@link ObservationListFragment} fragment initialzed with <tt>activityId</tt>
     *
     * @param key
     * @param activityId
     * @return
     */
    public static ObservationListFragment newInstance(String activityId) {
        ObservationListFragment mObservationListFragment = new ObservationListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ACTIVITY_ID, activityId);
        mObservationListFragment.setArguments(args);
        return mObservationListFragment;
    }

    /**
     * Sets the status flag depending on the callee and creates {@link ObservationListFragment} instance
     *
     * @param statusFlag
     * @return
     */
    public static ObservationListFragment newInstance(long statusFlag) {
        ObservationListFragment mObservationListFragment = new ObservationListFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_STATUS_FLAG, statusFlag);
        mObservationListFragment.setArguments(args);
        return mObservationListFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mInterface = (IRemoteObservationCallBacks) activity;
            mContext = activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement IObservationCallBacks");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_remote_observations, container, false);

        ButterKnife.bind(this, rootView);

        animation = AnimationUtils.loadAnimation(mContext, R.anim.simple_grow);

        txtView_observationList_info.setVisibility(View.GONE);

        setHasOptionsMenu(true);

        dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());
        mUploadAlertDialog = new UploadAlertDialog(mContext, this);


        // get the ActivityId from the Bundle.
        if (getArguments() != null) {
            args = getArguments();
        }
        if (args != null && args.containsKey(KEY_ACTIVITY_ID)) {
            activityId = args.getString(KEY_ACTIVITY_ID);
            Log.i(TAG, activityId);
            FLAG_GET_ACTIVITY_OBSERVATIONS = true;
        } else if (args != null && args.containsKey(KEY_STATUS_FLAG)) {
            long statusFlag = args.getLong(KEY_STATUS_FLAG);
            FLAG_ACTIVITY_RUNNING = statusFlag == ObservationMasterActivity.FLAG_ACTIVITY_RUNNING ? true : false;
        } else {
            activityId = null;
            FLAG_GET_ACTIVITY_OBSERVATIONS = false;
            FLAG_ACTIVITY_RUNNING = false;
        }


        // call to network depending on the type of call to be made.
        if (activityId != null) {
            // get all Observations depending on network connection.
            if (AppUtils.isConnectedOnline(mContext)) {
                // if the user has clicked ViewObservations in ActivityDetailFragment fragment, then show only the Activity's Observations
                // Observations specific to Activity
                BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", activityId, ApiRequestHandler.ACT_OBSERVATIONS));
                displayProgressDialog("Fetching Observations...");
            } else {
                // network not available.
                // if the user has clicked ViewObservations in ActivityDetailFragment fragment, then show only the Activity's Observations
                Log.i(TAG, "get all observations of Act");
                localObservationList = (List<Observation>) dbController.getAll(activityId);

            }
        } else {
            // get all the observations
            if (AppUtils.isConnectedOnline(mContext)) {
                // get all the observations made by User
                BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized("", ApiRequestHandler.GET_ALL));
                displayProgressDialog("Fetching Observations...");
            } else {
                // get all the observations. When the User clicks MyObservations in Nav Drawer or from DashboardFragment
                localObservationList = (List<Observation>) dbController.getAll();
            }

            if (!AppUtils.isConnectedOnline(mContext)) {

                if (mObservationList != null) {
                    mObservationList.clear();
                }
                // for local observations only
                if (localObservationList != null) {
                    Log.i(TAG, "->" + localObservationList.size());
                    // make observation list
                    mObservationList = localObservationList;

                }

                if (mObservationList.size() > 0 && mObservationList != null) {
                    displayObservationList();
                } else if (mObservationList.size() == 0) {
                    // display text message
                    txtView_observationList_info.setVisibility(View.VISIBLE);
                    mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightText));
                    recyclerView_observations.setVisibility(View.GONE);
                } else {
                    Log.i(TAG, "activity list size < 0");
                }
            }
        }

        // doesn't matter if the Network is online or offline, display the message saying Activity is running
        if (FLAG_ACTIVITY_RUNNING) {

        }

        recyclerView_observations.setHasFixedSize(true);

        if (mContext != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_observations.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No Layout manager supplied");
        }

        // onclick of Add Observation FAB.
        fab_addObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onObservationAddListener();
            }
        });

        return rootView;
    }

    /**
     * Displays progress dialog
     */
    private void displayProgressDialog(String message) {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * Dismisses progress dialog
     */
    private void dismissProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_observation_list);
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        mInterface.onObservationAddListener();
    }


    /**
     * Subscribes to the event of success in loading of all the {@link Observation} from server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationsLoadSuccess(ObservationEvent.OnListLoaded onLoaded) {
        dismissProgressDialog();
        // list of Observations from server
        responseObservationList = onLoaded.getResponseList();
        localObservationList = (List<Observation>) dbController.getAll();

        for (int i = 0; i < responseObservationList.size(); i++) {
            responseObservationList.get(i).setIsOnCloud(true);
        }

        for (int i = 0; i < localObservationList.size(); i++) {
            localObservationList.get(i).setIsOnCloud(false);
        }

        // add all local Observations to Remote observations.
        responseObservationList.addAll(localObservationList);

        if (mObservationList != null) {
            mObservationList.clear();
        }

        // finally make a list of All Observations from local and server.
        mObservationList = responseObservationList;

        // depending on size, display list of Observations.
        if (mObservationList.size() > 0 && mObservationList != null) {
            displayObservationList();
        } else if (mObservationList.size() == 0) {
            txtView_observationList_info.setVisibility(View.VISIBLE);
            recyclerView_observations.setVisibility(View.GONE);
            mRelativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightText));
        } else {

        }
    }

    /**
     * Subscribes to the failure event of getting all {@link Observation} from server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationsLoadFailure(ObservationEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
//        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Helper method to display list of Observations in RecyclerView.
     */
    private void displayObservationList() {
        fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        //pass the mObservationList to the Adapter
        mObservationsListAdapter = new ObservationsListAdapter(mObservationList, mContext);
        mObservationsListAdapter.notifyDataSetChanged();

        recyclerView_observations.setAdapter(mObservationsListAdapter);


        // implement touch event for the item click in RecyclerView
        recyclerView_observations.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_observations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                mInterface.onObservationListItemClickListener(mObservationList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        recyclerView_observations.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab_addObservation.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                fab_addObservation.animate().translationY(fab_addObservation.getHeight() + fabMargin).setInterpolator
                        (new AccelerateInterpolator(2)).start();
            }
        });

        // finally apply the animation
        fab_addObservation.startAnimation(animation);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_observation_list, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        } else {
            Log.i(TAG, "search is null");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_upload:
                uploadObservations();
                return true;
        }

        return false;

    }

    private void uploadObservations() {
        if (localObservationList != null) {
            mUploadAlertDialog.showObservationUploadAlertDialog(localObservationList.size());
        } else {
            mUploadAlertDialog.showObservationUploadAlertDialog(-1);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /*
    This will be fired when the text in the SearchView changes. Accordingly the Incremental search will be performed by the
    name of each element present in the list.
     */
    @Override
    public boolean onQueryTextChange(String query) {
        final List<Observation> filteredModelList = filter(mObservationList, query);
        mObservationsListAdapter.animateTo(filteredModelList);
        recyclerView_observations.scrollToPosition(0);
        return true;
    }

    /**
     * Helper method to filter the List of Observation on search text change in this Fragment.
     *
     * @param observationList
     * @param query
     * @return
     */
    private List<Observation> filter(List<Observation> observationList, String query) {
        query = query.toLowerCase();

        final List<Observation> filteredObservationList = new ArrayList<>();
        for (Observation observation : observationList) {
            // perform the search on TickName since it will be visible to user.
            final String text = observation.getTickName().toLowerCase();
            if (text.contains(query)) {
                filteredObservationList.add(observation);
            }
        }
        return filteredObservationList;
    }

    @Override
    public void onUploadClick(long resultCode) {
        if (resultCode == UploadAlertDialog.RESULT_OK) {
            mInterface.onUploadListOfObservations();
        } else if (resultCode == UploadAlertDialog.RESULT_INVALID) {

        } else if (resultCode == UploadAlertDialog.RESULT_NO_DATA) {
        }
    }


    /**
     * Callback Interface to handle onClick Listeners in {@link ObservationListFragment} Fragment.
     */
    public interface IRemoteObservationCallBacks {

        /**
         * Callback method to handle onClick Listener when user clicks on '+' button in {@link ObservationListFragment} Fragment.
         */
        public void onObservationAddListener();

        /**
         * Callback method to handle the on item click Listener of the Observations List in {@link ObservationListFragment}
         * Fragment
         *
         * @param observation
         */
        public void onObservationListItemClickListener(Observation mObservation);

        /**
         * Callback to upload list of Observation depending on user's choice
         */
        public void onUploadListOfObservations();
    }


}