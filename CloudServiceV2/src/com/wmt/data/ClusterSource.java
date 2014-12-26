package com.wmt.data;

import android.content.Context;

class ClusterSource extends MediaSource {
    static final int CLUSTER_ALBUMSET_FACE = 4;
    static final int CLUSTER_ALBUMSET_LOCATION = 1;
    static final int CLUSTER_ALBUMSET_SIZE = 3;
    static final int CLUSTER_ALBUMSET_TAG = 2;
    static final int CLUSTER_ALBUMSET_TIME = 0;
    static final int CLUSTER_ALBUM_FACE = 260;
    static final int CLUSTER_ALBUM_LOCATION = 257;
    static final int CLUSTER_ALBUM_SIZE = 259;
    static final int CLUSTER_ALBUM_TAG = 258;
    static final int CLUSTER_ALBUM_TIME = 256;
    Context mContext;
    DataManager mDataManager;
    PathMatcher mMatcher;

    public ClusterSource(Context context, DataManager dataManager) {
        super(new String[]{"cluster"});
        this.mContext = context;
        this.mDataManager = dataManager;
        this.mMatcher = new PathMatcher();
        this.mMatcher.add("/cluster/*/time", CLUSTER_ALBUMSET_TIME);
        this.mMatcher.add("/cluster/*/location", CLUSTER_ALBUMSET_LOCATION);
        this.mMatcher.add("/cluster/*/tag", CLUSTER_ALBUMSET_TAG);
        this.mMatcher.add("/cluster/*/size", CLUSTER_ALBUMSET_SIZE);
        this.mMatcher.add("/cluster/*/face", CLUSTER_ALBUMSET_FACE);
        this.mMatcher.add("/cluster/*/time/*", CLUSTER_ALBUM_TIME);
        this.mMatcher.add("/cluster/*/location/*", CLUSTER_ALBUM_LOCATION);
        this.mMatcher.add("/cluster/*/tag/*", CLUSTER_ALBUM_TAG);
        this.mMatcher.add("/cluster/*/size/*", CLUSTER_ALBUM_SIZE);
        this.mMatcher.add("/cluster/*/face/*", CLUSTER_ALBUM_FACE);
    }

    public MediaObject createMediaObject(Path path) {
        int matchType = this.mMatcher.match(path);
        MediaSet[] sets = this.mDataManager.getMediaSetsFromString(this.mMatcher.getVar(CLUSTER_ALBUMSET_TIME));
        switch (matchType) {
            case CLUSTER_ALBUMSET_TIME:
            case CLUSTER_ALBUMSET_LOCATION:
            case CLUSTER_ALBUMSET_TAG:
            case CLUSTER_ALBUMSET_SIZE:
            case CLUSTER_ALBUMSET_FACE:
                return new ClusterAlbumSet(path, this.mContext, this.mDataManager, sets[0], matchType);
            case CLUSTER_ALBUM_TIME:
            case CLUSTER_ALBUM_LOCATION:
            case CLUSTER_ALBUM_TAG:
            case CLUSTER_ALBUM_SIZE:
            case CLUSTER_ALBUM_FACE:
                return new ClusterAlbum(path, this.mDataManager, this.mDataManager.getMediaSet(path.getParent()));
            default:
                throw new RuntimeException("bad path: " + path);
        }
    }
}