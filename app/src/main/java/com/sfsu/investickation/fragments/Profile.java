package com.sfsu.investickation.fragments;


import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.handler.ApiRequestHandler;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private final String TAG = "~!@#Profile :";
    Bitmap bitmap;
    String selectedImagePath;
    @Bind(R.id.editText_profile_fullName)
    EditText et_fullName;
    @Bind(R.id.editText_profile_email)
    EditText et_email;
    @Bind(R.id.editText_profile_password)
    EditText et_password;
    @Bind(R.id.editText_profile_address)
    EditText et_address;
    @Bind(R.id.imageView_profile_userImage)
    ImageView imageView_userImage;
    private AuthPreferences mAuthPreferences;
    private Context mContext;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("My Profile");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mAuthPreferences = new AuthPreferences(mContext);


        // TODO: make a network call and get the data?
        BusProvider.bus().post(new UserEvent.OnLoadingInitialized(mAuthPreferences.getUser_id(), ApiRequestHandler.GET));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);


        final FloatingActionButton fabUserImage = (FloatingActionButton) rootView.findViewById(R.id.fab_userProfileImage);
        fabUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogForChoosingImage();
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
        } catch (Exception e) {

        }
    }

    /**
     * This method is used to popup a dialog box for allowing user to select
     * the User image from Camera or Gallery.
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
                    Log.d(TAG, e.getMessage());
                }

                // use to transform coordinates according to orientation.
                Matrix matrix = new Matrix();
                // rotate based on degrees
                matrix.postRotate(rotate);
                // create a new bitmap from the matrix
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // set Tick image to imageView.
                imageView_userImage.setImageBitmap(bitmap);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
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
                imageView_userImage.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().unregister(this);
    }
}
