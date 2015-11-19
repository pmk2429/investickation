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

import com.sfsu.adapters.TicksListAdapter;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.investickation.RecyclerItemClickListener;

import java.util.List;

/**
 * A simple {@link Tick} fragment which contains the Information; A Guide for each ticks. This fragment displays
 * a list of Ticks. Each ticks are displayed on a large thumbnail pic.
 */


public class TickGuideList extends Fragment {

    private final String LOGTAG = "~!@#$TickGuideList :";
    private IGuideIndexCallBacks mInterface;
    private Context mContext;
    private List<Tick> tickList;

    public TickGuideList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_index, container, false);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview_tickGuide);
        rv.setHasFixedSize(true);

        if (mContext != null) {
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            Log.d(LOGTAG, "Guide Activity not found");
        }

        //TODO: get the data passed by the Activity and pass it to Adapter
        //tickList = getArguments().getParcelableArrayList(AppUtils.TICK_LIST_KEY);

        tickList = Tick.initializeData();
        TicksListAdapter ticksListAdapter = new TicksListAdapter(tickList);
        rv.setAdapter(ticksListAdapter);


        // set on click listener for the item click of recyclerview
        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                mInterface.onGuideItemClick();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mInterface = (IGuideIndexCallBacks) activity;
            mContext = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGuideIndexListener");
        }
    }

    /**
     * Callback Interface to handle onClick Listeners in {@link TickGuideList} Fragment.
     */
    public interface IGuideIndexCallBacks {

        /**
         * Callback method to provide an interface to listen to data sent or button clicked in {@link TickGuideList} Fragment
         */
        public void onGuideItemClick();


    }

}
