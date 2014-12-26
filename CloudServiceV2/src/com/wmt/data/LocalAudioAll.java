package com.wmt.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import com.wmt.util.Utils;
import java.util.ArrayList;

public class LocalAudioAll extends MediaSet {
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_DEFAULT = -1;
    public static final int SORT_BY_TITLE = 0;
    private static final String TAG = "LocalAudioAll";
    private final Uri mBaseUri;
    private int mCachedCount;
    private final ContentResolver mContentResolver;
    private DataManager mDataManager;
    private long mDate;
    private int mFilterSize;
    private Path mItemPath;
    private final String mName;
    private final ChangeNotifier mNotifier;
    private boolean mReloadRequested;
    private int mSortMode;

    public LocalAudioAll(DataManager dataManager, Path path, ContentResolver resolver, String name, int sortMode) {
        super(path, nextVersionNumber());
        this.mCachedCount = -1;
        this.mFilterSize = 0;
        this.mReloadRequested = false;
        this.mDate = 0;
        this.mName = name;
        this.mBaseUri = Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        this.mContentResolver = resolver;
        this.mItemPath = path;
        this.mNotifier = new ChangeNotifier(this, this.mBaseUri, dataManager);
        this.mDataManager = dataManager;
        this.mSortMode = sortMode;
    }

    public LocalAudioAll(DataManager dataManager, Path path, ContentResolver resolver, String name, long date) {
        super(path, nextVersionNumber());
        this.mCachedCount = -1;
        this.mFilterSize = 0;
        this.mReloadRequested = false;
        this.mDate = 0;
        this.mName = name;
        this.mBaseUri = Media.getContentUri(DataManager.getDeviceString(DataManager.getDeviceFromPath(path)));
        this.mContentResolver = resolver;
        this.mItemPath = path;
        this.mNotifier = new ChangeNotifier(this, this.mBaseUri, dataManager);
        this.mDataManager = dataManager;
        this.mSortMode = -1;
    }

    private void loadIfNecessary() {
        if (this.mNotifier.isDirty() || this.mReloadRequested) {
            this.mDataVersion = nextVersionNumber();
            this.mCachedCount = -1;
            this.mReloadRequested = false;
        }
    }

    private MediaItem loadOrUpdateItem(ContentResolver resolver, Path path, Cursor cursor) {
        LocalMediaItem item = (LocalMediaItem) path.getObject();
        if (item == null) {
            return new LocalAudio(path, resolver, cursor, this.mDataManager.isBigThumbDataEnabled());
        }
        item.updateContent(cursor);
        return item;
    }

    public void delete() {
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        loadIfNecessary();
        Uri uri = this.mBaseUri.buildUpon().appendQueryParameter("limit", start + "," + count).build();
        ArrayList<MediaItem> list = new ArrayList();
        Cursor cursor = null;
        try {
            if (this.mSortMode == 1) {
                cursor = this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "_size>? and date_added>?", new String[]{String.valueOf(this.mFilterSize), String.valueOf(this.mDate)}, "date_added");
            } else if (this.mSortMode == 0) {
                cursor = this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "_size>? and date_added>?", new String[]{String.valueOf(this.mFilterSize), String.valueOf(this.mDate)}, "title_key");
            } else {
                cursor = this.mContentResolver.query(uri, LocalAudio.getProjection(this.mDataManager.isBigThumbDataEnabled()), "_size>? and date_added>?", new String[]{String.valueOf(this.mFilterSize), String.valueOf(this.mDate)}, null);
            }
            if (cursor == null) {
                Log.w(TAG, "query fail: " + uri);
                if (cursor != null) {
                    cursor.close();
                }
                return list;
            }
            while (cursor.moveToNext()) {
                list.add(loadOrUpdateItem(this.mContentResolver, this.mItemPath.getChild(cursor.getInt(SORT_BY_TITLE)), cursor));
            }
            if (cursor != null) {
                cursor.close();
            }
            return list;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public int getMediaItemCount() {
        loadIfNecessary();
        if (this.mCachedCount == -1) {
            Cursor cursor = this.mContentResolver.query(this.mBaseUri, new String[]{"count(*)"}, "_size>? and date_added>?", new String[]{String.valueOf(this.mFilterSize), String.valueOf(this.mDate)}, null);
            if (cursor == null) {
                Log.w(TAG, "query fail");
                return 0;
            } else {
                try {
                    Utils.assertTrue(cursor.moveToNext());
                    this.mCachedCount = cursor.getInt(SORT_BY_TITLE);
                    cursor.close();
                } catch (Exception e) {
                    Exception e2 = e;
                    Log.v(TAG, "----------- exception --------------");
                    e2.printStackTrace();
                    cursor.close();
                }
            }
        }
        return this.mCachedCount;
    }

    public String getName() {
        return this.mName;
    }

    public int getSupportedOperations() {
        return 1029;
    }

    public boolean isLeafAlbum() {
        return true;
    }

    public long reload() {
        loadIfNecessary();
        return this.mDataVersion;
    }

    public void setFilterSize(int sizeInByte) {
        if (this.mFilterSize != sizeInByte) {
            this.mFilterSize = sizeInByte;
            this.mReloadRequested = true;
        }
    }
}