package com.sfsu.investickation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pavitra on 11/21/2015.
 */
public class WelcomeScreen extends Fragment {
    final static String LAYOUT_ID = "layoutId";

    public static WelcomeScreen newInstance(int layoutId) {
        WelcomeScreen pane = new WelcomeScreen();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(bundle);
        return pane;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        return root;
    }
}
