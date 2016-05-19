package com.sfsu.network.error;

import com.google.gson.annotations.SerializedName;

/**
 * The error response returned is nested <tt>error</tt> json, the {@link ApiError} class is nested within ErrorResponse class.
 * Created by Pavitra on 12/28/2015.
 */
public class ErrorResponse {
    @SerializedName("error")
    private ApiError apiError;

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
