package com.sfsu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sfsu.investickation.R;

/**
 * Created by Pavitra on 2/4/2016.
 */
public class PermissionUtils {
    // int codes for each permissions
    public static final int WIFI = 12;
    public static final int CAMERA = 13;
    public static final int WRITE = 14;
    public static final int READ = 15;
    public static final int NETWORK = 16;
    public static final int INTERNET = 17;
    public static final int FINE_LOCATION = 18;
    public static final int COARSE_LOCATION = 19;
    // keys for Preference file
    private static final String WIFI_PERMISSION = "wifi_permission";
    private static final String CAMERA_PERMISSION = "camera_permission";
    private static final String WRITE_PERMISSION = "write_permission";
    private static final String READ_PERMISSION = "read_permission";
    private static final String NETWORK_PERMISSION = "network_permission";
    private static final String INTERNET_PERMISSION = "internet_permission";
    private static final String FINE_LOCATION_PERMISSION = "fine_location_permission";
    private static final String COARSE_LOCATION_PERMISSION = "coarse_location_permission";
    // preferences
    private SharedPreferences mPermissionPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    public PermissionUtils(Context context) {
        this.mContext = context;
        mPermissionPreferences = mContext.getSharedPreferences(mContext.getString(R.string.perm_pref_file_name), Context
                .MODE_PRIVATE);
        mEditor = mPermissionPreferences.edit();
    }

    public SharedPreferences getPermissionPreferences() {
        return mPermissionPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

    public void setPermission(int requestCode) {
        switch (requestCode) {
            case WIFI:
                mEditor.putBoolean(WIFI_PERMISSION, true);
                break;
            case CAMERA:
                mEditor.putBoolean(CAMERA_PERMISSION, true);
                break;
            case WRITE:
                mEditor.putBoolean(WRITE_PERMISSION, true);
                break;
            case READ:
                mEditor.putBoolean(READ_PERMISSION, true);
                break;
            case NETWORK:
                mEditor.putBoolean(NETWORK_PERMISSION, true);
                break;
            case INTERNET:
                mEditor.putBoolean(INTERNET_PERMISSION, true);
                break;
            case FINE_LOCATION:
                mEditor.putBoolean(FINE_LOCATION_PERMISSION, true);
                break;
            case COARSE_LOCATION:
                mEditor.putBoolean(COARSE_LOCATION_PERMISSION, true);
                break;
        }
        mEditor.commit();
    }

    public void setPermission(int[] requestCodes) {
        for (int i = 0; i < requestCodes.length; i++) {
            int permissionCode = requestCodes[i];
            switch (permissionCode) {
                case WIFI:
                    mEditor.putBoolean(WIFI_PERMISSION, true);
                    break;
                case CAMERA:
                    mEditor.putBoolean(CAMERA_PERMISSION, true);
                    break;
                case WRITE:
                    mEditor.putBoolean(WRITE_PERMISSION, true);
                    break;
                case READ:
                    mEditor.putBoolean(READ_PERMISSION, true);
                    break;
                case NETWORK:
                    mEditor.putBoolean(NETWORK_PERMISSION, true);
                    break;
                case INTERNET:
                    mEditor.putBoolean(INTERNET_PERMISSION, true);
                    break;
                case FINE_LOCATION:
                    mEditor.putBoolean(FINE_LOCATION_PERMISSION, true);
                    break;
                case COARSE_LOCATION:
                    mEditor.putBoolean(COARSE_LOCATION_PERMISSION, true);
                    break;
            }
        }
        mEditor.commit();
    }

    public boolean isFineLocationPermissionApproved() {
        return mPermissionPreferences.getBoolean(FINE_LOCATION_PERMISSION, false);
    }

    public boolean isCoarseLocationPermissionApproved() {
        return mPermissionPreferences.getBoolean(COARSE_LOCATION_PERMISSION, false);
    }

    public boolean isWiFiPermissionApproved() {
        return mPermissionPreferences.getBoolean(WIFI_PERMISSION, false);
    }

    public boolean isInternetPermissionApproved() {
        return mPermissionPreferences.getBoolean(INTERNET_PERMISSION, false);
    }

    public boolean isWritePermissionApproved() {
        return mPermissionPreferences.getBoolean(WRITE_PERMISSION, false);
    }

    public boolean isReadPermissionApproved() {
        return mPermissionPreferences.getBoolean(READ_PERMISSION, false);
    }

    public boolean isCameraPermissionApproved() {
        return mPermissionPreferences.getBoolean(CAMERA_PERMISSION, false);
    }

    public boolean isNetworkPermissionApproved() {
        return mPermissionPreferences.getBoolean(NETWORK_PERMISSION, false);
    }

    /**
     * Clears the all the permissions from SharedPreferences
     *
     * @return
     */
    public boolean clearPermissions() {
        mEditor.clear();
        //editor.apply();

        return mEditor.commit();
    }

}
