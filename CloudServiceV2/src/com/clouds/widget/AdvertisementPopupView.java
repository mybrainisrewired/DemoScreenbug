package com.clouds.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.clouds.object.NotificationInfo;
import com.clouds.server.R;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AdvertisementPopupView {
    private static final int UPDATE_IMG = 1;
    private Context context;
    private Bitmap imageBitmap;
    private ImageView iv_icon;
    Handler mHandler;
    private TextView tv_content;
    private TextView tv_title;

    class ImageThread extends Thread {
        private String sUrl;

        public ImageThread(String sUrl) {
            this.sUrl = sUrl;
        }

        public void run() {
            super.run();
            try {
                URL url = new URL(this.sUrl);
                try {
                    AdvertisementPopupView.this.imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    AdvertisementPopupView.this.mHandler.sendEmptyMessage(UPDATE_IMG);
                } catch (MalformedURLException e) {
                    e = e;
                    e.printStackTrace();
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                }
            } catch (MalformedURLException e3) {
                MalformedURLException e4 = e3;
                e4.printStackTrace();
            } catch (IOException e5) {
                IOException e6 = e5;
                e6.printStackTrace();
            }
        }
    }

    public AdvertisementPopupView(Context context) {
        this.context = null;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_IMG:
                        if (AdvertisementPopupView.this.imageBitmap != null) {
                            AdvertisementPopupView.this.iv_icon.setImageBitmap(AdvertisementPopupView.this.imageBitmap);
                        }
                    default:
                        break;
                }
            }
        };
        this.tv_title = null;
        this.tv_content = null;
        this.iv_icon = null;
        this.imageBitmap = null;
        this.context = context;
    }

    private void initView(View view) {
        this.tv_title = (TextView) view.findViewById(R.id.tv_title);
        this.tv_content = (TextView) view.findViewById(R.id.tv_content);
        this.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        this.iv_icon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            }
        });
    }

    public void initData(NotificationInfo notifyInfo) {
        String title = notifyInfo.getTitle();
        String content = notifyInfo.getText();
        String link = notifyInfo.getLink();
        String imageUrl = "http://pica.nipic.com/2007-08-14/20078149520212_2.jpg";
        this.tv_title.setText(title);
        this.tv_content.setText(content);
        new ImageThread(imageUrl).start();
    }

    public void showPopupView(int advertisementType) {
        View layout = LayoutInflater.from(this.context).inflate(R.layout.push_message, null);
        WindowManager wm = (WindowManager) this.context.getSystemService("window");
        LayoutParams params = AdvertisementLayout.params;
        params.type = 2007;
        params.flags = 40;
        params.width = -2;
        params.height = -2;
        params.alpha = 1.0f;
        params.gravity = 85;
        params.x = 0;
        params.y = 0;
        initView(layout);
        wm.addView(layout, params);
    }
}