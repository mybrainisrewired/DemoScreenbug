package com.millennialmedia.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.text.TextUtils;
import com.mopub.common.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

final class AdCache {
    private static final String CACHED_AD_FILE = "ad.dat";
    private static final String CACHE_INCOMPLETE_PREFIX = "incompleteDownload_";
    private static final String CACHE_PREFIX = "nextCachedAd_";
    private static final String CACHE_PREFIX_APID = "nextCachedAd_apids";
    private static final String LOCK_FILE = "ad.lock";
    static final int PRIORITY_FETCH = 3;
    static final int PRIORITY_PRECACHE = 1;
    static final int PRIORITY_REFRESH = 2;
    private static final String PRIVATE_CACHE_DIR = ".mmsyscache";
    private static final String TAG = "AdCache";
    private static Set<String> apidListSet;
    private static String cachedVideoList;
    private static boolean cachedVideoListLoaded;
    private static Set<String> cachedVideoSet;
    private static Map<String, String> incompleteDownloadHashMap;
    private static boolean incompleteDownloadHashMapLoaded;
    static boolean isExternalEnabled;
    private static Map<String, String> nextCachedAdHashMap;
    private static boolean nextCachedAdHashMapLoaded;

    static class AnonymousClass_3 implements Runnable {
        final /* synthetic */ Context val$context;

        AnonymousClass_3(Context context) {
            this.val$context = context;
        }

        public void run() {
            MMLog.d(TAG, TAG);
            AdCache.cleanUpExpiredAds(this.val$context);
            AdCache.cleanupCache(this.val$context);
        }
    }

    static interface AdCacheTaskListener {
        void downloadCompleted(CachedAd cachedAd, boolean z);

        void downloadStart(CachedAd cachedAd);
    }

    static class Iterator {
        static final int ITERATE_ID = 0;
        static final int ITERATE_INFO = 1;
        static final int ITERATE_OBJECT = 2;

        Iterator() {
        }

        boolean callback(CachedAd ad) {
            return false;
        }

        boolean callback(String id) {
            return false;
        }

        boolean callback(String id, int type, Date expiration, String acid, long deferredViewStart, ObjectInputStream objectInputStream) {
            return false;
        }

        void done() {
        }
    }

    static class AnonymousClass_1 extends Iterator {
        final /* synthetic */ Context val$context;
        final /* synthetic */ Set val$hashSet;

        AnonymousClass_1(Context context, Set set) {
            this.val$context = context;
            this.val$hashSet = set;
        }

        boolean callback(CachedAd cachedAd) {
            if (cachedAd.acid != null && cachedAd.getType() == 1 && cachedAd.isOnDisk(this.val$context)) {
                this.val$hashSet.add(cachedAd.acid);
            }
            return true;
        }
    }

    static class AnonymousClass_5 extends Iterator {
        final /* synthetic */ Context val$context;

        AnonymousClass_5(Context context) {
            this.val$context = context;
        }

        boolean callback(CachedAd ad) {
            MMLog.d(TAG, String.format("Deleting ad %s.", new Object[]{ad.getId()}));
            ad.delete(this.val$context);
            return true;
        }
    }

    static {
        isExternalEnabled = true;
    }

    private AdCache() {
    }

    static synchronized void cachedVideoWasAdded(Context context, String acid) {
        synchronized (AdCache.class) {
            if (acid != null) {
                if (!cachedVideoListLoaded) {
                    getCachedVideoList(context);
                }
                if (cachedVideoSet == null) {
                    cachedVideoSet = new HashSet();
                }
                cachedVideoSet.add(acid);
                cachedVideoList = null;
            }
        }
    }

    static synchronized void cachedVideoWasRemoved(Context context, String acid) {
        synchronized (AdCache.class) {
            if (acid != null) {
                if (!cachedVideoListLoaded) {
                    getCachedVideoList(context);
                }
                if (cachedVideoSet != null) {
                    cachedVideoSet.remove(acid);
                    cachedVideoList = null;
                }
            }
        }
    }

    static void cleanCache(Context context) {
        ThreadUtils.execute(new AnonymousClass_3(context));
    }

    static void cleanUpExpiredAds(Context context) {
        iterateCachedAds(context, PRIORITY_PRECACHE, new Iterator() {
            boolean callback(String id, int type, Date expiration, String acid, long deferredViewStart, ObjectInputStream objectInputStream) {
                if (expiration != null && expiration.getTime() <= System.currentTimeMillis()) {
                    try {
                        CachedAd ad = (CachedAd) objectInputStream.readObject();
                        MMLog.e(TAG, String.format("Deleting expired ad %s.", new Object[]{ad.getId()}));
                    } catch (Exception e) {
                        Exception e2 = e;
                        MMLog.e(TAG, String.format("There was a problem reading the cached ad %s.", new Object[]{id}), e2);
                    }
                }
                return true;
            }
        });
    }

    static void cleanupCache(Context context) {
        cleanupInternalCache(context);
        if (isExternalStorageAvailable(context)) {
            cleanupExternalCache(context);
        }
    }

    static void cleanupDirectory(File file, long timeout) {
        File[] arr$ = file.listFiles();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            File entry = arr$[i$];
            if (entry.isFile()) {
                if (System.currentTimeMillis() - entry.lastModified() > timeout) {
                    entry.delete();
                }
            } else if (entry.isDirectory()) {
                try {
                    cleanupDirectory(entry, timeout);
                    if (entry.list() != null && entry.list().length == 0) {
                        entry.delete();
                    }
                } catch (SecurityException e) {
                    MMLog.e(TAG, "Security Exception cleaning up directory", e);
                }
            }
            i$++;
        }
    }

    private static void cleanupExternalCache(Context context) {
        File externalCacheDir = getExternalCacheDirectory(context);
        if (externalCacheDir != null && externalCacheDir.exists() && externalCacheDir.isDirectory()) {
            cleanupDirectory(externalCacheDir, HandShake.sharedHandShake(context).creativeCacheTimeout);
        }
    }

    private static void cleanupInternalCache(Context context) {
        File internalCacheDir = getInternalCacheDirectory(context);
        if (internalCacheDir != null && internalCacheDir.exists() && internalCacheDir.isDirectory()) {
            cleanupDirectory(internalCacheDir, HandShake.sharedHandShake(context).creativeCacheTimeout);
        }
    }

    static boolean deleteFile(Context context, String fileName) {
        File deleteFile = getCachedAdFile(context, fileName);
        return deleteFile != null ? deleteFile.delete() : false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean downloadComponent(java.lang.String r20_urlString, java.lang.String r21_name, java.io.File r22_path, android.content.Context r23_context) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.AdCache.downloadComponent(java.lang.String, java.lang.String, java.io.File, android.content.Context):boolean");
        /*
        r16 = android.text.TextUtils.isEmpty(r20);
        if (r16 == 0) goto L_0x0010;
    L_0x0006:
        r16 = "AdCache";
        r17 = "No Url ...";
        com.millennialmedia.android.MMLog.d(r16, r17);
        r16 = 0;
    L_0x000f:
        return r16;
    L_0x0010:
        r9 = new java.io.File;
        r0 = r22;
        r1 = r21;
        r9.<init>(r0, r1);
        r16 = "AdCache";
        r17 = "Downloading Component: %s from %s";
        r18 = 2;
        r0 = r18;
        r0 = new java.lang.Object[r0];
        r18 = r0;
        r19 = 0;
        r18[r19] = r21;
        r19 = 1;
        r18[r19] = r20;
        r17 = java.lang.String.format(r17, r18);
        com.millennialmedia.android.MMLog.v(r16, r17);
        r14 = r9.getParentFile();
        r16 = r14.exists();
        if (r16 != 0) goto L_0x0061;
    L_0x003e:
        r16 = r14.mkdirs();
        if (r16 != 0) goto L_0x0061;
    L_0x0044:
        r16 = "AdCache";
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r0 = r17;
        r17 = r0.append(r14);
        r18 = " does not exist and cannot create directory.";
        r17 = r17.append(r18);
        r17 = r17.toString();
        com.millennialmedia.android.MMLog.v(r16, r17);
        r16 = 0;
        goto L_0x000f;
    L_0x0061:
        r16 = r9.exists();
        if (r16 == 0) goto L_0x0090;
    L_0x0067:
        r16 = r9.length();
        r18 = 0;
        r16 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1));
        if (r16 <= 0) goto L_0x0090;
    L_0x0071:
        r16 = "AdCache";
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r0 = r17;
        r1 = r21;
        r17 = r0.append(r1);
        r18 = " already exists, skipping...";
        r17 = r17.append(r18);
        r17 = r17.toString();
        com.millennialmedia.android.MMLog.v(r16, r17);
        r16 = 1;
        goto L_0x000f;
    L_0x0090:
        r10 = 0;
        r12 = 0;
        r5 = -1;
        r4 = new java.net.URL;	 Catch:{ Exception -> 0x01ff }
        r0 = r20;
        r4.<init>(r0);	 Catch:{ Exception -> 0x01ff }
        r16 = 1;
        java.net.HttpURLConnection.setFollowRedirects(r16);	 Catch:{ Exception -> 0x01ff }
        r3 = r4.openConnection();	 Catch:{ Exception -> 0x01ff }
        r3 = (java.net.HttpURLConnection) r3;	 Catch:{ Exception -> 0x01ff }
        r16 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
        r0 = r16;
        r3.setConnectTimeout(r0);	 Catch:{ Exception -> 0x01ff }
        r16 = "GET";
        r0 = r16;
        r3.setRequestMethod(r0);	 Catch:{ Exception -> 0x01ff }
        r3.connect();	 Catch:{ Exception -> 0x01ff }
        r10 = r3.getInputStream();	 Catch:{ Exception -> 0x01ff }
        r16 = "Content-Length";
        r0 = r16;
        r15 = r3.getHeaderField(r0);	 Catch:{ Exception -> 0x01ff }
        if (r15 == 0) goto L_0x00c9;
    L_0x00c5:
        r5 = java.lang.Long.parseLong(r15);	 Catch:{ Exception -> 0x01ff }
    L_0x00c9:
        if (r10 != 0) goto L_0x0105;
    L_0x00cb:
        r16 = "AdCache";
        r17 = "Connection stream is null downloading %s.";
        r18 = 1;
        r0 = r18;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x01ff }
        r18 = r0;
        r19 = 0;
        r18[r19] = r21;	 Catch:{ Exception -> 0x01ff }
        r17 = java.lang.String.format(r17, r18);	 Catch:{ Exception -> 0x01ff }
        com.millennialmedia.android.MMLog.e(r16, r17);	 Catch:{ Exception -> 0x01ff }
        r16 = 0;
        if (r10 == 0) goto L_0x00e9;
    L_0x00e6:
        r10.close();	 Catch:{ IOException -> 0x00f0 }
    L_0x00e9:
        if (r12 == 0) goto L_0x000f;
    L_0x00eb:
        r12.close();	 Catch:{ IOException -> 0x00f0 }
        goto L_0x000f;
    L_0x00f0:
        r8 = move-exception;
        r16 = "AdCache";
        r17 = "Content caching error downloading component: ";
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        if (r9 == 0) goto L_0x0101;
    L_0x00fe:
        r9.delete();
    L_0x0101:
        r16 = 0;
        goto L_0x000f;
    L_0x0105:
        r13 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x01ff }
        r13.<init>(r9);	 Catch:{ Exception -> 0x01ff }
        r16 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r16;
        r2 = new byte[r0];	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
    L_0x0110:
        r11 = r10.read(r2);	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        if (r11 <= 0) goto L_0x014e;
    L_0x0116:
        r16 = 0;
        r0 = r16;
        r13.write(r2, r0, r11);	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        goto L_0x0110;
    L_0x011e:
        r7 = move-exception;
        r12 = r13;
    L_0x0120:
        r16 = "AdCache";
        r17 = "Exception downloading component %s: %s";
        r18 = 2;
        r0 = r18;
        r0 = new java.lang.Object[r0];	 Catch:{ all -> 0x01fd }
        r18 = r0;
        r19 = 0;
        r18[r19] = r21;	 Catch:{ all -> 0x01fd }
        r19 = 1;
        r18[r19] = r7;	 Catch:{ all -> 0x01fd }
        r17 = java.lang.String.format(r17, r18);	 Catch:{ all -> 0x01fd }
        com.millennialmedia.android.MMLog.e(r16, r17);	 Catch:{ all -> 0x01fd }
        if (r10 == 0) goto L_0x0140;
    L_0x013d:
        r10.close();	 Catch:{ IOException -> 0x01d3 }
    L_0x0140:
        if (r12 == 0) goto L_0x0145;
    L_0x0142:
        r12.close();	 Catch:{ IOException -> 0x01d3 }
    L_0x0145:
        if (r9 == 0) goto L_0x014a;
    L_0x0147:
        r9.delete();
    L_0x014a:
        r16 = 0;
        goto L_0x000f;
    L_0x014e:
        if (r9 == 0) goto L_0x0188;
    L_0x0150:
        r16 = r9.length();	 Catch:{ SecurityException -> 0x0194 }
        r16 = (r16 > r5 ? 1 : (r16 == r5 ? 0 : -1));
        if (r16 == 0) goto L_0x015e;
    L_0x0158:
        r16 = -1;
        r16 = (r5 > r16 ? 1 : (r5 == r16 ? 0 : -1));
        if (r16 != 0) goto L_0x0181;
    L_0x015e:
        r16 = 1;
        if (r10 == 0) goto L_0x0165;
    L_0x0162:
        r10.close();	 Catch:{ IOException -> 0x016c }
    L_0x0165:
        if (r13 == 0) goto L_0x000f;
    L_0x0167:
        r13.close();	 Catch:{ IOException -> 0x016c }
        goto L_0x000f;
    L_0x016c:
        r8 = move-exception;
        r16 = "AdCache";
        r17 = "Content caching error downloading component: ";
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        if (r9 == 0) goto L_0x017d;
    L_0x017a:
        r9.delete();
    L_0x017d:
        r16 = 0;
        goto L_0x000f;
    L_0x0181:
        r16 = "AdCache";
        r17 = "Content-Length does not match actual length.";
        com.millennialmedia.android.MMLog.e(r16, r17);	 Catch:{ SecurityException -> 0x0194 }
    L_0x0188:
        if (r10 == 0) goto L_0x018d;
    L_0x018a:
        r10.close();	 Catch:{ IOException -> 0x01be }
    L_0x018d:
        if (r13 == 0) goto L_0x0192;
    L_0x018f:
        r13.close();	 Catch:{ IOException -> 0x01be }
    L_0x0192:
        r12 = r13;
        goto L_0x0145;
    L_0x0194:
        r7 = move-exception;
        r16 = "AdCache";
        r17 = "Exception downloading component %s: ";
        r18 = 1;
        r0 = r18;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        r18 = r0;
        r19 = 0;
        r18[r19] = r21;	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        r17 = java.lang.String.format(r17, r18);	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r7);	 Catch:{ Exception -> 0x011e, all -> 0x01b1 }
        goto L_0x0188;
    L_0x01b1:
        r16 = move-exception;
        r12 = r13;
    L_0x01b3:
        if (r10 == 0) goto L_0x01b8;
    L_0x01b5:
        r10.close();	 Catch:{ IOException -> 0x01e8 }
    L_0x01b8:
        if (r12 == 0) goto L_0x01bd;
    L_0x01ba:
        r12.close();	 Catch:{ IOException -> 0x01e8 }
    L_0x01bd:
        throw r16;
    L_0x01be:
        r8 = move-exception;
        r16 = "AdCache";
        r17 = "Content caching error downloading component: ";
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        if (r9 == 0) goto L_0x01cf;
    L_0x01cc:
        r9.delete();
    L_0x01cf:
        r16 = 0;
        goto L_0x000f;
    L_0x01d3:
        r8 = move-exception;
        r16 = "AdCache";
        r17 = "Content caching error downloading component: ";
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        if (r9 == 0) goto L_0x01e4;
    L_0x01e1:
        r9.delete();
    L_0x01e4:
        r16 = 0;
        goto L_0x000f;
    L_0x01e8:
        r8 = move-exception;
        r16 = "AdCache";
        r17 = "Content caching error downloading component: ";
        r0 = r16;
        r1 = r17;
        com.millennialmedia.android.MMLog.e(r0, r1, r8);
        if (r9 == 0) goto L_0x01f9;
    L_0x01f6:
        r9.delete();
    L_0x01f9:
        r16 = 0;
        goto L_0x000f;
    L_0x01fd:
        r16 = move-exception;
        goto L_0x01b3;
    L_0x01ff:
        r7 = move-exception;
        goto L_0x0120;
        */
    }

    static boolean downloadComponentExternalStorage(String urlString, String name, Context context) {
        File externalDir = getExternalCacheDirectory(context);
        return (externalDir == null || !externalDir.isDirectory()) ? false : downloadComponent(urlString, name, externalDir, context);
    }

    static boolean downloadComponentExternalStorage(String urlString, String subDirectory, String name, Context context) {
        File externalDir = getExternalCacheDirectory(context);
        return (externalDir == null || !externalDir.isDirectory()) ? false : downloadComponent(urlString, name, new File(externalDir, subDirectory), context);
    }

    static boolean downloadComponentInternalCache(String urlString, String name, Context context) {
        File internalDir = getInternalCacheDirectory(context);
        return (internalDir == null || !internalDir.isDirectory()) ? false : downloadComponent(urlString, name, internalDir, context);
    }

    static File getCacheDirectory(Context context) {
        return isExternalStorageAvailable(context) ? getExternalCacheDirectory(context) : getInternalCacheDirectory(context);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File getCachedAdFile(android.content.Context r10_context, java.lang.String r11_id) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.AdCache.getCachedAdFile(android.content.Context, java.lang.String):java.io.File");
        /*
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8 = r8.append(r11);
        r9 = "ad.dat";
        r8 = r8.append(r9);
        r4 = r8.toString();
        r7 = isExternalStorageAvailable(r10);
        r2 = getInternalCacheDirectory(r10);
        r0 = 0;
        if (r2 == 0) goto L_0x002a;
    L_0x001e:
        r8 = r2.isDirectory();	 Catch:{ Exception -> 0x0065 }
        if (r8 == 0) goto L_0x002a;
    L_0x0024:
        r1 = new java.io.File;	 Catch:{ Exception -> 0x0065 }
        r1.<init>(r2, r4);	 Catch:{ Exception -> 0x0065 }
        r0 = r1;
    L_0x002a:
        if (r0 == 0) goto L_0x0032;
    L_0x002c:
        r8 = r0.exists();	 Catch:{ Exception -> 0x0065 }
        if (r8 != 0) goto L_0x006f;
    L_0x0032:
        if (r7 != 0) goto L_0x006f;
    L_0x0034:
        r5 = r10.getFilesDir();	 Catch:{ Exception -> 0x0065 }
        if (r5 == 0) goto L_0x006f;
    L_0x003a:
        r6 = new java.io.File;	 Catch:{ Exception -> 0x0065 }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0065 }
        r8.<init>();	 Catch:{ Exception -> 0x0065 }
        r9 = ".mmsyscache";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x0065 }
        r9 = java.io.File.separator;	 Catch:{ Exception -> 0x0065 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x0065 }
        r8 = r8.append(r4);	 Catch:{ Exception -> 0x0065 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x0065 }
        r6.<init>(r5, r8);	 Catch:{ Exception -> 0x0065 }
        r8 = r6.exists();	 Catch:{ Exception -> 0x0065 }
        if (r8 == 0) goto L_0x006f;
    L_0x005e:
        r8 = r6.isFile();	 Catch:{ Exception -> 0x0065 }
        if (r8 == 0) goto L_0x006f;
    L_0x0064:
        return r6;
    L_0x0065:
        r3 = move-exception;
        r8 = "AdCache";
        r9 = "getCachedAdFile: ";
        com.millennialmedia.android.MMLog.e(r8, r9, r3);
        r6 = 0;
        goto L_0x0064;
    L_0x006f:
        r6 = r0;
        goto L_0x0064;
        */
    }

    static synchronized String getCachedVideoList(Context context) {
        String str;
        synchronized (AdCache.class) {
            if (cachedVideoList == null) {
                if (!cachedVideoListLoaded) {
                    Set<String> hashSet = new HashSet();
                    iterateCachedAds(context, PRIORITY_REFRESH, new AnonymousClass_1(context, hashSet));
                    cachedVideoSet = hashSet;
                    cachedVideoListLoaded = true;
                }
                if (cachedVideoSet != null && cachedVideoSet.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    java.util.Iterator i$ = cachedVideoSet.iterator();
                    while (i$.hasNext()) {
                        String acid = (String) i$.next();
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append("," + acid);
                        } else {
                            stringBuilder.append(acid);
                        }
                    }
                    cachedVideoList = stringBuilder.toString();
                }
            }
            str = cachedVideoList;
        }
        return str;
    }

    static File getDownloadFile(Context context, String name) {
        File dir = getExternalCacheDirectory(context);
        return dir != null ? new File(dir, name) : null;
    }

    static File getDownloadFile(Context context, String subDirectory, String name) {
        File dir = getExternalCacheDirectory(context);
        return dir != null ? new File(dir, subDirectory + File.separator + name) : null;
    }

    static File getExternalCacheDirectory(Context context) {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        if (externalStorageDir == null) {
            return null;
        }
        File extCacheDir = new File(externalStorageDir, PRIVATE_CACHE_DIR);
        return (extCacheDir.exists() || extCacheDir.mkdirs()) ? extCacheDir : null;
    }

    static synchronized String getIncompleteDownload(Context context, String adName) {
        String str;
        synchronized (AdCache.class) {
            if (!incompleteDownloadHashMapLoaded) {
                loadIncompleteDownloadHashMap(context);
            }
            str = adName == null ? null : (String) incompleteDownloadHashMap.get(adName);
        }
        return str;
    }

    static File getInternalCacheDirectory(Context context) {
        if (context == null) {
            return null;
        }
        File cacheDir = new File(context.getFilesDir(), PRIVATE_CACHE_DIR);
        return (cacheDir == null || cacheDir.exists() || cacheDir.mkdirs()) ? cacheDir : null;
    }

    static synchronized String getNextCachedAd(Context context, String adName) {
        String str;
        synchronized (AdCache.class) {
            if (!nextCachedAdHashMapLoaded) {
                loadNextCachedAdHashMap(context);
            }
            str = adName == null ? null : (String) nextCachedAdHashMap.get(adName);
        }
        return str;
    }

    static boolean hasDownloadFile(Context context, String name) {
        File file = getDownloadFile(context, name);
        return file != null && file.exists();
    }

    static boolean isExternalMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    static boolean isExternalStorageAvailable(Context context) {
        if (context == null) {
            return false;
        }
        String storageState = Environment.getExternalStorageState();
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0 && !TextUtils.isEmpty(storageState) && storageState.equals("mounted") && isExternalEnabled;
    }

    static void iterateCachedAds(android.content.Context r21_context, int r22_mode, com.millennialmedia.android.AdCache.Iterator r23_iterator) {
        throw new UnsupportedOperationException("Method not decompiled: com.millennialmedia.android.AdCache.iterateCachedAds(android.content.Context, int, com.millennialmedia.android.AdCache$Iterator):void");
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Try/catch wrap count limit reached in com.millennialmedia.android.AdCache.iterateCachedAds(android.content.Context, int, com.millennialmedia.android.AdCache$Iterator):void
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:52)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:40)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:16)
	at jadx.core.ProcessClass.process(ProcessClass.java:22)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:209)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:133)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
	at java.lang.Thread.run(Thread.java:745)
*/
        /*
        r13 = getInternalCacheDirectory(r21);
        r8 = 0;
        r9 = 0;
        if (r13 == 0) goto L_0x0052;
    L_0x0008:
        r1 = new com.millennialmedia.android.AdCache$2;
        r1.<init>();
        r11 = r13.listFiles(r1);
        if (r11 == 0) goto L_0x0052;
    L_0x0013:
        r12 = r11;
        r0 = r12.length;
        r16 = r0;
        r15 = 0;
        r17 = r8;
    L_0x001a:
        r0 = r16;
        if (r15 >= r0) goto L_0x013c;
    L_0x001e:
        r10 = r12[r15];
        if (r10 == 0) goto L_0x0028;
    L_0x0022:
        r1 = r10.exists();	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        if (r1 != 0) goto L_0x003e;
    L_0x0028:
        if (r17 == 0) goto L_0x0138;
    L_0x002a:
        r17.close();	 Catch:{ IOException -> 0x0033 }
        r8 = 0;
    L_0x002e:
        r15 = r15 + 1;
        r17 = r8;
        goto L_0x001a;
    L_0x0033:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        r8 = r17;
        goto L_0x002e;
    L_0x003e:
        if (r22 != 0) goto L_0x0073;
    L_0x0040:
        r1 = r10.getName();	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        r0 = r23;
        r1 = r0.callback(r1);	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        if (r1 != 0) goto L_0x0061;
    L_0x004c:
        if (r17 == 0) goto L_0x013c;
    L_0x004e:
        r17.close();	 Catch:{ IOException -> 0x0056 }
        r8 = 0;
    L_0x0052:
        r23.done();
        return;
    L_0x0056:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        r8 = r17;
        goto L_0x0052;
    L_0x0061:
        if (r17 == 0) goto L_0x0138;
    L_0x0063:
        r17.close();	 Catch:{ IOException -> 0x0068 }
        r8 = 0;
        goto L_0x002e;
    L_0x0068:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        r8 = r17;
        goto L_0x002e;
    L_0x0073:
        r8 = new java.io.ObjectInputStream;	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        r1 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        r1.<init>(r10);	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        r8.<init>(r1);	 Catch:{ Exception -> 0x00ed, all -> 0x011f }
        r3 = r8.readInt();	 Catch:{ Exception -> 0x0136 }
        r4 = r8.readObject();	 Catch:{ Exception -> 0x0136 }
        r4 = (java.util.Date) r4;	 Catch:{ Exception -> 0x0136 }
        r5 = r8.readObject();	 Catch:{ Exception -> 0x0136 }
        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x0136 }
        r6 = r8.readLong();	 Catch:{ Exception -> 0x0136 }
        r1 = 1;
        r0 = r22;
        if (r0 != r1) goto L_0x00b6;
    L_0x0096:
        r2 = r10.getName();	 Catch:{ Exception -> 0x0136 }
        r1 = r23;
        r1 = r1.callback(r2, r3, r4, r5, r6, r8);	 Catch:{ Exception -> 0x0136 }
        if (r1 != 0) goto L_0x00db;
    L_0x00a2:
        r8.close();	 Catch:{ Exception -> 0x0136 }
        r8 = 0;
        if (r8 == 0) goto L_0x0052;
    L_0x00a8:
        r8.close();	 Catch:{ IOException -> 0x00ad }
        r8 = 0;
        goto L_0x0052;
    L_0x00ad:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        goto L_0x0052;
    L_0x00b6:
        r1 = r8.readObject();	 Catch:{ Exception -> 0x0136 }
        r0 = r1;
        r0 = (com.millennialmedia.android.CachedAd) r0;	 Catch:{ Exception -> 0x0136 }
        r9 = r0;
        r0 = r23;
        r1 = r0.callback(r9);	 Catch:{ Exception -> 0x0136 }
        if (r1 != 0) goto L_0x00db;
    L_0x00c6:
        r8.close();	 Catch:{ Exception -> 0x0136 }
        r8 = 0;
        if (r8 == 0) goto L_0x0052;
    L_0x00cc:
        r8.close();	 Catch:{ IOException -> 0x00d1 }
        r8 = 0;
        goto L_0x0052;
    L_0x00d1:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        goto L_0x0052;
    L_0x00db:
        if (r8 == 0) goto L_0x002e;
    L_0x00dd:
        r8.close();	 Catch:{ IOException -> 0x00e3 }
        r8 = 0;
        goto L_0x002e;
    L_0x00e3:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        goto L_0x002e;
    L_0x00ed:
        r14 = move-exception;
        r8 = r17;
    L_0x00f0:
        r1 = "AdCache";
        r2 = "There was a problem reading the cached ad %s.";
        r18 = 1;
        r0 = r18;
        r0 = new java.lang.Object[r0];	 Catch:{ all -> 0x0134 }
        r18 = r0;
        r19 = 0;
        r20 = r10.getName();	 Catch:{ all -> 0x0134 }
        r18[r19] = r20;	 Catch:{ all -> 0x0134 }
        r0 = r18;
        r2 = java.lang.String.format(r2, r0);	 Catch:{ all -> 0x0134 }
        com.millennialmedia.android.MMLog.e(r1, r2, r14);	 Catch:{ all -> 0x0134 }
        if (r8 == 0) goto L_0x002e;
    L_0x010f:
        r8.close();	 Catch:{ IOException -> 0x0115 }
        r8 = 0;
        goto L_0x002e;
    L_0x0115:
        r14 = move-exception;
        r1 = "AdCache";
        r2 = "Failed to close";
        com.millennialmedia.android.MMLog.e(r1, r2, r14);
        goto L_0x002e;
    L_0x011f:
        r1 = move-exception;
        r8 = r17;
    L_0x0122:
        if (r8 == 0) goto L_0x0128;
    L_0x0124:
        r8.close();	 Catch:{ IOException -> 0x0129 }
        r8 = 0;
    L_0x0128:
        throw r1;
    L_0x0129:
        r14 = move-exception;
        r2 = "AdCache";
        r18 = "Failed to close";
        r0 = r18;
        com.millennialmedia.android.MMLog.e(r2, r0, r14);
        goto L_0x0128;
    L_0x0134:
        r1 = move-exception;
        goto L_0x0122;
    L_0x0136:
        r14 = move-exception;
        goto L_0x00f0;
    L_0x0138:
        r8 = r17;
        goto L_0x002e;
    L_0x013c:
        r8 = r17;
        goto L_0x0052;
        */
    }

    static CachedAd load(Context context, String id) {
        if (id == null || id.equals(Preconditions.EMPTY_ARGUMENTS)) {
            return null;
        }
        ObjectInputStream objectInputStream = null;
        File cachedAdFile = getCachedAdFile(context, id);
        if (cachedAdFile == null) {
            return null;
        }
        try {
            ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(cachedAdFile));
            try {
                objectInputStream2.readInt();
                Date expiration = (Date) objectInputStream2.readObject();
                objectInputStream2.readObject();
                long deferredViewStart = objectInputStream2.readLong();
                CachedAd ad = (CachedAd) objectInputStream2.readObject();
                if (objectInputStream2 != null) {
                    try {
                        objectInputStream2.close();
                    } catch (IOException e) {
                        MMLog.e(TAG, "Failed to close", e);
                        return ad;
                    }
                }
                return ad;
            } catch (FileNotFoundException e2) {
                fe = e2;
                objectInputStream = objectInputStream2;
                MMLog.e(TAG, "There was a problem loading up the cached ad " + id + ". Ad is not on disk.", fe);
                if (objectInputStream != null) {
                    return null;
                }
                objectInputStream.close();
                return null;
            } catch (Exception e3) {
                e = e3;
                objectInputStream = objectInputStream2;
                MMLog.e(TAG, String.format("There was a problem loading up the cached ad %s.", new Object[]{id}), e);
                if (objectInputStream != null) {
                    return null;
                }
                objectInputStream.close();
                return null;
            } catch (Throwable th) {
                th = th;
                objectInputStream = objectInputStream2;
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e4) {
            FileNotFoundException fe2 = e4;
            MMLog.e(TAG, "There was a problem loading up the cached ad " + id + ". Ad is not on disk.", fe2);
            if (objectInputStream != null) {
                return null;
            }
            try {
                objectInputStream.close();
                return null;
            } catch (IOException e5) {
                MMLog.e(TAG, "Failed to close", e5);
                return null;
            }
        } catch (Exception e6) {
            Exception e7 = e6;
            MMLog.e(TAG, String.format("There was a problem loading up the cached ad %s.", new Object[]{id}), e7);
            if (objectInputStream != null) {
                return null;
            }
            try {
                objectInputStream.close();
                return null;
            } catch (IOException e8) {
                MMLog.e(TAG, "Failed to close", e8);
                return null;
            }
        }
    }

    private static void loadApidListSet(SharedPreferences settings) {
        apidListSet = new HashSet();
        String apids = settings.getString(CACHE_PREFIX_APID, null);
        if (apids != null) {
            String[] apidArray = apids.split(MMSDK.COMMA);
            if (apidArray != null && apidArray.length > 0) {
                String[] arr$ = apidArray;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    apidListSet.add(arr$[i$]);
                    i$++;
                }
            }
        }
    }

    static CachedAd loadIncompleteDownload(Context context, String adName) {
        String nextIncompleteAd = getIncompleteDownload(context, adName);
        return nextIncompleteAd == null ? null : load(context, nextIncompleteAd);
    }

    private static void loadIncompleteDownloadHashMap(Context context) {
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        incompleteDownloadHashMap = new ConcurrentHashMap();
        if (apidListSet == null) {
            loadApidListSet(settings);
        }
        java.util.Iterator it = apidListSet.iterator();
        while (it.hasNext()) {
            String apid = (String) it.next();
            String[] arr$ = MMAdImpl.getAdTypes();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String adType = arr$[i$];
                String result = settings.getString(CACHE_INCOMPLETE_PREFIX + adType + '_' + apid, null);
                if (result != null) {
                    incompleteDownloadHashMap.put(adType + '_' + apid, result);
                }
                i$++;
            }
        }
        incompleteDownloadHashMapLoaded = true;
    }

    static CachedAd loadNextCachedAd(Context context, String adName) {
        String nextAd = getNextCachedAd(context, adName);
        return (nextAd == null || nextAd.equals(Preconditions.EMPTY_ARGUMENTS)) ? null : load(context, nextAd);
    }

    private static void loadNextCachedAdHashMap(Context context) {
        SharedPreferences settings = context.getSharedPreferences("MillennialMediaSettings", 0);
        nextCachedAdHashMap = new ConcurrentHashMap();
        if (apidListSet == null) {
            loadApidListSet(settings);
        }
        java.util.Iterator it = apidListSet.iterator();
        while (it.hasNext()) {
            String apid = (String) it.next();
            String[] arr$ = MMAdImpl.getAdTypes();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String adType = arr$[i$];
                String result = settings.getString(CACHE_PREFIX + adType + '_' + apid, null);
                if (result != null) {
                    nextCachedAdHashMap.put(adType + '_' + apid, result);
                }
                i$++;
            }
        }
        nextCachedAdHashMapLoaded = true;
    }

    static void resetCache(Context context) {
        java.util.Iterator i$;
        iterateCachedAds(context, PRIORITY_REFRESH, new AnonymousClass_5(context));
        cachedVideoSet = null;
        cachedVideoList = null;
        cachedVideoListLoaded = false;
        if (nextCachedAdHashMap != null) {
            i$ = nextCachedAdHashMap.keySet().iterator();
            while (i$.hasNext()) {
                setNextCachedAd(context, (String) i$.next(), null);
            }
        }
        if (incompleteDownloadHashMap != null) {
            i$ = incompleteDownloadHashMap.keySet().iterator();
            while (i$.hasNext()) {
                setIncompleteDownload(context, i$.next(), null);
            }
        }
    }

    static boolean save(Context context, CachedAd ad) {
        Throwable th;
        File lockFile = null;
        ObjectOutputStream objectOutputStream = null;
        if (ad == null) {
            return false;
        }
        File cachedAdFile = getCachedAdFile(context, ad.getId());
        if (cachedAdFile == null) {
            return false;
        }
        MMLog.v(TAG, String.format("Saving CachedAd %s to %s", new Object[]{ad.getId(), cachedAdFile}));
        try {
            File lockFile2 = new File(cachedAdFile.getParent(), ad.getId() + LOCK_FILE);
            try {
                if (lockFile2.createNewFile()) {
                    ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(cachedAdFile));
                    try {
                        objectOutputStream2.writeInt(ad.getType());
                        objectOutputStream2.writeObject(ad.expiration);
                        objectOutputStream2.writeObject(ad.acid);
                        objectOutputStream2.writeLong(ad.deferredViewStart);
                        objectOutputStream2.writeObject(ad);
                        try {
                            lockFile2.delete();
                            if (objectOutputStream2 != null) {
                                objectOutputStream2.close();
                            }
                        } catch (IOException e) {
                            MMLog.e(TAG, "Failed to close", e);
                        }
                        if (ad.saveAssets(context)) {
                            return true;
                        } else {
                            ad.delete(context);
                            return false;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        objectOutputStream = objectOutputStream2;
                        lockFile = lockFile2;
                        MMLog.e(TAG, String.format("There was a problem saving the cached ad %s.", new Object[]{ad.getId()}), e);
                        lockFile.delete();
                        if (objectOutputStream != null) {
                            return false;
                        }
                        objectOutputStream.close();
                        return false;
                    } catch (Throwable th2) {
                        th = th2;
                        objectOutputStream = objectOutputStream2;
                        lockFile = lockFile2;
                        lockFile.delete();
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                        }
                        throw th;
                    }
                } else {
                    MMLog.v(TAG, String.format("Could not save the cached ad %s. Ad was locked.", new Object[]{ad.getId()}));
                    try {
                        lockFile2.delete();
                        if (0 != 0) {
                            null.close();
                        }
                    } catch (IOException e3) {
                        MMLog.e(TAG, "Failed to close", e3);
                    }
                    return false;
                }
            } catch (Exception e4) {
                e = e4;
                lockFile = lockFile2;
                try {
                    MMLog.e(TAG, String.format("There was a problem saving the cached ad %s.", new Object[]{ad.getId()}), e);
                    lockFile.delete();
                    if (objectOutputStream != null) {
                        return false;
                    }
                    objectOutputStream.close();
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    try {
                        lockFile.delete();
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                        }
                    } catch (IOException e5) {
                        MMLog.e(TAG, "Failed to close", e5);
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                lockFile = lockFile2;
                lockFile.delete();
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            Exception e7 = e6;
            MMLog.e(TAG, String.format("There was a problem saving the cached ad %s.", new Object[]{ad.getId()}), e7);
            try {
                lockFile.delete();
                if (objectOutputStream != null) {
                    return false;
                }
                objectOutputStream.close();
                return false;
            } catch (IOException e8) {
                MMLog.e(TAG, "Failed to close", e8);
                return false;
            }
        }
    }

    private static void saveApidListSet(Editor editor, String adName) {
        int start = adName.indexOf(95);
        if (start >= 0 && start < adName.length()) {
            String apid = adName.substring(start + 1);
            if (apid != null && !apidListSet.contains(apid)) {
                StringBuilder builder = null;
                if (!apidListSet.isEmpty()) {
                    java.util.Iterator<String> apidIter = apidListSet.iterator();
                    builder = new StringBuilder();
                    while (apidIter.hasNext()) {
                        builder.append(((String) apidIter.next()) + MMSDK.COMMA);
                    }
                }
                editor.putString(CACHE_PREFIX_APID, (builder == null ? Preconditions.EMPTY_ARGUMENTS : builder.toString()) + apid);
                apidListSet.add(apid);
            }
        }
    }

    private static void saveIncompleteDownloadHashMap(Context context, String adName) {
        if (adName != null) {
            Editor editor = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
            saveApidListSet(editor, adName);
            editor.putString(CACHE_INCOMPLETE_PREFIX + adName, (String) incompleteDownloadHashMap.get(adName));
            editor.commit();
        }
    }

    private static void saveNextCachedAdHashMapValue(Context context, String adName) {
        if (adName != null) {
            Editor editor = context.getSharedPreferences("MillennialMediaSettings", 0).edit();
            saveApidListSet(editor, adName);
            editor.putString(CACHE_PREFIX + adName, (String) nextCachedAdHashMap.get(adName));
            editor.commit();
        }
    }

    static void setEnableExternalStorage(boolean on) {
        isExternalEnabled = on;
    }

    static synchronized void setIncompleteDownload(Context context, String adName, String id) {
        synchronized (AdCache.class) {
            if (!incompleteDownloadHashMapLoaded) {
                loadIncompleteDownloadHashMap(context);
            }
            if (adName != null) {
                Map map = incompleteDownloadHashMap;
                if (id == null) {
                    id = Preconditions.EMPTY_ARGUMENTS;
                }
                map.put(adName, id);
                saveIncompleteDownloadHashMap(context, adName);
            }
        }
    }

    static synchronized void setNextCachedAd(Context context, String adName, String id) {
        synchronized (AdCache.class) {
            if (!nextCachedAdHashMapLoaded) {
                loadNextCachedAdHashMap(context);
            }
            if (adName != null) {
                Map map = nextCachedAdHashMap;
                if (id == null) {
                    id = Preconditions.EMPTY_ARGUMENTS;
                }
                map.put(adName, id);
                saveNextCachedAdHashMapValue(context, adName);
            }
        }
    }

    static boolean startDownloadTask(Context context, String adName, CachedAd ad, AdCacheTaskListener listener) {
        return AdCacheThreadPool.sharedThreadPool().startDownloadTask(context, adName, ad, listener);
    }
}