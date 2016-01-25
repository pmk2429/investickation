package com.sfsu.investickation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfsu.investickation.R;

/**
 *
 */
public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    public ScreenSlidePageFragment() {
    }

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewpager_content, container, false);

        TextView textView_info = (TextView) rootView.findViewById(R.id.textView_pager_info);
        ImageView icon_info = (ImageView) rootView.findViewById(R.id.icon_pager_info);

        // Set the title and icon depending on page number.
        switch (mPageNumber) {
            case 0:
                icon_info.setImageResource(R.mipmap.ic_directions_walk_white_36dp);
                textView_info.setText(R.string.pager_activities);
                break;
            case 1:
                icon_info.setImageResource(R.mipmap.ic_photo_camera_white_36dp);
                textView_info.setText(R.string.pager_observations);
                break;
            case 2:
                icon_info.setImageResource(R.mipmap.ic_cloud_upload_white_24dp);
                textView_info.setText(R.string.pager_upload);
                break;
            case 3:
                icon_info.setImageResource(R.mipmap.ic_bug_report_white_36dp);
                textView_info.setText(R.string.pager_tick);
                break;
        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}
