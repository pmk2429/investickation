package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.widget.Toast;

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

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Fragment to post Observation stored offline on remote server. Runs asynchronously when user clicks on the upload button in
 * {@link ObservationDetailFragment}. Uses RxJava to post the Observation data and image in synchronous fashion
 */
public class PostObservationFragment extends Fragment {

    private static final String TAG = "~!@#$PostObs";
    private Context mContext;
    private Observation mOfflineObservation;
    private DatabaseDataController dbController;

    public PostObservationFragment() {
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

        mOfflineObservation = (Observation) dbController.getAll();

        // make a call and upload Activities
        if (mOfflineObservation != null) {
            BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(mOfflineObservation, ApiRequestHandler.ADD));
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
     * Subscribes to the event of success in posting locally stored Observation to the server. Once the Observation data is
     * uploaded, a synchronous call is made to server to store the image for the same Observation by updating the Observation
     *
     * @param onMassUploadListLoaded
     */
    @Subscribe
    public void onPostOfflineObservationSuccess(ObservationEvent.OnLoaded onLoaded) {

        File imageFile = new File(onLoaded.getResponse().getImageUrl());
        // create RequestBody to send the image to server
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        // create dynamic file name for the image
        String fileParam = "file\"; filename=\"" + imageFile.getName();
        // finally create ImageData object that contains RequestBody and Image name
        ImageData mImageData = new ImageData(requestBody, fileParam);
        // once done, post the File to the Bus for posting it on server.
        BusProvider.bus().post(new FileUploadEvent.OnLoadingInitialized(mImageData, onLoaded.getResponse().getId(),
                ApiRequestHandler.UPLOAD_TICK_IMAGE));
    }

    /**
     * Subscribes to the event of failure in posting Observation on the server
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onPostOfflineObservationFailure(ObservationEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Subscribes to event of successful Observation Image upload to the server. The <tt>OnLoaded</tt> will return the {@link
     * Observation} as a response after successful network post request.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationImageUploadSuccess(FileUploadEvent.OnLoaded onLoaded) {
        if (dbController == null)
            dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());

        if (deleteLocallyStoredObservation(mOfflineObservation)) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }


    /**
     * Subscribes to the event of failure in updating the Observation and uploading Image on the server
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Once all the Activities are uploaded, delete them from database
     */
    private boolean deleteLocallyStoredObservation(Observation mOfflineObservation) {
        boolean check = false;
        try {
            check = dbController.delete(mOfflineObservation.getId());
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "deleteLocallyStoredObservation: " + e.getLocalizedMessage());
        }
        return check;
    }

}
