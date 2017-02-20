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
import butterknife.ButterKnife;

/**
 * Holds the {@link LoginFragment} and {@link RegisterFragment} Fragments. HomeFragment fragment will be displayed when the user will open the app or
 * logs in for the first time after Navigating through {@link WelcomeScreenFragment}. In addition, when the Account logs out of the
 * Application, the same Fragment will be opened.
 */
public class HomeFragment extends Fragment {

    public final String TAG = "~!@#$HomeFragment";
    // TextView
    @Bind(R.id.textview_login)
    TextView txtView_login;
    @Bind(R.id.textview_signup)
    TextView txtView_signUp;

    private IHomeCallbacks mInterface;
    private Context mContext;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, rootView);

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
     * Callback Interface to handle the onClick listeners for {@link HomeFragment} Fragment.
     */
    public interface IHomeCallbacks {
        /**
         * Callback method when the user clicks on the <tt>LoginFragment</tt> button in {@link HomeFragment}
         */
        public void onLoginClicked();

        /**
         * Callback method when the user clicks on the <tt>RegisterFragment</tt> button in {@link HomeFragment}
         */
        public void onSignUpClicked();
    }

}
