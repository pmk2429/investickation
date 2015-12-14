package com.sfsu.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;

import java.util.List;

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

    private List<Activities> activityList;

    public ActivitiesListAdapter(List<Activities> activityList) {
        this.activityList = activityList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // creates a ViewHolder
    @Override
    public ActivitiesListAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_activities, null);
        ActivityViewHolder activityViewHolder = new ActivityViewHolder(v);
        return activityViewHolder;
    }

    @Override
    public void onBindViewHolder(ActivitiesListAdapter.ActivityViewHolder holder, int position) {
        if (holder != null) {
            String activityName = activityList.get(position).getActivityName() + " @ " + activityList.get(position)
                    .getLocation_area();
            holder.txtViewActivityName.setText(activityName);
            String pets = activityList.get(position).getNum_people() + " pets";
            holder.txtViewDistance.setText(pets);
            String observations = activityList.get(position).getNum_of_ticks() + " Observations";
            holder.txtViewObservations.setText(observations);
            String people = activityList.get(position).getNum_people() + " people";
            holder.txtViewPeople.setText(people);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // returns the size of item data MUST return the size of the Data
    @Override
    public int getItemCount() {
        return activityList.size();
    }


    public void animateTo(List<Activities> activities) {
        applyAndAnimateRemovals(activities);
        applyAndAnimateAdditions(activities);
        applyAndAnimateMovedItems(activities);
    }

    private void applyAndAnimateRemovals(List<Activities> newActivities) {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            final Activities activity = activityList.get(i);
            if (!newActivities.contains(activity)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Activities> newActivities) {
        for (int i = 0, count = newActivities.size(); i < count; i++) {
            final Activities activity = newActivities.get(i);
            if (!activityList.contains(activity)) {
                addItem(i, activity);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Activities> newActivities) {
        for (int toPosition = newActivities.size() - 1; toPosition >= 0; toPosition--) {
            final Activities activity = newActivities.get(toPosition);
            final int fromPosition = activityList.indexOf(activity);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Activities activity = activityList.remove(fromPosition);
        activityList.add(toPosition, activity);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Activities removeItem(int position) {
        final Activities activity = activityList.remove(position);
        notifyItemRemoved(position);
        return activity;
    }

    public void addItem(int position, Activities activity) {
        activityList.add(position, activity);
        notifyItemInserted(position);
    }


    /**
     * ViewHolder pattern for holding {@link Activities} objects.
     */
    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewActivity;
        private TextView txtViewActivityName;
        private TextView txtViewDistance;
        private TextView txtViewObservations;
        private TextView txtViewPeople;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            cardViewActivity = (CardView) itemView.findViewById(R.id.cardview_activity);
            txtViewActivityName = (TextView) itemView.findViewById(R.id.textView_activityNameMain);
            txtViewDistance = (TextView) itemView.findViewById(R.id.textView_totalDistance);
            txtViewObservations = (TextView) itemView.findViewById(R.id.textView_numOfObservations);
            txtViewPeople = (TextView) itemView.findViewById(R.id.textView_totalPeople);
        }
    }

}
