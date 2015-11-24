package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfsu.investickation.R;


public class Login extends Fragment implements View.OnClickListener {

    private final String LOGTAG = "~!@#LOGIN :";
    private ILoginCallBack mListener;
    private Context context;


    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);


        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ILoginCallBack) activity;
            context = activity;
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
