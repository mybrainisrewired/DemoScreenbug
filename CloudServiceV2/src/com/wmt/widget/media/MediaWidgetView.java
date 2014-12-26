package com.wmt.widget.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterViewAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews.RemoteView;
import android.widget.StackView;
import com.wmt.data.ContentListener;
import com.wmt.data.DataManager;
import com.wmt.data.LocalMediaItem;
import com.wmt.data.MediaItem;
import com.wmt.data.MediaSet;
import com.wmt.data.Path;
import com.wmt.libs.ImageLoadThread;
import com.wmt.media.ImageMan;
import com.wmt.util.LruCache;
import com.wmt.util.Utils;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import org.codehaus.jackson.org.objectweb.asm.Type;

@RemoteView
public class MediaWidgetView extends LinearLayout implements OnItemClickListener, ContentListener {
    private static final int CACHE_CAPACITY = 100;
    private static final int DEFAULT_NUM_VIEWS = 4;
    private static final int HORIZONTAL_SPACING = 4;
    public static final String KEY_SINGLE_IMAGE_URL = "single-image-url";
    private static final int MAX_NUM_VIEWS = 10;
    protected static final int OPENED_DEVICE_UPDATED = 0;
    protected static final int RELOAD = 2;
    private static final int STANDARD_COLUMN_WIDTH = 90;
    private static final String TAG = "MediaWidgetView";
    protected static final int UPDATE_UI = 1;
    public static final String VIDEO_CURRENT = "android.intent.wmt_extra.video_current";
    public static final String VIDEO_LIST = "android.intent.wmt_extra.video_list";
    private AdapterView mAdapterView;
    private int mAppWidgetId;
    private Context mContext;
    protected int mDevId;
    private FrameLayout mEmpty;
    protected Handler mHandler;
    private int mHeightSize;
    private boolean mIsOnDetached;
    private ImageLoadThread mLoadThread;
    private DeviceLoadingTask mLoader;
    private LocalMediaItem mLocalMediaItem;
    private ImageButton mMediaApp;
    private Bundle mMediaInfos;
    private int mMediaItemHeight;
    private int mMediaItemWidth;
    private int mMediaType;
    private ProgressBar mProgress;
    private ImageView mSingleView;
    private WidgetSource mSource;
    private int mType;
    private LruCache<Object, Bitmap> sCache;

    class DeviceLoadingTask extends AsyncTask<Integer, Void, Void> {
        Bitmap mBitmap;
        int mCount;

        DeviceLoadingTask() {
        }

        protected Void doInBackground(Integer... params) {
            MediaWidgetView.this.mSource.reload();
            if (MediaWidgetView.this.mType == 0) {
                this.mBitmap = MediaWidgetView.this.getThumBitmap(OPENED_DEVICE_UPDATED, MediaWidgetView.this.mType);
            }
            return null;
        }

        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(Void result) {
            if (!MediaWidgetView.this.mIsOnDetached) {
                MediaWidgetView.this.sCache.clear();
                if (MediaWidgetView.this.mSource != null) {
                    this.mCount = MediaWidgetView.this.mSource.size();
                } else {
                    this.mCount = 0;
                }
                MediaWidgetView.this.mProgress.setVisibility(Type.DOUBLE);
                if (this.mCount > 0) {
                    MediaWidgetView.this.mEmpty.setVisibility(Type.DOUBLE);
                    if (MediaWidgetView.this.mType != 0) {
                        if (MediaWidgetView.this.mMediaType == 2) {
                            MediaWidgetView.this.calculateMaxNumViews(MediaWidgetView.this.mHeightSize);
                        }
                        MediaWidgetView.this.mAdapterView.setAdapter(new com.wmt.widget.media.MediaWidgetView.MediaAdapter(MediaWidgetView.this.mContext, MediaWidgetView.this.mSource));
                        MediaWidgetView.this.mAdapterView.setVisibility(OPENED_DEVICE_UPDATED);
                        if (MediaWidgetView.this.mMediaType == 2) {
                            ((BaseAdapter) MediaWidgetView.this.mAdapterView.getAdapter()).notifyDataSetChanged();
                        }
                    } else {
                        MediaWidgetView.this.mSingleView.setImageBitmap(this.mBitmap);
                        MediaWidgetView.this.mSingleView.setVisibility(OPENED_DEVICE_UPDATED);
                    }
                    MediaWidgetView.this.mLoader = null;
                } else {
                    MediaWidgetView.this.mEmpty.setVisibility(OPENED_DEVICE_UPDATED);
                }
                MediaWidgetView.this.postInvalidate();
            }
        }
    }

    public class MediaAdapter extends BaseAdapter {
        private WeakReference<Context> mContextRef;
        private WidgetSource mItems;

        public MediaAdapter(Context context, WidgetSource it) {
            this.mItems = it;
            this.mContextRef = new WeakReference(context);
        }

        public int getCount() {
            return this.mItems.size();
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup par) {
            if (position >= this.mItems.size()) {
                return null;
            }
            View photoItem;
            if (convertView == null) {
                photoItem = new MediaWidgetItem((Context) this.mContextRef.get());
                photoItem.setLayoutParams(new LayoutParams(MediaWidgetView.this.mMediaItemWidth, MediaWidgetView.this.mMediaItemHeight));
                photoItem.setScaleType(ScaleType.CENTER_CROP);
                if (MediaWidgetView.this.mMediaType == 2 && par.findViewWithTag(Integer.valueOf(position)) != null) {
                    par.findViewWithTag(Integer.valueOf(position)).setTag(null);
                }
            } else {
                MediaWidgetItem photoItem2 = (MediaWidgetItem) convertView;
                if (convertView.getLayoutParams().width != MediaWidgetView.this.mMediaItemWidth) {
                    photoItem2.setLayoutParams(new LayoutParams(MediaWidgetView.this.mMediaItemWidth, MediaWidgetView.this.mMediaItemHeight));
                    photoItem2.setScaleType(ScaleType.CENTER_CROP);
                }
            }
            photoItem.setTag(Integer.valueOf(position));
            Bitmap b = (Bitmap) MediaWidgetView.this.sCache.get(Integer.valueOf(position));
            if (b == null) {
                Object object = MediaWidgetView.this.mLoadThread.requestLoadQueue(Integer.valueOf(position));
                if (!(object == null || par.findViewWithTag(object) == null)) {
                    MediaWidgetView.this.mHandler.sendMessageDelayed(MediaWidgetView.this.mHandler.obtainMessage(RELOAD, (Integer) object), 100);
                }
            }
            photoItem.setImageBitmap(b);
            return photoItem;
        }
    }

    private static class MediaWidgetItem extends ImageView {
        protected Rect mRect;

        public MediaWidgetItem(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint p = new Paint();
            p.setColor(-1);
            p.setStyle(Style.STROKE);
            p.setStrokeWidth(4.0f);
            canvas.drawRect(this.mRect, p);
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            this.mRect = new Rect(0, 0, right - left, bottom - top);
        }
    }

    private static class Msg {
        Bitmap bitmap;
        Object id;

        private Msg() {
        }
    }

    public MediaWidgetView(Context context) {
        super(context);
        this.mDevId = 1;
        this.sCache = new LruCache(100);
        this.mIsOnDetached = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OPENED_DEVICE_UPDATED:
                        MediaWidgetView.this.openStorage();
                    case UPDATE_UI:
                        MediaWidgetItem photoItem = (MediaWidgetItem) MediaWidgetView.this.mAdapterView.findViewWithTag(((Msg) msg.obj).id);
                        if (photoItem != null) {
                            photoItem.setImageBitmap(((Msg) msg.obj).bitmap);
                            MediaWidgetView.this.sCache.put(((Msg) msg.obj).id, ((Msg) msg.obj).bitmap);
                        }
                    case RELOAD:
                        if (MediaWidgetView.this.mLoadThread != null && MediaWidgetView.this.mAdapterView != null) {
                            Object object = MediaWidgetView.this.mLoadThread.requestLoadQueue(msg.obj);
                            if (object != null && MediaWidgetView.this.mAdapterView.findViewWithTag(object) != null) {
                                MediaWidgetView.this.mHandler.sendMessageDelayed(MediaWidgetView.this.mHandler.obtainMessage(RELOAD, (Integer) object), 100);
                            }
                        }
                    default:
                        break;
                }
            }
        };
        init(context);
    }

    public MediaWidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDevId = 1;
        this.sCache = new LruCache(100);
        this.mIsOnDetached = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OPENED_DEVICE_UPDATED:
                        MediaWidgetView.this.openStorage();
                    case UPDATE_UI:
                        MediaWidgetItem photoItem = (MediaWidgetItem) MediaWidgetView.this.mAdapterView.findViewWithTag(((Msg) msg.obj).id);
                        if (photoItem != null) {
                            photoItem.setImageBitmap(((Msg) msg.obj).bitmap);
                            MediaWidgetView.this.sCache.put(((Msg) msg.obj).id, ((Msg) msg.obj).bitmap);
                        }
                    case RELOAD:
                        if (MediaWidgetView.this.mLoadThread != null && MediaWidgetView.this.mAdapterView != null) {
                            Object object = MediaWidgetView.this.mLoadThread.requestLoadQueue(msg.obj);
                            if (object != null && MediaWidgetView.this.mAdapterView.findViewWithTag(object) != null) {
                                MediaWidgetView.this.mHandler.sendMessageDelayed(MediaWidgetView.this.mHandler.obtainMessage(RELOAD, (Integer) object), 100);
                            }
                        }
                    default:
                        break;
                }
            }
        };
        init(context);
    }

    public MediaWidgetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mDevId = 1;
        this.sCache = new LruCache(100);
        this.mIsOnDetached = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OPENED_DEVICE_UPDATED:
                        MediaWidgetView.this.openStorage();
                    case UPDATE_UI:
                        MediaWidgetItem photoItem = (MediaWidgetItem) MediaWidgetView.this.mAdapterView.findViewWithTag(((Msg) msg.obj).id);
                        if (photoItem != null) {
                            photoItem.setImageBitmap(((Msg) msg.obj).bitmap);
                            MediaWidgetView.this.sCache.put(((Msg) msg.obj).id, ((Msg) msg.obj).bitmap);
                        }
                    case RELOAD:
                        if (MediaWidgetView.this.mLoadThread != null && MediaWidgetView.this.mAdapterView != null) {
                            Object object = MediaWidgetView.this.mLoadThread.requestLoadQueue(msg.obj);
                            if (object != null && MediaWidgetView.this.mAdapterView.findViewWithTag(object) != null) {
                                MediaWidgetView.this.mHandler.sendMessageDelayed(MediaWidgetView.this.mHandler.obtainMessage(RELOAD, (Integer) object), 100);
                            }
                        }
                    default:
                        break;
                }
            }
        };
        init(context);
    }

    private void calculateMaxNumViews(int heightSize) {
        int maxNunViews = Math.min((heightSize - 170) / 25, this.mSource.size());
        StackView stackView = (StackView) this.mAdapterView;
        if (maxNunViews > 10) {
            maxNunViews = 10;
        }
        stackView.setMaxNumViews(Math.max(maxNunViews, HORIZONTAL_SPACING));
        this.mHeightSize = heightSize;
    }

    private static String convertSize(Long size) {
        String strSize = "";
        if (size.longValue() >= 1073741824) {
            return ((((float) size.longValue()) / 1.07374182E9f) + "000").substring(OPENED_DEVICE_UPDATED, String.valueOf(((float) size.longValue()) / 1.07374182E9f).indexOf(".") + 2) + "GB";
        } else if (size.longValue() >= 1048576) {
            return ((((float) size.longValue()) / 1048576.0f) + "000").substring(OPENED_DEVICE_UPDATED, String.valueOf(((float) size.longValue()) / 1048576.0f).indexOf(".") + 2) + "MB";
        } else if (size.longValue() < 1024) {
            return size.longValue() < 1024 ? String.valueOf(size) + "B" : strSize;
        } else {
            return ((((float) size.longValue()) / 1024.0f) + "000").substring(OPENED_DEVICE_UPDATED, String.valueOf(((float) size.longValue()) / 1024.0f).indexOf(".") + 2) + "KB";
        }
    }

    private void countItemWidth(int widthSize, int heightSize) {
        if (this.mMediaType == 1) {
            int width = widthSize;
            int count = (width + 4) / 94;
            if (count != 0) {
                this.mMediaItemWidth = ((width - (count * 90)) - ((count - 1) * 4)) / count + 90;
            } else {
                this.mMediaItemWidth = 90;
            }
            this.mMediaItemHeight = this.mMediaItemWidth;
        } else if (this.mMediaType == 2) {
            this.mMediaItemWidth = Math.min(widthSize - 8, 220);
            this.mMediaItemHeight = 170;
            if (this.mHeightSize != heightSize && this.mMediaType == 2) {
                calculateMaxNumViews(heightSize);
            }
        }
        if (this.mMediaType == 1) {
            ((GridView) this.mAdapterView).setColumnWidth(this.mMediaItemWidth);
        }
        if (this.mLoadThread != null) {
            this.mLoadThread.setQueueSize(this.mAdapterView.getChildCount());
        }
    }

    private void deinit() {
        this.mIsOnDetached = true;
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
        }
        if (this.mLoadThread != null) {
            this.mLoadThread.exit();
            try {
                this.mLoadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.sCache.clear();
        if (this.mSource != null) {
            this.mSource.close();
        }
    }

    private final View findViewByNameId(Resources res, View convertView, String name) {
        return convertView.findViewById(res.getIdentifier(name, "id", this.mContext.getPackageName()));
    }

    private void getMediaInfos(Bundle imageInfos) {
        ensureWorkerThread();
        if (imageInfos != null) {
            if (this.mSource != null) {
                this.mSource.close();
            }
            this.mAppWidgetId = imageInfos.getInt("appWidgetId");
            this.mType = imageInfos.getInt("widget-type");
            this.mMediaType = imageInfos.getInt("media-type");
            if (this.mType == 2) {
                this.mSource = new LocalMediaSource(this.mContext, Integer.valueOf(imageInfos.getString("album-path")).intValue(), this.mMediaType);
            } else if (this.mType == 1) {
                path = Path.fromString(imageInfos.getString("album-path"));
                manager = new DataManager(this.mContext);
                manager.initializeSourceMap();
                this.mSource = new MediaSetSource((MediaSet) manager.getMediaObject(path), null, null);
            } else {
                try {
                    manager = new DataManager(this.mContext);
                    manager.initializeSourceMap();
                    Uri uri = Uri.parse(imageInfos.getString(KEY_SINGLE_IMAGE_URL));
                    path = manager.findPathByUri(uri);
                    this.mLocalMediaItem = (LocalMediaItem) manager.getMediaObject(path);
                    if (this.mLocalMediaItem != null) {
                        this.mSource = new MediaSetSource(manager.getMediaObject(manager.getDefaultSetOf(path)), this.mLocalMediaItem, manager.findPathByUri(uri));
                    } else {
                        this.mSource = new EmptySource();
                    }
                } catch (Exception e) {
                    Exception e2 = e;
                    this.mSource = new EmptySource();
                    e2.printStackTrace();
                }
            }
            this.mSource.setContentListener(this);
            this.mHandler.sendEmptyMessage(OPENED_DEVICE_UPDATED);
        }
    }

    private Bitmap getThumBitmap(int id, int type) {
        MediaItem mediaItem = this.mSource.getMediaItem(id);
        if (mediaItem == null) {
            return null;
        }
        byte[] encodedData = mediaItem.getThumbData();
        if (encodedData == null) {
            return null;
        }
        Bitmap bitmap;
        if (type != 0) {
            bitmap = ImageMan.loadMem(encodedData, null, encodedData.length, this.mMediaItemWidth, this.mMediaItemHeight, ImageMan.thumbclip);
        } else {
            bitmap = this.mLocalMediaItem == null ? null : BitmapFactory.decodeFile(this.mLocalMediaItem.getFilePath());
        }
        if (bitmap == null) {
            return null;
        }
        if (this.mMediaType == 1 && mediaItem.getRotation() != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate((float) mediaItem.getRotation());
            Bitmap bmp = Bitmap.createBitmap(bitmap, OPENED_DEVICE_UPDATED, OPENED_DEVICE_UPDATED, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != bmp) {
                bitmap.recycle();
            }
            return bmp;
        } else if (this.mMediaType != 2) {
            return bitmap;
        } else {
            Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
            Canvas canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
            Paint paint = new Paint();
            paint.setColor(-1342177280);
            canvas.drawRect(0.0f, (float) (this.mMediaItemHeight - 20), (float) this.mMediaItemWidth, (float) this.mMediaItemHeight, paint);
            paint.setColor(-1);
            paint.setTextSize(14.0f);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.createFromFile("/system/fonts/Roboto-Regular.ttf"));
            String s = "...";
            String name = mediaItem.getDisplayName();
            String size = convertSize(Long.valueOf(mediaItem.getSize()));
            int sizeLength = (int) paint.measureText(size);
            int length = (int) paint.measureText(s);
            if (((int) paint.measureText(name)) + sizeLength > bitmap.getWidth() - 8) {
                name = name.substring(OPENED_DEVICE_UPDATED, paint.breakText(name, true, (float) (bitmap.getWidth() - sizeLength - length - 8), null)) + s;
            }
            canvas.drawText(name, 4.0f, (float) (bitmap.getHeight() - 4), paint);
            canvas.drawText(size, (float) (bitmap.getWidth() - sizeLength - 4), (float) (bitmap.getHeight() - 4), paint);
            canvas.setBitmap(Utils.s_nullBitmap);
            bitmap.recycle();
            return tempBitmap;
        }
    }

    private void init() {
        this.mIsOnDetached = false;
        getMediaInfos(this.mMediaInfos);
    }

    private void init(Context context) {
        this.mContext = context;
        setFocusable(true);
        setClickable(true);
        PackageManager pm = context.getPackageManager();
        try {
            String pn = this.mContext.getPackageName();
            Resources res = pm.getResourcesForApplication(pn);
            View convertView = View.inflate(context, res.getIdentifier("media_layout", "layout", pn), null);
            this.mAdapterView = (AdapterView) findViewByNameId(res, convertView, "photo_grid");
            this.mProgress = (ProgressBar) findViewByNameId(res, convertView, "loading_grid");
            this.mSingleView = (ImageView) findViewByNameId(res, convertView, "singleview");
            this.mSingleView.setScaleType(ScaleType.CENTER_CROP);
            this.mSingleView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (MediaWidgetView.this.mSource != null && MediaWidgetView.this.mMediaType == 1) {
                        Intent intent = new Intent();
                        intent.setData(MediaWidgetView.this.mSource.getContentUri(OPENED_DEVICE_UPDATED));
                        intent.setComponent(new ComponentName("com.wmt.photo", "com.wmt.photo.PhotoMainActivity"));
                        intent.addFlags(268435456);
                        intent.setAction("android.intent.action.VIEW");
                        try {
                            MediaWidgetView.this.mContext.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            this.mEmpty = (FrameLayout) findViewByNameId(res, convertView, "empty_grid");
            this.mMediaApp = (ImageButton) findViewByNameId(res, convertView, "photoapp");
            this.mMediaApp.setImageDrawable(Utils.getApkIcon(this.mContext, pn));
            this.mMediaApp.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("appWidgetId", MediaWidgetView.this.mAppWidgetId);
                    intent.addFlags(268468224);
                    try {
                        intent.setComponent(new ComponentName(MediaWidgetView.this.mContext.getPackageName(), MediaWidgetView.this.mContext.getPackageName() + ".widget.WidgetConfigure"));
                        MediaWidgetView.this.mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.mAdapterView.setOnItemClickListener(this);
            addView(convertView);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openStorage() {
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
        }
        this.mAdapterView.setAdapter(null);
        if (this.mMediaType == 2) {
            try {
                Method method = AdapterViewAnimator.class.getDeclaredMethod("configureViewAnimator", new Class[]{Integer.TYPE, Integer.TYPE});
                method.setAccessible(true);
                method.invoke(this.mAdapterView, new Object[]{Integer.valueOf(HORIZONTAL_SPACING), Integer.valueOf(UPDATE_UI)});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mAdapterView.setVisibility(HORIZONTAL_SPACING);
        this.mEmpty.setVisibility(Type.DOUBLE);
        this.mSingleView.setVisibility(Type.DOUBLE);
        this.mProgress.setVisibility(OPENED_DEVICE_UPDATED);
        this.mLoader = (DeviceLoadingTask) new DeviceLoadingTask().execute(new Integer[]{Integer.valueOf(OPENED_DEVICE_UPDATED)});
        postInvalidate();
    }

    synchronized void ensureWorkerThread() {
        int i = UPDATE_UI;
        synchronized (this) {
            reqWorkerThreadExitAndWait();
            this.mLoadThread = new ImageLoadThread() {
                protected void loadThumbBitmap(Object object) {
                    Msg msg = new Msg();
                    msg.bitmap = MediaWidgetView.this.getThumBitmap(((Integer) object).intValue(), MediaWidgetView.this.mType);
                    msg.id = object;
                    MediaWidgetView.this.mHandler.sendMessage(MediaWidgetView.this.mHandler.obtainMessage(UPDATE_UI, msg));
                }
            };
            ImageLoadThread imageLoadThread = this.mLoadThread;
            if (this.mLoadThread.getPriority() - 1 >= 1) {
                i = this.mLoadThread.getPriority() - 1;
            }
            imageLoadThread.setPriority(i);
            this.mLoadThread.start();
        }
    }

    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow: call init");
        super.onAttachedToWindow();
        init();
    }

    public void onContentDirty() {
        this.mHandler.sendEmptyMessage(OPENED_DEVICE_UPDATED);
    }

    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow: call deinit");
        super.onDetachedFromWindow();
        deinit();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.addFlags(268468224);
        if (this.mMediaType == 1) {
            intent.setData(this.mSource.getContentUri(arg2));
            intent.setComponent(new ComponentName("com.wmt.photo", "com.wmt.photo.PhotoMainActivity"));
            intent.setAction("android.intent.action.VIEW");
        } else {
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(this.mSource.getContentUri(arg2), "video/*");
            intent.putExtra("com.wmt.video", "com.wmt.video");
            intent.putStringArrayListExtra(VIDEO_LIST, this.mSource.getPlayList());
            intent.putExtra(VIDEO_CURRENT, arg2);
        }
        try {
            this.mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mAdapterView.getLayoutParams();
        layoutParams.height = heightSize;
        this.mAdapterView.setLayoutParams(layoutParams);
        countItemWidth(widthSize, heightSize);
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
        setMeasuredDimension(widthSize, heightSize);
    }

    void reqWorkerThreadExitAndWait() {
        if (this.mLoadThread != null) {
            this.mLoadThread.exit();
            try {
                this.mLoadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mLoadThread = null;
        }
    }

    @RemotableViewMethod
    public void setMediaInfo(Bundle bundle) {
        this.mMediaInfos = bundle;
        getMediaInfos(this.mMediaInfos);
    }
}