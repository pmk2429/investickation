package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.TickDialogAdapter;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.LocationController;
import com.sfsu.entities.EntityLocation;
import com.sfsu.entities.Observation;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.model.TickDao;
import com.sfsu.utils.AppUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.ValidationUtil;

import java.io.File;
import java.util.List;

public class AddObservation extends Fragment implements LocationController.ILocationCallBacks, TextValidator.ITextValidate {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private final String LOGTAG = "~!@#$AddObservation :";
    private Bitmap bitmap;
    private ImageView imageView_tickAddObservation;
    private String selectedImagePath, picturePath;
    private Button btn_PostObservation;
    private Observation newObservationObj;
    private IAddObservationCallBack mInterface;
    private Context mContext;
    private Intent locationIntent;
    private EditText et_tickName, et_tickSpecies, et_numOfTicks;
    private String activityUUID, requestToken, userId;
    private Bundle args;
    private LocationController mLocationController;
    private EntityLocation entityLocation;
    private DatabaseDataController dbController;
    private List<Tick> tickList;
    private boolean isTotalTicksNumber, isTickNameValid, isTickSpeciesValid;
    private TextValidator mTextValidator;

    // the BroadcastReceiver is used to get the data from the Service and send it to Retrofit
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLocationDataFromService(intent);
        }
    };

    public AddObservation() {
        // Required empty public constructor
    }

    private void getLocationDataFromService(Intent intent) {
        // TODO: add the Lat Long into the Observation and send it to Retrofit API
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add Observation");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        args = getArguments();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_observation, container, false);

        if (args != null && args.containsKey(UserActivityMasterActivity.KEY_ACTIVITY_UUID)) {
            activityUUID = args.getString(UserActivityMasterActivity.KEY_ACTIVITY_UUID);
        } else {
            activityUUID = "";
        }

        // initialize the LocationController
        mLocationController = new LocationController(mContext, this);
        mLocationController.connectGoogleApi();

        // identify the Views in this Fragment.
        btn_PostObservation = (Button) v.findViewById(R.id.button_postObservation);
        imageView_tickAddObservation = (ImageView) v.findViewById(R.id.imageView_addObs_tickImage);


        // identify all the EditTexts and set Validation on all.
        et_tickName = (EditText) v.findViewById(R.id.editText_addObs_tickName);
        et_tickName.setKeyListener(null);
        et_tickName.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickName));

        et_tickSpecies = (EditText) v.findViewById(R.id.editText_addObs_tickSpecies);
        et_tickSpecies.setKeyListener(null);
        et_tickName.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_tickSpecies));

        et_numOfTicks = (EditText) v.findViewById(R.id.editText_addObs_numOfTicks);
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, AddObservation.this, et_numOfTicks));

        et_tickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseTickDialog(inflater);
            }
        });
        et_tickSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChooseTickDialog(inflater);
            }
        });

        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) v.findViewById(R.id.fab_addObs_addTickImage);
        addTickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialogForChoosingImage();
            }
        });

        btn_PostObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTotalTicksNumber) {
                    String tickName = et_tickName.getText().toString();
                    String tickSpecies = et_tickSpecies.getText().toString();
                    int numOfTicks = Integer.parseInt(et_numOfTicks.getText().toString());
                    requestToken = "skbdjskmdcvslkvndjfnvsckjnskdb";
                    userId = "3dknv-vjbv78-dcnbdvkn";

                    // finally when all values are collected, create a new Observation object.
                    newObservationObj = new Observation(selectedImagePath, tickName, numOfTicks, AppUtils.getCurrentTimeStamp(),
                            entityLocation, requestToken, userId);

                    // pass the object to the ObservationActivity.
                    mInterface.postObservationData(newObservationObj);
                    // once the data is sent to RetrofitController get the response from same and pass it to RemoteObservations
                }
            }
        });

        return v;
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
        ListView listViewTicks = (ListView) customView.findViewById(R.id.listView_chooseTick);

        tickList = Tick.getAllTicks();

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
                        // set Texts in EditTexts.
                        setTickData(tickList.get(position));
                        dialog.dismiss();
                    }
                });
    }


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
     * Helper method to get the Tick object from the TickName;
     *
     * @param tickName
     * @return
     */
    private Tick getTickFromName(String tickName) {
        dbController = new DatabaseDataController(mContext, new TickDao());
        return (Tick) dbController.get(tickName);
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


        // TODO: write logic for making FileName and Directory.

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
                        Log.d(LOGTAG, e.getMessage());
                    }

                    // Picasso.with(mContext).load(imageFile).centerCrop().into(imageView_tickAddObservation);

                    imageView_tickAddObservation.setImageBitmap(bitmap);
                    //TODO: create BLOB or large Binary representation and send it on server.

                } catch (Exception e) {
                    Log.d(LOGTAG, e.getMessage());
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

                try {
                    //bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, false);
                } catch (Exception e) {
                    Log.d("---Exception", e.getMessage());
                }
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

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image with both dimensions larger than or equal to the
            // requested height and width.
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
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPause() {
        super.onPause();
//        getActivity().unregisterReceiver(broadcastReceiver);
//        getActivity().stopService(locationIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
//        getActivity().startService(locationIntent);
//        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));

    }

    @Override
    public void setCurrentLocation(Location mLocation) {
        this.entityLocation = new EntityLocation(mLocation.getLatitude(), mLocation.getLongitude());

    }

    @Override
    public void setLocationArea(String locationArea) {

    }

    private boolean validateNumber(EditText mEditText, String text) {
        if (text.isEmpty() || !AppUtils.isNumeric(text)) {
            mEditText.setError(getString(R.string.error_NAN));
            mEditText.requestFocus();
            return false;
        } else {
            mEditText.setError(null);
        }
        return true;
    }

    @Override
    public void validate(View mView, String text) {
        Log.i(LOGTAG, "method called");
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.editText_addObs_tickName:
                Log.i(LOGTAG, "name");
                isTickNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_addObs_tickSpecies:
                Log.i(LOGTAG, "species");
                isTickSpeciesValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_addObs_numOfTicks:
                Log.i(LOGTAG, "number");
                isTotalTicksNumber = validateNumber(mEditText, text);
                break;
        }
    }

    /**
     * Callback interface to handle the onClick Listeners in {@link AddObservation} Fragment.
     */
    public static interface IAddObservationCallBack {
        /**
         * Callback method to post the new {@link Observation} on the Server via {@link RetrofitController}.
         *
         * @param newObservation
         */
        void postObservationData(Observation newObservation);
    }

}
