package com.wmt.data;

import android.util.Log;
import com.wmt.data.utils.Future;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.WeakHashMap;

public abstract class MediaSet extends MediaObject {
    private static final Future<Integer> FUTURE_STUB;
    public static final int INDEX_NOT_FOUND = -1;
    protected static final int INVALID_COUNT = -1;
    public static final int MEDIAITEM_BATCH_FETCH_COUNT = 500;
    public static final int SYNC_RESULT_CANCELLED = 1;
    public static final int SYNC_RESULT_ERROR = 2;
    public static final int SYNC_RESULT_SUCCESS = 0;
    public boolean mIsDeleted;
    private WeakHashMap<ContentListener, Object> mListeners;

    public static interface ItemConsumer {
        void consume(int i, MediaItem mediaItem);
    }

    public static interface SyncListener {
        void onSyncDone(MediaSet mediaSet, int i);
    }

    private class MultiSetSyncFuture implements Future<Integer>, com.wmt.data.MediaSet.SyncListener {
        private static final String TAG = "Gallery.MultiSetSync";
        private boolean mIsCancelled;
        private final com.wmt.data.MediaSet.SyncListener mListener;
        private final HashMap<MediaSet, Future<Integer>> mMediaSetMap;
        private int mResult;

        MultiSetSyncFuture(com.wmt.data.MediaSet.SyncListener listener) {
            this.mMediaSetMap = new HashMap();
            this.mIsCancelled = false;
            this.mResult = -1;
            this.mListener = listener;
        }

        public synchronized void cancel() {
            if (!this.mIsCancelled) {
                this.mIsCancelled = true;
                Iterator i$ = this.mMediaSetMap.values().iterator();
                while (i$.hasNext()) {
                    ((Future) i$.next()).cancel();
                }
                this.mMediaSetMap.clear();
                if (this.mResult < 0) {
                    this.mResult = 1;
                }
            }
        }

        public synchronized Integer get() {
            waitDone();
            return Integer.valueOf(this.mResult);
        }

        public synchronized boolean isCancelled() {
            return this.mIsCancelled;
        }

        public synchronized boolean isDone() {
            return this.mMediaSetMap.isEmpty();
        }

        public void onSyncDone(MediaSet mediaSet, int resultCode) {
            com.wmt.data.MediaSet.SyncListener listener = null;
            synchronized (this) {
                if (this.mMediaSetMap.remove(mediaSet) != null) {
                    Log.d(TAG, "onSyncDone: " + Utils.maskDebugInfo(mediaSet.getName()) + " #pending=" + this.mMediaSetMap.size());
                    if (resultCode == 2) {
                        this.mResult = 2;
                    }
                    if (this.mMediaSetMap.isEmpty()) {
                        if (this.mResult < 0) {
                            this.mResult = 0;
                        }
                        notifyAll();
                        listener = this.mListener;
                    }
                }
            }
            if (listener != null) {
                listener.onSyncDone(MediaSet.this, this.mResult);
            }
        }

        synchronized void requestSyncOnEmptySets(MediaSet[] sets) {
            MediaSet[] arr$ = sets;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                MediaSet set = arr$[i$];
                if (set.getMediaItemCount() == 0 && !this.mMediaSetMap.containsKey(set)) {
                    Future<Integer> future = set.requestSync(this);
                    if (!future.isDone()) {
                        this.mMediaSetMap.put(set, future);
                        Log.d(TAG, "  request sync: " + Utils.maskDebugInfo(set.getName()));
                    }
                }
                i$++;
            }
            Log.d(TAG, "requestSyncOnEmptySets actual=" + this.mMediaSetMap.size());
        }

        public synchronized void waitDone() {
            while (!isDone()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Log.d(TAG, "waitDone() interrupted");
                }
            }
        }
    }

    static {
        FUTURE_STUB = new Future<Integer>() {
            public void cancel() {
            }

            public Integer get() {
                return Integer.valueOf(0);
            }

            public boolean isCancelled() {
                return false;
            }

            public boolean isDone() {
                return true;
            }

            public void waitDone() {
            }
        };
    }

    public MediaSet(Path path, long version) {
        super(path, version);
        this.mIsDeleted = false;
        this.mListeners = new WeakHashMap();
    }

    public void addContentListener(ContentListener listener) {
        if (!this.mListeners.containsKey(listener)) {
            this.mListeners.put(listener, null);
        }
    }

    public void addItems(ArrayList<MediaItem> items) {
        throw new UnsupportedOperationException();
    }

    public void delete(int index) {
        throw new UnsupportedOperationException();
    }

    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    public void deleteMediaItems(ArrayList<MediaItem> items) {
        throw new UnsupportedOperationException();
    }

    protected int enumerateMediaItems(ItemConsumer consumer, int startIndex) {
        int total = getMediaItemCount();
        int start = 0;
        while (start < total) {
            int count = Math.min(MEDIAITEM_BATCH_FETCH_COUNT, total - start);
            ArrayList<MediaItem> items = getMediaItem(start, count);
            int i = 0;
            int n = items.size();
            while (i < n) {
                consumer.consume(startIndex + start + i, (MediaItem) items.get(i));
                i++;
            }
            start += count;
        }
        return total;
    }

    public void enumerateMediaItems(ItemConsumer consumer) {
        enumerateMediaItems(consumer, 0);
    }

    protected int enumerateTotalMediaItems(ItemConsumer consumer, int startIndex) {
        int start = 0 + enumerateMediaItems(consumer, startIndex);
        int i = 0;
        while (i < getSubMediaSetCount()) {
            start += getSubMediaSet(i).enumerateTotalMediaItems(consumer, startIndex + start);
            i++;
        }
        return start;
    }

    public void enumerateTotalMediaItems(ItemConsumer consumer) {
        enumerateTotalMediaItems(consumer, 0);
    }

    public MediaDetails getDetails() {
        MediaDetails details = super.getDetails();
        details.addDetail(SYNC_RESULT_CANCELLED, getName());
        return details;
    }

    protected int getIndexOf(Path path, ArrayList<MediaItem> list) {
        int i = 0;
        int n = list.size();
        while (i < n) {
            if (((MediaItem) list.get(i)).mPath == path) {
                return i;
            }
            i++;
        }
        return INVALID_COUNT;
    }

    public int getIndexOfItem(Path path, int hint) {
        int start = Math.max(0, hint - 250);
        int index = getIndexOf(path, getMediaItem(start, MEDIAITEM_BATCH_FETCH_COUNT));
        if (index != -1) {
            return start + index;
        }
        if (start == 0) {
            start = 500;
        } else {
            start = 0;
        }
        ArrayList<MediaItem> list = getMediaItem(start, MEDIAITEM_BATCH_FETCH_COUNT);
        while (true) {
            index = getIndexOf(path, list);
            if (index != -1) {
                return start + index;
            }
            if (list.size() < 500) {
                return -1;
            }
            start += 500;
            list = getMediaItem(start, MEDIAITEM_BATCH_FETCH_COUNT);
        }
    }

    public boolean getIsDeleted() {
        return this.mIsDeleted;
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        return new ArrayList();
    }

    public int getMediaItemCount() {
        return 0;
    }

    public abstract String getName();

    public MediaSet getSubMediaSet(int index) {
        throw new IndexOutOfBoundsException();
    }

    public int getSubMediaSetCount() {
        return 0;
    }

    public int getTotalMediaItemCount() {
        int total = getMediaItemCount();
        int i = 0;
        while (i < getSubMediaSetCount()) {
            total += getSubMediaSet(i).getTotalMediaItemCount();
            i++;
        }
        return total;
    }

    public boolean isLeafAlbum() {
        return false;
    }

    public void notifyContentChanged() {
        Iterator i$ = this.mListeners.keySet().iterator();
        while (i$.hasNext()) {
            ((ContentListener) i$.next()).onContentDirty();
        }
    }

    public abstract long reload();

    public void reloadRequest() {
        throw new UnsupportedOperationException();
    }

    public void removeContentListener(ContentListener listener) {
        if (this.mListeners.containsKey(listener)) {
            this.mListeners.remove(listener);
        }
    }

    public Future<Integer> requestSync(SyncListener listener) {
        return FUTURE_STUB;
    }

    protected Future<Integer> requestSyncOnEmptySets(MediaSet[] sets, SyncListener listener) {
        MultiSetSyncFuture future = new MultiSetSyncFuture(listener);
        future.requestSyncOnEmptySets(sets);
        return future;
    }

    public void setFilterSize(int sizeInByte) {
        throw new UnsupportedOperationException();
    }

    public void setIsDeleted(boolean delected) {
        this.mIsDeleted = delected;
    }

    public void setMediaItemCount(int count) {
    }

    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    public void setOrderBy(String fieldName) {
        throw new UnsupportedOperationException();
    }
}