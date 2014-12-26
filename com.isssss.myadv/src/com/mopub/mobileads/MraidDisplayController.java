package com.mopub.mobileads;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.BannerInfoTable;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.logging.MoPubLog;
import com.mopub.common.util.Dips;
import com.mopub.common.util.Drawables;
import com.mopub.common.util.ResponseHeader;
import com.mopub.common.util.Streams;
import com.mopub.mobileads.MraidView.BaseMraidListener;
import com.mopub.mobileads.MraidView.ExpansionStyle;
import com.mopub.mobileads.MraidView.NativeCloseButtonStyle;
import com.mopub.mobileads.MraidView.PlacementType;
import com.mopub.mobileads.MraidView.ViewState;
import com.mopub.mobileads.factories.HttpClientFactory;
import com.mopub.mobileads.util.HttpResponses;
import com.mopub.mobileads.util.Interstitials;
import com.mopub.mobileads.util.Mraids;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mobi.vserv.android.ads.VservConstants;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

class MraidDisplayController extends MraidAbstractController {
    private static final int CLOSE_BUTTON_SIZE_DP = 50;
    private static final String[] DATE_FORMATS;
    private static final String LOGTAG = "MraidDisplayController";
    private static final int MAX_NUMBER_DAYS_IN_MONTH = 31;
    private static final long VIEWABILITY_TIMER_MILLIS = 3000;
    private FrameLayout mAdContainerLayout;
    private boolean mAdWantsCustomCloseButton;
    private ImageView mCloseButton;
    private final OnClickListener mCloseOnClickListener;
    protected float mDensity;
    private RelativeLayout mExpansionLayout;
    private final ExpansionStyle mExpansionStyle;
    private Handler mHandler;
    private final NativeCloseButtonStyle mNativeCloseButtonStyle;
    private OrientationBroadcastReceiver mOrientationBroadcastReceiver;
    private final int mOriginalRequestedOrientation;
    private FrameLayout mPlaceholderView;
    private FrameLayout mRootView;
    protected int mScreenHeight;
    protected int mScreenWidth;
    private MraidView mTwoPartExpansionView;
    private int mViewIndexInParent;
    private ViewState mViewState;

    class AnonymousClass_3 implements Runnable {
        private final /* synthetic */ String val$message;

        AnonymousClass_3(String str) {
            this.val$message = str;
        }

        public void run() {
            Toast.makeText(MraidDisplayController.this.getContext(), this.val$message, 0).show();
        }
    }

    class AnonymousClass_4 implements Runnable {
        private MediaScannerConnection mediaScannerConnection;
        private InputStream pictureInputStream;
        private OutputStream pictureOutputStream;
        private URI uri;
        private final /* synthetic */ File val$pictureStoragePath;
        private final /* synthetic */ String val$uriString;

        AnonymousClass_4(String str, File file) {
            this.val$uriString = str;
            this.val$pictureStoragePath = file;
        }

        private void loadPictureIntoGalleryApp(String filename) {
            MoPubMediaScannerConnectionClient mediaScannerConnectionClient = new MoPubMediaScannerConnectionClient(filename, null, null);
            this.mediaScannerConnection = new MediaScannerConnection(MraidDisplayController.this.getContext().getApplicationContext(), mediaScannerConnectionClient);
            mediaScannerConnectionClient.setMediaScannerConnection(this.mediaScannerConnection);
            this.mediaScannerConnection.connect();
        }

        public void run() {
            try {
                this.uri = URI.create(this.val$uriString);
                HttpResponse httpResponse = HttpClientFactory.create().execute(new HttpGet(this.uri));
                this.pictureInputStream = httpResponse.getEntity().getContent();
                String redirectLocation = HttpResponses.extractHeader(httpResponse, ResponseHeader.LOCATION);
                if (redirectLocation != null) {
                    this.uri = URI.create(redirectLocation);
                }
                File pictureFile = new File(this.val$pictureStoragePath, MraidDisplayController.this.getFileNameForUriAndHttpResponse(this.uri, httpResponse));
                String pictureFileFullPath = pictureFile.toString();
                this.pictureOutputStream = new FileOutputStream(pictureFile);
                Streams.copyContent(this.pictureInputStream, this.pictureOutputStream);
                loadPictureIntoGalleryApp(pictureFileFullPath);
                Streams.closeStream(this.pictureInputStream);
                Streams.closeStream(this.pictureOutputStream);
            } catch (Exception e) {
                MraidDisplayController.this.mHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass_4.this.this$0.showUserToast("Image failed to download.");
                        AnonymousClass_4.this.this$0.getMraidView().fireErrorEvent(MraidJavascriptCommand.STORE_PICTURE, "Error downloading and saving image file.");
                        MoPubLog.d("Error downloading and saving image file.");
                    }
                });
                Streams.closeStream(this.pictureInputStream);
                Streams.closeStream(this.pictureOutputStream);
            }
        }
    }

    class AnonymousClass_5 implements DialogInterface.OnClickListener {
        private final /* synthetic */ String val$imageUrl;

        AnonymousClass_5(String str) {
            this.val$imageUrl = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            MraidDisplayController.this.downloadImage(this.val$imageUrl);
        }
    }

    private class MoPubMediaScannerConnectionClient implements MediaScannerConnectionClient {
        private final String mFilename;
        private MediaScannerConnection mMediaScannerConnection;
        private final String mMimeType;

        private MoPubMediaScannerConnectionClient(String filename, String mimeType) {
            this.mFilename = filename;
            this.mMimeType = mimeType;
        }

        private void setMediaScannerConnection(MediaScannerConnection connection) {
            this.mMediaScannerConnection = connection;
        }

        public void onMediaScannerConnected() {
            if (this.mMediaScannerConnection != null) {
                this.mMediaScannerConnection.scanFile(this.mFilename, this.mMimeType);
            }
        }

        public void onScanCompleted(String path, Uri uri) {
            if (this.mMediaScannerConnection != null) {
                this.mMediaScannerConnection.disconnect();
            }
        }
    }

    class OrientationBroadcastReceiver extends BroadcastReceiver {
        private Context mContext;
        private int mLastRotation;

        OrientationBroadcastReceiver() {
        }

        private boolean isRegistered() {
            return this.mContext != null;
        }

        public void onReceive(Context context, Intent intent) {
            if (isRegistered() && intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                int orientation = MraidDisplayController.this.getDisplayRotation();
                if (orientation != this.mLastRotation) {
                    this.mLastRotation = orientation;
                    MraidDisplayController.this.onOrientationChanged(this.mLastRotation);
                }
            }
        }

        public void register(Context context) {
            this.mContext = context;
            context.registerReceiver(this, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
        }

        public void unregister() {
            this.mContext.unregisterReceiver(this);
            this.mContext = null;
        }
    }

    static {
        DATE_FORMATS = new String[]{"yyyy-MM-dd'T'HH:mm:ssZZZZZ", "yyyy-MM-dd'T'HH:mmZZZZZ"};
    }

    MraidDisplayController(MraidView view, ExpansionStyle expStyle, NativeCloseButtonStyle buttonStyle) {
        int i = -1;
        super(view);
        this.mViewState = ViewState.HIDDEN;
        this.mHandler = new Handler();
        this.mOrientationBroadcastReceiver = new OrientationBroadcastReceiver();
        this.mScreenWidth = -1;
        this.mScreenHeight = -1;
        this.mExpansionStyle = expStyle;
        this.mNativeCloseButtonStyle = buttonStyle;
        Context context = getContext();
        if (context instanceof Activity) {
            i = ((Activity) context).getRequestedOrientation();
        }
        this.mOriginalRequestedOrientation = i;
        initialize();
        this.mCloseOnClickListener = new OnClickListener() {
            public void onClick(View view) {
                MraidDisplayController.this.close();
            }
        };
        this.mAdContainerLayout = createAdContainerLayout();
        this.mExpansionLayout = createExpansionLayout();
        this.mPlaceholderView = createPlaceholderView();
    }

    private String dayNumberToDayOfMonthString(int number) throws IllegalArgumentException {
        if (number != 0 && number >= -31 && number <= 31) {
            return number;
        }
        throw new IllegalArgumentException(new StringBuilder("invalid day of month ").append(number).toString());
    }

    private String dayNumberToDayOfWeekString(int number) throws IllegalArgumentException {
        switch (number) {
            case MMAdView.TRANSITION_NONE:
                return "SU";
            case MMAdView.TRANSITION_FADE:
                return "MO";
            case MMAdView.TRANSITION_UP:
                return "TU";
            case MMAdView.TRANSITION_DOWN:
                return "WE";
            case MMAdView.TRANSITION_RANDOM:
                return "TH";
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return "FR";
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return "SA";
            default:
                throw new IllegalArgumentException(new StringBuilder("invalid day of week ").append(number).toString());
        }
    }

    private void downloadImage(String uriString) {
        File pictureStoragePath = getPictureStoragePath();
        pictureStoragePath.mkdirs();
        new Thread(new AnonymousClass_4(uriString, pictureStoragePath)).start();
    }

    private void expandLayouts(View expansionContentView, int expandWidth, int expandHeight) {
        int closeButtonSize = (int) (50.0f * this.mDensity + 0.5f);
        if (expandWidth < closeButtonSize) {
            expandWidth = closeButtonSize;
        }
        if (expandHeight < closeButtonSize) {
            expandHeight = closeButtonSize;
        }
        View dimmingView = new View(getContext());
        dimmingView.setBackgroundColor(0);
        dimmingView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mExpansionLayout.addView(dimmingView, new LayoutParams(-1, -1));
        this.mAdContainerLayout.addView(expansionContentView, new LayoutParams(-1, -1));
        addCloseEventRegion(this.mAdContainerLayout);
        LayoutParams lp = new LayoutParams(expandWidth, expandHeight);
        lp.addRule(ApiEventType.API_MRAID_CLOSE);
        this.mExpansionLayout.addView(this.mAdContainerLayout, lp);
    }

    private Context getContext() {
        return getMraidView().getContext();
    }

    private int getDisplayRotation() {
        return ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getOrientation();
    }

    private String getFileNameForUriAndHttpResponse(URI uri, HttpResponse response) {
        String path = uri.getPath();
        if (path == null) {
            return null;
        }
        String filename = new File(path).getName();
        Header header = response.getFirstHeader(MraidCommandStorePicture.MIME_TYPE_HEADER);
        if (header == null) {
            return filename;
        }
        String[] fields = header.getValue().split(";");
        int length = fields.length;
        int i = 0;
        while (i < length) {
            String field = fields[i];
            if (field.contains("image/")) {
                String extension = new StringBuilder(".").append(field.split("/")[1]).toString();
                return !filename.endsWith(extension) ? new StringBuilder(String.valueOf(filename)).append(extension).toString() : filename;
            } else {
                i++;
            }
        }
        return filename;
    }

    private File getPictureStoragePath() {
        return new File(Environment.getExternalStorageDirectory(), "Pictures");
    }

    private void initialize() {
        this.mViewState = ViewState.LOADING;
        initializeScreenMetrics();
        this.mOrientationBroadcastReceiver.register(getContext());
    }

    private void initializeScreenMetrics() {
        Context context = getContext();
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metrics);
        this.mDensity = metrics.density;
        int statusBarHeight = 0;
        int titleBarHeight = 0;
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            Rect rect = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(rect);
            statusBarHeight = rect.top;
            titleBarHeight = window.findViewById(16908290).getTop() - statusBarHeight;
        }
        int heightPixels = metrics.heightPixels - statusBarHeight - titleBarHeight;
        this.mScreenWidth = (int) (((double) metrics.widthPixels) * (160.0d / ((double) metrics.densityDpi)));
        this.mScreenHeight = (int) (((double) heightPixels) * (160.0d / ((double) metrics.densityDpi)));
    }

    private void onOrientationChanged(int currentRotation) {
        initializeScreenMetrics();
        getMraidView().fireChangeEventForProperty(MraidScreenSizeProperty.createWithSize(this.mScreenWidth, this.mScreenHeight));
    }

    private Date parseDate(String dateTime) {
        Date result = null;
        int i = 0;
        while (i < DATE_FORMATS.length) {
            try {
                result = new SimpleDateFormat(DATE_FORMATS[i]).parse(dateTime);
            } catch (ParseException e) {
            }
            if (result != null) {
                return result;
            }
            i++;
        }
        return result;
    }

    private String parseRecurrenceRule(Map<String, String> params) throws IllegalArgumentException {
        StringBuilder rule = new StringBuilder();
        if (params.containsKey("frequency")) {
            String frequency = (String) params.get("frequency");
            int interval = -1;
            if (params.containsKey("interval")) {
                interval = Integer.parseInt((String) params.get("interval"));
            }
            if ("daily".equals(frequency)) {
                rule.append("FREQ=DAILY;");
                if (interval != -1) {
                    rule.append(new StringBuilder("INTERVAL=").append(interval).append(";").toString());
                }
            } else if ("weekly".equals(frequency)) {
                rule.append("FREQ=WEEKLY;");
                if (interval != -1) {
                    rule.append(new StringBuilder("INTERVAL=").append(interval).append(";").toString());
                }
                if (params.containsKey("daysInWeek")) {
                    String weekdays = translateWeekIntegersToDays((String) params.get("daysInWeek"));
                    if (weekdays == null) {
                        throw new IllegalArgumentException("invalid ");
                    }
                    rule.append(new StringBuilder("BYDAY=").append(weekdays).append(";").toString());
                }
            } else if ("monthly".equals(frequency)) {
                rule.append("FREQ=MONTHLY;");
                if (interval != -1) {
                    rule.append(new StringBuilder("INTERVAL=").append(interval).append(";").toString());
                }
                if (params.containsKey("daysInMonth")) {
                    String monthDays = translateMonthIntegersToDays((String) params.get("daysInMonth"));
                    if (monthDays == null) {
                        throw new IllegalArgumentException();
                    }
                    rule.append(new StringBuilder("BYMONTHDAY=").append(monthDays).append(";").toString());
                }
            } else {
                throw new IllegalArgumentException("frequency is only supported for daily, weekly, and monthly.");
            }
        }
        return rule.toString();
    }

    private void resetViewToDefaultState() {
        setNativeCloseButtonEnabled(false);
        this.mAdContainerLayout.removeAllViewsInLayout();
        this.mExpansionLayout.removeAllViewsInLayout();
        this.mRootView.removeView(this.mExpansionLayout);
        getMraidView().requestLayout();
        ViewGroup parent = (ViewGroup) this.mPlaceholderView.getParent();
        parent.addView(getMraidView(), this.mViewIndexInParent);
        parent.removeView(this.mPlaceholderView);
        parent.invalidate();
    }

    private void setOrientationLockEnabled(boolean enabled) {
        try {
            int requestedOrientation;
            Activity activity = (Activity) getContext();
            if (enabled) {
                requestedOrientation = activity.getResources().getConfiguration().orientation;
            } else {
                requestedOrientation = this.mOriginalRequestedOrientation;
            }
            activity.setRequestedOrientation(requestedOrientation);
        } catch (ClassCastException e) {
            Log.d(LOGTAG, "Unable to modify device orientation.");
        }
    }

    private void showUserDialog(String imageUrl) {
        new Builder(getContext()).setTitle("Save Image").setMessage("Download image to Picture gallery?").setNegativeButton("Cancel", null).setPositiveButton("Okay", new AnonymousClass_5(imageUrl)).setCancelable(true).show();
    }

    private void showUserToast(String message) {
        this.mHandler.post(new AnonymousClass_3(message));
    }

    private void swapViewWithPlaceholderView() {
        ViewGroup parent = (ViewGroup) getMraidView().getParent();
        if (parent != null) {
            int count = parent.getChildCount();
            int index = 0;
            while (index < count && parent.getChildAt(index) != getMraidView()) {
                index++;
            }
            this.mViewIndexInParent = index;
            parent.addView(this.mPlaceholderView, index, new ViewGroup.LayoutParams(getMraidView().getWidth(), getMraidView().getHeight()));
            parent.removeView(getMraidView());
        }
    }

    private Map<String, Object> translateJSParamsToAndroidCalendarEventMapping(Map<String, String> params) throws Exception {
        Map<String, Object> validatedParamsMapping = new HashMap();
        if (params.containsKey(BannerInfoTable.COLUMN_DESCRIPTION) && params.containsKey(VservConstants.VPLAY0)) {
            validatedParamsMapping.put(BannerInfoTable.COLUMN_TITLE, params.get(BannerInfoTable.COLUMN_DESCRIPTION));
            if (!params.containsKey(VservConstants.VPLAY0) || params.get(VservConstants.VPLAY0) == null) {
                throw new IllegalArgumentException("Invalid calendar event: start is null.");
            }
            Date startDateTime = parseDate((String) params.get(VservConstants.VPLAY0));
            if (startDateTime != null) {
                validatedParamsMapping.put("beginTime", Long.valueOf(startDateTime.getTime()));
                if (params.containsKey("end") && params.get("end") != null) {
                    Date endDateTime = parseDate((String) params.get("end"));
                    if (endDateTime != null) {
                        validatedParamsMapping.put("endTime", Long.valueOf(endDateTime.getTime()));
                    } else {
                        throw new IllegalArgumentException("Invalid calendar event: end time is malformed. Date format expecting (yyyy-MM-DDTHH:MM:SS-xx:xx) or (yyyy-MM-DDTHH:MM-xx:xx) i.e. 2013-08-14T09:00:01-08:00");
                    }
                }
                if (params.containsKey("location")) {
                    validatedParamsMapping.put("eventLocation", params.get("location"));
                }
                if (params.containsKey("summary")) {
                    validatedParamsMapping.put(BannerInfoTable.COLUMN_DESCRIPTION, params.get("summary"));
                }
                if (params.containsKey("transparency")) {
                    int i;
                    String str = "availability";
                    if (((String) params.get("transparency")).equals("transparent")) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    validatedParamsMapping.put(str, Integer.valueOf(i));
                }
                validatedParamsMapping.put("rrule", parseRecurrenceRule(params));
                return validatedParamsMapping;
            } else {
                throw new IllegalArgumentException("Invalid calendar event: start time is malformed. Date format expecting (yyyy-MM-DDTHH:MM:SS-xx:xx) or (yyyy-MM-DDTHH:MM-xx:xx) i.e. 2013-08-14T09:00:01-08:00");
            }
        } else {
            throw new IllegalArgumentException("Missing start and description fields");
        }
    }

    private String translateMonthIntegersToDays(String expression) throws IllegalArgumentException {
        StringBuilder daysResult = new StringBuilder();
        boolean[] daysAlreadyCounted = new boolean[63];
        String[] days = expression.split(",");
        int i = 0;
        while (i < days.length) {
            int dayNumber = Integer.parseInt(days[i]);
            if (!daysAlreadyCounted[dayNumber + 31]) {
                daysResult.append(new StringBuilder(String.valueOf(dayNumberToDayOfMonthString(dayNumber))).append(",").toString());
                daysAlreadyCounted[dayNumber + 31] = true;
            }
            i++;
        }
        if (days.length == 0) {
            throw new IllegalArgumentException("must have at least 1 day of the month if specifying repeating weekly");
        }
        daysResult.deleteCharAt(daysResult.length() - 1);
        return daysResult.toString();
    }

    private String translateWeekIntegersToDays(String expression) throws IllegalArgumentException {
        StringBuilder daysResult = new StringBuilder();
        boolean[] daysAlreadyCounted = new boolean[7];
        String[] days = expression.split(",");
        int i = 0;
        while (i < days.length) {
            int dayNumber = Integer.parseInt(days[i]);
            if (dayNumber == 7) {
                dayNumber = 0;
            }
            if (!daysAlreadyCounted[dayNumber]) {
                daysResult.append(new StringBuilder(String.valueOf(dayNumberToDayOfWeekString(dayNumber))).append(",").toString());
                daysAlreadyCounted[dayNumber] = true;
            }
            i++;
        }
        if (days.length == 0) {
            throw new IllegalArgumentException("must have at least 1 day of the week if specifying repeating weekly");
        }
        daysResult.deleteCharAt(daysResult.length() - 1);
        return daysResult.toString();
    }

    void addCloseEventRegion(FrameLayout frameLayout) {
        int buttonSizePixels = Dips.dipsToIntPixels(50.0f, getContext());
        Interstitials.addCloseEventRegion(frameLayout, new FrameLayout.LayoutParams(buttonSizePixels, buttonSizePixels, 53), this.mCloseOnClickListener);
    }

    protected void close() {
        if (this.mViewState == ViewState.EXPANDED) {
            resetViewToDefaultState();
            setOrientationLockEnabled(false);
            this.mViewState = ViewState.DEFAULT;
            getMraidView().fireChangeEventForProperty(MraidStateProperty.createWithViewState(this.mViewState));
        } else if (this.mViewState == ViewState.DEFAULT) {
            getMraidView().setVisibility(MMAdView.TRANSITION_RANDOM);
            this.mViewState = ViewState.HIDDEN;
            getMraidView().fireChangeEventForProperty(MraidStateProperty.createWithViewState(this.mViewState));
        }
        if (getMraidView().getMraidListener() != null) {
            getMraidView().getMraidListener().onClose(getMraidView(), this.mViewState);
        }
    }

    FrameLayout createAdContainerLayout() {
        return new FrameLayout(getContext());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void createCalendarEvent(java.util.Map<java.lang.String, java.lang.String> r12_params) {
        throw new UnsupportedOperationException("Method not decompiled: com.mopub.mobileads.MraidDisplayController.createCalendarEvent(java.util.Map):void");
        /*
        r11 = this;
        r8 = r11.getMraidView();
        r2 = r8.getContext();
        r8 = com.mopub.mobileads.util.Mraids.isCalendarAvailable(r2);
        if (r8 == 0) goto L_0x00b1;
    L_0x000e:
        r1 = r11.translateJSParamsToAndroidCalendarEventMapping(r12);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r8 = new android.content.Intent;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r9 = "android.intent.action.INSERT";
        r8.<init>(r9);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r9 = "vnd.android.cursor.item/event";
        r5 = r8.setType(r9);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r8 = r1.keySet();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r8 = r8.iterator();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
    L_0x0027:
        r9 = r8.hasNext();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        if (r9 != 0) goto L_0x0036;
    L_0x002d:
        r8 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r5.setFlags(r8);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r2.startActivity(r5);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
    L_0x0035:
        return;
    L_0x0036:
        r6 = r8.next();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r6 = (java.lang.String) r6;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r7 = r1.get(r6);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r9 = r7 instanceof java.lang.Long;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        if (r9 == 0) goto L_0x0062;
    L_0x0044:
        r7 = (java.lang.Long) r7;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r9 = r7.longValue();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r5.putExtra(r6, r9);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        goto L_0x0027;
    L_0x004e:
        r0 = move-exception;
        r8 = "MraidDisplayController";
        r9 = "no calendar app installed";
        android.util.Log.d(r8, r9);
        r8 = r11.getMraidView();
        r9 = com.mopub.mobileads.MraidCommandFactory.MraidJavascriptCommand.CREATE_CALENDAR_EVENT;
        r10 = "Action is unsupported on this device - no calendar app installed";
        r8.fireErrorEvent(r9, r10);
        goto L_0x0035;
    L_0x0062:
        r9 = r7 instanceof java.lang.Integer;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        if (r9 == 0) goto L_0x0097;
    L_0x0066:
        r7 = (java.lang.Integer) r7;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r9 = r7.intValue();	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r5.putExtra(r6, r9);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        goto L_0x0027;
    L_0x0070:
        r4 = move-exception;
        r8 = "MraidDisplayController";
        r9 = new java.lang.StringBuilder;
        r10 = "create calendar: invalid parameters ";
        r9.<init>(r10);
        r10 = r4.getMessage();
        r9 = r9.append(r10);
        r9 = r9.toString();
        android.util.Log.d(r8, r9);
        r8 = r11.getMraidView();
        r9 = com.mopub.mobileads.MraidCommandFactory.MraidJavascriptCommand.CREATE_CALENDAR_EVENT;
        r10 = r4.getMessage();
        r8.fireErrorEvent(r9, r10);
        goto L_0x0035;
    L_0x0097:
        r7 = (java.lang.String) r7;	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        r5.putExtra(r6, r7);	 Catch:{ ActivityNotFoundException -> 0x004e, IllegalArgumentException -> 0x0070, Exception -> 0x009d }
        goto L_0x0027;
    L_0x009d:
        r3 = move-exception;
        r8 = "MraidDisplayController";
        r9 = "could not create calendar event";
        android.util.Log.d(r8, r9);
        r8 = r11.getMraidView();
        r9 = com.mopub.mobileads.MraidCommandFactory.MraidJavascriptCommand.CREATE_CALENDAR_EVENT;
        r10 = "could not create calendar event";
        r8.fireErrorEvent(r9, r10);
        goto L_0x0035;
    L_0x00b1:
        r8 = "MraidDisplayController";
        r9 = "unsupported action createCalendarEvent for devices pre-ICS";
        android.util.Log.d(r8, r9);
        r8 = r11.getMraidView();
        r9 = com.mopub.mobileads.MraidCommandFactory.MraidJavascriptCommand.CREATE_CALENDAR_EVENT;
        r10 = "Action is unsupported on this device (need Android version Ice Cream Sandwich or above)";
        r8.fireErrorEvent(r9, r10);
        goto L_0x0035;
        */
    }

    RelativeLayout createExpansionLayout() {
        return new RelativeLayout(getContext());
    }

    FrameLayout createPlaceholderView() {
        return new FrameLayout(getContext());
    }

    public void destroy() {
        try {
            this.mOrientationBroadcastReceiver.unregister();
        } catch (IllegalArgumentException e) {
            e = e;
            IllegalArgumentException e2;
            if (!e2.getMessage().contains("Receiver not registered")) {
                throw e2;
            }
        }
    }

    protected void expand(String url, int width, int height, boolean shouldUseCustomClose, boolean shouldLockOrientation) {
        if (this.mExpansionStyle != ExpansionStyle.DISABLED) {
            if (url == null || URLUtil.isValidUrl(url)) {
                this.mRootView = (FrameLayout) getMraidView().getRootView().findViewById(16908290);
                useCustomClose(shouldUseCustomClose);
                setOrientationLockEnabled(shouldLockOrientation);
                swapViewWithPlaceholderView();
                View expansionContentView = getMraidView();
                if (url != null) {
                    this.mTwoPartExpansionView = new MraidView(getContext(), getMraidView().getAdConfiguration(), ExpansionStyle.DISABLED, NativeCloseButtonStyle.AD_CONTROLLED, PlacementType.INLINE);
                    this.mTwoPartExpansionView.setMraidListener(new BaseMraidListener() {
                        public void onClose(MraidView view, ViewState newViewState) {
                            MraidDisplayController.this.close();
                        }
                    });
                    this.mTwoPartExpansionView.loadUrl(url);
                    expansionContentView = this.mTwoPartExpansionView;
                }
                expandLayouts(expansionContentView, (int) (((float) width) * this.mDensity), (int) (((float) height) * this.mDensity));
                this.mRootView.addView(this.mExpansionLayout, new LayoutParams(-1, -1));
                if (this.mNativeCloseButtonStyle == NativeCloseButtonStyle.ALWAYS_VISIBLE || !(this.mAdWantsCustomCloseButton || this.mNativeCloseButtonStyle == NativeCloseButtonStyle.ALWAYS_HIDDEN)) {
                    setNativeCloseButtonEnabled(true);
                }
                this.mViewState = ViewState.EXPANDED;
                getMraidView().fireChangeEventForProperty(MraidStateProperty.createWithViewState(this.mViewState));
                if (getMraidView().getMraidListener() != null) {
                    getMraidView().getMraidListener().onExpand(getMraidView());
                }
            } else {
                getMraidView().fireErrorEvent(MraidJavascriptCommand.EXPAND, "URL passed to expand() was invalid.");
            }
        }
    }

    @Deprecated
    public OnClickListener getCloseOnClickListener() {
        return this.mCloseOnClickListener;
    }

    protected void getCurrentPosition() {
        getMraidView().fireErrorEvent(MraidJavascriptCommand.GET_CURRENT_POSITION, "Unsupported action getCurrentPosition");
    }

    protected void getDefaultPosition() {
        getMraidView().fireErrorEvent(MraidJavascriptCommand.GET_DEFAULT_POSITION, "Unsupported action getDefaultPosition");
    }

    protected void getMaxSize() {
        getMraidView().fireErrorEvent(MraidJavascriptCommand.GET_MAX_SIZE, "Unsupported action getMaxSize");
    }

    protected void getScreenSize() {
        getMraidView().fireErrorEvent(MraidJavascriptCommand.GET_SCREEN_SIZE, "Unsupported action getScreenSize");
    }

    protected void initializeJavaScriptState() {
        ArrayList<MraidProperty> properties = new ArrayList();
        properties.add(MraidScreenSizeProperty.createWithSize(this.mScreenWidth, this.mScreenHeight));
        properties.add(MraidViewableProperty.createWithViewable(getMraidView().getIsVisible()));
        getMraidView().fireChangeEventForProperties(properties);
        this.mViewState = ViewState.DEFAULT;
        getMraidView().fireChangeEventForProperty(MraidStateProperty.createWithViewState(this.mViewState));
        initializeSupportedFunctionsProperty();
    }

    protected void initializeSupportedFunctionsProperty() {
        Context context = getContext();
        getMraidView().fireChangeEventForProperty(new MraidSupportsProperty().withTel(Mraids.isTelAvailable(context)).withSms(Mraids.isSmsAvailable(context)).withCalendar(Mraids.isCalendarAvailable(context)).withInlineVideo(Mraids.isInlineVideoAvailable(context)).withStorePicture(Mraids.isStorePictureSupported(context)));
    }

    protected boolean isExpanded() {
        return this.mViewState == ViewState.EXPANDED;
    }

    protected void setNativeCloseButtonEnabled(boolean enabled) {
        if (this.mRootView != null) {
            if (enabled) {
                if (this.mCloseButton == null) {
                    StateListDrawable states = new StateListDrawable();
                    states.addState(new int[]{-16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_NORMAL.decodeImage(this.mRootView.getContext()));
                    states.addState(new int[]{16842919}, Drawables.INTERSTITIAL_CLOSE_BUTTON_PRESSED.decodeImage(this.mRootView.getContext()));
                    this.mCloseButton = new ImageButton(getContext());
                    this.mCloseButton.setImageDrawable(states);
                    this.mCloseButton.setBackgroundDrawable(null);
                    this.mCloseButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            MraidDisplayController.this.close();
                        }
                    });
                }
                int closeButtonSize = Dips.dipsToIntPixels(50.0f, getContext());
                this.mAdContainerLayout.addView(this.mCloseButton, new FrameLayout.LayoutParams(closeButtonSize, closeButtonSize, 5));
            } else {
                this.mAdContainerLayout.removeView(this.mCloseButton);
            }
            MraidView view = getMraidView();
            if (view.getOnCloseButtonStateChangeListener() != null) {
                view.getOnCloseButtonStateChangeListener().onCloseButtonStateChange(view, enabled);
            }
        }
    }

    protected void showUserDownloadImageAlert(String imageUrl) {
        Context context = getContext();
        if (!Mraids.isStorePictureSupported(context)) {
            getMraidView().fireErrorEvent(MraidJavascriptCommand.STORE_PICTURE, "Error downloading file - the device does not have an SD card mounted, or the Android permission is not granted.");
            MoPubLog.d("Error downloading file - the device does not have an SD card mounted, or the Android permission is not granted.");
        } else if (context instanceof Activity) {
            showUserDialog(imageUrl);
        } else {
            showUserToast("Downloading image to Picture gallery...");
            downloadImage(imageUrl);
        }
    }

    protected void showVideo(String videoUrl) {
        MraidVideoPlayerActivity.startMraid(getContext(), videoUrl, getMraidView().getAdConfiguration());
    }

    protected void useCustomClose(boolean shouldUseCustomCloseButton) {
        this.mAdWantsCustomCloseButton = shouldUseCustomCloseButton;
        MraidView view = getMraidView();
        boolean enabled = !shouldUseCustomCloseButton;
        if (view.getOnCloseButtonStateChangeListener() != null) {
            view.getOnCloseButtonStateChangeListener().onCloseButtonStateChange(view, enabled);
        }
    }
}