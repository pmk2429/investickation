package investickations.com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.investickation.R;

/**
 *
 */
public class Register extends Fragment implements View.OnClickListener {

    @InjectView(R.id.button_Register)
    Button btnRegister;
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.inject(getActivity());
        if (btnRegister != null) {
            btnRegister.setOnClickListener(this);
        } else {
            Log.d(AppConfig.LOGSTRING, "Register button not found");
        }

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
        mListener.onRegisterItemListener();
    }

    /**
     * Interface to communicate to other Fragment and/or Activity
     */
    public interface IRegisterCallBacks {
        public void onRegisterItemListener();
    }

}
