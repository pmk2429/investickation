package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.ImageController;
import com.sfsu.entities.ImageData;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.sfsu.utils.PermissionUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Edit the {@link com.sfsu.entities.Observation} posted by User.
 * <p>
 * <b>Reusing the layout<b> and as a result, the View reference ids are same as that of {@link AddObservationFragment}.
 * The Observation can be stored locally or on cloud and depending on its storage
 */

// TODO: this Fragment contains the code dupe from AddObservationFragment. Change it before publishing

public class EditObservationFragment extends Fragment implements View.OnClickListener, TextValidator.ITextValidate {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private static final String KEY_EDIT_OBSERVATION = "edit_observation";
    private static final int GALLERY_CAMERA_PERMISSION = 24;
    private final String LOGTAG = "~!@#EditObsFrag:";
    private final String TAG = "~!@#$AddObservation";
    // ImageView
    @Bind(R.id.imageView_addObs_tickImage)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_editObs_updateObservation)
    Button btn_UpdateObservation;
    @Bind(R.id.button_addObs_chooseFromGuide)
    Button btn_chooseFromGuide;
    // EditTexts
    @Bind(R.id.editText_addObs_numOfTicks)
    EditText et_numOfTicks;
    @Bind(R.id.editText_addObs_tickName)
    EditText et_tickName;
    @Bind(R.id.editText_addObs_description)
    EditText et_description;
    private IEditObservationCallbacks mInterface;
    private Observation mObservation;
    private Context mContext;
    private DatabaseDataController dbController;
    private PermissionUtils mPermissionUtils;
    private ProgressDialog mProgressDialog;
    /* selectedImagePath for Camera will be retrieved from the file
    and from data stream for image chosen from Gallery
     */
    private String selectedImagePath;
    private boolean isTotalTicksNumber, isTickNameValid;
    private ImageController mImageController;

    public EditObservationFragment() {
        // Required empty public constructor
    }

    public static EditObservationFragment newInstance(Observation mObservation) {
        EditObservationFragment fragment = new EditObservationFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_EDIT_OBSERVATION, mObservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_observation_edit);
        if (getArguments() != null) {
            mObservation = getArguments().getParcelable(KEY_EDIT_OBSERVATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_observation, container, false);
        ButterKnife.bind(this, rootView);

        et_tickName.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_tickName));
        //et_description.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_numOfTicks));
        // initialize the Floating button.
        final FloatingActionButton fab_addTickImage = (FloatingActionButton) rootView.findViewById(R.id.fab_addObs_addTickImage);

        btn_chooseFromGuide.setText(getString(R.string.text_edit_referTickGuide));

        // populate all the views with the Observation object restored from Bundle
        if (mObservation != null) {
            Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_tickAddObservation);
            et_tickName.setText(mObservation.getTickName());
            et_description.setText(mObservation.getDescription());
            et_numOfTicks.setText(mObservation.getNum_of_ticks());
        }

        fab_addTickImage.setOnClickListener(this);
        btn_UpdateObservation.setOnClickListener(this);

        return rootView;
    }

    /**
     * In order to edit the Observation, we are allowing user to change only <tt>image</tt>, <tt>name</tt>, <tt>description</tt>,
     * <tt>num_of_ticks</tt> and <tt>species</tt>.
     * <p>
     * So a check is done to know if the original Observation data and the new edited Observation data are same or not.
     * If they differ, then only update is called and data is updated.
     * </p>
     */
    private void updateObservation() {

        try {
            if (isTickNameValid && isTotalTicksNumber) {
                if (!mObservation.getTickName().equals(et_tickName.getText().toString())) {
                    mObservation.setTickName(et_tickName.getText().toString());
                }
                if (mObservation.getNum_of_ticks() != Integer.parseInt(et_numOfTicks.getText().toString())) {
                    mObservation.setNum_of_ticks(Integer.parseInt(et_numOfTicks.getText().toString()));
                }
                if (!mObservation.getDescription().equals(et_description.getText().toString())) {
                    mObservation.setDescription(et_description.getText().toString());
                }


                // depending on network connection, save the Observation in local storage or server
                if (AppUtils.isConnectedOnline(mContext)) {
                    BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(mObservation, ApiRequestHandler.ADD));
                    displayProgressDialog("Posting Observation...");
                } else {
                    // create Unique ID for the Running activity of length 32.
                    String observationUUID = RandomStringUtils.randomAlphanumeric(Observation.ID_LENGTH);
                    // set the remaining params.
                    mObservation.setImageUrl(selectedImagePath);
                    long resultCode = dbController.save(mObservation);

                    if (resultCode != -1) {
                        // if saved to DB successfully, open ObservationsList
                        mInterface.updateObservation();
                    } else {
                        Toast.makeText(mContext, "Fail to update Observation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IEditObservationCallbacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IEditObservationCallbacks");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_editObs_updateObservation:
                updateObservation();
                break;
            case R.id.fab_addObs_addTickImage:
                openDialogForChoosingImage();
                break;
        }

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

    /**
     * This method is used to popup a dialog box for allowing user to select
     * the Tick image from Camera or Gallery.
     */
    private void openDialogForChoosingImage() {
        AlertDialog.Builder chooseImageAlertDialog = new AlertDialog.Builder(mContext);
        chooseImageAlertDialog.setTitle(R.string.alertDialog_tickImage_title);
        chooseImageAlertDialog.setMessage(R.string.alertDialog_tickImage_message);

        // Choose from Gallery.
        chooseImageAlertDialog.setPositiveButton(R.string.alertDialog_tickImage_gallery,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;
                        // Select the Intent depending on Camera
                        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // finally call intent for Result.
                        pictureActionIntent.setType("image/*");
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                    }
                });

        // Choose from Camera.
        chooseImageAlertDialog.setNegativeButton(R.string.alertDialog_tickImage_camera,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = null;
                        try {
                            f = mImageController.setUpPhotoFile();
                            // IMP : here the image path is retrieved from File created using ImageController
                            selectedImagePath = f.getAbsolutePath();
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        } catch (IOException e) {
                            e.printStackTrace();
                            f = null;
                            selectedImagePath = null;
                        }
                        startActivityForResult(takePictureIntent, CAMERA_PICTURE);
                    }
                });
        chooseImageAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if the Image was selected from the Camera
        if (requestCode == CAMERA_PICTURE) {
            handleCameraPicture();
        }
        // if the image is chosen from Gallery
        else if (requestCode == GALLERY_PICTURE) {
            try {
                if (data != null) {
                    InputStream inputStream = mContext.getContentResolver().openInputStream(data.getData());
                    Drawable testExternalStorage = BitmapDrawable.createFromStream(inputStream, "tick");
                    imageView_tickAddObservation.setImageDrawable(testExternalStorage);
                    // set the image path from InputStream
                    selectedImagePath = data.getData().getPath();
                }
            } catch (Exception e) {
                Log.i(TAG, "->G :" + e.getMessage());
            }
        }
    }

    /**
     * Handles the captured camera image
     */
    private void handleCameraPicture() {
        if (selectedImagePath != null) {
            setPic();
            mImageController.galleryAddPic(selectedImagePath);
            selectedImagePath = null;
        } else {
            Log.i(TAG, "handleCameraPicture: selectedPath null");
        }
    }


    /**
     * Previews the captured image into ImageView
     */
    private void setPic() {
        Bitmap bitmap = mImageController.getBitmapForImageView(imageView_tickAddObservation, selectedImagePath);
        /* Associate the Bitmap to the ImageView */
        imageView_tickAddObservation.setImageBitmap(bitmap);

    }


    @Override
    public void validate(View mView, String text) {
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.editText_addObs_tickName:
                isTickNameValid = ValidationUtil.validateString(mEditText, text);
                break;
//            case R.id.editText_addObs_tickSpecies:
//                isTickSpeciesValid = ValidationUtil.validateString(mEditText, text);
//                break;
            case R.id.editText_addObs_numOfTicks:
                isTotalTicksNumber = ValidationUtil.validateNumber(mEditText, text);
                break;
        }
    }


    /**
     * Subscribes to the event of successful observation creation. Once the Observation is created successfully, the Account
     * captured image of tick inside the Observation is posted on server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationDataPostSuccess(ObservationEvent.OnLoaded onLoaded) {
        dismissProgressDialog();
        Observation observationResponse = onLoaded.getResponse();
        // create File from the path
        File imageFile = new File(selectedImagePath);
        // create RequestBody to send the image to server
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        // create dynamic file name for the image
        String fileParam = "file\"; filename=\"" + imageFile.getName();
        // finally create ImageData object that contains RequestBody and Image name
        ImageData mImageData = new ImageData(requestBody, fileParam);
        // once done, post the File to the Bus for posting it on server.
        BusProvider.bus().post(new FileUploadEvent.OnLoadingInitialized(mImageData, observationResponse.getId(),
                ApiRequestHandler.UPLOAD_TICK_IMAGE));
        // show progress dialog
        displayProgressDialog("Uploading Image...");
    }

    /**
     * Subscribes to the event of Failure in posting the Observation data on the server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationDataPostFailure(ObservationEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
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
        dismissProgressDialog();
        // pass the Observation response object to the ObservationActivity.
        mInterface.updateObservation();
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    public interface IEditObservationCallbacks {

        void updateObservation();
    }

}
