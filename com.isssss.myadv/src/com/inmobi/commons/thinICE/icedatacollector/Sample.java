package com.inmobi.commons.thinICE.icedatacollector;

import android.telephony.NeighboringCellInfo;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.inmobi.commons.thinICE.cellular.CellOperatorInfo;
import com.inmobi.commons.thinICE.cellular.CellTowerInfo;
import com.inmobi.commons.thinICE.location.LocationInfo;
import com.inmobi.commons.thinICE.wifi.WifiInfo;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class Sample {
    public CellOperatorInfo cellOperator;
    public CellTowerInfo connectedCellTowerInfo;
    public WifiInfo connectedWifiAp;
    public HashMap<String, LocationInfo> lastKnownLocations;
    public long utc;
    public List<NeighboringCellInfo> visibleCellTowerInfo;
    public List<WifiInfo> visibleWifiAp;
    public int zoneOffset;

    Sample() {
        Calendar instance = Calendar.getInstance();
        this.utc = instance.getTimeInMillis();
        this.zoneOffset = instance.get(ApiEventType.API_MRAID_GET_ORIENTATION) + instance.get(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE);
    }

    public String toString() {
        Iterator it;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName()).append("[\n");
        stringBuilder.append("-- utc: ").append(this.utc).append("\n");
        stringBuilder.append("-- zoneOffset: ").append(this.zoneOffset).append("\n");
        stringBuilder.append("-- cell operator: ").append(this.cellOperator).append("\n");
        stringBuilder.append("-- connected wifi access point: ").append(this.connectedWifiAp).append("\n");
        stringBuilder.append("-- last known locations:");
        if (this.lastKnownLocations == null) {
            stringBuilder.append(" null\n");
        } else {
            stringBuilder.append("\n");
            it = this.lastKnownLocations.values().iterator();
            while (it.hasNext()) {
                stringBuilder.append("   + ").append((LocationInfo) it.next()).append("\n");
            }
        }
        stringBuilder.append("-- visible wifi aps:");
        if (this.visibleWifiAp == null) {
            stringBuilder.append(" null\n");
        } else {
            stringBuilder.append("\n");
            it = this.visibleWifiAp.iterator();
            while (it.hasNext()) {
                stringBuilder.append("   + ").append((WifiInfo) it.next()).append("\n");
            }
        }
        stringBuilder.append("-- connected serving cell tower: ").append(this.connectedCellTowerInfo).append("\n");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}