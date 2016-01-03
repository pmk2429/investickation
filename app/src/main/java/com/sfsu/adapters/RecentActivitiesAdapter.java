package com.sfsu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sfsu.entities.Activities;
import com.sfsu.investickation.R;
import com.sfsu.utils.AppUtils;

import java.util.List;

/**
 * Custom ArrayAdapter for displaying list of {@link Activities} details.
 * Created by Pavitra on 1/2/2016.
 */
public class RecentActivitiesAdapter extends ArrayAdapter<Activities> {

    private Context mContext;
    private List<Activities> mActivitiesList;

    public RecentActivitiesAdapter(Context context, List<Activities> objects) {
        super(context, 0, objects);
        this.mActivitiesList = objects;
        this.mContext = mContext;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_activities, parent, false);
        }
        TextView textView_activityName = (TextView) convertView.findViewById(R.id.textView_actListItem_actName);
        TextView textView_activityDate = (TextView) convertView.findViewById(R.id.textView_actListItem_date);

        String activityDetails = mActivitiesList.get(position).getActivityName() + " @ " + mActivitiesList.get(position)
                .getLocation_area();
        textView_activityName.setText(activityDetails);

        String date = AppUtils.getDateAndTime(mActivitiesList.get(position).getTimestamp());
        textView_activityDate.setText(date);

        return convertView;
    }
}
