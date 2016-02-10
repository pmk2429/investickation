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

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.entities.Account;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the {@link Account} data. Allows users to edit the data.
 */
public class Profile extends Fragment {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private final String TAG = "~!@#Profile";
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
    private DatabaseDataController dbController;
    private Context mContext;
    private Account mUser;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mAuthPreferences = new AuthPreferences(mContext);
        dbController = new DatabaseDataController(mContext, new UsersDao());
        //BusProvider.bus().post(new UserEvent.OnLoadingInitialized(mAuthPreferences.getUser_id(), ApiRequestHandler.GET));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);

        try {
            // get the Account object from Database
//            if (dbController != null) {
//                mUser = (Account) dbController.get(mAuthPreferences.getUser_id());
//            }

            // set the values in the EditText
//            if (mUser != null) {
//                et_fullName.setText(mUser.getFull_name());
//                String address = mUser.getAddress() + " " + mUser.getCity() + " " + mUser.getState() + " " + mUser.getZipCode();
//                et_address.setText(address);
//                et_email.setText(mUser.getEmail());
//                et_password.setText(mUser.getPassword());
//            } else {
//                et_fullName.setText("Full Name");
//                et_address.setText("Address");
//                et_email.setText("Email");
//                et_password.setText("Password");
//            }

            et_fullName.setText("Pavitra Kansara");
            et_address.setText("150 Font Blvd, San Francisco, CA, 94132");
            et_email.setText("pmk@mail.sfsu.edu");
            et_password.setText("12345");
        } catch (NullPointerException ne) {

        } catch (Exception e) {

        }

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
     * the Account image from Camera or Gallery.
     */
    private void startDialogForChoosingImage() {
        AlertDialog.Builder chooseImageAlertDialog = new AlertDialog.Builder(getActivity());
        chooseImageAlertDialog.setTitle("Add User image?");
        chooseImageAlertDialog.setMessage("How do you want to select your picture?");

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
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_profile);
        BusProvider.bus().register(this);
    }
}
