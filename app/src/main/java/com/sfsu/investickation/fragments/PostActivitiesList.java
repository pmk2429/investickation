package com.sfsu.investickation.fragments;

import android.app.ProgressDialog;
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
 * Intermediate Fragment to post List of Activities when the User clicks on the upload button in {@link ActivityList} fragment
 */
public class PostActivitiesList extends Fragment {

    private static final String TAG = "~!@#$PostActList";
    private Context mContext;
    private List<Activities> localActivitiesList;
    private DatabaseDataController dbController;
    private ProgressDialog mProgressDialog;

    public PostActivitiesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);

        dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());

        mProgressDialog = new ProgressDialog(mContext);

        localActivitiesList = (List<Activities>) dbController.getAll();

        // make a call and upload Activities
        if (localActivitiesList != null) {
            BusProvider.bus().post(new ActivityEvent.OnListLoadingInitialized(localActivitiesList, ApiRequestHandler.ADD));
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setMessage("Uploading Activities...");
//            mProgressDialog.show();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Subscribes to the event of success in posting Activities to the server
     *
     * @param onMassUploadListLoaded
     */
    @Subscribe
    public void onActivitiesListStoreSuccess(ActivityEvent.OnMassUploadListLoaded onMassUploadListLoaded) {
        if (dbController == null)
            dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());

        if (deleteUploadedActivities()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void onActivitiesListStoreFailure(ActivityEvent.OnLoadingError onLoadingError) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     * Once all the Activities are uploaded, delete them from database
     */
    private boolean deleteUploadedActivities() {
        boolean check = false;
        try {
            for (int i = 0; i < localActivitiesList.size(); i++) {
                Activities mActivity = localActivitiesList.get(i);
                check = dbController.delete(mActivity.getId());
            }
        } catch (Exception e) {
        }
        return check;
    }
}
