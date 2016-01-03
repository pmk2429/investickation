package com.sfsu.controllers;

import android.content.Context;
import android.graphics.Bitmap;

import com.sfsu.image.ImageManager;
import com.squareup.okhttp.RequestBody;

import java.io.File;

/**
 * <p>
 * Controller for all the Image manipulation and bitmap handling operations. Holds the context of the calling Activity/Fragment.
 * </p>
 * This class provides abstraction for the Image manager layer and allows user to call the methods define in the ImageManager.
 * Created by Pavitra on 12/30/2015.
 */
public class ImageController {
    private Context mContext;
    private ImageManager mImageManager;


    public ImageController(Context mContext) {
        this.mContext = mContext;
        this.mImageManager = new ImageManager();
    }

    public Bitmap getImageBitmap() {
        return null;
    }

    public String getImagePath() {
        return null;
    }

    public File getImageFile() {
        return null;
    }

    public String getImageName() {
        return "";
    }

    public RequestBody getImageRequestBody() {
        return null;
    }
}
