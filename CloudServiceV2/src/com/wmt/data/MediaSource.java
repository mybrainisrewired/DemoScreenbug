package com.wmt.data;

import android.net.Uri;
import android.util.Log;
import com.wmt.data.MediaSet.ItemConsumer;
import java.util.ArrayList;

public abstract class MediaSource {
    private static final String TAG = "MediaSource";
    private String[] mPrefix;

    public static class PathId {
        public int id;
        public Path path;

        public PathId(Path path, int id) {
            this.path = path;
            this.id = id;
        }
    }

    protected MediaSource(String[] prefix) {
        this.mPrefix = prefix;
    }

    public abstract MediaObject createMediaObject(Path path);

    public Path findPathByUri(Uri uri) {
        return null;
    }

    public Path getDefaultSetOf(Path item) {
        return null;
    }

    public String[] getPrefixs() {
        return this.mPrefix;
    }

    public long getTotalTargetCacheSize() {
        return 0;
    }

    public long getTotalUsedCacheSize() {
        return 0;
    }

    public void mapMediaItems(ArrayList<PathId> list, ItemConsumer consumer) {
        int n = list.size();
        int i = 0;
        while (i < n) {
            PathId pid = (PathId) list.get(i);
            MediaObject obj = pid.path.getObject();
            if (obj == null) {
                try {
                    obj = createMediaObject(pid.path);
                } catch (Throwable th) {
                    Log.w(TAG, "cannot create media object: " + pid.path);
                }
            }
            if (obj != null) {
                consumer.consume(pid.id, (MediaItem) obj);
            }
            i++;
        }
    }

    public void pause() {
    }

    public void resume() {
    }
}