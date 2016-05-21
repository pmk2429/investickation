package com.sfsu.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    private Context mContext;

    public TicksListAdapter(List<Tick> tickList, Context mContext) {
        this.tickList = new ArrayList<>(tickList);
        this.mContext = mContext;
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
            if (tickList != null) {
                Tick mTick = tickList.get(position);
                holder.txtTickName.setText(mTick.getTickName());
                holder.txtTickSpecies.setText(mTick.getSpecies());
                Picasso.with(mContext).load(mTick.getImageUrl()).into(holder.imageViewTick);
                holder.txtTickDetail.setText(R.string.tick_click_here_for_details);
            }
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

    /**
     * Method to animate this Adapter for the holding List based on the changes in Query text.
     *
     * @param Ticks
     */
    public void animateTo(List<Tick> ticks) {
        applyAndAnimateRemovals(ticks);
        applyAndAnimateAdditions(ticks);
        applyAndAnimateMovedItems(ticks);
    }

    private void applyAndAnimateRemovals(List<Tick> newTicks) {
        for (int i = tickList.size() - 1; i >= 0; i--) {
            final Tick tick = tickList.get(i);
            if (!newTicks.contains(tick)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Tick> newTicks) {
        for (int i = 0, count = newTicks.size(); i < count; i++) {
            final Tick tick = newTicks.get(i);
            if (!tickList.contains(tick)) {
                addItem(i, tick);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Tick> newTicks) {
        for (int toPosition = newTicks.size() - 1; toPosition >= 0; toPosition--) {
            final Tick tick = newTicks.get(toPosition);
            final int fromPosition = tickList.indexOf(tick);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Tick tick = tickList.remove(fromPosition);
        tickList.add(toPosition, tick);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Tick removeItem(int position) {
        final Tick tick = tickList.remove(position);
        notifyItemRemoved(position);
        return tick;
    }

    public void addItem(int position, Tick tick) {
        tickList.add(position, tick);
        notifyItemInserted(position);
    }

    /**
     * Clears all the items from the Adapter
     */
    public void clear() {
        tickList.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds all the items passed as param to the Adapter
     *
     * @param list
     */
    public void addAll(List<Tick> list) {
        tickList.addAll(list);
        notifyDataSetChanged();
    }

    public static class TickViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        private TextView txtTickName;
        private TextView txtTickSpecies;
        private TextView txtTickDetail;
        private ImageView imageViewTick;


        public TickViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardview_ticks);
            txtTickName = (TextView) itemView.findViewById(R.id.textView_tickRecyclerItem_tickName);
            txtTickSpecies = (TextView) itemView.findViewById(R.id.textView_tickRecyclerItem_species);
            txtTickDetail = (TextView) itemView.findViewById(R.id.textView_tickRecyclerItem_tickDetail);
            imageViewTick = (ImageView) itemView.findViewById(R.id.imageView_tickRecyclerItem_image);
        }
    }

}
