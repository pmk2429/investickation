package com.sfsu.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private Context mContext;

    public ActivitiesListAdapter(List<Activities> activityList, Context mContext) {
        this.activityList = new ArrayList<>(activityList);
        this.mContext = mContext;
    }

    // creates a ViewHolder
    @Override
    public ActivitiesListAdapter.ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_activities, viewGroup, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivitiesListAdapter.ActivityViewHolder holder, int position) {
        if (holder != null) {

            Activities mActivity = activityList.get(position);
            // render all the data in the View.
            String activityName = mActivity.getActivityName() + " @ " + mActivity.getLocation_area();
            holder.txtView_activityName.setText(activityName);
            String pets = mActivity.getNum_of_pets() + " pets";
            holder.txtView_distance.setText(pets);
            String observations = mActivity.getNum_of_ticks() + " Observations";
            holder.txtView_observations.setText(observations);
            String people = mActivity.getNum_of_people() + " people";
            holder.txtView_people.setText(people);

            String image_url = mActivity.getImage_url();

            if (image_url == "" || image_url == null) {
                holder.imageView_staticMap.setImageResource(R.mipmap.placeholder_activity);
            } else {
                Picasso.with(mContext).load(mActivity.getImage_url()).into(holder.imageView_staticMap);
            }
        }
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
        Log.i("~!@#$Adptr", "removal called");
        for (int i = activityList.size() - 1; i >= 0; i--) {
            final Activities activity = activityList.get(i);
            if (!newActivities.contains(activity)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Activities> newActivities) {
        Log.i("~!@#$Adptr", "addition called");
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

        private CardView cardViewActivity;
        private ImageView imageView_staticMap;
        private TextView txtView_activityName;
        private TextView txtView_distance;
        private TextView txtView_observations;
        private TextView txtView_people;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            cardViewActivity = (CardView) itemView.findViewById(R.id.cardview_activity);
            imageView_staticMap = (ImageView) itemView.findViewById(R.id.imageView_actList_staticMap);
            txtView_activityName = (TextView) itemView.findViewById(R.id.textView_activityNameMain);
            txtView_distance = (TextView) itemView.findViewById(R.id.textView_totalDistance);
            txtView_observations = (TextView) itemView.findViewById(R.id.textView_numOfObservations);
            txtView_people = (TextView) itemView.findViewById(R.id.textView_totalPeople);
        }
    }

}
