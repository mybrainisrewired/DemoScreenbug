package com.millennialmedia.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.mopub.common.Preconditions;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;

class BridgeMMMedia extends MMJSObject {
    private static final String PATH = "path";
    private static final String TAG = "BridgeMMMedia";
    private static Object pickerActivityObject;
    private String AVAILABLE_SOURCE_TYPES;
    private String GET_DEVICE_VOLUME;
    private String GET_PICTURE;
    private String IS_SOURCE_TYPE_AVAILABLE;
    private String PLAY_AUDIO;
    private String PLAY_SOUND;
    private String PLAY_VIDEO;
    private String STOP_AUDIO;
    private String WRITE_TO_PHOTO_LIBRARY;
    MediaScannerConnection mediaScanner;

    class AnonymousClass_1 implements MediaScannerConnectionClient {
        final /* synthetic */ String val$path;

        AnonymousClass_1(String str) {
            this.val$path = str;
        }

        public void onMediaScannerConnected() {
            if (BridgeMMMedia.this.mediaScanner != null) {
                BridgeMMMedia.this.mediaScanner.scanFile(this.val$path, null);
            }
        }

        public void onScanCompleted(String path, Uri uri) {
            if (uri == null) {
                MMLog.d(TAG, "Failed to scan " + path);
            }
        }
    }

    static class Audio implements OnCompletionListener {
        private static final int MAX_SOUNDS = 4;
        private static Audio sharedInstance;
        private OnLoadCompleteListener completionListener;
        CopyOnWriteArrayList<OnCompletionListener> completionListeners;
        private WeakReference<Context> contextRef;
        private MediaPlayer mediaPlayer;
        CopyOnWriteArrayList<PeriodicListener> periodicListeners;
        Runnable periodicUpdater;
        private SoundPool soundPool;

        private abstract class OnLoadCompleteListener {
            private static final int TEST_PERIOD_MS = 100;
            private ArrayList<Integer> sampleIds;
            private SoundPool soundPool;
            private Timer timer;

            public OnLoadCompleteListener(SoundPool soundPool) {
                this.sampleIds = new ArrayList();
                this.soundPool = soundPool;
            }

            abstract void onLoadComplete(SoundPool soundPool, int i, int i2);

            synchronized void release() {
                if (this.timer != null) {
                    this.timer.cancel();
                    this.timer.purge();
                }
            }

            synchronized void testSample(int sampleId) {
                this.sampleIds.add(Integer.valueOf(sampleId));
                if (this.sampleIds.size() == 1) {
                    this.timer = new Timer();
                    this.timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            ArrayList<Integer> completedOnes = new ArrayList();
                            Iterator i$ = OnLoadCompleteListener.this.sampleIds.iterator();
                            while (i$.hasNext()) {
                                Integer sampleId = (Integer) i$.next();
                                int streamId = OnLoadCompleteListener.this.soundPool.play(sampleId.intValue(), BitmapDescriptorFactory.HUE_RED, 0.0f, 0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                if (streamId != 0) {
                                    OnLoadCompleteListener.this.soundPool.stop(streamId);
                                    OnLoadCompleteListener.this.onLoadComplete(OnLoadCompleteListener.this.soundPool, sampleId.intValue(), 0);
                                    completedOnes.add(sampleId);
                                }
                            }
                            OnLoadCompleteListener.this.sampleIds.removeAll(completedOnes);
                            if (OnLoadCompleteListener.this.sampleIds.size() == 0) {
                                OnLoadCompleteListener.this.timer.cancel();
                                OnLoadCompleteListener.this.timer.purge();
                            }
                        }
                    }, 0, 100);
                }
            }
        }

        static interface PeriodicListener {
            void onUpdate(int i);
        }

        class AnonymousClass_2 extends OnLoadCompleteListener {
            AnonymousClass_2(SoundPool x0) {
                super(x0);
            }

            public synchronized void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (soundPool != null) {
                    Context context = (Context) Audio.this.contextRef.get();
                    if (context != null) {
                        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
                        float streamVolume = (((float) audioManager.getStreamVolume(MMAdView.TRANSITION_DOWN)) + 0.0f) / ((float) audioManager.getStreamMaxVolume(MMAdView.TRANSITION_DOWN));
                        soundPool.play(sampleId, streamVolume, streamVolume, 1, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    }
                }
            }
        }

        private Audio() {
            this.periodicUpdater = new Runnable() {
                public void run() {
                    if (Audio.this.mediaPlayer != null) {
                        if (Audio.this.mediaPlayer.isPlaying()) {
                            int currentPositionMillis = Audio.this.mediaPlayer.getCurrentPosition();
                            if (Audio.this.periodicListeners != null) {
                                Iterator i$ = Audio.this.periodicListeners.iterator();
                                while (i$.hasNext()) {
                                    ((PeriodicListener) i$.next()).onUpdate(currentPositionMillis);
                                }
                            }
                        }
                        MMSDK.runOnUiThreadDelayed(this, 500);
                    }
                }
            };
        }

        private Audio(Context context) {
            this.periodicUpdater = new Runnable() {
                public void run() {
                    if (Audio.this.mediaPlayer != null) {
                        if (Audio.this.mediaPlayer.isPlaying()) {
                            int currentPositionMillis = Audio.this.mediaPlayer.getCurrentPosition();
                            if (Audio.this.periodicListeners != null) {
                                Iterator i$ = Audio.this.periodicListeners.iterator();
                                while (i$.hasNext()) {
                                    ((PeriodicListener) i$.next()).onUpdate(currentPositionMillis);
                                }
                            }
                        }
                        MMSDK.runOnUiThreadDelayed(this, 500);
                    }
                }
            };
            this.contextRef = new WeakReference(context.getApplicationContext());
        }

        static synchronized Audio sharedAudio(Context context) {
            Audio audio;
            synchronized (Audio.class) {
                if (sharedInstance == null) {
                    sharedInstance = new Audio(context);
                }
                audio = sharedInstance;
            }
            return audio;
        }

        boolean addCompletionListener(OnCompletionListener listener) {
            if (this.completionListeners == null) {
                this.completionListeners = new CopyOnWriteArrayList();
            }
            return !this.completionListeners.contains(listener) ? this.completionListeners.add(listener) : false;
        }

        boolean addPeriodicListener(PeriodicListener listener) {
            if (this.periodicListeners == null) {
                this.periodicListeners = new CopyOnWriteArrayList();
            }
            return !this.periodicListeners.contains(listener) ? this.periodicListeners.add(listener) : false;
        }

        synchronized boolean isPlaying() {
            boolean z;
            z = this.mediaPlayer != null && this.mediaPlayer.isPlaying();
            return z;
        }

        public synchronized void onCompletion(MediaPlayer mp) {
            if (this.completionListeners != null) {
                Iterator i$ = this.completionListeners.iterator();
                while (i$.hasNext()) {
                    ((OnCompletionListener) i$.next()).onCompletion(this.mediaPlayer);
                }
            }
            if (this.mediaPlayer != null) {
                this.mediaPlayer.release();
                this.mediaPlayer = null;
            }
        }

        synchronized MMJSResponse playAudio(Uri uri, boolean loop) {
            try {
                if (this.mediaPlayer != null) {
                    this.mediaPlayer.release();
                    this.mediaPlayer = null;
                }
                this.mediaPlayer = MediaPlayer.create((Context) this.contextRef.get(), uri);
                this.mediaPlayer.setLooping(loop);
                this.mediaPlayer.start();
                this.mediaPlayer.setOnCompletionListener(this);
                MMSDK.runOnUiThread(this.periodicUpdater);
            } catch (Exception e) {
                MMLog.e(TAG, "Exception in playAudio method", e.getCause());
            }
            return MMJSResponse.responseWithSuccess("Audio playback started");
        }

        synchronized MMJSResponse playSound(File file) {
            try {
                if (this.soundPool == null) {
                    this.soundPool = new SoundPool(4, 3, 0);
                    this.completionListener = new AnonymousClass_2(this.soundPool);
                }
                this.completionListener.testSample(this.soundPool.load(file.getAbsolutePath(), 1));
            } catch (Exception e) {
            }
            return MMJSResponse.responseWithSuccess("Sound playback started");
        }

        boolean removeCompletionListener(OnCompletionListener listener) {
            return this.completionListeners != null ? this.completionListeners.remove(listener) : false;
        }

        boolean removePeriodicListener(PeriodicListener listener) {
            return this.periodicListeners != null ? this.periodicListeners.remove(listener) : false;
        }

        synchronized MMJSResponse stop() {
            if (this.mediaPlayer != null) {
                onCompletion(this.mediaPlayer);
            }
            if (this.soundPool != null) {
                this.soundPool.release();
                this.soundPool = null;
            }
            if (this.completionListener != null) {
                this.completionListener.release();
                this.completionListener = null;
            }
            return MMJSResponse.responseWithSuccess("Audio stopped");
        }
    }

    static class PickerActivity extends MMBaseActivity {
        private Uri fileUri;
        boolean hasRequestedPic;

        PickerActivity() {
            this.hasRequestedPic = false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void onActivityResult(int r25_requestCode, int r26_resultCode, android.content.Intent r27_data) {
            throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.BridgeMMMedia.PickerActivity.onActivityResult(int, int, android.content.Intent):void");
            /*
            r24 = this;
            super.onActivityResult(r25, r26, r27);
            if (r27 == 0) goto L_0x007a;
        L_0x0005:
            r18 = 0;
            r22 = 0;
            r3 = r27.getData();	 Catch:{ Exception -> 0x00b0 }
            if (r3 != 0) goto L_0x00d2;
        L_0x000f:
            r13 = r27.getExtras();	 Catch:{ Exception -> 0x00b0 }
            if (r13 == 0) goto L_0x007a;
        L_0x0015:
            r5 = r27.getExtras();	 Catch:{ Exception -> 0x00b0 }
            r6 = "data";
            r8 = r5.get(r6);	 Catch:{ Exception -> 0x00b0 }
            r8 = (android.graphics.Bitmap) r8;	 Catch:{ Exception -> 0x00b0 }
            r17 = new java.io.File;	 Catch:{ Exception -> 0x01c0 }
            r5 = r24.getIntent();	 Catch:{ Exception -> 0x01c0 }
            r5 = r5.getData();	 Catch:{ Exception -> 0x01c0 }
            r5 = r5.getPath();	 Catch:{ Exception -> 0x01c0 }
            r0 = r17;
            r0.<init>(r5);	 Catch:{ Exception -> 0x01c0 }
            r10 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x01c0 }
            r10.<init>();	 Catch:{ Exception -> 0x01c0 }
            r5 = android.graphics.Bitmap.CompressFormat.PNG;	 Catch:{ Exception -> 0x01c0 }
            r6 = 0;
            r8.compress(r5, r6, r10);	 Catch:{ Exception -> 0x01c0 }
            r9 = r10.toByteArray();	 Catch:{ Exception -> 0x01c0 }
            r11 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x01c0 }
            r11.<init>(r9);	 Catch:{ Exception -> 0x01c0 }
            r23 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x01c0 }
            r0 = r23;
            r1 = r17;
            r0.<init>(r1);	 Catch:{ Exception -> 0x01c0 }
            r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
            r12 = new byte[r5];	 Catch:{ Exception -> 0x0064, all -> 0x01bb }
        L_0x0055:
            r21 = r11.read(r12);	 Catch:{ Exception -> 0x0064, all -> 0x01bb }
            if (r21 <= 0) goto L_0x008b;
        L_0x005b:
            r5 = 0;
            r0 = r23;
            r1 = r21;
            r0.write(r12, r5, r1);	 Catch:{ Exception -> 0x0064, all -> 0x01bb }
            goto L_0x0055;
        L_0x0064:
            r16 = move-exception;
            r22 = r23;
        L_0x0067:
            r5 = "BridgeMMMedia";
            r6 = "Problem getting bitmap from data";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ all -> 0x00bb }
            if (r18 == 0) goto L_0x0075;
        L_0x0072:
            r18.close();	 Catch:{ Exception -> 0x00a5 }
        L_0x0075:
            if (r22 == 0) goto L_0x007a;
        L_0x0077:
            r22.close();	 Catch:{ Exception -> 0x00a5 }
        L_0x007a:
            r6 = pickerActivityObject;
            monitor-enter(r6);
            r5 = pickerActivityObject;	 Catch:{ all -> 0x019c }
            r5.notify();	 Catch:{ all -> 0x019c }
            monitor-exit(r6);	 Catch:{ all -> 0x019c }
            r24.finish();
            return;
        L_0x008b:
            if (r18 == 0) goto L_0x0090;
        L_0x008d:
            r18.close();	 Catch:{ Exception -> 0x0098 }
        L_0x0090:
            if (r23 == 0) goto L_0x0095;
        L_0x0092:
            r23.close();	 Catch:{ Exception -> 0x0098 }
        L_0x0095:
            r22 = r23;
            goto L_0x007a;
        L_0x0098:
            r16 = move-exception;
            r5 = "BridgeMMMedia";
            r6 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ Exception -> 0x019f }
            r22 = r23;
            goto L_0x007a;
        L_0x00a5:
            r16 = move-exception;
            r5 = "BridgeMMMedia";
            r6 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ Exception -> 0x00b0 }
            goto L_0x007a;
        L_0x00b0:
            r16 = move-exception;
        L_0x00b1:
            r5 = "BridgeMMMedia";
            r6 = "Error with picture: ";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);
            goto L_0x007a;
        L_0x00bb:
            r5 = move-exception;
        L_0x00bc:
            if (r18 == 0) goto L_0x00c1;
        L_0x00be:
            r18.close();	 Catch:{ Exception -> 0x00c7 }
        L_0x00c1:
            if (r22 == 0) goto L_0x00c6;
        L_0x00c3:
            r22.close();	 Catch:{ Exception -> 0x00c7 }
        L_0x00c6:
            throw r5;	 Catch:{ Exception -> 0x00b0 }
        L_0x00c7:
            r16 = move-exception;
            r6 = "BridgeMMMedia";
            r7 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r6, r7, r0);	 Catch:{ Exception -> 0x00b0 }
            goto L_0x00c6;
        L_0x00d2:
            if (r3 == 0) goto L_0x007a;
        L_0x00d4:
            r5 = 1;
            r4 = new java.lang.String[r5];	 Catch:{ Exception -> 0x00b0 }
            r5 = 0;
            r6 = "_data";
            r4[r5] = r6;	 Catch:{ Exception -> 0x00b0 }
            r2 = r24.getContentResolver();	 Catch:{ Exception -> 0x00b0 }
            if (r2 == 0) goto L_0x007a;
        L_0x00e2:
            r5 = 0;
            r6 = 0;
            r7 = 0;
            r15 = r2.query(r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x00b0 }
            if (r15 == 0) goto L_0x007a;
        L_0x00eb:
            r5 = "_data";
            r20 = r15.getColumnIndex(r5);	 Catch:{ Exception -> 0x00b0 }
            r5 = -1;
            r0 = r20;
            if (r0 == r5) goto L_0x007a;
        L_0x00f6:
            r15.moveToFirst();	 Catch:{ Exception -> 0x00b0 }
            r14 = new java.io.File;	 Catch:{ Exception -> 0x00b0 }
            r0 = r20;
            r5 = r15.getString(r0);	 Catch:{ Exception -> 0x00b0 }
            r14.<init>(r5);	 Catch:{ Exception -> 0x00b0 }
            r15.close();	 Catch:{ Exception -> 0x00b0 }
            r17 = new java.io.File;	 Catch:{ Exception -> 0x01b5 }
            r5 = r24.getIntent();	 Catch:{ Exception -> 0x01b5 }
            r5 = r5.getData();	 Catch:{ Exception -> 0x01b5 }
            r5 = r5.getPath();	 Catch:{ Exception -> 0x01b5 }
            r0 = r17;
            r0.<init>(r5);	 Catch:{ Exception -> 0x01b5 }
            r19 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x01b5 }
            r0 = r19;
            r0.<init>(r14);	 Catch:{ Exception -> 0x01b5 }
            r23 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x01b7, all -> 0x01ab }
            r0 = r23;
            r1 = r17;
            r0.<init>(r1);	 Catch:{ Exception -> 0x01b7, all -> 0x01ab }
            r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
            r12 = new byte[r5];	 Catch:{ Exception -> 0x013f, all -> 0x01af }
        L_0x012e:
            r0 = r19;
            r21 = r0.read(r12);	 Catch:{ Exception -> 0x013f, all -> 0x01af }
            if (r21 <= 0) goto L_0x0165;
        L_0x0136:
            r5 = 0;
            r0 = r23;
            r1 = r21;
            r0.write(r12, r5, r1);	 Catch:{ Exception -> 0x013f, all -> 0x01af }
            goto L_0x012e;
        L_0x013f:
            r16 = move-exception;
            r22 = r23;
            r18 = r19;
        L_0x0144:
            r5 = "BridgeMMMedia";
            r6 = "Error copying image";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ all -> 0x0185 }
            if (r18 == 0) goto L_0x0152;
        L_0x014f:
            r18.close();	 Catch:{ Exception -> 0x0159 }
        L_0x0152:
            if (r22 == 0) goto L_0x007a;
        L_0x0154:
            r22.close();	 Catch:{ Exception -> 0x0159 }
            goto L_0x007a;
        L_0x0159:
            r16 = move-exception;
            r5 = "BridgeMMMedia";
            r6 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ Exception -> 0x00b0 }
            goto L_0x007a;
        L_0x0165:
            if (r19 == 0) goto L_0x016a;
        L_0x0167:
            r19.close();	 Catch:{ Exception -> 0x0175 }
        L_0x016a:
            if (r23 == 0) goto L_0x016f;
        L_0x016c:
            r23.close();	 Catch:{ Exception -> 0x0175 }
        L_0x016f:
            r22 = r23;
            r18 = r19;
            goto L_0x007a;
        L_0x0175:
            r16 = move-exception;
            r5 = "BridgeMMMedia";
            r6 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r5, r6, r0);	 Catch:{ Exception -> 0x01a4 }
            r22 = r23;
            r18 = r19;
            goto L_0x007a;
        L_0x0185:
            r5 = move-exception;
        L_0x0186:
            if (r18 == 0) goto L_0x018b;
        L_0x0188:
            r18.close();	 Catch:{ Exception -> 0x0191 }
        L_0x018b:
            if (r22 == 0) goto L_0x0190;
        L_0x018d:
            r22.close();	 Catch:{ Exception -> 0x0191 }
        L_0x0190:
            throw r5;	 Catch:{ Exception -> 0x00b0 }
        L_0x0191:
            r16 = move-exception;
            r6 = "BridgeMMMedia";
            r7 = "Error closing file";
            r0 = r16;
            com.millennialmedia.android.MMLog.e(r6, r7, r0);	 Catch:{ Exception -> 0x00b0 }
            goto L_0x0190;
        L_0x019c:
            r5 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x019c }
            throw r5;
        L_0x019f:
            r16 = move-exception;
            r22 = r23;
            goto L_0x00b1;
        L_0x01a4:
            r16 = move-exception;
            r22 = r23;
            r18 = r19;
            goto L_0x00b1;
        L_0x01ab:
            r5 = move-exception;
            r18 = r19;
            goto L_0x0186;
        L_0x01af:
            r5 = move-exception;
            r22 = r23;
            r18 = r19;
            goto L_0x0186;
        L_0x01b5:
            r16 = move-exception;
            goto L_0x0144;
        L_0x01b7:
            r16 = move-exception;
            r18 = r19;
            goto L_0x0144;
        L_0x01bb:
            r5 = move-exception;
            r22 = r23;
            goto L_0x00bc;
        L_0x01c0:
            r16 = move-exception;
            goto L_0x0067;
            */
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getLastNonConfigurationInstance() != null) {
                this.hasRequestedPic = ((Bundle) getLastNonConfigurationInstance()).getBoolean("hasRequestedPic");
            }
            if (!this.hasRequestedPic) {
                Intent intent;
                if (getIntent().getStringExtra(AnalyticsSQLiteHelper.EVENT_LIST_TYPE).equalsIgnoreCase("Camera")) {
                    intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    this.fileUri = getIntent().getData();
                    intent.putExtra("return-data", true);
                    this.hasRequestedPic = true;
                    startActivityForResult(intent, 0);
                } else {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    this.hasRequestedPic = true;
                    startActivityForResult(intent, 0);
                }
            }
        }

        public Object onRetainNonConfigurationInstance() {
            super.onRetainNonConfigurationInstance();
            Bundle outState = new Bundle();
            outState.putBoolean("hasRequestedPic", this.hasRequestedPic);
            return outState;
        }
    }

    BridgeMMMedia() {
        this.IS_SOURCE_TYPE_AVAILABLE = "isSourceTypeAvailable";
        this.AVAILABLE_SOURCE_TYPES = "availableSourceTypes";
        this.GET_PICTURE = "getPicture";
        this.WRITE_TO_PHOTO_LIBRARY = "writeToPhotoLibrary";
        this.PLAY_VIDEO = "playVideo";
        this.STOP_AUDIO = "stopAudio";
        this.GET_DEVICE_VOLUME = "getDeviceVolume";
        this.PLAY_AUDIO = "playAudio";
        this.PLAY_SOUND = "playSound";
    }

    private static Bitmap centerOfImage(Bitmap image, int width, int height) {
        return cropImage(image, (int) ((float) ((image.getWidth() - width) / 2)), (int) ((float) ((image.getHeight() - height) / 2)), width, height);
    }

    private static Bitmap cropImage(Bitmap bitmap, int left, int top, int width, int height) {
        return Bitmap.createBitmap(bitmap, left, top, width, height);
    }

    private boolean isCameraAvailable() {
        Context context = (Context) this.contextRef.get();
        if (context == null || context.getPackageManager().checkPermission("android.permission.CAMERA", context.getPackageName()) != 0) {
            return false;
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        return context.getPackageManager().queryIntentActivities(intent, Cast.MAX_MESSAGE_LENGTH).size() > 0;
    }

    private boolean isPictureChooserAvailable() {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return false;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        return context.getPackageManager().queryIntentActivities(intent, Cast.MAX_MESSAGE_LENGTH).size() > 0;
    }

    private static Bitmap resizeImage(Bitmap image, int newW, int newH, int quality) {
        return Bitmap.createScaledBitmap(image, newW, newH, true);
    }

    static Bitmap resizeImage(Bitmap image, String contentMode, int toW, int toH, int quality) {
        float horizontalRatio = ((float) toW) / ((float) image.getWidth());
        float verticalRatio = ((float) toH) / ((float) image.getHeight());
        if (contentMode.equals("Center")) {
            return centerOfImage(image, toW, toH);
        }
        float ratio;
        if (contentMode.equals("ScaleToAspectFit")) {
            ratio = Math.min(horizontalRatio, verticalRatio);
            return resizeImage(image, (int) (((float) image.getWidth()) * ratio), (int) (((float) image.getHeight()) * ratio), quality);
        } else if (!contentMode.equals("ScaleToAspectFill")) {
            return resizeImage(image, toW, toH, quality);
        } else {
            ratio = Math.max(horizontalRatio, verticalRatio);
            return cropImage(resizeImage(image, (int) (((float) image.getWidth()) * ratio), (int) (((float) image.getHeight()) * ratio), quality), 0, 0, toW, toH);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final byte[] scaleBitmapToBytes(java.io.File r22_file, int r23_w, int r24_h, java.lang.String r25_contentMode) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.BridgeMMMedia.scaleBitmapToBytes(java.io.File, int, int, java.lang.String):byte[]");
        /*
        r10 = 0;
        r12 = 0;
        r17 = 0;
        r11 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x00d7 }
        r0 = r22;
        r11.<init>(r0);	 Catch:{ FileNotFoundException -> 0x00d7 }
        r16 = new android.graphics.BitmapFactory$Options;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r16.<init>();	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r19 = 1;
        r0 = r19;
        r1 = r16;
        r1.inJustDecodeBounds = r0;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r19 = 0;
        r0 = r19;
        r1 = r16;
        android.graphics.BitmapFactory.decodeStream(r11, r0, r1);	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r0 = r16;
        r14 = r0.outHeight;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r0 = r16;
        r0 = r0.outWidth;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r18 = r0;
        r15 = 1;
        r0 = r24;
        if (r14 > r0) goto L_0x0036;
    L_0x0030:
        r0 = r18;
        r1 = r23;
        if (r0 <= r1) goto L_0x0048;
    L_0x0036:
        r0 = r18;
        if (r0 <= r14) goto L_0x00b7;
    L_0x003a:
        r0 = (float) r14;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r19 = r0;
        r0 = r24;
        r0 = (float) r0;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r20 = r0;
        r19 = r19 / r20;
        r15 = java.lang.Math.round(r19);	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
    L_0x0048:
        r13 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r0 = r22;
        r13.<init>(r0);	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        r19 = 0;
        r0 = r19;
        r1 = r16;
        r1.inJustDecodeBounds = r0;	 Catch:{ FileNotFoundException -> 0x018c, all -> 0x0183 }
        r0 = r16;
        r0.inSampleSize = r15;	 Catch:{ FileNotFoundException -> 0x018c, all -> 0x0183 }
        r19 = 0;
        r0 = r19;
        r1 = r16;
        r17 = android.graphics.BitmapFactory.decodeStream(r13, r0, r1);	 Catch:{ FileNotFoundException -> 0x018c, all -> 0x0183 }
        if (r11 == 0) goto L_0x006a;
    L_0x0067:
        r11.close();	 Catch:{ IOException -> 0x00c8 }
    L_0x006a:
        if (r13 == 0) goto L_0x006f;
    L_0x006c:
        r13.close();	 Catch:{ IOException -> 0x00c8 }
    L_0x006f:
        r12 = r13;
        r10 = r11;
    L_0x0071:
        r7 = 0;
        if (r17 == 0) goto L_0x00b6;
    L_0x0074:
        r19 = 1;
        r0 = r17;
        r1 = r25;
        r2 = r23;
        r3 = r24;
        r4 = r19;
        r9 = resizeImage(r0, r1, r2, r3, r4);
        r5 = 0;
        r6 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x017d }
        r6.<init>();	 Catch:{ Exception -> 0x017d }
        r19 = "";
        r0 = r25;
        r1 = r19;
        r19 = r0.equals(r1);	 Catch:{ Exception -> 0x0121, all -> 0x017a }
        if (r19 == 0) goto L_0x0115;
    L_0x0096:
        r19 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0121, all -> 0x017a }
        r20 = 100;
        r0 = r17;
        r1 = r19;
        r2 = r20;
        r0.compress(r1, r2, r6);	 Catch:{ Exception -> 0x0121, all -> 0x017a }
    L_0x00a3:
        r7 = r6.toByteArray();	 Catch:{ Exception -> 0x0121, all -> 0x017a }
        if (r17 == 0) goto L_0x00ac;
    L_0x00a9:
        r17.recycle();	 Catch:{ Exception -> 0x014e }
    L_0x00ac:
        if (r9 == 0) goto L_0x00b1;
    L_0x00ae:
        r9.recycle();	 Catch:{ Exception -> 0x014e }
    L_0x00b1:
        if (r6 == 0) goto L_0x00b6;
    L_0x00b3:
        r6.close();	 Catch:{ Exception -> 0x014e }
    L_0x00b6:
        return r7;
    L_0x00b7:
        r0 = r18;
        r0 = (float) r0;
        r19 = r0;
        r0 = r23;
        r0 = (float) r0;
        r20 = r0;
        r19 = r19 / r20;
        r15 = java.lang.Math.round(r19);	 Catch:{ FileNotFoundException -> 0x0188, all -> 0x017f }
        goto L_0x0048;
    L_0x00c8:
        r8 = move-exception;
        r19 = "BridgeMMMedia";
        r20 = "Error closing file";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        r12 = r13;
        r10 = r11;
        goto L_0x0071;
    L_0x00d7:
        r8 = move-exception;
    L_0x00d8:
        r19 = "BridgeMMMedia";
        r20 = "Can't find file to scale bitmap";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);	 Catch:{ all -> 0x00fc }
        if (r10 == 0) goto L_0x00e8;
    L_0x00e5:
        r10.close();	 Catch:{ IOException -> 0x00ee }
    L_0x00e8:
        if (r12 == 0) goto L_0x0071;
    L_0x00ea:
        r12.close();	 Catch:{ IOException -> 0x00ee }
        goto L_0x0071;
    L_0x00ee:
        r8 = move-exception;
        r19 = "BridgeMMMedia";
        r20 = "Error closing file";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        goto L_0x0071;
    L_0x00fc:
        r19 = move-exception;
    L_0x00fd:
        if (r10 == 0) goto L_0x0102;
    L_0x00ff:
        r10.close();	 Catch:{ IOException -> 0x0108 }
    L_0x0102:
        if (r12 == 0) goto L_0x0107;
    L_0x0104:
        r12.close();	 Catch:{ IOException -> 0x0108 }
    L_0x0107:
        throw r19;
    L_0x0108:
        r8 = move-exception;
        r20 = "BridgeMMMedia";
        r21 = "Error closing file";
        r0 = r20;
        r1 = r21;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        goto L_0x0107;
    L_0x0115:
        r19 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ Exception -> 0x0121, all -> 0x017a }
        r20 = 100;
        r0 = r19;
        r1 = r20;
        r9.compress(r0, r1, r6);	 Catch:{ Exception -> 0x0121, all -> 0x017a }
        goto L_0x00a3;
    L_0x0121:
        r8 = move-exception;
        r5 = r6;
    L_0x0123:
        r7 = 0;
        r19 = "BridgeMMMedia";
        r20 = "Error scaling bitmap";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);	 Catch:{ all -> 0x015c }
        if (r17 == 0) goto L_0x0134;
    L_0x0131:
        r17.recycle();	 Catch:{ Exception -> 0x0140 }
    L_0x0134:
        if (r9 == 0) goto L_0x0139;
    L_0x0136:
        r9.recycle();	 Catch:{ Exception -> 0x0140 }
    L_0x0139:
        if (r5 == 0) goto L_0x00b6;
    L_0x013b:
        r5.close();	 Catch:{ Exception -> 0x0140 }
        goto L_0x00b6;
    L_0x0140:
        r8 = move-exception;
        r19 = "BridgeMMMedia";
        r20 = "Error closing file";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        goto L_0x00b6;
    L_0x014e:
        r8 = move-exception;
        r19 = "BridgeMMMedia";
        r20 = "Error closing file";
        r0 = r19;
        r1 = r20;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        goto L_0x00b6;
    L_0x015c:
        r19 = move-exception;
    L_0x015d:
        if (r17 == 0) goto L_0x0162;
    L_0x015f:
        r17.recycle();	 Catch:{ Exception -> 0x016d }
    L_0x0162:
        if (r9 == 0) goto L_0x0167;
    L_0x0164:
        r9.recycle();	 Catch:{ Exception -> 0x016d }
    L_0x0167:
        if (r5 == 0) goto L_0x016c;
    L_0x0169:
        r5.close();	 Catch:{ Exception -> 0x016d }
    L_0x016c:
        throw r19;
    L_0x016d:
        r8 = move-exception;
        r20 = "BridgeMMMedia";
        r21 = "Error closing file";
        r0 = r20;
        r1 = r21;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        goto L_0x016c;
    L_0x017a:
        r19 = move-exception;
        r5 = r6;
        goto L_0x015d;
    L_0x017d:
        r8 = move-exception;
        goto L_0x0123;
    L_0x017f:
        r19 = move-exception;
        r10 = r11;
        goto L_0x00fd;
    L_0x0183:
        r19 = move-exception;
        r12 = r13;
        r10 = r11;
        goto L_0x00fd;
    L_0x0188:
        r8 = move-exception;
        r10 = r11;
        goto L_0x00d8;
    L_0x018c:
        r8 = move-exception;
        r12 = r13;
        r10 = r11;
        goto L_0x00d8;
        */
    }

    private void scanMedia(String path) {
        Context context = (Context) this.contextRef.get();
        if (context != null) {
            this.mediaScanner = new MediaScannerConnection(context.getApplicationContext(), new AnonymousClass_1(path));
            if (this.mediaScanner != null) {
                this.mediaScanner.connect();
            }
        }
    }

    public MMJSResponse availableSourceTypes(Map<String, String> arguments) {
        JSONArray jsonArray = new JSONArray();
        if (isCameraAvailable()) {
            jsonArray.put("Camera");
        }
        if (isPictureChooserAvailable()) {
            jsonArray.put("Photo Library");
        }
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = jsonArray;
        return response;
    }

    MMJSResponse executeCommand(String name, Map<String, String> arguments) {
        if (this.IS_SOURCE_TYPE_AVAILABLE.equals(name)) {
            return isSourceTypeAvailable(arguments);
        }
        if (this.AVAILABLE_SOURCE_TYPES.equals(name)) {
            return availableSourceTypes(arguments);
        }
        if (this.GET_PICTURE.equals(name)) {
            return getPicture(arguments);
        }
        if (this.WRITE_TO_PHOTO_LIBRARY.equals(name)) {
            return writeToPhotoLibrary(arguments);
        }
        if (this.PLAY_VIDEO.equals(name)) {
            return playVideo(arguments);
        }
        if (this.STOP_AUDIO.equals(name)) {
            return stopAudio(arguments);
        }
        if (this.GET_DEVICE_VOLUME.equals(name)) {
            return getDeviceVolume(arguments);
        }
        if (this.PLAY_AUDIO.equals(name)) {
            return playAudio(arguments);
        }
        return this.PLAY_SOUND.equals(name) ? playSound(arguments) : null;
    }

    public MMJSResponse getDeviceVolume(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        if (context == null) {
            return MMJSResponse.responseWithError("no volume available");
        }
        int volume = MMSDK.getMediaVolume(context);
        MMJSResponse response = MMJSResponse.responseWithSuccess();
        response.response = Integer.valueOf(volume);
        return response;
    }

    public synchronized MMJSResponse getPicture(Map<String, String> arguments) {
        MMJSResponse responseWithError;
        try {
            Context context = (Context) this.contextRef.get();
            String type = (String) arguments.get("sourceType");
            String height = (String) arguments.get("constrainHeight");
            String width = (String) arguments.get("constrainWidth");
            String contentMode = (String) arguments.get("contentMode");
            if (contentMode == null) {
                contentMode = Preconditions.EMPTY_ARGUMENTS;
            }
            if (height == null || width == null) {
                responseWithError = MMJSResponse.responseWithError("Missing constrainHeight and/or constrainWidth");
            } else {
                int h = (int) Float.parseFloat(height);
                int w = (int) Float.parseFloat(width);
                if (h * w > 360000) {
                    responseWithError = MMJSResponse.responseWithError("constrainHeight * constrainWidth > 360000");
                } else {
                    if (!(context == null || type == null)) {
                        File file = new File(AdCache.getInternalCacheDirectory(context), "tmp_mm_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                        if (!(type.equalsIgnoreCase("Camera") && isCameraAvailable())) {
                            if (!type.equalsIgnoreCase("Photo Library")) {
                            }
                        }
                        try {
                            pickerActivityObject = new Object();
                            synchronized (pickerActivityObject) {
                                IntentUtils.startPickerActivity(context, file, type);
                                pickerActivityObject.wait();
                            }
                            pickerActivityObject = null;
                        } catch (Exception e) {
                            MMLog.e(TAG, "Error with pickerActivity synchronization", e);
                            pickerActivityObject = null;
                        }
                        if (file != null && file.exists() && file.length() > 0) {
                            byte[] contents = scaleBitmapToBytes(file, w, h, contentMode);
                            file.delete();
                            if (contents != null) {
                                responseWithError = new MMJSResponse();
                                responseWithError.result = 1;
                                responseWithError.dataResponse = contents;
                            }
                        }
                    }
                    responseWithError = null;
                }
            }
        } catch (Throwable th) {
        }
        return responseWithError;
    }

    public MMJSResponse isSourceTypeAvailable(Map<String, String> arguments) {
        String type = (String) arguments.get("sourceType");
        if (type != null) {
            if (type.equalsIgnoreCase("Camera") && isCameraAvailable()) {
                return MMJSResponse.responseWithSuccess("true");
            }
            if (type.equalsIgnoreCase("Photo Library") && isPictureChooserAvailable()) {
                return MMJSResponse.responseWithSuccess("true");
            }
        }
        return MMJSResponse.responseWithError("false");
    }

    public MMJSResponse playAudio(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String path = (String) arguments.get(PATH);
        if (context == null || path == null) {
            return null;
        }
        Audio audio = Audio.sharedAudio(context);
        if (audio == null) {
            return null;
        }
        if (audio.isPlaying()) {
            return MMJSResponse.responseWithError("Audio already playing.");
        }
        if (path.startsWith("http")) {
            return audio.playAudio(Uri.parse(path), Boolean.parseBoolean((String) arguments.get("repeat")));
        }
        File file = AdCache.getDownloadFile(context, path);
        return file.exists() ? audio.playAudio(Uri.fromFile(file), Boolean.parseBoolean((String) arguments.get("repeat"))) : null;
    }

    public MMJSResponse playSound(Map<String, String> arguments) {
        if (this.contextRef == null) {
            return null;
        }
        Context context = (Context) this.contextRef.get();
        String path = (String) arguments.get(PATH);
        if (!(context == null || path == null)) {
            File file = AdCache.getDownloadFile(context, path);
            if (file.exists()) {
                Audio audio = Audio.sharedAudio((Context) this.contextRef.get());
                if (audio != null) {
                    return audio.playSound(file);
                }
            }
        }
        return null;
    }

    public MMJSResponse playVideo(Map<String, String> arguments) {
        Context context = (Context) this.contextRef.get();
        String path = (String) arguments.get(PATH);
        if (context == null || path == null || !path.startsWith("http")) {
            File file = AdCache.getDownloadFile(context, path);
            if (file.exists()) {
                IntentUtils.startVideoPlayerActivityWithData(context, file);
                return MMJSResponse.responseWithSuccess(file.getName());
            }
        } else {
            IntentUtils.startVideoPlayerActivityWithData(context, path);
            return MMJSResponse.responseWithSuccess(path);
        }
        return null;
    }

    public MMJSResponse stopAudio(Map<String, String> arguments) {
        if (this.contextRef != null) {
            Audio audio = Audio.sharedAudio((Context) this.contextRef.get());
            if (audio != null) {
                return audio.stop();
            }
        }
        return null;
    }

    public synchronized MMJSResponse writeToPhotoLibrary(Map<String, String> arguments) {
        MMJSResponse mMJSResponse;
        Context context = (Context) this.contextRef.get();
        String pathString = (String) arguments.get(PATH);
        if (context == null || TextUtils.isEmpty(pathString)) {
            mMJSResponse = null;
        } else {
            Uri path = Uri.parse((String) arguments.get(PATH));
            String subDirectory = "Pictures";
            String name = path.getLastPathSegment();
            String scheme = path.getScheme();
            if (scheme == null || !scheme.equals("http") || AdCache.downloadComponentExternalStorage(path.toString(), "Pictures", name, context)) {
                File source = AdCache.getDownloadFile(context, "Pictures", path.getLastPathSegment());
                if (source.exists()) {
                    scanMedia(source.getAbsolutePath());
                    mMJSResponse = !AdCache.isExternalMounted() ? MMJSResponse.responseWithError("Storage not mounted, cannot add image to photo library photo library") : MMJSResponse.responseWithSuccess("Image saved to photo library");
                } else {
                    mMJSResponse = MMJSResponse.responseWithError("No file at " + source.getAbsolutePath());
                }
            } else {
                mMJSResponse = MMJSResponse.responseWithError("Failed to download");
            }
        }
        return mMJSResponse;
    }
}