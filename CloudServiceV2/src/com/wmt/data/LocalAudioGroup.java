package com.wmt.data;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.wmt.util.Utils;
import java.util.ArrayList;

public abstract class LocalAudioGroup extends MediaSet {
    protected static final String[] COUNT_PROJECTION;
    private static final String TAG = "LocalAudioGroup";
    protected final Uri mBaseUri;
    protected int mCachedCount;
    protected final DataManager mDataManager;
    protected int mFilterSize;
    protected final int mId;
    protected final Path mItemPath;
    protected final ChangeNotifier mNotifier;
    protected String mOrderByField;

    static {
        COUNT_PROJECTION = new String[]{"count(*)"};
    }

    public LocalAudioGroup(Path path, DataManager dataManager, int id) {
        super(path, nextVersionNumber());
        this.mCachedCount = -1;
        this.mFilterSize = -1;
        this.mOrderByField = null;
        this.mItemPath = path;
        this.mId = id;
        this.mBaseUri = getBaseUri(path, id);
        this.mDataManager = dataManager;
        this.mNotifier = new ChangeNotifier(this, this.mBaseUri, dataManager);
    }

    private void loadIfNecessary() {
        if (this.mNotifier.isDirty()) {
            this.mDataVersion = nextVersionNumber();
            this.mCachedCount = -1;
        }
    }

    private ArrayList<MediaItem> loadMediaItemFromDB(int start, int count) {
        loadIfNecessary();
        ArrayList<MediaItem> list = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = getQueryCursor(start, count);
            if (cursor == null) {
                Log.w(TAG, "query fail");
                if (cursor != null) {
                    cursor.close();
                }
                return list;
            }
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                list.add(loadOrUpdateItem(this.mItemPath.getChild(id), cursor, id));
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

    protected abstract Uri getBaseUri(Path path, int i);

    protected abstract ArrayList<MediaItem> getCachedList();

    protected abstract Cursor getCountCursor();

    public int getID() {
        return this.mId;
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        ArrayList<MediaItem> cachedList = getCachedList();
        if (cachedList == null || cachedList == null) {
            return loadMediaItemFromDB(start, count);
        }
        ArrayList<MediaItem> list = new ArrayList();
        if (cachedList.size() <= start) {
            return list;
        }
        int end = start + count;
        if (cachedList.size() <= end) {
            end = cachedList.size() - 1;
        }
        int i = start;
        while (i <= end) {
            if (((MediaItem) cachedList.get(i)).getSize() > ((long) this.mFilterSize)) {
                list.add(cachedList.get(i));
            }
            i++;
        }
        return list;
    }

    public int getMediaItemCount() {
        loadIfNecessary();
        if (this.mCachedCount == -1) {
            Cursor cursor = getCountCursor();
            if (cursor == null) {
                Log.w(TAG, "query fail");
                return 0;
            } else {
                Utils.assertTrue(cursor.moveToNext());
                this.mCachedCount = cursor.getInt(0);
                cursor.close();
            }
        }
        return this.mCachedCount;
    }

    protected abstract Cursor getQueryCursor(int i, int i2);

    protected abstract MediaItem loadOrUpdateItem(Path path, Cursor cursor, int i);

    public long reload() {
        loadIfNecessary();
        return this.mDataVersion;
    }

    public void setFilterSize(int sizeInByte) {
        if (this.mFilterSize != sizeInByte) {
            this.mFilterSize = sizeInByte;
            this.mCachedCount = -1;
        }
    }

    public void setMediaItemCount(int count) {
        this.mCachedCount = count;
    }

    public void setOrderBy(String filedName) {
        this.mOrderByField = filedName;
    }
}