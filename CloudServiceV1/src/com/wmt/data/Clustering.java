package com.wmt.data;

import java.util.ArrayList;

public abstract class Clustering {
    public abstract ArrayList<Path> getCluster(int i);

    public abstract String getClusterName(int i);

    public abstract int getNumberOfClusters();

    public abstract void run(MediaSet mediaSet);
}