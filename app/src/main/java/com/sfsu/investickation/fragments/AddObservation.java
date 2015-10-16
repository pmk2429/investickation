package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.investickation.R;

public class AddObservation extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;


    public AddObservation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Add Observation");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_observation, container, false);
        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) v.findViewById(R.id.fab_addTickImage);
        addTickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
        });

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
