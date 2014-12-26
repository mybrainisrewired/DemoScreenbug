package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.AbstractWindowedCursor;
import android.database.CharArrayBuffer;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class DataHolder implements SafeParcelable {
    private static final Builder BP;
    public static final DataHolderCreator CREATOR;
    private final int Ah;
    private final String[] BH;
    Bundle BI;
    private final CursorWindow[] BJ;
    private final Bundle BK;
    int[] BL;
    int BM;
    private Object BN;
    private boolean BO;
    boolean mClosed;
    private final int xH;

    public static class Builder {
        private final String[] BH;
        private final ArrayList<HashMap<String, Object>> BQ;
        private final String BR;
        private final HashMap<Object, Integer> BS;
        private boolean BT;
        private String BU;

        private Builder(String[] columns, String uniqueColumn) {
            this.BH = (String[]) fq.f(columns);
            this.BQ = new ArrayList();
            this.BR = uniqueColumn;
            this.BS = new HashMap();
            this.BT = false;
            this.BU = null;
        }

        private void a(HashMap<String, Object> hashMap) {
            Object obj = hashMap.get(this.BR);
            if (obj != null) {
                Integer num = (Integer) this.BS.remove(obj);
                if (num != null) {
                    this.BQ.remove(num.intValue());
                }
                this.BS.put(obj, Integer.valueOf(this.BQ.size()));
            }
        }

        private void et() {
            if (this.BR != null) {
                this.BS.clear();
                int size = this.BQ.size();
                int i = 0;
                while (i < size) {
                    Object obj = ((HashMap) this.BQ.get(i)).get(this.BR);
                    if (obj != null) {
                        this.BS.put(obj, Integer.valueOf(i));
                    }
                    i++;
                }
            }
        }

        public DataHolder build(int statusCode) {
            return new DataHolder((com.google.android.gms.common.data.DataHolder.Builder)statusCode, (int)null, (Bundle)null);
        }

        public DataHolder build(int statusCode, Bundle metadata) {
            return new DataHolder((com.google.android.gms.common.data.DataHolder.Builder)statusCode, (int)metadata, (Bundle)-1, (int)null);
        }

        public DataHolder build(int statusCode, Bundle metadata, int maxResults) {
            return new DataHolder((com.google.android.gms.common.data.DataHolder.Builder)statusCode, (int)metadata, (Bundle)maxResults, (int)null);
        }

        public int getCount() {
            return this.BQ.size();
        }

        public com.google.android.gms.common.data.DataHolder.Builder removeRowsWithValue(String column, Object value) {
            int i = this.BQ.size() - 1;
            while (i >= 0) {
                if (fo.equal(((HashMap) this.BQ.get(i)).get(column), value)) {
                    this.BQ.remove(i);
                }
                i--;
            }
            return this;
        }

        public com.google.android.gms.common.data.DataHolder.Builder sort(String sortColumn) {
            fb.d(sortColumn);
            if (!(this.BT && sortColumn.equals(this.BU))) {
                Collections.sort(this.BQ, new a(sortColumn));
                et();
                this.BT = true;
                this.BU = sortColumn;
            }
            return this;
        }

        public com.google.android.gms.common.data.DataHolder.Builder withRow(ContentValues values) {
            fb.d(values);
            HashMap hashMap = new HashMap(values.size());
            Iterator it = values.valueSet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                hashMap.put(entry.getKey(), entry.getValue());
            }
            return withRow(hashMap);
        }

        public com.google.android.gms.common.data.DataHolder.Builder withRow(HashMap row) {
            fb.d(row);
            if (this.BR != null) {
                a(row);
            }
            this.BQ.add(row);
            this.BT = false;
            return this;
        }
    }

    private static final class a implements Comparator<HashMap<String, Object>> {
        private final String BV;

        a(String str) {
            this.BV = (String) fq.f(str);
        }

        public int a(HashMap<String, Object> hashMap, HashMap<String, Object> hashMap2) {
            Object f = fq.f(hashMap.get(this.BV));
            Object f2 = fq.f(hashMap2.get(this.BV));
            if (f.equals(f2)) {
                return 0;
            }
            if (f instanceof Boolean) {
                return ((Boolean) f).compareTo((Boolean) f2);
            }
            if (f instanceof Long) {
                return ((Long) f).compareTo((Long) f2);
            }
            if (f instanceof Integer) {
                return ((Integer) f).compareTo((Integer) f2);
            }
            if (f instanceof String) {
                return ((String) f).compareTo((String) f2);
            }
            throw new IllegalArgumentException("Unknown type for lValue " + f);
        }

        public /* synthetic */ int compare(Object x0, Object x1) {
            return a((HashMap) x0, (HashMap) x1);
        }
    }

    static class AnonymousClass_1 extends com.google.android.gms.common.data.DataHolder.Builder {
        AnonymousClass_1(String[] strArr, String str) {
            super(str, null);
        }

        public com.google.android.gms.common.data.DataHolder.Builder withRow(ContentValues values) {
            throw new UnsupportedOperationException("Cannot add data to empty builder");
        }

        public com.google.android.gms.common.data.DataHolder.Builder withRow(HashMap<String, Object> row) {
            throw new UnsupportedOperationException("Cannot add data to empty builder");
        }
    }

    static {
        CREATOR = new DataHolderCreator();
        BP = new AnonymousClass_1(new String[0], null);
    }

    DataHolder(int versionCode, String[] columns, CursorWindow[] windows, int statusCode, Bundle metadata) {
        this.mClosed = false;
        this.BO = true;
        this.xH = versionCode;
        this.BH = columns;
        this.BJ = windows;
        this.Ah = statusCode;
        this.BK = metadata;
    }

    public DataHolder(AbstractWindowedCursor cursor, int statusCode, Bundle metadata) {
        this(cursor.getColumnNames(), a(cursor), statusCode, metadata);
    }

    private DataHolder(Builder builder, int statusCode, Bundle metadata) {
        this(builder.BH, a(builder, -1), statusCode, metadata);
    }

    private DataHolder(Builder builder, int statusCode, Bundle metadata, int maxResults) {
        this(builder.BH, a(builder, maxResults), statusCode, metadata);
    }

    public DataHolder(String[] columns, CursorWindow[] windows, int statusCode, Bundle metadata) {
        this.mClosed = false;
        this.BO = true;
        this.xH = 1;
        this.BH = (String[]) fq.f(columns);
        this.BJ = (CursorWindow[]) fq.f(windows);
        this.Ah = statusCode;
        this.BK = metadata;
        validateContents();
    }

    private static CursorWindow[] a(AbstractWindowedCursor abstractWindowedCursor) {
        int i;
        ArrayList arrayList = new ArrayList();
        int count = abstractWindowedCursor.getCount();
        CursorWindow window = abstractWindowedCursor.getWindow();
        if (window == null || window.getStartPosition() != 0) {
            i = 0;
        } else {
            window.acquireReference();
            abstractWindowedCursor.setWindow(null);
            arrayList.add(window);
            i = window.getNumRows();
        }
        while (i < count && abstractWindowedCursor.moveToPosition(i)) {
            CursorWindow window2 = abstractWindowedCursor.getWindow();
            if (window2 != null) {
                window2.acquireReference();
                abstractWindowedCursor.setWindow(null);
            } else {
                window2 = new CursorWindow(false);
                window2.setStartPosition(i);
                abstractWindowedCursor.fillWindow(i, window2);
            }
            if (window2.getNumRows() == 0) {
                break;
            }
            arrayList.add(window2);
            i = window2.getNumRows() + window2.getStartPosition();
        }
        abstractWindowedCursor.close();
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    private static CursorWindow[] a(Builder builder, int i) {
        int i2 = 0;
        if (builder.BH.length == 0) {
            return new CursorWindow[0];
        }
        List list;
        if (i < 0 || i >= builder.BQ.size()) {
            ArrayList arrayList = builder.BQ;
        } else {
            list = builder.BQ.subList(0, i);
        }
        int size = list.size();
        CursorWindow cursorWindow = new CursorWindow(false);
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(cursorWindow);
        cursorWindow.setNumColumns(builder.BH.length);
        int i3 = 0;
        boolean z = false;
        while (i3 < size) {
            try {
                int i4;
                int i5;
                CursorWindow cursorWindow2;
                if (cursorWindow.allocRow()) {
                    i4 = i5;
                } else {
                    Log.d("DataHolder", "Allocating additional cursor window for large data set (row " + i3 + ")");
                    cursorWindow = new CursorWindow(false);
                    cursorWindow.setStartPosition(i3);
                    cursorWindow.setNumColumns(builder.BH.length);
                    arrayList2.add(cursorWindow);
                    if (cursorWindow.allocRow()) {
                        i4 = 0;
                    } else {
                        Log.e("DataHolder", "Unable to allocate row to hold data.");
                        arrayList2.remove(cursorWindow);
                        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
                    }
                }
                Map map = (Map) list.get(i3);
                boolean z2 = true;
                int i6 = 0;
                while (i6 < builder.BH.length && z2) {
                    String str = builder.BH[i6];
                    Object obj = map.get(str);
                    if (obj == null) {
                        z2 = cursorWindow.putNull(i4, i6);
                    } else if (obj instanceof String) {
                        z2 = cursorWindow.putString((String) obj, i4, i6);
                    } else if (obj instanceof Long) {
                        z2 = cursorWindow.putLong(((Long) obj).longValue(), i4, i6);
                    } else if (obj instanceof Integer) {
                        z2 = cursorWindow.putLong((long) ((Integer) obj).intValue(), i4, i6);
                    } else if (obj instanceof Boolean) {
                        z2 = cursorWindow.putLong(((Boolean) obj).booleanValue() ? 1 : 0, i4, i6);
                    } else if (obj instanceof byte[]) {
                        z2 = cursorWindow.putBlob((byte[]) obj, i4, i6);
                    } else if (obj instanceof Double) {
                        z2 = cursorWindow.putDouble(((Double) obj).doubleValue(), i4, i6);
                    } else {
                        throw new IllegalArgumentException("Unsupported object for column " + str + ": " + obj);
                    }
                    i6++;
                }
                if (z2) {
                    int i7 = i4 + 1;
                    i5 = i3;
                    cursorWindow2 = cursorWindow;
                } else {
                    Log.d("DataHolder", "Couldn't populate window data for row " + i3 + " - allocating new window.");
                    cursorWindow.freeLastRow();
                    CursorWindow cursorWindow3 = new CursorWindow(false);
                    cursorWindow3.setNumColumns(builder.BH.length);
                    arrayList2.add(cursorWindow3);
                    i5 = i3 - 1;
                    cursorWindow2 = cursorWindow3;
                    z2 = false;
                }
                cursorWindow = cursorWindow2;
                i3 = i5 + 1;
                z = z2;
            } catch (RuntimeException e) {
                RuntimeException runtimeException = e;
                size = arrayList2.size();
                int size2;
                while (i2 < size2) {
                    ((CursorWindow) arrayList2.get(i2)).close();
                    i2++;
                }
                throw runtimeException;
            }
        }
        return (CursorWindow[]) arrayList2.toArray(new CursorWindow[arrayList2.size()]);
    }

    public static Builder builder(String[] columns) {
        return new Builder(null, null);
    }

    public static Builder builder(String[] columns, String uniqueColumn) {
        fq.f(uniqueColumn);
        return new Builder(uniqueColumn, null);
    }

    private void e(String str, int i) {
        if (this.BI == null || !this.BI.containsKey(str)) {
            throw new IllegalArgumentException("No such column: " + str);
        } else if (isClosed()) {
            throw new IllegalArgumentException("Buffer is closed.");
        } else if (i < 0 || i >= this.BM) {
            throw new CursorIndexOutOfBoundsException(i, this.BM);
        }
    }

    public static DataHolder empty(int statusCode) {
        return empty(statusCode, null);
    }

    public static DataHolder empty(int statusCode, Bundle metadata) {
        return new DataHolder(BP, statusCode, metadata);
    }

    public int G(int i) {
        int i2 = 0;
        boolean z = i >= 0 && i < this.BM;
        fq.x(z);
        while (i2 < this.BL.length) {
            if (i < this.BL[i2]) {
                i2--;
                break;
            } else {
                i2++;
            }
        }
        return i2 == this.BL.length ? i2 - 1 : i2;
    }

    public void c(Object obj) {
        this.BN = obj;
    }

    public void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                int i = 0;
                while (i < this.BJ.length) {
                    this.BJ[i].close();
                    i++;
                }
            }
        }
    }

    public void copyToBuffer(String column, int row, int windowIndex, CharArrayBuffer dataOut) {
        e(column, row);
        this.BJ[windowIndex].copyStringToBuffer(row, this.BI.getInt(column), dataOut);
    }

    public int describeContents() {
        return 0;
    }

    String[] er() {
        return this.BH;
    }

    CursorWindow[] es() {
        return this.BJ;
    }

    protected void finalize() throws Throwable {
        if (this.BO && this.BJ.length > 0 && !isClosed()) {
            Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call close() on all DataBuffer extending objects when you are done with them. (" + (this.BN == null ? "internal object: " + toString() : this.BN.toString()) + ")");
            close();
        }
        super.finalize();
    }

    public boolean getBoolean(String column, int row, int windowIndex) {
        e(column, row);
        return Long.valueOf(this.BJ[windowIndex].getLong(row, this.BI.getInt(column))).longValue() == 1;
    }

    public byte[] getByteArray(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].getBlob(row, this.BI.getInt(column));
    }

    public int getCount() {
        return this.BM;
    }

    public double getDouble(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].getDouble(row, this.BI.getInt(column));
    }

    public int getInteger(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].getInt(row, this.BI.getInt(column));
    }

    public long getLong(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].getLong(row, this.BI.getInt(column));
    }

    public Bundle getMetadata() {
        return this.BK;
    }

    public int getStatusCode() {
        return this.Ah;
    }

    public String getString(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].getString(row, this.BI.getInt(column));
    }

    int getVersionCode() {
        return this.xH;
    }

    public boolean hasColumn(String column) {
        return this.BI.containsKey(column);
    }

    public boolean hasNull(String column, int row, int windowIndex) {
        e(column, row);
        return this.BJ[windowIndex].isNull(row, this.BI.getInt(column));
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public Uri parseUri(String column, int row, int windowIndex) {
        String string = getString(column, row, windowIndex);
        return string == null ? null : Uri.parse(string);
    }

    public void validateContents() {
        int i = 0;
        this.BI = new Bundle();
        int i2 = 0;
        while (i2 < this.BH.length) {
            this.BI.putInt(this.BH[i2], i2);
            i2++;
        }
        this.BL = new int[this.BJ.length];
        i2 = 0;
        while (i < this.BJ.length) {
            this.BL[i] = i2;
            i2 += this.BJ[i].getNumRows() - i2 - this.BJ[i].getStartPosition();
            i++;
        }
        this.BM = i2;
    }

    public void writeToParcel(Parcel dest, int flags) {
        DataHolderCreator.a(this, dest, flags);
    }
}