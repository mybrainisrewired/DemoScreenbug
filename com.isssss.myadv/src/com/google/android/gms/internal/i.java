package com.google.android.gms.internal;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class i implements h {
    protected MotionEvent jN;
    protected DisplayMetrics jO;
    protected n jP;
    private o jQ;

    protected i(Context context, n nVar, o oVar) {
        this.jP = nVar;
        this.jQ = oVar;
        try {
            this.jO = context.getResources().getDisplayMetrics();
        } catch (UnsupportedOperationException e) {
            this.jO = new DisplayMetrics();
            this.jO.density = 1.0f;
        }
    }

    private String a(Context context, String str, boolean z) {
        int i = ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES;
        try {
            byte[] u;
            synchronized (this) {
                t();
                if (z) {
                    c(context);
                } else {
                    b(context);
                }
                u = u();
            }
            return u.length == 0 ? Integer.toString(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES) : a(u, str);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toString(i);
        } catch (UnsupportedEncodingException e2) {
            return Integer.toString(i);
        } catch (IOException e3) {
            return Integer.toString(MMAdView.TRANSITION_DOWN);
        }
    }

    private void t() {
        this.jQ.reset();
    }

    private byte[] u() throws IOException {
        return this.jQ.z();
    }

    public String a(Context context) {
        return a(context, null, false);
    }

    public String a(Context context, String str) {
        return a(context, str, true);
    }

    String a(byte[] bArr, String str) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        byte[] bArr2;
        if (bArr.length > 239) {
            t();
            a((int)ApiEventType.API_MRAID_GET_MAX_SIZE, 1);
            bArr = u();
        }
        if (bArr.length < 239) {
            bArr2 = new byte[(239 - bArr.length)];
            new SecureRandom().nextBytes(bArr2);
            bArr2 = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).put(bArr2).array();
        } else {
            bArr2 = ByteBuffer.allocate(240).put((byte) bArr.length).put(bArr).array();
        }
        MessageDigest instance = MessageDigest.getInstance("MD5");
        instance.update(bArr2);
        bArr2 = ByteBuffer.allocate(AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY).put(instance.digest()).put(bArr2).array();
        byte[] bArr3 = new byte[256];
        new f().a(bArr2, bArr3);
        if (str != null && str.length() > 0) {
            a(str, bArr3);
        }
        return this.jP.a(bArr3, true);
    }

    public void a(int i, int i2, int i3) {
        if (this.jN != null) {
            this.jN.recycle();
        }
        this.jN = MotionEvent.obtain(0, (long) i3, 1, ((float) i) * this.jO.density, ((float) i2) * this.jO.density, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 0, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 0, 0);
    }

    protected void a(int i, long j) throws IOException {
        this.jQ.b(i, j);
    }

    protected void a(int i, String str) throws IOException {
        this.jQ.b(i, str);
    }

    public void a(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (this.jN != null) {
                this.jN.recycle();
            }
            this.jN = MotionEvent.obtain(motionEvent);
        }
    }

    void a(String str, byte[] bArr) throws UnsupportedEncodingException {
        if (str.length() > 32) {
            str = str.substring(0, ApiEventType.API_MRAID_PLAY_AUDIO);
        }
        new km(str.getBytes("UTF-8")).m(bArr);
    }

    protected abstract void b(Context context);

    protected abstract void c(Context context);
}