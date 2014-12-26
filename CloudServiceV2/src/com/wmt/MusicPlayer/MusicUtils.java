package com.wmt.MusicPlayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.provider.MediaStore.Audio.Playlists.Members;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.wmt.MusicPlayer.IMediaPlaybackService.Stub;
import com.wmt.data.DataManager;
import com.wmt.data.MediaItem;
import com.wmt.media.ImageMan;
import com.wmt.util.LruCache;
import com.wmt.util.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import org.codehaus.jackson.impl.JsonWriteContext;
import org.codehaus.jackson.io.CharacterEscapes;

public class MusicUtils {
    private static final int CACHE_CAPACITY = 32;
    public static final String EXTERNAL_VOLUME = "external";
    private static final int INDEX_THUMB_DATA = 0;
    public static final String INTERNAL_VOLUME = "internal";
    public static final int LAST = 3;
    public static final String LOCAL_VOLUME = "local";
    public static final String META_CHANGED = "com.wmt.music.metachanged";
    public static final int NEXT = 2;
    public static final String NOTIFY_ACTION = "MyMusic3DActivity_NOTIFY_ACTION";
    public static final int NOW = 1;
    public static final int PLAYBACKSERVICE_STATUS = 1;
    public static final String PLAYBACK_COMPLETE = "com.wmt.music.playbackcomplete";
    public static final String PLAYFILE_ERROR = "com.wmt.music.playfile_error";
    public static final String PLAYFILE_PREPARE_ERROR = "com.wmt.music.prepare_error";
    public static final String PLAYFILE_UNEXIST = "com.wmt.music.playfile_unexit";
    public static final String PLAYSTATE_CHANGED = "com.wmt.music.playstatechanged";
    public static final String PREPARE_COMPLETE_NEW_SONG_PLAY = "com.wmt.music.preparecompletenewsongstartplaying";
    public static final String QUEUE_CHANGED = "com.wmt.music.queuechanged";
    public static final int REPEAT_ALL = 2;
    public static final int REPEAT_CURRENT = 1;
    public static final int REPEAT_NONE = 0;
    public static final String SERVICE_BIND = "com.wmt.music.sServiceBind";
    public static final int SHUFFLE_AUTO = 2;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
    public static final int STORAGE_DEVICE_LOCAL = 0;
    public static final int STORAGE_DEVICE_SD = 1;
    public static final int STORAGE_DEVICE_USB = 2;
    private static final String TAG = "MusicUtils";
    public static final String[] THUMBDATA_PROJECTION;
    public static final String THUMBDATA_WHERE_CLAUSE = "_id=?";
    public static final String UDISK_VOLUME = "udisk";
    private static final int decodeFlags = 278;
    private static Bitmap mCachedBit = null;
    private static int mDurationFormatLongId = 0;
    private static int mDurationFormatShortId = 0;
    private static String mLastSdStatus = null;
    public static int mNNNtracksDelectedId = 0;
    private static int mNNNtrackstoplaylistId = 0;
    public static boolean mPlayOnce = false;
    private static int mStringCurrentPlaylistId = 0;
    private static int mStringFavId = 0;
    private static int mStringNewPlaylistId = 0;
    private static final String pn = "com.wmt.music";
    static int sActiveTabIndex;
    private static LruCache<Long, Drawable> sArtCache;
    private static int sArtCacheId;
    private static int sArtId;
    private static final Options sBitmapOptions;
    private static final Options sBitmapOptionsCache;
    private static HashMap<Context, ServiceBinder> sConnectionMap;
    private static ContentValues[] sContentValuesCache;
    private static final long[] sEmptyList;
    private static final String sExternalMediaUri;
    private static StringBuilder sFormatBuilder;
    private static Formatter sFormatter;
    private static int sLogPtr;
    private static LogEntry[] sMusicLog;
    public static IMediaPlaybackService sService;
    private static Time sTime;
    private static final Object[] sTimeArgs;

    static class AnonymousClass_1 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$message;

        AnonymousClass_1(Context context, String str) {
            this.val$context = context;
            this.val$message = str;
        }

        public void run() {
            Toast.makeText(this.val$context, this.val$message, STORAGE_DEVICE_LOCAL).show();
        }
    }

    public static interface Defs {
        public static final int ADD_TO_PLAYLIST = 1;
        public static final int CHILD_MENU_BASE = 14;
        public static final int DELETE_ITEM = 10;
        public static final int EFFECTS_PANEL = 13;
        public static final int GOTO_PLAYBACK = 7;
        public static final int GOTO_START = 6;
        public static final int NEW_PLAYLIST = 4;
        public static final int OPEN_URL = 0;
        public static final int PARTY_SHUFFLE = 8;
        public static final int PLAYLIST_SELECTED = 3;
        public static final int PLAY_SELECTION = 5;
        public static final int QUEUE = 12;
        public static final int SCAN_DONE = 11;
        public static final int SHUFFLE_ALL = 9;
        public static final int USE_AS_RINGTONE = 2;
    }

    public static class FastBitmapDrawable extends Drawable {
        private Bitmap mBitmap;
        private int mHeight;
        private int mWidth;

        public FastBitmapDrawable(Bitmap b) {
            this.mBitmap = b;
            if (b != null) {
                this.mWidth = this.mBitmap.getWidth();
                this.mHeight = this.mBitmap.getHeight();
            } else {
                this.mHeight = 0;
                this.mWidth = 0;
            }
        }

        public void draw(Canvas canvas) {
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, null);
        }

        public int getIntrinsicHeight() {
            return this.mHeight;
        }

        public int getIntrinsicWidth() {
            return this.mWidth;
        }

        public int getOpacity() {
            return -1;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    public static class LogEntry {
        Object item;
        long time;

        LogEntry(Object o) {
            this.item = o;
            this.time = System.currentTimeMillis();
        }

        void dump(PrintWriter out) {
            sTime.set(this.time);
            out.print(sTime.toString() + " : ");
            if (this.item instanceof Exception) {
                ((Exception) this.item).printStackTrace(out);
            } else {
                out.println(this.item);
            }
        }
    }

    private static class ServiceBinder implements ServiceConnection {
        ServiceConnection mCallback;

        ServiceBinder(ServiceConnection callback) {
            this.mCallback = callback;
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            sService = Stub.asInterface(service);
            MusicUtils.initAlbumArtCache();
            if (this.mCallback != null) {
                this.mCallback.onServiceConnected(className, service);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (this.mCallback != null) {
                this.mCallback.onServiceDisconnected(className);
            }
            sService = null;
        }
    }

    public static class ServiceToken {
        ContextWrapper mWrappedContext;

        ServiceToken(ContextWrapper context) {
            this.mWrappedContext = context;
        }
    }

    static {
        mPlayOnce = false;
        sService = null;
        sConnectionMap = new HashMap();
        sEmptyList = new long[0];
        mStringFavId = -1;
        mNNNtracksDelectedId = -1;
        sContentValuesCache = null;
        mNNNtrackstoplaylistId = -1;
        sFormatBuilder = new StringBuilder();
        sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());
        sTimeArgs = new Object[5];
        mDurationFormatShortId = -1;
        mDurationFormatLongId = -1;
        mStringCurrentPlaylistId = -1;
        mStringNewPlaylistId = -1;
        sArtId = -2;
        mCachedBit = null;
        sBitmapOptionsCache = new Options();
        sBitmapOptions = new Options();
        sArtCache = new LruCache(32);
        sArtCacheId = -1;
        sBitmapOptionsCache.inPreferredConfig = Config.RGB_565;
        sBitmapOptionsCache.inDither = false;
        sBitmapOptions.inPreferredConfig = Config.RGB_565;
        sBitmapOptions.inDither = false;
        THUMBDATA_PROJECTION = new String[]{"thumbdata"};
        sExternalMediaUri = Media.EXTERNAL_CONTENT_URI.toString();
        sActiveTabIndex = -1;
        sMusicLog = new LogEntry[100];
        sLogPtr = 0;
        sTime = new Time();
    }

    public static void addToCurrentPlaylist(Context context, long[] list) {
        if (sService != null) {
            try {
                sService.enqueue(list, LAST);
                if (mNNNtrackstoplaylistId == -1) {
                    Log.v(TAG, "mNNNtrackstoplaylistId haven't init");
                }
                Toast.makeText(context, context.getResources().getQuantityString(mNNNtrackstoplaylistId, list.length, new Object[]{Integer.valueOf(list.length)}), STORAGE_DEVICE_LOCAL).show();
            } catch (RemoteException e) {
            }
        }
    }

    public static void addToPlaylist(Context context, long[] ids, long playlistid, int devid) {
        if (ids == null) {
            Log.e("MusicBase", "ListSelection null");
        } else {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = Members.getContentUri(getDeviceString(devid), playlistid);
            String[] idsString = new String[ids.length];
            int i = STORAGE_DEVICE_LOCAL;
            while (i < ids.length) {
                idsString[i] = String.valueOf(ids[i]);
                i++;
            }
            Cursor c = resolver.query(uri, new String[]{"audio_id"}, null, null, "audio_id");
            if (c != null) {
                int len = c.getCount();
                if (len != 0) {
                    long[] idsExist = new long[len];
                    i = STORAGE_DEVICE_LOCAL;
                    while (i < len) {
                        c.moveToNext();
                        idsExist[i] = c.getLong(STORAGE_DEVICE_LOCAL);
                        i++;
                    }
                    c.close();
                    boolean[] idsIsUniqueFlag = new boolean[ids.length];
                    int numUnique = STORAGE_DEVICE_LOCAL;
                    i = STORAGE_DEVICE_LOCAL;
                    while (i < ids.length) {
                        if (Arrays.binarySearch(idsExist, ids[i]) < 0) {
                            idsIsUniqueFlag[i] = true;
                            numUnique++;
                        } else {
                            idsIsUniqueFlag[i] = false;
                        }
                        i++;
                    }
                    long[] idsUnique = new long[numUnique];
                    int num = STORAGE_DEVICE_LOCAL;
                    i = STORAGE_DEVICE_LOCAL;
                    while (i < ids.length) {
                        if (idsIsUniqueFlag[i]) {
                            int num2 = num + 1;
                            idsUnique[num] = ids[i];
                            num = num2;
                        }
                        i++;
                    }
                    ids = idsUnique;
                } else {
                    c.close();
                }
            }
            int size = ids.length;
            if (size != 0) {
                Cursor cur = resolver.query(uri, new String[]{"count(*)"}, null, null, null);
                cur.moveToFirst();
                int base = cur.getInt(STORAGE_DEVICE_LOCAL);
                cur.close();
                int numinserted = STORAGE_DEVICE_LOCAL;
                i = STORAGE_DEVICE_LOCAL;
                while (i < size) {
                    makeInsertItems(ids, i, 1000, base);
                    numinserted += resolver.bulkInsert(uri, sContentValuesCache);
                    i += 1000;
                }
                if (mNNNtrackstoplaylistId == -1) {
                    Log.v(TAG, "mNNNtrackstoplaylistId haven't init");
                }
                Toast.makeText(context, context.getResources().getQuantityString(mNNNtrackstoplaylistId, numinserted, new Object[]{Integer.valueOf(numinserted)}), STORAGE_DEVICE_LOCAL).show();
            }
        }
    }

    public static ServiceToken bindToService(Activity context) {
        return bindToService(context, null);
    }

    public static ServiceToken bindToService(Context context, ServiceConnection callback) {
        ContextWrapper cw = new ContextWrapper(context);
        Intent ServiceIntent = new Intent();
        ServiceIntent.setClassName(pn, "com.wmt.music.MediaPlaybackService");
        cw.startService(ServiceIntent);
        ServiceBinder sb = new ServiceBinder(callback);
        initResourceId(context);
        if (cw.bindService(ServiceIntent, sb, STORAGE_DEVICE_LOCAL)) {
            sConnectionMap.put(cw, sb);
            return new ServiceToken(cw);
        } else {
            Log.e("Music", "Failed to bind to service");
            return null;
        }
    }

    public static void clearAlbumArtCache() {
        synchronized (sArtCache) {
            sArtCache.clear();
        }
    }

    public static void clearPlaylist(Context context, int plid, int dev_id) {
        context.getContentResolver().delete(Members.getContentUri(getDeviceString(dev_id), (long) plid), null, null);
    }

    public static void debugDump(PrintWriter out) {
        int i = STORAGE_DEVICE_LOCAL;
        while (i < sMusicLog.length) {
            int idx = sLogPtr + i;
            if (idx >= sMusicLog.length) {
                idx -= sMusicLog.length;
            }
            LogEntry entry = sMusicLog[idx];
            if (entry != null) {
                entry.dump(out);
            }
            i++;
        }
    }

    public static void debugLog(Object o) {
        sMusicLog[sLogPtr] = new LogEntry(o);
        sLogPtr++;
        if (sLogPtr >= sMusicLog.length) {
            sLogPtr = 0;
        }
    }

    public static void deleteTracks(Context context, ArrayList<MediaItem> items, int devId, Handler handler) {
        int i;
        int deleteSize = STORAGE_DEVICE_LOCAL;
        try {
            if (sService.getDeviceId() == devId) {
                long[] removeItems = new long[items.size()];
                i = STORAGE_DEVICE_LOCAL;
                while (i < items.size()) {
                    removeItems[i] = (long) ((MediaItem) items.get(i)).getID();
                    i++;
                }
                sService.removeNowPlayTracks(removeItems);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        i = STORAGE_DEVICE_LOCAL;
        while (i < items.size()) {
            if (new File(((MediaItem) items.get(i)).getFilePath()).delete()) {
                deleteSize++;
                Log.e(TAG, "delete file " + ((MediaItem) items.get(i)).getFilePath());
            } else {
                Log.e(TAG, "Failed to delete file " + ((MediaItem) items.get(i)).getFilePath());
            }
            i++;
        }
        handler.post(new AnonymousClass_1(context, context.getResources().getQuantityString(mNNNtracksDelectedId, deleteSize, new Object[]{Integer.valueOf(deleteSize)})));
    }

    public static long[] getAllSongs(Context context, int dev_id) {
        Context context2 = context;
        Cursor c = query(context2, getDeviceUri(dev_id), new String[]{"_id"}, "is_music=1", null, null);
        if (c != null) {
            try {
                if (c.getCount() != 0) {
                    int len = c.getCount();
                    long[] list = new long[len];
                    int i = STORAGE_DEVICE_LOCAL;
                    while (i < len) {
                        c.moveToNext();
                        list[i] = c.getLong(STORAGE_DEVICE_LOCAL);
                        i++;
                    }
                    if (c == null) {
                        return list;
                    }
                    c.close();
                    return list;
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        }
        if (c != null) {
            c.close();
        }
        return null;
    }

    private static Bitmap getArtworkQuick(Context context, long album_id, int devid, int w, int h) {
        w--;
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/" + getDeviceString(devid) + "/audio/albumart"), album_id);
        if (uri != null) {
            ParcelFileDescriptor fd = null;
            try {
                fd = res.openFileDescriptor(uri, "r");
                int sampleSize = STORAGE_DEVICE_SD;
                sBitmapOptionsCache.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
                int nextWidth = sBitmapOptionsCache.outWidth >> 1;
                int nextHeight = sBitmapOptionsCache.outHeight >> 1;
                while (nextWidth > w && nextHeight > h) {
                    sampleSize <<= 1;
                    nextWidth >>= 1;
                    nextHeight >>= 1;
                }
                sBitmapOptionsCache.inSampleSize = sampleSize;
                sBitmapOptionsCache.inJustDecodeBounds = false;
                Bitmap b = BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor(), null, sBitmapOptionsCache);
                if (b != null) {
                    if (!(sBitmapOptionsCache.outWidth == w && sBitmapOptionsCache.outHeight == h)) {
                        Bitmap tmp = Bitmap.createScaledBitmap(b, w, h, true);
                        if (tmp != b) {
                            b.recycle();
                        }
                        b = tmp;
                    }
                }
                if (fd == null) {
                    return b;
                }
                try {
                    fd.close();
                    return b;
                } catch (IOException e) {
                    return b;
                }
            } catch (FileNotFoundException e2) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (IOException e4) {
                    }
                }
            }
        }
        return null;
    }

    public static Bitmap getBitmapByAlbum(Context context, long audioid, int devid, int w, int h) {
        if (audioid < 0 || devid < 0) {
            return null;
        }
        byte[] thumb = getThumbData(context, audioid, devid);
        return thumb != null ? ImageMan.loadMem(thumb, STORAGE_DEVICE_LOCAL, thumb.length, w, h, decodeFlags) : null;
    }

    public static Drawable getCachedArtwork(Context context, long artIndex, int dev_id, Drawable defaultArtwork) {
        Drawable d;
        synchronized (sArtCache) {
            d = (Drawable) sArtCache.get(Long.valueOf(artIndex));
        }
        if (d == null) {
            d = defaultArtwork;
            Bitmap icon = defaultArtwork.getBitmap();
            Bitmap b = getCoverFromDB(context, artIndex, dev_id, icon.getWidth(), icon.getHeight());
            if (b != null) {
                d = new FastBitmapDrawable(b);
                synchronized (sArtCache) {
                    Drawable value = (Drawable) sArtCache.get(Long.valueOf(artIndex));
                    if (value == null) {
                        sArtCache.put(Long.valueOf(artIndex), d);
                    } else {
                        d = value;
                    }
                }
            }
        }
        return d;
    }

    public static int getCardId(Context context, int dev_id) {
        Cursor c = context.getContentResolver().query(Uri.parse("content://media/" + getDeviceString(dev_id) + "/fs_id"), null, null, null, null);
        if (c == null) {
            return -1;
        }
        c.moveToFirst();
        int id = c.getInt(STORAGE_DEVICE_LOCAL);
        c.close();
        return id;
    }

    protected static Uri getContentURIForPath(String path) {
        return Uri.fromFile(new File(path));
    }

    private static Bitmap getCoverFromDB(Context context, long audio_id, int dev_id, int w, int h) {
        w--;
        byte[] thumbData = getThumbData(context, audio_id, dev_id);
        return thumbData != null ? ImageMan.loadMem(thumbData, STORAGE_DEVICE_LOCAL, thumbData.length, w, h, decodeFlags) : null;
    }

    public static long getCurrentAlbumId() {
        if (sService != null) {
            try {
                return sService.getAlbumId();
            } catch (RemoteException e) {
            }
        }
        return -1;
    }

    public static long getCurrentArtistId() {
        if (sService != null) {
            try {
                return sService.getArtistId();
            } catch (RemoteException e) {
            }
        }
        return -1;
    }

    public static long getCurrentAudioId() {
        if (sService != null) {
            try {
                return sService.getAudioId();
            } catch (RemoteException e) {
            }
        }
        return -1;
    }

    public static int getCurrentShuffleMode() {
        if (sService == null) {
            return STORAGE_DEVICE_LOCAL;
        }
        try {
            return sService.getShuffleMode();
        } catch (RemoteException e) {
            return STORAGE_DEVICE_LOCAL;
        }
    }

    private static Bitmap getDefaultArtwork(Context context) {
        Options opts = new Options();
        opts.inPreferredConfig = Config.ARGB_8888;
        int id = -1;
        try {
            id = context.getPackageManager().getResourcesForApplication(pn).getIdentifier("albumart_mp_unknown", "drawable", pn);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(context.getResources().openRawResource(id), null, opts);
    }

    public static int getDevId() {
        if (sService != null) {
            try {
                return sService.getDeviceId();
            } catch (RemoteException e) {
            }
        }
        return -1;
    }

    public static boolean getDeviceState(int dev_id) {
        IMountService mountService = IMountService.Stub.asInterface(ServiceManager.getService("mount"));
        String state = null;
        switch (dev_id) {
            case STORAGE_DEVICE_LOCAL:
                return true;
            case STORAGE_DEVICE_SD:
                try {
                    state = mountService.getVolumeState(DataManager.EXTERNAL_MOUNT_POINT);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return state.equals("mounted");
            case STORAGE_DEVICE_USB:
                try {
                    state = mountService.getVolumeState(DataManager.UDISK_MOUNT_POINT);
                } catch (RemoteException e2) {
                    e2.printStackTrace();
                }
                return state.equals("mounted");
            default:
                return false;
        }
    }

    public static String getDeviceString(int dev_id) {
        switch (dev_id) {
            case STORAGE_DEVICE_LOCAL:
                return LOCAL_VOLUME;
            case STORAGE_DEVICE_SD:
                return EXTERNAL_VOLUME;
            case STORAGE_DEVICE_USB:
                return UDISK_VOLUME;
            default:
                return null;
        }
    }

    public static Uri getDeviceUri(int dev_id) {
        switch (dev_id) {
            case STORAGE_DEVICE_LOCAL:
                return Media.getContentUri(LOCAL_VOLUME);
            case STORAGE_DEVICE_SD:
                return Media.getContentUri(EXTERNAL_VOLUME);
            case STORAGE_DEVICE_USB:
                return Media.getContentUri(UDISK_VOLUME);
            default:
                return null;
        }
    }

    public static long getDuration() {
        if (sService != null) {
            try {
                return sService.duration();
            } catch (RemoteException e) {
            }
        }
        return 0;
    }

    static int getIntPref(Context context, String name, int def) {
        return context.getSharedPreferences(context.getPackageName(), STORAGE_DEVICE_LOCAL).getInt(name, def);
    }

    public static Drawable getNoCachedArtwork(Context context, long artIndex, int dev_id, Drawable defaultArtwork) {
        Log.v(TAG, "get No Cached Artwork");
        Drawable d = defaultArtwork;
        Bitmap icon = defaultArtwork.getBitmap();
        Bitmap b = getCoverFromDB(context, artIndex, dev_id, icon.getWidth(), icon.getHeight());
        return b != null ? new FastBitmapDrawable(b) : d;
    }

    public static long getPosition() {
        if (sService != null) {
            try {
                return sService.position();
            } catch (RemoteException e) {
            }
        }
        return -1;
    }

    public static long[] getQueue() {
        if (sService != null) {
            try {
                return sService.getQueue();
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public static long[] getSongListForAlbum(Context context, long id, int dev_id) {
        String[] ccols = new String[]{"_id"};
        String where = "album_id=" + id + " AND " + "is_music" + "=1";
        Cursor cursor = query(context, getDeviceUri(dev_id), ccols, where, null, "track");
        if (cursor == null) {
            return sEmptyList;
        }
        long[] list = getSongListForCursor(cursor);
        cursor.close();
        return list;
    }

    public static long[] getSongListForArtist(Context context, long id, int dev_id) {
        String[] ccols = new String[]{"_id"};
        String where = "artist_id=" + id + " AND " + "is_music" + "=1";
        Cursor cursor = query(context, getDeviceUri(dev_id), ccols, where, null, "album_key,track");
        if (cursor == null) {
            return sEmptyList;
        }
        long[] list = getSongListForCursor(cursor);
        cursor.close();
        return list;
    }

    public static long[] getSongListForCursor(Cursor cursor) {
        if (cursor == null) {
            return sEmptyList;
        }
        int len = cursor.getCount();
        long[] list = new long[len];
        cursor.moveToFirst();
        try {
            int colidx = cursor.getColumnIndexOrThrow("audio_id");
        } catch (IllegalArgumentException e) {
            colidx = cursor.getColumnIndexOrThrow("_id");
        }
        int i = STORAGE_DEVICE_LOCAL;
        while (i < len) {
            list[i] = cursor.getLong(colidx);
            cursor.moveToNext();
            i++;
        }
        return list;
    }

    public static long[] getSongListForPlaylist(Context context, long plid, int dev_id) {
        Context context2 = context;
        Cursor cursor = query(context2, Members.getContentUri(getDeviceString(dev_id), plid), new String[]{"audio_id"}, null, null, "play_order");
        if (cursor == null) {
            return sEmptyList;
        }
        long[] list = getSongListForCursor(cursor);
        cursor.close();
        return list;
    }

    public static byte[] getThumbData(Context context, long audio_id, int dev_id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = getDeviceUri(dev_id);
        String[] selectionArgs = new String[]{String.valueOf(audio_id)};
        if (!getDeviceState(dev_id)) {
            return null;
        }
        Cursor cur = resolver.query(uri, THUMBDATA_PROJECTION, THUMBDATA_WHERE_CLAUSE, selectionArgs, null);
        if (cur == null) {
            return null;
        }
        byte[] data = null;
        try {
            cur.moveToFirst();
            int count = cur.getCount();
            if (count > 0) {
                if (count > 1) {
                    Log.e(TAG, count + "copies thumbdata exist for file which rowid is " + audio_id);
                }
                data = cur.getBlob(STORAGE_DEVICE_LOCAL);
            } else {
                Log.e(TAG, "no copy thumbdata exists for " + audio_id);
            }
            cur.close();
            cur.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            cur.close();
            return null;
        }
    }

    public static void initAlbumArtCache() {
        try {
            int id = sService.getMediaMountedCount();
            if (id != sArtCacheId) {
                clearAlbumArtCache();
                sArtCacheId = id;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void initResourceId(Context context) {
        try {
            Resources res = context.getPackageManager().getResourcesForApplication(pn);
            mDurationFormatShortId = res.getIdentifier("durationformatshort", "string", pn);
            mDurationFormatLongId = res.getIdentifier("durationformatlong", "string", pn);
            mNNNtrackstoplaylistId = res.getIdentifier("NNNtrackstoplaylist", "plurals", pn);
            mNNNtracksDelectedId = res.getIdentifier("NNNtracksdeleted", "plurals", pn);
            mStringCurrentPlaylistId = res.getIdentifier("queue", "string", pn);
            mStringNewPlaylistId = res.getIdentifier("new_playlist", "string", pn);
            mStringFavId = res.getIdentifier("favorites", "string", pn);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMediaScannerScanning(Context context) {
        boolean result = false;
        Cursor cursor = query(context, MediaStore.getMediaScannerUri(), new String[]{"volume"}, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                result = EXTERNAL_VOLUME.equals(cursor.getString(STORAGE_DEVICE_LOCAL));
            }
            cursor.close();
        }
        return result;
    }

    public static boolean isMusicLoaded() {
        if (sService == null) {
            return false;
        }
        try {
            return sService.getPath() != null;
        } catch (RemoteException e) {
            return false;
        }
    }

    public static boolean isPlaying() {
        if (sService != null) {
            try {
                return sService.isPlaying();
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    private static void makeInsertItems(long[] ids, int offset, int len, int base) {
        if (offset + len > ids.length) {
            len = ids.length - offset;
        }
        if (sContentValuesCache == null || sContentValuesCache.length != len) {
            sContentValuesCache = new ContentValues[len];
        }
        int i = STORAGE_DEVICE_LOCAL;
        while (i < len) {
            if (sContentValuesCache[i] == null) {
                sContentValuesCache[i] = new ContentValues();
            }
            sContentValuesCache[i].put("play_order", Integer.valueOf(base + offset + i));
            sContentValuesCache[i].put("audio_id", Long.valueOf(ids[offset + i]));
            i++;
        }
    }

    public static void makePlaylistArray(Context context, ArrayList<Long> listId, ArrayList<String> listName, int dev_id) {
        String[] cols = new String[]{"_id", "name"};
        ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            System.out.println("resolver = null");
        } else {
            Cursor cur = resolver.query(Playlists.getContentUri(getDeviceString(dev_id)), cols, "name != ''", null, "name");
            listName.clear();
            listId.clear();
            listName.add(context.getString(mStringNewPlaylistId));
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    String title = cur.getString(STORAGE_DEVICE_SD);
                    if (title.equals("favorites")) {
                        title = context.getResources().getString(mStringFavId);
                    }
                    listName.add(title);
                    listId.add(Long.valueOf(cur.getLong(STORAGE_DEVICE_LOCAL)));
                    cur.moveToNext();
                }
            }
            if (cur != null) {
                cur.close();
            }
        }
    }

    public static String makeTimeString(Context context, long secs) {
        if (mDurationFormatShortId == -1 || mDurationFormatLongId == -1) {
            Log.v(TAG, "some strings' id  haven't init");
        }
        String durationformat = context.getString(secs < 3600 ? mDurationFormatShortId : mDurationFormatLongId);
        sFormatBuilder.setLength(STORAGE_DEVICE_LOCAL);
        Object[] timeArgs = sTimeArgs;
        timeArgs[0] = Long.valueOf(secs / 3600);
        timeArgs[1] = Long.valueOf(secs / 60);
        timeArgs[2] = Long.valueOf((secs / 60) % 60);
        timeArgs[3] = Long.valueOf(secs);
        timeArgs[4] = Long.valueOf(secs % 60);
        return sFormatter.format(durationformat, timeArgs).toString();
    }

    public static void modifyID3(Context context, long songID, int deviceID, String title, String artist) {
        ContentValues values = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        values.put("title", title);
        values.put("artist", artist);
        resolver.update(ContentUris.withAppendedId(getDeviceUri(deviceID), songID), values, null, null);
    }

    public static void pause() {
        try {
            if (sService != null && sService.isPlaying()) {
                sService.pause();
            }
        } catch (RemoteException e) {
        }
    }

    public static void play() {
        try {
            if (sService != null && !sService.isPlaying()) {
                sService.play();
            }
        } catch (RemoteException e) {
        }
    }

    public static void playAll(Context context, Cursor cursor, int devId) {
        playAll(context, cursor, (int)STORAGE_DEVICE_LOCAL, devId, false);
    }

    public static void playAll(Context context, Cursor cursor, int position, int devId) {
        playAll(context, cursor, position, devId, false);
    }

    private static void playAll(Context context, Cursor cursor, int position, int devId, boolean force_shuffle) {
        playAll(context, getSongListForCursor(cursor), position, devId, force_shuffle);
    }

    public static void playAll(Context context, long[] list, int position, int devId) {
        playAll(context, list, position, devId, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void playAll(android.content.Context r10_context, long[] r11_list, int r12_position, int r13_devId, boolean r14_force_shuffle) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.MusicPlayer.MusicUtils.playAll(android.content.Context, long[], int, int, boolean):void");
        /*
        r9 = 0;
        r5 = -1;
        mPlayOnce = r9;
        r6 = "MusicUtils";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "devid = ";
        r7 = r7.append(r8);
        r7 = r7.append(r13);
        r8 = "; position = ";
        r7 = r7.append(r8);
        r7 = r7.append(r12);
        r8 = ";list length = ";
        r7 = r7.append(r8);
        r8 = r11.length;
        r7 = r7.append(r8);
        r7 = r7.toString();
        android.util.Log.v(r6, r7);
        r6 = r11.length;
        if (r6 == 0) goto L_0x0038;
    L_0x0034:
        r6 = sService;
        if (r6 != 0) goto L_0x0049;
    L_0x0038:
        r5 = "MusicUtils";
        r6 = "attempt to play empty song list";
        android.util.Log.d(r5, r6);
        r3 = "Selected playlist is empty.";
        r5 = android.widget.Toast.makeText(r10, r3, r9);
        r5.show();
    L_0x0048:
        return;
    L_0x0049:
        if (r14 == 0) goto L_0x0051;
    L_0x004b:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r7 = 1;
        r6.setShuffleMode(r7);	 Catch:{ RemoteException -> 0x0089 }
    L_0x0051:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r0 = r6.getAudioId();	 Catch:{ RemoteException -> 0x0089 }
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r2 = r6.getQueuePosition();	 Catch:{ RemoteException -> 0x0089 }
        if (r12 == r5) goto L_0x0099;
    L_0x005f:
        if (r2 != r12) goto L_0x0099;
    L_0x0061:
        r6 = r11[r12];	 Catch:{ RemoteException -> 0x0089 }
        r6 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0099;
    L_0x0067:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r4 = r6.getQueue();	 Catch:{ RemoteException -> 0x0089 }
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r6 = r6.getDeviceId();	 Catch:{ RemoteException -> 0x0089 }
        if (r6 != r13) goto L_0x0099;
    L_0x0075:
        r6 = java.util.Arrays.equals(r11, r4);	 Catch:{ RemoteException -> 0x0089 }
        if (r6 == 0) goto L_0x0099;
    L_0x007b:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r6 = r6.isPlaying();	 Catch:{ RemoteException -> 0x0089 }
        if (r6 == 0) goto L_0x008b;
    L_0x0083:
        r5 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r5.pause();	 Catch:{ RemoteException -> 0x0089 }
        goto L_0x0048;
    L_0x0089:
        r5 = move-exception;
        goto L_0x0048;
    L_0x008b:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r6 = r6.isInitialized();	 Catch:{ RemoteException -> 0x0089 }
        if (r6 == 0) goto L_0x0099;
    L_0x0093:
        r5 = sService;	 Catch:{ RemoteException -> 0x0089 }
        r5.play();	 Catch:{ RemoteException -> 0x0089 }
        goto L_0x0048;
    L_0x0099:
        if (r12 >= 0) goto L_0x009c;
    L_0x009b:
        r12 = 0;
    L_0x009c:
        r6 = sService;	 Catch:{ RemoteException -> 0x0089 }
        if (r14 == 0) goto L_0x00a4;
    L_0x00a0:
        r6.openAsync(r11, r5, r13);	 Catch:{ RemoteException -> 0x0089 }
        goto L_0x0048;
    L_0x00a4:
        r5 = r12;
        goto L_0x00a0;
        */
    }

    public static void playPauseClicked(Context context, Cursor cursor, int position, int device) {
        if (sService == null) {
            return;
        }
        if (cursor == null) {
            Log.v(TAG, "cursor is null");
        } else {
            try {
                if (sService.isPlaying()) {
                    sService.pause();
                } else {
                    cursor.moveToPosition(position);
                    if (sService.getAudioId() == cursor.getLong(cursor.getColumnIndexOrThrow("_id")) && sService.isInitialized()) {
                        sService.play();
                    } else {
                        playAll(context, cursor, position, device);
                    }
                }
            } catch (RemoteException e) {
            }
        }
    }

    public static void playPlaylist(Context context, long plid, int dev_id) {
        long[] list = getSongListForPlaylist(context, plid, dev_id);
        if (list != null) {
            playAll(context, list, -1, dev_id, false);
        }
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, STORAGE_DEVICE_LOCAL);
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return cursor;
            }
            if (limit > 0) {
                Log.v("music5", "*****************baseur =" + uri.toString());
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
                Log.v("music5", "*****************baseur =" + uri.toString());
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException e) {
            return cursor;
        }
    }

    public static void seekTo(long position) {
        if (sService != null) {
            try {
                sService.seek(position);
            } catch (RemoteException e) {
            }
        }
    }

    static void setBackground(View v, Bitmap bm) {
        if (bm == null) {
            v.setBackgroundResource(0);
        } else {
            int vwidth = v.getWidth();
            int vheight = v.getHeight();
            int bwidth = bm.getWidth();
            int bheight = bm.getHeight();
            float scale = Math.max(((float) vwidth) / ((float) bwidth), ((float) vheight) / ((float) bheight)) * 1.3f;
            Canvas c = new Canvas(Bitmap.createBitmap(vwidth, vheight, Config.ARGB_8888));
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            ColorMatrix greymatrix = new ColorMatrix();
            greymatrix.setSaturation(0.0f);
            ColorMatrix darkmatrix = new ColorMatrix();
            darkmatrix.setScale(0.3f, 0.3f, 0.3f, 1.0f);
            greymatrix.postConcat(darkmatrix);
            paint.setColorFilter(new ColorMatrixColorFilter(greymatrix));
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) ((-bwidth) / 2), (float) ((-bheight) / 2));
            matrix.postRotate(10.0f);
            matrix.postScale(scale, scale);
            matrix.postTranslate((float) (vwidth / 2), (float) (vheight / 2));
            c.drawBitmap(bm, matrix, paint);
            Drawable bitmapDrawable = drawable;
            v.setBackgroundDrawable(drawable);
            c.setBitmap(Utils.s_nullBitmap);
        }
    }

    static void setIntPref(Context context, String name, int value) {
        Editor ed = context.getSharedPreferences(context.getPackageName(), STORAGE_DEVICE_LOCAL).edit();
        ed.putInt(name, value);
        SharedPreferencesCompat.apply(ed);
    }

    public static void setSpinnerState(Activity a) {
        if (isMediaScannerScanning(a)) {
            a.getWindow().setFeatureInt(JsonWriteContext.STATUS_EXPECT_NAME, -3);
            a.getWindow().setFeatureInt(JsonWriteContext.STATUS_EXPECT_NAME, -1);
        } else {
            a.getWindow().setFeatureInt(JsonWriteContext.STATUS_EXPECT_NAME, CharacterEscapes.ESCAPE_CUSTOM);
        }
    }

    public static void shuffleAll(Context context, Cursor cursor, int devId) {
        playAll(context, cursor, (int)STORAGE_DEVICE_LOCAL, devId, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void togglePartyShuffle() {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.MusicPlayer.MusicUtils.togglePartyShuffle():void");
        /*
        r2 = 2;
        r1 = sService;
        if (r1 == 0) goto L_0x0011;
    L_0x0005:
        r0 = getCurrentShuffleMode();
        if (r0 != r2) goto L_0x0012;
    L_0x000b:
        r1 = sService;	 Catch:{ RemoteException -> 0x0019 }
        r2 = 0;
        r1.setShuffleMode(r2);	 Catch:{ RemoteException -> 0x0019 }
    L_0x0011:
        return;
    L_0x0012:
        r1 = sService;	 Catch:{ RemoteException -> 0x0019 }
        r2 = 2;
        r1.setShuffleMode(r2);	 Catch:{ RemoteException -> 0x0019 }
        goto L_0x0011;
    L_0x0019:
        r1 = move-exception;
        goto L_0x0011;
        */
    }

    public static void unbindFromService(ServiceToken token) {
        if (token == null) {
            Log.e(TAG, "Trying to unbind with null token");
        } else {
            ContextWrapper cw = token.mWrappedContext;
            ServiceBinder sb = (ServiceBinder) sConnectionMap.remove(cw);
            if (sb == null) {
                Log.e(TAG, "Trying to unbind for unknown Context");
            } else {
                cw.unbindService(sb);
                if (sConnectionMap.isEmpty()) {
                    sService = null;
                }
            }
        }
    }
}