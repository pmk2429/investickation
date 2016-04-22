package com.sfsu.investickation.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sfsu.application.InvestickationApp;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.entities.Account;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.LoginEvent;
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.service.DownloadTickService;
import com.sfsu.session.LoginResponse;
import com.sfsu.session.SessionManager;
import com.sfsu.utils.AppUtils;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <p>
 * Registers the {@link Account} and sends the Account input data to server and on successful creation of a Account, creates a copy in
 * SQLite server for future {@link Account} related accesses and operations.
 * </p>
 * <p>
 * Meanwhile, when the server gives back the response, this Fragment passes the {@link Account} object to the
 * {@link com.sfsu.investickation.HomeActivity} where the copy of a Account object is stored on the local SQLite DB.
 * </p>
 * The main reason for storing the Account on local DB is to avoid making unwanted network calls when the user wants to log back
 * in.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, ITextValidate {

    private final String TAG = "~!@#RegisterFragment";
    // EditTexts
    @Bind(R.id.editText_register_fullName)
    EditText et_fullName;
    @Bind(R.id.editText_register_email)
    EditText et_email;
    @Bind(R.id.editText_register_password)
    EditText et_password;
    @Bind(R.id.editText_register_zip)
    EditText et_zipcode;
    @Bind(R.id.editText_register_address)
    EditText et_address;
    @Bind(R.id.editText_register_city)
    EditText et_city;
    @Bind(R.id.editText_register_state)
    EditText et_state;
    //Button
    @Bind(R.id.button_registerUser)
    Button btnRegisterUser;
    // Checkbox
    @Bind(R.id.checkbox_privacyAgreement)
    CheckBox checkbox_privacyAgreement;
    private IRegisterCallBacks mListener;
    private Context mContext;
    private DatabaseDataController dbController;
    private AuthPreferences mAuthPreferences;
    private SessionManager mSessionManager;
    private Account mUserObj;
    private boolean isFullNameValid, isEmailValid, isPasswordValid, isAddressValid, isZipcodeValid, isCityValid, isStateValid;
    private boolean isPrivacyAgreementRead;
    private ProgressDialog mProgressDialog;

    public RegisterFragment() {
        // IMP - Don't delete
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_register);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // bind the widgets
        ButterKnife.bind(this, v);

        et_fullName.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_fullName));
        et_email.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_email));
        et_password.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_password));
        et_address.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_address));
        et_zipcode.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_zipcode));
        et_city.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_city));
        et_state.addTextChangedListener(new TextValidator(mContext, RegisterFragment.this, et_state));

        checkbox_privacyAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPrivacyDialog(inflater);
            }
        });

        // when the user opens the RegisterFragment fragment for the first time, fade out the color of RegisterFragment Button.
        btnRegisterUser.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightText));
        btnRegisterUser.setEnabled(false);

        // preference manager for access token and user_id.
        mAuthPreferences = new AuthPreferences(mContext);
        mSessionManager = new SessionManager(mContext);

        isPrivacyAgreementRead = false;
        // implement the onClick method
        btnRegisterUser.setOnClickListener(this);
        return v;
    }

    /**
     * Helper method to display the custom Alert dialog for showing privacy agreement.
     */
    private void displayPrivacyDialog(LayoutInflater inflater) {
        final AlertDialog.Builder privacyDialog = new AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
        View convertView = inflater.inflate(R.layout.alertdialog_privacy_agreement, null);

        // enable the button on Agree click and allow user to register .
        privacyDialog.setPositiveButton(R.string.alertDialog_agree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPrivacyAgreementRead = true;
                btnRegisterUser.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
                btnRegisterUser.setEnabled(true);
                checkbox_privacyAgreement.setChecked(true);
                dialog.dismiss();
            }
        });

        // on cancel clicked, disable the button.
        privacyDialog.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPrivacyAgreementRead = false;
                btnRegisterUser.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightText));
                btnRegisterUser.setEnabled(false);
                checkbox_privacyAgreement.setChecked(false);
                dialog.dismiss();
            }
        });

        privacyDialog.setView(convertView);
        privacyDialog.show();
    }

    // This makes sure that the container activity has implemented the callback interface. If not, it throws an exception.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IRegisterCallBacks) {
                mListener = (IRegisterCallBacks) context;
                mContext = context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IRegisterCallbacks to communicate with RegisterFragment");
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
            if (isFullNameValid && isEmailValid && isPasswordValid && isAddressValid && isPrivacyAgreementRead) {

                // get all the values from the Registration form
                String fullName = et_fullName.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                int zipcode = Integer.valueOf(et_zipcode.getText().toString());
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String state = et_state.getText().toString();

                // make a new Account object.
                mUserObj = Account.createUser(fullName, address, city, state, zipcode, email, password);


                if (AppUtils.isConnectedOnline(mContext)) {
                    // once the user object is created, pass it to Bus to send it over to api via retrofit
                    BusProvider.bus().post(new UserEvent.OnLoadingInitialized(mUserObj, ApiRequestHandler.ADD));
                    mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setTitle(getString(R.string.progressDialog_register_title));
                    mProgressDialog.setMessage(getString(R.string.progressDialog_register_message));
                    mProgressDialog.show();
                } else {
                    Toast.makeText(mContext, "Internet connection not available", Toast.LENGTH_LONG).show();
                }
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
     * Subscribes to event of successful creation of {@link Account} on server.
     *
     * @param onLoaded
     */
    @Subscribe
    public void onUserCreateSuccess(UserEvent.OnLoaded onLoaded) {
        // preference manager for access token and user_id.
        mAuthPreferences = new AuthPreferences(mContext);
        dbController = new DatabaseDataController(mContext, new UsersDao());
        // once the user is successfully created, store the response in the SQLite database to store the user info for further
        // requirements.
        Account userResponseObj = onLoaded.getResponse();

        // set password to the user response object.
        userResponseObj.setPassword(mUserObj.getPassword());

        // save the response user object in DB for further references.
        long resultCode = dbController.save(userResponseObj);

        // on successful storage of user, perform further operations.
        if (resultCode != -1) {
            // once the user has successfully registered, make another call to API for the email and password to get the access
            // token and follow the same procedure as for the LoginFragment.
            //mListener.onRegisterButtonClick(mUserObj);
            BusProvider.bus().post(new LoginEvent.OnLoadingInitialized(mUserObj.getEmail(), mUserObj.getPassword()));
        }
    }

    /**
     * Subscribes to failure in creating user on server.
     *
     * @param onLoadingError
     */
    @Subscribe
    public void onUserCreateFailure(UserEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Subscribes to the LoginFragment event if the Response returned from the api is {@link LoginResponse}
     *
     * @param onLoaded
     */
    @Subscribe
    public void onUserLoginSuccess(LoginEvent.OnLoaded onLoaded) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        // Save the Access Token in Shared Preferences
        LoginResponse mLoginResponse = onLoaded.getResponse();
        boolean isCredentialsSet = mAuthPreferences.setCredentials(mLoginResponse.getAccessToken(), mLoginResponse.getUser_id());

        // if the Auth preferences is successfully set in SharedPreferences, then set the LoginFragment flag.
        if (isCredentialsSet) {
            mSessionManager.setLogin(true);
            InvestickationApp.getInstance().initResources();
        }

        // once the User is logged in, make a request to download all Ticks from the server and store it in DB
        getActivity().startService(new Intent(getActivity(), DownloadTickService.class));

        // once the token is set successfully, open the dashboard.
        mListener.onUserRegistrationDone();
    }

    @Subscribe
    public void onLoginError(LoginEvent.OnLoadingError onLoadingError) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Callback Interface to implement onclick Listener in {@link RegisterFragment} Fragment.
     */
    public interface IRegisterCallBacks {
        /**
         * Callback listener when the user clicks on the RegisterFragment button in {@link RegisterFragment} fragment. This method
         * will handle the calls for Registration and delegates the control to LoginFragment fragment to make another call to the
         * server in order to get Access Token and Account id.
         *
         * @param userResponse
         */
        void onUserRegistrationDone();

    }

}
