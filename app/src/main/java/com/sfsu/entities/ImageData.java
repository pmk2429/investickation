package com.sfsu.entities;


import okhttp3.RequestBody;

/**
 * Holding class for Bitmaps and Images. Defines RequestBody, Description and Name of the Image.
 * <p/>
 * Created by Pavitra on 1/2/2016.
 */
public class ImageData {

    private RequestBody mRequestBody;
    private String image_name;

    public ImageData(RequestBody mRequestBody, String image_name) {
        this.mRequestBody = mRequestBody;
        this.image_name = image_name;
    }

    public RequestBody getRequestBody() {
        return mRequestBody;
    }

    public void setRequestBody(RequestBody mRequestBody) {
        this.mRequestBody = mRequestBody;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
