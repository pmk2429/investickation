package com.sfsu.reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sfsu.investickation.R;

/**
 * AlertDialog class which provides methods to set up AlertDialog for different scenarios.
 * <p>
 */
public class AlertDialogMaster {

    private EditText et_setReminder_manualInput;
    private EditText et_changeReminder_manualInput;
    private Button btnHalfHour;
    private long REMINDER_INTERVAL;
    private boolean isHalfHourButtonClicked, isHourButtonClicked, isManualInputSet;
    private Context mContext;
    private IReminderCallback mInterface;
    private IReminderChangeCallBack mChangeInterface;


    public AlertDialogMaster(Context context, Fragment fragment) {
        try {
            this.mContext = context;
            mInterface = (IReminderCallback) fragment;
            mChangeInterface = (IReminderChangeCallBack) fragment;
        } catch (Exception e) {

        }
    }

    public void setupNewReminderDialog() {
        // initialize the value of each flags.
        isHalfHourButtonClicked = isHourButtonClicked = isManualInputSet = false;
        // setup Custom AlertDialog builder.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.alertdialog_reminder, null);
        alertDialog.setTitle("Set Reminder");

        // set the onClickListener for each buttons defined in the custom layout.
        et_setReminder_manualInput = (EditText) convertView.findViewById(R.id.editText_alertDialog_manualInput);
        btnHalfHour = (Button) convertView.findViewById(R.id.button_alertDialog_30);

        btnHalfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableView(btnHalfHour);
            }
        });

        // initialize and set et_setReminder_manualInput.
        et_setReminder_manualInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_setReminder_manualInput.getText().toString().equals("") && !et_setReminder_manualInput.getText().toString().equals(null)) {
                    enableView(et_setReminder_manualInput);
                }
            }
        });


        // on click of the Positive Button, set the textView_Reminder value for selected option.
        alertDialog.setPositiveButton(R.string.alertDialog_reminder_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // when the flags are set,
                if (isHalfHourButtonClicked) {
                    // get the total minutes of interval
                    REMINDER_INTERVAL = 30; // 30 minutes
                } else if (isManualInputSet) {
                    // get the total minutes of interval
                    REMINDER_INTERVAL = Long.parseLong(et_setReminder_manualInput.getText().toString());
                } else {
                    REMINDER_INTERVAL = -123;
                }
                // pass it to callback
                mInterface.setReminderValue(REMINDER_INTERVAL);
                // close the dialog.
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // finally display the alert dialog.
        alertDialog.setView(convertView);
        alertDialog.show();
    }

    /**
     * Method to toggle the color of the clicked button in Alert Dialog.
     *
     * @param mButton
     */
    private void toggleBackground(Button mButton) {
        int colorPrimary = mContext.getResources().getColor(R.color.colorPrimary);
        int colorSecondary = mContext.getResources().getColor(R.color.colorSecondary);

        ColorDrawable viewColor = (ColorDrawable) mButton.getBackground();
        int currentColor = viewColor.getColor();

        if (currentColor == colorPrimary) {
            mButton.setBackgroundColor(colorSecondary);
        } else {
            mButton.setBackgroundColor(colorPrimary);
        }
    }

    /**
     * Helper method to clear the focus of the View.
     *
     * @param v
     */
    private void clearFocusView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                btnHalfHour.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                break;

//            case R.id.button_alertDialog_60:
//                btnHour.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
//                break;

            case R.id.editText_alertDialog_manualInput:
                et_setReminder_manualInput.clearFocus();
                break;
        }
    }

    /**
     * Helper method to enable the view for reminder option
     *
     * @param v
     */
    private void enableView(View v) {
        switch (v.getId()) {
            case R.id.button_alertDialog_30:
                isHalfHourButtonClicked = true;
                //isHourButtonClicked = false;
                isManualInputSet = false;
                toggleBackground(btnHalfHour);
                //clearFocusView(btnHour);
                clearFocusView(et_setReminder_manualInput);
                break;

//            case R.id.button_alertDialog_60:
//                isHourButtonClicked = true;
//                isHalfHourButtonClicked = false;
//                isManualInputSet = false;
//                toggleBackground(btnHour);
//                clearFocusView(btnHalfHour);
//                clearFocusView(et_setReminder_manualInput);
//                break;

            case R.id.editText_alertDialog_manualInput:
                isManualInputSet = true;
                isHalfHourButtonClicked = false;
                //isHourButtonClicked = false;
                //clearFocusView(btnHour);
                clearFocusView(btnHalfHour);
                break;
        }
    }

    public void displayOngoingReminderStatusDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View convertView = inflater.inflate(R.layout.alertdialog_reminder_status, null);
        alertDialog.setTitle("Current reminder");
        alertDialog.setIcon(R.mipmap.ic_notifications_active_black_24dp);

        et_changeReminder_manualInput = (EditText) convertView.findViewById(R.id.editText_alertDialog_changeReminder);

        // initialize and set et_setReminder_manualInput.
        et_changeReminder_manualInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!et_changeReminder_manualInput.getText().toString().equals("") && !et_changeReminder_manualInput.getText().
                        toString().equals(null)) {
                    enableView(et_changeReminder_manualInput);
                }
            }
        });


        // on click of the Positive Button, set the textView_Reminder value for selected option.
        alertDialog.setPositiveButton(R.string.alertDialog_reminder_set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                REMINDER_INTERVAL = Long.parseLong(et_changeReminder_manualInput.getText().toString());

                // pass it to callback
                mChangeInterface.setReminderChangeValue(REMINDER_INTERVAL);
                // close the dialog.
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.alertDialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // finally display the alert dialog.
        alertDialog.setView(convertView);
        alertDialog.show();
    }

    /**
     * Callback interface for displaying the AlertDialog in {@link AlertDialogMaster}
     */
    public interface IReminderCallback {
        /**
         * Method to set the reminder value on user selection.
         *
         * @param reminderValue
         */
        void setReminderValue(long reminderValue);
    }

    public interface IReminderChangeCallBack {
        /**
         * Method to set the reminder value on user selection.
         *
         * @param reminderValue
         */
        void setReminderChangeValue(long reminderValue);
    }
}
