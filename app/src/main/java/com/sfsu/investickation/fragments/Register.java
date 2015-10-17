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

import com.sfsu.entities.User;
import com.sfsu.investickation.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class Register extends Fragment implements View.OnClickListener {

    @InjectView(R.id.button_Register)
    private Button btnRegister;
    @InjectView(R.id.editText_username)
    private EditText et_username;
    @InjectView(R.id.editText_email)
    private EditText et_email;
    @InjectView(R.id.editText_password)
    private EditText et_password;
    @InjectView(R.id.editText_phone)
    private EditText et_phone;
    @InjectView(R.id.editText_address)
    private EditText et_address;
    @InjectView(R.id.image_usermain)
    private ImageView imageView_userImage;
    private IRegisterCallBacks mListener;


    public Register() {
        // IMP - Don't delete
    }

    // Factory method to create Fragment instance.
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Register");
        if (getArguments() != null) {

        }
        ButterKnife.inject(getActivity());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.inject(getActivity());

        // implement the onClick method
        btnRegister.setOnClickListener(this);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRegisterItemListener();
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
                    + " must implement OnFragmentInteractionListener to communicate with Register");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        // get all the values from the Registration form
        String userName = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String zipcode = et_phone.getText().toString();
        String address = et_address.getText().toString();

        // make a new object.
        User userObj = User.createUser(userName, email, password, zipcode, address);

        // once the User object is created, simply pass it to RetroFit library
        // TODO: access code for creating User entitiy using Retrofit

    }

    /**
     * Interface to communicate to other Fragment and/or Activity
     */
    public interface IRegisterCallBacks {
        public void onRegisterItemListener();
    }

}
