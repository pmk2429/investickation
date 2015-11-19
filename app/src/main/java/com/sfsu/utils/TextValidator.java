package com.sfsu.utils;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * TextValidator is used to validate the input View for null, empty String, number or character
 * .
 * Created by Pavitra on 11/13/2015.
 */
public abstract class TextValidator implements TextWatcher {

    private EditText mEditText;
    private View view;

    public TextValidator(View view, Fragment fragment) {
        this.view = view;
    }

    /**
     * Abstract method to validate the String in the EditText
     *
     * @param editText
     * @param text
     */
    public abstract void validate(EditText editText, String text);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = mEditText.getText().toString();
        validate(mEditText, text);
    }
}
