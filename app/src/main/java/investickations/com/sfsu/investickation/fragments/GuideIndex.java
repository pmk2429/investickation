package investickations.com.sfsu.investickation.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */


// TODO: Change the name of Interface to something more relevant and appropriate.
// TODO: Change the name of method to make it appropriate to Item click listener

public class GuideIndex extends Fragment implements View.OnClickListener {

    IGuideIndexListener mInterface;

    public GuideIndex() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_index, container, false);
        Button btnTest = (Button) v.findViewById(R.id.btnTest);

        if (btnTest != null) {
            btnTest.setOnClickListener(this);
        } else {
            Log.d(AppConfig.LOGSTRING, "Button not found");
        }
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            mInterface = (IGuideIndexListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGuideIndexListener");
        }
    }

    @Override
    public void onClick(View v) {
        mInterface.onItemClick();
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface IGuideIndexListener {

        /**
         * method to provide an interface to listen to data sent or button clicked in GuideIndex Fragment
         */
        public void onItemClick();
    }

}
