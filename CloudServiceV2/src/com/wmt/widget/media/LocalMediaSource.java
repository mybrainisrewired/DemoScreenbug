package com.wmt.widget.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import com.wmt.MusicPlayer.MusicUtils;
import com.wmt.data.ContentListener;
import com.wmt.data.DataManager;
import com.wmt.data.LocalAudioAll;
import com.wmt.data.MediaItem;
import com.wmt.data.Path;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.codehaus.jackson.org.objectweb.asm.ClassWriter;

public class LocalMediaSource implements WidgetSource {
    private static final String[] COUNT_PROJECTION;
    private static final String DATE_TAKEN = "datetaken";
    private static final String ORDER;
    private static final String[] PROJECTION;
    private static final String SELECTION;
    private final Uri CONTENT_URI;
    private ContentListener mContentListener;
    private ContentObserver mContentObserver;
    private Context mContext;
    private DataManager mDataManager;
    private Path mLocalMediaRoot;
    private ArrayList<Long> mMedias;

    class AnonymousClass_1 extends ContentObserver {
        AnonymousClass_1(Handler x0) {
            super(x0);
        }

        public void onChange(boolean selfChange) {
            if (LocalMediaSource.this.mContentListener != null) {
                LocalMediaSource.this.mContentListener.onContentDirty();
            }
        }
    }

    static {
        PROJECTION = new String[]{"_id"};
        COUNT_PROJECTION = new String[]{"count(*)"};
        SELECTION = String.format("%s != %s", new Object[]{"bucket_id", Integer.valueOf(getDownloadBucketId())});
        ORDER = String.format("%s DESC", new Object[]{DATE_TAKEN});
    }

    public LocalMediaSource(Context context, int id, int mediaType) {
        this.mMedias = new ArrayList();
        this.mContext = context;
        this.mDataManager = new DataManager(context);
        this.mDataManager.initializeSourceMap();
        this.mContentObserver = new AnonymousClass_1(new Handler());
        this.CONTENT_URI = getDevUri(id, mediaType);
        this.mContext.getContentResolver().registerContentObserver(this.CONTENT_URI, true, this.mContentObserver);
    }

    private Uri getDevUri(int id, int mediaType) {
        String devStr;
        switch (id) {
            case LocalAudioAll.SORT_BY_TITLE:
                devStr = MusicUtils.LOCAL_VOLUME;
                this.mLocalMediaRoot = Path.fromString(mediaType == 1 ? "/local/image/item" : "/local/video/item");
                break;
            case LocalAudioAll.SORT_BY_DATE:
                devStr = MusicUtils.EXTERNAL_VOLUME;
                this.mLocalMediaRoot = Path.fromString(mediaType == 1 ? "/sd/image/item" : "/sd/video/item");
                break;
            case ClassWriter.COMPUTE_FRAMES:
                devStr = MusicUtils.UDISK_VOLUME;
                this.mLocalMediaRoot = Path.fromString(mediaType == 1 ? "/usb/image/item" : "/usb/video/item");
                break;
            default:
                devStr = MusicUtils.LOCAL_VOLUME;
                this.mLocalMediaRoot = Path.fromString(mediaType == 1 ? "/local/image/item" : "/local/video/item");
                break;
        }
        return mediaType == 1 ? Media.getContentUri(devStr) : Video.Media.getContentUri(devStr);
    }

    private static int getDownloadBucketId() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath().toLowerCase().hashCode();
    }

    private int[] getExponentialIndice(int total) {
        Random random = new Random();
        HashSet<Integer> selected = new HashSet(total);
        while (selected.size() < total) {
            int row = (int) (((-Math.log(random.nextDouble())) * ((double) total)) / 2.0d);
            if (row < total) {
                selected.add(Integer.valueOf(row));
            }
        }
        int[] values = new int[total];
        int index = 0;
        Iterator i$ = selected.iterator();
        while (i$.hasNext()) {
            int index2 = index + 1;
            values[index] = ((Integer) i$.next()).intValue();
            index = index2;
        }
        return values;
    }

    private int getMediaCount(ContentResolver resolver) {
        Cursor cursor = resolver.query(this.CONTENT_URI, COUNT_PROJECTION, SELECTION, null, null);
        if (cursor == null) {
            return 0;
        }
        Utils.assertTrue(cursor.moveToNext());
        int i = cursor.getInt(0);
        cursor.close();
        return i;
    }

    public void close() {
        this.mContext.getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    public Uri getContentUri(int index) {
        return index < this.mMedias.size() ? this.CONTENT_URI.buildUpon().appendPath(String.valueOf(this.mMedias.get(index))).build() : null;
    }

    public MediaItem getMediaItem(int index) {
        if (index >= this.mMedias.size()) {
            return null;
        }
        return (MediaItem) this.mDataManager.getMediaObject(this.mLocalMediaRoot.getChild(((Long) this.mMedias.get(index)).longValue()));
    }

    public synchronized ArrayList<String> getPlayList() {
        ArrayList<String> playList;
        playList = new ArrayList();
        int size = size();
        if (size > 0) {
            int i = 0;
            while (i < size) {
                playList.add(getContentUri(i).toString());
                i++;
            }
        }
        return playList;
    }

    public void reload() {
        int[] choosedIds = getExponentialIndice(getMediaCount(this.mContext.getContentResolver()));
        Arrays.sort(choosedIds);
        this.mMedias.clear();
        Cursor cursor = this.mContext.getContentResolver().query(this.CONTENT_URI, PROJECTION, SELECTION, null, ORDER);
        if (cursor != null) {
            int[] arr$ = choosedIds;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                if (cursor.moveToPosition(arr$[i$])) {
                    this.mMedias.add(Long.valueOf(cursor.getLong(0)));
                }
                i$++;
            }
            cursor.close();
        }
    }

    public void setContentListener(ContentListener listener) {
        this.mContentListener = listener;
    }

    public int size() {
        return this.mMedias.size();
    }
}