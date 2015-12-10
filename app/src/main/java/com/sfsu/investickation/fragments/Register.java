package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.entities.User;
import com.sfsu.investickation.BuildConfig;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.UserEvent;
import com.sfsu.utils.AppUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;

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
    private EditText et_fullName, et_email, et_password, et_zipcode, et_address, et_city, et_state;
    private ImageView imageView_userImage;
    private IRegisterCallBacks mListener;
    private Context mContext;
    private DatabaseDataController dbController;
    private AuthPreferences mAuthPreferences;
    private User mUserObj;
    private boolean isFullNameValid, isEmailValid, isPasswordValid, isAddressValid, isZipcodeValid, isCityValid, isStateValid;

    public Register() {
        // IMP - Don't delete
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Register");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

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
        et_city = (EditText) v.findViewById(R.id.editText_register_city);
        et_city.addTextChangedListener(new TextValidator(mContext, Register.this, et_city));
        et_state = (EditText) v.findViewById(R.id.editText_register_state);
        et_state.addTextChangedListener(new TextValidator(mContext, Register.this, et_state));

        // preference manager for access token and user_id.
        mAuthPreferences = new AuthPreferences(mContext);
        dbController = new DatabaseDataController(mContext, new UsersDao());

        btnRegisterUser = (Button) v.findViewById(R.id.button_registerUser);
        // implement the onClick method
        btnRegisterUser.setOnClickListener(this);
        return v;
    }

    // This makes sure that the container activity has implemented the callback interface. If not, it throws an exception.
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
    public void onClick(View v) {
        if (v.getId() == btnRegisterUser.getId()) {

            // verify all the users input data.
            if (isFullNameValid && isEmailValid && isPasswordValid && isAddressValid) {

                // get all the values from the Registration form
                String fullName = et_fullName.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                int zipcode = Integer.valueOf(et_zipcode.getText().toString());
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String state = et_state.getText().toString();

                // make a new User object.
                mUserObj = User.createUser(fullName, address, city, state, zipcode, email, password);

                Log.i(LOGTAG, "creating user.......");
                // once the user object is created, pass it to Bus to send it over to api via retrofit
                BusProvider.bus().post(new UserEvent.OnLoadingInitialized(mUserObj, AppUtils.ADD_METHOD));
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
            case R.id.editText_register_city:
                isCityValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_register_state:
                isStateValid = ValidationUtil.validateString(mEditText, text);
                break;
            case R.id.editText_register_zip:
                isZipcodeValid = ValidationUtil.validateNumber(mEditText, text);
                break;
        }
    }


    /**
     * Subscribes to successful user creation on server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onUserCreateSuccess(UserEvent.OnLoaded onLoaded) {
        // once the user is successfully created, store the response in the SQLite database to store the user info for further
        // requirements.
        User userResponse = onLoaded.getResponse();

        // set password to the user response object and save it to DB.
        userResponse.setPassword(mUserObj.getPassword());

        // TODO: store the user response in the database.
        //dbController.save(userResponse);

        // once the user has successfully registered, make another call to API for the email and password to get the access
        // token and follow the same procedure as for the Login.
        mListener.onRegisterButtonClick(mUserObj);
    }

    /**
     * Subscribes to failure in creating user on server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onUserCreateFailure(UserEvent.OnLoadingError onLoadingError) {
        if (BuildConfig.DEBUG)
            Log.i(LOGTAG, onLoadingError.toString());
        Log.i(LOGTAG, "failure to create user");
    }

    /**
     * Callback Interface to implement onclick Listener in {@link Register} Fragment.
     */
    public interface IRegisterCallBacks {
        /**
         * Callback listener when the user clicks on the Register button in {@link Register} fragment. This method
         * will handle the calls for Registration and delegates the control to Login fragment to make another call to the
         * server in order to get Access Token and User id.
         *
         * @param userResponse
         */
        public void onRegisterButtonClick(User mUserObj);

    }

}
