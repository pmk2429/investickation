package investickations.com.sfsu.controllers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import investickations.com.sfsu.entities.Observation;
import investickations.com.sfsu.investickation.R;

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

    public ObservationsListAdapter(List<Observation> observationList) {
        this.observationList = observationList;
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
            if (observationList != null && observationList.size() > 0) {
                for (Observation observation : observationList) {
//                  holder.imageView_tickImage.setImageResource();
//                  holder.imageView_imageStatus.setImageResource();
                    holder.txtView_observationName.setText(observation.getTickName());
                    holder.txtView_location.setText(observation.getLocation());
                    holder.txtView_timestamp.setText(Long.toString((new Date(observation.getTimestamp()).getTime())));
                }
            }
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
