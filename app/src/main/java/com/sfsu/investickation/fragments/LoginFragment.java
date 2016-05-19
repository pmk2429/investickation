package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sfsu.application.InvestickationApp;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.exceptions.ServerUnavailableException;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.LoginEvent;
import com.sfsu.receiver.TicksDownloadReceiver;
import com.sfsu.service.DownloadTickIntentService;
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
 * Allows user to LoginFragment to InvesTICKations app. The details entered by the user will be validated and compared to the
 * corresponding entry in the local DB. If the match is found then user will be logged in and a network request will be made to
 * the REST API via {@link com.sfsu.controllers.RetrofitController} to get the <b>request token</b>.
 * <p>
 * This <b>request token</b> will be stored in Account's session via a {@link SessionManager} to authenticate and
 * validate Account for successive operations.
 * </p>
 * <p>
 * Once the User is logged in successfully, all the {@link com.sfsu.entities.Tick} are downloaded from the server and stored in
 * the local database for future references. This is to make sure that User always have access to the Ticks while in offline
 * mode and so it gives more flexibility and usability to the user while accessing the app offline.
 * </p>
 */
public class LoginFragment extends Fragment implements View.OnClickListener, ITextValidate {

    private final String TAG = "~!@#LOGIN";
    @Bind(R.id.button_login_login)
    Button btnLogin;
    @Bind(R.id.editText_login_email)
    EditText et_email;
    @Bind(R.id.editText_login_password)
    EditText et_password;
    private ILoginCallBack mListener;
    private DatabaseDataController dbController;
    private Context mContext;
    private UsersDao usersDao;
    private SessionManager mSessionManager;
    private boolean isEmailValid, isPasswordValid;
    private AuthPreferences mAuthPreferences;
    private ProgressDialog mProgressDialog;
    private TicksDownloadReceiver mTicksDownloadReceiver;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersDao = new UsersDao();
        // initialize the DatabaseController for UsersDao related DB calls.
        dbController = new DatabaseDataController(mContext, usersDao);
        mTicksDownloadReceiver = new TicksDownloadReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        et_email.addTextChangedListener(new TextValidator(mContext, LoginFragment.this, et_email));
        et_password.addTextChangedListener(new TextValidator(mContext, LoginFragment.this, et_password));

        // preference manager for access token and user_id.
        mAuthPreferences = new AuthPreferences(mContext);
        mSessionManager = new SessionManager(mContext);

        // set onClickListener on this Fragment.
        btnLogin.setOnClickListener(this);

        return rootView;
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

        if (isEmailValid && isPasswordValid) {
            // FIXME: possible bug in additional Context dependency injection
            try {
                login(email, password, mContext);
            } catch (ServerUnavailableException e) {
                if (BuildConfig.DEBUG)
                    Log.e(TAG, "onClick: ", e);
                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(mContext, "Please enter valid credentials!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Helper method to login the Account by making a network call to api using retrofit.
     *
     * @param email
     * @param password
     */
    public void login(final String email, final String password, Context mContext) throws ServerUnavailableException {
        // verify and validate email and password input fields
        BusProvider.bus().post(new LoginEvent.OnLoadingInitialized(email, password));
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle(getString(R.string.progressDialog_login_title));
        mProgressDialog.setMessage(getString(R.string.progressDialog_login_message));
        mProgressDialog.show();

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
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mTicksDownloadReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(DownloadTickIntentService.ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mTicksDownloadReceiver, filter);
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

        /**
         * Once the User is logged in, download all the Ticks in background using service and redirect the User to the
         * Dashboard for using the application.
         */
        if (AppUtils.isConnectedOnline(mContext)) {
            // call to download the Ticks from Server
            getActivity().startService(new Intent(mContext, DownloadTickIntentService.class));
        }

        // Finally open the dashboard
        mListener.userLoggedIn();
    }

    @Subscribe
    public void onLoginError(LoginEvent.OnLoadingError onLoadingError) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * Callback interface to handle onclick Listeners of {@link LoginFragment} Fragment.
     */
    public interface ILoginCallBack {

        /**
         * Callback method to handle the onclick of LoginFragment button in {@link LoginFragment} Fragment.
         * <p>When the user successfully logs in from the server, a Account session is created and the credentials are stored in
         * the SharedPreferences for further future usage.</p>
         */
        public void userLoggedIn();
    }

}
