package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.content.IntentFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import com.google.ads.AdSize;
import com.millennialmedia.android.MMAdView;
import mobi.vserv.org.ormma.controller.OrmmaController.Dimensions;
import mobi.vserv.org.ormma.controller.OrmmaController.Properties;
import mobi.vserv.org.ormma.controller.util.OrmmaConfigurationBroadcastReceiver;
import mobi.vserv.org.ormma.view.OrmmaView;
import org.json.JSONException;
import org.json.JSONObject;

public class OrmmaDisplayController extends OrmmaController {
    private boolean bMaxSizeSet;
    private OrmmaConfigurationBroadcastReceiver mBroadCastReceiver;
    private float mDensity;
    private int mMaxHeight;
    private int mMaxWidth;
    private WindowManager mWindowManager;

    public OrmmaDisplayController(OrmmaView adView, Context c) {
        super(adView, c);
        this.bMaxSizeSet = false;
        this.mMaxWidth = -1;
        this.mMaxHeight = -1;
        DisplayMetrics metrics = new DisplayMetrics();
        this.mWindowManager = (WindowManager) c.getSystemService("window");
        this.mWindowManager.getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
    }

    private Dimensions getDeviceDimensions(Dimensions d) {
        d.width = (int) (((float) d.width) * this.mDensity);
        d.height = (int) (((float) d.height) * this.mDensity);
        d.x = (int) (((float) d.x) * this.mDensity);
        d.y = (int) (((float) d.y) * this.mDensity);
        if (d.height < 0) {
            d.height = this.mOrmmaView.getHeight();
        }
        if (d.width < 0) {
            d.width = this.mOrmmaView.getWidth();
        }
        int[] loc = new int[2];
        this.mOrmmaView.getLocationInWindow(loc);
        if (d.x < 0) {
            d.x = loc[0];
        }
        if (d.y < 0) {
            d.y = loc[1] - 0;
            Log.i("vserv", new StringBuilder("JS d.y:: ").append(d.y).toString());
        }
        return d;
    }

    @JavascriptInterface
    public void VideoType(String t_type) {
        this.mOrmmaView.setVideoType(t_type);
    }

    @JavascriptInterface
    public void close() {
        this.mOrmmaView.close();
    }

    public String dimensions() {
        return new StringBuilder("{ \"top\" :").append((int) (((float) this.mOrmmaView.getTop()) / this.mDensity)).append(",").append("\"left\" :").append((int) (((float) this.mOrmmaView.getLeft()) / this.mDensity)).append(",").append("\"bottom\" :").append((int) (((float) this.mOrmmaView.getBottom()) / this.mDensity)).append(",").append("\"right\" :").append((int) (((float) this.mOrmmaView.getRight()) / this.mDensity)).append("}").toString();
    }

    @JavascriptInterface
    public void expand(String dimensions, String URL, String properties) {
        try {
            this.mOrmmaView.expand(getDeviceDimensions((Dimensions) getFromJSON(new JSONObject(dimensions), Dimensions.class)), URL, (Properties) getFromJSON(new JSONObject(properties), Properties.class));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        } catch (InstantiationException e4) {
            e4.printStackTrace();
        } catch (JSONException e5) {
            e5.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getMaxSize() {
        return this.bMaxSizeSet ? new StringBuilder("{ width: ").append(this.mMaxWidth).append(", ").append("height: ").append(this.mMaxHeight).append("}").toString() : getScreenSize();
    }

    @JavascriptInterface
    public int getOrientation() {
        switch (this.mWindowManager.getDefaultDisplay().getOrientation()) {
            case MMAdView.TRANSITION_NONE:
                return 0;
            case MMAdView.TRANSITION_FADE:
                return AdSize.LARGE_AD_HEIGHT;
            case MMAdView.TRANSITION_UP:
                return 180;
            case MMAdView.TRANSITION_DOWN:
                return 270;
            default:
                return -1;
        }
    }

    @JavascriptInterface
    public String getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.mWindowManager.getDefaultDisplay().getMetrics(metrics);
        return new StringBuilder("{ width: ").append((int) (((float) metrics.widthPixels) / metrics.density)).append(", ").append("height: ").append((int) (((float) metrics.heightPixels) / metrics.density)).append("}").toString();
    }

    @JavascriptInterface
    public String getSize() {
        return this.mOrmmaView.getSize();
    }

    @JavascriptInterface
    public void hide() {
        this.mOrmmaView.hide();
    }

    public boolean isVisible() {
        return this.mOrmmaView.getVisibility() == 0;
    }

    @JavascriptInterface
    public void logHTML(String html) {
    }

    public void onOrientationChanged(int orientation) {
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ orientation: ").append(orientation).append("});").toString());
    }

    @JavascriptInterface
    public void open(String url, boolean back, boolean forward, boolean refresh) {
        if (URLUtil.isValidUrl(url)) {
            this.mOrmmaView.open(url, back, forward, refresh);
        } else {
            this.mOrmmaView.raiseError("Invalid url", "open");
        }
    }

    @JavascriptInterface
    public void playAudio(String url, boolean autoPlay, boolean controls, boolean loop, boolean position, String startStyle, String stopStyle) {
        if (Defines.ENABLE_lOGGING) {
            Log.d("post", new StringBuilder("playAudio: url: ").append(url).append(" autoPlay: ").append(autoPlay).append(" controls: ").append(controls).append(" loop: ").append(loop).append(" position: ").append(position).append(" startStyle: ").append(startStyle).append(" stopStyle: ").append(stopStyle).toString());
        }
        if (URLUtil.isValidUrl(url)) {
            this.mOrmmaView.playAudio(url, autoPlay, controls, loop, position, startStyle, stopStyle);
        } else {
            this.mOrmmaView.raiseError("Invalid url", "playAudio");
        }
    }

    @JavascriptInterface
    public void playVideo(String url, boolean audioMuted, boolean autoPlay, boolean controls, boolean loop, int[] position, String startStyle, String stopStyle) {
        if (Defines.ENABLE_lOGGING) {
            Log.i("vserv", new StringBuilder("playVideo: url: ").append(url).append(" audioMuted: ").append(audioMuted).append(" autoPlay: ").append(autoPlay).append(" controls: ").append(controls).append(" loop: ").append(loop).append(" x: ").append(position[0]).append(" y: ").append(position[1]).append(" width: ").append(position[2]).append(" height: ").append(position[3]).append(" startStyle: ").append(startStyle).append(" stopStyle: ").append(stopStyle).toString());
        }
        Dimensions dimensions = null;
        if (position[0] != -1) {
            dimensions = new Dimensions();
            dimensions.x = position[0];
            dimensions.y = position[1];
            dimensions.width = position[2];
            dimensions.height = position[3];
            dimensions = getDeviceDimensions(dimensions);
        }
        if (URLUtil.isValidUrl(url)) {
            this.mOrmmaView.playVideo(url, audioMuted, autoPlay, controls, loop, dimensions, startStyle, stopStyle);
        } else {
            this.mOrmmaView.raiseError("Invalid url", "playVideo");
        }
    }

    @JavascriptInterface
    public void resize(int width, int height) {
        Log.i("vserv", new StringBuilder("resize: width: ").append(width).append(" height: ").append(height).toString());
        if ((this.mMaxHeight <= 0 || height <= this.mMaxHeight) && (this.mMaxWidth <= 0 || width <= this.mMaxWidth)) {
            this.mOrmmaView.resize((int) (this.mDensity * ((float) width)), (int) (this.mDensity * ((float) height)));
        } else {
            this.mOrmmaView.raiseError("Maximum size exceeded", "resize");
        }
    }

    @JavascriptInterface
    public void setMaxSize(int w, int h) {
        this.bMaxSizeSet = true;
        this.mMaxWidth = w;
        this.mMaxHeight = h;
    }

    @JavascriptInterface
    public void show() {
        this.mOrmmaView.show();
    }

    @JavascriptInterface
    public void showClose() {
        this.mOrmmaView.showClose();
    }

    public void startConfigurationListener() {
        try {
            if (this.mBroadCastReceiver == null) {
                this.mBroadCastReceiver = new OrmmaConfigurationBroadcastReceiver(this);
            }
            this.mContext.registerReceiver(this.mBroadCastReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
        } catch (Exception e) {
        }
    }

    public void stopAllListeners() {
        stopConfigurationListener();
        this.mBroadCastReceiver = null;
    }

    public void stopConfigurationListener() {
        try {
            this.mContext.unregisterReceiver(this.mBroadCastReceiver);
        } catch (Exception e) {
        }
    }

    @JavascriptInterface
    public void vservsns(String invokeUrl, String[] exitUrl, String shareUrl, String successMessage) {
        this.mOrmmaView.callOrmmaOpen(invokeUrl, exitUrl, shareUrl, successMessage);
    }
}