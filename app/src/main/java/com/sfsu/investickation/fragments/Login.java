package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.helper.SessionManager;
import com.sfsu.investickation.HomeActivity;
import com.sfsu.investickation.MainActivity;
import com.sfsu.investickation.R;
import com.sfsu.model.UsersDao;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.LoginEvent;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;
import com.squareup.otto.Subscribe;

/**
 * Allows user to Login to InvesTICKations app. The details entered by the user will be validated and compared to the
 * corresponding entry in the local DB. If the match is found then user will be logged in and a network request will be made to
 * the REST API via {@link com.sfsu.controllers.RetrofitController} to get the <b>request token</b>.
 * <p>
 * This <b>request token</b> will be stored in User's session via a {@link com.sfsu.helper.SessionManager} to authenticate and
 * validate User for successive operations.
 * </p>
 */
public class Login extends Fragment implements View.OnClickListener, ITextValidate {

    private final String LOGTAG = "~!@#LOGIN :";
    private ILoginCallBack mListener;
    private DatabaseDataController dbController;
    private Context mContext;
    private UsersDao usersDao;
    private SessionManager session;
    private Button btnLogin;
    private EditText et_email, et_password;
    private boolean isEmailValid, isPasswordValid;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersDao = new UsersDao();
        // initialize the DatabaseController for UsersDao related DB calls.
        dbController = new DatabaseDataController(mContext, usersDao);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = (Button) v.findViewById(R.id.button_login_login);

        et_email = (EditText) v.findViewById(R.id.editText_login_email);
        et_email.addTextChangedListener(new TextValidator(mContext, Login.this, et_email));

        et_password = (EditText) v.findViewById(R.id.editText_login_password);
        et_password.addTextChangedListener(new TextValidator(mContext, Login.this, et_password));

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in so redirect to MainActivity
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            ((HomeActivity) mContext).finish();
        }

        // set onClickListener on this Fragment.
        btnLogin.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ILoginCallBack) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ILoginCallbacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        final String email = et_email.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        // verify and validate email and password input fields
        if (isEmailValid && isPasswordValid) {
            BusProvider.bus().post(new LoginEvent.OnLoadingInitialized(email, password));
        } else {
            // Prompt user to enter credentials
            Snackbar.make(v, "Please enter valid credentials!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void validate(View mView, String text) {
        EditText mEditText = (EditText) mView;
        switch (mView.getId()) {
            case R.id.editText_login_email:
                isEmailValid = ValidationUtil.validateEmail(mEditText, text);
                break;
            case R.id.editText_login_password:
                isPasswordValid = ValidationUtil.validateString(mEditText, text);
                break;
        }
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

    @Subscribe
    public void onUserLoginSuccess(LoginEvent.OnLoaded onLoaded) {
        // Save the Access Token in Shared Preferences


        // save the User id into SharedPreferences.
    }

    @Subscribe
    public void onLoginError(LoginEvent.OnLoadingError onLoadingError) {
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Callback interface to handle onclick Listeners of {@link Login} Fragment.
     */
    public interface ILoginCallBack {

        /**
         * Callback method to handle the onclick of Login button in {@link Login} Fragment. On click of Login makes a database
         * call
         */
        public void onLoginButtonClick();
    }

}
