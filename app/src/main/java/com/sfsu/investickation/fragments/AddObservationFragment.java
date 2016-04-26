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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;
import com.sfsu.adapters.TickDialogAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.ImageController;
import com.sfsu.controllers.LocationController;
import com.sfsu.db.ObservationsDao;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.ImageData;
import com.sfsu.entities.Observation;
import com.sfsu.entities.Tick;
import com.sfsu.helper.TickHelper;
import com.sfsu.image.AlbumStorageDirFactory;
import com.sfsu.image.BaseAlbumDirFactory;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.FileUploadEvent;
import com.sfsu.network.events.ObservationEvent;
import com.sfsu.network.events.TickEvent;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Most important Fragment in the application
 */
public class AddObservationFragment extends Fragment implements LocationController.ILocationCallBacks,
        TextValidator.ITextValidate, View.OnClickListener {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private static final int GALLERY_CAMERA_PERMISSION = 24;
    private final String TAG = "~!@#$AddObservation";
    // ImageView
    @Bind(R.id.imageView_addObs_tickImage)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_addObs_postObservation)
    Button btn_PostObservation;
    @Bind(R.id.button_addObs_chooseFromGuide)
    Button btn_chooseFromGuide;
    // EditTexts
    @Bind(R.id.editText_addObs_numOfTicks)
    EditText et_numOfTicks;
    @Bind(R.id.editText_addObs_tickName)
    EditText et_tickName;
    @Bind(R.id.editText_addObs_description)
    EditText et_description;
    // flags
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
    private List<Tick> tickList;
    private boolean isTotalTicksNumber, isTickNameValid;
    private TextValidator mTextValidator;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private PermissionUtils mPermissionUtils;
    private ProgressDialog mProgressDialog;
    private ImageController mImageController;


    public AddObservationFragment() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link AddObservationFragment} Fragment built using key and {@link Observation}.
     *
     * @param key
     * @param value
     * @return
     */
    public static AddObservationFragment newInstance(String key, String value) {
        AddObservationFragment addObservationFragment = new AddObservationFragment();
        Bundle args = new Bundle();
        args.putString(key, value);
        addObservationFragment.setArguments(args);
        return addObservationFragment;
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
        View rootView = inflater.inflate(R.layout.fragment_add_observation, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            args = getArguments();
        }
        dbController = new DatabaseDataController(mContext, ObservationsDao.getInstance());
        mAuthPreferences = new AuthPreferences(mContext);
        mImageController = new ImageController(mContext);

        mAlbumStorageDirFactory = new BaseAlbumDirFactory();

        if (args != null && args.containsKey(UserActivityMasterActivity.KEY_ACTIVITY_ID)) {
            activityId = args.getString(UserActivityMasterActivity.KEY_ACTIVITY_ID);
        } else {
            //FIXME ActivityId will be empty
            activityId = "";
        }

        // get user_id.
        userId = mAuthPreferences.getUser_id();
        et_tickName.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_tickName));
        //et_description.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_numOfTicks));
        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) rootView.findViewById(R.id.fab_addObs_addTickImage);

        addTickImage.setOnClickListener(this);
        btn_PostObservation.setOnClickListener(this);
        // required to pass the LayoutInflater
        btn_chooseFromGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseTickDialog(inflater);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            openDialogForChoosingImage();
        } else {
            // will be called only first time the user chooses to select Image
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

                // depending on network connection, save the Observation in local storage or server
                if (AppUtils.isConnectedOnline(mContext)) {
                    BusProvider.bus().post(new ObservationEvent.OnLoadingInitialized(newObservationObj, ApiRequestHandler.ADD));
                    displayProgressDialog("Posting Observation...");
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
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Helper method to setup and display Choose Tick Dialog having Custom Layout
     *
     * @param inflater
     */

    private void openChooseTickDialog(LayoutInflater inflater) {
        final AlertDialog.Builder alertDialogChooseTick = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
        View customView = inflater.inflate(R.layout.alertdialog_choosetick_list, null);

        // identify the ListView
        final ListView listViewTicks = (ListView) customView.findViewById(R.id.listView_chooseTick);

        tickList = TickHelper.getAllTicks();

        // Build an ArrayAdapter
        final TickDialogAdapter dialogAdapter = new TickDialogAdapter(mContext, R.layout.alertdialog_choosetick_item, tickList);
        listViewTicks.setAdapter(dialogAdapter);

        // make the divider invisible
        listViewTicks.setDivider(null);
        listViewTicks.setDividerHeight(0);

        dialogAdapter.setNotifyOnChange(true);
        dialogAdapter.notifyDataSetChanged();

        // set the characteristics of AlertDialog.
        alertDialogChooseTick.setTitle(getActivity().getResources().getString(R.string.alert_chooseTick));
        alertDialogChooseTick.setView(customView);

        // create a dialog to get the reference to when dismissing the Dialog.
        final AlertDialog dialog = alertDialogChooseTick.create();
        dialog.show();

        // when the user simply presses the news item, then the DetailedNewsActivity will get started.
        listViewTicks.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // pass the Tick name and Species using EventBus
                        Tick tickSelected = tickList.get(position);
                        BusProvider.bus().post(new TickEvent.OnTickSelected(tickSelected));
                        dialog.dismiss();
                    }
                });
    }

    /**
     * Subscribes to the event of success in selecting Tick from Dialog
     *
     * @param onTickSelected
     */
    @Subscribe
    public void onTickItemSelectedFromDialogSuccess(TickEvent.OnTickSelected onTickSelected) {
        if (onTickSelected != null) {
            et_tickName.setText(onTickSelected.getResponse().getTickName());
            et_numOfTicks.requestFocus();
        }
    }

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
                openDialogForChoosingImage();
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
        // if the Image was selected from the Camera. selectedImagePath is already set
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

                    // set the image path for the image chosen from Gallery
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
        // setup google Map using the GoogleMapController.
        // initialize the LocationController
        if (AppUtils.isConnectedOnline(mContext)) {
            mLocationController = new LocationController(mContext, this);
            mLocationController.connectGoogleApi();
        }
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
        mInterface.postObservationData(ObservationMasterActivity.FLAG_ACTIVITY_RUNNING);
    }


    @Subscribe
    public void onObservationImageUploadFailure(FileUploadEvent.OnLoadingError onLoadingError) {
        dismissProgressDialog();
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Callback interface to handle the onClick Listeners in {@link AddObservationFragment} Fragment.
     */
    public static interface IAddObservationCallBack {
        /**
         * Callback method to open {@link ObservationListFragment} after {@link Observation} is posted on server or SQLite DB.
         *
         * @param newObservation
         */
        void postObservationData(long statusFlag);
    }

}
