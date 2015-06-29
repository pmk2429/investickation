package investickations.com.sfsu.controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import investickations.com.sfsu.entities.Activity;

/**
 * <p>
 * <tt>ActivitiesListAdapter</tt> is a Custom Adapter for displaying activities in the RecyclerView.
 * This provides a row item for each of the entry in the RecyclerView inflated using custom card view layout
 * ActivitiesListAdapter expects a custom layout as one of the parameter in the constructor.
 * Since the RecyclerView needs LayoutManagaer, this class implements own layout manager for define the data
 * in the layout.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class ActivitiesListAdapter extends RecyclerView.Adapter<ActivitiesListAdapter.ActivityViewHolder> {

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        public ActivityViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public ActivitiesListAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ActivitiesListAdapter.ActivityViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
