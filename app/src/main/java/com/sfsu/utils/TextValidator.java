package com.sfsu.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Pavitra on 11/13/2015.
 */
public abstract class TextValidator implements TextWatcher {

    private final EditText mEditText;

    public TextValidator(EditText editText) {
        this.mEditText = editText;
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
