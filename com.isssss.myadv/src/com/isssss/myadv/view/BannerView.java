package com.isssss.myadv.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.drive.DriveFile;
import com.isssss.myadv.model.BannerInfo;
import com.isssss.myadv.utils.FileUtil;
import com.millennialmedia.android.MMAdView;
import mobi.vserv.android.ads.R;

public class BannerView {
    private static View mBanner;
    private static ImageView mBanner_close;
    private static TextView mBanner_content;
    private static ImageView mBanner_icon;
    private static TextView mBanner_title;
    private static WindowManager mWindowManager;

    class AnonymousClass_3 implements OnClickListener {
        private final /* synthetic */ BannerInfo val$banner;
        private final /* synthetic */ Context val$context;

        AnonymousClass_3(BannerInfo bannerInfo, Context context) {
            this.val$banner = bannerInfo;
            this.val$context = context;
        }

        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(new StringBuilder("http://").append(this.val$banner.getApkUrl()).toString()));
            intent.setFlags(DriveFile.MODE_READ_ONLY);
            this.val$context.startActivity(intent);
            BannerView.removeBannerView();
        }
    }

    public static void addBannerView(Context context, BannerInfo banner, Bitmap bitmap) {
        if (!(mWindowManager == null || mBanner == null)) {
            removeBannerView();
        }
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        }
        if (mBanner == null) {
            mBanner = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.activity_main, null);
        }
        LayoutParams params = new LayoutParams();
        params.type = 2010;
        params.width = -1;
        params.height = -2;
        params.gravity = 81;
        params.flags = 8;
        mBanner_icon = (ImageView) mBanner.findViewById(R.string.auth_client_needs_enabling_title);
        mBanner_title = (TextView) mBanner.findViewById(R.string.auth_client_needs_installation_title);
        mBanner_content = (TextView) mBanner.findViewById(R.string.auth_client_needs_update_title);
        mBanner_close = (ImageView) mBanner.findViewById(R.string.auth_client_play_services_err_notification_msg);
        mBanner_close.setVisibility(MMAdView.TRANSITION_RANDOM);
        mBanner_icon.setImageBitmap(bitmap);
        mBanner_title.setText(banner.getTitle());
        mBanner_content.setText(banner.getDescription());
        mBanner_close.setImageDrawable(FileUtil.getImageFromAssetsFile("img/sssss_vserv_close_advertisement.png", context));
        mWindowManager.addView(mBanner, params);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mBanner_close != null) {
                    mBanner_close.setVisibility(0);
                }
            }
        }, 20000);
        mBanner_close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BannerView.removeBannerView();
            }
        });
        mBanner.setOnClickListener(new AnonymousClass_3(banner, context));
    }

    public static void removeBannerView() {
        if (mWindowManager != null && mBanner != null) {
            mWindowManager.removeView(mBanner);
            mBanner = null;
            mBanner_title = null;
            mBanner_content = null;
            mBanner_close = null;
        }
    }
}