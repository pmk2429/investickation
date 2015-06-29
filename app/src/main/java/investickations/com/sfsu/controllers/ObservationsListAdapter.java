package investickations.com.sfsu.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import investickations.com.sfsu.entities.Observation;
import investickations.com.sfsu.investickation.ObservationMainActivity;

/**
 * <p>
 * <tt>ObservationsListAdapter</tt> is a Custom Adapter which is used to display a Custom RowView
 * in the ObservationsList in Observations Fragment in the RecyclerView.
 * This Class extends RecyclerView.Adapter in order to create the custom view.
 * Since the RecyclerView needs LayoutManagaer, this class implements own layout manager for define the data
 * in the layout.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class ObservationsListAdapter extends RecyclerView.Adapter<ObservationsListAdapter.ObservationViewHolder> {


    public static class ObservationViewHolder extends RecyclerView.ViewHolder {


        public ObservationViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ObservationsListAdapter.ObservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ObservationsListAdapter.ObservationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
