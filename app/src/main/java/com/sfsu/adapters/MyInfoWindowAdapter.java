package com.sfsu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sfsu.entities.Account;
import com.sfsu.investickation.R;

/**
 * Custom InfoWindow for displaying {@link com.sfsu.entities.Tick} in GoogleMap when {@link Account} clicks on the {@link Marker}.
 * <p>
 * Created by Pavitra on 1/4/2016.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private Context mContext;

    MyInfoWindowAdapter(Context context) {
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        myContentsView = inflater.inflate(R.layout.infowindow_tick, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
