package com.sfsu.investickation.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import com.sfsu.image.AlbumStorageDirFactory;
import com.sfsu.image.BaseAlbumDirFactory;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.AppUtils;
import com.sfsu.utils.PermissionUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddObservation extends Fragment implements LocationController.ILocationCallBacks,
        TextValidator.ITextValidate, View.OnClickListener {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private static final String JPEG_FILE_PREFIX = "TICK_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int GALLERY_CAMERA_PERMISSION = 24;
    private final String TAG = "~!@#$AddObservation";
    // ImageView
    @Bind(R.id.imageView_addObs_tickImage)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_addObs_postObservation)
    Button btn_PostObservation;
    // EditTexts
    @Bind(R.id.editText_addObs_numOfTicks)
    EditText et_numOfTicks;
    @Bind(R.id.editText_addObs_tickName)
    EditText et_tickName;
    @Bind(R.id.editText_addObs_description)
    EditText et_description;
    private boolean FLAG_PERMISSION_GRANTED;
    // Others
    private String selectedImagePath, geoLocation;
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
    private boolean isTotalTicksNumber, isTickNameValid;
    private TextValidator mTextValidator;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private PermissionUtils mPermissionUtils;
    private ProgressDialog mProgressDialog;


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
        mPermissionUtils = new PermissionUtils(mContext);
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
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();


        if (args != null && args.containsKey(UserActivityMasterActivity.KEY_ACTIVITY_ID)) {
            activityId = args.getString(UserActivityMasterActivity.KEY_ACTIVITY_ID);
        } else {
            activityId = "";
        }

        // get user_id.
        userId = mAuthPreferences.getUser_id();

        et_tickName.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickName));
        //et_description.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_numOfTicks));

        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) v.findViewById(R.id.fab_addObs_addTickImage);

        addTickImage.setOnClickListener(this);

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
            case R.id.fab_addObs_addTickImage:
                handleImageDialog();
                break;
        }
    }

    /**
     * Handles the custom image dialog to either prompt user for permission or to display custom dialog for choosing image
     */
    private void handleImageDialog() {
        boolean isCameraApproved = mPermissionUtils.isCameraPermissionApproved();
        boolean isWriteStorageApproved = mPermissionUtils.isWritePermissionApproved();
        boolean isReadStorageApproved = mPermissionUtils.isReadPermissionApproved();

        if (isCameraApproved && isWriteStorageApproved && isReadStorageApproved) {
            startDialogForChoosingImage();
        } else {
            askForPermission();
        }
    }

    /**
     * Helper method to post Observation on server or store in local storage
     */
    private void postObservation() {
        try {
            if (isTickNameValid && isTotalTicksNumber) {
                String tickName = et_tickName.getText().toString();
                //String tickSpecies = et_tickSpecies.getText().toString();
                int numOfTicks = Integer.parseInt(et_numOfTicks.getText().toString());
                String description = et_description.getText().toString() == null ? "" : et_description.getText().toString();

                // finally when all values are collected, create a new Observation object.
                newObservationObj = new Observation(tickName, numOfTicks, description, AppUtils.getCurrentTimeStamp(),
                        activityId, userId);

                // set all the location values
                geoLocation = geoLocation != null ? geoLocation : "";
                latitude = latitude != 0 ? latitude : 0.0;
                longitude = longitude != 0 ? longitude : 0.0;

                newObservationObj.setGeoLocation(geoLocation);
                newObservationObj.setLatitude(latitude);
                newObservationObj.setLongitude(longitude);

                // depending on network connection, save the Observation on storage or server
                if (AppUtils.isConnectedOnline(mContext)) {
                    BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(newObservationObj, ApiRequestHandler.ADD));
                    displayProgressDialog("Making Observation...");
                } else {
                    // create Unique ID for the Running activity of length 32.
                    String observationUUID = RandomStringUtils.randomAlphanumeric(Observation.ID_LENGTH);

                    // set the remaining params.
                    newObservationObj.setId(observationUUID);

                    newObservationObj.setImageUrl(selectedImagePath);

                    long resultCode = dbController.save(newObservationObj);

                    if (resultCode != -1) {
                        // if saved to DB successfully, open ObservationsList
                        mInterface.postObservationData(ObservationMasterActivity.FLAG_ACTIVITY_RUNNING);
                    } else {
                        Toast.makeText(mContext, "Fail to store Observation", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
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
     * Checks for runtime permission
     */
    private void askForPermission() {
        int hasCameraPermission = 0;
        int hasReadPermission = 0;
        int hasWritePermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasCameraPermission = mContext.checkSelfPermission(Manifest.permission.CAMERA);
            hasReadPermission = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            hasWritePermission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<>();
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), GALLERY_CAMERA_PERMISSION);
            }
        } else {
            Log.i(TAG, "askForPermission: not working");
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: reached");
        switch (requestCode) {
            case GALLERY_CAMERA_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissionUtils.setPermission(PermissionUtils.CAMERA);
                        mPermissionUtils.setPermission(PermissionUtils.READ);
                        mPermissionUtils.setPermission(PermissionUtils.WRITE);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
                startDialogForChoosingImage();
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    /**
     * This method is used to popup a dialog box for allowing user to select
     * the Tick image from Camera or Gallery.
     */
    private void startDialogForChoosingImage() {
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
                            f = setUpPhotoFile();
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
            galleryAddPic();
            selectedImagePath = null;
        } else {
            Log.i(TAG, "handleCameraPicture: selectedPath null");
        }
    }


    /**
     * Returns the Photo album for this application
     */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    /**
     * Returns the album directory for the application in external storage
     *
     * @return
     */
    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null;
                    }
                }
            }
        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }


    /**
     * Method to create the temp file by specifying the name using prefix, suffix and timestamp
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss", Locale.US).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp;
        File albumF = getAlbumDir();
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
    }

    /**
     * Intermediate method to create File for the captured Image
     *
     * @return
     * @throws IOException
     */
    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        selectedImagePath = f.getAbsolutePath();
        return f;
    }

    /**
     * Previews the captured image into ImageView
     */
    private void setPic() {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = imageView_tickAddObservation.getWidth();
        int targetH = imageView_tickAddObservation.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        imageView_tickAddObservation.setImageBitmap(bitmap);

        Log.i("~!@#$", selectedImagePath);
    }

    /**
     * Finally saves the image to the gallery in external storage
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(selectedImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
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
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public void setCurrentLocation(Location mLocation) {
    }

    @Override
    public void setLatLng(LatLng mLatLng) {
        this.latitude = mLatLng.latitude;
        this.longitude = mLatLng.longitude;
    }

    @Override
    public void setLocationArea(String locationArea) {
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
//            case R.id.editText_addObs_tickSpecies:
//                isTickSpeciesValid = ValidationUtil.validateString(mEditText, text);
//                break;
            case R.id.editText_addObs_numOfTicks:
                isTotalTicksNumber = ValidationUtil.validateNumber(mEditText, text);
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
        //Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
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
        mInterface.postObservationData(ObservationMasterActivity.FLAG_ACTIVITY_RUNNING);
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
        Log.i(TAG, onLoadingError.getErrorMessage());
    }


    /**
     * Callback interface to handle the onClick Listeners in {@link AddObservation} Fragment.
     */
    public static interface IAddObservationCallBack {
        /**
         * Callback method to open {@link ObservationList} after {@link Observation} is posted on server or SQLite DB.
         *
         * @param newObservation
         */
        void postObservationData(long statusFlag);
    }

}
