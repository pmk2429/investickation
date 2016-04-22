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

    List<Tick> tickList;
    Context mContext;
    int mResource;

    public TickDialogAdapter(Context context, int resource, List<Tick> objects) {
        super(context, resource, objects);
        this.tickList = objects;
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        Tick tickObj = tickList.get(position);
        if (tickObj != null) {
            ImageView imageViewTick = (ImageView) convertView.findViewById(R.id.imageView_alertTick_tickImage);
            TextView textViewTicName = (TextView) convertView.findViewById(R.id.textView_alertTick_tickName);
            TextView textViewTicSpecies = (TextView) convertView.findViewById(R.id.textView_alertTick_tickSpecies);

            Picasso.with(mContext).load(tickObj.getImageUrl()).into(imageViewTick);
            textViewTicName.setText(tickObj.getTickName());
            textViewTicSpecies.setText(tickObj.getSpecies());
        }
        return convertView;
    }
}
