package com.isssss.myadv.kernel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.isssss.myadv.dao.BannerConfigDao;
import com.isssss.myadv.view.GoogleBanner;
import com.millennialmedia.android.MMAdView;
import com.mopub.mobileads.MoPubView;

public class BannerController {
    private static AdView mAdview;
    private static Context mContext;
    public static BannerController mInstance;
    private static LayoutParams params;
    private static WindowManager wm;
    private MoPubView mMoPubView;

    private BannerController(Context context, WindowManager mWindowManager, AdView adview) {
        mContext = context;
        wm = mWindowManager;
        mAdview = adview;
    }

    public static BannerController getInstance(Context context, WindowManager mWindowManager, AdView adview) {
        if (mInstance == null) {
            mInstance = new BannerController(context, mWindowManager, adview);
        } else {
            mInstance = null;
            mInstance = new BannerController(context, mWindowManager, adview);
        }
        return mInstance;
    }

    public static void removeAdView() {
        if (wm != null && mAdview != null) {
            wm.removeView(mAdview);
            GoogleBanner.mInstance = null;
            wm = null;
        }
    }

    private void setLayoutParams(int position) {
        switch (position) {
            case MMAdView.TRANSITION_FADE:
                params.gravity = 49;
            case MMAdView.TRANSITION_UP:
                params.gravity = 81;
            case MMAdView.TRANSITION_DOWN:
                params.gravity = 17;
            default:
                break;
        }
    }

    public void addAdView() {
        params = new LayoutParams();
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService("layout_inflater");
        params.type = 2010;
        params.width = -2;
        params.height = -2;
        setLayoutParams(BannerConfigDao.getInstance(mContext).getConfig().getPosition());
        params.flags = 8;
        try {
            wm.addView(mAdview, params);
        } catch (Exception e) {
            removeAdView();
        }
        mAdview.loadAd(new Builder().build());
    }
}