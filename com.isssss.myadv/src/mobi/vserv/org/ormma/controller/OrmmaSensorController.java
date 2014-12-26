package mobi.vserv.org.ormma.controller;

import android.content.Context;
import android.webkit.JavascriptInterface;
import mobi.vserv.org.ormma.controller.listeners.AccelListener;
import mobi.vserv.org.ormma.view.OrmmaView;

public class OrmmaSensorController extends OrmmaController {
    final int INTERVAL;
    private AccelListener mAccel;
    private float mLastX;
    private float mLastY;
    private float mLastZ;

    public OrmmaSensorController(OrmmaView adView, Context context) {
        super(adView, context);
        this.INTERVAL = 1000;
        this.mLastX = 0.0f;
        this.mLastY = 0.0f;
        this.mLastZ = 0.0f;
        this.mAccel = new AccelListener(context, this);
    }

    @JavascriptInterface
    public float getHeading() {
        return this.mAccel.getHeading();
    }

    @JavascriptInterface
    public String getTilt() {
        return new StringBuilder("{ x : \"").append(this.mLastX).append("\", y : \"").append(this.mLastY).append("\", z : \"").append(this.mLastZ).append("\"}").toString();
    }

    public void onHeadingChange(float f) {
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ heading: ").append((int) (((double) f) * 57.29577951308232d)).append("});").toString());
    }

    public void onShake() {
        this.mOrmmaView.injectJavaScript("window.ormmaview.fireShakeEvent()");
    }

    public void onTilt(float x, float y, float z) {
        this.mLastX = x;
        this.mLastY = y;
        this.mLastZ = z;
        this.mOrmmaView.injectJavaScript(new StringBuilder("window.ormmaview.fireChangeEvent({ tilt: ").append(getTilt()).append("})").toString());
    }

    public void startHeadingListener() {
        this.mAccel.startTrackingHeading();
    }

    public void startShakeListener() {
        this.mAccel.startTrackingShake();
    }

    public void startTiltListener() {
        this.mAccel.startTrackingTilt();
    }

    void stop() {
    }

    public void stopAllListeners() {
        this.mAccel.stopAllListeners();
    }

    public void stopHeadingListener() {
        this.mAccel.stopTrackingHeading();
    }

    public void stopShakeListener() {
        this.mAccel.stopTrackingShake();
    }

    public void stopTiltListener() {
        this.mAccel.stopTrackingTilt();
    }
}