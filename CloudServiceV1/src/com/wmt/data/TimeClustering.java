package com.wmt.data;

import android.content.Context;
import com.wmt.data.MediaSet.ItemConsumer;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TimeClustering extends Clustering {
    private static int CLUSTER_SPLIT_MULTIPLIER = 0;
    private static final int GEOGRAPHIC_DISTANCE_CUTOFF_IN_MILES = 20;
    private static final long MAX_CLUSTER_SPLIT_TIME_IN_MS = 7200000;
    private static final int MAX_MAX_CLUSTER_SIZE = 50;
    private static final int MAX_MIN_CLUSTER_SIZE = 15;
    private static final long MIN_CLUSTER_SPLIT_TIME_IN_MS = 60000;
    private static final int MIN_MAX_CLUSTER_SIZE = 20;
    private static final int MIN_MIN_CLUSTER_SIZE = 8;
    private static final int MIN_PARTITION_CHANGE_FACTOR = 2;
    private static final int NUM_CLUSTERS_TARGETED = 9;
    private static final int PARTITION_CLUSTER_SPLIT_TIME_FACTOR = 2;
    private static final String TAG = "TimeClustering";
    private static final Comparator<SmallItem> sDateComparator;
    private long mClusterSplitTime;
    private ArrayList<Cluster> mClusters;
    private Context mContext;
    private Cluster mCurrCluster;
    private long mLargeClusterSplitTime;
    private int mMaxClusterSize;
    private int mMinClusterSize;
    private String[] mNames;

    private static class DateComparator implements Comparator<SmallItem> {
        private DateComparator() {
        }

        public int compare(SmallItem item1, SmallItem item2) {
            return Utils.compare(item1.dateInMs, item2.dateInMs);
        }
    }

    class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ SmallItem[] val$buf;
        final /* synthetic */ double[] val$latLng;
        final /* synthetic */ int val$total;

        AnonymousClass_1(int i, double[] dArr, SmallItem[] smallItemArr) {
            this.val$total = i;
            this.val$latLng = dArr;
            this.val$buf = smallItemArr;
        }

        public void consume(int index, MediaItem item) {
            if (index >= 0 && index < this.val$total) {
                SmallItem s = new SmallItem();
                s.path = item.getPath();
                s.dateInMs = item.getDateInMs();
                item.getLatLong(this.val$latLng);
                s.lat = this.val$latLng[0];
                s.lng = this.val$latLng[1];
                this.val$buf[index] = s;
            }
        }
    }

    static {
        CLUSTER_SPLIT_MULTIPLIER = 3;
        sDateComparator = new DateComparator();
    }

    public TimeClustering(Context context) {
        this.mClusterSplitTime = 3630000;
        this.mLargeClusterSplitTime = this.mClusterSplitTime / 2;
        this.mMinClusterSize = 11;
        this.mMaxClusterSize = 35;
        this.mContext = context;
        this.mClusters = new ArrayList();
        this.mCurrCluster = new Cluster();
    }

    private void compute(SmallItem currentItem) {
        int numClusters;
        int numCurrClusterItems;
        if (currentItem != null) {
            numClusters = this.mClusters.size();
            numCurrClusterItems = this.mCurrCluster.size();
            boolean geographicallySeparateItem = false;
            boolean itemAddedToCurrentCluster = false;
            if (numCurrClusterItems == 0) {
                this.mCurrCluster.addItem(currentItem);
            } else {
                SmallItem prevItem = this.mCurrCluster.getLastItem();
                if (isGeographicallySeparated(prevItem, currentItem)) {
                    this.mClusters.add(this.mCurrCluster);
                    geographicallySeparateItem = true;
                } else if (numCurrClusterItems > this.mMaxClusterSize) {
                    splitAndAddCurrentCluster();
                } else if (timeDistance(prevItem, currentItem) < this.mClusterSplitTime) {
                    this.mCurrCluster.addItem(currentItem);
                    itemAddedToCurrentCluster = true;
                } else if (numClusters <= 0 || numCurrClusterItems >= this.mMinClusterSize || this.mCurrCluster.mGeographicallySeparatedFromPrevCluster) {
                    this.mClusters.add(this.mCurrCluster);
                } else {
                    mergeAndAddCurrentCluster();
                }
                if (!itemAddedToCurrentCluster) {
                    this.mCurrCluster = new Cluster();
                    if (geographicallySeparateItem) {
                        this.mCurrCluster.mGeographicallySeparatedFromPrevCluster = true;
                    }
                    this.mCurrCluster.addItem(currentItem);
                }
            }
        } else if (this.mCurrCluster.size() > 0) {
            numClusters = this.mClusters.size();
            numCurrClusterItems = this.mCurrCluster.size();
            if (numCurrClusterItems > this.mMaxClusterSize) {
                splitAndAddCurrentCluster();
            } else if (numClusters <= 0 || numCurrClusterItems >= this.mMinClusterSize || this.mCurrCluster.mGeographicallySeparatedFromPrevCluster) {
                this.mClusters.add(this.mCurrCluster);
            } else {
                mergeAndAddCurrentCluster();
            }
            this.mCurrCluster = new Cluster();
        }
    }

    private int getPartitionIndexForCurrentCluster() {
        int partitionIndex = -1;
        float largestChange = 2.0f;
        ArrayList<SmallItem> currClusterItems = this.mCurrCluster.getItems();
        int numCurrClusterItems = this.mCurrCluster.size();
        int minClusterSize = this.mMinClusterSize;
        if (numCurrClusterItems > minClusterSize + 1) {
            int i = minClusterSize;
            while (i < numCurrClusterItems - minClusterSize) {
                SmallItem prevItem = (SmallItem) currClusterItems.get(i - 1);
                SmallItem currItem = (SmallItem) currClusterItems.get(i);
                SmallItem nextItem = (SmallItem) currClusterItems.get(i + 1);
                long timeNext = nextItem.dateInMs;
                long timeCurr = currItem.dateInMs;
                long timePrev = prevItem.dateInMs;
                if (!(timeNext == 0 || timeCurr == 0 || timePrev == 0)) {
                    long diff1 = Math.abs(timeNext - timeCurr);
                    long diff2 = Math.abs(timeCurr - timePrev);
                    float change = Math.max(((float) diff1) / (((float) diff2) + 0.01f), ((float) diff2) / (((float) diff1) + 0.01f));
                    if (change > largestChange) {
                        if (timeDistance(currItem, prevItem) > this.mLargeClusterSplitTime) {
                            partitionIndex = i;
                            largestChange = change;
                        } else if (timeDistance(nextItem, currItem) > this.mLargeClusterSplitTime) {
                            partitionIndex = i + 1;
                            largestChange = change;
                        }
                    }
                }
                i++;
            }
        }
        return partitionIndex;
    }

    private static boolean isGeographicallySeparated(SmallItem itemA, SmallItem itemB) {
        if (Utils.isValidLocation(itemA.lat, itemA.lng) && Utils.isValidLocation(itemB.lat, itemB.lng)) {
            return Utils.toMile(Utils.fastDistanceMeters(Math.toRadians(itemA.lat), Math.toRadians(itemA.lng), Math.toRadians(itemB.lat), Math.toRadians(itemB.lng))) > 20.0d;
        } else {
            return false;
        }
    }

    private void mergeAndAddCurrentCluster() {
        int numClusters = this.mClusters.size();
        Cluster prevCluster = (Cluster) this.mClusters.get(numClusters - 1);
        ArrayList<SmallItem> currClusterItems = this.mCurrCluster.getItems();
        int numCurrClusterItems = this.mCurrCluster.size();
        if (prevCluster.size() < this.mMinClusterSize) {
            int i = 0;
            while (i < numCurrClusterItems) {
                prevCluster.addItem((SmallItem) currClusterItems.get(i));
                i++;
            }
            this.mClusters.set(numClusters - 1, prevCluster);
        } else {
            this.mClusters.add(this.mCurrCluster);
        }
    }

    private void setTimeRange(long timeRange, int numItems) {
        if (numItems != 0) {
            int meanItemsPerCluster = numItems / 9;
            this.mMinClusterSize = meanItemsPerCluster / 2;
            this.mMaxClusterSize = meanItemsPerCluster * 2;
            this.mClusterSplitTime = (timeRange / ((long) numItems)) * ((long) CLUSTER_SPLIT_MULTIPLIER);
        }
        this.mClusterSplitTime = Utils.clamp(this.mClusterSplitTime, (long)MIN_CLUSTER_SPLIT_TIME_IN_MS, (long)MAX_CLUSTER_SPLIT_TIME_IN_MS);
        this.mLargeClusterSplitTime = this.mClusterSplitTime / 2;
        this.mMinClusterSize = Utils.clamp(this.mMinClusterSize, (int)MIN_MIN_CLUSTER_SIZE, (int)MAX_MIN_CLUSTER_SIZE);
        this.mMaxClusterSize = Utils.clamp(this.mMaxClusterSize, (int)MIN_MAX_CLUSTER_SIZE, (int)MAX_MAX_CLUSTER_SIZE);
    }

    private void splitAndAddCurrentCluster() {
        ArrayList<SmallItem> currClusterItems = this.mCurrCluster.getItems();
        int numCurrClusterItems = this.mCurrCluster.size();
        int secondPartitionStartIndex = getPartitionIndexForCurrentCluster();
        if (secondPartitionStartIndex != -1) {
            Cluster partitionedCluster = new Cluster();
            int j = 0;
            while (j < secondPartitionStartIndex) {
                partitionedCluster.addItem((SmallItem) currClusterItems.get(j));
                j++;
            }
            this.mClusters.add(partitionedCluster);
            partitionedCluster = new Cluster();
            j = secondPartitionStartIndex;
            while (j < numCurrClusterItems) {
                partitionedCluster.addItem((SmallItem) currClusterItems.get(j));
                j++;
            }
            this.mClusters.add(partitionedCluster);
        } else {
            this.mClusters.add(this.mCurrCluster);
        }
    }

    private static long timeDistance(SmallItem a, SmallItem b) {
        return Math.abs(a.dateInMs - b.dateInMs);
    }

    public ArrayList<Path> getCluster(int index) {
        ArrayList<SmallItem> items = ((Cluster) this.mClusters.get(index)).getItems();
        ArrayList<Path> result = new ArrayList(items.size());
        int i = 0;
        int n = items.size();
        while (i < n) {
            result.add(((SmallItem) items.get(i)).path);
            i++;
        }
        return result;
    }

    public String getClusterName(int index) {
        return this.mNames[index];
    }

    public int getNumberOfClusters() {
        return this.mClusters.size();
    }

    public void run(MediaSet baseSet) {
        int total = baseSet.getTotalMediaItemCount();
        SmallItem[] buf = new SmallItem[total];
        baseSet.enumerateTotalMediaItems(new AnonymousClass_1(total, new double[2], buf));
        ArrayList<SmallItem> items = new ArrayList(total);
        int i = 0;
        while (i < total) {
            if (buf[i] != null) {
                items.add(buf[i]);
            }
            i++;
        }
        Collections.sort(items, sDateComparator);
        int n = items.size();
        long minTime = 0;
        long maxTime = 0;
        i = 0;
        while (i < n) {
            long t = ((SmallItem) items.get(i)).dateInMs;
            if (t != 0) {
                if (minTime == 0) {
                    maxTime = t;
                    minTime = t;
                } else {
                    minTime = Math.min(minTime, t);
                    maxTime = Math.max(maxTime, t);
                }
            }
            i++;
        }
        setTimeRange(maxTime - minTime, n);
        i = 0;
        while (i < n) {
            compute((SmallItem) items.get(i));
            i++;
        }
        compute(null);
        int m = this.mClusters.size();
        this.mNames = new String[m];
        i = 0;
        while (i < m) {
            this.mNames[i] = ((Cluster) this.mClusters.get(i)).generateCaption(this.mContext);
            i++;
        }
    }
}