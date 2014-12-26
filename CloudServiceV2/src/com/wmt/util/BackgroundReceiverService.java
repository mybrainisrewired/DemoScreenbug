package com.wmt.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.remotectrl.DiscoveryListener;
import com.wmt.remotectrl.EventPacket;
import com.wmt.remotectrl.EventPacket.DataWrapper;
import com.wmt.remotectrl.ServerAcceptedChannelSelector;
import com.wmt.remotectrl.ServerAcceptedChannelSelector.DataWrapperListener;
import java.io.ByteArrayOutputStream;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class BackgroundReceiverService implements DataWrapperListener {
    private static final int CONNECT_INTERRUPT = 401;
    private static final int CONNECT_SUCCUSS = 400;
    private static final String TAG = "BackgroundReceiverService";
    private final int DO_SNAPSHOT;
    private Context mContext;
    Handler mHandler;
    BroadcastReceiver mReceiver;
    private ServerAcceptedChannelSelector mSelector;
    private boolean mSnapshotEnd;

    public BackgroundReceiverService() {
        this.DO_SNAPSHOT = 1;
        this.mSnapshotEnd = true;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    new Thread(new Runnable() {
                        public void run() {
                            boolean requiresRotation = false;
                            boolean z = true;
                            try {
                                AnonymousClass_1.this.this$0.mSnapshotEnd = false;
                                Display mDisplay = ((WindowManager) AnonymousClass_1.this.this$0.mContext.getSystemService("window")).getDefaultDisplay();
                                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                                Matrix mDisplayMatrix = new Matrix();
                                mDisplay.getRealMetrics(mDisplayMetrics);
                                float[] dims = new float[]{(float) mDisplayMetrics.widthPixels, (float) mDisplayMetrics.heightPixels};
                                float degrees = AnonymousClass_1.this.this$0.getDegreesForRotation(mDisplay.getRotation());
                                if (degrees > 0.0f) {
                                    requiresRotation = z;
                                }
                                if (requiresRotation) {
                                    mDisplayMatrix.reset();
                                    mDisplayMatrix.preRotate(-degrees);
                                    mDisplayMatrix.mapPoints(dims);
                                    dims[0] = Math.abs(dims[0]);
                                    dims[1] = Math.abs(dims[1]);
                                }
                                Log.d(TAG, "DO snapshot now" + dims[0] + " , " + dims[1]);
                                Bitmap bitmap = Surface.screenshot((int) dims[0], (int) dims[1]);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                if (bitmap != null) {
                                    bitmap.compress(CompressFormat.JPEG, 75, baos);
                                    AnonymousClass_1.this.this$0.mSelector.setWriteDataBuffer(Opcodes.DSUB, baos.toByteArray());
                                    Log.d(TAG, "Finished sending" + bitmap.getByteCount());
                                } else {
                                    Log.d(TAG, "Snapshot failed.");
                                }
                                AnonymousClass_1.this.this$0.mSnapshotEnd = z;
                            } catch (Exception e) {
                                e.printStackTrace();
                                AnonymousClass_1.this.this$0.mSnapshotEnd = z;
                            }
                        }
                    }).start();
                }
            }
        };
    }

    private float getDegreesForRotation(int value) {
        switch (value) {
            case LocalAudioAll.SORT_BY_TITLE:
                return 90.0f;
            case ClassWriter.COMPUTE_FRAMES:
                return 270.0f;
            case JsonWriteContext.STATUS_OK_AFTER_SPACE:
                return 180.0f;
            default:
                return 0.0f;
        }
    }

    public void handleDataWrapper(DataWrapper dw) {
        Intent intent;
        if (dw.mId == 8712) {
            intent = new Intent();
            intent.setAction(EventPacket.INPUT_MSG);
            intent.putExtra("message", new String(dw.mData));
            this.mContext.sendBroadcast(intent);
        } else if (dw.mId == 4) {
            if (this.mSnapshotEnd) {
                this.mHandler.sendEmptyMessage(1);
            }
        } else if (dw.mId == 400) {
            bundle = new Bundle();
            bundle.putBoolean("state", true);
            bundle.putString("ip", new String(dw.mData));
            intent = new Intent();
            intent.setAction("com.wmt.aircontrol.connection");
            intent.putExtras(bundle);
            this.mContext.sendBroadcast(intent);
        } else if (dw.mId == 401) {
            bundle = new Bundle();
            bundle.putBoolean("state", false);
            bundle.putString("ip", new String(dw.mData));
            intent = new Intent();
            intent.setAction("com.wmt.aircontrol.connection");
            intent.putExtras(bundle);
            this.mContext.sendBroadcast(intent);
        }
    }

    public void init(Context context) {
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EventPacket.INPUT_HIDE);
        intentFilter.addAction(EventPacket.INPUT_SHOW);
        intentFilter.addAction(EventPacket.SYNC_MSG);
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() {
                /* JADX WARNING: inconsistent code. */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onReceive(android.content.Context r7_context, android.content.Intent r8_intent) {
                    throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.BackgroundReceiverService.AnonymousClass_2.onReceive(android.content.Context, android.content.Intent):void");
                    /*
                    r6 = this;
                    r5 = 8712; // 0x2208 float:1.2208E-41 double:4.3043E-320;
                    r0 = r8.getAction();
                    r2 = "com.wmt.remotectrlime.input.hide";
                    r2 = r0.equals(r2);
                    if (r2 == 0) goto L_0x0031;
                L_0x000e:
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    if (r2 == 0) goto L_0x0030;
                L_0x0016:
                    r2 = "BackgroundReceiverService";
                    r3 = "Dismiss IME";
                    android.util.Log.d(r2, r3);
                    r2 = "com.wmt.remotectrlime.input.hide";
                    r2 = r2.getBytes();
                    r1 = com.wmt.remotectrl.EventPacket.getSimpleDataFormat(r5, r2);
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    r2.setWriteDataBuffer(r1);
                L_0x0030:
                    return;
                L_0x0031:
                    r2 = "com.wmt.remotectrlime.input.show";
                    r2 = r0.equals(r2);
                    if (r2 == 0) goto L_0x005c;
                L_0x0039:
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    if (r2 == 0) goto L_0x0030;
                L_0x0041:
                    r2 = "BackgroundReceiverService";
                    r3 = "Display IME";
                    android.util.Log.d(r2, r3);
                    r2 = "com.wmt.remotectrlime.input.show";
                    r2 = r2.getBytes();
                    r1 = com.wmt.remotectrl.EventPacket.getSimpleDataFormat(r5, r2);
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    r2.setWriteDataBuffer(r1);
                    goto L_0x0030;
                L_0x005c:
                    r2 = "com.wmt.remotectrlime.sync_msg";
                    r2 = r0.equals(r2);
                    if (r2 == 0) goto L_0x0030;
                L_0x0064:
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    if (r2 == 0) goto L_0x0030;
                L_0x006c:
                    r2 = "BackgroundReceiverService";
                    r3 = new java.lang.StringBuilder;
                    r3.<init>();
                    r4 = "SYNC MSG=";
                    r3 = r3.append(r4);
                    r4 = "syncInfo";
                    r4 = r8.getStringExtra(r4);
                    r3 = r3.append(r4);
                    r3 = r3.toString();
                    android.util.Log.d(r2, r3);
                    r2 = "syncInfo";
                    r2 = r8.getStringExtra(r2);
                    r2 = r2.getBytes();
                    r1 = com.wmt.remotectrl.EventPacket.getSimpleDataFormat(r5, r2);
                    r2 = com.wmt.util.BackgroundReceiverService.this;
                    r2 = r2.mSelector;
                    r2.setWriteDataBuffer(r1);
                    goto L_0x0030;
                    */
                }
            };
            this.mContext.registerReceiver(this.mReceiver, intentFilter);
        }
        Display display = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
        this.mSelector = new ServerAcceptedChannelSelector(8714, this, display.getRawWidth(), display.getRawHeight());
        this.mSelector.start();
        new DiscoveryListener().start();
        SystemProperties.set("ctl.start", "httpserv");
    }
}