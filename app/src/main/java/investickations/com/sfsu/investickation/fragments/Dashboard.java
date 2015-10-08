package investickations.com.sfsu.investickation.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import investickations.com.sfsu.investickation.R;


public class Dashboard extends Fragment implements View.OnClickListener {

    private IDashboardCallback mListener;
    private CardView btn_action;
    private RelativeLayout relativeLayoutDashboard;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        btn_action = (CardView) v.findViewById(R.id.btn_activity_start);
        btn_action.setOnClickListener(this);

        btn_action = (CardView) v.findViewById(R.id.btn_observation_post);
        btn_action.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_observation);
        relativeLayoutDashboard.setOnClickListener(this);

        relativeLayoutDashboard = (RelativeLayout) v.findViewById(R.id.relativeLayout_activity);
        relativeLayoutDashboard.setOnClickListener(this);

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IDashboardCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDashboardCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_activity_start) {
            mListener.onActivityButtonClicked();
        } else if (v.getId() == R.id.btn_observation_post) {
            mListener.onObservationButtonClicked();
        } else if (v.getId() == R.id.relativeLayout_observation) {
            mListener.onViewObservationsClicked();
        } else if (v.getId() == R.id.relativeLayout_activity) {
            mListener.onViewActivitiesClicked();
        }
    }


    public interface IDashboardCallback {
        public void onDashboardInteraction(Uri uri);

        public void onActivityButtonClicked();

        public void onObservationButtonClicked();

        public void onViewActivitiesClicked();

        public void onViewObservationsClicked();
    }

}
