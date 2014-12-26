package com.google.android.gms.tagmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.google.android.gms.internal.gl;
import com.google.android.gms.internal.gn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class v implements c {
    private static final String XB;
    private gl Wv;
    private final Executor XC;
    private a XD;
    private int XE;
    private final Context mContext;

    class AnonymousClass_1 implements Runnable {
        final /* synthetic */ List XF;
        final /* synthetic */ long XG;

        AnonymousClass_1(List list, long j) {
            this.XF = list;
            this.XG = j;
        }

        public void run() {
            v.this.b(this.XF, this.XG);
        }
    }

    class AnonymousClass_2 implements Runnable {
        final /* synthetic */ com.google.android.gms.tagmanager.DataLayer.c.a XI;

        AnonymousClass_2(com.google.android.gms.tagmanager.DataLayer.c.a aVar) {
            this.XI = aVar;
        }

        public void run() {
            this.XI.a(v.this.ks());
        }
    }

    class AnonymousClass_3 implements Runnable {
        final /* synthetic */ String XJ;

        AnonymousClass_3(String str) {
            this.XJ = str;
        }

        public void run() {
            v.this.by(this.XJ);
        }
    }

    class a extends SQLiteOpenHelper {
        a(Context context, String str) {
            super(context, str, null, 1);
        }

        private void a(SQLiteDatabase sQLiteDatabase) {
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM datalayer WHERE 0", null);
            Set hashSet = new HashSet();
            String[] columnNames = rawQuery.getColumnNames();
            int i = 0;
            while (i < columnNames.length) {
                hashSet.add(columnNames[i]);
                i++;
            }
            rawQuery.close();
            if (!hashSet.remove("key") || !hashSet.remove("value") || !hashSet.remove("ID") || !hashSet.remove("expires")) {
                throw new SQLiteException("Database column missing");
            } else if (!hashSet.isEmpty()) {
                throw new SQLiteException("Database has extra columns");
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
                    bh.z("Error querying for table " + str);
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
                    bh.z("Error querying for table " + str);
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
            SQLiteDatabase sQLiteDatabase = null;
            try {
                sQLiteDatabase = super.getWritableDatabase();
            } catch (SQLiteException e) {
                v.this.mContext.getDatabasePath("google_tagmanager.db").delete();
            }
            return sQLiteDatabase == null ? super.getWritableDatabase() : sQLiteDatabase;
        }

        public void onCreate(SQLiteDatabase db) {
            ak.G(db.getPath());
        }

        public void onOpen(SQLiteDatabase db) {
            if (VERSION.SDK_INT < 15) {
                Cursor rawQuery = db.rawQuery("PRAGMA journal_mode=memory", null);
                rawQuery.moveToFirst();
                rawQuery.close();
            }
            if (a("datalayer", db)) {
                a(db);
            } else {
                db.execSQL(XB);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private static class b {
        final byte[] XK;
        final String Xy;

        b(String str, byte[] bArr) {
            this.Xy = str;
            this.XK = bArr;
        }

        public String toString() {
            return "KeyAndSerialized: key = " + this.Xy + " serialized hash = " + Arrays.hashCode(this.XK);
        }
    }

    static {
        XB = String.format("CREATE TABLE IF NOT EXISTS %s ( '%s' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, '%s' STRING NOT NULL, '%s' BLOB NOT NULL, '%s' INTEGER NOT NULL);", new Object[]{"datalayer", "ID", "key", "value", "expires"});
    }

    public v(Context context) {
        this(context, gn.ft(), "google_tagmanager.db", 2000, Executors.newSingleThreadExecutor());
    }

    v(Context context, gl glVar, String str, int i, Executor executor) {
        this.mContext = context;
        this.Wv = glVar;
        this.XE = i;
        this.XC = executor;
        this.XD = new a(this.mContext, str);
    }

    private SQLiteDatabase L(String str) {
        try {
            return this.XD.getWritableDatabase();
        } catch (SQLiteException e) {
            bh.z(str);
            return null;
        }
    }

    private List<a> b(List<b> list) {
        List<a> arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            b bVar = (b) it.next();
            arrayList.add(new a(bVar.Xy, j(bVar.XK)));
        }
        return arrayList;
    }

    private synchronized void b(List<b> list, long j) {
        try {
            long currentTimeMillis = this.Wv.currentTimeMillis();
            u(currentTimeMillis);
            cb(list.size());
            c(list, currentTimeMillis + j);
            kv();
        } catch (Throwable th) {
        }
    }

    private void by(String str) {
        SQLiteDatabase L = L("Error opening database for clearKeysWithPrefix.");
        if (L != null) {
            try {
                bh.y("Cleared " + L.delete("datalayer", "key = ? OR key LIKE ?", new String[]{str, str + ".%"}) + " items");
                kv();
            } catch (SQLiteException e) {
                bh.z("Error deleting entries with key prefix: " + str + " (" + e + ").");
                kv();
            }
        }
    }

    private List<b> c(List<a> list) {
        List<b> arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            arrayList.add(new b(aVar.Xy, j(aVar.Xz)));
        }
        return arrayList;
    }

    private void c(List<b> list, long j) {
        SQLiteDatabase L = L("Error opening database for writeEntryToDatabase.");
        if (L != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                b bVar = (b) it.next();
                ContentValues contentValues = new ContentValues();
                contentValues.put("expires", Long.valueOf(j));
                contentValues.put("key", bVar.Xy);
                contentValues.put("value", bVar.XK);
                L.insert("datalayer", null, contentValues);
            }
        }
    }

    private void cb(int i) {
        int ku = ku() - this.XE + i;
        if (ku > 0) {
            List cc = cc(ku);
            bh.x("DataLayer store full, deleting " + cc.size() + " entries to make room.");
            g((String[]) cc.toArray(new String[0]));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> cc(int r14) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.v.cc(int):java.util.List<java.lang.String>");
        /*
        r13 = this;
        r10 = 0;
        r9 = new java.util.ArrayList;
        r9.<init>();
        if (r14 > 0) goto L_0x000f;
    L_0x0008:
        r0 = "Invalid maxEntries specified. Skipping.";
        com.google.android.gms.tagmanager.bh.z(r0);
        r0 = r9;
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = "Error opening database for peekEntryIds.";
        r0 = r13.L(r0);
        if (r0 != 0) goto L_0x0019;
    L_0x0017:
        r0 = r9;
        goto L_0x000e;
    L_0x0019:
        r1 = "datalayer";
        r2 = 1;
        r2 = new java.lang.String[r2];	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r3 = 0;
        r4 = "ID";
        r2[r3] = r4;	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = "%s ASC";
        r8 = 1;
        r8 = new java.lang.Object[r8];	 Catch:{ SQLiteException -> 0x005c, all -> 0x007e }
        r11 = 0;
        r12 = "ID";
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
        r3 = "Error in peekEntries fetching entryIds: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0086 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x0086 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0086 }
        r0 = r0.toString();	 Catch:{ all -> 0x0086 }
        com.google.android.gms.tagmanager.bh.z(r0);	 Catch:{ all -> 0x0086 }
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

    private void g(String[] strArr) {
        if (strArr != null && strArr.length != 0) {
            SQLiteDatabase L = L("Error opening database for deleteEntries.");
            if (L != null) {
                try {
                    L.delete("datalayer", String.format("%s in (%s)", new Object[]{"ID", TextUtils.join(",", Collections.nCopies(strArr.length, "?"))}), strArr);
                } catch (SQLiteException e) {
                    bh.z("Error deleting entries " + Arrays.toString(strArr));
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.Object j(byte[] r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.v.j(byte[]):java.lang.Object");
        /*
        r5 = this;
        r0 = 0;
        r2 = new java.io.ByteArrayInputStream;
        r2.<init>(r6);
        r1 = new java.io.ObjectInputStream;	 Catch:{ IOException -> 0x0018, ClassNotFoundException -> 0x0025, all -> 0x0032 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x0018, ClassNotFoundException -> 0x0025, all -> 0x0032 }
        r0 = r1.readObject();	 Catch:{ IOException -> 0x0045, ClassNotFoundException -> 0x0043, all -> 0x0041 }
        if (r1 == 0) goto L_0x0014;
    L_0x0011:
        r1.close();	 Catch:{ IOException -> 0x0047 }
    L_0x0014:
        r2.close();	 Catch:{ IOException -> 0x0047 }
    L_0x0017:
        return r0;
    L_0x0018:
        r1 = move-exception;
        r1 = r0;
    L_0x001a:
        if (r1 == 0) goto L_0x001f;
    L_0x001c:
        r1.close();	 Catch:{ IOException -> 0x0023 }
    L_0x001f:
        r2.close();	 Catch:{ IOException -> 0x0023 }
        goto L_0x0017;
    L_0x0023:
        r1 = move-exception;
        goto L_0x0017;
    L_0x0025:
        r1 = move-exception;
        r1 = r0;
    L_0x0027:
        if (r1 == 0) goto L_0x002c;
    L_0x0029:
        r1.close();	 Catch:{ IOException -> 0x0030 }
    L_0x002c:
        r2.close();	 Catch:{ IOException -> 0x0030 }
        goto L_0x0017;
    L_0x0030:
        r1 = move-exception;
        goto L_0x0017;
    L_0x0032:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x0036:
        if (r1 == 0) goto L_0x003b;
    L_0x0038:
        r1.close();	 Catch:{ IOException -> 0x003f }
    L_0x003b:
        r2.close();	 Catch:{ IOException -> 0x003f }
    L_0x003e:
        throw r0;
    L_0x003f:
        r1 = move-exception;
        goto L_0x003e;
    L_0x0041:
        r0 = move-exception;
        goto L_0x0036;
    L_0x0043:
        r3 = move-exception;
        goto L_0x0027;
    L_0x0045:
        r3 = move-exception;
        goto L_0x001a;
    L_0x0047:
        r1 = move-exception;
        goto L_0x0017;
        */
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] j(java.lang.Object r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.tagmanager.v.j(java.lang.Object):byte[]");
        /*
        r5 = this;
        r0 = 0;
        r2 = new java.io.ByteArrayOutputStream;
        r2.<init>();
        r1 = new java.io.ObjectOutputStream;	 Catch:{ IOException -> 0x001b, all -> 0x0028 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x001b, all -> 0x0028 }
        r1.writeObject(r6);	 Catch:{ IOException -> 0x0039, all -> 0x0037 }
        r0 = r2.toByteArray();	 Catch:{ IOException -> 0x0039, all -> 0x0037 }
        if (r1 == 0) goto L_0x0017;
    L_0x0014:
        r1.close();	 Catch:{ IOException -> 0x003b }
    L_0x0017:
        r2.close();	 Catch:{ IOException -> 0x003b }
    L_0x001a:
        return r0;
    L_0x001b:
        r1 = move-exception;
        r1 = r0;
    L_0x001d:
        if (r1 == 0) goto L_0x0022;
    L_0x001f:
        r1.close();	 Catch:{ IOException -> 0x0026 }
    L_0x0022:
        r2.close();	 Catch:{ IOException -> 0x0026 }
        goto L_0x001a;
    L_0x0026:
        r1 = move-exception;
        goto L_0x001a;
    L_0x0028:
        r1 = move-exception;
        r4 = r1;
        r1 = r0;
        r0 = r4;
    L_0x002c:
        if (r1 == 0) goto L_0x0031;
    L_0x002e:
        r1.close();	 Catch:{ IOException -> 0x0035 }
    L_0x0031:
        r2.close();	 Catch:{ IOException -> 0x0035 }
    L_0x0034:
        throw r0;
    L_0x0035:
        r1 = move-exception;
        goto L_0x0034;
    L_0x0037:
        r0 = move-exception;
        goto L_0x002c;
    L_0x0039:
        r3 = move-exception;
        goto L_0x001d;
    L_0x003b:
        r1 = move-exception;
        goto L_0x001a;
        */
    }

    private List<a> ks() {
        u(this.Wv.currentTimeMillis());
        List<a> b = b(kt());
        kv();
        return b;
    }

    private List<b> kt() {
        SQLiteDatabase L = L("Error opening database for loadSerialized.");
        List<b> arrayList = new ArrayList();
        if (L == null) {
            return arrayList;
        }
        Cursor query = L.query("datalayer", new String[]{"key", "value"}, null, null, null, null, "ID", null);
        while (query.moveToNext()) {
            arrayList.add(new b(query.getString(0), query.getBlob(1)));
        }
        query.close();
        return arrayList;
    }

    private int ku() {
        int i = 0;
        SQLiteDatabase L = L("Error opening database for getNumStoredEntries.");
        if (L != null) {
            try {
                Cursor rawQuery = L.rawQuery("SELECT COUNT(*) from datalayer", null);
                if (rawQuery.moveToFirst()) {
                    i = (int) rawQuery.getLong(0);
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
            } catch (SQLiteException e) {
                try {
                    bh.z("Error getting numStoredEntries");
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

    private void kv() {
        try {
            this.XD.close();
        } catch (SQLiteException e) {
        }
    }

    private void u(long j) {
        SQLiteDatabase L = L("Error opening database for deleteOlderThan.");
        if (L != null) {
            try {
                bh.y("Deleted " + L.delete("datalayer", "expires <= ?", new String[]{Long.toString(j)}) + " expired items");
            } catch (SQLiteException e) {
                bh.z("Error deleting old entries.");
            }
        }
    }

    public void a(com.google.android.gms.tagmanager.DataLayer.c.a aVar) {
        this.XC.execute(new AnonymousClass_2(aVar));
    }

    public void a(List<a> list, long j) {
        this.XC.execute(new AnonymousClass_1(c(list), j));
    }

    public void bx(String str) {
        this.XC.execute(new AnonymousClass_3(str));
    }
}