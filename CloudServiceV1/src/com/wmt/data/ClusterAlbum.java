package com.wmt.data;

import com.wmt.data.MediaSet.ItemConsumer;
import java.util.ArrayList;

public class ClusterAlbum extends MediaSet implements ContentListener {
    private static final String TAG = "ClusterAlbum";
    private MediaSet mClusterAlbumSet;
    private DataManager mDataManager;
    private String mName;
    private ArrayList<Path> mPaths;

    static class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ MediaItem[] val$buf;

        AnonymousClass_1(MediaItem[] mediaItemArr) {
            this.val$buf = mediaItemArr;
        }

        public void consume(int index, MediaItem item) {
            this.val$buf[index] = item;
        }
    }

    public ClusterAlbum(Path path, DataManager dataManager, MediaSet clusterAlbumSet) {
        super(path, nextVersionNumber());
        this.mPaths = new ArrayList();
        this.mName = "";
        this.mDataManager = dataManager;
        this.mClusterAlbumSet = clusterAlbumSet;
        this.mClusterAlbumSet.addContentListener(this);
    }

    public static ArrayList<MediaItem> getMediaItemFromPath(ArrayList<Path> paths, int start, int count, DataManager dataManager) {
        if (start >= paths.size()) {
            return new ArrayList();
        }
        int end = Math.min(start + count, paths.size());
        MediaItem[] buf = new MediaItem[(end - start)];
        dataManager.mapMediaItems(new ArrayList(paths.subList(start, end)), new AnonymousClass_1(buf), 0);
        ArrayList<MediaItem> result = new ArrayList(end - start);
        int i = 0;
        while (i < buf.length) {
            result.add(buf[i]);
            i++;
        }
        return result;
    }

    public void delete() {
        this.mDataManager.mapMediaItems(this.mPaths, new ItemConsumer() {
            public void consume(int index, MediaItem item) {
                if ((item.getSupportedOperations() & 1) != 0) {
                    item.delete();
                }
            }
        }, 0);
    }

    protected int enumerateMediaItems(ItemConsumer consumer, int startIndex) {
        this.mDataManager.mapMediaItems(this.mPaths, consumer, startIndex);
        return this.mPaths.size();
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        return getMediaItemFromPath(this.mPaths, start, count, this.mDataManager);
    }

    public int getMediaItemCount() {
        return this.mPaths.size();
    }

    ArrayList<Path> getMediaItems() {
        return this.mPaths;
    }

    public String getName() {
        return this.mName;
    }

    public int getSupportedOperations() {
        return 1029;
    }

    public int getTotalMediaItemCount() {
        return this.mPaths.size();
    }

    public boolean isLeafAlbum() {
        return true;
    }

    public void onContentDirty() {
        notifyContentChanged();
    }

    public long reload() {
        if (this.mClusterAlbumSet.reload() > this.mDataVersion) {
            this.mDataVersion = nextVersionNumber();
        }
        return this.mDataVersion;
    }

    void setMediaItems(ArrayList<Path> paths) {
        this.mPaths = paths;
    }

    public void setName(String name) {
        this.mName = name;
    }
}