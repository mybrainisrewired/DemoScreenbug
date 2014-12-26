package com.wmt.data;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import com.wmt.data.MediaSet.ItemConsumer;
import com.wmt.opengl.grid.ItemAnimation;
import java.util.ArrayList;
import java.util.HashSet;

public class ClusterAlbumSet extends MediaSet implements ContentListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "ClusterAlbumSet";
    private ArrayList<ClusterAlbum> mAlbums;
    private MediaSet mBaseSet;
    private Context mContext;
    private DataManager mDataManager;
    private boolean mFirstReloadDone;
    private int mKind;

    class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ HashSet val$existing;

        AnonymousClass_1(HashSet hashSet) {
            this.val$existing = hashSet;
        }

        public void consume(int index, MediaItem item) {
            this.val$existing.add(item.getPath());
        }
    }

    public ClusterAlbumSet(Path path, Context context, DataManager dataManager, MediaSet baseSet, int kind) {
        super(path, -1);
        this.mAlbums = new ArrayList();
        this.mContext = context;
        this.mDataManager = dataManager;
        this.mBaseSet = baseSet;
        this.mKind = kind;
        baseSet.addContentListener(this);
    }

    private void updateClusters() {
        Clustering clustering;
        long totalBefore = SystemClock.elapsedRealtime();
        this.mAlbums.clear();
        switch (this.mKind) {
            case LocalAudioAll.SORT_BY_TITLE:
                clustering = new TimeClustering(this.mContext);
                break;
            case LocalAudioAll.SORT_BY_DATE:
                clustering = new LocationClustering(this.mContext);
                break;
            case ItemAnimation.CUR_Z:
                clustering = new TagClustering(this.mContext);
                break;
            case ItemAnimation.CUR_ARC:
                clustering = new FaceClustering(this.mContext);
                break;
            default:
                clustering = new SizeClustering(this.mContext);
                break;
        }
        long before = SystemClock.elapsedRealtime();
        clustering.run(this.mBaseSet);
        long after = SystemClock.elapsedRealtime();
        int n = clustering.getNumberOfClusters();
        int i = 0;
        while (i < n) {
            Path childPath;
            String childName = clustering.getClusterName(i);
            if (this.mKind == 2) {
                childPath = this.mPath.getChild(Uri.encode(childName));
            } else if (this.mKind == 3) {
                childPath = this.mPath.getChild(((SizeClustering) clustering).getMinSize(i));
            } else {
                childPath = this.mPath.getChild(i);
            }
            ClusterAlbum album = (ClusterAlbum) this.mDataManager.peekMediaObject(childPath);
            if (album == null) {
                album = new ClusterAlbum(childPath, this.mDataManager, this);
            }
            album.setMediaItems(clustering.getCluster(i));
            album.setName(childName);
            this.mAlbums.add(album);
            i++;
        }
        Log.v(TAG, "updateClusters take " + (SystemClock.elapsedRealtime() - totalBefore) + " time, cluster takes " + (after - before) + " time");
    }

    private void updateClustersContents() {
        long before = SystemClock.elapsedRealtime();
        HashSet<Path> existing = new HashSet();
        this.mBaseSet.enumerateTotalMediaItems(new AnonymousClass_1(existing));
        int i = this.mAlbums.size() - 1;
        while (i >= 0) {
            ArrayList<Path> oldPaths = ((ClusterAlbum) this.mAlbums.get(i)).getMediaItems();
            ArrayList<Path> newPaths = new ArrayList();
            int m = oldPaths.size();
            int j = 0;
            while (j < m) {
                Path p = (Path) oldPaths.get(j);
                if (existing.contains(p)) {
                    newPaths.add(p);
                }
                j++;
            }
            ((ClusterAlbum) this.mAlbums.get(i)).setMediaItems(newPaths);
            if (newPaths.isEmpty()) {
                this.mAlbums.remove(i);
            }
            i--;
        }
        Log.v(TAG, "updateClustersContents takes " + (SystemClock.elapsedRealtime() - before) + " times");
    }

    public String getName() {
        return this.mBaseSet.getName();
    }

    public synchronized MediaSet getSubMediaSet(int index) {
        MediaSet mediaSet;
        loadIfNecessary();
        if (index < this.mAlbums.size()) {
            mediaSet = (MediaSet) this.mAlbums.get(index);
        } else {
            mediaSet = null;
        }
        return mediaSet;
    }

    public synchronized int getSubMediaSetCount() {
        loadIfNecessary();
        return this.mAlbums.size();
    }

    public void loadIfNecessary() {
        if (this.mBaseSet.reload() > this.mDataVersion) {
            if (this.mFirstReloadDone) {
                updateClustersContents();
            } else {
                updateClusters();
                this.mFirstReloadDone = true;
            }
            this.mDataVersion = nextVersionNumber();
        }
    }

    public void onContentDirty() {
        notifyContentChanged();
    }

    public synchronized long reload() {
        loadIfNecessary();
        return this.mDataVersion;
    }

    public void setFilterSize(int sizeInByte) {
        this.mBaseSet.setFilterSize(sizeInByte);
    }

    public void setOrderBy(String fieldName) {
        this.mBaseSet.setOrderBy(fieldName);
    }
}