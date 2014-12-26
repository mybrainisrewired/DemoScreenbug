package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;

public class EdgeEffectCompat {
    private static final EdgeEffectImpl IMPL;
    private Object mEdgeEffect;

    static interface EdgeEffectImpl {
        boolean draw(Object obj, Canvas canvas);

        void finish(Object obj);

        boolean isFinished(Object obj);

        Object newEdgeEffect(Context context);

        boolean onAbsorb(Object obj, int i);

        boolean onPull(Object obj, float f);

        boolean onRelease(Object obj);

        void setSize(Object obj, int i, int i2);
    }

    static class BaseEdgeEffectImpl implements EdgeEffectImpl {
        BaseEdgeEffectImpl() {
        }

        public boolean draw(Object edgeEffect, Canvas canvas) {
            return false;
        }

        public void finish(Object edgeEffect) {
        }

        public boolean isFinished(Object edgeEffect) {
            return true;
        }

        public Object newEdgeEffect(Context context) {
            return null;
        }

        public boolean onAbsorb(Object edgeEffect, int velocity) {
            return false;
        }

        public boolean onPull(Object edgeEffect, float deltaDistance) {
            return false;
        }

        public boolean onRelease(Object edgeEffect) {
            return false;
        }

        public void setSize(Object edgeEffect, int width, int height) {
        }
    }

    static class EdgeEffectIcsImpl implements EdgeEffectImpl {
        EdgeEffectIcsImpl() {
        }

        public boolean draw(Object edgeEffect, Canvas canvas) {
            return EdgeEffectCompatIcs.draw(edgeEffect, canvas);
        }

        public void finish(Object edgeEffect) {
            EdgeEffectCompatIcs.finish(edgeEffect);
        }

        public boolean isFinished(Object edgeEffect) {
            return EdgeEffectCompatIcs.isFinished(edgeEffect);
        }

        public Object newEdgeEffect(Context context) {
            return EdgeEffectCompatIcs.newEdgeEffect(context);
        }

        public boolean onAbsorb(Object edgeEffect, int velocity) {
            return EdgeEffectCompatIcs.onAbsorb(edgeEffect, velocity);
        }

        public boolean onPull(Object edgeEffect, float deltaDistance) {
            return EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
        }

        public boolean onRelease(Object edgeEffect) {
            return EdgeEffectCompatIcs.onRelease(edgeEffect);
        }

        public void setSize(Object edgeEffect, int width, int height) {
            EdgeEffectCompatIcs.setSize(edgeEffect, width, height);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new EdgeEffectIcsImpl();
        } else {
            IMPL = new BaseEdgeEffectImpl();
        }
    }

    public EdgeEffectCompat(Context context) {
        this.mEdgeEffect = IMPL.newEdgeEffect(context);
    }

    public boolean draw(Canvas canvas) {
        return IMPL.draw(this.mEdgeEffect, canvas);
    }

    public void finish() {
        IMPL.finish(this.mEdgeEffect);
    }

    public boolean isFinished() {
        return IMPL.isFinished(this.mEdgeEffect);
    }

    public boolean onAbsorb(int velocity) {
        return IMPL.onAbsorb(this.mEdgeEffect, velocity);
    }

    public boolean onPull(float deltaDistance) {
        return IMPL.onPull(this.mEdgeEffect, deltaDistance);
    }

    public boolean onRelease() {
        return IMPL.onRelease(this.mEdgeEffect);
    }

    public void setSize(int width, int height) {
        IMPL.setSize(this.mEdgeEffect, width, height);
    }
}