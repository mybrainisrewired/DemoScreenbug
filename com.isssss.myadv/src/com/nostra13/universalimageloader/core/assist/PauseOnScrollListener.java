package com.nostra13.universalimageloader.core.assist;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.millennialmedia.android.MMAdView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PauseOnScrollListener implements OnScrollListener {
    private final OnScrollListener externalListener;
    private ImageLoader imageLoader;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;

    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case MMAdView.TRANSITION_NONE:
                this.imageLoader.resume();
                break;
            case MMAdView.TRANSITION_FADE:
                if (this.pauseOnScroll) {
                    this.imageLoader.pause();
                }
                break;
            case MMAdView.TRANSITION_UP:
                if (this.pauseOnFling) {
                    this.imageLoader.pause();
                }
                break;
        }
        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(view, scrollState);
        }
    }
}