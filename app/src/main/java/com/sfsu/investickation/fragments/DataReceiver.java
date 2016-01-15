package com.sfsu.investickation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sfsu.investickation.R;

/**
 * Receives the broadcast from {@link com.sfsu.service.PeriodicDataUploadService} for any data available only once.
 */
public class DataReceiver extends Fragment {

    private IDataReceiverCallbacks mListener;
    private Context mContext;

    public DataReceiver() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_receiver, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDataReceiverCallbacks) {
            mListener = (IDataReceiverCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IDataReceiverCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface IDataReceiverCallbacks {
        void onFragmentInteraction();
    }
}
