package com.inmobi.re.controller;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.location.LocationRequest;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.InMobi;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.inmobi.commons.internal.ApiStatCollector;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.internal.InternalSDKUtil;
import com.inmobi.commons.internal.Log;
import com.inmobi.commons.metric.EventLog;
import com.inmobi.re.configs.Initializer;
import com.inmobi.re.container.IMWebView;
import com.inmobi.re.container.mraidimpl.AudioTriggerCallback;
import com.inmobi.re.container.mraidimpl.AudioTriggerer;
import com.inmobi.re.controller.JSController.Dimensions;
import com.inmobi.re.controller.util.Constants;
import com.inmobi.re.controller.util.ImageProcessing;
import com.inmobi.re.controller.util.StartActivityForResultCallback;
import com.isssss.myadv.dao.BannerInfoTable;
import com.isssss.myadv.dao.PushIdsTable;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import mobi.vserv.android.ads.VservConstants;
import mobi.vserv.org.ormma.view.OrmmaView;
import org.json.JSONException;
import org.json.JSONObject;

public class JSUtilityController extends JSController {
    public static SimpleDateFormat[] calendarUntiFormats;
    public static SimpleDateFormat[] formats;
    DownloadManager a;
    Object b;
    int c;
    private JSDisplayController d;
    private boolean e;
    private Map<String, Boolean> f;
    private AudioTriggerCallback g;
    private boolean h;
    private boolean i;

    class b extends BroadcastReceiver {
        b() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.DOWNLOAD_COMPLETE".equals(intent.getAction())) {
                long longExtra = intent.getLongExtra("extra_download_id", 0);
                Query query = new Query();
                query.setFilterById(new long[]{longExtra});
                Cursor query2 = JSUtilityController.this.query(query);
                if (query2.moveToFirst()) {
                    int columnIndex = query2.getColumnIndex("status");
                    if (16 == query2.getInt(columnIndex)) {
                        JSUtilityController.this.imWebView.raiseError("download failed", "storePicture");
                    } else if (8 != query2.getInt(columnIndex)) {
                    }
                }
            }
        }
    }

    class f extends Thread {
        final /* synthetic */ String a;

        f(String str) {
            this.a = str;
        }

        public void run() {
            Throwable th;
            HttpURLConnection httpURLConnection = null;
            try {
                String replaceAll = this.a.replaceAll("%25", "%");
                Log.debug(Constants.RENDERING_LOG_TAG, "Pinging URL: " + replaceAll);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(replaceAll).openConnection();
                try {
                    httpURLConnection2.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT);
                    httpURLConnection2.setRequestMethod("GET");
                    httpURLConnection2.setRequestProperty("User-Agent", InternalSDKUtil.getUserAgent());
                    Log.debug(Constants.RENDERING_LOG_TAG, "Async Ping Connection Response Code: " + httpURLConnection2.getResponseCode());
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                } catch (Exception e) {
                    httpURLConnection = httpURLConnection2;
                    th = e;
                    Log.debug(Constants.RENDERING_LOG_TAG, "Error doing async Ping. ", th);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (Throwable th2) {
                    httpURLConnection = httpURLConnection2;
                    th = th2;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                th = e2;
                try {
                    Log.debug(Constants.RENDERING_LOG_TAG, "Error doing async Ping. ", th);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    throw th;
                }
            }
        }
    }

    class g extends TimerTask {
        g() {
        }

        public void run() {
            try {
                JSUtilityController.this.imWebView.raiseVibrateCompleteEvent();
            } catch (Exception e) {
                Log.internal(Constants.RENDERING_LOG_TAG, "Vibrate callback execption", e);
            }
        }
    }

    class a implements StartActivityForResultCallback {
        final /* synthetic */ String a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        a(String str, String str2, String str3) {
            this.a = str;
            this.b = str2;
            this.c = str3;
        }

        public void onActivityResult(int i, Intent intent) {
            try {
                if (JSUtilityController.this.a("android.permission.READ_CALENDAR") && JSUtilityController.this.a("android.permission.WRITE_CALENDAR")) {
                    if (JSUtilityController.this.c == JSUtilityController.this.a()) {
                        Log.internal(Constants.RENDERING_LOG_TAG, "User cancelled the create calendar event");
                    } else {
                        Uri uri;
                        if (VERSION.SDK_INT >= 8) {
                            uri = Uri.parse("content://com.android.calendar/events");
                        } else {
                            uri = Uri.parse("content://calendar/events");
                        }
                        ContentResolver contentResolver = JSUtilityController.this.imWebView.getActivity().getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        if (this.a.equals("tentative")) {
                            contentValues.put("eventStatus", Integer.valueOf(0));
                        } else if (this.a.equals("confirmed")) {
                            contentValues.put("eventStatus", Integer.valueOf(1));
                        } else if (this.a.equals("cancelled")) {
                            contentValues.put("eventStatus", Integer.valueOf(MMAdView.TRANSITION_UP));
                        }
                        contentResolver.update(ContentUris.withAppendedId(uri, (long) JSUtilityController.this.a()), contentValues, null, null);
                        if (this.b != null && !Preconditions.EMPTY_ARGUMENTS.equals(this.b)) {
                            int parseInt;
                            try {
                                parseInt = Integer.parseInt(this.b) / 60000;
                            } catch (NumberFormatException e) {
                                try {
                                    parseInt = ((int) (JSUtilityController.convertDateString(this.b).getTimeInMillis() - JSUtilityController.convertDateString(this.c).getTimeInMillis())) / 60000;
                                } catch (Exception e2) {
                                    JSUtilityController.this.imWebView.raiseError("Reminder format is incorrect. Invalid date", "createCalendarEvent");
                                }
                            }
                            if (parseInt > 0) {
                                JSUtilityController.this.imWebView.raiseError("Reminder format is incorrect. Reminder can be set only before the event starts", "createCalendarEvent");
                            } else {
                                Uri parse;
                                int i2 = -parseInt;
                                contentValues = new ContentValues();
                                contentValues.put("hasAlarm", Integer.valueOf(1));
                                contentResolver.update(ContentUris.withAppendedId(uri, (long) JSUtilityController.this.a()), contentValues, null, null);
                                if (VERSION.SDK_INT >= 8) {
                                    parse = Uri.parse("content://com.android.calendar/reminders");
                                } else {
                                    parse = Uri.parse("content://calendar/reminders");
                                }
                                ContentValues contentValues2 = new ContentValues();
                                contentValues2.put("event_id", Integer.valueOf(JSUtilityController.this.a()));
                                contentValues2.put("method", Integer.valueOf(1));
                                contentValues2.put("minutes", Integer.valueOf(i2));
                                contentResolver.insert(parse, contentValues2);
                            }
                        }
                    }
                }
            } catch (Exception e3) {
                Throwable th = e3;
                th.printStackTrace();
                Log.internal(Constants.RENDERING_LOG_TAG, "Exception adding reminder", th);
            }
        }
    }

    class c implements StartActivityForResultCallback {
        c() {
        }

        public void onActivityResult(int i, Intent intent) {
            if (i == -1) {
                Bitmap compressedBitmap = ImageProcessing.getCompressedBitmap(ImageProcessing.convertMediaUriToPath(intent.getData(), JSUtilityController.this.mContext), JSUtilityController.this.mContext);
                int width = compressedBitmap.getWidth();
                int height = compressedBitmap.getHeight();
                JSUtilityController.this.imWebView.raiseGalleryImageSelectedEvent(ImageProcessing.getBase64EncodedImage(compressedBitmap, JSUtilityController.this.mContext), width, height);
            } else {
                JSUtilityController.this.imWebView.raiseError("User did not select a picture", "getGalleryImage");
            }
        }
    }

    class d implements StartActivityForResultCallback {
        final /* synthetic */ Uri a;

        d(Uri uri) {
            this.a = uri;
        }

        public void onActivityResult(int i, Intent intent) {
            if (i == -1) {
                String convertMediaUriToPath;
                if (intent == null) {
                    convertMediaUriToPath = ImageProcessing.convertMediaUriToPath(this.a, JSUtilityController.this.mContext);
                } else {
                    convertMediaUriToPath = ImageProcessing.convertMediaUriToPath(intent.getData(), JSUtilityController.this.mContext);
                }
                Bitmap compressedBitmap = ImageProcessing.getCompressedBitmap(convertMediaUriToPath, JSUtilityController.this.mContext);
                int width = compressedBitmap.getWidth();
                int height = compressedBitmap.getHeight();
                JSUtilityController.this.imWebView.raiseCameraPictureCapturedEvent(ImageProcessing.getBase64EncodedImage(compressedBitmap, JSUtilityController.this.mContext), width, height);
            } else {
                JSUtilityController.this.imWebView.raiseError("User did not take a picture", "takeCameraPicture");
            }
        }
    }

    class e implements StartActivityForResultCallback {
        e() {
        }

        public void onActivityResult(int i, Intent intent) {
        }
    }

    static {
        formats = new SimpleDateFormat[]{new SimpleDateFormat("yyyy-MM-dd'T'hh:mmZ", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.ENGLISH), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH), new SimpleDateFormat("yyyyMMddHHmmssZ", Locale.ENGLISH), new SimpleDateFormat("yyyyMMddHHmm", Locale.ENGLISH), new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH), new SimpleDateFormat("yyyyMM", Locale.ENGLISH), new SimpleDateFormat("yyyy", Locale.ENGLISH)};
        calendarUntiFormats = new SimpleDateFormat[]{new SimpleDateFormat("yyyyMMdd'T'HHmmssZ"), new SimpleDateFormat("yyyyMMdd'T'HHmm"), new SimpleDateFormat("yyyyMMdd")};
    }

    public JSUtilityController(IMWebView iMWebView, Context context) {
        super(iMWebView, context);
        this.a = null;
        this.e = false;
        this.c = 0;
        this.f = new HashMap();
        this.g = new g(this);
        this.h = false;
        this.i = false;
        this.d = new JSDisplayController(iMWebView, context);
        iMWebView.addJavascriptInterface(this.d, "displayController");
    }

    private int a() {
        Cursor query;
        String[] strArr = new String[]{PushIdsTable.COLUMN_ID, BannerInfoTable.COLUMN_TITLE};
        if (VERSION.SDK_INT >= 8) {
            query = this.imWebView.getActivity().getContentResolver().query(Uri.parse("content://com.android.calendar/events"), strArr, null, null, null);
        } else {
            query = this.imWebView.getActivity().getContentResolver().query(Uri.parse("content://calendar/events"), strArr, null, null, null);
        }
        if (query != null && query.moveToLast()) {
            int columnIndex = query.getColumnIndex(BannerInfoTable.COLUMN_TITLE);
            int columnIndex2 = query.getColumnIndex(PushIdsTable.COLUMN_ID);
            String string = query.getString(columnIndex);
            String string2 = query.getString(columnIndex2);
            if (string != null) {
                return Integer.parseInt(string2);
            }
        }
        return 0;
    }

    private int a(String str, int i) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return i;
        }
    }

    private String b(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith("tel:")) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder("tel:");
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private int[] b() {
        int i = 0;
        int[] iArr = new int[2];
        try {
            FrameLayout frameLayout = (FrameLayout) ((ViewGroup) this.imWebView.getOriginalParent()).getRootView().findViewById(16908290);
            iArr[0] = (int) (((float) frameLayout.getWidth()) / this.imWebView.getDensity());
            iArr[1] = (int) (((float) frameLayout.getHeight()) / this.imWebView.getDensity());
        } catch (Exception e) {
            iArr[1] = i;
            iArr[i] = i;
        }
        return iArr;
    }

    private void c() {
        if (this.i) {
            registerMicListener();
        }
    }

    private void c(String str) {
        new f(str).start();
    }

    public static GregorianCalendar convertDateString(String str) {
        SimpleDateFormat[] simpleDateFormatArr = formats;
        int i = 0;
        while (i < simpleDateFormatArr.length) {
            try {
                Date parse = simpleDateFormatArr[i].parse(str);
                Calendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(parse);
                return (GregorianCalendar) gregorianCalendar;
            } catch (Exception e) {
                i++;
            }
        }
        return null;
    }

    private void d() {
        this.i = this.h;
        unRegisterMicListener();
    }

    boolean a(String str) {
        PackageManager packageManager = this.imWebView.getActivity().getPackageManager();
        return packageManager.checkPermission(str, packageManager.getNameForUid(Binder.getCallingUid())) == 0;
    }

    @JavascriptInterface
    public void asyncPing(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(31), null));
        try {
            Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> asyncPing: url: " + str);
            if (URLUtil.isValidUrl(str)) {
                c(str);
            } else {
                this.imWebView.raiseError("Invalid url", "asyncPing");
            }
        } catch (Exception e) {
        }
    }

    @JavascriptInterface
    public void cancelSaveContent(String str) {
        this.imWebView.cancelSaveContent(str);
    }

    @JavascriptInterface
    public void closeVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(50), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> closeVideo: id :" + str);
        this.imWebView.closeVideo(str);
    }

    @JavascriptInterface
    public void createCalendarEvent(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        try {
            if (a("android.permission.READ_CALENDAR") && a("android.permission.WRITE_CALENDAR")) {
                this.c = a();
            }
            ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(30), null));
            Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> createEvent: date: " + str2 + " location: " + str4 + " body: " + str5);
            if (supports(Event.INTENT_CALENDAR_EVENT)) {
                Calendar convertDateString = convertDateString(str2);
                Calendar convertDateString2 = convertDateString(str3);
                if (convertDateString == null || convertDateString2 == null) {
                    Log.debug(Constants.RENDERING_LOG_TAG, "exception");
                    this.imWebView.raiseError("Date format is incorrect", "createCalendarEvent");
                } else {
                    Intent intent = new Intent(this.imWebView.getActivity(), IMBrowserActivity.class);
                    intent.putExtra(IMBrowserActivity.EXTRA_BROWSER_ACTIVITY_TYPE, LocationRequest.PRIORITY_HIGH_ACCURACY);
                    intent.putExtra(AnalyticsEvent.EVENT_ID, IMBrowserActivity.generateId(new a(str7, str10, str2)));
                    intent.putExtra("eventId", str);
                    intent.putExtra(OrmmaView.ACTION_KEY, "createCalendarEvent");
                    intent.putExtra(BannerInfoTable.COLUMN_DESCRIPTION, str5);
                    intent.putExtra("summary", str6);
                    intent.putExtra("location", str4);
                    intent.putExtra(VservConstants.VPLAY0, convertDateString.getTimeInMillis());
                    intent.putExtra("end", convertDateString2.getTimeInMillis());
                    intent.putExtra("status", str7);
                    intent.putExtra("transparency", str8);
                    intent.putExtra("recurrence", str9);
                    if (!(str10 == null || Preconditions.EMPTY_ARGUMENTS.equals(str10))) {
                        intent.putExtra("hasAlarm", true);
                    }
                    this.imWebView.getActivity().startActivity(intent);
                    if (this.imWebView.mListener != null) {
                        this.imWebView.mListener.onLeaveApplication();
                    }
                }
            } else {
                Log.internal(Constants.RENDERING_LOG_TAG, "createCalendarEvent called even if it is not supported");
                this.imWebView.raiseError("createCalendarEvent called even if it is not supported", "createCalendarEvent");
            }
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "Error creating reminder event", e);
        }
    }

    @JavascriptInterface
    public int getAudioVolume(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(37), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> getAudioVolume: ");
        return this.imWebView.getAudioVolume(str);
    }

    @JavascriptInterface
    public String getCurrentPosition() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(18), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> getCurrentPosition");
        synchronized (this.imWebView.mutexcPos) {
            this.imWebView.sendToCPHandler();
            while (this.imWebView.acqMutexcPos.get()) {
                try {
                    this.imWebView.mutexcPos.wait();
                } catch (InterruptedException e) {
                    Log.debug(Constants.RENDERING_LOG_TAG, "mutexcPos failed ", e);
                }
            }
            this.imWebView.acqMutexcPos.set(true);
        }
        return this.imWebView.curPosition.toString();
    }

    @JavascriptInterface
    public String getDefaultPosition() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(19), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> getDefaultPosition");
        synchronized (this.imWebView.mutexdPos) {
            this.imWebView.sendToDPHandler();
            while (this.imWebView.acqMutexdPos.get()) {
                try {
                    this.imWebView.mutexdPos.wait();
                } catch (InterruptedException e) {
                    Log.debug(Constants.RENDERING_LOG_TAG, "mutexdPos failed ", e);
                }
            }
            this.imWebView.acqMutexdPos.set(true);
        }
        Log.debug(Constants.RENDERING_LOG_TAG, "mutexdPassed" + this.imWebView.defPosition);
        return this.imWebView.defPosition.toString();
    }

    @JavascriptInterface
    public String getGalleryImage() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(25), null));
        if (supports("getGalleryImage")) {
            Intent intent = new Intent(this.imWebView.getActivity(), IMBrowserActivity.class);
            intent.putExtra(IMBrowserActivity.EXTRA_BROWSER_ACTIVITY_TYPE, LocationRequest.PRIORITY_HIGH_ACCURACY);
            intent.putExtra(AnalyticsEvent.EVENT_ID, IMBrowserActivity.generateId(new c()));
            intent.putExtra(OrmmaView.ACTION_KEY, "getGalleryImage");
            this.imWebView.getActivity().startActivity(intent);
            if (this.imWebView.mListener != null) {
                this.imWebView.mListener.onLeaveApplication();
            }
        } else {
            Log.internal(Constants.RENDERING_LOG_TAG, "getGalleryImage called even if it is not supported");
        }
        return null;
    }

    @JavascriptInterface
    public String getMaxSize() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(20), null));
        int[] b = b();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(MMLayout.KEY_WIDTH, b[0]);
            jSONObject.put(MMLayout.KEY_HEIGHT, b[1]);
        } catch (JSONException e) {
        }
        return jSONObject.toString();
    }

    @JavascriptInterface
    public double getMicIntensity() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(51), null));
        return this.imWebView.getLastGoodKnownMicValue();
    }

    @JavascriptInterface
    public String getScreenSize() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(17), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> getScreenSize");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        int i = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        int i2 = (int) (((float) displayMetrics.heightPixels) / displayMetrics.density);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(MMLayout.KEY_WIDTH, i);
            jSONObject.put(MMLayout.KEY_HEIGHT, i2);
        } catch (JSONException e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Failed to get screen size");
        }
        return jSONObject.toString();
    }

    @JavascriptInterface
    public String getSdkVersion() {
        return InMobi.getVersion();
    }

    @JavascriptInterface
    public int getVideoVolume(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(45), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> getVideoVolume: ");
        return this.imWebView.getVideoVolume(str);
    }

    @JavascriptInterface
    public void hideVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(48), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> hideVideo: id :" + str);
        this.imWebView.hideVideo(str);
    }

    @JavascriptInterface
    public void incentCompleted(String str) {
        HashMap hashMap = null;
        try {
            JSONObject jSONObject = new JSONObject(str);
            HashMap hashMap2 = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                try {
                    hashMap2.put(str2, jSONObject.get(str2));
                } catch (JSONException e) {
                    this.imWebView.incentCompleted(null);
                }
            }
            this.imWebView.incentCompleted(hashMap2);
        } catch (JSONException e2) {
            Log.internal(Constants.RENDERING_LOG_TAG, "JSON error");
            this.imWebView.incentCompleted(hashMap);
        }
    }

    @JavascriptInterface
    public boolean isAudioMuted(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(35), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> isAudioMuted: ");
        return this.imWebView.isAudioMuted(str);
    }

    @JavascriptInterface
    public boolean isVideoMuted(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(43), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> isVideoMuted: ");
        return this.imWebView.isVideoMuted(str);
    }

    @JavascriptInterface
    public void log(String str) {
        Log.debug(Constants.RENDERING_LOG_TAG, "Ad Log Message: " + str);
    }

    @JavascriptInterface
    public void makeCall(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(29), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> makeCall: number: " + str);
        try {
            String b = b(str);
            if (b == null) {
                this.imWebView.raiseError("Bad Phone Number", "makeCall");
            } else {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(b.toString()));
                intent.addFlags(DriveFile.MODE_READ_ONLY);
                this.imWebView.getActivity().startActivity(intent);
                this.imWebView.fireOnLeaveApplication();
            }
        } catch (Exception e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Exception in making call ", e);
            this.imWebView.raiseError("Exception in making call", "makeCall");
        }
    }

    @JavascriptInterface
    public void muteAudio(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(33), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> muteAudio: ");
        this.imWebView.muteAudio(str);
    }

    @JavascriptInterface
    public void muteVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(41), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> muteVideo: ");
        this.imWebView.muteVideo(str);
    }

    @JavascriptInterface
    public void onUserInteraction(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            HashMap hashMap = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                try {
                    hashMap.put(str2, jSONObject.getString(str2));
                } catch (JSONException e) {
                }
            }
            this.imWebView.userInteraction(hashMap);
        } catch (Exception e2) {
        }
    }

    @JavascriptInterface
    public void openExternal(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(2), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> openExternal: url: " + str);
        this.imWebView.openExternal(str);
    }

    @JavascriptInterface
    public void pauseAudio(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(39), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> pauseAudio: id :" + str);
        this.imWebView.pauseAudio(str);
    }

    @JavascriptInterface
    public void pauseVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(47), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> pauseVideo: id :" + str);
        this.imWebView.pauseVideo(str);
    }

    @JavascriptInterface
    public void playAudio(String str, boolean z, boolean z2, boolean z3, String str2, String str3, String str4) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(32), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "playAudio: url: " + str + " autoPlay: " + z + " controls: " + z2 + " loop: " + z3 + " startStyle: " + str2 + " stopStyle: " + str3 + " id:" + str4);
        this.imWebView.playAudio(str, z, z2, z3, str2, str3, str4);
    }

    @JavascriptInterface
    public void playVideo(String str, boolean z, boolean z2, boolean z3, boolean z4, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(40), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> playVideo: url: " + str + " audioMuted: " + z + " autoPlay: " + z2 + " controls: " + z3 + " loop: " + z4 + " x: " + str2 + " y: " + str3 + " width: " + str4 + " height: " + str5 + " startStyle: " + str6 + " stopStyle: " + str7 + " id:" + str8);
        Dimensions dimensions = new Dimensions();
        dimensions.x = a(str2, -99999);
        dimensions.y = a(str3, -99999);
        dimensions.width = a(str4, -99999);
        dimensions.height = a(str5, -99999);
        if (dimensions.width == -99999 && dimensions.height == -99999) {
            int[] b = b();
            dimensions.x = 0;
            dimensions.y = 0;
            dimensions.width = b[0];
            dimensions.height = b[1];
        }
        this.imWebView.playVideo(str, z, z2, z3, z4, dimensions, str6, str7, str8);
    }

    @JavascriptInterface
    public void postToSocial(int i, String str, String str2, String str3) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(21), null));
        if (supports("postToSocial." + i)) {
            String str4 = str == null ? Preconditions.EMPTY_ARGUMENTS : str;
            if (str2 == null) {
                str4 = Preconditions.EMPTY_ARGUMENTS;
            }
            if (str3 == null) {
                str3 = Preconditions.EMPTY_ARGUMENTS;
            }
            Intent intent = new Intent(this.imWebView.getActivity(), IMBrowserActivity.class);
            int generateId = IMBrowserActivity.generateId(new e());
            intent.putExtra(IMBrowserActivity.EXTRA_BROWSER_ACTIVITY_TYPE, LocationRequest.PRIORITY_HIGH_ACCURACY);
            intent.putExtra(AnalyticsEvent.EVENT_ID, generateId);
            intent.putExtra(OrmmaView.ACTION_KEY, "postToSocial");
            intent.putExtra("socialType", i);
            intent.putExtra("text", str4);
            intent.putExtra("link", str2);
            intent.putExtra("image", str3);
            this.imWebView.getActivity().startActivity(intent);
            if (this.imWebView.mListener != null) {
                this.imWebView.mListener.onLeaveApplication();
            }
        } else {
            this.imWebView.raiseError("Social type " + i + " is not supported.", "postToSocial");
        }
    }

    @SuppressLint({"NewApi"})
    public void registerBroadcastListener() {
        c();
        if (this.b == null && VERSION.SDK_INT > 8) {
            try {
                if (this.a == null) {
                    this.a = (DownloadManager) this.imWebView.getActivity().getSystemService(AdTrackerConstants.GOAL_DOWNLOAD);
                }
                this.b = new b();
                if (this.e) {
                    this.b = null;
                } else {
                    this.imWebView.getActivity().registerReceiver((BroadcastReceiver) this.b, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
                }
            } catch (Exception e) {
                Log.internal(Constants.RENDERING_LOG_TAG, "JSUtilityController-> registerBroadcastListener. Unable to register download listener", e);
            }
        }
    }

    @JavascriptInterface
    public void registerMicListener() {
        if (!this.h) {
            this.h = true;
            AudioTriggerer.addEventListener(this.g);
        }
    }

    public void reset() {
        if (this.d != null) {
            this.d.reset();
        }
    }

    @JavascriptInterface
    public void saveContent(String str, String str2) {
        File file = new File(InternalSDKUtil.getContext().getExternalFilesDir(null) + "/im_cached_content/");
        if (file.exists()) {
            file.delete();
        }
        file.mkdir();
        char[] toCharArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while (i < 20) {
            stringBuilder.append(toCharArray[random.nextInt(toCharArray.length)]);
            i++;
        }
        this.imWebView.saveFile(new File(file, stringBuilder.toString()), str2, str);
    }

    @JavascriptInterface
    public void seekAudio(String str, int i) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(38), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> seekAudio: ");
        this.imWebView.seekAudio(str, i);
    }

    @JavascriptInterface
    public void seekVideo(String str, int i) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(46), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> seekVideo: ");
        this.imWebView.seekVideo(str, i);
    }

    @JavascriptInterface
    public void sendMail(String str, String str2, String str3) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(28), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> sendMail: recipient: " + str + " subject: " + str2 + " body: " + str3);
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("plain/text");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
            intent.putExtra("android.intent.extra.SUBJECT", str2);
            intent.putExtra("android.intent.extra.TEXT", str3);
            intent.addFlags(DriveFile.MODE_READ_ONLY);
            this.imWebView.getActivity().startActivity(Intent.createChooser(intent, "Choose the Email Client."));
            this.imWebView.fireOnLeaveApplication();
        } catch (Exception e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Exception in sending mail ", e);
            this.imWebView.raiseError("Exception in sending mail", "sendMail");
        }
    }

    @JavascriptInterface
    public void sendSMS(String str, String str2) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(27), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> sendSMS: recipient: " + str + " body: " + str2);
        try {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + Uri.encode(str)));
            intent.putExtra("sms_body", str2);
            intent.addFlags(DriveFile.MODE_READ_ONLY);
            this.imWebView.getActivity().startActivity(intent);
            this.imWebView.fireOnLeaveApplication();
        } catch (Exception e) {
            Log.debug(Constants.RENDERING_LOG_TAG, "Exception in sending SMS ", e);
            this.imWebView.raiseError("Exception in sending SMS", "sendSMS");
        }
    }

    @JavascriptInterface
    public void setAudioVolume(String str, int i) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(36), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> setAudioVolume: " + str + " " + i);
        this.imWebView.setAudioVolume(str, i);
    }

    @JavascriptInterface
    public void setPlayableSettings(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            Map hashMap = new HashMap();
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str2 = (String) keys.next();
                try {
                    hashMap.put(str2, jSONObject.get(str2));
                } catch (JSONException e) {
                    Log.internal(Constants.RENDERING_LOG_TAG, "Playable Ads Settings map key " + str2 + " has invalid value");
                }
            }
            this.imWebView.getPlayableListener().onPlayableSettingsReceived(hashMap);
        } catch (Exception e2) {
            Log.internal(Constants.RENDERING_LOG_TAG, "Exception setting playable settings", e2);
        }
    }

    @JavascriptInterface
    public void setVideoVolume(String str, int i) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(44), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> setVideoVolume: ");
        this.imWebView.setVideoVolume(str, i);
    }

    public void setWebViewClosed(boolean z) {
        this.e = z;
    }

    @JavascriptInterface
    public void showAlert(String str) {
        Log.debug(Constants.RENDERING_LOG_TAG, str);
    }

    @JavascriptInterface
    public void showVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(49), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> showVideo: id :" + str);
        this.imWebView.showVideo(str);
    }

    public void stopAllListeners() {
        try {
            this.d.stopAllListeners();
        } catch (Exception e) {
        }
    }

    @JavascriptInterface
    @SuppressLint({"NewApi"})
    public void storePicture(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(23), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "Store picture called on URL: " + str);
        try {
            Uri parse = Uri.parse(InternalSDKUtil.getFinalRedirectedUrl(str));
            if (supports("storePicture")) {
                try {
                    Request request = new Request(parse);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, parse.getLastPathSegment());
                    this.a.enqueue(request);
                } catch (Exception e) {
                    this.imWebView.raiseError("Unable to store.", "storePicture");
                }
            }
        } catch (Exception e2) {
            this.imWebView.raiseError("Invalid URL.", "storePicture");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"NewApi"})
    public boolean supports(java.lang.String r9) {
        throw new UnsupportedOperationException("Method not decompiled: com.inmobi.re.controller.JSUtilityController.supports(java.lang.String):boolean");
        /*
        r8 = this;
        r7 = 11;
        r6 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r2 = 0;
        r1 = 1;
        r0 = com.inmobi.commons.internal.ApiStatCollector.getLogger();
        r3 = new com.inmobi.commons.metric.EventLog;
        r4 = new com.inmobi.commons.internal.ApiStatCollector$ApiEventType;
        r5 = 22;
        r4.<init>(r5);
        r5 = 0;
        r3.<init>(r4, r5);
        r0.logEvent(r3);
        r0 = "html5video";
        r0 = r9.equals(r0);
        if (r0 != 0) goto L_0x002a;
    L_0x0022:
        r0 = "inlineVideo";
        r0 = r9.equals(r0);
        if (r0 == 0) goto L_0x0054;
    L_0x002a:
        r0 = android.os.Build.VERSION.SDK_INT;
        if (r0 < r7) goto L_0x004f;
    L_0x002e:
        r0 = r8.imWebView;
        r0 = r0.isHardwareAccelerated();
        if (r0 == 0) goto L_0x004d;
    L_0x0036:
        r0 = r8.imWebView;
        r0 = r0.isEnabledHardwareAcceleration();
        if (r0 == 0) goto L_0x004d;
    L_0x003e:
        r0 = r1;
    L_0x003f:
        r0 = java.lang.Boolean.valueOf(r0);
    L_0x0043:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
    L_0x004c:
        return r1;
    L_0x004d:
        r0 = r2;
        goto L_0x003f;
    L_0x004f:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x0043;
    L_0x0054:
        r0 = r8.f;
        r0 = r0.get(r9);
        r0 = (java.lang.Boolean) r0;
        if (r0 == 0) goto L_0x0063;
    L_0x005e:
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x0063:
        r0 = r8.imWebView;
        r0 = r0.getActivity();
        r0 = r0.getPackageManager();
        r3 = "tel";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x007f;
    L_0x0075:
        r0 = r8.f;
        r2 = java.lang.Boolean.valueOf(r1);
        r0.put(r9, r2);
        goto L_0x004c;
    L_0x007f:
        r3 = "sms";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x00b6;
    L_0x0087:
        r0 = new android.content.Intent;
        r3 = "android.intent.action.VIEW";
        r0.<init>(r3);
        r3 = "vnd.android-dir/mms-sms";
        r0.setType(r3);
        r3 = r8.imWebView;
        r3 = r3.getActivity();
        r3 = r3.getPackageManager();
        r0 = r3.resolveActivity(r0, r6);
        if (r0 != 0) goto L_0x00b1;
    L_0x00a3:
        r0 = java.lang.Boolean.valueOf(r2);
    L_0x00a7:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x00b1:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x00a7;
    L_0x00b6:
        r3 = "calendar";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x00ee;
    L_0x00be:
        r0 = new android.content.Intent;
        r3 = "android.intent.action.VIEW";
        r0.<init>(r3);
        r3 = "vnd.android.cursor.item/event";
        r0.setType(r3);
        r3 = r8.imWebView;
        r3 = r3.getActivity();
        r3 = r3.getPackageManager();
        r0 = r3.resolveActivity(r0, r6);
        if (r0 != 0) goto L_0x00e9;
    L_0x00da:
        r0 = java.lang.Boolean.valueOf(r2);
    L_0x00de:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x00e9:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x00de;
    L_0x00ee:
        r3 = "microphone";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x0117;
    L_0x00f6:
        r3 = "android.permission.RECORD_AUDIO";
        r4 = android.os.Binder.getCallingUid();
        r4 = r0.getNameForUid(r4);
        r0 = r0.checkPermission(r3, r4);
        if (r0 != 0) goto L_0x0115;
    L_0x0106:
        r0 = java.lang.Boolean.valueOf(r1);
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x0115:
        r1 = r2;
        goto L_0x0106;
    L_0x0117:
        r3 = "storePicture";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x0146;
    L_0x011f:
        r3 = android.os.Build.VERSION.SDK_INT;
        r4 = 8;
        if (r3 <= r4) goto L_0x0144;
    L_0x0125:
        r3 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r4 = android.os.Binder.getCallingUid();
        r4 = r0.getNameForUid(r4);
        r0 = r0.checkPermission(r3, r4);
        if (r0 != 0) goto L_0x0144;
    L_0x0135:
        r0 = java.lang.Boolean.valueOf(r1);
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x0144:
        r1 = r2;
        goto L_0x0135;
    L_0x0146:
        r3 = "postToSocial.2";
        r3 = r9.equals(r3);
        if (r3 != 0) goto L_0x0156;
    L_0x014e:
        r3 = "postToSocial.3";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x0165;
    L_0x0156:
        r0 = java.lang.Boolean.valueOf(r1);
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x0165:
        r3 = "takeCameraPicture";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x01af;
    L_0x016d:
        r3 = new android.content.Intent;
        r4 = "android.media.action.IMAGE_CAPTURE";
        r3.<init>(r4);
        r4 = r8.imWebView;
        r4 = r4.getActivity();
        r4 = r4.getPackageManager();
        r3 = r4.resolveActivity(r3, r6);
        r4 = "android.permission.WRITE_EXTERNAL_STORAGE";
        r5 = android.os.Binder.getCallingUid();
        r5 = r0.getNameForUid(r5);
        r0 = r0.checkPermission(r4, r5);
        if (r0 != 0) goto L_0x01a4;
    L_0x0192:
        r0 = r1;
    L_0x0193:
        if (r3 != 0) goto L_0x01a6;
    L_0x0195:
        r0 = java.lang.Boolean.valueOf(r2);
    L_0x0199:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x01a4:
        r0 = r2;
        goto L_0x0193;
    L_0x01a6:
        if (r0 == 0) goto L_0x01ad;
    L_0x01a8:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x0199;
    L_0x01ad:
        r1 = r2;
        goto L_0x01a8;
    L_0x01af:
        r3 = "getGalleryImage";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x01e4;
    L_0x01b7:
        r0 = new android.content.Intent;
        r3 = "android.intent.action.PICK";
        r4 = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        r0.<init>(r3, r4);
        r3 = r8.imWebView;
        r3 = r3.getActivity();
        r3 = r3.getPackageManager();
        r0 = r3.resolveActivity(r0, r6);
        if (r0 != 0) goto L_0x01df;
    L_0x01d0:
        r0 = java.lang.Boolean.valueOf(r2);
    L_0x01d4:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x01df:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x01d4;
    L_0x01e4:
        r3 = "vibrate";
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x023b;
    L_0x01ec:
        r3 = "android.permission.VIBRATE";
        r4 = android.os.Binder.getCallingUid();
        r4 = r0.getNameForUid(r4);
        r0 = r0.checkPermission(r3, r4);
        if (r0 != 0) goto L_0x0222;
    L_0x01fc:
        r0 = r1;
    L_0x01fd:
        r3 = java.lang.Boolean.valueOf(r0);
        r0 = "vibrator";
        r4 = r8.d;
        r4 = r4.imWebView;
        r4 = r4.getActivity();
        r0 = r4.getSystemService(r0);
        r0 = (android.os.Vibrator) r0;
        if (r0 != 0) goto L_0x0224;
    L_0x0213:
        r0 = java.lang.Boolean.valueOf(r2);
    L_0x0217:
        r1 = r8.f;
        r1.put(r9, r0);
        r1 = r0.booleanValue();
        goto L_0x004c;
    L_0x0222:
        r0 = r2;
        goto L_0x01fd;
    L_0x0224:
        r4 = android.os.Build.VERSION.SDK_INT;
        if (r4 < r7) goto L_0x023e;
    L_0x0228:
        r3 = r3.booleanValue();
        if (r3 == 0) goto L_0x0239;
    L_0x022e:
        r0 = r0.hasVibrator();
        if (r0 == 0) goto L_0x0239;
    L_0x0234:
        r0 = java.lang.Boolean.valueOf(r1);
        goto L_0x0217;
    L_0x0239:
        r1 = r2;
        goto L_0x0234;
    L_0x023b:
        r1 = r2;
        goto L_0x004c;
    L_0x023e:
        r0 = r3;
        goto L_0x0217;
        */
    }

    @JavascriptInterface
    public String supportsFeature(String str) {
        return String.valueOf(supports(str));
    }

    @JavascriptInterface
    public String takeCameraPicture() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(24), null));
        if (supports("takeCameraPicture")) {
            Intent intent = new Intent(this.imWebView.getActivity(), IMBrowserActivity.class);
            intent.putExtra(IMBrowserActivity.EXTRA_BROWSER_ACTIVITY_TYPE, LocationRequest.PRIORITY_HIGH_ACCURACY);
            Parcelable insert = this.mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
            intent.putExtra(AnalyticsEvent.EVENT_ID, IMBrowserActivity.generateId(new d(insert)));
            intent.putExtra("URI", insert);
            intent.putExtra(OrmmaView.ACTION_KEY, "takeCameraPicture");
            this.imWebView.getActivity().startActivity(intent);
            if (this.imWebView.mListener != null) {
                this.imWebView.mListener.onLeaveApplication();
            }
        } else {
            Log.internal(Constants.RENDERING_LOG_TAG, "takeCameraPicture called even if it is not supported");
        }
        return null;
    }

    @JavascriptInterface
    public void unMuteAudio(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(34), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> unMuteAudio: ");
        this.imWebView.unMuteAudio(str);
    }

    @JavascriptInterface
    public void unMuteVideo(String str) {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(42), null));
        Log.debug(Constants.RENDERING_LOG_TAG, "JSUtilityController-> unMuteVideo: ");
        this.imWebView.unMuteVideo(str);
    }

    @SuppressLint({"NewApi"})
    public void unRegisterBroadcastListener() {
        try {
            d();
            if (VERSION.SDK_INT > 8) {
                this.imWebView.getActivity().unregisterReceiver((BroadcastReceiver) this.b);
                this.b = null;
            }
        } catch (Exception e) {
            Log.internal(Constants.RENDERING_LOG_TAG, "JSUtilityController-> unregisterBroadcastListener. Unable to unregister download listener");
        }
    }

    @JavascriptInterface
    public void unRegisterMicListener() {
        if (this.h) {
            this.h = false;
            AudioTriggerer.removeEventListener(this.g);
        }
    }

    @JavascriptInterface
    public void vibrate() {
        ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(26), null));
        if (!this.imWebView.isViewable()) {
            this.imWebView.raiseError("Creative not visible. Will not vibrate.", "vibrate");
        } else if (supports("vibrate")) {
            ((Vibrator) this.imWebView.getActivity().getSystemService("vibrator")).vibrate(2000);
            new Timer().schedule(new g(), 2000);
        } else {
            Log.internal(Constants.RENDERING_LOG_TAG, "vibrate called even if it is not supported");
        }
    }

    @JavascriptInterface
    public void vibrate(String str, int i) {
        try {
            ApiStatCollector.getLogger().logEvent(new EventLog(new ApiEventType(26), null));
            if (!this.imWebView.isViewable()) {
                this.imWebView.raiseError("Creative not visible. Will not vibrate.", "vibrate");
            } else if (supports("vibrate")) {
                Vibrator vibrator = (Vibrator) this.imWebView.getActivity().getSystemService("vibrator");
                String replaceAll = str.replaceAll("\\[", Preconditions.EMPTY_ARGUMENTS).replaceAll("\\]", Preconditions.EMPTY_ARGUMENTS);
                if (replaceAll == null || Preconditions.EMPTY_ARGUMENTS.equals(replaceAll.trim())) {
                    vibrator.cancel();
                } else {
                    int i2;
                    String[] split = replaceAll.split(",");
                    int length = split.length;
                    if (length > Initializer.getConfigParams().getMaxVibPatternLength()) {
                        Log.internal(Constants.RENDERING_LOG_TAG, "vibration pattern exceeds max length. Will be truncated to max " + Initializer.getConfigParams().getMaxVibPatternLength() + "ms");
                        i2 = Initializer.getConfigParams().getMaxVibPatternLength();
                    } else {
                        i2 = length;
                    }
                    long[] jArr = new long[i2];
                    length = 0;
                    while (length < i2) {
                        try {
                            jArr[length] = Long.parseLong(split[length]);
                            if (jArr[length] > ((long) Initializer.getConfigParams().getMaxVibDuration())) {
                                Log.internal(Constants.RENDERING_LOG_TAG, "vibration duration exceeds max. Will only vibrate for max " + Initializer.getConfigParams().getMaxVibDuration() + "ms");
                                jArr[length] = (long) Initializer.getConfigParams().getMaxVibDuration();
                            }
                            if (jArr[length] < 0) {
                                this.imWebView.raiseError("Negative duration not allowed in vibration .", "vibrate");
                            }
                            length++;
                        } catch (NumberFormatException e) {
                            this.imWebView.raiseError("Invalid values of pattern in vibration .", "vibrate");
                        }
                    }
                    if (jArr != null && jArr.length != 0) {
                        vibrator.vibrate(jArr, i);
                    }
                }
            } else {
                this.imWebView.raiseError("Vibrate called even if it is not supported.", "vibrate");
                Log.internal(Constants.RENDERING_LOG_TAG, "vibrate called even if it is not supported");
            }
        } catch (Exception e2) {
            Log.internal(Constants.RENDERING_LOG_TAG, "vibrate exception", e2);
        }
    }
}