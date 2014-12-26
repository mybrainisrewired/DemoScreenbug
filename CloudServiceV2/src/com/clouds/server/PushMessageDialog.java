package com.clouds.server;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.clouds.object.NotificationInfo;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;

public class PushMessageDialog extends Activity {
    private static final int UPDATE_IMG = 1;
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
                    PushMessageDialog.this.imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    PushMessageDialog.this.mHandler.sendEmptyMessage(UPDATE_IMG);
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

    public PushMessageDialog() {
        this.imageBitmap = null;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_IMG:
                        if (PushMessageDialog.this.imageBitmap != null) {
                            PushMessageDialog.this.iv_icon.setImageBitmap(PushMessageDialog.this.imageBitmap);
                        }
                    default:
                        break;
                }
            }
        };
    }

    private void initData() {
        NotificationInfo notifyInfo = (NotificationInfo) getIntent().getSerializableExtra("notify");
        String title = notifyInfo.getTitle();
        String content = notifyInfo.getText();
        String link = notifyInfo.getLink();
        String imageUrl = "http://pica.nipic.com/2007-08-14/20078149520212_2.jpg";
        this.tv_title.setText(title);
        this.tv_content.setText(content);
        new ImageThread(imageUrl).start();
    }

    private void initView() {
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tv_content = (TextView) findViewById(R.id.tv_content);
        this.iv_icon = (ImageView) findViewById(R.id.iv_icon);
        this.iv_icon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PushMessageDialog.this.finish();
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(UPDATE_IMG);
        getWindow().setType(2003);
        getWindow().setFlags(SmileConstants.MAX_SHARED_STRING_VALUES, SmileConstants.MAX_SHARED_STRING_VALUES);
        WindowManager wm = (WindowManager) getSystemService("window");
        Display d = getWindowManager().getDefaultDisplay();
        LayoutParams p = getWindow().getAttributes();
        p.height = (int) (((double) d.getHeight()) * 0.3d);
        p.width = (int) (((double) d.getWidth()) * 0.3d);
        p.alpha = 1.0f;
        p.dimAmount = 0.0f;
        getWindow().setAttributes(p);
        getWindow().setGravity(Opcodes.CASTORE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_message);
        initView();
        initData();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == 4 && event.getRepeatCount() == 0) ? true : super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}