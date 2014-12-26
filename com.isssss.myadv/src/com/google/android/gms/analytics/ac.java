package com.google.android.gms.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.internal.ef;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.impl.client.DefaultHttpClient;

class ac implements d {
    private static final String vx;
    private final Context mContext;
    private final e sO;
    private i tg;
    private final String vA;
    private ab vB;
    private long vC;
    private final int vD;
    private final a vy;
    private volatile n vz;

    class a extends SQLiteOpenHelper {
        private boolean vF;
        private long vG;

        a(Context context, String str) {
            super(context, str, null, 1);
            this.vG = 0;
        }

        private void a(SQLiteDatabase sQLiteDatabase) {
            boolean z = 0;
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM hits2 WHERE 0", null);
            Set hashSet = new HashSet();
            String[] columnNames = rawQuery.getColumnNames();
            int i = 0;
            while (i < columnNames.length) {
                hashSet.add(columnNames[i]);
                i++;
            }
            rawQuery.close();
            if (hashSet.remove("hit_id") && hashSet.remove("hit_url") && hashSet.remove("hit_string") && hashSet.remove("hit_time")) {
                if (!hashSet.remove("hit_app_id")) {
                    z = 1;
                }
                if (!hashSet.isEmpty()) {
                    throw new SQLiteException("Database has extra columns");
                } else if (i != 0) {
                    sQLiteDatabase.execSQL("ALTER TABLE hits2 ADD COLUMN hit_app_id");
                }
            } else {
                throw new SQLiteException("Database column missing");
            }
        }

        private boolean a(String str, SQLiteDatabase sQLiteDatabase) {
            Cursor cursor;
            boolean z = false;
            Cursor cursor2 = null;
            try {
                Cursor query = sQLiteDatabase.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
                try {
                    boolean moveToFirst = query.moveToFirst();
                    if (query == null) {
                        return moveToFirst;
                    }
                    query.close();
                    return moveToFirst;
                } catch (SQLiteException e) {
                    cursor = query;
                    aa.z("Error querying for table " + str);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return z;
                } catch (Throwable th) {
                    th = th;
                    cursor2 = query;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e2) {
                cursor = cursor2;
                try {
                    aa.z("Error querying for table " + str);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return z;
                } catch (Throwable th2) {
                    cursor2 = cursor;
                    th = th2;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                if (cursor2 != null) {
                    cursor2.close();
                }
                throw th4;
            }
        }

        public SQLiteDatabase getWritableDatabase() {
            if (!this.vF || this.vG + 3600000 <= ac.this.tg.currentTimeMillis()) {
                SQLiteDatabase sQLiteDatabase = null;
                this.vF = true;
                this.vG = ac.this.tg.currentTimeMillis();
                try {
                    sQLiteDatabase = super.getWritableDatabase();
                } catch (SQLiteException e) {
                    ac.this.mContext.getDatabasePath(ac.this.vA).delete();
                }
                if (sQLiteDatabase == null) {
                    sQLiteDatabase = super.getWritableDatabase();
                }
                this.vF = false;
                return sQLiteDatabase;
            } else {
                throw new SQLiteException("Database creation failed");
            }
        }

        public void onCreate(SQLiteDatabase db) {
            p.G(db.getPath());
        }

        public void onOpen(SQLiteDatabase db) {
            if (VERSION.SDK_INT < 15) {
                Cursor rawQuery = db.rawQuery("PRAGMA journal_mode=memory", null);
                rawQuery.moveToFirst();
                rawQuery.close();
            }
            if (a("hits2", db)) {
                a(db);
            } else {
                db.execSQL(vx);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    static {
        vx = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' INTEGER NOT NULL, '%s' TEXT NOT NULL, '%s' TEXT NOT NULL, '%s' INTEGER);", new Object[]{"hits2", "hit_id", "hit_time", "hit_url", "hit_string", "hit_app_id"});
    }

    ac(e eVar, Context context) {
        this(eVar, context, "google_analytics_v4.db", 2000);
    }

    ac(e eVar, Context context, String str, int i) {
        this.mContext = context.getApplicationContext();
        this.vA = str;
        this.sO = eVar;
        this.tg = new i() {
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        };
        this.vy = new a(this.mContext, this.vA);
        this.vz = new ah(new DefaultHttpClient(), this.mContext);
        this.vC = 0;
        this.vD = i;
    }

    private SQLiteDatabase L(String str) {
        try {
            return this.vy.getWritableDatabase();
        } catch (SQLiteException e) {
            aa.z(str);
            return null;
        }
    }

    private void a(Map<String, String> map, long j, String str) {
        SQLiteDatabase L = L("Error opening database for putHit");
        if (L != null) {
            long parseLong;
            ContentValues contentValues = new ContentValues();
            contentValues.put("hit_string", w(map));
            contentValues.put("hit_time", Long.valueOf(j));
            if (map.containsKey("AppUID")) {
                try {
                    parseLong = Long.parseLong((String) map.get("AppUID"));
                } catch (NumberFormatException e) {
                    parseLong = 0;
                }
            } else {
                parseLong = 0;
            }
            contentValues.put("hit_app_id", Long.valueOf(parseLong));
            if (str == null) {
                str = "http://www.google-analytics.com/collect";
            }
            if (str.length() == 0) {
                aa.z("Empty path: not sending hit");
            } else {
                contentValues.put("hit_url", str);
                try {
                    L.insert("hits2", null, contentValues);
                    this.sO.r(false);
                } catch (SQLiteException e2) {
                    aa.z("Error storing hit");
                }
            }
        }
    }

    private void a(Map<String, String> map, Collection<ef> collection) {
        String substring = "&_v".substring(1);
        if (collection != null) {
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                ef efVar = (ef) it.next();
                if ("appendVersion".equals(efVar.getId())) {
                    map.put(substring, efVar.getValue());
                    return;
                }
            }
        }
    }

    private void cV() {
        int cX = cX() - this.vD + 1;
        if (cX > 0) {
            List s = s(cX);
            aa.y("Store full, deleting " + s.size() + " hits to make room.");
            a((String[]) s.toArray(new String[0]));
        }
    }

    static String w(Map<String, String> map) {
        Iterable arrayList = new ArrayList(map.size());
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            arrayList.add(y.encode((String) entry.getKey()) + "=" + y.encode((String) entry.getValue()));
        }
        return TextUtils.join("&", arrayList);
    }

    public void a(Map<String, String> map, long j, String str, Collection<ef> collection) {
        cW();
        cV();
        a(map, collection);
        a(map, j, str);
    }

    void a(String[] strArr) {
        boolean z = true;
        if (strArr == null || strArr.length == 0) {
            aa.z("Empty hitIds passed to deleteHits.");
        } else {
            SQLiteDatabase L = L("Error opening database for deleteHits.");
            if (L != null) {
                try {
                    L.delete("hits2", String.format("HIT_ID in (%s)", new Object[]{TextUtils.join(",", Collections.nCopies(strArr.length, "?"))}), strArr);
                    e eVar = this.sO;
                    if (cX() != 0) {
                        z = false;
                    }
                    eVar.r(z);
                } catch (SQLiteException e) {
                    aa.z("Error deleting hits " + strArr);
                }
            }
        }
    }

    @Deprecated
    void b(Collection<x> collection) {
        if (collection == null || collection.isEmpty()) {
            aa.z("Empty/Null collection passed to deleteHits.");
        } else {
            String[] strArr = new String[collection.size()];
            Iterator it = collection.iterator();
            int i = 0;
            while (it.hasNext()) {
                int i2 = i + 1;
                strArr[i] = String.valueOf(((x) it.next()).cP());
                i = i2;
            }
            a(strArr);
        }
    }

    public void bW() {
        boolean z = true;
        aa.y("Dispatch running...");
        if (this.vz.ch()) {
            List t = t(ApiEventType.API_MRAID_PLAY_VIDEO);
            if (t.isEmpty()) {
                aa.y("...nothing to dispatch");
                this.sO.r(true);
            } else {
                if (this.vB == null) {
                    this.vB = new ab("_t=dispatch&_v=ma4.0.1", true);
                }
                if (cX() > t.size()) {
                    z = false;
                }
                int a = this.vz.a(t, this.vB, z);
                aa.y("sent " + a + " of " + t.size() + " hits");
                b(t.subList(0, Math.min(a, t.size())));
                if (a != t.size() || cX() <= 0) {
                    this.vB = null;
                } else {
                    GoogleAnalytics.getInstance(this.mContext).dispatchLocalHits();
                }
            }
        }
    }

    public n bX() {
        return this.vz;
    }

    int cW() {
        boolean z = true;
        long currentTimeMillis = this.tg.currentTimeMillis();
        if (currentTimeMillis <= this.vC + 86400000) {
            return 0;
        }
        this.vC = currentTimeMillis;
        SQLiteDatabase L = L("Error opening database for deleteStaleHits.");
        if (L == null) {
            return 0;
        }
        int delete = L.delete("hits2", "HIT_TIME < ?", new String[]{Long.toString(this.tg.currentTimeMillis() - 2592000000L)});
        e eVar = this.sO;
        if (cX() != 0) {
            z = false;
        }
        eVar.r(z);
        return delete;
    }

    int cX() {
        int i = 0;
        SQLiteDatabase L = L("Error opening database for getNumStoredHits.");
        if (L != null) {
            try {
                Cursor rawQuery = L.rawQuery("SELECT COUNT(*) from hits2", null);
                if (rawQuery.moveToFirst()) {
                    i = (int) rawQuery.getLong(0);
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
            } catch (SQLiteException e) {
                try {
                    aa.z("Error getting numStoredHits");
                    if (0 != 0) {
                        null.close();
                    }
                } catch (Throwable th) {
                    if (0 != 0) {
                        null.close();
                    }
                }
            }
        }
        return i;
    }

    public void j(long j) {
        boolean z = true;
        SQLiteDatabase L = L("Error opening database for clearHits");
        if (L != null) {
            if (j == 0) {
                L.delete("hits2", null, null);
            } else {
                L.delete("hits2", "hit_app_id = ?", new String[]{Long.valueOf(j).toString()});
            }
            e eVar = this.sO;
            if (cX() != 0) {
                z = false;
            }
            eVar.r(z);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    java.util.List<java.lang.String> s(int r14) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.ac.s(int):java.util.List<java.lang.String>");
        /*
        r13 = this;
        r10 = 0;
        r9 = new java.util.ArrayList;
        r9.<init>();
        if (r14 > 0) goto L_0x000f;
    L_0x0008:
        r0 = "Invalid maxHits specified. Skipping";
        com.google.android.gms.analytics.aa.z(r0);
        r0 = r9;
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = "Error opening database for peekHitIds.";
        r0 = r13.L(r0);
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        r0 = r9;
        goto L_0x000e;
    L_0x0019:
        r1 = "hits2";
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r3 = 0;
        r4 = "hit_id";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = "%s ASC";
        r8 = 1;
        r8 = new java.lang.Object[r8];	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r11 = 0;
        r12 = "hit_id";
        r8[r11] = r12;	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r7 = java.lang.String.format(r7, r8);	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r8 = java.lang.Integer.toString(r14);	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8);	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r0 = r1.moveToFirst();	 Catch:{ SQLiteException -> 0x0088 }
        if (r0 == 0) goto L_0x0055;
    L_0x0043:
        r0 = 0;
        r2 = r1.getLong(r0);	 Catch:{ SQLiteException -> 0x0088 }
        r0 = java.lang.String.valueOf(r2);	 Catch:{ SQLiteException -> 0x0088 }
        r9.add(r0);	 Catch:{ SQLiteException -> 0x0088 }
        r0 = r1.moveToNext();	 Catch:{ SQLiteException -> 0x0088 }
        if (r0 != 0) goto L_0x0043;
    L_0x0055:
        if (r1 == 0) goto L_0x005a;
    L_0x0057:
        r1.close();
    L_0x005a:
        r0 = r9;
        goto L_0x000e;
    L_0x005c:
        r0 = move-exception;
        r1 = r10;
    L_0x005e:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0086 }
        r2.<init>();	 Catch:{ all -> 0x0086 }
        r3 = "Error in peekHits fetching hitIds: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0086 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x0086 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0086 }
        r0 = r0.toString();	 Catch:{ all -> 0x0086 }
        com.google.android.gms.analytics.aa.z(r0);	 Catch:{ all -> 0x0086 }
        if (r1 == 0) goto L_0x005a;
    L_0x007a:
        r1.close();
        goto L_0x005a;
    L_0x007e:
        r0 = move-exception;
        r1 = r10;
    L_0x0080:
        if (r1 == 0) goto L_0x0085;
    L_0x0082:
        r1.close();
    L_0x0085:
        throw r0;
    L_0x0086:
        r0 = move-exception;
        goto L_0x0080;
    L_0x0088:
        r0 = move-exception;
        goto L_0x005e;
        */
    }

    public java.util.List<com.google.android.gms.analytics.x> t(int r16) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.ac.t(int):java.util.List<com.google.android.gms.analytics.x>");
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Try/catch wrap count limit reached in com.google.android.gms.analytics.ac.t(int):java.util.List<com.google.android.gms.analytics.x>
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
        r15 = this;
        r10 = new java.util.ArrayList;
        r10.<init>();
        r1 = "Error opening database for peekHits";
        r1 = r15.L(r1);
        if (r1 != 0) goto L_0x000f;
    L_0x000d:
        r1 = r10;
    L_0x000e:
        return r1;
    L_0x000f:
        r11 = 0;
        r2 = "hits2";
        r3 = 2;
        r3 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r4 = 0;
        r5 = "hit_id";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r4 = 1;
        r5 = "hit_time";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = "%s ASC";
        r9 = 1;
        r9 = new java.lang.Object[r9];	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r12 = 0;
        r13 = "hit_id";
        r9[r12] = r13;	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r8 = java.lang.String.format(r8, r9);	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r9 = java.lang.Integer.toString(r16);	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r12 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ SQLiteException -> 0x00d2, all -> 0x00f7 }
        r11 = new java.util.ArrayList;	 Catch:{ SQLiteException -> 0x0179, all -> 0x0173 }
        r11.<init>();	 Catch:{ SQLiteException -> 0x0179, all -> 0x0173 }
        r2 = r12.moveToFirst();	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        if (r2 == 0) goto L_0x005d;
    L_0x0044:
        r2 = new com.google.android.gms.analytics.x;	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        r3 = 0;
        r4 = 0;
        r4 = r12.getLong(r4);	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        r6 = 1;
        r6 = r12.getLong(r6);	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        r2.<init>(r3, r4, r6);	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        r11.add(r2);	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        r2 = r12.moveToNext();	 Catch:{ SQLiteException -> 0x017f, all -> 0x0173 }
        if (r2 != 0) goto L_0x0044;
    L_0x005d:
        if (r12 == 0) goto L_0x0062;
    L_0x005f:
        r12.close();
    L_0x0062:
        r10 = 0;
        r2 = "hits2";
        r3 = 3;
        r3 = new java.lang.String[r3];	 Catch:{ SQLiteException -> 0x0171 }
        r4 = 0;
        r5 = "hit_id";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x0171 }
        r4 = 1;
        r5 = "hit_string";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x0171 }
        r4 = 2;
        r5 = "hit_url";
        r3[r4] = r5;	 Catch:{ SQLiteException -> 0x0171 }
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = "%s ASC";
        r9 = 1;
        r9 = new java.lang.Object[r9];	 Catch:{ SQLiteException -> 0x0171 }
        r13 = 0;
        r14 = "hit_id";
        r9[r13] = r14;	 Catch:{ SQLiteException -> 0x0171 }
        r8 = java.lang.String.format(r8, r9);	 Catch:{ SQLiteException -> 0x0171 }
        r9 = java.lang.Integer.toString(r16);	 Catch:{ SQLiteException -> 0x0171 }
        r2 = r1.query(r2, r3, r4, r5, r6, r7, r8, r9);	 Catch:{ SQLiteException -> 0x0171 }
        r1 = r2.moveToFirst();	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        if (r1 == 0) goto L_0x00ca;
    L_0x0097:
        r3 = r10;
    L_0x0098:
        r0 = r2;
        r0 = (android.database.sqlite.SQLiteCursor) r0;	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = r0;
        r1 = r1.getWindow();	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = r1.getNumRows();	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        if (r1 <= 0) goto L_0x00fe;
    L_0x00a6:
        r1 = r11.get(r3);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = (com.google.android.gms.analytics.x) r1;	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r4 = 1;
        r4 = r2.getString(r4);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1.J(r4);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = r11.get(r3);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = (com.google.android.gms.analytics.x) r1;	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r4 = 2;
        r4 = r2.getString(r4);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1.K(r4);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
    L_0x00c2:
        r1 = r3 + 1;
        r3 = r2.moveToNext();	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        if (r3 != 0) goto L_0x0185;
    L_0x00ca:
        if (r2 == 0) goto L_0x00cf;
    L_0x00cc:
        r2.close();
    L_0x00cf:
        r1 = r11;
        goto L_0x000e;
    L_0x00d2:
        r1 = move-exception;
        r2 = r1;
        r3 = r11;
        r1 = r10;
    L_0x00d6:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0176 }
        r4.<init>();	 Catch:{ all -> 0x0176 }
        r5 = "Error in peekHits fetching hitIds: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0176 }
        r2 = r2.getMessage();	 Catch:{ all -> 0x0176 }
        r2 = r4.append(r2);	 Catch:{ all -> 0x0176 }
        r2 = r2.toString();	 Catch:{ all -> 0x0176 }
        com.google.android.gms.analytics.aa.z(r2);	 Catch:{ all -> 0x0176 }
        if (r3 == 0) goto L_0x000e;
    L_0x00f2:
        r3.close();
        goto L_0x000e;
    L_0x00f7:
        r1 = move-exception;
    L_0x00f8:
        if (r11 == 0) goto L_0x00fd;
    L_0x00fa:
        r11.close();
    L_0x00fd:
        throw r1;
    L_0x00fe:
        r4 = "HitString for hitId %d too large.  Hit will be deleted.";
        r1 = 1;
        r5 = new java.lang.Object[r1];	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r6 = 0;
        r1 = r11.get(r3);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = (com.google.android.gms.analytics.x) r1;	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r7 = r1.cP();	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = java.lang.Long.valueOf(r7);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r5[r6] = r1;	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        r1 = java.lang.String.format(r4, r5);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        com.google.android.gms.analytics.aa.z(r1);	 Catch:{ SQLiteException -> 0x011c, all -> 0x016e }
        goto L_0x00c2;
    L_0x011c:
        r1 = move-exception;
        r12 = r2;
    L_0x011e:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0167 }
        r2.<init>();	 Catch:{ all -> 0x0167 }
        r3 = "Error in peekHits fetching hitString: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0167 }
        r1 = r1.getMessage();	 Catch:{ all -> 0x0167 }
        r1 = r2.append(r1);	 Catch:{ all -> 0x0167 }
        r1 = r1.toString();	 Catch:{ all -> 0x0167 }
        com.google.android.gms.analytics.aa.z(r1);	 Catch:{ all -> 0x0167 }
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0167 }
        r2.<init>();	 Catch:{ all -> 0x0167 }
        r3 = 0;
        r4 = r11.iterator();	 Catch:{ all -> 0x0167 }
    L_0x0142:
        r1 = r4.hasNext();	 Catch:{ all -> 0x0167 }
        if (r1 == 0) goto L_0x015a;
    L_0x0148:
        r1 = r4.next();	 Catch:{ all -> 0x0167 }
        r1 = (com.google.android.gms.analytics.x) r1;	 Catch:{ all -> 0x0167 }
        r5 = r1.cO();	 Catch:{ all -> 0x0167 }
        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0167 }
        if (r5 == 0) goto L_0x0163;
    L_0x0158:
        if (r3 == 0) goto L_0x0162;
    L_0x015a:
        if (r12 == 0) goto L_0x015f;
    L_0x015c:
        r12.close();
    L_0x015f:
        r1 = r2;
        goto L_0x000e;
    L_0x0162:
        r3 = 1;
    L_0x0163:
        r2.add(r1);	 Catch:{ all -> 0x0167 }
        goto L_0x0142;
    L_0x0167:
        r1 = move-exception;
    L_0x0168:
        if (r12 == 0) goto L_0x016d;
    L_0x016a:
        r12.close();
    L_0x016d:
        throw r1;
    L_0x016e:
        r1 = move-exception;
        r12 = r2;
        goto L_0x0168;
    L_0x0171:
        r1 = move-exception;
        goto L_0x011e;
    L_0x0173:
        r1 = move-exception;
        r11 = r12;
        goto L_0x00f8;
    L_0x0176:
        r1 = move-exception;
        r11 = r3;
        goto L_0x00f8;
    L_0x0179:
        r1 = move-exception;
        r2 = r1;
        r3 = r12;
        r1 = r10;
        goto L_0x00d6;
    L_0x017f:
        r1 = move-exception;
        r2 = r1;
        r3 = r12;
        r1 = r11;
        goto L_0x00d6;
    L_0x0185:
        r3 = r1;
        goto L_0x0098;
        */
    }
}