package com.sfsu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <tt>ActivitiesListAdapter</tt> is a Custom Adapter for displaying activities in the RecyclerView.
 * This provides a row item for each of the entry in the RecyclerView inflated using custom card view layout
 * ActivitiesListAdapter expects a custom layout as one of the parameter in the constructor.
 * Since the RecyclerView needs LayoutManager, this class implements own layout manager for define the data
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
        try {
            if (holder != null) {

                Activities mActivity = activityList.get(position);
                // render all the data in the View.
                // Activity name and location
                StringBuilder actNameStringBuilder = new StringBuilder();
                if (mActivity.getLocation_area() != null && !mActivity.getLocation_area().equals("")) {
                    actNameStringBuilder.append(mActivity.getActivityName() + " @ " + mActivity.getLocation_area());
                } else {
                    actNameStringBuilder.append(mActivity.getActivityName());
                }
                holder.txtView_activityName.setText(actNameStringBuilder.toString());
                // pets
                String pets = mActivity.getNum_of_pets() + " pets";
                holder.txtView_pets.setText(pets);
                // Observations
                String observations = mActivity.getNum_of_ticks() + " Observations";
                holder.txtView_observations.setText(observations);
                // total People
                String people = mActivity.getNum_of_people() + " people";
                holder.txtView_people.setText(people);

                if (mActivity.getImage_url() != null) {

                    String image_url = mActivity.getImage_url();

                    // depending on the image url, display the activity image
                    if (image_url == null || image_url.equals("")) {
                        holder.imageView_staticMap.setImageResource(R.mipmap.placeholder_activity);
                    } else {
                        // imageFile
                        File imgFile = new File(image_url);
                        if (AppUtils.isConnectedOnline(mContext)) {
                            if (image_url.startsWith("http")) {
                                Picasso.with(mContext).load(image_url).into(holder.imageView_staticMap);
                            } else {
                                if (imgFile.exists()) {
                                    Bitmap tickBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    holder.imageView_staticMap.setImageBitmap(tickBitmap);
                                }
                            }
                        } else {
                            if (imgFile.exists()) {
                                Bitmap tickBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                holder.imageView_staticMap.setImageBitmap(tickBitmap);
                            }
                        }
                    }
                }

                if (mActivity.isOnCloud()) {
                    holder.txtView_storage.setText(R.string.text_cloud);
                } else {
                    holder.txtView_storage.setText(R.string.text_local);
                }
            }
        } catch (NullPointerException ne) {

        } catch (Exception e) {

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
     * Clears all the items from the Adapter
     */
    public void clear() {
        activityList.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds all the items passed as param to the Adapter
     *
     * @param list
     */
    public void addAll(List<Activities> list) {
        activityList.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * ViewHolder pattern for holding {@link Activities} objects.
     */
    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        private CardView cardViewActivity;
        private ImageView imageView_staticMap;
        //        private ImageView icon_storage;
        private TextView txtView_activityName;
        private TextView txtView_storage;
        private TextView txtView_pets;
        private TextView txtView_observations;
        private TextView txtView_people;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            cardViewActivity = (CardView) itemView.findViewById(R.id.cardview_actList_details);
            imageView_staticMap = (ImageView) itemView.findViewById(R.id.imageView_actList_staticMap);
            //icon_storage = (ImageView) itemView.findViewById(R.id.icon_actList_storage);
            txtView_activityName = (TextView) itemView.findViewById(R.id.textView_actList_name);
            txtView_storage = (TextView) itemView.findViewById(R.id.textView_actList_storageStatus);
            txtView_pets = (TextView) itemView.findViewById(R.id.textView_actList_totalPets);
            txtView_observations = (TextView) itemView.findViewById(R.id.textView_actList_totalObservations);
            txtView_people = (TextView) itemView.findViewById(R.id.textView_actList_totalPeople);
        }
    }

}
