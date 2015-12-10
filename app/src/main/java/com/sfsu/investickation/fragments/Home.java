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

/**
 * Holds the Callbacks for the Login and Register Fragments. This Fragment will be displayed to the user when the User logs in
 * for the first time after Navigating through WelcomeScreen. In addition, when the User logs out of the Application, the same
 * Fragment will be opened.
 */
public class Home extends Fragment {

    private IHomeCallbacks mInterface;
    private Context mContext;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView txtView_login = (TextView) rootView.findViewById(R.id.textView_home_login);
        final TextView txtView_signUp = (TextView) rootView.findViewById(R.id.textView_home_signUp);


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
                    + " must implement IHomeCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    /**
     * Callback Interface to handle the onClick Listeners for {@link Home} Fragment.
     */
    public interface IHomeCallbacks {
        /**
         * Callback method when the user clicks on the Login button in {@Home} Fragment
         */
        public void onLoginClicked();

        /**
         * Callback method when the user clicks on the Register button in {@Home} Fragment
         */
        public void onSignUpClicked();
    }

}
