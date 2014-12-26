package com.google.android.gms.internal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.view.MotionEvent;
import java.util.Iterator;
import java.util.Map;

public final class dr {
    private final Context mContext;
    private int mState;
    private String rh;
    private final float ri;
    private float rj;
    private float rk;
    private float rl;

    class AnonymousClass_1 implements OnClickListener {
        final /* synthetic */ String rm;

        AnonymousClass_1(String str) {
            this.rm = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            dr.this.mContext.startActivity(Intent.createChooser(new Intent("android.intent.action.SEND").setType("text/plain").putExtra("android.intent.extra.TEXT", this.rm), "Share via"));
        }
    }

    public dr(Context context) {
        this.mState = 0;
        this.mContext = context;
        this.ri = context.getResources().getDisplayMetrics().density;
    }

    public dr(Context context, String str) {
        this(context);
        this.rh = str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(int r8, float r9, float r10) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.dr.a(int, float, float):void");
        /*
        r7 = this;
        r6 = 3;
        r5 = -1;
        r4 = 2;
        r3 = 1;
        if (r8 != 0) goto L_0x0010;
    L_0x0006:
        r0 = 0;
        r7.mState = r0;
        r7.rj = r9;
        r7.rk = r10;
        r7.rl = r10;
    L_0x000f:
        return;
    L_0x0010:
        r0 = r7.mState;
        if (r0 == r5) goto L_0x000f;
    L_0x0014:
        if (r8 != r4) goto L_0x0092;
    L_0x0016:
        r0 = r7.rk;
        r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x002f;
    L_0x001c:
        r7.rk = r10;
    L_0x001e:
        r0 = r7.rk;
        r1 = r7.rl;
        r0 = r0 - r1;
        r1 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r2 = r7.ri;
        r1 = r1 * r2;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 <= 0) goto L_0x0038;
    L_0x002c:
        r7.mState = r5;
        goto L_0x000f;
    L_0x002f:
        r0 = r7.rl;
        r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1));
        if (r0 >= 0) goto L_0x001e;
    L_0x0035:
        r7.rl = r10;
        goto L_0x001e;
    L_0x0038:
        r0 = r7.mState;
        if (r0 == 0) goto L_0x0040;
    L_0x003c:
        r0 = r7.mState;
        if (r0 != r4) goto L_0x0066;
    L_0x0040:
        r0 = r7.rj;
        r0 = r9 - r0;
        r1 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r2 = r7.ri;
        r1 = r1 * r2;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 < 0) goto L_0x0055;
    L_0x004d:
        r7.rj = r9;
        r0 = r7.mState;
        r0 = r0 + 1;
        r7.mState = r0;
    L_0x0055:
        r0 = r7.mState;
        if (r0 == r3) goto L_0x005d;
    L_0x0059:
        r0 = r7.mState;
        if (r0 != r6) goto L_0x0084;
    L_0x005d:
        r0 = r7.rj;
        r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x000f;
    L_0x0063:
        r7.rj = r9;
        goto L_0x000f;
    L_0x0066:
        r0 = r7.mState;
        if (r0 == r3) goto L_0x006e;
    L_0x006a:
        r0 = r7.mState;
        if (r0 != r6) goto L_0x0055;
    L_0x006e:
        r0 = r7.rj;
        r0 = r9 - r0;
        r1 = -1035468800; // 0xffffffffc2480000 float:-50.0 double:NaN;
        r2 = r7.ri;
        r1 = r1 * r2;
        r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1));
        if (r0 > 0) goto L_0x0055;
    L_0x007b:
        r7.rj = r9;
        r0 = r7.mState;
        r0 = r0 + 1;
        r7.mState = r0;
        goto L_0x0055;
    L_0x0084:
        r0 = r7.mState;
        if (r0 != r4) goto L_0x000f;
    L_0x0088:
        r0 = r7.rj;
        r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1));
        if (r0 >= 0) goto L_0x000f;
    L_0x008e:
        r7.rj = r9;
        goto L_0x000f;
    L_0x0092:
        if (r8 != r3) goto L_0x000f;
    L_0x0094:
        r0 = r7.mState;
        r1 = 4;
        if (r0 != r1) goto L_0x000f;
    L_0x0099:
        r7.showDialog();
        goto L_0x000f;
        */
    }

    private void showDialog() {
        Object obj;
        if (TextUtils.isEmpty(this.rh)) {
            obj = "No debug information";
        } else {
            Uri build = new Builder().encodedQuery(this.rh).build();
            StringBuilder stringBuilder = new StringBuilder();
            Map b = dq.b(build);
            Iterator it = b.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                stringBuilder.append(str).append(" = ").append((String) b.get(str)).append("\n\n");
            }
            obj = stringBuilder.toString().trim();
            if (TextUtils.isEmpty(obj)) {
                obj = "No debug information";
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setMessage(obj);
        builder.setTitle("Ad Information");
        builder.setPositiveButton("Share", new AnonymousClass_1(obj));
        builder.setNegativeButton("Close", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void c(MotionEvent motionEvent) {
        int historySize = motionEvent.getHistorySize();
        int i = 0;
        while (i < historySize) {
            a(motionEvent.getActionMasked(), motionEvent.getHistoricalX(0, i), motionEvent.getHistoricalY(0, i));
            i++;
        }
        a(motionEvent.getActionMasked(), motionEvent.getX(), motionEvent.getY());
    }

    public void t(String str) {
        this.rh = str;
    }
}