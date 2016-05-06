package com.sfsu.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.sfsu.investickation.R;

/**
 * Created by Pavitra on 1/18/2016.
 */
public class UploadAlertDialog {
    public static final long RESULT_OK = 0x1111;
    public static final long RESULT_INVALID = 0x2222;
    public static final long RESULT_NO_DATA = 0x3333;
    private Context mContext;
    private long result;
    private IUploadDataCallback mInterface;

    public UploadAlertDialog(Context mContext, Fragment fragment) {
        try {
            this.mContext = mContext;
            this.mInterface = (IUploadDataCallback) fragment;
        } catch (Exception e) {
        }
    }

    /**
     * Builds an Alert dialog for the total number of Activities, Observations in the local SQL database storage
     *
     * @param count
     * @return
     */
    public void showUploadAlertDialog(int count) {
        AlertDialog.Builder alarmReminderDialog = new AlertDialog.Builder(mContext);
        StringBuilder sb = new StringBuilder();
        if (count > 0) {
            alarmReminderDialog.setTitle(R.string.alertDialog_title_upload_activities);
            String activityText = count > 0 ? " Activities " : " Activity ";
            sb.append("Total ").append(count).append(activityText).append("found. Do you want to upload?");
            alarmReminderDialog.setMessage(sb.toString());
            alarmReminderDialog.setIcon(R.mipmap.ic_cloud_upload_black_24dp);

            alarmReminderDialog.setPositiveButton(R.string.alertDialog_upload, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_OK);
                }
            });

            alarmReminderDialog.setNegativeButton(R.string.alertDialog_later, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_INVALID);
                }
            });
        } else {
            alarmReminderDialog.setTitle(R.string.alertDialog_title_upload_activities);
            String noUploadText = "Yay! Your data is in sync with server";
            alarmReminderDialog.setMessage(noUploadText);
            alarmReminderDialog.setIcon(R.mipmap.ic_cloud_upload_black_24dp);

            alarmReminderDialog.setPositiveButton(R.string.alertDialog_OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_NO_DATA);
                }
            });
        }
        alarmReminderDialog.show();

    }

    /**
     * Builds an Alert dialog for the total number of Activities, Observations in the local SQL database storage
     *
     * @param count
     * @return
     */
    public void showObservationListUploadAlertDialog(int count) {
        AlertDialog.Builder alarmReminderDialog = new AlertDialog.Builder(mContext);
        StringBuilder sb = new StringBuilder();
        if (count > 0) {
            alarmReminderDialog.setTitle(R.string.alertDialog_title_upload_observations);
            String activityText = count > 0 ? " Observations " : " Observation ";
            sb.append("Total ").append(count).append(activityText).append("found. Do you want to upload?");
            alarmReminderDialog.setMessage(sb.toString());
            alarmReminderDialog.setIcon(R.mipmap.ic_cloud_upload_black_24dp);

            alarmReminderDialog.setPositiveButton(R.string.alertDialog_upload, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_OK);
                }
            });

            alarmReminderDialog.setNegativeButton(R.string.alertDialog_later, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_INVALID);
                }
            });
        } else {
            alarmReminderDialog.setTitle(R.string.alertDialog_title_upload_activities);
            String noUploadText = "Yay! Your data is in sync with server";
            alarmReminderDialog.setMessage(noUploadText);
            alarmReminderDialog.setIcon(R.mipmap.ic_cloud_upload_black_24dp);

            alarmReminderDialog.setPositiveButton(R.string.alertDialog_OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mInterface.onUploadClick(RESULT_NO_DATA);
                }
            });
        }
        alarmReminderDialog.show();

    }

    /**
     * Builds an Alert dialog for the total number of Activities, Observations in the local SQL database storage
     *
     * @param count
     * @return
     */
    public void showObservationUploadAlertDialog() {
        AlertDialog.Builder alarmReminderDialog = new AlertDialog.Builder(mContext);
        StringBuilder sb = new StringBuilder();
        alarmReminderDialog.setTitle(R.string.alertDialog_title_upload_observations);
        sb.append("Are you sure you want to upload the Observation?");
        alarmReminderDialog.setMessage(sb.toString());
        alarmReminderDialog.setIcon(R.mipmap.ic_cloud_upload_black_24dp);

        alarmReminderDialog.setPositiveButton(R.string.alertDialog_upload, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mInterface.onUploadClick(RESULT_OK);
            }
        });

        alarmReminderDialog.setNegativeButton(R.string.alertDialog_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mInterface.onUploadClick(RESULT_INVALID);
            }
        });
        alarmReminderDialog.show();

    }

    public interface IUploadDataCallback {
        void onUploadClick(long resultCode);
    }
}
