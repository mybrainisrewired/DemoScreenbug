package com.wmt.widget.media;

import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import com.wmt.data.ContentListener;
import com.wmt.data.LocalMediaItem;
import com.wmt.data.MediaItem;
import com.wmt.data.MediaSet;
import com.wmt.data.Path;
import com.wmt.remotectrl.KeyTouchInputEvent;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Arrays;

public class MediaSetSource implements WidgetSource, ContentListener {
    private static final int CACHE_SIZE = 32;
    private MediaItem[] mCache;
    private int mCacheEnd;
    private int mCacheStart;
    private ContentListener mContentListener;
    private Path mItemPath;
    private LocalMediaItem mLocalMediaItem;
    private MediaSet mSource;
    private long mSourceVersion;

    public MediaSetSource(MediaSet source, LocalMediaItem localImage, Path itemPath) {
        this.mCache = new MediaItem[32];
        this.mSourceVersion = -1;
        this.mSource = (MediaSet) Utils.checkNotNull(source);
        this.mSource.addContentListener(this);
        this.mLocalMediaItem = localImage;
        this.mItemPath = itemPath;
    }

    private void ensureCacheRange(int index) {
        if (index < this.mCacheStart || index >= this.mCacheEnd) {
            Log.v("MediaSetSource", "cache Missed. ensureCacheRange index = " + index + ", mCacheStart = " + this.mCacheStart + ", mCacheEnd = " + this.mCacheEnd);
            long token = Binder.clearCallingIdentity();
            boolean emptyCache = this.mCacheStart == 0 && this.mCacheEnd == 0;
            int start;
            int readNum;
            if (index < this.mCacheStart) {
                start = index - ((int) Math.floor(31.0d));
                if (start < 0) {
                    start = 0;
                }
                readNum = this.mCacheStart - start;
                if (readNum > 32) {
                    readNum = CACHE_SIZE;
                }
                updateCache(this.mSource.getMediaItem(start, readNum), start, emptyCache);
            } else {
                start = index - ((int) Math.floor(31.0d));
                readNum = CACHE_SIZE;
                if (start < 0) {
                    start = 0;
                }
                if (start < this.mCacheEnd) {
                    readNum = 32 - this.mCacheEnd - start;
                    start = this.mCacheEnd;
                }
                updateCache(this.mSource.getMediaItem(start, readNum), start, emptyCache);
            }
            Binder.restoreCallingIdentity(token);
        }
    }

    private void updateCache(ArrayList<MediaItem> items, int start, boolean fillCache) {
        boolean z;
        boolean z2 = true;
        if (items.size() <= 32) {
            z = true;
        } else {
            z = false;
        }
        Utils.assertTrue(z);
        int end = start + items.size();
        int i;
        if (items.size() == 32 || fillCache) {
            i = 0;
            while (i < items.size()) {
                this.mCache[i] = (MediaItem) items.get(i);
                i++;
            }
            this.mCacheStart = start;
            this.mCacheEnd = end;
        } else if (start < this.mCacheStart) {
            if (end < this.mCacheStart) {
                z2 = false;
            }
            Utils.assertTrue(z2);
            updateSize = this.mCacheStart - start;
            i = KeyTouchInputEvent.EV_MAX;
            while (i >= updateSize) {
                this.mCache[i] = this.mCache[i - updateSize];
                i--;
            }
            i = 0;
            while (i < updateSize) {
                this.mCache[i] = (MediaItem) items.get(i);
                i++;
            }
            this.mCacheStart = start;
            this.mCacheEnd = this.mCacheStart + 32;
        } else if (end > this.mCacheEnd) {
            if (start > this.mCacheEnd) {
                z2 = false;
            }
            Utils.assertTrue(z2);
            updateSize = end - this.mCacheEnd;
            i = updateSize;
            while (i < 32) {
                this.mCache[i - updateSize] = this.mCache[i];
                i++;
            }
            int newStart = 32 - updateSize;
            i = newStart;
            while (i < 32) {
                this.mCache[i] = (MediaItem) items.get(i - newStart);
                i++;
            }
            this.mCacheEnd = end;
            this.mCacheStart = end - 32;
        }
    }

    public void close() {
        this.mSource.removeContentListener(this);
    }

    public synchronized Uri getContentUri(int index) {
        Uri contentUri;
        if (this.mItemPath != null) {
            contentUri = this.mLocalMediaItem.getContentUri();
        } else {
            ensureCacheRange(index);
            if (index < this.mCacheStart || index >= this.mCacheEnd) {
                contentUri = null;
            } else {
                contentUri = this.mCache[index - this.mCacheStart].getContentUri();
            }
        }
        return contentUri;
    }

    public synchronized MediaItem getMediaItem(int index) {
        MediaItem mediaItem;
        if (this.mLocalMediaItem != null) {
            mediaItem = this.mLocalMediaItem;
        } else {
            ensureCacheRange(index);
            if (index < this.mCacheStart || index >= this.mCacheEnd) {
                mediaItem = null;
            } else {
                mediaItem = this.mCache[index - this.mCacheStart];
            }
        }
        return mediaItem;
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

    public void onContentDirty() {
        if (this.mContentListener != null) {
            this.mContentListener.onContentDirty();
        }
    }

    public void reload() {
        long version = this.mSource.reload();
        if (this.mSourceVersion != version) {
            this.mSourceVersion = version;
            this.mCacheStart = 0;
            this.mCacheEnd = 0;
            if (this.mItemPath != null && this.mSource.getIndexOfItem(this.mItemPath, 0) == -1) {
                this.mLocalMediaItem = null;
            }
            Arrays.fill(this.mCache, null);
        }
    }

    public void setContentListener(ContentListener listener) {
        this.mContentListener = listener;
    }

    public int size() {
        if (this.mItemPath != null) {
            return this.mLocalMediaItem != null ? 1 : 0;
        } else {
            long token = Binder.clearCallingIdentity();
            int mediaItemCount = this.mSource.getMediaItemCount();
            Binder.restoreCallingIdentity(token);
            return mediaItemCount;
        }
    }
}