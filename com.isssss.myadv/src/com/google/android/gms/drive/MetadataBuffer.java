package com.google.android.gms.drive;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.drive.metadata.MetadataField;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.google.android.gms.drive.metadata.internal.c;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class MetadataBuffer extends DataBuffer<Metadata> {
    private static final String[] EL;
    private final String EM;
    private a EN;

    private static class a extends Metadata {
        private final DataHolder BB;
        private final int BE;
        private final int EO;

        public a(DataHolder dataHolder, int i) {
            this.BB = dataHolder;
            this.EO = i;
            this.BE = dataHolder.G(i);
        }

        protected <T> T a(MetadataField<T> metadataField) {
            return metadataField.a(this.BB, this.EO, this.BE);
        }

        public Metadata fB() {
            MetadataBundle fT = MetadataBundle.fT();
            Iterator it = c.fS().iterator();
            while (it.hasNext()) {
                ((MetadataField) it.next()).a(this.BB, fT, this.EO, this.BE);
            }
            return new b(fT);
        }

        public /* synthetic */ Object freeze() {
            return fB();
        }

        public boolean isDataValid() {
            return !this.BB.isClosed();
        }
    }

    static {
        List arrayList = new ArrayList();
        Iterator it = c.fS().iterator();
        while (it.hasNext()) {
            arrayList.addAll(((MetadataField) it.next()).fR());
        }
        EL = (String[]) arrayList.toArray(new String[0]);
    }

    public MetadataBuffer(DataHolder dataHolder, String nextPageToken) {
        super(dataHolder);
        this.EM = nextPageToken;
    }

    public Metadata get(int row) {
        a aVar = this.EN;
        if (aVar != null && aVar.EO == row) {
            return aVar;
        }
        Metadata aVar2 = new a(this.BB, row);
        this.EN = aVar2;
        return aVar2;
    }

    public String getNextPageToken() {
        return this.EM;
    }
}