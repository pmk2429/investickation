package investickations.com.sfsu.investickation.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import investickations.com.sfsu.controllers.ActivitiesListAdapter;
import investickations.com.sfsu.entities.Activities;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.investickation.R;


// TODO: Change the name of Interface to something more relevant and appropriate.
// TODO: Change the name of method to make it appropriate to Item click listener

public class ActivityList extends Fragment implements View.OnClickListener {

    private IActivityCallBacks mInterface;
    private Context context;

    private List<Activities> activities;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_list, container, false);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview_activity_list);
        rv.setHasFixedSize(true);

        if (context != null) {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
        } else {
            Log.d(AppConfig.LOGSTRING, " No layout manager supplied");
        }
        activities = Activities.initializeData();

        ActivitiesListAdapter adapter = new ActivitiesListAdapter(activities);
        rv.setAdapter(adapter);
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IActivityCallBacks) activity;
            context = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IActivityInteractionListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    @Override
    public void onClick(View v) {
        mInterface.onFragmentInteraction();
    }


    public interface IActivityCallBacks {
        public void onFragmentInteraction();
    }

}
