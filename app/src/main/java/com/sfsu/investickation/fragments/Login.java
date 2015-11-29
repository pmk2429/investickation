package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.LoginController;
import com.sfsu.helper.SessionManager;
import com.sfsu.investickation.HomeActivity;
import com.sfsu.investickation.MainActivity;
import com.sfsu.investickation.R;
import com.sfsu.model.UsersDao;
import com.sfsu.network.rest.service.LoginService;
import com.sfsu.network.handler.ServiceManager;
import com.sfsu.validation.TextValidator;
import com.sfsu.validation.TextValidator.ITextValidate;
import com.sfsu.validation.ValidationUtil;

import retrofit.Call;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                // verify and validate email and password input fields
                if (isEmailValid && isPasswordValid) {
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(mContext, "Please enter valid credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });


        return v;
    }

    /**
     * Helper method to check Login credentials entered by user.
     *
     * @param email
     * @param password
     */
    private void checkLogin(final String email, final String password) {

        LoginController mLoginController = new LoginController(mContext);
        mLoginController.checkLogin(email, password);

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        // make a call to retrofit controller to check Login sessions.
        LoginService mLoginService = ServiceManager.createService(LoginService.class);

        // TODO: change this code and delegate the calls to LoginController or SessionManager to handle Retrofit calls.
        Call<Login> userLoginCall = mLoginService.login(email, password);


        // success
        /*
        * On success, perform following steps:
        * 1) get the Access Token
        * 2) session.setLogin(true);
        * 3) Get User object
        * 4) store it in DB for future purpose.
        * 5) open main activity
        * */

        // response

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
        mListener.onLoginButtonClick();
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
