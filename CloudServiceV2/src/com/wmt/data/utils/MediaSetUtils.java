package com.wmt.data.utils;

import android.os.Environment;
import com.wmt.data.MediaSet;
import com.wmt.data.Path;
import com.wmt.util.Utils;
import java.util.Comparator;

public class MediaSetUtils {
    public static final int CAMERA_BUCKET_ID;
    private static final Path[] CAMERA_PATHS;
    public static final int DOWNLOAD_BUCKET_ID;
    public static final Comparator<MediaSet> NAME_COMPARATOR;

    public static class NameComparator implements Comparator<MediaSet> {
        public int compare(MediaSet set1, MediaSet set2) {
            int result = set1.getName().compareToIgnoreCase(set2.getName());
            return result != 0 ? result : set1.getPath().toString().compareTo(set2.getPath().toString());
        }
    }

    static {
        NAME_COMPARATOR = new NameComparator();
        CAMERA_BUCKET_ID = Utils.getBucketId(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
        DOWNLOAD_BUCKET_ID = Utils.getBucketId(Environment.getExternalStorageDirectory().toString() + "/download");
        CAMERA_PATHS = new Path[]{Path.fromString("/local/all/" + CAMERA_BUCKET_ID), Path.fromString("/local/image/" + CAMERA_BUCKET_ID), Path.fromString("/local/video/" + CAMERA_BUCKET_ID)};
    }

    public static boolean isCameraSource(Path path) {
        return CAMERA_PATHS[0] == path || CAMERA_PATHS[1] == path || CAMERA_PATHS[2] == path;
    }
}