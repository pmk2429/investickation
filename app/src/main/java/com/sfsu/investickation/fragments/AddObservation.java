package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.ImageController;
import com.sfsu.controllers.LocationController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.ImageData;
import com.sfsu.entities.Observation;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.ValidationUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddObservation extends Fragment implements LocationController.ILocationCallBacks,
        TextValidator.ITextValidate, View.OnClickListener {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private final String TAG = "~!@#$AddObservation";
    // ImageView
    @Bind(R.id.imageView_addObs_tickImage)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_addObs_postObservation)
    Button btn_PostObservation;
    // EditTexts
    @Bind(R.id.editText_addObs_tickSpecies)
    EditText et_tickSpecies;
    @Bind(R.id.editText_addObs_numOfTicks)
    EditText et_numOfTicks;
    @Bind(R.id.editText_addObs_tickName)
    EditText et_tickName;
    @Bind(R.id.editText_addObs_description)
    EditText et_description;
    // Others
    private Bitmap bitmap;
    private String selectedImagePath, picturePath, geoLocation;
    private Observation newObservationObj;
    private IAddObservationCallBack mInterface;
    private Context mContext;
    private Intent locationIntent;

    private String activityId, requestToken, userId, image_name;
    private Bundle args;
    private LocationController mLocationController;
    private EntityLocation entityLocation;
    private double latitude, longitude;
    //    private DatabaseDataController dbController;
    private ImageController mImageController;
    private List<Tick> tickList;
    private boolean isTotalTicksNumber, isTickNameValid, isTickSpeciesValid;
    private TextValidator mTextValidator;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;


    public AddObservation() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link AddObservation} Fragment built using key and {@link Observation}.
     *
     * @param key
     * @param value
     * @return
     */
    public static AddObservation newInstance(String key, String value) {
        AddObservation addObservation = new AddObservation();
        Bundle args = new Bundle();
        args.putString(key, value);
        addObservation.setArguments(args);
        return addObservation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_observation, container, false);

        ButterKnife.bind(this, v);

        if (getArguments() != null) {
            args = getArguments();
        }
        dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());
        mAuthPreferences = new AuthPreferences(mContext);

        if (args != null && args.containsKey(UserActivityMasterActivity.KEY_ACTIVITY_ID)) {
            activityId = args.getString(UserActivityMasterActivity.KEY_ACTIVITY_ID);
        } else {
            activityId = "";
        }

        // get user_id.
        userId = mAuthPreferences.getUser_id();

        et_tickName.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickName));
        et_tickSpecies.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_numOfTicks));

        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) v.findViewById(R.id.fab_addObs_addTickImage);
        addTickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialogForChoosingImage();
            }
        });

        btn_PostObservation.setOnClickListener(this);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize the LocationController
        mLocationController = new LocationController(mContext, this);
        mLocationController.connectGoogleApi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_addObs_postObservation:
                postObservation();
                break;
        }
    }

    /**
     * Helper method to post Observation on server or store in local storage
     */
    private void postObservation() {
        try {
            if (isTickNameValid && isTickSpeciesValid && isTotalTicksNumber) {
                String tickName = et_tickName.getText().toString();
                String tickSpecies = et_tickSpecies.getText().toString();
                int numOfTicks = Integer.parseInt(et_numOfTicks.getText().toString());
                String description = et_description.getText().toString() == null ? "" : et_description.getText().toString();

                // finally when all values are collected, create a new Observation object.
                newObservationObj = new Observation(tickName, tickSpecies, numOfTicks, description, AppUtils.getCurrentTimeStamp(),
                        activityId, userId);

                // depending on network connection, save the Observation on storage or server
                if (AppUtils.isConnectedOnline(mContext)) {
                    BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(newObservationObj, ApiRequestHandler.ADD));
                } else {
                    // create Unique ID for the Running activity of length 32.
                    String observationUUID = RandomStringUtils.randomAlphanumeric(Observation.ID_LENGTH);

                    // set the remaining params.
                    newObservationObj.setId(observationUUID);

                    newObservationObj.setImageUrl(selectedImagePath);

                    // set Location params separately.
                    newObservationObj.setGeoLocation("");
                    newObservationObj.setLatitude(latitude);
                    newObservationObj.setLongitude(longitude);

                    Log.i(TAG, newObservationObj.toString());

                    long resultCode = dbController.save(newObservationObj);

                    if (resultCode != -1) {
                        // if saved to DB successfully, open ObservationsList
                        mInterface.postObservationData(newObservationObj);
                    } else {
                        Toast.makeText(mContext, "Fail to store Observation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "error: " + e.getMessage());
        }

    }


    /**
     * Helper method to setup and display Choose Tick Dialog having Custom Layout
     *
     * @param inflater
     */

//    private void openChooseTickDialog(LayoutInflater inflater) {
//        final AlertDialog.Builder alertDialogChooseTick = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
//        View customView = inflater.inflate(R.layout.alertdialog_choosetick_list, null);
//
//        // identify the ListView
//        ListView listViewTicks = (ListView) customView.findViewById(R.id.listView_chooseTick);
//
//        tickList = TickHelper.getAllTicks();
//
//        // Build an ArrayAdapter
//        final TickDialogAdapter dialogAdapter = new TickDialogAdapter(mContext, R.layout.alertdialog_choosetick_item, tickList);
//        listViewTicks.setAdapter(dialogAdapter);
//
//        // make the divider invisible
//        listViewTicks.setDivider(null);
//        listViewTicks.setDividerHeight(0);
//
//        dialogAdapter.setNotifyOnChange(true);
//        dialogAdapter.notifyDataSetChanged();
//
//        // set the characteristics of AlertDialog.
//        alertDialogChooseTick.setTitle(getActivity().getResources().getString(R.string.alert_chooseTick));
//        alertDialogChooseTick.setView(customView);
//
//        // create a dialog to get the reference to when dismissing the Dialog.
//        final AlertDialog dialog = alertDialogChooseTick.create();
//        dialog.show();
//
//        // when the user simply presses the news item, then the DetailedNewsActivity will get started.
//        listViewTicks.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        // set Texts in EditTexts.
//                        setTickData(tickList.get(position));
//                        dialog.dismiss();
//                    }
//                });
//    }


    /**
     * Helper method to populate the Values of EditTexts in {@link AddObservation} Fragment
     *
     * @param tickData
     */
    public void setTickData(Tick tickData) {
        et_tickName.setText(tickData.getTickName());
        et_tickSpecies.setText(tickData.getSpecies());
    }

    /**
     * This method is used to popup a dialog box for allowing user to select
     * the Tick image from Camera or Gallery.
     */
    private void startDialogForChoosingImage() {
        AlertDialog.Builder chooseImageAlertDialog = new AlertDialog.Builder(getActivity());
        chooseImageAlertDialog.setTitle("Add Tick picture");
        chooseImageAlertDialog.setMessage("How do you want to select Tick picture?");

        // Choose from Gallery.
        chooseImageAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;
                        // Select the Intent depending on Camera
                        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // finally call intent for Result.
                        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
                    }
                });

        // Choose from Camera.
        chooseImageAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File pictureFile = new File(android.os.Environment.getExternalStorageDirectory(), "demo.jpg");
                        // put the file name as an extra in Intent.
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
                        // finally call intent for Result.
                        startActivityForResult(intent, CAMERA_PICTURE);
                    }
                });
        chooseImageAlertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        selectedImagePath = null;

        // if the Image was selected from the Camera
        if (resultCode == getActivity().RESULT_OK && requestCode == CAMERA_PICTURE) {
            if (null != data) {
                // get the Path to the File created using Camera
                File imageFile = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : imageFile.listFiles()) {
                    if (temp.getName().equals("demo.jpg")) {
                        imageFile = temp;
                        break;
                    }
                }

                if (!imageFile.exists()) {
                    Toast.makeText(getActivity(), "Error capturing Tick image", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    int imageHeight = imageView_tickAddObservation.getHeight();
                    int imageWidth = imageView_tickAddObservation.getWidth();
                    // set the selectedImagePath
                    selectedImagePath = imageFile.getAbsolutePath();

                    bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    bitmap = getScaledBitmap(imageFile.getAbsolutePath(), 250, 250);
                    int rotate = 0;
                    try {
                        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotate = 270;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotate = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotate = 90;
                                break;
                        }
                        // use to transform coordinates according to orientation.
                        Matrix matrix = new Matrix();
                        // rotate based on degrees
                        matrix.postRotate(rotate);
                        // create a new bitmap from the matrix
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }

                    // Picasso.with(mContext).load(imageFile).centerCrop().into(imageView_tickAddObservation);

                    imageView_tickAddObservation.setImageBitmap(bitmap);

                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }

        }
        // if the image is chosen from Gallery
        else if (resultCode == getActivity().RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {
                // get the data from the Intent
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                // get the cursor to the selectedImage
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                // get the index
                int columnIndex = c.getColumnIndex(filePath[0]);
                // get the path to the selectedImage using cursor's methods
                selectedImagePath = c.getString(columnIndex);
                c.close();

                // set the bitmap in ImageView
                imageView_tickAddObservation.setImageBitmap(getScaledBitmap(selectedImagePath, 600, 600));
            } else {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Helper method to get Scaled Bitmap.
     *
     * @param picturePath
     * @param width
     * @param height
     * @return
     */
    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    /**
     * Method to calculate the Bitmap in Sample Size.
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IAddObservationCallBack) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IAddObservationCallBack");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_observation_add);
        BusProvider.bus().register(this);
    }

    @Override
    public void setCurrentLocation(Location mLocation) {
    }

    @Override
    public void setLatLng(LatLng mLatLng) {
        Log.i(TAG, "lat lng changed");
        this.latitude = mLatLng.latitude;
        this.longitude = mLatLng.longitude;
    }

    @Override
    public void setLocationArea(String locationArea) {
        Log.i(TAG, "location area");
        if (locationArea != null || !locationArea.equals("")) {
            this.geoLocation = locationArea;
        } else {
            this.geoLocation = "";
        }
    }

    @Override
    public void validate(View mView, String text) {
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.editText_addObs_tickName:
                isTickNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_addObs_tickSpecies:
                isTickSpeciesValid = ValidationUtil.validateString(mEditText, text);
                break;
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
        Observation observationResponse = onLoaded.getResponse();

        Log.i(TAG, "1) observation posted successfully");

        File file = new File(selectedImagePath);

        image_name = file.getName();

        Log.i(TAG, image_name);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        ImageData mImageData = new ImageData(requestBody, image_name, "");

        Log.i(TAG, "2) " + observationResponse.getId());

        // once done, post the File to the Bus for posting it on server.
        BusProvider.bus().post(new FileUploadEvent.OnLoadingInitialized(mImageData, observationResponse.getId(),
                ApiRequestHandler.UPLOAD_TICK_IMAGE));
    }

    /**
     * Subscribes to the event of Failure in posting the Observation data on the server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onObservationDataPostFailure(ObservationEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, "E: observation failure");
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
        Log.i(TAG, "5) image uploaded successfully");
        // pass the Observation response object to the ObservationActivity.
        mInterface.postObservationData(onLoaded.getResponse());
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        Log.i(TAG, "5a) image upload failure");
//        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
        Log.i(TAG, onLoadingError.getErrorMessage());
    }


    /**
     * Callback interface to handle the onClick Listeners in {@link AddObservation} Fragment.
     */
    public static interface IAddObservationCallBack {
        /**
         * Callback method to post the new {@link Observation} on the SQLite DB after Response is obtained from server.
         *
         * @param newObservation
         */
        void postObservationData(Observation newObservation);
    }

}
