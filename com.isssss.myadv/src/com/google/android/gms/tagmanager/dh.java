package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.d.a;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class dh {
    private static final Object aaF;
    private static Long aaG;
    private static Double aaH;
    private static dg aaI;
    private static String aaJ;
    private static Boolean aaK;
    private static List<Object> aaL;
    private static Map<Object, Object> aaM;
    private static a aaN;

    static {
        aaF = null;
        aaG = new Long(0);
        aaH = new Double(0.0d);
        aaI = dg.w(0);
        aaJ = new String(Preconditions.EMPTY_ARGUMENTS);
        aaK = new Boolean(false);
        aaL = new ArrayList(0);
        aaM = new HashMap();
        aaN = r(aaJ);
    }

    public static a bX(String str) {
        a aVar = new a();
        aVar.type = 5;
        aVar.fS = str;
        return aVar;
    }

    private static dg bY(String str) {
        try {
            return dg.bW(str);
        } catch (NumberFormatException e) {
            bh.w("Failed to convert '" + str + "' to a number.");
            return aaI;
        }
    }

    private static Long bZ(String str) {
        dg bY = bY(str);
        return bY == aaI ? aaG : Long.valueOf(bY.longValue());
    }

    private static Double ca(String str) {
        dg bY = bY(str);
        return bY == aaI ? aaH : Double.valueOf(bY.doubleValue());
    }

    private static Boolean cb(String str) {
        return "true".equalsIgnoreCase(str) ? Boolean.TRUE : "false".equalsIgnoreCase(str) ? Boolean.FALSE : aaK;
    }

    private static double getDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        bh.w("getDouble received non-Number");
        return 0.0d;
    }

    public static String j(a aVar) {
        return m(o(aVar));
    }

    public static dg k(a aVar) {
        return n(o(aVar));
    }

    public static Long l(a aVar) {
        return o(o(aVar));
    }

    public static Object lN() {
        return aaF;
    }

    public static Long lO() {
        return aaG;
    }

    public static Double lP() {
        return aaH;
    }

    public static Boolean lQ() {
        return aaK;
    }

    public static dg lR() {
        return aaI;
    }

    public static String lS() {
        return aaJ;
    }

    public static a lT() {
        return aaN;
    }

    public static Double m(a aVar) {
        return p(o(aVar));
    }

    public static String m(Object obj) {
        return obj == null ? aaJ : obj.toString();
    }

    public static dg n(Object obj) {
        return obj instanceof dg ? (dg) obj : t(obj) ? dg.w(u(obj)) : s(obj) ? dg.a(Double.valueOf(getDouble(obj))) : bY(m(obj));
    }

    public static Boolean n(a aVar) {
        return q(o(aVar));
    }

    public static Long o(Object obj) {
        return t(obj) ? Long.valueOf(u(obj)) : bZ(m(obj));
    }

    public static Object o(a aVar) {
        int i = 0;
        if (aVar == null) {
            return aaF;
        }
        a[] aVarArr;
        int length;
        switch (aVar.type) {
            case MMAdView.TRANSITION_FADE:
                return aVar.fN;
            case MMAdView.TRANSITION_UP:
                ArrayList arrayList = new ArrayList(aVar.fO.length);
                aVarArr = aVar.fO;
                length = aVarArr.length;
                while (i < length) {
                    Object o = o(aVarArr[i]);
                    if (o == aaF) {
                        return aaF;
                    }
                    arrayList.add(o);
                    i++;
                }
                return arrayList;
            case MMAdView.TRANSITION_DOWN:
                if (aVar.fP.length != aVar.fQ.length) {
                    bh.w("Converting an invalid value to object: " + aVar.toString());
                    return aaF;
                } else {
                    Map hashMap = new HashMap(aVar.fQ.length);
                    while (i < aVar.fP.length) {
                        Object o2 = o(aVar.fP[i]);
                        Object o3 = o(aVar.fQ[i]);
                        if (o2 != aaF && o3 != aaF) {
                            hashMap.put(o2, o3);
                            i++;
                        }
                        return aaF;
                    }
                    return hashMap;
                }
            case MMAdView.TRANSITION_RANDOM:
                bh.w("Trying to convert a macro reference to object");
                return aaF;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                bh.w("Trying to convert a function id to object");
                return aaF;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return Long.valueOf(aVar.fT);
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                StringBuffer stringBuffer = new StringBuffer();
                aVarArr = aVar.fV;
                length = aVarArr.length;
                while (i < length) {
                    String j = j(aVarArr[i]);
                    if (j == aaJ) {
                        return aaF;
                    }
                    stringBuffer.append(j);
                    i++;
                }
                return stringBuffer.toString();
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return Boolean.valueOf(aVar.fU);
            default:
                bh.w("Failed to convert a value of type: " + aVar.type);
                return aaF;
        }
    }

    public static Double p(Object obj) {
        return s(obj) ? Double.valueOf(getDouble(obj)) : ca(m(obj));
    }

    public static Boolean q(Object obj) {
        return obj instanceof Boolean ? (Boolean) obj : cb(m(obj));
    }

    public static a r(Object obj) {
        boolean z = false;
        a aVar = new a();
        if (obj instanceof a) {
            return (a) obj;
        }
        if (obj instanceof String) {
            aVar.type = 1;
            aVar.fN = (String) obj;
        } else if (obj instanceof List) {
            aVar.type = 2;
            List list = (List) obj;
            arrayList = new ArrayList(list.size());
            Iterator it = list.iterator();
            z = false;
            while (it.hasNext()) {
                a r = r(it.next());
                if (r == aaN) {
                    return aaN;
                }
                z = z || r.fX;
                arrayList.add(r);
                z = z;
            }
            aVar.fO = (a[]) arrayList.toArray(new a[0]);
            z = z;
        } else if (obj instanceof Map) {
            aVar.type = 3;
            Set entrySet = ((Map) obj).entrySet();
            arrayList = new ArrayList(entrySet.size());
            List arrayList2 = new ArrayList(entrySet.size());
            Iterator it2 = entrySet.iterator();
            z = false;
            while (it2.hasNext()) {
                Entry entry = (Entry) it2.next();
                a r2 = r(entry.getKey());
                a r3 = r(entry.getValue());
                if (r2 != aaN && r3 != aaN) {
                    z = z || r2.fX || r3.fX;
                    arrayList.add(r2);
                    arrayList2.add(r3);
                    z = z;
                }
                return aaN;
            }
            aVar.fP = (a[]) arrayList.toArray(new a[0]);
            aVar.fQ = (a[]) arrayList2.toArray(new a[0]);
            z = z;
        } else if (s(obj)) {
            aVar.type = 1;
            aVar.fN = obj.toString();
        } else if (t(obj)) {
            aVar.type = 6;
            aVar.fT = u(obj);
        } else if (obj instanceof Boolean) {
            aVar.type = 8;
            aVar.fU = ((Boolean) obj).booleanValue();
        } else {
            bh.w("Converting to Value from unknown object type: " + (obj == null ? "null" : obj.getClass().toString()));
            return aaN;
        }
        aVar.fX = z;
        return aVar;
    }

    private static boolean s(Object obj) {
        return obj instanceof Double || obj instanceof Float || (obj instanceof dg && ((dg) obj).lI());
    }

    private static boolean t(Object obj) {
        return obj instanceof Byte || obj instanceof Short || obj instanceof Integer || obj instanceof Long || (obj instanceof dg && ((dg) obj).lJ());
    }

    private static long u(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }
        bh.w("getInt64 received non-Number");
        return 0;
    }
}