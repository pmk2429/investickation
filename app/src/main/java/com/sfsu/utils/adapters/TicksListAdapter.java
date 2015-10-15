package com.sfsu.utils.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;

/**
 * <p>
 * <tt>TickListAdapter</tt> is a Custom Adapter for displaying ticks in the RecyclerView.
 * This provides a row item for each of the entry in the RecyclerView inflated using custom card view layout
 * TickListAdapter expects a custom layout as one of the parameter in the constructor.
 * Since the RecyclerView needs LayoutManagaer, this class implements own layout manager for define the data
 * in the layout.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class TicksListAdapter extends RecyclerView.Adapter<TicksListAdapter.TickViewHolder> {

    private List<Tick> tickList;


    public TicksListAdapter(List<Tick> tickList) {
        this.tickList = tickList;
    }

    public static class TickViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        private TextView txtTickName;
        private TextView txtTickDetail;
        private ImageView imageViewTick;


        public TickViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardview_ticks);
            txtTickName = (TextView) itemView.findViewById(R.id.textView_tickName);
            txtTickDetail = (TextView) itemView.findViewById(R.id.textView_tickDetail);
            imageViewTick = (ImageView) itemView.findViewById(R.id.image_tickGuideMain);
        }
    }

    // this method is used to create the Fragment view
    @Override
    public TicksListAdapter.TickViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_tickguide, null);
        TickViewHolder tickViewHolder = new TickViewHolder(v);
        return tickViewHolder;
    }

    @Override
    public void onBindViewHolder(TicksListAdapter.TickViewHolder holder, int position) {
        if (holder != null) {
            
            holder.txtTickName.setText("American Dog Tick");
            holder.txtTickDetail.setText("Click Here to view details");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return tickList.size();
    }


}
