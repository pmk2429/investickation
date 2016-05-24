package com.sfsu.investickation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Button;
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
    @Bind(R.id.button_profile_save)
    Button btn_Save;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;
    private Context mContext;
    private Account mUser;
    private MenuItem cancelMenuItem, saveMenuItem, editMenuItem;

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
        Log.i(TAG, "onCreateView: reached");
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
        cancelMenuItem = menu.findItem(R.id.action_cancel);
        saveMenuItem = menu.findItem(R.id.action_save);
        editMenuItem = menu.findItem(R.id.action_edit);
        // initially set to hidden
        cancelMenuItem.setVisible(false);
        saveMenuItem.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditableView();
                break;
            case R.id.action_cancel:
                clearEditMode();
                break;
            case R.id.action_save:
                saveProfileEdits();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Save the Edits made by the user - call Server and update the local DB details
     */
    private void saveProfileEdits() {

    }

    /**
     * Toggles back to the default mode - view
     */
    private void clearEditMode() {
        btn_Save.setVisibility(View.INVISIBLE);
        // change the menu items
        cancelMenuItem.setVisible(false);
        saveMenuItem.setVisible(false);
        editMenuItem.setVisible(true);
    }

    /**
     * Toggles editable mode - view
     */
    private void showEditableView() {
        btn_Save.setVisibility(View.VISIBLE);
        // change the menu items
        cancelMenuItem.setVisible(true);
        saveMenuItem.setVisible(true);
        editMenuItem.setVisible(false);
    }

    private void editUserInformation() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("savedUser", mUser);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable("savedUser");
        }
    }
}
