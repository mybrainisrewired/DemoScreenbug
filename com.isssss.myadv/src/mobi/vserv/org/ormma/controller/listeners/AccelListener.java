package mobi.vserv.org.ormma.controller.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.millennialmedia.android.MMAdView;
import java.util.List;
import mobi.vserv.org.ormma.controller.OrmmaSensorController;

public class AccelListener implements SensorEventListener {
    private static final int FORCE_THRESHOLD = 1000;
    private static final int SHAKE_COUNT = 2;
    private static final int SHAKE_DURATION = 2000;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int TIME_THRESHOLD = 100;
    private boolean bAccReady;
    private boolean bMagReady;
    private float[] mAccVals;
    private float[] mActualOrientation;
    String mKey;
    private float[] mLastAccVals;
    private long mLastForce;
    private long mLastShake;
    private long mLastTime;
    private float[] mMagVals;
    OrmmaSensorController mSensorController;
    private int mSensorDelay;
    private int mShakeCount;
    int registeredHeadingListeners;
    int registeredShakeListeners;
    int registeredTiltListeners;
    private SensorManager sensorManager;

    public AccelListener(Context ctx, OrmmaSensorController sensorController) {
        this.registeredTiltListeners = 0;
        this.registeredShakeListeners = 0;
        this.registeredHeadingListeners = 0;
        this.mSensorDelay = 3;
        this.mAccVals = new float[]{0.0f, 0.0f, 0.0f};
        this.mLastAccVals = new float[]{0.0f, 0.0f, 0.0f};
        this.mActualOrientation = new float[]{-1.0f, -1.0f, -1.0f};
        this.mSensorController = sensorController;
        this.sensorManager = (SensorManager) ctx.getSystemService("sensor");
    }

    private void start() {
        List<Sensor> list = this.sensorManager.getSensorList(1);
        if (list.size() > 0) {
            this.sensorManager.registerListener(this, (Sensor) list.get(0), this.mSensorDelay);
        }
    }

    private void startMag() {
        List<Sensor> list = this.sensorManager.getSensorList(SHAKE_COUNT);
        if (list.size() > 0) {
            this.sensorManager.registerListener(this, (Sensor) list.get(0), this.mSensorDelay);
            start();
        }
    }

    public float getHeading() {
        return this.mActualOrientation[0];
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case MMAdView.TRANSITION_FADE:
                this.mLastAccVals = this.mAccVals;
                this.mAccVals = (float[]) event.values.clone();
                this.bAccReady = true;
                break;
            case SHAKE_COUNT:
                this.mMagVals = (float[]) event.values.clone();
                this.bMagReady = true;
                break;
        }
        if (this.mMagVals != null && this.mAccVals != null && this.bAccReady && this.bMagReady) {
            this.bAccReady = false;
            this.bMagReady = false;
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, new float[9], this.mAccVals, this.mMagVals);
            this.mActualOrientation = new float[3];
            SensorManager.getOrientation(R, this.mActualOrientation);
            this.mSensorController.onHeadingChange(this.mActualOrientation[0]);
        }
        if (event.sensor.getType() == 1) {
            long now = System.currentTimeMillis();
            if (now - this.mLastForce > 500) {
                this.mShakeCount = 0;
            }
            if (now - this.mLastTime > 100) {
                if ((Math.abs(((((this.mAccVals[0] + this.mAccVals[1]) + this.mAccVals[2]) - this.mLastAccVals[0]) - this.mLastAccVals[1]) - this.mLastAccVals[2]) / ((float) (now - this.mLastTime))) * 10000.0f > 1000.0f) {
                    int i = this.mShakeCount + 1;
                    this.mShakeCount = i;
                    if (i >= 2 && now - this.mLastShake > 2000) {
                        this.mLastShake = now;
                        this.mShakeCount = 0;
                        this.mSensorController.onShake();
                    }
                    this.mLastForce = now;
                }
                this.mLastTime = now;
                this.mSensorController.onTilt(this.mAccVals[0], this.mAccVals[1], this.mAccVals[2]);
            }
        }
    }

    public void setSensorDelay(int delay) {
        this.mSensorDelay = delay;
        if (this.registeredTiltListeners > 0 || this.registeredShakeListeners > 0) {
            stop();
            start();
        }
    }

    public void startTrackingHeading() {
        if (this.registeredHeadingListeners == 0) {
            startMag();
        }
        this.registeredHeadingListeners++;
    }

    public void startTrackingShake() {
        if (this.registeredShakeListeners == 0) {
            setSensorDelay(1);
            start();
        }
        this.registeredShakeListeners++;
    }

    public void startTrackingTilt() {
        if (this.registeredTiltListeners == 0) {
            start();
        }
        this.registeredTiltListeners++;
    }

    public void stop() {
        if (this.registeredHeadingListeners == 0 && this.registeredShakeListeners == 0 && this.registeredTiltListeners == 0) {
            this.sensorManager.unregisterListener(this);
        }
    }

    public void stopAllListeners() {
        this.registeredTiltListeners = 0;
        this.registeredShakeListeners = 0;
        this.registeredHeadingListeners = 0;
        try {
            stop();
        } catch (Exception e) {
        }
    }

    public void stopTrackingHeading() {
        if (this.registeredHeadingListeners > 0) {
            int i = this.registeredHeadingListeners - 1;
            this.registeredHeadingListeners = i;
            if (i == 0) {
                stop();
            }
        }
    }

    public void stopTrackingShake() {
        if (this.registeredShakeListeners > 0) {
            int i = this.registeredShakeListeners - 1;
            this.registeredShakeListeners = i;
            if (i == 0) {
                setSensorDelay(MMAdView.TRANSITION_DOWN);
                stop();
            }
        }
    }

    public void stopTrackingTilt() {
        if (this.registeredTiltListeners > 0) {
            int i = this.registeredTiltListeners - 1;
            this.registeredTiltListeners = i;
            if (i == 0) {
                stop();
            }
        }
    }
}