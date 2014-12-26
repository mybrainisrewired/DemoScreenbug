package com.wmt.MusicPlayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews.RemoteView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.wmt.MusicPlayer.MusicUtils.ServiceToken;
import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.libs.ImageLoadThread;
import com.wmt.util.LruCache;
import com.wmt.util.Utils;
import java.util.Arrays;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;
import org.codehaus.jackson.org.objectweb.asm.Type;

@RemoteView
public class ListMusicView extends LinearLayout implements ServiceConnection, OnItemClickListener {
    private static final int CACHE_CAPACITY = 100;
    private static final String TAG = "ListMusicView";
    private static LruCache<Long, Drawable> mArtCache = null;
    protected static final int mSliceHeight = 120;
    protected static final int mSliceWidth = 120;
    private static final String pn = "com.wmt.music";
    private MusicListAdapter mAdapter;
    private int mButtonPause;
    private int mButtonPausePress;
    private int mButtonPlay;
    private int mButtonPlayPress;
    private Context mContext;
    private String[] mCursorCols;
    private BitmapDrawable mDefaultBigAlbumIcon;
    private BitmapDrawable mDefaultSmallAlbumIcon;
    private int mDevice;
    private TextView mEmpty;
    private ImageButton mImageButton;
    private int mLayoutClickItemId;
    private int mLayoutListId;
    private int mLayoutNormalItemId;
    private ListView mList;
    private ImageLoadThread mLoadThread;
    private ImageView mMusicAppIcon;
    private PlayingItem mPlayingItem;
    private ProgressBar mProgress;
    private Handler mProgressRefresher;
    private boolean mRegist;
    private BroadcastReceiver mScanListener;
    private SeekBar mSeekBar;
    private boolean mSeeking;
    private ServiceToken mServiceToken;
    private final Handler mSetImageHandler;
    private BroadcastReceiver mStatusListener;
    private View mTopShadowRepeat;
    private Cursor mTrackCursor;
    private String mUnknownAlbum;
    private String mUnknownArtist;
    private BroadcastReceiver mUnmountReceiver;
    private boolean paused;

    public class Msg {
        long audio_id;
        Drawable drawable;
    }

    public class MusicListAdapter extends SimpleCursorAdapter {
        private static final int REFRESH = 1;
        private static final int TYPE_No_PLAYING = 1;
        private static final int TYPE_PLAYING = 0;
        int mArtistIdx;
        int mAudioIdIdx;
        private TextView mCurrentTime;
        int mDurationIdx;
        private final Handler mHandler;
        private LayoutInflater mInflater;
        private long mLastSeekEventTime;
        private ListMusicView mListMusicView;
        private long mPosOverride;
        private OnSeekBarChangeListener mSeekListener;
        int mTitleIdx;

        class AnonymousClass_2 implements OnClickListener {
            final /* synthetic */ int val$position;

            AnonymousClass_2(int i) {
                this.val$position = i;
            }

            public void onClick(View v) {
                com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mProgressRefresher.removeCallbacksAndMessages(null);
                MusicUtils.playPauseClicked(com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mContext, com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mListMusicView.mTrackCursor, this.val$position, com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mDevice);
                if (MusicUtils.isPlaying()) {
                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mProgressRefresher.postDelayed(new com.wmt.MusicPlayer.ListMusicView.ProgressRefresher(), 200);
                }
            }
        }

        MusicListAdapter(Context context, int layout, ListMusicView view, Cursor cursor, String[] from, int[] to) {
            super(context, layout, cursor, from, to);
            this.mPosOverride = -1;
            this.mSeekListener = new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && MusicUtils.sService != null) {
                        long now = SystemClock.elapsedRealtime();
                        if (now - com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mLastSeekEventTime > 250) {
                            com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mLastSeekEventTime = now;
                            com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mPosOverride = (long) progress;
                            MusicUtils.seekTo((long) progress);
                        }
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mSeeking = true;
                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mLastSeekEventTime = 0;
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mSeeking = false;
                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.mPosOverride = -1;
                }
            };
            this.mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case TYPE_No_PLAYING:
                            com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.queueNextRefresh(com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.refreshNow());
                        default:
                            break;
                    }
                }
            };
            this.mListMusicView = view;
            getColumnIndices(cursor);
            this.mInflater = LayoutInflater.from(context);
        }

        private void getColumnIndices(Cursor cursor) {
            if (cursor != null) {
                this.mTitleIdx = cursor.getColumnIndexOrThrow("title");
                this.mArtistIdx = cursor.getColumnIndexOrThrow("artist");
                this.mDurationIdx = cursor.getColumnIndexOrThrow("duration");
                try {
                    this.mAudioIdIdx = cursor.getColumnIndexOrThrow("audio_id");
                } catch (IllegalArgumentException e) {
                    this.mAudioIdIdx = cursor.getColumnIndexOrThrow("_id");
                }
            }
        }

        private void queueNextRefresh(long delay) {
            if (!ListMusicView.this.paused) {
                Message msg = this.mHandler.obtainMessage(TYPE_No_PLAYING);
                this.mHandler.removeMessages(TYPE_No_PLAYING);
                this.mHandler.sendMessageDelayed(msg, delay);
            }
        }

        private long refreshNow() {
            if (MusicUtils.sService == null || this.mCurrentTime == null) {
                return 500;
            }
            try {
                long pos = this.mPosOverride < 0 ? MusicUtils.sService.position() : this.mPosOverride;
                long remaining = 1000 - pos % 1000;
                if (pos < 0 || MusicUtils.sService.duration() <= 0) {
                    if (this.mCurrentTime != null) {
                        this.mCurrentTime.setText("--:--");
                    }
                    ListMusicView.this.mProgress.setProgress(1000);
                    return remaining;
                } else {
                    this.mCurrentTime.setText(MusicUtils.makeTimeString(this.mContext, pos / 1000));
                    if (MusicUtils.sService.isPlaying()) {
                        this.mCurrentTime.setVisibility(0);
                    } else {
                        this.mCurrentTime.setVisibility(this.mCurrentTime.getVisibility() == 4 ? 0 : JsonWriteContext.STATUS_EXPECT_VALUE);
                        remaining = 500;
                    }
                    long duration = MusicUtils.getDuration();
                    if (duration <= 0) {
                        ListMusicView.this.mProgress.setProgress(0);
                        return remaining;
                    } else {
                        ListMusicView.this.mProgress.setProgress((int) ((1000 * pos) / duration));
                        return remaining;
                    }
                }
            } catch (RemoteException e) {
                return 500;
            }
        }

        public void changeCursor(Cursor cursor) {
            if (cursor == null || cursor.getCount() <= 0) {
                ListMusicView.this.mEmpty.setVisibility(0);
                ListMusicView.this.mList.setVisibility(JsonWriteContext.STATUS_EXPECT_VALUE);
            } else {
                if (cursor != this.mListMusicView.mTrackCursor) {
                    this.mListMusicView.mTrackCursor = cursor;
                    super.changeCursor(cursor);
                    getColumnIndices(cursor);
                }
                ListMusicView.this.mEmpty.setVisibility(Type.DOUBLE);
                ListMusicView.this.mList.setVisibility(0);
            }
            ListMusicView.this.mList.invalidateViews();
        }

        public int getItemViewType(int position) {
            return this.mCursor.getLong(this.mAudioIdIdx) == MusicUtils.getCurrentAudioId() ? 0 : TYPE_No_PLAYING;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            NameNotFoundException e;
            if (this.mCursor.moveToPosition(position)) {
                ViewHolderNoPlaying holderNoPlaying = null;
                ViewHolderPlaying holderPlaying = null;
                try {
                    Resources res = this.mContext.getPackageManager().getResourcesForApplication(pn);
                    int type = getItemViewType(position);
                    boolean newview = false;
                    if (convertView != null) {
                        switch (type) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                if (convertView.getTag() instanceof ViewHolderNoPlaying) {
                                    newview = true;
                                }
                                break;
                            case TYPE_No_PLAYING:
                                if (convertView.getTag() instanceof ViewHolderPlaying) {
                                    newview = true;
                                }
                                break;
                        }
                    }
                    if (convertView == null || newview) {
                        switch (type) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                convertView = this.mInflater.inflate(ListMusicView.this.mLayoutClickItemId, null);
                                ViewHolderPlaying holderPlaying2 = new ViewHolderPlaying(null);
                                try {
                                    holderPlaying2.mTitle = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_title");
                                    holderPlaying2.mArtist = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_artist");
                                    holderPlaying2.mThumb = (ImageView) ListMusicView.this.findViewByNameId(res, convertView, "music_thumb");
                                    holderPlaying2.mButton = (ImageButton) ListMusicView.this.findViewByNameId(res, convertView, "paly_button");
                                    holderPlaying2.mSeekBar = (SeekBar) ListMusicView.this.findViewByNameId(res, convertView, "seek");
                                    holderPlaying2.mPosition = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_position");
                                    holderPlaying2.mDuration = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_duration");
                                    convertView.setTag(holderPlaying2);
                                    holderPlaying = holderPlaying2;
                                } catch (NameNotFoundException e2) {
                                    e = e2;
                                    e.printStackTrace();
                                    return convertView;
                                }
                                break;
                            case TYPE_No_PLAYING:
                                convertView = this.mInflater.inflate(ListMusicView.this.mLayoutNormalItemId, null);
                                ViewHolderNoPlaying holderNoPlaying2 = new ViewHolderNoPlaying(null);
                                try {
                                    holderNoPlaying2.mTitle = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_title");
                                    holderNoPlaying2.mArtist = (TextView) ListMusicView.this.findViewByNameId(res, convertView, "music_artist");
                                    holderNoPlaying2.mThumb = (ImageView) ListMusicView.this.findViewByNameId(res, convertView, "music_thumb");
                                    convertView.setTag(holderNoPlaying2);
                                    holderNoPlaying = holderNoPlaying2;
                                } catch (NameNotFoundException e3) {
                                    e = e3;
                                    e.printStackTrace();
                                    return convertView;
                                }
                                break;
                        }
                    } else {
                        switch (type) {
                            case LocalAudioAll.SORT_BY_TITLE:
                                holderPlaying = (ViewHolderPlaying) convertView.getTag();
                                break;
                            case TYPE_No_PLAYING:
                                holderNoPlaying = (ViewHolderNoPlaying) convertView.getTag();
                                break;
                            default:
                                break;
                        }
                    }
                    String artistName = this.mCursor.getString(this.mArtistIdx);
                    String songTitle = this.mCursor.getString(this.mTitleIdx);
                    if (artistName == null || artistName.equals("<unknown>")) {
                        artistName = ListMusicView.this.mUnknownArtist;
                    }
                    if (songTitle == null || songTitle.equals("<unknown>")) {
                        songTitle = ListMusicView.this.mUnknownAlbum;
                    }
                    switch (type) {
                        case LocalAudioAll.SORT_BY_TITLE:
                            holderPlaying.mSeekBar.setOnSeekBarChangeListener(this.mSeekListener);
                            holderPlaying.mSeekBar.setMax((int) MusicUtils.getDuration());
                            ListMusicView.this.mSeekBar = holderPlaying.mSeekBar;
                            ListMusicView.this.mImageButton = holderPlaying.mButton;
                            ListMusicView.this.updatePlayPauseButton();
                            ListMusicView.this.mImageButton.setOnTouchListener(new OnTouchListener() {
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == 0) {
                                        if (MusicUtils.isPlaying()) {
                                            v.setBackgroundResource(com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mButtonPausePress);
                                        } else {
                                            v.setBackgroundResource(com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.mButtonPlayPress);
                                        }
                                    } else if (event.getAction() == 1) {
                                        com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.updatePlayPauseButton();
                                    }
                                    return false;
                                }
                            });
                            this.mCurrentTime = holderPlaying.mPosition;
                            holderPlaying.mButton.setOnClickListener(new AnonymousClass_2(position));
                            holderPlaying.mDuration.setText(MusicUtils.makeTimeString(this.mContext, MusicUtils.getDuration() / 1000));
                            holderPlaying.mArtist.setText(this.mCursor.getString(this.mArtistIdx));
                            holderPlaying.mTitle.setText(this.mCursor.getString(this.mTitleIdx));
                            if (ListMusicView.this.mPlayingItem.mPlayingAudioId != this.mCursor.getLong(this.mAudioIdIdx)) {
                                ListMusicView.this.mPlayingItem.mPlayingAudioId = this.mCursor.getLong(this.mAudioIdIdx);
                                ListMusicView.this.mPlayingItem.mPlayingDrawable = MusicUtils.getNoCachedArtwork(this.mContext, this.mCursor.getLong(this.mAudioIdIdx), ListMusicView.this.mDevice, ListMusicView.this.mDefaultBigAlbumIcon);
                            }
                            holderPlaying.mThumb.setImageDrawable(ListMusicView.this.mPlayingItem.mPlayingDrawable);
                            holderPlaying.mThumb.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                    com.wmt.MusicPlayer.ListMusicView.MusicListAdapter.this.this$0.startMusicActivity(true);
                                }
                            });
                            break;
                        case TYPE_No_PLAYING:
                            holderNoPlaying.mArtist.setText(this.mCursor.getString(this.mArtistIdx));
                            holderNoPlaying.mTitle.setText(this.mCursor.getString(this.mTitleIdx));
                            Drawable drawable = (Drawable) mArtCache.get(Long.valueOf(this.mCursor.getLong(this.mAudioIdIdx)));
                            if (drawable != null) {
                                holderNoPlaying.mThumb.setImageDrawable(drawable);
                            } else {
                                holderNoPlaying.mThumb.setTag(Long.valueOf(this.mCursor.getLong(this.mAudioIdIdx)));
                                holderNoPlaying.mThumb.setImageDrawable(ListMusicView.this.mDefaultSmallAlbumIcon);
                                ListMusicView.this.mLoadThread.requestLoadQueue(Long.valueOf(this.mCursor.getLong(this.mAudioIdIdx)));
                            }
                            break;
                    }
                } catch (NameNotFoundException e4) {
                    e = e4;
                    e.printStackTrace();
                    return convertView;
                }
                return convertView;
            } else {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }
        }

        public int getViewTypeCount() {
            return ClassWriter.COMPUTE_FRAMES;
        }

        public void setListMusicView(ListMusicView newListMusicView) {
            this.mListMusicView = newListMusicView;
        }
    }

    private class NowPlayingCursor extends AbstractCursor {
        private String[] mCols;
        private Cursor mCurrentPlaylistCursor;
        private long[] mCursorIdxs;
        private long[] mNowPlaying;
        private IMediaPlaybackService mService;
        private int mSize;

        public NowPlayingCursor(IMediaPlaybackService service, String[] cols) {
            this.mService = service;
            this.mCols = cols;
            makeNowPlayingCursor();
        }

        private void makeNowPlayingCursor() {
            mArtCache.clear();
            ListMusicView.this.mPlayingItem.clear();
            this.mCurrentPlaylistCursor = null;
            try {
                if (MusicUtils.sService == null || MusicUtils.sService.getQueue().length < 1) {
                    ListMusicView.this.mDevice = -1;
                } else {
                    this.mNowPlaying = MusicUtils.sService.getQueue();
                    ListMusicView.this.mDevice = MusicUtils.sService.getDeviceId();
                    this.mSize = this.mNowPlaying.length;
                    if (this.mSize != 0) {
                        StringBuilder where = new StringBuilder();
                        where.append("_id IN (");
                        int i = 0;
                        while (i < this.mSize) {
                            where.append(this.mNowPlaying[i]);
                            if (i < this.mSize - 1) {
                                where.append(",");
                            }
                            i++;
                        }
                        where.append(")");
                        this.mCurrentPlaylistCursor = MusicUtils.query(ListMusicView.this.mContext, MusicUtils.getDeviceUri(ListMusicView.this.mDevice), this.mCols, where.toString(), null, "_id");
                        if (this.mCurrentPlaylistCursor == null) {
                            this.mSize = 0;
                        } else {
                            int size = this.mCurrentPlaylistCursor.getCount();
                            this.mCursorIdxs = new long[size];
                            this.mCurrentPlaylistCursor.moveToFirst();
                            int colidx = this.mCurrentPlaylistCursor.getColumnIndexOrThrow("_id");
                            i = 0;
                            while (i < size) {
                                this.mCursorIdxs[i] = this.mCurrentPlaylistCursor.getLong(colidx);
                                this.mCurrentPlaylistCursor.moveToNext();
                                i++;
                            }
                            this.mCurrentPlaylistCursor.moveToFirst();
                            int removed = 0;
                            try {
                                i = this.mNowPlaying.length - 1;
                                while (i >= 0) {
                                    long trackid = this.mNowPlaying[i];
                                    if (Arrays.binarySearch(this.mCursorIdxs, trackid) < 0) {
                                        removed += this.mService.removeTrack(trackid, ListMusicView.this.mDevice);
                                    }
                                    i--;
                                }
                                if (removed > 0) {
                                    this.mNowPlaying = this.mService.getQueue();
                                    this.mSize = this.mNowPlaying.length;
                                    if (this.mSize == 0) {
                                        this.mCursorIdxs = null;
                                    }
                                }
                            } catch (RemoteException e) {
                                this.mNowPlaying = new long[0];
                            }
                        }
                    }
                }
            } catch (RemoteException e2) {
                this.mNowPlaying = new long[0];
            }
        }

        public void deactivate() {
            if (this.mCurrentPlaylistCursor != null) {
                this.mCurrentPlaylistCursor.deactivate();
            }
        }

        public String[] getColumnNames() {
            return this.mCols;
        }

        public int getCount() {
            return this.mSize;
        }

        public double getDouble(int column) {
            return this.mCurrentPlaylistCursor.getDouble(column);
        }

        public float getFloat(int column) {
            return this.mCurrentPlaylistCursor.getFloat(column);
        }

        public int getInt(int column) {
            try {
                return this.mCurrentPlaylistCursor.getInt(column);
            } catch (Exception e) {
                onChange(true);
                return 0;
            }
        }

        public long getLong(int column) {
            try {
                return this.mCurrentPlaylistCursor.getLong(column);
            } catch (Exception e) {
                onChange(true);
                return 0;
            }
        }

        public short getShort(int column) {
            return this.mCurrentPlaylistCursor.getShort(column);
        }

        public String getString(int column) {
            try {
                return this.mCurrentPlaylistCursor.getString(column);
            } catch (Exception e) {
                onChange(true);
                return "";
            }
        }

        public int getType(int column) {
            return this.mCurrentPlaylistCursor.getType(column);
        }

        public boolean isNull(int column) {
            return this.mCurrentPlaylistCursor.isNull(column);
        }

        public boolean onMove(int oldPosition, int newPosition) {
            if (oldPosition == newPosition) {
                return true;
            }
            if (this.mNowPlaying == null || this.mCursorIdxs == null || newPosition >= this.mNowPlaying.length) {
                return false;
            }
            this.mCurrentPlaylistCursor.moveToPosition(Arrays.binarySearch(this.mCursorIdxs, this.mNowPlaying[newPosition]));
            return true;
        }

        public boolean requery() {
            makeNowPlayingCursor();
            return true;
        }
    }

    private static class PlayingItem {
        long mPlayingAudioId;
        Drawable mPlayingDrawable;

        private PlayingItem() {
            this.mPlayingAudioId = -1;
        }

        public void clear() {
            this.mPlayingDrawable = null;
            this.mPlayingAudioId = -1;
        }
    }

    public class ProgressRefresher implements Runnable {
        public void run() {
            if (!(ListMusicView.this.mSeekBar == null || ListMusicView.this.mSeeking || MusicUtils.getDuration() == 0)) {
                ListMusicView.this.mSeekBar.setProgress((int) MusicUtils.getPosition());
            }
            ListMusicView.this.mProgressRefresher.removeCallbacksAndMessages(null);
            if (MusicUtils.isPlaying()) {
                ListMusicView.this.mProgressRefresher.postDelayed(new com.wmt.MusicPlayer.ListMusicView.ProgressRefresher(), 200);
            }
        }
    }

    private class ViewHolderNoPlaying {
        TextView mArtist;
        ImageView mThumb;
        TextView mTitle;

        private ViewHolderNoPlaying() {
        }
    }

    private class ViewHolderPlaying {
        TextView mArtist;
        ImageButton mButton;
        TextView mDuration;
        TextView mPosition;
        SeekBar mSeekBar;
        ImageView mThumb;
        TextView mTitle;

        private ViewHolderPlaying() {
        }
    }

    static {
        mArtCache = new LruCache(100);
    }

    public ListMusicView(Context context) {
        super(context);
        this.mRegist = false;
        this.mSeeking = false;
        this.mPlayingItem = new PlayingItem();
        this.mDevice = -1;
        this.mUnmountReceiver = null;
        this.mSetImageHandler = new Handler() {
            public void handleMessage(Message msg) {
                ImageView imageViewByTag = (ImageView) ListMusicView.this.mList.findViewWithTag(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id));
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                    mArtCache.put(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id), ((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                }
            }
        };
        this.mStatusListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(MusicUtils.META_CHANGED) || action.equals(MusicUtils.PLAYSTATE_CHANGED) || action.equals(MusicUtils.PREPARE_COMPLETE_NEW_SONG_PLAY)) {
                    ListMusicView.this.updateUI();
                } else if (intent.getAction().equals(MusicUtils.QUEUE_CHANGED)) {
                    if (ListMusicView.this.mAdapter != null) {
                        ListMusicView.this.mAdapter.changeCursor(new NowPlayingCursor(MusicUtils.sService, ListMusicView.this.mCursorCols));
                    }
                } else if (action.equals(MusicUtils.PLAYBACK_COMPLETE)) {
                    ListMusicView.this.mList.invalidateViews();
                }
            }
        };
        this.mScanListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String path = intent.getData().getPath();
                if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                    ListMusicView.this.updateTrackCursor();
                }
            }
        };
        init(context);
    }

    public ListMusicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRegist = false;
        this.mSeeking = false;
        this.mPlayingItem = new PlayingItem();
        this.mDevice = -1;
        this.mUnmountReceiver = null;
        this.mSetImageHandler = new Handler() {
            public void handleMessage(Message msg) {
                ImageView imageViewByTag = (ImageView) ListMusicView.this.mList.findViewWithTag(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id));
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                    mArtCache.put(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id), ((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                }
            }
        };
        this.mStatusListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(MusicUtils.META_CHANGED) || action.equals(MusicUtils.PLAYSTATE_CHANGED) || action.equals(MusicUtils.PREPARE_COMPLETE_NEW_SONG_PLAY)) {
                    ListMusicView.this.updateUI();
                } else if (intent.getAction().equals(MusicUtils.QUEUE_CHANGED)) {
                    if (ListMusicView.this.mAdapter != null) {
                        ListMusicView.this.mAdapter.changeCursor(new NowPlayingCursor(MusicUtils.sService, ListMusicView.this.mCursorCols));
                    }
                } else if (action.equals(MusicUtils.PLAYBACK_COMPLETE)) {
                    ListMusicView.this.mList.invalidateViews();
                }
            }
        };
        this.mScanListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String path = intent.getData().getPath();
                if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                    ListMusicView.this.updateTrackCursor();
                }
            }
        };
        init(context);
    }

    public ListMusicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mRegist = false;
        this.mSeeking = false;
        this.mPlayingItem = new PlayingItem();
        this.mDevice = -1;
        this.mUnmountReceiver = null;
        this.mSetImageHandler = new Handler() {
            public void handleMessage(Message msg) {
                ImageView imageViewByTag = (ImageView) ListMusicView.this.mList.findViewWithTag(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id));
                if (imageViewByTag != null) {
                    imageViewByTag.setImageDrawable(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                    mArtCache.put(Long.valueOf(((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).audio_id), ((com.wmt.MusicPlayer.ListMusicView.Msg) msg.obj).drawable);
                }
            }
        };
        this.mStatusListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(MusicUtils.META_CHANGED) || action.equals(MusicUtils.PLAYSTATE_CHANGED) || action.equals(MusicUtils.PREPARE_COMPLETE_NEW_SONG_PLAY)) {
                    ListMusicView.this.updateUI();
                } else if (intent.getAction().equals(MusicUtils.QUEUE_CHANGED)) {
                    if (ListMusicView.this.mAdapter != null) {
                        ListMusicView.this.mAdapter.changeCursor(new NowPlayingCursor(MusicUtils.sService, ListMusicView.this.mCursorCols));
                    }
                } else if (action.equals(MusicUtils.PLAYBACK_COMPLETE)) {
                    ListMusicView.this.mList.invalidateViews();
                }
            }
        };
        this.mScanListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String path = intent.getData().getPath();
                if (action.equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
                    ListMusicView.this.updateTrackCursor();
                }
            }
        };
        init(context);
    }

    private void bmpRecycle() {
        System.gc();
    }

    private void deinit() {
        this.mLoadThread.exit();
        this.mProgressRefresher.removeCallbacksAndMessages(null);
        this.mList.setAdapter(null);
        this.paused = true;
        mArtCache.clear();
        if (this.mRegist) {
            this.mContext.unregisterReceiver(this.mStatusListener);
            this.mContext.unregisterReceiver(this.mScanListener);
        }
        if (this.mUnmountReceiver != null) {
            this.mContext.unregisterReceiver(this.mUnmountReceiver);
            this.mUnmountReceiver = null;
        }
        MusicUtils.unbindFromService(this.mServiceToken);
        bmpRecycle();
    }

    private final View findViewByNameId(Resources res, View convertView, String name) {
        return convertView.findViewById(res.getIdentifier(name, "id", pn));
    }

    private void getResource() {
        try {
            Resources res = this.mContext.getPackageManager().getResourcesForApplication(pn);
            int identifier = res.getIdentifier("widget_musicplayicon", "drawable", pn);
            this.mButtonPlay = res.getIdentifier("widget_buttonplay", "drawable", pn);
            this.mButtonPause = res.getIdentifier("widget_buttonpause", "drawable", pn);
            this.mButtonPlayPress = res.getIdentifier("widget_buttonplaypress", "drawable", pn);
            this.mButtonPausePress = res.getIdentifier("widget_buttonpausepress", "drawable", pn);
            identifier = res.getIdentifier("widget_buttonpausepress", "drawable", pn);
            this.mDefaultSmallAlbumIcon = new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeResource(res, res.getIdentifier("widget_album_unknown_cover_small", "drawable", pn)));
            this.mDefaultSmallAlbumIcon.setFilterBitmap(false);
            this.mDefaultSmallAlbumIcon.setDither(false);
            this.mDefaultBigAlbumIcon = new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeResource(res, res.getIdentifier("widget_album_unknown_cover_big", "drawable", pn)));
            this.mDefaultBigAlbumIcon.setFilterBitmap(false);
            this.mDefaultBigAlbumIcon.setDither(false);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        int i = 1;
        this.mServiceToken = MusicUtils.bindToService(this.mContext, this);
        getResource();
        if (this.mAdapter != null) {
            this.mList.setAdapter(this.mAdapter);
        }
        this.mLoadThread = new ImageLoadThread() {
            protected void loadThumbBitmap(Object object) {
                com.wmt.MusicPlayer.ListMusicView.Msg msg = new com.wmt.MusicPlayer.ListMusicView.Msg();
                msg.drawable = MusicUtils.getNoCachedArtwork(ListMusicView.this.mContext, ((Long) object).longValue(), ListMusicView.this.mDevice, ListMusicView.this.mDefaultSmallAlbumIcon);
                msg.audio_id = ((Long) object).longValue();
                ListMusicView.this.mSetImageHandler.sendMessage(ListMusicView.this.mSetImageHandler.obtainMessage(0, msg));
            }
        };
        ImageLoadThread imageLoadThread = this.mLoadThread;
        if (this.mLoadThread.getPriority() - 1 >= 1) {
            i = this.mLoadThread.getPriority() - 1;
        }
        imageLoadThread.setPriority(i);
        this.mLoadThread.start();
        this.paused = false;
    }

    private void init(Context context) {
        this.mContext = context;
        setFocusable(true);
        setClickable(true);
        this.mCursorCols = new String[]{"_id", "title", "_data", "album", "artist", "artist_id", "duration"};
        try {
            Resources res = context.getPackageManager().getResourcesForApplication(pn);
            this.mLayoutListId = res.getIdentifier("musicwidget_list", "layout", pn);
            this.mLayoutNormalItemId = res.getIdentifier("musicwidget_item_normal", "layout", pn);
            this.mLayoutClickItemId = res.getIdentifier("musicwidget_item_clicked", "layout", pn);
            this.mUnknownArtist = context.getString(res.getIdentifier("unknown_artist_name", "string", pn));
            this.mUnknownAlbum = context.getString(res.getIdentifier("unknown_album_name", "string", pn));
            View convertView = View.inflate(context, this.mLayoutListId, null);
            this.mList = (ListView) findViewByNameId(res, convertView, "music_list");
            this.mProgress = (ProgressBar) findViewByNameId(res, convertView, "loading");
            this.mEmpty = (TextView) findViewByNameId(res, convertView, "empty");
            this.mTopShadowRepeat = findViewByNameId(res, convertView, "top_shadow");
            this.mMusicAppIcon = (ImageView) findViewByNameId(res, convertView, "app_shortcut");
            this.mMusicAppIcon.setImageDrawable(Utils.getApkIcon(context, pn));
            this.mMusicAppIcon.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ListMusicView.this.startMusicActivity(false);
                }
            });
            this.mEmpty.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ListMusicView.this.startMusicActivity(false);
                }
            });
            this.mList.setOnItemClickListener(this);
            this.mList.setVerticalFadingEdgeEnabled(false);
            this.mList.setDivider(null);
            addView(convertView);
            this.mProgressRefresher = new Handler();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startMusicActivity(boolean withNotify) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(pn, "com.wmt.music.MusicMainActivity");
        intent.addFlags(268435456);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        if (withNotify) {
            intent.setAction(MusicUtils.NOTIFY_ACTION);
        }
        this.mContext.startActivity(intent);
    }

    private void updatePlayPauseButton() {
        if (this.mImageButton != null) {
            if (MusicUtils.isPlaying()) {
                this.mImageButton.setBackgroundResource(this.mButtonPause);
            } else {
                this.mImageButton.setBackgroundResource(this.mButtonPlay);
            }
        }
    }

    private Cursor updateTrackCursor() {
        Cursor ret = null;
        new StringBuilder().append("title != ''");
        if (MusicUtils.sService != null) {
            ret = new NowPlayingCursor(MusicUtils.sService, this.mCursorCols);
            this.mAdapter.changeCursor(ret);
            try {
                this.mList.setSelection(MusicUtils.sService.getQueuePosition());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (ret.getCount() == 0) {
                Log.v(TAG, "Count == 0");
            }
        }
        if (ret != null) {
            this.mAdapter.changeCursor(ret);
        }
        return ret;
    }

    private void updateUI() {
        try {
            if (!(this.mAdapter == null || MusicUtils.sService == null || !MusicUtils.sService.isInitialized())) {
                this.mProgressRefresher.postDelayed(new ProgressRefresher(), 200);
                this.mAdapter.queueNextRefresh(1);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mList.invalidateViews();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        deinit();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicUtils.playAll(this.mContext, this.mTrackCursor, position, this.mDevice);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        int h = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        LayoutParams layoutParams = (LayoutParams) this.mList.getLayoutParams();
        layoutParams.height = heightSize;
        this.mList.setLayoutParams(layoutParams);
        if (this.mLoadThread != null) {
            this.mLoadThread.setQueueSize(this.mList.getChildCount());
        }
        super.onMeasure(w, h);
        setMeasuredDimension(widthSize, heightSize);
    }

    public void onServiceConnected(ComponentName name, IBinder service) {
        IntentFilter f = new IntentFilter();
        f.addAction("android.intent.action.MEDIA_SCANNER_STARTED");
        f.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        f.addAction("android.intent.action.MEDIA_UNMOUNTED");
        f.addDataScheme("file");
        this.mContext.registerReceiver(this.mScanListener, f);
        IntentFilter f2 = new IntentFilter();
        f2.addAction(MusicUtils.PLAYSTATE_CHANGED);
        f2.addAction(MusicUtils.QUEUE_CHANGED);
        f2.addAction(MusicUtils.META_CHANGED);
        f2.addAction(MusicUtils.PREPARE_COMPLETE_NEW_SONG_PLAY);
        f2.addAction(MusicUtils.PLAYBACK_COMPLETE);
        this.mContext.registerReceiver(this.mStatusListener, f2);
        registerExternalStorageListener();
        this.mRegist = true;
        if (this.mAdapter == null) {
            this.mAdapter = new MusicListAdapter(this.mContext, this.mLayoutNormalItemId, this, null, new String[0], new int[0]);
            this.mList.setAdapter(this.mAdapter);
            updateTrackCursor();
        }
    }

    public void onServiceDisconnected(ComponentName name) {
    }

    public void registerExternalStorageListener() {
        if (this.mUnmountReceiver == null) {
            this.mUnmountReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    Uri uri = intent.getData();
                    String path = uri.getPath();
                    if (!uri.getScheme().equals("file")) {
                        return;
                    }
                    if (action.equals("android.intent.action.MEDIA_EJECT")) {
                        ListMusicView.this.mList.invalidate();
                    } else if (action.equals("android.intent.action.MEDIA_MOUNTED")) {
                        if (path.equals(DataManager.UDISK_MOUNT_POINT)) {
                        }
                        ListMusicView.this.mList.invalidate();
                    }
                }
            };
            IntentFilter iFilter = new IntentFilter();
            iFilter.addAction("android.intent.action.MEDIA_EJECT");
            iFilter.addAction("android.intent.action.MEDIA_MOUNTED");
            iFilter.addDataScheme("file");
            this.mContext.registerReceiver(this.mUnmountReceiver, iFilter);
        }
    }
}