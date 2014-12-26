package com.wmt.data;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class LocalMergeAlbum extends MediaSet implements ContentListener {
    private static final int PAGE_SIZE = 64;
    private static final String TAG = "LocalMergeAlbum";
    private final Comparator<MediaItem> mComparator;
    private FetchCache[] mFetcher;
    private TreeMap<Integer, int[]> mIndex;
    private String mName;
    private final MediaSet[] mSources;
    private int mSupportedOperation;

    private static class FetchCache {
        private MediaSet mBaseSet;
        private SoftReference<ArrayList<MediaItem>> mCacheRef;
        private int mStartPos;

        public FetchCache(MediaSet baseSet) {
            this.mBaseSet = baseSet;
        }

        public MediaItem getItem(int index) {
            boolean needLoading = false;
            ArrayList<MediaItem> arrayList = null;
            if (this.mCacheRef == null || index < this.mStartPos || index >= this.mStartPos + 64) {
                needLoading = true;
            } else {
                arrayList = this.mCacheRef.get();
                if (arrayList == null) {
                    needLoading = true;
                }
            }
            if (needLoading) {
                arrayList = this.mBaseSet.getMediaItem(index, PAGE_SIZE);
                this.mCacheRef = new SoftReference(arrayList);
                this.mStartPos = index;
            }
            return (index < this.mStartPos || index >= this.mStartPos + arrayList.size()) ? null : (MediaItem) arrayList.get(index - this.mStartPos);
        }

        public void invalidate() {
            this.mCacheRef = null;
        }
    }

    public LocalMergeAlbum(Path path, Comparator<MediaItem> comparator, MediaSet[] sources) {
        super(path, -1);
        this.mIndex = new TreeMap();
        this.mComparator = comparator;
        this.mSources = sources;
        this.mName = sources.length == 0 ? "" : sources[0].getName();
        MediaSet[] arr$ = this.mSources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].addContentListener(this);
            i$++;
        }
    }

    private void invalidateCache() {
        int i = 0;
        int n = this.mSources.length;
        while (i < n) {
            this.mFetcher[i].invalidate();
            i++;
        }
        this.mIndex.clear();
        this.mIndex.put(Integer.valueOf(0), new Object[this.mSources.length]);
    }

    private void updateData() {
        int supported = this.mSources.length == 0 ? 0 : -1;
        this.mFetcher = new FetchCache[this.mSources.length];
        int i = 0;
        int n = this.mSources.length;
        while (i < n) {
            this.mFetcher[i] = new FetchCache(this.mSources[i]);
            supported &= this.mSources[i].getSupportedOperations();
            i++;
        }
        this.mSupportedOperation = supported;
        this.mIndex.clear();
        this.mIndex.put(Integer.valueOf(0), new Object[this.mSources.length]);
        this.mName = this.mSources.length == 0 ? "" : this.mSources[0].getName();
    }

    public void delete() {
        MediaSet[] arr$ = this.mSources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].delete();
            i$++;
        }
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        SortedMap<Integer, int[]> head = this.mIndex.headMap(Integer.valueOf(start + 1));
        int markPos = ((Integer) head.lastKey()).intValue();
        int[] subPos = (int[]) ((int[]) head.get(Integer.valueOf(markPos))).clone();
        MediaItem[] slot = new MediaItem[this.mSources.length];
        int size = this.mSources.length;
        int i = 0;
        while (i < size) {
            slot[i] = this.mFetcher[i].getItem(subPos[i]);
            i++;
        }
        ArrayList<MediaItem> result = new ArrayList();
        i = markPos;
        while (i < start + count) {
            int k = -1;
            int j = 0;
            while (j < size) {
                if (slot[j] != null) {
                    if (k == -1 || this.mComparator.compare(slot[j], slot[k]) < 0) {
                        k = j;
                    }
                }
                j++;
            }
            if (k == -1) {
                return result;
            }
            subPos[k] = subPos[k] + 1;
            if (i >= start) {
                result.add(slot[k]);
            }
            slot[k] = this.mFetcher[k].getItem(subPos[k]);
            if ((i + 1) % 64 == 0) {
                this.mIndex.put(Integer.valueOf(i + 1), subPos.clone());
            }
            i++;
        }
        return result;
    }

    public int getMediaItemCount() {
        return getTotalMediaItemCount();
    }

    public String getName() {
        return this.mName;
    }

    public int getSupportedOperations() {
        return this.mSupportedOperation;
    }

    public int getTotalMediaItemCount() {
        int count = 0;
        MediaSet[] arr$ = this.mSources;
        int i$ = 0;
        while (i$ < arr$.length) {
            count += arr$[i$].getTotalMediaItemCount();
            i$++;
        }
        return count;
    }

    public boolean isLeafAlbum() {
        return true;
    }

    public void onContentDirty() {
        notifyContentChanged();
    }

    public long reload() {
        boolean changed = false;
        int i = 0;
        int n = this.mSources.length;
        while (i < n) {
            if (this.mSources[i].reload() > this.mDataVersion) {
                changed = true;
            }
            i++;
        }
        if (changed) {
            this.mDataVersion = nextVersionNumber();
            updateData();
            invalidateCache();
        }
        return this.mDataVersion;
    }

    public void rotate(int degrees) {
        MediaSet[] arr$ = this.mSources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].rotate(degrees);
            i$++;
        }
    }

    public void setFilterSize(int sizeInByte) {
        MediaSet[] arr$ = this.mSources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].setFilterSize(sizeInByte);
            i$++;
        }
    }

    public void setOrderBy(String orderByField) {
        MediaSet[] arr$ = this.mSources;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].setOrderBy(orderByField);
            i$++;
        }
    }
}