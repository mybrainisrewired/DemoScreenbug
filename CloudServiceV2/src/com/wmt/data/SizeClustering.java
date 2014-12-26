package com.wmt.data;

import android.content.Context;
import com.wmt.data.MediaSet.ItemConsumer;
import java.util.ArrayList;

public class SizeClustering extends Clustering {
    private static final long GIGA_BYTES = 1073741824;
    private static final long MEGA_BYTES = 1048576;
    private static final long[] SIZE_LEVELS;
    private static final String TAG = "SizeClustering";
    private ArrayList<Path>[] mClusters;
    private Context mContext;
    private long[] mMinSizes;
    private String[] mNames;

    class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ ArrayList[] val$group;

        AnonymousClass_1(ArrayList[] arrayListArr) {
            this.val$group = arrayListArr;
        }

        public void consume(int index, MediaItem item) {
            long size = item.getSize();
            int i = 0;
            while (i < SIZE_LEVELS.length - 1 && size >= SIZE_LEVELS[i + 1]) {
                i++;
            }
            ArrayList<Path> list = this.val$group[i];
            if (list == null) {
                list = new ArrayList();
                this.val$group[i] = list;
            }
            list.add(item.getPath());
        }
    }

    static {
        SIZE_LEVELS = new long[]{0, 1048576, 10485760, 104857600, 1073741824, 2147483648L, 4294967296L};
    }

    public SizeClustering(Context context) {
        this.mContext = context;
    }

    private String getSizeString(int index) {
        long bytes = SIZE_LEVELS[index];
        return bytes >= 1073741824 ? (bytes / 1073741824) + "GB" : (bytes / 1048576) + "MB";
    }

    public ArrayList<Path> getCluster(int index) {
        return this.mClusters[index];
    }

    public String getClusterName(int index) {
        return this.mNames[index];
    }

    public long getMinSize(int index) {
        return this.mMinSizes[index];
    }

    public int getNumberOfClusters() {
        return this.mClusters.length;
    }

    public void run(MediaSet baseSet) {
        ArrayList[] group = new ArrayList[SIZE_LEVELS.length];
        baseSet.enumerateTotalMediaItems(new AnonymousClass_1(group));
        int count = 0;
        int i = 0;
        while (i < group.length) {
            if (group[i] != null) {
                count++;
            }
            i++;
        }
        this.mClusters = new ArrayList[count];
        this.mNames = new String[count];
        this.mMinSizes = new long[count];
        int k = 0;
        i = group.length - 1;
        while (i >= 0) {
            if (group[i] != null) {
                this.mClusters[k] = group[i];
                if (i == 0) {
                    this.mNames[k] = String.format("size below %s", new Object[]{getSizeString(i + 1)});
                } else if (i == group.length - 1) {
                    this.mNames[k] = String.format("size above %s", new Object[]{getSizeString(i)});
                } else {
                    String minSize = getSizeString(i);
                    String maxSize = getSizeString(i + 1);
                    this.mNames[k] = String.format("size between %s", new Object[]{minSize, maxSize});
                }
                this.mMinSizes[k] = SIZE_LEVELS[i];
                k++;
            }
            i--;
        }
    }
}