package com.sfsu.investickation.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sfsu.network.events.UserEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.utils.Preconditions;
import com.squareup.otto.Subscribe;

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
    @Bind(R.id.editText_profile_city)
    EditText et_city;
    @Bind(R.id.editText_profile_state)
    EditText et_state;
    @Bind(R.id.editText_profile_zip)
    EditText et_zip;
    @Bind(R.id.button_profile_save)
    Button btn_Save;
    private AuthPreferences mAuthPreferences;
    private DatabaseDataController dbController;
    private Context mContext;
    private Account mUser;
    private MenuItem cancelMenuItem, editMenuItem;
    private ProgressDialog mProgressDialog;

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

        mUser = (Account) dbController.get(mAuthPreferences.getUser_id());
        populateViews();

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
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_personal_info, menu);
        cancelMenuItem = menu.findItem(R.id.action_cancel);
        editMenuItem = menu.findItem(R.id.action_edit);
        // initially set to hidden
        cancelMenuItem.setVisible(false);

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
        editMenuItem.setVisible(true);
    }

    /**
     * Toggles editable mode - view
     */
    private void showEditableView() {
        btn_Save.setVisibility(View.VISIBLE);
        // change the menu items
        cancelMenuItem.setVisible(true);
        editMenuItem.setVisible(false);
    }

    /**
     * Update User details on server and once the update is successful, update in the local DB
     */
    private void editUserInformation() {
        BusProvider.bus().post(new UserEvent.OnLoadingInitialized(mUser, mUser.getId(), ApiRequestHandler.UPDATE));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.progressDialog_updating_user_details));
        mProgressDialog.show();
    }

    @Subscribe
    public void onUserDetailsUpdatedSuccess(UserEvent.OnLoaded onLoaded) {
        mProgressDialog = Preconditions.checkNotNull(mProgressDialog);
        mProgressDialog.dismiss();

        // retrieve the user and update the same user in the database
        mUser = onLoaded.getResponse();

        Thread updateUserThread = new Thread(new Runnable() {
            private final Handler myHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                }
            };

            private void passMessageToHandler() {

            }

            @Override
            public void run() {

            }
        });

        populateViews();
    }

    private void populateViews() {
        try {
            et_fullName.setText(mUser.getFull_name());
            et_address.setText(mUser.getAddress());
            et_email.setText(mUser.getEmail());
            et_city.setText(mUser.getCity());
            et_state.setText(mUser.getState());
            et_zip.setText(mUser.getZipCode());
        } catch (NullPointerException ne) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "populateViews ", ne);
        }
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
