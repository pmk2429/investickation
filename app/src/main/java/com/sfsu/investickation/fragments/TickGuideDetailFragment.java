package com.sfsu.investickation.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sfsu.adapters.ImagePagerAdapter;
import com.sfsu.entities.Tick;
import com.sfsu.investickation.R;
import com.sfsu.investickation.TickGuideMasterActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Displays the detailed information about all the Ticks contained in the InvesTICKations project.
 */
public class TickGuideDetailFragment extends Fragment {

    private final String TAG = "~!@#TickDetailFrag";
    @Bind(R.id.textView_tickDet_knownFor)
    TextView txtView_knownFor;
    @Bind(R.id.textView_tickDet_tickFormalName)
    TextView txtView_tickFormalName;
    @Bind(R.id.textView_tickDet_tickLocation)
    TextView txtView_tickLocation;
    @Bind(R.id.textView_tickDet_tickName)
    TextView txtView_tickName;
    @Bind(R.id.textView_tickDet_tickSpecies)
    TextView txtView_tickSpecies;
    @Bind(R.id.textView_tickDet_tickDetails)
    TextView txtView_description;
    @Bind(R.id.viewPager_tickDet_Images)
    ViewPager mViewPager;
    @Bind(R.id.circlePagerIndicator_tickDet_images)
    CirclePageIndicator mCirclePageIndicator;
    private Bundle args;
    private Tick mTick;
    private Context mContext;
    private ImagePagerAdapter mImagePagerAdapter;
    // arrayList of imageUrls
    private List<Integer> imageUrls;
    private int currentPage;


    public TickGuideDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Method to create {@link TickGuideDetailFragment} instance.
     *
     * @param key
     * @param tickObj
     * @return
     */
    public static TickGuideDetailFragment newInstance(String key, Tick tickObj) {
        TickGuideDetailFragment tickGuideDetailFragment = new TickGuideDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(key, tickObj);
        tickGuideDetailFragment.setArguments(args);
        return tickGuideDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mContext = context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            args = getArguments();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tick_guide_detail, container, false);
        ButterKnife.bind(this, rootView);

        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarTickDetail);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id
                .collapsing_toolbar_guideDetail);
        String title = getResources().getString(R.string.tickDetails_toolbar_title);
        collapsingToolbar.setTitle(title);

        imageUrls = new ArrayList<>();
        imageUrls.add(R.drawable.image_1);
        imageUrls.add(R.drawable.image_2);
        imageUrls.add(R.drawable.image_3);
        imageUrls.add(R.drawable.image_4);

        mImagePagerAdapter = new ImagePagerAdapter(mContext, imageUrls);

        mViewPager.setAdapter(mImagePagerAdapter);
        mCirclePageIndicator.setViewPager(mViewPager);

        mCirclePageIndicator.setOnPageChangeListener(new PageChangeListener());

        mViewPager.setCurrentItem(currentPage);

        try {
            if (args.getParcelable(TickGuideMasterActivity.KEY_TICK_DETAIL) != null) {
                mTick = args.getParcelable(TickGuideMasterActivity.KEY_TICK_DETAIL);
            }

            txtView_tickName.setText(mTick.getTickName());
            txtView_tickSpecies.setText(mTick.getSpecies());
            txtView_knownFor.setText(mTick.getKnown_for());
            txtView_tickFormalName.setText(mTick.getScientific_name());
            txtView_description.setText(mTick.getDescription());
            txtView_tickLocation.setText(mTick.getFound_near_habitat());

            // for demo purpose, just create an ArrayList of images and show it from drawable


            //Picasso.with(mContext).load(mTick.getImageUrl()).into(imageView_tickImage);
        } catch (NullPointerException ne) {
            throw new NullPointerException();
        } catch (Exception e) {

        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_fragment_tick_detail);
    }

    /**
     * Listens to the change of the pages in ViewPager and sets the current content based on the CurrentPage
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
        }
    }
}
