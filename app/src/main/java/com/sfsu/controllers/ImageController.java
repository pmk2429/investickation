package com.sfsu.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.sfsu.image.AlbumStorageDirFactory;
import com.sfsu.image.BaseAlbumDirFactory;
import com.sfsu.investickation.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.RequestBody;

/**
 * <p>
 * Controller for all the Image manipulation and bitmap handling operations. Holds the context of the calling Activity/Fragment.
 * </p>
 * This class provides abstraction for the Image manager layer and allows user to call the methods define in the ImageManager.
 * Created by Pavitra on 12/30/2015.
 */
public class ImageController {
    private static final String JPEG_FILE_PREFIX = "TICK_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private Context mContext;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    public ImageController(Context mContext) {
        this.mContext = mContext;
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
    }

    public String getImageName() {
        return "";
    }

    public RequestBody getImageRequestBody() {
        return null;
    }

    /**
     * Returns the Photo album for this application
     */
    public String getAlbumName() {
        return mContext.getString(R.string.album_name);
    }

    /**
     * Returns the album directory for the application in external storage
     *
     * @return
     */
    public File getAlbumDir() {
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
            Log.i(mContext.getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }


    /**
     * Method to create the temp file by specifying the name using prefix, suffix and timestamp
     *
     * @return
     * @throws IOException
     */
    public File createImageFile() throws IOException {
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
    public File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        return f;
    }

    /**
     * Returns the Bitmap matching the dimension and attributes of the ImageView.
     */
    public Bitmap getBitmapForImageView(ImageView mImageView, String selectedImagePath) {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

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

        return bitmap;
    }

    /**
     * Finally saves the image to the gallery in external storage
     *
     * @param selectedImagePath - Image path for the user selected Image to display it in ImageView
     */
    public void galleryAddPic(String selectedImagePath) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(selectedImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

}
