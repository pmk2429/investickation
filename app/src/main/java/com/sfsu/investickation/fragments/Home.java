package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfsu.investickation.R;

import butterknife.Bind;

/**
 * Holds the Callbacks for the Login and Register Fragments. This Fragment will be displayed to the user opens the app or logs in
 * for the first time after Navigating through {@link WelcomeScreen}. In addition, when the User logs out of the Application, the same
 * Fragment will be opened.
 */
public class Home extends Fragment {

    public final String TAG = "~!@#$Home";
    // TextView
    @Bind(R.id.textView_home_login)
    TextView txtView_login;
    @Bind(R.id.textView_home_signUp)
    TextView txtView_signUp;

    private IHomeCallbacks mInterface;
    private Context mContext;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        txtView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onLoginClicked();
            }
        });

        txtView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onSignUpClicked();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IHomeCallbacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IHomeCallbacks interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    /**
     * Callback Interface to handle the onClick listeners for {@link Home} Fragment.
     */
    public interface IHomeCallbacks {
        /**
         * Callback method when the user clicks on the <tt>Login</tt> button in {@Home} Fragment
         */
        public void onLoginClicked();

        /**
         * Callback method when the user clicks on the <tt>Register</tt> button in {@Home} Fragment
         */
        public void onSignUpClicked();
    }

}
