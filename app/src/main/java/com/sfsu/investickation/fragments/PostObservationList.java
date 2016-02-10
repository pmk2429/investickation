package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.ImageData;
import com.sfsu.entities.Observation;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
     * Subscribes to the event of success in posting Activities to the server. Once the list is uploaded, upload Images for
     * each tick
     *
     * @param onMassUploadListLoaded
     */
    @Subscribe
    public void onObservationsListStoreSuccess(ObservationEvent.OnListLoaded onListLoaded) {
        if (dbController == null)
            dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());

        for (int i = 0; i < localObservationList.size(); i++) {
            uploadImage(localObservationList.get(i));
        }
    }

    private void uploadImage(Observation mObservation) {
        File imageFile = new File(mObservation.getImageUrl());
        // create RequestBody to send the image to server
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        // create dynamic file name for the image
        String fileParam = "file\"; filename=\"" + imageFile.getName();
        // finally create ImageData object that contains RequestBody and Image name
        ImageData mImageData = new ImageData(requestBody, fileParam);
        // once done, post the File to the Bus for posting it on server.
        BusProvider.bus().post(new FileUploadEvent.OnLoadingInitialized(mImageData, mObservation.getId(),
                ApiRequestHandler.UPLOAD_TICK_IMAGE));
    }

    @Subscribe
    public void onObservationsListStoreFailure(ObservationEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, onLoadingError.getErrorMessage());
    }


    /**
     * Subscribes to event of successful Observation Image upload to the server. The <tt>OnLoaded</tt> will return the {@link
     * Observation} as a response after successful network post request.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationImageUploadSuccess(FileUploadEvent.OnLoaded onLoaded) {
        // finally delete the Observation from local storage
        if (deleteUploadedObservations()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
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
