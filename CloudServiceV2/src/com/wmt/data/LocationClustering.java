package com.wmt.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.wmt.data.MediaSet.ItemConsumer;
import com.wmt.data.utils.ReverseGeocoder;
import com.wmt.data.utils.ReverseGeocoder.SetLatLong;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;

class LocationClustering extends Clustering {
    private static final int MAX_GROUPS = 20;
    private static final int MAX_ITERATIONS = 30;
    private static final int MIN_GROUPS = 1;
    private static final float STOP_CHANGE_RATIO = 0.01f;
    private static final String TAG = "LocationClustering";
    private ArrayList<ArrayList<SmallItem>> mClusters;
    private Context mContext;
    private Handler mHandler;
    private ArrayList<String> mNames;
    private String mNoLocationString;

    private static class Point {
        public double latRad;
        public double lngRad;

        public Point(double lat, double lng) {
            this.latRad = Math.toRadians(lat);
            this.lngRad = Math.toRadians(lng);
        }
    }

    private static class SmallItem {
        double lat;
        double lng;
        Path path;

        private SmallItem() {
        }
    }

    class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ SmallItem[] val$buf;
        final /* synthetic */ double[] val$latLong;
        final /* synthetic */ int val$total;

        AnonymousClass_1(int i, double[] dArr, SmallItem[] smallItemArr) {
            this.val$total = i;
            this.val$latLong = dArr;
            this.val$buf = smallItemArr;
        }

        public void consume(int index, MediaItem item) {
            if (index >= 0 && index < this.val$total) {
                SmallItem s = new SmallItem();
                s.path = item.getPath();
                item.getLatLong(this.val$latLong);
                s.lat = this.val$latLong[0];
                s.lng = this.val$latLong[1];
                this.val$buf[index] = s;
            }
        }
    }

    public LocationClustering(Context context) {
        this.mContext = context;
        this.mNoLocationString = DataManager.UnknownLocation;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    private static String generateName(ArrayList<SmallItem> items, ReverseGeocoder geocoder) {
        SetLatLong set = new SetLatLong();
        int n = items.size();
        int i = 0;
        while (i < n) {
            SmallItem item = (SmallItem) items.get(i);
            double itemLatitude = item.lat;
            double itemLongitude = item.lng;
            if (set.mMinLatLatitude > itemLatitude) {
                set.mMinLatLatitude = itemLatitude;
                set.mMinLatLongitude = itemLongitude;
            }
            if (set.mMaxLatLatitude < itemLatitude) {
                set.mMaxLatLatitude = itemLatitude;
                set.mMaxLatLongitude = itemLongitude;
            }
            if (set.mMinLonLongitude > itemLongitude) {
                set.mMinLonLatitude = itemLatitude;
                set.mMinLonLongitude = itemLongitude;
            }
            if (set.mMaxLonLongitude < itemLongitude) {
                set.mMaxLonLatitude = itemLatitude;
                set.mMaxLonLongitude = itemLongitude;
            }
            i++;
        }
        return geocoder.computeAddress(set);
    }

    private static int[] kMeans(Point[] points, int[] bestK) {
        int n = points.length;
        int minK = Math.min(n, MIN_GROUPS);
        int maxK = Math.min(n, MAX_GROUPS);
        Point[] center = new Point[maxK];
        Point[] groupSum = new Point[maxK];
        int[] groupCount = new int[maxK];
        int[] grouping = new int[n];
        int i = 0;
        while (i < maxK) {
            center[i] = new Point();
            groupSum[i] = new Point();
            i++;
        }
        float bestScore = Float.MAX_VALUE;
        int[] bestGrouping = new int[n];
        bestK[0] = 1;
        float lastDistance = 0.0f;
        float f = 0.0f;
        int k = minK;
        while (k <= maxK) {
            int delta = n / k;
            i = 0;
            while (i < k) {
                Point p = points[i * delta];
                center[i].latRad = p.latRad;
                center[i].lngRad = p.lngRad;
                i++;
            }
            int iter = 0;
            while (iter < 30) {
                i = 0;
                while (i < k) {
                    groupSum[i].latRad = 0.0d;
                    groupSum[i].lngRad = 0.0d;
                    groupCount[i] = 0;
                    i++;
                }
                f = 0.0f;
                i = 0;
                while (i < n) {
                    p = points[i];
                    float bestDistance = Float.MAX_VALUE;
                    int bestIndex = 0;
                    int j = 0;
                    while (j < k) {
                        float distance = (float) Utils.fastDistanceMeters(p.latRad, p.lngRad, center[j].latRad, center[j].lngRad);
                        if (distance < 1.0f) {
                            distance = 0.0f;
                        }
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            bestIndex = j;
                        }
                        j++;
                    }
                    grouping[i] = bestIndex;
                    groupCount[bestIndex] = groupCount[bestIndex] + 1;
                    Point point = groupSum[bestIndex];
                    point.latRad += p.latRad;
                    point = groupSum[bestIndex];
                    point.lngRad += p.lngRad;
                    f += bestDistance;
                    i++;
                }
                i = 0;
                while (i < k) {
                    if (groupCount[i] > 0) {
                        groupSum[i].latRad /= (double) groupCount[i];
                        groupSum[i].lngRad /= (double) groupCount[i];
                    }
                    i++;
                }
                if (f == 0.0f || Math.abs(lastDistance - f) / f < 0.01f) {
                    break;
                }
                lastDistance = f;
                iter++;
            }
            int[] reassign = new int[k];
            i = 0;
            int realK = 0;
            while (i < k) {
                int realK2;
                if (groupCount[i] > 0) {
                    realK2 = realK + 1;
                    reassign[i] = realK;
                } else {
                    realK2 = realK;
                }
                i++;
                realK = realK2;
            }
            float score = f * ((float) Math.sqrt((double) realK));
            if (score < bestScore) {
                bestScore = score;
                bestK[0] = realK;
                i = 0;
                while (i < n) {
                    bestGrouping[i] = reassign[grouping[i]];
                    i++;
                }
                if (score == 0.0f) {
                    return bestGrouping;
                }
            }
            k++;
        }
        return bestGrouping;
    }

    public ArrayList<Path> getCluster(int index) {
        ArrayList<SmallItem> items = (ArrayList) this.mClusters.get(index);
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
        return (String) this.mNames.get(index);
    }

    public int getNumberOfClusters() {
        return this.mClusters.size();
    }

    public void run(MediaSet baseSet) {
        int total = baseSet.getTotalMediaItemCount();
        SmallItem[] buf = new SmallItem[total];
        double[] latLong = new double[2];
        AnonymousClass_1 anonymousClass_1 = anonymousClass_1;
        LocationClustering locationClustering = this;
        int i = total;
        baseSet.enumerateTotalMediaItems(anonymousClass_1);
        ArrayList<SmallItem> withLatLong = new ArrayList();
        ArrayList<SmallItem> withoutLatLong = new ArrayList();
        ArrayList<Point> points = new ArrayList();
        int i2 = 0;
        while (i2 < total) {
            SmallItem s = buf[i2];
            if (s != null) {
                if (Utils.isValidLocation(s.lat, s.lng)) {
                    withLatLong.add(s);
                    points.add(new Point(s.lat, s.lng));
                } else {
                    withoutLatLong.add(s);
                }
            }
            i2++;
        }
        ArrayList<ArrayList<SmallItem>> clusters = new ArrayList();
        int m = withLatLong.size();
        if (m > 0) {
            int[] bestK = new int[1];
            int[] index = kMeans(points.toArray(new Point[m]), bestK);
            i2 = 0;
            while (i2 < bestK[0]) {
                clusters.add(new ArrayList());
                i2++;
            }
            i2 = 0;
            while (i2 < m) {
                ((ArrayList) clusters.get(index[i2])).add(withLatLong.get(i2));
                i2++;
            }
        }
        ReverseGeocoder geocoder = new ReverseGeocoder(this.mContext);
        this.mNames = new ArrayList();
        boolean hasUnresolvedAddress = false;
        this.mClusters = new ArrayList();
        Iterator i$ = clusters.iterator();
        while (i$.hasNext()) {
            ArrayList<SmallItem> cluster = (ArrayList) i$.next();
            String name = generateName(cluster, geocoder);
            if (name != null) {
                this.mNames.add(name);
                this.mClusters.add(cluster);
            } else {
                withoutLatLong.addAll(cluster);
                hasUnresolvedAddress = true;
            }
        }
        if (withoutLatLong.size() > 0) {
            this.mNames.add(this.mNoLocationString);
            this.mClusters.add(withoutLatLong);
        }
        if (hasUnresolvedAddress) {
            Runnable anonymousClass_2 = runnable;
            locationClustering = this;
            this.mHandler.post(runnable);
        }
    }
}