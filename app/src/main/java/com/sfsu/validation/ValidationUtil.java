package com.sfsu.validation;

import android.widget.EditText;

import com.sfsu.utils.AppUtils;

/**
 * Created by Pavitra on 11/20/2015.
 */
public class ValidationUtil {

    public static final String ERROR_INVALID_STR = "Invalid String";
    public static final String ERROR_NAN = "Invalid number";


    public static boolean validateNumber(EditText mEditText, String text) {
        if (text.isEmpty() || !AppUtils.isNumeric(text)) {
            mEditText.setError(ERROR_NAN);
            mEditText.requestFocus();
            return false;
        } else {
            mEditText.setError(null);
        }
        return true;
    }

    public static boolean validateString(EditText mEditText, String text) {
        if (mEditText.getText().toString().trim().isEmpty()) {
            mEditText.setError(ERROR_INVALID_STR);
            mEditText.requestFocus();
            return false;
        } else {
            mEditText.setError(null);
        }
        return true;
    }
}
