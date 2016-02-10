package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.Observation;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Fragment to post List of Activities when the User clicks on the upload button in {@link ActivityList} fragment
 */
public class PostObservationList extends Fragment {

    private static final String TAG = "~!@#$PostObsList";
    private Context mContext;
    private List<Observation> localObservationList;
    private DatabaseDataController dbController;

    public PostObservationList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);

        localObservationList = (List<Observation>) dbController.getAll();

        // make a call and upload Activities
        if (localObservationList != null) {
            BusProvider.bus().post(new ObservationEvent.OnListLoadingInitialized(localObservationList, ApiRequestHandler.ADD));
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
    public void onObservationsListStoreSuccess(ObservationEvent.OnListLoaded onListLoaded) {
        if (dbController == null)
            dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());

        if (deleteUploadedObservations()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void onObservationsListStoreFailure(ObservationEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, onLoadingError.getErrorMessage());
    }

    /**
     * Once all the Activities are uploaded, delete them from database
     */
    private boolean deleteUploadedObservations() {
        boolean check = false;
        try {
            for (int i = 0; i < localObservationList.size(); i++) {
                Observation mObservation = localObservationList.get(i);
                check = dbController.delete(mObservation.getId());
            }
        } catch (Exception e) {
        }
        return check;
    }

}
