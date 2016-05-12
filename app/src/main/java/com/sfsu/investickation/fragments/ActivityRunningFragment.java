package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.controllers.GoogleMapController;
import com.sfsu.controllers.LocationController;
import com.sfsu.db.ActivitiesDao;
import com.sfsu.dialogs.AlertDialogMaster;
import com.sfsu.dialogs.AlertDialogMaster.IReminderChangeCallBack;
import com.sfsu.entities.Account;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.map.StaticMap;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.network.events.ActivityEvent;
import com.sfsu.network.handler.ApiRequestHandler;
import com.sfsu.service.LocationService;
import com.sfsu.service.PeriodicAlarm;
import com.sfsu.utils.AppUtils;
import com.squareup.otto.Subscribe;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Provides interface to user to add {@link com.sfsu.entities.Observation} for the current ongoing
 * {@link Activities}. At this point in time, the {@link Activities} is created on the server and the state of Activity is
 * <tt>RUNNING</tt>. So only when the Activity is in RUNNING state, the {@link Account} can make observations
 * or receive location updates. This fragment the action callback to start new <tt>Observation</tt>.
 * Once the Add Observation button is clicked, the user will be redirected to add Observation for the current ongoing activity.
 * <p>
 * The object passed by the UserActivityMasterActivity from ActivityNewFragment fragment will be in RUNNING state. Hence, in Running
 * state, the Activity performs various operations such as getting Location updates, getting the updates from the
 * BroadcastReceiver, recording the observations etc.
 * </p>
 * <p>
 * All these operations will be carried out when the Activity is in RUNNING state ONLY.
 * </p>
 * <p>Once the user clicks on <tt>'Add Observation'</tt> button, the {@link AddObservationFragment} Fragment is opened and
 * the control is redirected to new Fragment inside {@link com.sfsu.investickation.ObservationMasterActivity}. When this happens,
 * the current ongoing activity is stored in SharedPreferences and retrieved when the user returns back to the same Fragment.</p>
 * Observation,
 */
public class ActivityRunningFragment extends Fragment implements LocationController.ILocationCallBacks, View.OnClickListener,
        AlertDialogMaster.IReminderCallback, IReminderChangeCallBack {

    public static final String TAG = "~!@#$ActivityRunning";
    private static final String KEY_LAT_LNG = "lat_lng_json";
    // FAB
    @Bind(R.id.fab_actRun_activityStop)
    FloatingActionButton fab_stopActivity;
    @Bind(R.id.fab_actRun_reminder)
    FloatingActionButton fab_reminder;
    // Map
    @Bind(R.id.mapView_activityRunning)
    MapView mapView;
    // CardView
    @Bind(R.id.cardView_activityRunning_addObservation)
    CardView btn_addObservation;
    // TextView
    @Bind(R.id.textView_actRun_activityName)
    TextView txtView_activityName;
    // properties
    private LatLng[] mLatLngArray;
    private Activities ongoingActivityObj;
    private Context mContext;
    private IActivityRunningCallBacks mListener;
    private GoogleMapController mGoogleMapController;
    private DatabaseDataController dbController;
    private Bundle activityBundle;
    private boolean FLAG_RUNNING, FLAG_IS_TIMER_SET;
    private long REMINDER_INTERVAL;
    private String reminderText;
    private SharedPreferences activityPref;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Set<LatLng> mLatLngSet = new HashSet<>();
    private Intent locationIntent;
    private AlertDialogMaster alertDialogMaster;
    private PeriodicAlarm mPeriodicAlarm;
    private ProgressDialog mProgressDialog;
    /**
     * BroadcastReceiver to receive the updates when Location is changed.
     */
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            collectLocationData(intent);
        }
    };

    /**
     * BroadcastReceiver to receive the updates when the Alarm goes off
     */
    private BroadcastReceiver alarmBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WAKING CLOCK");
            //Acquire the lock
            mWakeLock.acquire();

            Bundle intentExtras = intent.getExtras();

            if (intentExtras != null && intentExtras.getBoolean(PeriodicAlarm.ONE_TIME, Boolean.FALSE)) {
                // timer for one time
            }

            // open alert dialog
            //openAlarmDialog();

            // set notification
            setNotification();

            //Release the lock
            mWakeLock.release();
        }
    };


    public ActivityRunningFragment() {
        // Required empty public constructor
    }

    /**
     * Returns the instance of {@link ActivityRunningFragment} fragment with bundle.
     *
     * @param key
     * @param mActivity
     * @return
     */
    public static ActivityRunningFragment newInstance(Bundle activityBundle) {
        ActivityRunningFragment mActivityRunningFragment = new ActivityRunningFragment();
        if (activityBundle != null) {
            mActivityRunningFragment.setArguments(activityBundle);
        }
        return mActivityRunningFragment;
    }

    private void collectLocationData(Intent intent) {
        try {
            Location mLocation = intent.getParcelableExtra(LocationService.KEY_LOCATION_CHANGED);
            if (mLocation != null) {
                LatLng mLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                mLatLngSet.add(mLatLng);
            }
            if (mLatLngSet != null && mLatLngSet.size() > 0) {
                if (mLatLngSet.size() > 0) {
                    for (LatLng latLng : mLatLngSet) {
                        Log.i(TAG, latLng.latitude + " : " + latLng.longitude);
                    }
                }
            } else {
                Log.i(TAG, "array of LatLngs not updated");
            }
        } catch (Exception e) {

        }
    }

    /**
     * Open AlertDialog when the Alarm goes off. Every time the AlarmManager ticks, the alarm will be opened. This will allow
     * user for making Observation depending on the User choice. If User cancels the, then the Activity will continue.
     */
    private void openAlarmDialog() {

        AlertDialog.Builder alarmReminderDialog = new AlertDialog.Builder(mContext);
        alarmReminderDialog.setTitle(R.string.alertDialog_alert_title);
        alarmReminderDialog.setMessage(R.string.alertDialog_alert_message);
        alarmReminderDialog.setIcon(R.mipmap.ic_bug_report_black_24dp);

        alarmReminderDialog.setPositiveButton(R.string.alertDialog_open_observation, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onAddNewObservationClicked(ongoingActivityObj.getId());
            }
        });

        alarmReminderDialog.setNegativeButton(R.string.alertDialog_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alarmReminderDialog.setNeutralButton(R.string.alertDialog_stop_reminder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopReminder();
            }
        });

        alarmReminderDialog.show();
    }

    /**
     * Helper to set Notification when the alarm goes off
     */
    private void setNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_bug_report_white_36dp);
        mBuilder.setContentTitle("Found a Tick?");
        mBuilder.setContentText("Click here to make an Observation of encountered Tick!");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

        Intent resultIntent = new Intent(mContext, ObservationMasterActivity.class);
        // put the extras in addObservationIntent to perform fragment Transaction efficiently.
        resultIntent.putExtra(UserActivityMasterActivity.KEY_ACTIVITY_ADD_OBS, 1);
        resultIntent.putExtra(UserActivityMasterActivity.KEY_ACTIVITY_ID, ongoingActivityObj.getId());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(ObservationMasterActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);


        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(12345, mBuilder.build());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IActivityRunningCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityRunningCallBacks interface");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_running, container, false);
        // bind the views
        ButterKnife.bind(this, rootView);

        alertDialogMaster = new AlertDialogMaster(mContext, this);
        mPeriodicAlarm = new PeriodicAlarm(mContext);
        dbController = new DatabaseDataController(mContext, ActivitiesDao.getInstance());
        gson = new Gson();

        try {
            if (getArguments() != null) {
                activityBundle = getArguments();
            }
            activityPref = mContext.getSharedPreferences(UserActivityMasterActivity.PREF_ACTIVITY_DATA, Context.MODE_PRIVATE);

            // in times of changing the Orientation of Screen, we have to get the MapView from savedInstanceState
            final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
            mapView.onCreate(mapViewSavedInstanceState);

        } catch (Exception e) {

        }
        return rootView;
    }

    // initialize the time consuming tasks after View is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize all controllers and services.
        mGoogleMapController = new GoogleMapController(mContext);

        // setup google Map.
        mGoogleMapController.setupGoogleMap(mapView);
    }

    /**
     * Persists the Data for the running activity
     */
    private void persistCurrentActivity() {
        try {
            editor = activityPref.edit();
            String activityJson = gson.toJson(ongoingActivityObj);
            editor.putString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, activityJson);
            if (mLatLngSet != null && mLatLngSet.size() > 0) {
                Set<String> latLngString = new HashSet<>();
                Iterator<LatLng> iterator = mLatLngSet.iterator();
                while (iterator.hasNext()) {
                    LatLng temp = iterator.next();
                    String value = Double.toString(temp.latitude) + "," + Double.toString(temp.longitude);
                    latLngString.add(value);
                }
                editor.putStringSet(KEY_LAT_LNG, latLngString);
            }
            editor.apply();
        } catch (Exception e) {

        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_activity_running);
        mProgressDialog = new ProgressDialog(mContext);

        try {
            // retrieve the data from Arguments if the Fragment is opened for first time, or from the SharedPreferences.
            if (activityBundle != null) {
                if (activityBundle.getParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT) != null) {
                    ongoingActivityObj = activityBundle.getParcelable(UserActivityMasterActivity.KEY_NEW_ACTIVITY_OBJECT);
                    FLAG_IS_TIMER_SET = activityBundle.getBoolean(UserActivityMasterActivity.KEY_REMINDER_SET);
                    REMINDER_INTERVAL = activityBundle.getLong(UserActivityMasterActivity.KEY_REMINDER_INTERVAL);
                }
            } else {
                // get data from SharedPref which was persisted before
                String activityJson = activityPref.getString(UserActivityMasterActivity.EDITOR_ONGOING_ACTIVITY, null);
                ongoingActivityObj = gson.fromJson(activityJson, Activities.class);
                Set<String> latLngStringSet = activityPref.getStringSet(KEY_LAT_LNG, null);
                if (latLngStringSet != null) {
                    Set<String> temp = latLngStringSet;
                    // build HashSet of LatLng again
                    Iterator<String> iter = temp.iterator();
                    while (iter.hasNext()) {
                        String val = iter.next();
                        String[] latLngVals = val.split(",");
                        mLatLngSet.add(new LatLng(Double.parseDouble(latLngVals[0]), Double.parseDouble(latLngVals[1])));
                    }
                } else {
                    Log.i(TAG, "null");
                }

                // finally add LatLng to Set
                if (mLatLngSet != null) {
                    Iterator<LatLng> latLngIterator = mLatLngSet.iterator();
                    while (latLngIterator.hasNext()) {
                        LatLng mLatLng = latLngIterator.next();
                    }
                }
            }

            // check if Activities object is not null.
            // IMP FLAG must always be true in onResume when Activity is RUNNING
            if (ongoingActivityObj != null) {
                populateView();
                FLAG_RUNNING = ongoingActivityObj.getState() == Activities.STATE.RUNNING;
            }

            // perform check for RUNNING state of Activity and if true, register receiver for getting location updates.
            if (FLAG_RUNNING) {
                locationIntent = new Intent(mContext, LocationService.class);
                // start the service to capture Location updates.
                mContext.startService(locationIntent);

                // register for BroadcastReceiver
                mContext.registerReceiver(locationReceiver, new IntentFilter(LocationService.BROADCAST_ACTION));

            }

            // depending on whether the timer is set or not, display the corresponding icon.
            if (FLAG_IS_TIMER_SET) {
                fab_reminder.setIcon(R.mipmap.ic_notifications_active_white_24dp);
            } else {
                fab_reminder.setIcon(R.mipmap.ic_notifications_white_24dp);
            }

            // register AlarmReceiver
            mContext.registerReceiver(alarmBroadcastReceiver, new IntentFilter(PeriodicAlarm.BROADCAST_ACTION));

            // register for Event Bus
            BusProvider.bus().register(this);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }


    /**
     * Helper method to populate the View in this Fragment.
     */
    private void populateView() {
        ongoingActivityObj.setState(Activities.STATE.RUNNING);
        // when the newActivityObject is retrieved from the Intent, create a StringBuilder and set the text to TextView
        StringBuilder textViewData = new StringBuilder();
        if (AppUtils.isConnectedOnline(mContext)) {
            if (!ongoingActivityObj.getLocation_area().equals("") && !ongoingActivityObj.getLocation_area().equals(null))
                textViewData.append(ongoingActivityObj.getActivityName() + " @ " + ongoingActivityObj.getLocation_area());
        } else {
            textViewData.append(ongoingActivityObj.getActivityName());
        }
        txtView_activityName.setText(textViewData.toString());

        // open alarm dialog for reminder depending on whether its set or not
        fab_reminder.setOnClickListener(this);

        // Open the New Observation Fragment in button click and pass the activityId to make Activity related Observations.
        btn_addObservation.setOnClickListener(this);

        // remove the SharedPreferences and set state of the ongoing activity to CREATED.
        fab_stopActivity.setOnClickListener(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // store the information on map.
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);

        // in case of out of memory scenario
        persistCurrentActivity();

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();

        // in case user opens {@link AddObservationFragment} fragment or closes the screen
        persistCurrentActivity();

        // unregister the Bus.
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mapView.onDestroy();
            mGoogleMapController.clear();
            mLatLngSet = null;
            // when the fragment is destroyed, kill off Location Service and AlarmManager
            mContext.unregisterReceiver(locationReceiver);
            mContext.stopService(locationIntent);
            mContext.unregisterReceiver(alarmBroadcastReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView_activityRunning_addObservation:
                mListener.onAddNewObservationClicked(ongoingActivityObj.getId());
                break;

            case R.id.fab_actRun_activityStop:
                clearResources();
                updateRunningActivity();
                break;

            case R.id.fab_actRun_reminder:
                openReminderDialog();
                break;
        }
    }


    @Override
    public void setCurrentLocation(Location mLocation) {

    }

    @Override
    public void setLatLng(LatLng mLatLng) {
        if (mLatLng != null) {
            mLatLngSet.add(mLatLng);
        }
    }

    @Override
    public void setLocationArea(String locationArea) {

    }

    /**
     * If the Alarm is already set, open the status dialog, else open dialog to set Reminder.
     */
    private void openReminderDialog() {
        if (FLAG_IS_TIMER_SET) {
            openReminderStatusDialog(REMINDER_INTERVAL);
        } else {
            setupReminderDialog();
        }
    }

    /**
     * Helper method to open AlertDialog when the user clicks on the reminder.
     */
    private void setupReminderDialog() {
        alertDialogMaster.setupNewReminderDialog();
    }


    private void openReminderStatusDialog(long reminderValue) {
        alertDialogMaster.displayOngoingReminderStatusDialog(reminderValue);
    }

    @Override
    public void setReminderValue(long reminderValue) {
        try {
            REMINDER_INTERVAL = reminderValue;

            if (REMINDER_INTERVAL != 0) {

                FLAG_IS_TIMER_SET = true;
                fab_reminder.setIcon(R.mipmap.ic_notifications_active_white_24dp);

                // start the Alarm Reminder.
                startReminder();
            } else {
                FLAG_IS_TIMER_SET = false;
                fab_reminder.setIcon(R.mipmap.ic_notifications_white_24dp);
            }
        } catch (NullPointerException ne) {
        }
    }

    @Override
    public void setReminderChangeValue(long reminderValue) {
        try {
            REMINDER_INTERVAL = reminderValue;

            if (REMINDER_INTERVAL != 0 && REMINDER_INTERVAL != -123) {

                // if user stops the reminder
                if (REMINDER_INTERVAL == AlertDialogMaster.STOP_REMINDER) {
                    stopReminder();
                } else {

                    FLAG_IS_TIMER_SET = true;
                    fab_reminder.setIcon(R.mipmap.ic_notifications_active_white_24dp);

                    // start the Alarm Reminder.
                    startReminder();
                }
            } else {
                FLAG_IS_TIMER_SET = false;
                fab_reminder.setIcon(R.mipmap.ic_notifications_white_24dp);
            }
        } catch (NullPointerException ne) {
        }


    }

    /**
     * Helper method to set the reminder depending on User's choice
     */
    private void startReminder() {
        // cancel previous timer
        if (FLAG_IS_TIMER_SET) {
            // if reminder is already running, cancel the current reminder
            mPeriodicAlarm.cancelAlarm();
        }
        mPeriodicAlarm.setAlarm(REMINDER_INTERVAL);
    }

    /**
     * Stop the reminder
     */
    private void stopReminder() {
        REMINDER_INTERVAL = 0;
        FLAG_IS_TIMER_SET = false;
        mPeriodicAlarm.cancelAlarm();
        fab_reminder.setIcon(R.mipmap.ic_notifications_white_24dp);
    }


    /**
     * Clear and Free all the resources
     */
    private void clearResources() {
        // delete the SharedPref data
        activityPref.edit().remove(UserActivityMasterActivity.PREF_ACTIVITY_DATA).apply();
        // stop timer is the flag is set
        // stop the Service
        mContext.stopService(locationIntent);
        mContext.unregisterReceiver(locationReceiver);
        // unregister alarm
        mContext.unregisterReceiver(alarmBroadcastReceiver);
        mPeriodicAlarm.cancelAlarm();

        FLAG_RUNNING = false;

        if (!FLAG_IS_TIMER_SET)
            mPeriodicAlarm.cancelAlarm();
    }

    @Override
    public void onStop() {
        super.onStop();
        dbController.closeConnection();
    }

    /**
     * Helper method to UPDATE the ongoing Activity for Location updates and adding LatLng.
     */
    private void updateRunningActivity() {
        try {
            // onStop button click, change the state of Activity to CREATED.
            ongoingActivityObj.setState(Activities.STATE.CREATED);

            FLAG_RUNNING = false;

            if (mLatLngSet != null) {
                Iterator<LatLng> latLngs = mLatLngSet.iterator();
                while (latLngs.hasNext()) {
                    LatLng temp = latLngs.next();
                    Log.i(TAG, temp.latitude + " : " + temp.longitude);
                }
                mLatLngArray = mLatLngSet.toArray(new LatLng[mLatLngSet.size()]);
            }

            // build the imageUrl
            String imageUrl = new StaticMap
                    .UrlBuilder()
                    .init()
                    .zoom(14)
                    .size(650, 350)
                    .path(mLatLngArray)
                    .build();

            // set image_url and update the Activity.
            ongoingActivityObj.setImage_url(imageUrl);

            // depending on whether the Network is available or not, perform onClick operation
            if (AppUtils.isConnectedOnline(mContext)) {
                // Update the Activities object on the Server
                BusProvider.bus().post(new ActivityEvent.OnLoadingInitialized(ongoingActivityObj, ongoingActivityObj.getId(),
                        ApiRequestHandler.UPDATE));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating Activity...");
                mProgressDialog.show();
            } else {
                // update the Activity in the Database
                boolean updated = dbController.update(ongoingActivityObj.getId(), ongoingActivityObj);
                mListener.onActivityStopButtonClicked();
            }

        } catch (Exception e) {

        }
    }

    /**
     * Subscribes to the event of successful {@link Activities} update
     *
     * @param onLoaded
     */
    @Subscribe
    public void onActivityUpdateSuccess(ActivityEvent.OnLoaded onLoaded) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mListener.onActivityStopButtonClicked();
    }

    @Subscribe
    public void onActivityUpdateFailure(ActivityEvent.OnLoadingError onLoadingError) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        //Toast.makeText(mContext, onLoadingError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    /**
     * Callback Interface for handling onClick Listeners in <tt>ActivityRunningFragment</tt> Fragment.
     */
    public interface IActivityRunningCallBacks {
        /**
         * Callback method when the user clicks on the Stop button in {@link ActivityRunningFragment} Fragment. OnClick of this button
         * will remove the SharedPreferences file which contains the Activities Object.
         * <p>The state of {@link Activities} is changed from <tt>RUNNING</tt> to <tt>CREATED</tt> and as a result the the
         * LocationController is stopped</p>
         *
         * @param currentActivityObj Current Ongoing Activity Object passed from the ActivityNewFragment
         */
        public void onActivityStopButtonClicked();


        /**
         * Callback method to handle the new AddObservationFragment button click in <tt>ActivityNewFragment</tt> Fragment. This method takes
         * in the unique UUID of the Activity currently ongoing to determing that the Observation is under a specific activity.
         *
         * @param activityId
         */
        public void onAddNewObservationClicked(String activityId);
    }

}
