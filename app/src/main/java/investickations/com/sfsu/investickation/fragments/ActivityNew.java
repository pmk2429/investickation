package investickations.com.sfsu.investickation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;

import investickations.com.sfsu.investickation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityNew extends Fragment {
    private IActivityNewCallBack mInterface;

    public ActivityNew() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Start New Activity");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_activity_new, container, false);

        final FloatingActionButton addProject = (FloatingActionButton) v.findViewById(R.id.fab_activity_start);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.onPlayButtonclick();
            }
        });

        return v;
    }

    public interface IActivityNewCallBack {
        public void onPlayButtonclick();
    }


}
