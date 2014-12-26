package org.dom4j.tree;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Map;
import org.dom4j.Namespace;

public class NamespaceCache {
    private static final String CONCURRENTREADERHASHMAP_CLASS = "EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap";
    protected static Map cache;
    protected static Map noPrefixCache;

    static {
        try {
            Constructor construct = Class.forName("java.util.concurrent.ConcurrentHashMap").getConstructor(new Class[]{Integer.TYPE, Float.TYPE, Integer.TYPE});
            cache = (Map) construct.newInstance(new Object[]{new Integer(11), new Float(0.75f), new Integer(1)});
            noPrefixCache = (Map) construct.newInstance(new Object[]{new Integer(11), new Float(0.75f), new Integer(1)});
        } catch (Throwable th) {
            try {
                Class clazz = Class.forName(CONCURRENTREADERHASHMAP_CLASS);
                cache = (Map) clazz.newInstance();
                noPrefixCache = (Map) clazz.newInstance();
            } catch (Throwable th2) {
                cache = new ConcurrentReaderHashMap();
                noPrefixCache = new ConcurrentReaderHashMap();
            }
        }
    }

    protected Namespace createNamespace(String prefix, String uri) {
        return new Namespace(prefix, uri);
    }

    public Namespace get(String uri) {
        WeakReference ref = (WeakReference) noPrefixCache.get(uri);
        Namespace answer = null;
        if (ref != null) {
            answer = ref.get();
        }
        if (answer == null) {
            synchronized (noPrefixCache) {
                ref = (WeakReference) noPrefixCache.get(uri);
                if (ref != null) {
                    answer = (Namespace) ref.get();
                }
                if (answer == null) {
                    answer = createNamespace("", uri);
                    noPrefixCache.put(uri, new WeakReference(answer));
                }
            }
        }
        return answer;
    }

    public Namespace get(String prefix, String uri) {
        Map uriCache = getURICache(uri);
        WeakReference ref = (WeakReference) uriCache.get(prefix);
        Namespace answer = null;
        if (ref != null) {
            answer = ref.get();
        }
        if (answer == null) {
            synchronized (uriCache) {
                ref = (WeakReference) uriCache.get(prefix);
                if (ref != null) {
                    answer = (Namespace) ref.get();
                }
                if (answer == null) {
                    answer = createNamespace(prefix, uri);
                    uriCache.put(prefix, new WeakReference(answer));
                }
            }
        }
        return answer;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.util.Map getURICache(java.lang.String r6_uri) {
        throw new UnsupportedOperationException("Method not decompiled: org.dom4j.tree.NamespaceCache.getURICache(java.lang.String):java.util.Map");
        /*
        r5 = this;
        r3 = cache;
        r1 = r3.get(r6);
        r1 = (java.util.Map) r1;
        if (r1 != 0) goto L_0x0025;
    L_0x000a:
        r4 = cache;
        monitor-enter(r4);
        r3 = cache;	 Catch:{ all -> 0x0026 }
        r3 = r3.get(r6);	 Catch:{ all -> 0x0026 }
        r0 = r3;
        r0 = (java.util.Map) r0;	 Catch:{ all -> 0x0026 }
        r1 = r0;
        if (r1 != 0) goto L_0x0024;
    L_0x0019:
        r2 = new org.dom4j.tree.ConcurrentReaderHashMap;	 Catch:{ all -> 0x0026 }
        r2.<init>();	 Catch:{ all -> 0x0026 }
        r3 = cache;	 Catch:{ all -> 0x0029 }
        r3.put(r6, r2);	 Catch:{ all -> 0x0029 }
        r1 = r2;
    L_0x0024:
        monitor-exit(r4);	 Catch:{ all -> 0x0026 }
    L_0x0025:
        return r1;
    L_0x0026:
        r3 = move-exception;
    L_0x0027:
        monitor-exit(r4);	 Catch:{ all -> 0x0026 }
        throw r3;
    L_0x0029:
        r3 = move-exception;
        r1 = r2;
        goto L_0x0027;
        */
    }
}