package com.sfsu.entities;

import com.squareup.okhttp.RequestBody;

/**
 * Holding class for Bitmaps and Images. Defines RequestBody, Description and Name of the Image.
 * <p/>
 * Created by Pavitra on 1/2/2016.
 */
public class ImageData {

    private RequestBody mRequestBody;
    private String image_name;
    private String description;

    public ImageData(RequestBody mRequestBody, String image_name, String description) {
        this.mRequestBody = mRequestBody;
        this.image_name = image_name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
