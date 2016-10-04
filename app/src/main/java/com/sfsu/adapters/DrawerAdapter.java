package com.sfsu.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.investickation.HomeActivity;
import com.sfsu.investickation.MainActivity;
import com.sfsu.investickation.ObservationMasterActivity;
import com.sfsu.investickation.R;
import com.sfsu.investickation.TickGuideMasterActivity;
import com.sfsu.investickation.UserActivityMasterActivity;
import com.sfsu.investickation.UserProfileActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    // Item Identifier in Navigation Drawer
    protected static final int NAVDRAWER_ITEM_MAIN = 1;
    protected static final int NAVDRAWER_ITEM_ACTIVITIES = 2;
    protected static final int NAVDRAWER_ITEM_OBSERVATIONS = 3;
    protected static final int NAVDRAWER_ITEM_GUIDE = 4;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 5;
    protected static final int NAVDRAWER_ITEM_REGISTER = 6;

    // Header and Row section ID
    private static final int HEADER_TYPE = 0;
    private static final int ROW_TYPE = 1;
    private static Context mContext;
    private List<String> mRows;


    public DrawerAdapter(List<String> mRows, int ICONS[], Context passedContext) {
        this.mRows = mRows;
        mContext = passedContext;
    }

    public DrawerAdapter() {
        // needed
    }

    // this method will handle the onClick event calls
    private static void goToNavDrawerItem(int item) {
        Intent intent;
        switch (item) {
            case NAVDRAWER_ITEM_MAIN:
                intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;

            case NAVDRAWER_ITEM_ACTIVITIES:
                intent = new Intent(mContext, UserActivityMasterActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;

            case NAVDRAWER_ITEM_OBSERVATIONS:
                intent = new Intent(mContext, ObservationMasterActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;

            case NAVDRAWER_ITEM_GUIDE:
                intent = new Intent(mContext, TickGuideMasterActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;

            case NAVDRAWER_ITEM_SETTINGS:
                intent = new Intent(mContext, UserProfileActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;

            case NAVDRAWER_ITEM_REGISTER:
                intent = new Intent(mContext, HomeActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            return new ViewHolder(view, viewType, mContext);
        } else if (viewType == ROW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            return new ViewHolder(view, viewType, mContext);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == ROW_TYPE) {
            String rowText = mRows.get(position - 1);
            holder.textView.setText(rowText);
        }
    }

    @Override
    public int getItemCount() {
        return mRows.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_TYPE;
        return ROW_TYPE;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected int viewType;
        Context context;

        @Bind(R.id.drawer_row_icon)
        ImageView imageView;
        @Bind(R.id.drawer_row_text)
        TextView textView;

        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);
            this.context = context;
            this.viewType = viewType;

            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            if (viewType == ROW_TYPE) {
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public void onClick(View v) {
            DrawerAdapter.goToNavDrawerItem(getPosition());
        }
    }
}