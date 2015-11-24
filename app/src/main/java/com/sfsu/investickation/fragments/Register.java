package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sfsu.controllers.RetrofitController;
import com.sfsu.entities.User;
import com.sfsu.investickation.R;

/**
 * Registers the User and sends the User input Data to Server via Retrofit Controller. The user enters the Full name. address,
 */
public class Register extends Fragment implements View.OnClickListener {

    private final String LOGTAG = "~!@#Register :";

    private Button btnRegisterUser;
    private EditText et_username, et_email, et_password, et_phone, et_address;
    private ImageView imageView_userImage;
    private RetrofitController retrofitController;
    private IRegisterCallBacks mListener;

    public Register() {
        // IMP - Don't delete
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Register");
        if (getArguments() != null) {

        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        // initialize the RetroFit controller.
        retrofitController = new RetrofitController(getActivity());

        btnRegisterUser = (Button) v.findViewById(R.id.button_registerUser);
        // implement the onClick method
        btnRegisterUser.setOnClickListener(this);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRegisterButtonClick();
        }
    }

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IRegisterCallBacks) activity;
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

            // get all the values from the Registration form
            String userName = et_username.getText().toString();
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();
            String zipcode = et_phone.getText().toString();
            String address = et_address.getText().toString();

            // make a new object.
            User userObj = User.createUser(userName, email, password, zipcode, address);
            // TODO: add the logic for passing the Object to Controller
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
