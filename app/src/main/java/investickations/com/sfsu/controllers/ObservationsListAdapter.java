package investickations.com.sfsu.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import investickations.com.sfsu.entities.Observation;
import investickations.com.sfsu.investickation.ObservationMainActivity;

/**
 * <p>
 * <tt>ObservationsListAdapter</tt> is a Custom Adapter which is used to display a Custom RowView
 * in the ObservationsList in Observations Fragment. This Class extends BaseAdapter in order to
 * create the custom view. The parameters passed to the ObservationsListAdapter expects Custom
 * Layout View (XML identifier) which is inflated to the RowView.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class ObservationsListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context myContext;
    int mResource;
    private List<Observation> observationList;

    public ObservationsListAdapter(Context context, int resource, List<Observation> observations) {
        this.myContext = context;
        this.observationList = observations;
        this.mResource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return observationList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, viewGroup, false);
        }

        // if the View is present, inflate the View with Observation Row Item
        Observation observationItem = observationList.get(position);
        if (observationItem != null) {

            // perform all the operations for inflating the View with Observation Details.
        }

        return convertView;
    }
}
