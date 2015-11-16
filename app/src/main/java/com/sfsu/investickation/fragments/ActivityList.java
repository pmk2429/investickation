package com.sfsu.investickation.fragments;

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
import android.view.WindowManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sfsu.adapters.ActivitiesListAdapter;
import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;

import java.util.List;


public class ActivityList extends Fragment implements View.OnClickListener {

    private final String LOGTAG = "~!@#ActivityList :";
    private IActivityCallBacks mInterface;
    private Context context;
    private List<Activities> serverActivitiesList;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Activities");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_list, container, false);

        RecyclerView recyclerView_activity = (RecyclerView) v.findViewById(R.id.recyclerview_activity_list);
        recyclerView_activity.setHasFixedSize(true);

        if (context != null) {
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView_activity.setLayoutManager(mLinearLayoutManager);

            // retrieve the list of serverActivitiesList passed from the UserActivityMainActivity
            if (getArguments() != null) {

//              TODO:  serverActivitiesList = getArguments().getParcelableArrayList(AppUtils.ACTIVITY_KEY);
            }
        } else {
            Log.d(LOGTAG, " No layout manager supplied");
        }

        // TODO: temporary method to get the data and display it in ListView.
        serverActivitiesList = Activities.initializeData();

        if (serverActivitiesList.size() > 0) {
            // set the List of Acvities to Adapter.
            ActivitiesListAdapter adapter = new ActivitiesListAdapter(serverActivitiesList);
            recyclerView_activity.setAdapter(adapter);

            // touch listener when the user clicks on the Activity in the List.
            recyclerView_activity.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_activity, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // call the interface callback to listen to the item click event

                    mInterface.onItemClickListener();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            }));
        } else {
            // TODO: display a message saying that no records found. and a sad emoticon
        }

        // Add new Activity button.
        final FloatingActionButton addProject = (FloatingActionButton) v.findViewById(R.id.fab_activity_add);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onActivityAddListener();
            }
        });

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
        mInterface.onItemClickListener();
    }


    /**
     * Callback interface to handle callback methods for the ActivityList fragment.
     */
    public interface IActivityCallBacks {
        /**
         * Callback method to handle the Item click event of ActivityList.
         */
        public void onItemClickListener();

        /**
         * Callback method to handle the click event of the Add Button in ActivityList Fragment.
         */
        public void onActivityAddListener();
    }

}
