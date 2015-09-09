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

import investickations.com.sfsu.adapters.TicksListAdapter;
import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.entities.Tick;
import investickations.com.sfsu.investickation.R;
import investickations.com.sfsu.investickation.RecyclerItemClickListener;

/**
 * A simple {@link Tick} fragment which contains the Information; A Guide for each ticks. This fragment displays
 * a list of Ticks. Each ticks are displayed on a large thumbnail pic.
 */


public class GuideIndex extends Fragment implements View.OnClickListener {

    IGuideIndexCallBacks mInterface;
    private Context context;

    private List<Tick> ticks;

    public GuideIndex() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide_index, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.recyclerview_tickGuide);
        rv.setHasFixedSize(true);

        if (context != null) {
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            Log.d(AppConfig.LOGSTRING, "Guide Activity not found");
        }

        ticks = Tick.initializeData();
        TicksListAdapter ticksListAdapter = new TicksListAdapter(ticks);
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
            context = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IGuideIndexListener");
        }
    }

    @Override
    public void onClick(View v) {
        mInterface.onGuideItemClick();
    }

    // The container Activity must implement this interface so the frag can deliver messages
    public interface IGuideIndexCallBacks {

        /**
         * method to provide an interface to listen to data sent or button clicked in GuideIndex Fragment
         */
        public void onGuideItemClick();
    }

}
