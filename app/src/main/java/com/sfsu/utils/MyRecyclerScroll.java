package com.sfsu.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Custom RecylerView scroller for loading Animation on FAB
 */
public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener {
    private static final float HIDE_THRESHOLD = 100;
    private static final float SHOW_THRESHOLD = 50;

    int scrollDist = 0;
    private boolean isVisible = true;

    // method not used because its action is called per pixel value change
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //  Check scrolled distance against the minimum
        if (isVisible && scrollDist > HIDE_THRESHOLD) {
            //  Hide fab & reset scrollDist
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        // MINIMUM because scrolling up gives - dy values(Y-axis)
        else if (!isVisible && scrollDist < -SHOW_THRESHOLD) {
            //  Show fab & reset scrollDist
            show();

            scrollDist = 0;
            isVisible = true;
        }

        //  Whether we scroll up or down, calculate scroll distance
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }

    }


    public abstract void show();

    public abstract void hide();
}
