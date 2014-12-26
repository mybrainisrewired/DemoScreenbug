package com.google.android.gms.internal;

import com.google.android.gms.internal.c.f;
import com.google.android.gms.internal.c.j;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.io.IOException;

public interface it {

    public static final class a extends kp<com.google.android.gms.internal.it.a> {
        public long aaY;
        public j aaZ;
        public f fK;

        public a() {
            lV();
        }

        public static com.google.android.gms.internal.it.a l(byte[] bArr) throws ks {
            return (com.google.android.gms.internal.it.a) kt.a(new com.google.android.gms.internal.it.a(), bArr);
        }

        public void a(ko koVar) throws IOException {
            koVar.b(1, this.aaY);
            if (this.fK != null) {
                koVar.a((int)MMAdView.TRANSITION_UP, this.fK);
            }
            if (this.aaZ != null) {
                koVar.a((int)MMAdView.TRANSITION_DOWN, this.aaZ);
            }
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return n(knVar);
        }

        public int c() {
            int c = super.c() + ko.d(1, this.aaY);
            if (this.fK != null) {
                c += ko.b((int)MMAdView.TRANSITION_UP, this.fK);
            }
            if (this.aaZ != null) {
                c += ko.b((int)MMAdView.TRANSITION_DOWN, this.aaZ);
            }
            this.adY = c;
            return c;
        }

        public boolean equals(com.google.android.gms.internal.it.a o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.it.a)) {
                return false;
            }
            o = o;
            if (this.aaY != o.aaY) {
                return false;
            }
            if (this.fK == null) {
                if (o.fK != null) {
                    return false;
                }
            } else if (!this.fK.equals(o.fK)) {
                return false;
            }
            if (this.aaZ == null) {
                if (o.aaZ != null) {
                    return false;
                }
            } else if (!this.aaZ.equals(o.aaZ)) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((this.aaZ == null ? 0 : this.aaZ.hashCode()) + (((this.fK == null ? 0 : this.fK.hashCode()) + ((((int) (this.aaY ^ (this.aaY >>> 32))) + 527) * 31)) * 31)) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i = this.adU.hashCode();
            }
            return hashCode + i;
        }

        public com.google.android.gms.internal.it.a lV() {
            this.aaY = 0;
            this.fK = null;
            this.aaZ = null;
            this.adU = null;
            this.adY = -1;
            return this;
        }

        public com.google.android.gms.internal.it.a n(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        this.aaY = knVar.mj();
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        if (this.fK == null) {
                            this.fK = new f();
                        }
                        knVar.a(this.fK);
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        if (this.aaZ == null) {
                            this.aaZ = new j();
                        }
                        knVar.a(this.aaZ);
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }
    }
}