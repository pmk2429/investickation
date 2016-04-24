package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Edit the {@link com.sfsu.entities.Observation} posted by User.
 * <p>
 * <b>Reusing the layout<b> and as a result, the View reference ids are same as that of {@link AddObservationFragment}
 */
public class EditObservationFragment extends Fragment {

    protected static final int CAMERA_PICTURE = 12;
    protected static final int GALLERY_PICTURE = 24;
    private static final String KEY_EDIT_OBSERVATION = "edit_observation";
    private static final String JPEG_FILE_PREFIX = "TICK_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int GALLERY_CAMERA_PERMISSION = 24;
    private final String LOGTAG = "~!@#EditObsFrag:";
    private final String TAG = "~!@#$AddObservation";
    // ImageView
    @Bind(R.id.imageView_addObs_tickImage)
    ImageView imageView_tickAddObservation;
    // Button
    @Bind(R.id.button_addObs_postObservation)
    Button btn_PostObservation;
    @Bind(R.id.button_addObs_chooseFromGuide)
    Button btn_chooseFromGuide;
    // EditTexts
    @Bind(R.id.editText_addObs_numOfTicks)
    EditText et_numOfTicks;
    @Bind(R.id.editText_addObs_tickName)
    EditText et_tickName;
    @Bind(R.id.editText_addObs_description)
    EditText et_description;
    private IEditObservationCallbacks mInterface;
    private Observation mObservation;

    public EditObservationFragment() {
        // Required empty public constructor
    }

    public static EditObservationFragment newInstance(Observation mObservation) {
        EditObservationFragment fragment = new EditObservationFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_EDIT_OBSERVATION, mObservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_observation_edit);
        if (getArguments() != null) {
            mObservation = getArguments().getParcelable(KEY_EDIT_OBSERVATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_observation, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (IDataReceiverCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IEditObservationCallbacks");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface IEditObservationCallbacks {

    }

}
