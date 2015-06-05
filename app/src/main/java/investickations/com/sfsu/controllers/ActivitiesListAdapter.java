package investickations.com.sfsu.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import investickations.com.sfsu.entities.Activity;

/**
 * <p>
 * <tt>ActivitiesListAdapter</tt> is a Custom Adapter for displaying activities in the ListView.
 * This provides a row Item for each of the entry in the ListView inflated using custom layout
 * ActivitiesListAdapter expects a custom layout as one of the parameter in the constructor
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class ActivitiesListAdapter extends BaseAdapter {

    private static LayoutInflater inflater;
    List<Activity> activityList;
    Context myContext;
    int mResource;

    public ActivitiesListAdapter(Context myContext, int mResource, List<Activity> activityList) {
        this.activityList = activityList;
        this.myContext = myContext;
        this.mResource = mResource;
        inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, viewGroup, false);
        }

        // if the View is present, inflate the View with Observation Row Item
        Activity observationItem = activityList.get(position);
        if (observationItem != null) {

            // perform all the operations for inflating the View with Observation Details.
        }

        return convertView;
    }
}
