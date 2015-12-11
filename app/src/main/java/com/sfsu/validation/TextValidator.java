package com.sfsu.validation;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * TextValidator is used to validate the input View for null, empty String, number or character. Depending on the input Code
 * and the type of the View, TextValidator performs Validation accordingly.
 * .
 * Created by Pavitra on 11/13/2015.
 */
public class TextValidator implements TextWatcher {

    public static final int VALID_STRING = 1;
    public static final int VALID_NUMBER = 2;
    public static final int VALID_EMAIL = 3;
    public static final int VALID_PASSWORD = 4;
    private View mView;
    private Context mContext;
    private ITextValidate mInterface;

    /**
     * Constructor overloading for getting the EditText to validate.
     *
     * @param mView
     */
    public TextValidator(Context mContext, Fragment fragment, View mView) {
        this.mInterface = (ITextValidate) fragment;
        this.mView = mView;
        this.mContext = mContext;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        EditText mEditText = (EditText) mView;
        String text = mEditText.getText().toString();
        mInterface.validate(mView, text);
    }

    /**
     * Callback interface to Validate the EditText for input String.
     */
    public interface ITextValidate {
        /**
         * Validates the given EditText for the input text.
         *
         * @param mView
         * @param text
         */
        public void validate(View mView, String text);
    }
}
