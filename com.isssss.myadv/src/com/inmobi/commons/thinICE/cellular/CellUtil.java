package com.inmobi.commons.thinICE.cellular;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import com.inmobi.commons.thinICE.icedatacollector.BuildSettings;
import com.inmobi.commons.thinICE.icedatacollector.IceDataCollector;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CellUtil {
    private static final String[] a;
    private static final String[] b;
    private static final String[] c;

    static class a {
        a() {
        }

        static int a(Context context) {
            try {
                ArrayList arrayList = (ArrayList) TelephonyManager.class.getMethod("getAllCellInfo", (Class[]) 0).invoke((TelephonyManager) context.getSystemService("phone"), (Object[]) 0);
                if (arrayList != null) {
                    Object obj = arrayList.get(0);
                    obj = obj.getClass().getMethod("getCellSignalStrength", (Class[]) 0).invoke(obj, (Object[]) 0);
                    return ((Integer) obj.getClass().getMethod("getDbm", (Class[]) 0).invoke(obj, (Object[]) 0)).intValue();
                }
            } catch (Exception e) {
                th = e;
                if (BuildSettings.DEBUG) {
                    Throwable th2;
                    Log.e(IceDataCollector.TAG, "Error getting cell tower signal strength", th2);
                }
            }
            return 0;
        }
    }

    static {
        a = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
        b = new String[]{"android.permission.ACCESS_FINE_LOCATION"};
        c = new String[]{"android.permission.ACCESS_COARSE_LOCATION"};
    }

    private static int[] a(String str) {
        int[] iArr = new int[]{-1, -1};
        if (!(str == null || str.equals(Preconditions.EMPTY_ARGUMENTS))) {
            try {
                int parseInt = Integer.parseInt(str.substring(0, MMAdView.TRANSITION_DOWN));
                int parseInt2 = Integer.parseInt(str.substring(MMAdView.TRANSITION_DOWN));
                iArr[0] = parseInt;
                iArr[1] = parseInt2;
            } catch (IndexOutOfBoundsException e) {
            } catch (NumberFormatException e2) {
            }
        }
        return iArr;
    }

    public static CellOperatorInfo getCellNetworkInfo(Context context) {
        CellOperatorInfo cellOperatorInfo = new CellOperatorInfo();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        int[] a = a(telephonyManager.getNetworkOperator());
        cellOperatorInfo.currentMcc = a[0];
        cellOperatorInfo.currentMnc = a[1];
        int[] a2 = a(telephonyManager.getSimOperator());
        cellOperatorInfo.simMcc = a2[0];
        cellOperatorInfo.simMnc = a2[1];
        return cellOperatorInfo;
    }

    public static CellTowerInfo getCurrentCellTower(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        int[] a = a(telephonyManager.getNetworkOperator());
        CellLocation cellLocation = telephonyManager.getCellLocation();
        if (cellLocation == null || a[0] == -1) {
            return null;
        }
        CellTowerInfo cellTowerInfo = new CellTowerInfo();
        String valueOf = String.valueOf(a[0]);
        String valueOf2 = String.valueOf(a[1]);
        int networkId;
        int systemId;
        if (cellLocation instanceof CdmaCellLocation) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
            networkId = cdmaCellLocation.getNetworkId();
            int baseStationId = cdmaCellLocation.getBaseStationId();
            systemId = cdmaCellLocation.getSystemId();
            cellTowerInfo.signalStrength = a.a(context);
            if (!(networkId == -1 || baseStationId == -1 || systemId == -1)) {
                String toHexString = Integer.toHexString(networkId);
                String toHexString2 = Integer.toHexString(baseStationId);
                cellTowerInfo.id = valueOf + "-" + valueOf2 + "-" + toHexString + "-" + toHexString2 + "-" + Integer.toHexString(systemId);
            }
        } else {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
            networkId = gsmCellLocation.getCid();
            systemId = gsmCellLocation.getLac();
            cellTowerInfo.signalStrength = a.a(context);
            if (!(networkId == -1 || systemId == -1)) {
                String toHexString3 = Integer.toHexString(systemId);
                cellTowerInfo.id = valueOf + "-" + valueOf2 + "-" + toHexString3 + "-" + Integer.toHexString(networkId);
            }
        }
        return cellTowerInfo;
    }

    public static List<NeighboringCellInfo> getVisibleCellTower(Context context) {
        List<NeighboringCellInfo> neighboringCellInfo = ((TelephonyManager) context.getSystemService("phone")).getNeighboringCellInfo();
        if (neighboringCellInfo != null) {
            return neighboringCellInfo.size() == 0 ? null : neighboringCellInfo;
        } else {
            return null;
        }
    }

    public static List<Integer> getVisibleCellTowerIds(Context context) {
        List visibleCellTower = getVisibleCellTower(context);
        if (visibleCellTower == null || visibleCellTower.size() == 0) {
            return null;
        }
        List<Integer> arrayList = new ArrayList();
        Iterator it = visibleCellTower.iterator();
        while (it.hasNext()) {
            arrayList.add(Integer.valueOf(((NeighboringCellInfo) it.next()).getCid()));
        }
        return arrayList;
    }

    public static boolean hasGetCurrentServingCellPermission(Context context) {
        String[] strArr = a;
        int length = strArr.length;
        int i = 0;
        boolean z = true;
        while (i < length) {
            if (context.checkCallingOrSelfPermission(strArr[i]) != 0) {
                z = false;
            }
            i++;
        }
        String[] strArr2 = b;
        int length2 = strArr2.length;
        int i2 = 0;
        boolean z2 = true;
        while (i2 < length2) {
            if (context.checkCallingOrSelfPermission(strArr2[i2]) != 0) {
                z2 = false;
            }
            i2++;
        }
        return (i == 0 && i == 0) ? false : true;
    }

    public static boolean hasGetVisibleCellTowerPermission(Context context) {
        String[] strArr = c;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            if (context.checkCallingOrSelfPermission(strArr[i]) != 0) {
                return false;
            }
            i++;
        }
        return true;
    }
}