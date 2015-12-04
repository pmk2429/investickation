package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sfsu.entities.User;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;

/**
 * Registers the User and sends the User input Data to server and creates a copy in SQLite server for future accesses. The data
 * entered by the User passed a Custom Validation and after that the User object is passed to {@link RetrofitController} which
 * will then after make a network call and save the data on server. Meanwhile, when the server gives back the response, this
 * Fragment passes the {@link User} object to the {@link com.sfsu.investickation.HomeActivity} where the copy of a User object
 * is stored on the local SQLite DB.
 * <p>
 * The main reason for storing the User on local DB is to avoid making unwanted network calls when the user wants to log back
 * in.
 */
public class Register extends Fragment implements View.OnClickListener, ITextValidate {

    private final String LOGTAG = "~!@#Register :";

    private Button btnRegisterUser;
    private EditText et_fullName, et_email, et_password, et_zipcode, et_address;
    private ImageView imageView_userImage;
    private IRegisterCallBacks mListener;
    private Context mContext;
    private boolean isFullNameValid, isEmailValid, isPasswordValid, isAddressValid;

    public Register() {
        // IMP - Don't delete
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Register");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // initialize the RetroFit controller.

        et_fullName = (EditText) v.findViewById(R.id.editText_register_fullName);
        et_fullName.addTextChangedListener(new TextValidator(mContext, Register.this, et_fullName));
        et_email = (EditText) v.findViewById(R.id.editText_register_email);
        et_email.addTextChangedListener(new TextValidator(mContext, Register.this, et_email));
        et_password = (EditText) v.findViewById(R.id.editText_register_password);
        et_password.addTextChangedListener(new TextValidator(mContext, Register.this, et_password));
        et_address = (EditText) v.findViewById(R.id.editText_register_address);
        et_address.addTextChangedListener(new TextValidator(mContext, Register.this, et_address));
        et_zipcode = (EditText) v.findViewById(R.id.editText_register_zip);
        et_zipcode.addTextChangedListener(new TextValidator(mContext, Register.this, et_zipcode));

        btnRegisterUser = (Button) v.findViewById(R.id.button_registerUser);
        // implement the onClick method
        btnRegisterUser.setOnClickListener(this);
        return v;
    }

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IRegisterCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IRegisterCallbacks to communicate with Register");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegisterUser.getId()) {

            // verify all the users input data.
            if (isFullNameValid && isEmailValid && isPasswordValid && isAddressValid) {

                // get all the values from the Registration form
                String fullName = et_fullName.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String zipcode = et_zipcode.getText().toString();
                String address = et_address.getText().toString();

                // make a new User object.
                User userObj = User.createUser(fullName, email, password, zipcode, address);

                // TODO: Make a single Initialization - Singleton Pattern
                // finally send it to RetrofitController to
            }
        }
    }

    @Override
    public void validate(View mView, String text) {
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.editText_register_fullName:
                isFullNameValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_register_email:
                isEmailValid = ValidationUtil.validateEmail(mEditText, text);
                break;
            case R.id.editText_register_password:
                isPasswordValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_register_address:
                isAddressValid = ValidationUtil.validateString(mEditText, text);
                break;


        }
    }


    /**
     * Callback Interface to implement onclick Listener in {@link Register} Fragment.
     */
    public interface IRegisterCallBacks {
        /**
         *
         */
        public void onRegisterButtonClick();
    }

}
