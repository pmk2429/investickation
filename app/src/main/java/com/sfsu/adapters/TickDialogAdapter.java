package com.sfsu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Custom List Adapter to set the Item layout in the ListView.
 * Created by Pavitra on 11/18/2015.
 */
public class TickDialogAdapter extends ArrayAdapter<Tick> {

    List<Tick> mTickList;
    Context mContext;
    int mResource;

    public TickDialogAdapter(Context context, int resource, List<Tick> objects) {
        super(context, resource, objects);
        this.mTickList = objects;
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        Tick tickObj = mTickList.get(position);
        if (tickObj != null) {
            ImageView imageViewTick = (ImageView) convertView.findViewById(R.id.image_view_alert_tick_image);
            TextView textViewTicName = (TextView) convertView.findViewById(R.id.textview_alert_tick_name);
            TextView textViewTicSpecies = (TextView) convertView.findViewById(R.id.textview_alert_tick_species);

            Picasso.with(mContext).load(tickObj.getImageUrl()).into(imageViewTick);
            textViewTicName.setText(tickObj.getTickName());
            textViewTicSpecies.setText(tickObj.getSpecies());
        }
        return convertView;
    }
}
