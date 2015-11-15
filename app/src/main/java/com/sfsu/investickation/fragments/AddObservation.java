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
import com.sfsu.controllers.RetrofitController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;

import java.io.File;
import java.sql.Timestamp;

public class AddObservation extends Fragment {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private final String LOGTAG = "~!@#$AddObservation :";
    private Bitmap bitmap;
    private ImageView imageView_tickAddObservation;
    private String selectedImagePath;
    private Button btn_PostObservation;
    private Observation newlyCreatedTickObj;
    private RetrofitController retrofitController;
    private IAddObservationCallBack mInterface;
    private Context mContext;
    private Intent locationIntent;
    private EditText et_tickName, et_tickSpecies;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_observation, container, false);

        btn_PostObservation = (Button) v.findViewById(R.id.button_postObservation);
        imageView_tickAddObservation = (ImageView) v.findViewById(R.id.imageView_addObs_tickImage);

        et_tickName = (EditText) v.findViewById(R.id.editText_addObs_newTick);
        et_tickSpecies = (EditText) v.findViewById(R.id.editText_addObs_tickSpecies);

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
                // TODO: complete this code. Get data from this Fragment and callback on ObservationMasterActivity to RetrofitController

                String tickName = et_tickName.getText().toString();
                String tickSpecies = et_tickSpecies.getText().toString();

                // TODO : logic for ID, and image manipulation
                // create a Tick Obj
                long currentTime = new Timestamp(System.currentTimeMillis()).getTime();
                double latitude = 37.773972;
                double longitude = -122.431297;
                String geoLocation = "San Francisco";


                newlyCreatedTickObj = Observation.createObservation(tickName, tickSpecies, geoLocation, "", latitude,
                        longitude, currentTime);

                // once the data for Observation is collected, get the current UserLocation

                // pass the object to the ObservationActivity.
                mInterface.postObservationData(newlyCreatedTickObj);
                // once the data is sent to RetrofitController get the response from same and pass it to RemoteObservations
            }
        });

        return v;
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
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
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
                } catch (Exception e) {
                    Log.d(LOGTAG, e.getMessage());
                }

                // use to transform coordinates according to orientation.
                Matrix matrix = new Matrix();
                // rotate based on degrees
                matrix.postRotate(rotate);
                // create a new bitmap from the matrix
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // set Tick image to imageView.
                imageView_tickAddObservation.setImageBitmap(bitmap);
                //TODO: create BLOB or large Binary representation and send it on server.

            } catch (Exception e) {
                Log.d(LOGTAG, e.getMessage());
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
                    bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                } catch (Exception e) {
                    Log.d("---Exception", e.getMessage());
                }
                // set the bitmap in ImageView
                imageView_tickAddObservation.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
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

    public static interface IAddObservationCallBack {
        void postObservationData(Observation newObservation);
    }


}
