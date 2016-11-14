package com.sfsu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.controllers.ImageController;
import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * <p>
 * <tt>ObservationsListAdapter</tt> is a Custom Adapter which is used to display a Custom RowView
 * in the ObservationListFragment in Observations Fragment in the RecyclerView.
 * This Class extends RecyclerView.Adapter in order to create the custom view.
 * Since the RecyclerView needs LayoutManagaer, this class implements own layout manager for define the data
 * in the layout.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class ObservationsListAdapter extends RecyclerView.Adapter<ObservationsListAdapter.ObservationViewHolder> {

    private static final String TAG = "~!@#$ObsListAdptr";
    List<Observation> mObservationList;
    private Context mContext;

    public ObservationsListAdapter(List<Observation> mObservationList, Context mContext) {
        this.mObservationList = mObservationList;
        this.mContext = mContext;
    }

    @Override
    public ObservationsListAdapter.ObservationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_observation, null);
        ObservationViewHolder mObservationViewHolder = new ObservationViewHolder(v);
        return mObservationViewHolder;
    }

    @Override
    public void onBindViewHolder(ObservationsListAdapter.ObservationViewHolder holder, int position) {
        try {
            if (holder != null) {
                Observation mObservation = mObservationList.get(position);
                holder.txtView_observationName.setText(mObservation.getTickName());
                holder.txtView_location.setText(mObservation.getGeoLocation());
                String[] dateAndTime = AppUtils.getDateAndTimeSeparate(mObservation.getTimestamp());
                holder.txtView_date.setText(dateAndTime[0]);// set date
                holder.txtView_time.setText(dateAndTime[1]); // set time

                // set Image url and also the storage status
                if (mObservation.isOnCloud()) {
                    holder.icon_storageStatus.setImageResource(R.mipmap.ic_cloud_done_black_36dp);
                } else {
                    holder.icon_storageStatus.setImageResource(R.mipmap.ic_sd_storage_black_24dp);
                }

                // FIXME: load image into ImageView
                if (mObservation.getImageUrl().startsWith("http")) {
                    Picasso.with(mContext).load(mObservation.getImageUrl()).into(holder.imageView_tickImage);
                } else {
                    Bitmap bitmap = new ImageController(mContext).getBitmapForImageView(holder.imageView_tickImage, mObservation.getImageUrl());
                    holder.imageView_tickImage.setImageBitmap(bitmap);
                }


                if (mObservation.isVerified()) {
                    holder.icon_verified.setImageResource(R.mipmap.ic_verified_gray_24dp);
                }

            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.i(TAG, "onBindViewHolder: " + e.getCause());
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mObservationList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void animateTo(List<Observation> observations) {
        applyAndAnimateRemovals(observations);
        applyAndAnimateAdditions(observations);
        applyAndAnimateMovedItems(observations);
    }

    private void applyAndAnimateRemovals(List<Observation> newObservations) {
        for (int i = mObservationList.size() - 1; i >= 0; i--) {
            final Observation observation = mObservationList.get(i);
            if (!newObservations.contains(observation)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Observation> newObservations) {
        for (int i = 0, count = newObservations.size(); i < count; i++) {
            final Observation observation = newObservations.get(i);
            if (!mObservationList.contains(observation)) {
                addItem(i, observation);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Observation> newObservations) {
        for (int toPosition = newObservations.size() - 1; toPosition >= 0; toPosition--) {
            final Observation observation = newObservations.get(toPosition);
            final int fromPosition = mObservationList.indexOf(observation);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Observation observation = mObservationList.remove(fromPosition);
        mObservationList.add(toPosition, observation);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Observation removeItem(int position) {
        final Observation observation = mObservationList.remove(position);
        notifyItemRemoved(position);
        return observation;
    }

    public void addItem(int position, Observation observation) {
        mObservationList.add(position, observation);
        notifyItemInserted(position);
    }

    /**
     * Clears all the items from the Adapter
     */
    public void clear() {
        mObservationList.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds all the items passed as param to the Adapter
     *
     * @param list
     */
    public void addAll(List<Observation> list) {
        mObservationList.addAll(list);
        notifyDataSetChanged();
    }


    public static class ObservationViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView imageView_tickImage, icon_storageStatus, icon_verified;
        TextView txtView_observationName, txtView_location, txtView_time, txtView_date;

        public ObservationViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardview_observation_container);
            imageView_tickImage = (ImageView) itemView.findViewById(R.id.imageview_tick_image);
            icon_storageStatus = (ImageView) itemView.findViewById(R.id.icon_observation_storage_status);
            icon_verified = (ImageView) itemView.findViewById(R.id.icon_observation_verified);
            txtView_observationName = (TextView) itemView.findViewById(R.id.textview_observation_name);
            txtView_location = (TextView) itemView.findViewById(R.id.textview_observation_location);
            txtView_date = (TextView) itemView.findViewById(R.id.textview_observation_date);
            txtView_time = (TextView) itemView.findViewById(R.id.textview_observation_time);
        }
    }
}
