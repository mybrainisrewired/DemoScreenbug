package com.google.android.gms.internal;

import com.google.android.gms.drive.metadata.SearchableOrderedMetadataField;
import com.google.android.gms.drive.metadata.SortableMetadataField;
import java.util.Date;

public class gt {
    public static final a Gs;
    public static final b Gt;
    public static final d Gu;
    public static final c Gv;
    public static final e Gw;

    public static class a extends com.google.android.gms.drive.metadata.internal.b implements SortableMetadataField<Date> {
        public a(String str, int i) {
            super(str, i);
        }
    }

    public static class b extends com.google.android.gms.drive.metadata.internal.b implements SearchableOrderedMetadataField<Date>, SortableMetadataField<Date> {
        public b(String str, int i) {
            super(str, i);
        }
    }

    public static class c extends com.google.android.gms.drive.metadata.internal.b implements SortableMetadataField<Date> {
        public c(String str, int i) {
            super(str, i);
        }
    }

    public static class d extends com.google.android.gms.drive.metadata.internal.b implements SearchableOrderedMetadataField<Date>, SortableMetadataField<Date> {
        public d(String str, int i) {
            super(str, i);
        }
    }

    public static class e extends com.google.android.gms.drive.metadata.internal.b implements SearchableOrderedMetadataField<Date>, SortableMetadataField<Date> {
        public e(String str, int i) {
            super(str, i);
        }
    }

    static {
        Gs = new a("created", 4100000);
        Gt = new b("lastOpenedTime", 4300000);
        Gu = new d("modified", 4100000);
        Gv = new c("modifiedByMe", 4100000);
        Gw = new e("sharedWithMe", 4100000);
    }
}