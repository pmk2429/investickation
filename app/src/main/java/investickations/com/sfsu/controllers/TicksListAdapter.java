package investickations.com.sfsu.controllers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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


    public static class TickViewHolder extends RecyclerView.ViewHolder {
        public TickViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public TicksListAdapter.TickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TicksListAdapter.TickViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
