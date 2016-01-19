package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.entities.Activities;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Fragment to post List of Activities when the User clicks on the upload button in {@link ActivityList} fragment
 */
public class PostActivitiesList extends Fragment {

    private static final String TAG = "~!@#$PostActList";
    private IPostActivitiesListCallback mInterface;
    private Context mContext;
    private List<Activities> localActivitiesList;
    private DatabaseDataController dbController;

    public PostActivitiesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);

        localActivitiesList = (List<Activities>) dbController.getAll();

        // make a call and upload Activities
        if (localActivitiesList != null) {
            BusProvider.bus().post(new ActivityEvent.OnListLoadingInitialized(localActivitiesList, ApiRequestHandler.ADD));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof IPostActivitiesListCallback) {
            mInterface = (IPostActivitiesListCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IPostActivitiesListCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    /**
     * Subscribes to the event of success in posting Activities to the server
     *
     * @param onMassUploadListLoaded
     */
    @Subscribe
    public void onActivitiesListStoreSuccess(ActivityEvent.OnMassUploadListLoaded onMassUploadListLoaded) {
        Log.i(TAG, "yayy it worked");
        int check = deleteUploadedActivities();
        if (check != -1) {
            Log.i(TAG, "local act deleted");
            mInterface.displayActivityList();
        }
    }

    @Subscribe
    public void onActivitiesListStoreFailure(ActivityEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, onLoadingError.getErrorMessage());
    }

    /**
     * Once all the Activities are uploaded, delete them from database
     */
    private int deleteUploadedActivities() {
        Log.i(TAG, "deleting activities");
        try {
            for (int i = 0; i < localActivitiesList.size(); i++) {
                Activities mActivity = localActivitiesList.get(i);
                boolean check = dbController.delete(mActivity.getId());
                Log.i(TAG, "->" + check);
            }
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Callback interface to handle event of successful upload of List of Activities and re open ActivityList fragment
     */
    public interface IPostActivitiesListCallback {

        /**
         * Callback method to open the ActivityList fragment when all the Activities are posted on server
         */
        void displayActivityList();
    }
}
