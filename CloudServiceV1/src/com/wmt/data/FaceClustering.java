package com.wmt.data;

import android.content.Context;
import com.wmt.data.MediaSet.ItemConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public class FaceClustering extends Clustering {
    private static final String TAG = "FaceClustering";
    private ArrayList<ArrayList<Path>> mClusters;
    private String[] mNames;
    private String mUntaggedString;

    class AnonymousClass_1 implements ItemConsumer {
        final /* synthetic */ TreeMap val$map;
        final /* synthetic */ ArrayList val$untagged;

        AnonymousClass_1(ArrayList arrayList, TreeMap treeMap) {
            this.val$untagged = arrayList;
            this.val$map = treeMap;
        }

        public void consume(int index, MediaItem item) {
            Path path = item.getPath();
            Face[] faces = item.getFaces();
            if (faces == null || faces.length == 0) {
                this.val$untagged.add(path);
            } else {
                int j = 0;
                while (j < faces.length) {
                    Face key = faces[j];
                    ArrayList<Path> list = (ArrayList) this.val$map.get(key);
                    if (list == null) {
                        list = new ArrayList();
                        this.val$map.put(key, list);
                    }
                    list.add(path);
                    j++;
                }
            }
        }
    }

    public FaceClustering(Context context) {
        this.mUntaggedString = DataManager.UnknownPeople;
    }

    public ArrayList<Path> getCluster(int index) {
        return (ArrayList) this.mClusters.get(index);
    }

    public String getClusterName(int index) {
        return this.mNames[index];
    }

    public int getNumberOfClusters() {
        return this.mClusters.size();
    }

    public void run(MediaSet baseSet) {
        TreeMap<Face, ArrayList<Path>> map = new TreeMap();
        ArrayList<Path> untagged = new ArrayList();
        baseSet.enumerateTotalMediaItems(new AnonymousClass_1(untagged, map));
        int m = map.size();
        this.mClusters = new ArrayList();
        this.mNames = new String[((untagged.size() > 0 ? 1 : 0) + m)];
        int i = 0;
        Iterator i$ = map.entrySet().iterator();
        while (i$.hasNext()) {
            Entry<Face, ArrayList<Path>> entry = (Entry) i$.next();
            int i2 = i + 1;
            this.mNames[i] = ((Face) entry.getKey()).getName();
            this.mClusters.add(entry.getValue());
            i = i2;
        }
        if (untagged.size() > 0) {
            i2 = i + 1;
            this.mNames[i] = this.mUntaggedString;
            this.mClusters.add(untagged);
        }
    }
}