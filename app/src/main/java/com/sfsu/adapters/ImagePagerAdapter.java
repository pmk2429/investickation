package com.sfsu.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sfsu.investickation.R;

import java.util.List;

/**
 * Adapter to display images in the ViewPager contained in the
 * {@link com.sfsu.investickation.fragments.TickGuideDetailFragment}. The slider displays multiple images for each Tick
 * Created by Pavitra on 4/30/2016.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mImageUrls;

    public ImagePagerAdapter(Context mContext, List<Integer> mImageUrls) {
        this.mContext = mContext;
        this.mImageUrls = mImageUrls;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pagerimage_slider, container, false);
        ImageView mImageView = (ImageView) view.findViewById(R.id.imageview_pager_tick_image_main);
        mImageView.setImageResource(mImageUrls.get(position));
        // IMP step
        container.addView(view, 0);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
