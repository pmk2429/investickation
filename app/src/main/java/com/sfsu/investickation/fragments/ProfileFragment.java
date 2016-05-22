package com.sfsu.investickation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.UsersDao;
import com.sfsu.entities.Account;
import com.sfsu.investickation.R;
import com.sfsu.network.auth.AuthPreferences;
import com.sfsu.network.bus.BusProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the {@link Account} data. Allows users to edit the data.
 */
public class ProfileFragment extends Fragment {

    private final String TAG = "~!@#ProfileFragment";
    @Bind(R.id.editText_profile_fullName)
    EditText et_fullName;
    @Bind(R.id.editText_profile_email)
    EditText et_email;
    @Bind(R.id.editText_profile_address)
    EditText et_address;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;
    private Context mContext;
    private Account mUser;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mAuthPreferences = new AuthPreferences(mContext);
        dbController = new DatabaseDataController(mContext, new UsersDao());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        try {
            mUser = (Account) dbController.get(mAuthPreferences.getUser_id());
            Log.i(TAG, "onCreateView: " + mUser.toString());
            et_fullName.setText(mUser.getFull_name());
            et_address.setText(mUser.getAddress());
            et_email.setText(mUser.getEmail());
        } catch (NullPointerException ne) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "onCreateView ", ne);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_profile);
        BusProvider.bus().register(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personal_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditableView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Makes the TextView editable
     */
    private void showEditableView() {

    }

    private void editUserInformation() {

    }
}
