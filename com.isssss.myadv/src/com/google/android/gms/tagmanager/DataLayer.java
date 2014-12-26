package com.google.android.gms.tagmanager;

import com.google.android.gms.location.LocationRequest;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataLayer {
    public static final String EVENT_KEY = "event";
    public static final Object OBJECT_NOT_PRESENT;
    static final String[] Xp;
    private static final Pattern Xq;
    private final ConcurrentHashMap<b, Integer> Xr;
    private final Map<String, Object> Xs;
    private final ReentrantLock Xt;
    private final LinkedList<Map<String, Object>> Xu;
    private final c Xv;
    private final CountDownLatch Xw;

    static final class a {
        public final String Xy;
        public final Object Xz;

        a(String str, Object obj) {
            this.Xy = str;
            this.Xz = obj;
        }

        public boolean equals(Object o) {
            if (!(o instanceof a)) {
                return false;
            }
            a o2 = (a) o;
            return this.Xy.equals(o2.Xy) && this.Xz.equals(o2.Xz);
        }

        public int hashCode() {
            return Arrays.hashCode(new Integer[]{Integer.valueOf(this.Xy.hashCode()), Integer.valueOf(this.Xz.hashCode())});
        }

        public String toString() {
            return "Key: " + this.Xy + " value: " + this.Xz.toString();
        }
    }

    static interface b {
        void y(Map<String, Object> map);
    }

    static interface c {

        public static interface a {
            void a(List<a> list);
        }

        void a(a aVar);

        void a(List<a> list, long j);

        void bx(String str);
    }

    static {
        OBJECT_NOT_PRESENT = new Object();
        Xp = "gtm.lifetime".toString().split("\\.");
        Xq = Pattern.compile("(\\d+)\\s*([smhd]?)");
    }

    DataLayer() {
        this(new c() {
            public void a(com.google.android.gms.tagmanager.DataLayer.c.a aVar) {
                aVar.a(new ArrayList());
            }

            public void a(List<a> list, long j) {
            }

            public void bx(String str) {
            }
        });
    }

    DataLayer(c persistentStore) {
        this.Xv = persistentStore;
        this.Xr = new ConcurrentHashMap();
        this.Xs = new HashMap();
        this.Xt = new ReentrantLock();
        this.Xu = new LinkedList();
        this.Xw = new CountDownLatch(1);
        ko();
    }

    private void A(Map<String, Object> map) {
        this.Xt.lock();
        this.Xu.offer(map);
        if (this.Xt.getHoldCount() == 1) {
            kp();
        }
        B(map);
        this.Xt.unlock();
    }

    private void B(Map<String, Object> map) {
        Long C = C(map);
        if (C != null) {
            List E = E(map);
            E.remove("gtm.lifetime");
            this.Xv.a(E, C.longValue());
        }
    }

    private Long C(Map<String, Object> map) {
        Object D = D(map);
        return D == null ? null : bw(D.toString());
    }

    private Object D(Map<String, Object> map) {
        Object obj;
        String[] strArr = Xp;
        int length = strArr.length;
        int i = 0;
        Map<String, Object> map2 = map;
        while (i < length) {
            Object obj2 = strArr[i];
            if (!(obj instanceof Map)) {
                return null;
            }
            i++;
            obj = ((Map) obj).get(obj2);
        }
        return obj;
    }

    private List<a> E(Map<String, Object> map) {
        Object arrayList = new ArrayList();
        a(map, Preconditions.EMPTY_ARGUMENTS, arrayList);
        return arrayList;
    }

    private void F(Map<String, Object> map) {
        synchronized (this.Xs) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                a(c(str, map.get(str)), this.Xs);
            }
        }
        G(map);
    }

    private void G(Map<String, Object> map) {
        Iterator it = this.Xr.keySet().iterator();
        while (it.hasNext()) {
            ((b) it.next()).y(map);
        }
    }

    private void a(Map<String, Object> map, String str, Collection<a> collection) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String str2 = str + (str.length() == 0 ? Preconditions.EMPTY_ARGUMENTS : ".") + ((String) entry.getKey());
            if (entry.getValue() instanceof Map) {
                a((Map) entry.getValue(), str2, collection);
            } else if (!str2.equals("gtm.lifetime")) {
                collection.add(new a(str2, entry.getValue()));
            }
        }
    }

    static Long bw(String str) {
        Matcher matcher = Xq.matcher(str);
        if (matcher.matches()) {
            long parseLong;
            try {
                parseLong = Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                bh.z("illegal number in _lifetime value: " + str);
                parseLong = 0;
            }
            if (parseLong <= 0) {
                bh.x("non-positive _lifetime: " + str);
                return null;
            } else {
                String group = matcher.group(MMAdView.TRANSITION_UP);
                if (group.length() == 0) {
                    return Long.valueOf(parseLong);
                }
                switch (group.charAt(0)) {
                    case LocationRequest.PRIORITY_HIGH_ACCURACY:
                        return Long.valueOf((((parseLong * 1000) * 60) * 60) * 24);
                    case LocationRequest.PRIORITY_LOW_POWER:
                        return Long.valueOf(((parseLong * 1000) * 60) * 60);
                    case 'm':
                        return Long.valueOf((parseLong * 1000) * 60);
                    case 's':
                        return Long.valueOf(parseLong * 1000);
                    default:
                        bh.z("unknown units in _lifetime: " + str);
                        return null;
                }
            }
        } else {
            bh.x("unknown _lifetime: " + str);
            return null;
        }
    }

    private void ko() {
        this.Xv.a(new a() {
            public void a(List<a> list) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    a aVar = (a) it.next();
                    DataLayer.this.A(DataLayer.this.c(aVar.Xy, aVar.Xz));
                }
                DataLayer.this.Xw.countDown();
            }
        });
    }

    private void kp() {
        int i = 0;
        while (true) {
            Map map = (Map) this.Xu.poll();
            if (map != null) {
                F(map);
                int i2 = i + 1;
                if (i2 > 500) {
                    this.Xu.clear();
                    throw new RuntimeException("Seems like an infinite loop of pushing to the data layer");
                } else {
                    i = i2;
                }
            } else {
                return;
            }
        }
    }

    public static List<Object> listOf(Object... objects) {
        List<Object> arrayList = new ArrayList();
        int i = 0;
        while (i < objects.length) {
            arrayList.add(objects[i]);
            i++;
        }
        return arrayList;
    }

    public static Map<String, Object> mapOf(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("expected even number of key-value pairs");
        }
        Map<String, Object> hashMap = new HashMap();
        int i = 0;
        while (i < objects.length) {
            if (objects[i] instanceof String) {
                hashMap.put((String) objects[i], objects[i + 1]);
                i += 2;
            } else {
                throw new IllegalArgumentException("key is not a string: " + objects[i]);
            }
        }
        return hashMap;
    }

    void a(b bVar) {
        this.Xr.put(bVar, Integer.valueOf(0));
    }

    void a(List<Object> list, List<Object> list2) {
        while (list2.size() < list.size()) {
            list2.add(null);
        }
        int i = 0;
        while (i < list.size()) {
            Object obj = list.get(i);
            if (obj instanceof List) {
                if (!list2.get(i) instanceof List) {
                    list2.set(i, new ArrayList());
                }
                a((List) obj, (List) list2.get(i));
            } else if (obj instanceof Map) {
                if (!list2.get(i) instanceof Map) {
                    list2.set(i, new HashMap());
                }
                a((Map) obj, (Map) list2.get(i));
            } else if (obj != OBJECT_NOT_PRESENT) {
                list2.set(i, obj);
            }
            i++;
        }
    }

    void a(Map<String, Object> map, Map<String, Object> map2) {
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Object obj = map.get(str);
            if (obj instanceof List) {
                if (!map2.get(str) instanceof List) {
                    map2.put(str, new ArrayList());
                }
                a((List) obj, (List) map2.get(str));
            } else if (obj instanceof Map) {
                if (!map2.get(str) instanceof Map) {
                    map2.put(str, new HashMap());
                }
                a((Map) obj, (Map) map2.get(str));
            } else {
                map2.put(str, obj);
            }
        }
    }

    void bv(String str) {
        push(str, null);
        this.Xv.bx(str);
    }

    Map<String, Object> c(String str, Object obj) {
        Map hashMap = new HashMap();
        String[] split = str.toString().split("\\.");
        int i = 0;
        Map map = hashMap;
        while (i < split.length - 1) {
            HashMap hashMap2 = new HashMap();
            map.put(split[i], hashMap2);
            i++;
            HashMap hashMap3 = hashMap2;
        }
        map.put(split[split.length - 1], obj);
        return hashMap;
    }

    public Object get(String key) {
        synchronized (this.Xs) {
            Object obj;
            Map map = this.Xs;
            String[] split = key.split("\\.");
            int length = split.length;
            Map map2 = map;
            int i = 0;
            while (i < length) {
                Object obj2 = split[i];
                if (obj instanceof Map) {
                    obj2 = ((Map) obj).get(obj2);
                    if (obj2 == null) {
                        return null;
                    } else {
                        i++;
                        obj = obj2;
                    }
                } else {
                    return null;
                }
            }
            return obj;
        }
    }

    public void push(String key, Object value) {
        push(c(key, value));
    }

    public void push(Map<String, Object> update) {
        try {
            this.Xw.await();
        } catch (InterruptedException e) {
            bh.z("DataLayer.push: unexpected InterruptedException");
        }
        A(update);
    }

    public void pushEvent(String eventName, Map<String, Object> update) {
        Map hashMap = new HashMap(update);
        hashMap.put(EVENT_KEY, eventName);
        push(hashMap);
    }
}