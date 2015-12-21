package com.sfsu.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Observation;
import com.sfsu.investickation.R;
import com.sfsu.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

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

    List<Observation> observationList;
    private Context mContext;

    public ObservationsListAdapter(List<Observation> observationList, Context mContext) {
        this.observationList = observationList;
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
        if (holder != null) {
            Observation mObservation = observationList.get(position);
            Picasso.with(mContext).load(mObservation.getImageUrl()).into(holder.imageView_tickImage);
            holder.txtView_observationName.setText(mObservation.getTickName());
            holder.txtView_location.setText(mObservation.getGeoLocation());
            String dateAndTime = AppUtils.getCurrentDateAndTime(mObservation.getTimestamp());
            holder.txtView_timestamp.setText(dateAndTime);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return observationList.size();
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
        for (int i = observationList.size() - 1; i >= 0; i--) {
            final Observation observation = observationList.get(i);
            if (!newObservations.contains(observation)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Observation> newObservations) {
        for (int i = 0, count = newObservations.size(); i < count; i++) {
            final Observation observation = newObservations.get(i);
            if (!observationList.contains(observation)) {
                addItem(i, observation);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Observation> newObservations) {
        for (int toPosition = newObservations.size() - 1; toPosition >= 0; toPosition--) {
            final Observation observation = newObservations.get(toPosition);
            final int fromPosition = observationList.indexOf(observation);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Observation observation = observationList.remove(fromPosition);
        observationList.add(toPosition, observation);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Observation removeItem(int position) {
        final Observation observation = observationList.remove(position);
        notifyItemRemoved(position);
        return observation;
    }

    public void addItem(int position, Observation observation) {
        observationList.add(position, observation);
        notifyItemInserted(position);
    }


    public static class ObservationViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView imageView_tickImage, imageView_imageStatus;
        TextView txtView_observationName, txtView_location, txtView_timestamp;

        public ObservationViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cardview_observation);
            imageView_tickImage = (ImageView) itemView.findViewById(R.id.image_tick_small);
            imageView_imageStatus = (ImageView) itemView.findViewById(R.id.image_status);
            txtView_observationName = (TextView) itemView.findViewById(R.id.textView_observationName);
            txtView_location = (TextView) itemView.findViewById(R.id.textView_location);
            txtView_timestamp = (TextView) itemView.findViewById(R.id.textView_timestamp);
        }
    }


}
