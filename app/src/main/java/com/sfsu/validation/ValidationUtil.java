package com.sfsu.validation;

import android.text.TextUtils;
import android.widget.EditText;

import com.sfsu.utils.AppUtils;

/**
 * Created by Pavitra on 11/20/2015.
 */
public class ValidationUtil {

    public static final String ERROR_INVALID_STR = "Invalid String";
    public static final String ERROR_NAN = "Invalid number";
    public static final String ERROR_INVALID_EMAIL = "Invalid email";


    /**
     * Validates the EditText for the input numeric value [0-9].
     *
     * @param mEditText
     * @param text
     * @return
     */
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

    /**
     * Validates the EditText for the input text [A-Z a-z].
     *
     * @param mEditText
     * @param text
     * @return
     */
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

    /**
     * Validates the input Email text entered by Account.
     *
     * @param mEditText
     * @param email
     * @return
     */
    public static boolean validateEmail(EditText mEditText, String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(email);
//        return matcher.matches();

        if (email.isEmpty() || !isValidEmail(email)) {
            mEditText.setError(ERROR_INVALID_EMAIL);
            mEditText.requestFocus();
            return false;
        } else {
            mEditText.setError(null);
        }

        return true;
    }

    /**
     * helper method for validating email.
     *
     * @param email
     * @return
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
