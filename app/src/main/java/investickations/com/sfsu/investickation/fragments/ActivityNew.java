package investickations.com.sfsu.investickation.fragments;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Calendar;

import investickations.com.sfsu.investickation.R;


public class ActivityNew extends Fragment {
    GoogleMap googleMap;
    SupportMapFragment fragment;
    private Context mContext;
    private IActivityNewCallBack mInterface;
    private TextView txtView_setReminder;

    public ActivityNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Start New Activity");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_new, container, false);


        // set the Timer Reminder.
        txtView_setReminder = (TextView) v.findViewById(R.id.spinner_reminder);
        txtView_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtView_setReminder.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        final FloatingActionButton addProject = (FloatingActionButton) v.findViewById(R.id.fab_activity_start);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onPlayButtonclick();
            }
        });

        return v;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        // identify the Fragment container in the Fragment
//        FragmentManager fm = getChildFragmentManager();
//        fragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView_ActivityNew);
//        if (fragment == null) {
//            fragment = SupportMapFragment.newInstance();
//            fm.beginTransaction().replace(R.id.mapView_ActivityNew, fragment).commit();
//        }
//
//        // call the onMapReady callback
//        fragment.getMapAsync(this);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IActivityNewCallBack) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityNewCallBack");
        }
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap = googleMap;
//
//
//        if (null != googleMap) {
//            UiSettings mapSettings;
//            mapSettings = googleMap.getUiSettings();
//            mapSettings.setZoomControlsEnabled(true);
//
//            LatLng SanFrancisco = new LatLng(37.773972, -122.431297);
//            googleMap.addMarker(new MarkerOptions().position(SanFrancisco).title("").draggable(false)
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).anchor(0.0f, 1.0f));
//
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(SanFrancisco) // Sets the center of the map to
//                    .zoom(12)                   // Sets the zoom
//                    .bearing(0) // Sets the orientation of the camera to east
//                    .tilt(0)    // Sets the tilt of the camera to 30 degrees
//                    .build();    // Creates a CameraPosition from the builder
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
//    }

    public interface IActivityNewCallBack {
        public void onPlayButtonclick();
    }
}
