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
import android.support.v4.content.ContextCompat;
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

import org.apache.commons.lang3.SerializationUtils;

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
    private final String TAG = "~!@#EditObs";
    // ImageView
    @Bind(R.id.imageview_tick_image)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_update_observation)
    Button btn_UpdateObservation;
    @Bind(R.id.button_choose_from_guide)
    Button btn_chooseFromGuide;
    // EditTexts
    @Bind(R.id.edittext_num_of_ticks)
    EditText et_numOfTicks;
    @Bind(R.id.edittext_tick_name)
    EditText et_tickName;
    @Bind(R.id.edittext_description)
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
        if (getArguments() != null) {
            mObservation = getArguments().getParcelable(KEY_EDIT_OBSERVATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_observation, container, false);
        ButterKnife.bind(this, rootView);

        et_tickName.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_tickName));
        //et_description.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_numOfTicks));
        // initialize the Floating button.

        final FloatingActionButton fab_addTickImage = (FloatingActionButton) rootView.findViewById(R.id.fab_add_tick_image);
        fab_addTickImage.setOnClickListener(this);

        btn_chooseFromGuide.setText(getString(R.string.text_edit_referTickGuide));

        // populate all the views with the Observation object restored from Bundle
        if (mObservation != null) {
            Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_tickAddObservation);
            et_tickName.setText(mObservation.getTickName());
            et_description.setText(mObservation.getDescription());
            et_numOfTicks.setText(String.valueOf(mObservation.getNum_of_ticks()));
        }

        // initially the color of the button will be Greyish to disable user updating observation
        //setStateOfUpdateButton(false);
        btn_UpdateObservation.setOnClickListener(this);

        return rootView;
    }

    /**
     * Sets the state of Update button depending on initial load, user edits and change of data
     *
     * @param isEditable
     */
    private void setStateOfUpdateButton(boolean isChanged) {
        if (isChanged) {
            btn_UpdateObservation.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            btn_UpdateObservation.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightText));
        }
        btn_UpdateObservation.setEnabled(isChanged);
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
        getActivity().setTitle(R.string.title_fragment_observation_edit);
        mProgressDialog = new ProgressDialog(mContext);
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
            case R.id.button_update_observation:
                updateObservation();
                break;
            case R.id.fab_add_tick_image:
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
            case R.id.edittext_tick_name:
                isTickNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.edittext_num_of_ticks:
                isTotalTicksNumber = ValidationUtil.validateNumber(mEditText, text);
                break;
        }
    }

    /**
     * In order to edit the Observation, we are allowing user to change only <tt>image</tt>, <tt>name</tt>, <tt>description</tt>,
     * <tt>num_of_ticks</tt> and <tt>species</tt>.
     * <p>
     * Update Observation checks for the following cases:
     * <ul>
     * <li>Observation state is changed</li>
     * <li>Observation content is validated</li>
     * <li>Observation is Stored on Cloud</li>
     * <ul><li>Update Observation only on Cloud ONLY else display Toast</li></ul>
     * <li>Observation is stored locally</li>
     * <ul><li>Update the Observation stored locally ONLY</li></ul>
     * </ul>
     * </p>
     * This ensure that no matter what the state of Observation is, update that Observation at the same place where it is stored.
     */
    private void updateObservation() {
        Observation originalObservation = SerializationUtils.clone(mObservation);
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

                // need to check the selectedImagePath for the image
                if (selectedImagePath != null) {
                    if (mObservation.isOnCloud())
                        mObservation.setImageUrl(null);
                    else
                        mObservation.setImageUrl(selectedImagePath);
                }

                // compare the originalObservation with this Observation and make
                if (mObservation.equals(originalObservation)) {
                    Toast.makeText(mContext, "Nothing to update", Toast.LENGTH_SHORT).show();
                } else {
                    // Observation is on Cloud
                    if (mObservation.isOnCloud()) {
                        // here the Observation is on Cloud to update it if network is available
                        if (AppUtils.isConnectedOnline(mContext)) {
                            BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(mObservation, mObservation.getId(),
                                    ApiRequestHandler.UPDATE));
                            displayProgressDialog(mContext.getString(R.string.progressDialog_updating_observation));
                        } else {
                            // display Toast that the Observation cant be updated now and wait until getting back in network
                            Toast.makeText(mContext, "No network available, cannot update the Observation now", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                    // observation is stored locally so update the Observation locally ONLY
                    else {
                        // get the resultCode after updating the Observation
                        boolean resultCode = dbController.update(mObservation.getId(), mObservation);

                        if (resultCode) {
                            // if saved to DB successfully, open ObservationDetail
                            mInterface.displayObservationDetails(mObservation);
                        } else {
                            Toast.makeText(mContext, "Fail to update Observation", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }


        } catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Subscribes to the event of successful observation update. Once the Observation is updated successfully, the image is
     * posted on the server on the updated Observation.
     * Only if the image is changed, we are making another synchronous call to change the image
     *
     * @param onLoaded
     */
    @Subscribe
    public void onObservationUpdateSuccess(ObservationEvent.OnLoaded onLoaded) {
        dismissProgressDialog();
        Observation updatedObservation = onLoaded.getResponse();
        if (selectedImagePath != null) {
            // create File from the path
            File imageFile = new File(selectedImagePath);
            // create RequestBody to send the image to server
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            // create dynamic file name for the image
            String fileParam = "file\"; filename=\"" + imageFile.getName();
            // finally create ImageData object that contains RequestBody and Image name
            ImageData mImageData = new ImageData(requestBody, fileParam);
            // once done, post the File to the Bus for posting it on server.
            BusProvider.bus().post(new FileUploadEvent.OnLoadingInitialized(mImageData, updatedObservation.getId(),
                    ApiRequestHandler.UPLOAD_TICK_IMAGE));
            // show progress dialog
            displayProgressDialog(mContext.getString(R.string.progressDialog_uploading_image));
        } else {
            mInterface.displayObservationDetails(updatedObservation);
        }
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
        mInterface.displayObservationDetails(onLoaded.getResponse());
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Callback interface to handle the onClicks in the {@link EditObservationFragment}
     */
    public interface IEditObservationCallbacks {

        /**
         * @param mObservation
         */
        void displayObservationDetails(Observation mObservation);
    }

}
