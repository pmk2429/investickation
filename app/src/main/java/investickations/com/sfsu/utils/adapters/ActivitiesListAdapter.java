package investickations.com.sfsu.utils.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import investickations.com.sfsu.entities.Activities;
import investickations.com.sfsu.investickation.R;

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

    List<Activities> activityList;

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
            holder.txtViewActivityName.setText("Hiking @ Golden Gate Presidio");
            holder.txtViewDistance.setText("5.5 miles");
            holder.txtViewObservations.setText("6 Observations");
            holder.txtViewPeople.setText("2 people");
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

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        private TextView txtViewActivityName;
        private TextView txtViewDistance;
        private TextView txtViewObservations;
        private TextView txtViewPeople;

        public ActivityViewHolder(View itemView) {

            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardview_activity);
            txtViewActivityName = (TextView) itemView.findViewById(R.id.textView_activityName);
            txtViewDistance = (TextView) itemView.findViewById(R.id.textView_totalDistance);
            txtViewObservations = (TextView) itemView.findViewById(R.id.textView_numOfObservations);
            txtViewPeople = (TextView) itemView.findViewById(R.id.textView_totalPeople);
        }
    }

}
