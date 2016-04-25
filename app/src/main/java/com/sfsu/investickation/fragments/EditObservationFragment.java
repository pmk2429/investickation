package com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.network.bus.BusProvider;
import com.sfsu.validation.TextValidator;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Edit the {@link com.sfsu.entities.Observation} posted by User.
 * <p>
 * <b>Reusing the layout<b> and as a result, the View reference ids are same as that of {@link AddObservationFragment}.
 * The Observation can be stored locally or on cloud and depending on its storage
 */

// TODO: this Fragment contains the boilerplate from AddObservationFragment so change it before publishing

public class EditObservationFragment extends Fragment implements View.OnClickListener {

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
    @Bind(R.id.button_editObs_updateObservation)
    Button btn_UpdateObservation;
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
    private Context mContext;

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

        et_tickName.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_tickName));
        //et_description.addTextChangedListener(new TextValidator(mContext, AddObservationFragment.this, et_tickSpecies));
        et_numOfTicks.addTextChangedListener(new TextValidator(mContext, EditObservationFragment.this, et_numOfTicks));
        // initialize the Floating button.
        final FloatingActionButton addTickImage = (FloatingActionButton) rootView.findViewById(R.id.fab_addObs_addTickImage);

        addTickImage.setOnClickListener(this);

        btn_chooseFromGuide.setText(getString(R.string.text_edit_referTickGuide));

        // populate all the views with the Observation object restored from Bundle
        if (mObservation != null) {
            Picasso.with(mContext).load(mObservation.getImageUrl()).into(imageView_tickAddObservation);
            et_tickName.setText(mObservation.getTickName());
            et_description.setText(mObservation.getDescription());
            et_numOfTicks.setText(mObservation.getNum_of_ticks());
        }


        btn_UpdateObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateObservation();
            }
        });

        return rootView;
    }

    /**
     * In order to edit the Observation, we are allowing user to change only <tt>image</tt>, <tt>name</tt>, <tt>description</tt>,
     * <tt>num_of_ticks</tt> and <tt>species</tt>.
     * <p>
     * So a check is done to know if the original Observation data and the new edited Observation data are same or not.
     * If they differ, then only update is called and data is updated.
     * </p>
     */
    private void updateObservation() {
        if (!mObservation.getTickName().equals(et_tickName.getText().toString())) {
            mObservation.setTickName(et_tickName.getText().toString());
        }
        if (mObservation.getNum_of_ticks() != Integer.parseInt(et_numOfTicks.getText().toString())) {
            mObservation.setNum_of_ticks(Integer.parseInt(et_numOfTicks.getText().toString()));
        }
        if (!mObservation.getDescription().equals(et_description.getText().toString())) {
            mObservation.setDescription(et_description.getText().toString());
        }
        

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IEditObservationCallbacks) activity;
            mContext = activity;
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

    @Override
    public void onClick(View v) {

    }


    public interface IEditObservationCallbacks {

    }

}
