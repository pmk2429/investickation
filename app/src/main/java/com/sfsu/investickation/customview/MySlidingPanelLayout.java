package com.sfsu.investickation.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Custom SlidingPanelLayout to disable the scrolling and enable onClickListener for opening Observation details
 * Created by Pavitra on 5/3/2016.
 */
public class MySlidingPanelLayout extends SlidingUpPanelLayout {
    public MySlidingPanelLayout(Context context) {
        super(context);
    }

    public MySlidingPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlidingPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // disable swipe
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
