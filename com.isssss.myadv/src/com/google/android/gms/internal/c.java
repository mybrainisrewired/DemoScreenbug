package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;
import mobi.vserv.android.ads.VservConstants;

public interface c {

    public static final class a extends kp<com.google.android.gms.internal.c.a> {
        public int eE;
        public int eF;
        public int level;

        public a() {
            b();
        }

        public com.google.android.gms.internal.c.a a(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        mh = knVar.mk();
                        switch (mh) {
                            case MMAdView.TRANSITION_FADE:
                            case MMAdView.TRANSITION_UP:
                            case MMAdView.TRANSITION_DOWN:
                                this.level = mh;
                                break;
                            default:
                                break;
                        }
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        this.eE = knVar.mk();
                        break;
                    case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                        this.eF = knVar.mk();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public void a(ko koVar) throws IOException {
            if (this.level != 1) {
                koVar.i(1, this.level);
            }
            if (this.eE != 0) {
                koVar.i(MMAdView.TRANSITION_UP, this.eE);
            }
            if (this.eF != 0) {
                koVar.i(MMAdView.TRANSITION_DOWN, this.eF);
            }
            super.a(koVar);
        }

        public com.google.android.gms.internal.c.a b() {
            this.level = 1;
            this.eE = 0;
            this.eF = 0;
            this.adU = null;
            this.adY = -1;
            return this;
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return a(knVar);
        }

        public int c() {
            int c = super.c();
            if (this.level != 1) {
                c += ko.j(1, this.level);
            }
            if (this.eE != 0) {
                c += ko.j(MMAdView.TRANSITION_UP, this.eE);
            }
            if (this.eF != 0) {
                c += ko.j(MMAdView.TRANSITION_DOWN, this.eF);
            }
            this.adY = c;
            return c;
        }

        public boolean equals(com.google.android.gms.internal.c.a o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.a)) {
                return false;
            }
            o = o;
            if (this.level != o.level || this.eE != o.eE || this.eF != o.eF) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public int hashCode() {
            int i = (((((this.level + 527) * 31) + this.eE) * 31) + this.eF) * 31;
            int hashCode = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode + i;
        }
    }

    public static final class b extends kp<com.google.android.gms.internal.c.b> {
        private static volatile com.google.android.gms.internal.c.b[] eG;
        public int[] eH;
        public int eI;
        public boolean eJ;
        public boolean eK;
        public int name;

        public b() {
            e();
        }

        public static com.google.android.gms.internal.c.b[] d() {
            if (eG == null) {
                synchronized (kr.adX) {
                    if (eG == null) {
                        eG = new com.google.android.gms.internal.c.b[0];
                    }
                }
            }
            return eG;
        }

        public void a(ko koVar) throws IOException {
            if (this.eK) {
                koVar.a(1, this.eK);
            }
            koVar.i(MMAdView.TRANSITION_UP, this.eI);
            if (this.eH != null && this.eH.length > 0) {
                int i = 0;
                while (i < this.eH.length) {
                    koVar.i(MMAdView.TRANSITION_DOWN, this.eH[i]);
                    i++;
                }
            }
            if (this.name != 0) {
                koVar.i(MMAdView.TRANSITION_RANDOM, this.name);
            }
            if (this.eJ) {
                koVar.a((int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, this.eJ);
            }
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return c(knVar);
        }

        public int c() {
            int i = 0;
            int c = super.c();
            if (this.eK) {
                c += ko.b(1, this.eK);
            }
            int j = ko.j(MMAdView.TRANSITION_UP, this.eI) + c;
            if (this.eH == null || this.eH.length <= 0) {
                c = j;
            } else {
                c = 0;
                while (c < this.eH.length) {
                    i += ko.cX(this.eH[c]);
                    c++;
                }
                c = j + i + this.eH.length * 1;
            }
            if (this.name != 0) {
                c += ko.j(MMAdView.TRANSITION_RANDOM, this.name);
            }
            if (this.eJ) {
                c += ko.b((int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, this.eJ);
            }
            this.adY = c;
            return c;
        }

        public com.google.android.gms.internal.c.b c(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        this.eK = knVar.ml();
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        this.eI = knVar.mk();
                        break;
                    case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE);
                        mh = this.eH == null ? 0 : this.eH.length;
                        Object obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eH, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.eH = obj;
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        int cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.eH == null ? 0 : this.eH.length;
                        Object obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.eH, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.eH = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                        this.name = knVar.mk();
                        break;
                    case ApiEventType.API_MRAID_HIDE_VIDEO:
                        this.eJ = knVar.ml();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public com.google.android.gms.internal.c.b e() {
            this.eH = kw.aea;
            this.eI = 0;
            this.name = 0;
            this.eJ = false;
            this.eK = false;
            this.adU = null;
            this.adY = -1;
            return this;
        }

        public boolean equals(com.google.android.gms.internal.c.b o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.b)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.eH, o.eH) || this.eI != o.eI || this.name != o.name || this.eJ != o.eJ || this.eK != o.eK) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public int hashCode() {
            int i = 1231;
            int hashCode = ((this.eJ ? 1231 : 1237) + ((((((kr.hashCode(this.eH) + 527) * 31) + this.eI) * 31) + this.name) * 31)) * 31;
            if (!this.eK) {
                i = 1237;
            }
            i = (hashCode + i) * 31;
            hashCode = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode + i;
        }
    }

    public static final class c extends kp<com.google.android.gms.internal.c.c> {
        private static volatile com.google.android.gms.internal.c.c[] eL;
        public String eM;
        public long eN;
        public long eO;
        public boolean eP;
        public long eQ;

        public c() {
            g();
        }

        public static com.google.android.gms.internal.c.c[] f() {
            if (eL == null) {
                synchronized (kr.adX) {
                    if (eL == null) {
                        eL = new com.google.android.gms.internal.c.c[0];
                    }
                }
            }
            return eL;
        }

        public void a(ko koVar) throws IOException {
            if (!this.eM.equals(Preconditions.EMPTY_ARGUMENTS)) {
                koVar.b(1, this.eM);
            }
            if (this.eN != 0) {
                koVar.b((int)MMAdView.TRANSITION_UP, this.eN);
            }
            if (this.eO != 2147483647L) {
                koVar.b((int)MMAdView.TRANSITION_DOWN, this.eO);
            }
            if (this.eP) {
                koVar.a((int)MMAdView.TRANSITION_RANDOM, this.eP);
            }
            if (this.eQ != 0) {
                koVar.b((int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, this.eQ);
            }
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return d(knVar);
        }

        public int c() {
            int c = super.c();
            if (!this.eM.equals(Preconditions.EMPTY_ARGUMENTS)) {
                c += ko.g(1, this.eM);
            }
            if (this.eN != 0) {
                c += ko.d(MMAdView.TRANSITION_UP, this.eN);
            }
            if (this.eO != 2147483647L) {
                c += ko.d(MMAdView.TRANSITION_DOWN, this.eO);
            }
            if (this.eP) {
                c += ko.b((int)MMAdView.TRANSITION_RANDOM, this.eP);
            }
            if (this.eQ != 0) {
                c += ko.d(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, this.eQ);
            }
            this.adY = c;
            return c;
        }

        public com.google.android.gms.internal.c.c d(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        this.eM = knVar.readString();
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        this.eN = knVar.mj();
                        break;
                    case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                        this.eO = knVar.mj();
                        break;
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                        this.eP = knVar.ml();
                        break;
                    case ApiEventType.API_MRAID_PLAY_VIDEO:
                        this.eQ = knVar.mj();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public boolean equals(com.google.android.gms.internal.c.c o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.c)) {
                return false;
            }
            o = o;
            if (this.eM == null) {
                if (o.eM != null) {
                    return false;
                }
            } else if (!this.eM.equals(o.eM)) {
                return false;
            }
            if (this.eN != o.eN || this.eO != o.eO || this.eP != o.eP || this.eQ != o.eQ) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public com.google.android.gms.internal.c.c g() {
            this.eM = Preconditions.EMPTY_ARGUMENTS;
            this.eN = 0;
            this.eO = 2147483647L;
            this.eP = false;
            this.eQ = 0;
            this.adU = null;
            this.adY = -1;
            return this;
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((this.eP ? 1231 : 1237) + (((((((this.eM == null ? 0 : this.eM.hashCode()) + 527) * 31) + ((int) (this.eN ^ (this.eN >>> 32)))) * 31) + ((int) (this.eO ^ (this.eO >>> 32)))) * 31)) * 31) + ((int) (this.eQ ^ (this.eQ >>> 32)))) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i = this.adU.hashCode();
            }
            return hashCode + i;
        }
    }

    public static final class d extends kp<com.google.android.gms.internal.c.d> {
        public com.google.android.gms.internal.d.a[] eR;
        public com.google.android.gms.internal.d.a[] eS;
        public com.google.android.gms.internal.c.c[] eT;

        public d() {
            h();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.google.android.gms.internal.ko r5) throws java.io.IOException {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.c.d.a(com.google.android.gms.internal.ko):void");
            /*
            r4 = this;
            r1 = 0;
            r0 = r4.eR;
            if (r0 == 0) goto L_0x001d;
        L_0x0005:
            r0 = r4.eR;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x001d;
        L_0x000a:
            r0 = r1;
        L_0x000b:
            r2 = r4.eR;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x001d;
        L_0x0010:
            r2 = r4.eR;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x001a;
        L_0x0016:
            r3 = 1;
            r5.a(r3, r2);
        L_0x001a:
            r0 = r0 + 1;
            goto L_0x000b;
        L_0x001d:
            r0 = r4.eS;
            if (r0 == 0) goto L_0x0039;
        L_0x0021:
            r0 = r4.eS;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0039;
        L_0x0026:
            r0 = r1;
        L_0x0027:
            r2 = r4.eS;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0039;
        L_0x002c:
            r2 = r4.eS;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0036;
        L_0x0032:
            r3 = 2;
            r5.a(r3, r2);
        L_0x0036:
            r0 = r0 + 1;
            goto L_0x0027;
        L_0x0039:
            r0 = r4.eT;
            if (r0 == 0) goto L_0x0054;
        L_0x003d:
            r0 = r4.eT;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0054;
        L_0x0042:
            r0 = r4.eT;
            r0 = r0.length;
            if (r1 >= r0) goto L_0x0054;
        L_0x0047:
            r0 = r4.eT;
            r0 = r0[r1];
            if (r0 == 0) goto L_0x0051;
        L_0x004d:
            r2 = 3;
            r5.a(r2, r0);
        L_0x0051:
            r1 = r1 + 1;
            goto L_0x0042;
        L_0x0054:
            super.a(r5);
            return;
            */
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return e(knVar);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int c() {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.c.d.c():int");
            /*
            r5 = this;
            r1 = 0;
            r0 = super.c();
            r2 = r5.eR;
            if (r2 == 0) goto L_0x0025;
        L_0x0009:
            r2 = r5.eR;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x0025;
        L_0x000e:
            r2 = r0;
            r0 = r1;
        L_0x0010:
            r3 = r5.eR;
            r3 = r3.length;
            if (r0 >= r3) goto L_0x0024;
        L_0x0015:
            r3 = r5.eR;
            r3 = r3[r0];
            if (r3 == 0) goto L_0x0021;
        L_0x001b:
            r4 = 1;
            r3 = com.google.android.gms.internal.ko.b(r4, r3);
            r2 = r2 + r3;
        L_0x0021:
            r0 = r0 + 1;
            goto L_0x0010;
        L_0x0024:
            r0 = r2;
        L_0x0025:
            r2 = r5.eS;
            if (r2 == 0) goto L_0x0045;
        L_0x0029:
            r2 = r5.eS;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x0045;
        L_0x002e:
            r2 = r0;
            r0 = r1;
        L_0x0030:
            r3 = r5.eS;
            r3 = r3.length;
            if (r0 >= r3) goto L_0x0044;
        L_0x0035:
            r3 = r5.eS;
            r3 = r3[r0];
            if (r3 == 0) goto L_0x0041;
        L_0x003b:
            r4 = 2;
            r3 = com.google.android.gms.internal.ko.b(r4, r3);
            r2 = r2 + r3;
        L_0x0041:
            r0 = r0 + 1;
            goto L_0x0030;
        L_0x0044:
            r0 = r2;
        L_0x0045:
            r2 = r5.eT;
            if (r2 == 0) goto L_0x0062;
        L_0x0049:
            r2 = r5.eT;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x0062;
        L_0x004e:
            r2 = r5.eT;
            r2 = r2.length;
            if (r1 >= r2) goto L_0x0062;
        L_0x0053:
            r2 = r5.eT;
            r2 = r2[r1];
            if (r2 == 0) goto L_0x005f;
        L_0x0059:
            r3 = 3;
            r2 = com.google.android.gms.internal.ko.b(r3, r2);
            r0 = r0 + r2;
        L_0x005f:
            r1 = r1 + 1;
            goto L_0x004e;
        L_0x0062:
            r5.adY = r0;
            return r0;
            */
        }

        public com.google.android.gms.internal.c.d e(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                Object obj;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
                        mh = this.eR == null ? 0 : this.eR.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eR, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.eR = obj;
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        b = kw.b(knVar, ApiEventType.API_MRAID_GET_CURRENT_POSITION);
                        mh = this.eS == null ? 0 : this.eS.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eS, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.eS = obj;
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_VIBRATE);
                        mh = this.eT == null ? 0 : this.eT.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eT, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.c();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.c();
                        knVar.a(obj[mh]);
                        this.eT = obj;
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public boolean equals(com.google.android.gms.internal.c.d o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.d)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.eR, o.eR) || !kr.equals(this.eS, o.eS) || !kr.equals(this.eT, o.eT)) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public com.google.android.gms.internal.c.d h() {
            this.eR = com.google.android.gms.internal.d.a.r();
            this.eS = com.google.android.gms.internal.d.a.r();
            this.eT = com.google.android.gms.internal.c.c.f();
            this.adU = null;
            this.adY = -1;
            return this;
        }

        public int hashCode() {
            int hashCode = (((((kr.hashCode(this.eR) + 527) * 31) + kr.hashCode(this.eS)) * 31) + kr.hashCode(this.eT)) * 31;
            int hashCode2 = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode2 + hashCode;
        }
    }

    public static final class e extends kp<com.google.android.gms.internal.c.e> {
        private static volatile com.google.android.gms.internal.c.e[] eU;
        public int key;
        public int value;

        public e() {
            j();
        }

        public static com.google.android.gms.internal.c.e[] i() {
            if (eU == null) {
                synchronized (kr.adX) {
                    if (eU == null) {
                        eU = new com.google.android.gms.internal.c.e[0];
                    }
                }
            }
            return eU;
        }

        public void a(ko koVar) throws IOException {
            koVar.i(1, this.key);
            koVar.i(MMAdView.TRANSITION_UP, this.value);
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return f(knVar);
        }

        public int c() {
            int c = super.c() + ko.j(1, this.key) + ko.j(MMAdView.TRANSITION_UP, this.value);
            this.adY = c;
            return c;
        }

        public boolean equals(com.google.android.gms.internal.c.e o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.e)) {
                return false;
            }
            o = o;
            if (this.key != o.key || this.value != o.value) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public com.google.android.gms.internal.c.e f(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        this.key = knVar.mk();
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        this.value = knVar.mk();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public int hashCode() {
            int i = (((this.key + 527) * 31) + this.value) * 31;
            int hashCode = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode + i;
        }

        public com.google.android.gms.internal.c.e j() {
            this.key = 0;
            this.value = 0;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }

    public static final class f extends kp<com.google.android.gms.internal.c.f> {
        public String[] eV;
        public String[] eW;
        public com.google.android.gms.internal.d.a[] eX;
        public com.google.android.gms.internal.c.e[] eY;
        public com.google.android.gms.internal.c.b[] eZ;
        public com.google.android.gms.internal.c.b[] fa;
        public com.google.android.gms.internal.c.b[] fb;
        public com.google.android.gms.internal.c.g[] fc;
        public String fd;
        public String fe;
        public String ff;
        public String fg;
        public com.google.android.gms.internal.c.a fh;
        public float fi;
        public boolean fj;
        public String[] fk;
        public int fl;

        public f() {
            k();
        }

        public static com.google.android.gms.internal.c.f a(byte[] bArr) throws ks {
            return (com.google.android.gms.internal.c.f) kt.a(new com.google.android.gms.internal.c.f(), bArr);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.google.android.gms.internal.ko r5) throws java.io.IOException {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.c.f.a(com.google.android.gms.internal.ko):void");
            /*
            r4 = this;
            r1 = 0;
            r0 = r4.eW;
            if (r0 == 0) goto L_0x001d;
        L_0x0005:
            r0 = r4.eW;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x001d;
        L_0x000a:
            r0 = r1;
        L_0x000b:
            r2 = r4.eW;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x001d;
        L_0x0010:
            r2 = r4.eW;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x001a;
        L_0x0016:
            r3 = 1;
            r5.b(r3, r2);
        L_0x001a:
            r0 = r0 + 1;
            goto L_0x000b;
        L_0x001d:
            r0 = r4.eX;
            if (r0 == 0) goto L_0x0039;
        L_0x0021:
            r0 = r4.eX;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0039;
        L_0x0026:
            r0 = r1;
        L_0x0027:
            r2 = r4.eX;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0039;
        L_0x002c:
            r2 = r4.eX;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0036;
        L_0x0032:
            r3 = 2;
            r5.a(r3, r2);
        L_0x0036:
            r0 = r0 + 1;
            goto L_0x0027;
        L_0x0039:
            r0 = r4.eY;
            if (r0 == 0) goto L_0x0055;
        L_0x003d:
            r0 = r4.eY;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0055;
        L_0x0042:
            r0 = r1;
        L_0x0043:
            r2 = r4.eY;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0055;
        L_0x0048:
            r2 = r4.eY;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0052;
        L_0x004e:
            r3 = 3;
            r5.a(r3, r2);
        L_0x0052:
            r0 = r0 + 1;
            goto L_0x0043;
        L_0x0055:
            r0 = r4.eZ;
            if (r0 == 0) goto L_0x0071;
        L_0x0059:
            r0 = r4.eZ;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0071;
        L_0x005e:
            r0 = r1;
        L_0x005f:
            r2 = r4.eZ;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0071;
        L_0x0064:
            r2 = r4.eZ;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x006e;
        L_0x006a:
            r3 = 4;
            r5.a(r3, r2);
        L_0x006e:
            r0 = r0 + 1;
            goto L_0x005f;
        L_0x0071:
            r0 = r4.fa;
            if (r0 == 0) goto L_0x008d;
        L_0x0075:
            r0 = r4.fa;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x008d;
        L_0x007a:
            r0 = r1;
        L_0x007b:
            r2 = r4.fa;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x008d;
        L_0x0080:
            r2 = r4.fa;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x008a;
        L_0x0086:
            r3 = 5;
            r5.a(r3, r2);
        L_0x008a:
            r0 = r0 + 1;
            goto L_0x007b;
        L_0x008d:
            r0 = r4.fb;
            if (r0 == 0) goto L_0x00a9;
        L_0x0091:
            r0 = r4.fb;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00a9;
        L_0x0096:
            r0 = r1;
        L_0x0097:
            r2 = r4.fb;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00a9;
        L_0x009c:
            r2 = r4.fb;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x00a6;
        L_0x00a2:
            r3 = 6;
            r5.a(r3, r2);
        L_0x00a6:
            r0 = r0 + 1;
            goto L_0x0097;
        L_0x00a9:
            r0 = r4.fc;
            if (r0 == 0) goto L_0x00c5;
        L_0x00ad:
            r0 = r4.fc;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00c5;
        L_0x00b2:
            r0 = r1;
        L_0x00b3:
            r2 = r4.fc;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00c5;
        L_0x00b8:
            r2 = r4.fc;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x00c2;
        L_0x00be:
            r3 = 7;
            r5.a(r3, r2);
        L_0x00c2:
            r0 = r0 + 1;
            goto L_0x00b3;
        L_0x00c5:
            r0 = r4.fd;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x00d6;
        L_0x00cf:
            r0 = 9;
            r2 = r4.fd;
            r5.b(r0, r2);
        L_0x00d6:
            r0 = r4.fe;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x00e7;
        L_0x00e0:
            r0 = 10;
            r2 = r4.fe;
            r5.b(r0, r2);
        L_0x00e7:
            r0 = r4.ff;
            r2 = "0";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x00f8;
        L_0x00f1:
            r0 = 12;
            r2 = r4.ff;
            r5.b(r0, r2);
        L_0x00f8:
            r0 = r4.fg;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x0109;
        L_0x0102:
            r0 = 13;
            r2 = r4.fg;
            r5.b(r0, r2);
        L_0x0109:
            r0 = r4.fh;
            if (r0 == 0) goto L_0x0114;
        L_0x010d:
            r0 = 14;
            r2 = r4.fh;
            r5.a(r0, r2);
        L_0x0114:
            r0 = r4.fi;
            r0 = java.lang.Float.floatToIntBits(r0);
            r2 = 0;
            r2 = java.lang.Float.floatToIntBits(r2);
            if (r0 == r2) goto L_0x0128;
        L_0x0121:
            r0 = 15;
            r2 = r4.fi;
            r5.b(r0, r2);
        L_0x0128:
            r0 = r4.fk;
            if (r0 == 0) goto L_0x0145;
        L_0x012c:
            r0 = r4.fk;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0145;
        L_0x0131:
            r0 = r1;
        L_0x0132:
            r2 = r4.fk;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0145;
        L_0x0137:
            r2 = r4.fk;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0142;
        L_0x013d:
            r3 = 16;
            r5.b(r3, r2);
        L_0x0142:
            r0 = r0 + 1;
            goto L_0x0132;
        L_0x0145:
            r0 = r4.fl;
            if (r0 == 0) goto L_0x0150;
        L_0x0149:
            r0 = 17;
            r2 = r4.fl;
            r5.i(r0, r2);
        L_0x0150:
            r0 = r4.fj;
            if (r0 == 0) goto L_0x015b;
        L_0x0154:
            r0 = 18;
            r2 = r4.fj;
            r5.a(r0, r2);
        L_0x015b:
            r0 = r4.eV;
            if (r0 == 0) goto L_0x0177;
        L_0x015f:
            r0 = r4.eV;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0177;
        L_0x0164:
            r0 = r4.eV;
            r0 = r0.length;
            if (r1 >= r0) goto L_0x0177;
        L_0x0169:
            r0 = r4.eV;
            r0 = r0[r1];
            if (r0 == 0) goto L_0x0174;
        L_0x016f:
            r2 = 19;
            r5.b(r2, r0);
        L_0x0174:
            r1 = r1 + 1;
            goto L_0x0164;
        L_0x0177:
            super.a(r5);
            return;
            */
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return g(knVar);
        }

        public int c() {
            int i;
            int i2;
            int i3;
            String str;
            kt ktVar;
            int i4 = 0;
            int c = super.c();
            if (this.eW == null || this.eW.length <= 0) {
                i = c;
            } else {
                i = 0;
                i2 = 0;
                i3 = 0;
                while (i < this.eW.length) {
                    str = this.eW[i];
                    if (str != null) {
                        i3++;
                        i2 += ko.cf(str);
                    }
                    i++;
                }
                i = c + i2 + i3 * 1;
            }
            if (this.eX != null && this.eX.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.eX.length) {
                    ktVar = this.eX[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)MMAdView.TRANSITION_UP, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (this.eY != null && this.eY.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.eY.length) {
                    ktVar = this.eY[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)MMAdView.TRANSITION_DOWN, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (this.eZ != null && this.eZ.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.eZ.length) {
                    ktVar = this.eZ[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)MMAdView.TRANSITION_RANDOM, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (this.fa != null && this.fa.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.fa.length) {
                    ktVar = this.fa[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (this.fb != null && this.fb.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.fb.length) {
                    ktVar = this.fb[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (this.fc != null && this.fc.length > 0) {
                i2 = i;
                i = 0;
                while (i < this.fc.length) {
                    ktVar = this.fc[i];
                    if (ktVar != null) {
                        i2 += ko.b((int)ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, ktVar);
                    }
                    i++;
                }
                i = i2;
            }
            if (!this.fd.equals(Preconditions.EMPTY_ARGUMENTS)) {
                i += ko.g(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES, this.fd);
            }
            if (!this.fe.equals(Preconditions.EMPTY_ARGUMENTS)) {
                i += ko.g(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, this.fe);
            }
            if (!this.ff.equals("0")) {
                i += ko.g(ApiEventType.API_MRAID_RESIZE, this.ff);
            }
            if (!this.fg.equals(Preconditions.EMPTY_ARGUMENTS)) {
                i += ko.g(ApiEventType.API_MRAID_CLOSE, this.fg);
            }
            if (this.fh != null) {
                i += ko.b((int)ApiEventType.API_MRAID_IS_VIEWABLE, this.fh);
            }
            if (Float.floatToIntBits(this.fi) != Float.floatToIntBits(BitmapDescriptorFactory.HUE_RED)) {
                i += ko.c((int)ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, this.fi);
            }
            if (this.fk != null && this.fk.length > 0) {
                i2 = 0;
                i3 = 0;
                c = 0;
                while (i2 < this.fk.length) {
                    str = this.fk[i2];
                    if (str != null) {
                        c++;
                        i3 += ko.cf(str);
                    }
                    i2++;
                }
                i = i + i3 + c * 2;
            }
            if (this.fl != 0) {
                i += ko.j(ApiEventType.API_MRAID_GET_SCREEN_SIZE, this.fl);
            }
            if (this.fj) {
                i += ko.b((int)ApiEventType.API_MRAID_GET_CURRENT_POSITION, this.fj);
            }
            if (this.eV != null && this.eV.length > 0) {
                i2 = 0;
                i3 = 0;
                while (i4 < this.eV.length) {
                    String str2 = this.eV[i4];
                    if (str2 != null) {
                        i3++;
                        i2 += ko.cf(str2);
                    }
                    i4++;
                }
                i = i + i2 + i3 * 2;
            }
            this.adY = i;
            return i;
        }

        public boolean equals(com.google.android.gms.internal.c.f o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.f)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.eV, o.eV) || !kr.equals(this.eW, o.eW) || !kr.equals(this.eX, o.eX) || !kr.equals(this.eY, o.eY) || !kr.equals(this.eZ, o.eZ) || !kr.equals(this.fa, o.fa) || !kr.equals(this.fb, o.fb) || !kr.equals(this.fc, o.fc)) {
                return false;
            }
            if (this.fd == null) {
                if (o.fd != null) {
                    return false;
                }
            } else if (!this.fd.equals(o.fd)) {
                return false;
            }
            if (this.fe == null) {
                if (o.fe != null) {
                    return false;
                }
            } else if (!this.fe.equals(o.fe)) {
                return false;
            }
            if (this.ff == null) {
                if (o.ff != null) {
                    return false;
                }
            } else if (!this.ff.equals(o.ff)) {
                return false;
            }
            if (this.fg == null) {
                if (o.fg != null) {
                    return false;
                }
            } else if (!this.fg.equals(o.fg)) {
                return false;
            }
            if (this.fh == null) {
                if (o.fh != null) {
                    return false;
                }
            } else if (!this.fh.equals(o.fh)) {
                return false;
            }
            if (Float.floatToIntBits(this.fi) != Float.floatToIntBits(o.fi) || this.fj != o.fj || !kr.equals(this.fk, o.fk) || this.fl != o.fl) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public com.google.android.gms.internal.c.f g(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                Object obj;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
                        mh = this.eW == null ? 0 : this.eW.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eW, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.readString();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.readString();
                        this.eW = obj;
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        b = kw.b(knVar, ApiEventType.API_MRAID_GET_CURRENT_POSITION);
                        mh = this.eX == null ? 0 : this.eX.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eX, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.eX = obj;
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_VIBRATE);
                        mh = this.eY == null ? 0 : this.eY.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eY, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.e();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.e();
                        knVar.a(obj[mh]);
                        this.eY = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_UNMUTE_AUDIO);
                        mh = this.eZ == null ? 0 : this.eZ.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eZ, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.b();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.b();
                        knVar.a(obj[mh]);
                        this.eZ = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_UNMUTE_VIDEO);
                        mh = this.fa == null ? 0 : this.fa.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fa, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.b();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.b();
                        knVar.a(obj[mh]);
                        this.fa = obj;
                        break;
                    case ApiEventType.API_MRAID_CLOSE_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_CLOSE_VIDEO);
                        mh = this.fb == null ? 0 : this.fb.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fb, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.b();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.b();
                        knVar.a(obj[mh]);
                        this.fb = obj;
                        break;
                    case 58:
                        b = kw.b(knVar, 58);
                        mh = this.fc == null ? 0 : this.fc.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fc, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.g();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.g();
                        knVar.a(obj[mh]);
                        this.fc = obj;
                        break;
                    case 74:
                        this.fd = knVar.readString();
                        break;
                    case 82:
                        this.fe = knVar.readString();
                        break;
                    case 98:
                        this.ff = knVar.readString();
                        break;
                    case 106:
                        this.fg = knVar.readString();
                        break;
                    case 114:
                        if (this.fh == null) {
                            this.fh = new com.google.android.gms.internal.c.a();
                        }
                        knVar.a(this.fh);
                        break;
                    case 125:
                        this.fi = knVar.readFloat();
                        break;
                    case TransportMediator.KEYCODE_MEDIA_RECORD:
                        b = kw.b(knVar, TransportMediator.KEYCODE_MEDIA_RECORD);
                        mh = this.fk == null ? 0 : this.fk.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fk, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.readString();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.readString();
                        this.fk = obj;
                        break;
                    case 136:
                        this.fl = knVar.mk();
                        break;
                    case 144:
                        this.fj = knVar.ml();
                        break;
                    case 154:
                        b = kw.b(knVar, 154);
                        mh = this.eV == null ? 0 : this.eV.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.eV, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.readString();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.readString();
                        this.eV = obj;
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public int hashCode() {
            int i = 0;
            int hashCode = ((((((this.fj ? 1231 : 1237) + (((((this.fh == null ? 0 : this.fh.hashCode()) + (((this.fg == null ? 0 : this.fg.hashCode()) + (((this.ff == null ? 0 : this.ff.hashCode()) + (((this.fe == null ? 0 : this.fe.hashCode()) + (((this.fd == null ? 0 : this.fd.hashCode()) + ((((((((((((((((kr.hashCode(this.eV) + 527) * 31) + kr.hashCode(this.eW)) * 31) + kr.hashCode(this.eX)) * 31) + kr.hashCode(this.eY)) * 31) + kr.hashCode(this.eZ)) * 31) + kr.hashCode(this.fa)) * 31) + kr.hashCode(this.fb)) * 31) + kr.hashCode(this.fc)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + Float.floatToIntBits(this.fi)) * 31)) * 31) + kr.hashCode(this.fk)) * 31) + this.fl) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i = this.adU.hashCode();
            }
            return hashCode + i;
        }

        public com.google.android.gms.internal.c.f k() {
            this.eV = kw.aef;
            this.eW = kw.aef;
            this.eX = com.google.android.gms.internal.d.a.r();
            this.eY = com.google.android.gms.internal.c.e.i();
            this.eZ = com.google.android.gms.internal.c.b.d();
            this.fa = com.google.android.gms.internal.c.b.d();
            this.fb = com.google.android.gms.internal.c.b.d();
            this.fc = com.google.android.gms.internal.c.g.l();
            this.fd = Preconditions.EMPTY_ARGUMENTS;
            this.fe = Preconditions.EMPTY_ARGUMENTS;
            this.ff = "0";
            this.fg = Preconditions.EMPTY_ARGUMENTS;
            this.fh = null;
            this.fi = 0.0f;
            this.fj = false;
            this.fk = kw.aef;
            this.fl = 0;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }

    public static final class g extends kp<com.google.android.gms.internal.c.g> {
        private static volatile com.google.android.gms.internal.c.g[] fm;
        public int[] fn;
        public int[] fo;
        public int[] fp;
        public int[] fq;
        public int[] fr;
        public int[] fs;
        public int[] ft;
        public int[] fu;
        public int[] fv;
        public int[] fw;

        public g() {
            m();
        }

        public static com.google.android.gms.internal.c.g[] l() {
            if (fm == null) {
                synchronized (kr.adX) {
                    if (fm == null) {
                        fm = new com.google.android.gms.internal.c.g[0];
                    }
                }
            }
            return fm;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.google.android.gms.internal.ko r5) throws java.io.IOException {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.c.g.a(com.google.android.gms.internal.ko):void");
            /*
            r4 = this;
            r1 = 0;
            r0 = r4.fn;
            if (r0 == 0) goto L_0x001b;
        L_0x0005:
            r0 = r4.fn;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x001b;
        L_0x000a:
            r0 = r1;
        L_0x000b:
            r2 = r4.fn;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x001b;
        L_0x0010:
            r2 = 1;
            r3 = r4.fn;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x000b;
        L_0x001b:
            r0 = r4.fo;
            if (r0 == 0) goto L_0x0035;
        L_0x001f:
            r0 = r4.fo;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0035;
        L_0x0024:
            r0 = r1;
        L_0x0025:
            r2 = r4.fo;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0035;
        L_0x002a:
            r2 = 2;
            r3 = r4.fo;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x0025;
        L_0x0035:
            r0 = r4.fp;
            if (r0 == 0) goto L_0x004f;
        L_0x0039:
            r0 = r4.fp;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x004f;
        L_0x003e:
            r0 = r1;
        L_0x003f:
            r2 = r4.fp;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x004f;
        L_0x0044:
            r2 = 3;
            r3 = r4.fp;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x003f;
        L_0x004f:
            r0 = r4.fq;
            if (r0 == 0) goto L_0x0069;
        L_0x0053:
            r0 = r4.fq;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0069;
        L_0x0058:
            r0 = r1;
        L_0x0059:
            r2 = r4.fq;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0069;
        L_0x005e:
            r2 = 4;
            r3 = r4.fq;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x0059;
        L_0x0069:
            r0 = r4.fr;
            if (r0 == 0) goto L_0x0083;
        L_0x006d:
            r0 = r4.fr;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0083;
        L_0x0072:
            r0 = r1;
        L_0x0073:
            r2 = r4.fr;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0083;
        L_0x0078:
            r2 = 5;
            r3 = r4.fr;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x0073;
        L_0x0083:
            r0 = r4.fs;
            if (r0 == 0) goto L_0x009d;
        L_0x0087:
            r0 = r4.fs;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x009d;
        L_0x008c:
            r0 = r1;
        L_0x008d:
            r2 = r4.fs;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x009d;
        L_0x0092:
            r2 = 6;
            r3 = r4.fs;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x008d;
        L_0x009d:
            r0 = r4.ft;
            if (r0 == 0) goto L_0x00b7;
        L_0x00a1:
            r0 = r4.ft;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00b7;
        L_0x00a6:
            r0 = r1;
        L_0x00a7:
            r2 = r4.ft;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00b7;
        L_0x00ac:
            r2 = 7;
            r3 = r4.ft;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x00a7;
        L_0x00b7:
            r0 = r4.fu;
            if (r0 == 0) goto L_0x00d2;
        L_0x00bb:
            r0 = r4.fu;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00d2;
        L_0x00c0:
            r0 = r1;
        L_0x00c1:
            r2 = r4.fu;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00d2;
        L_0x00c6:
            r2 = 8;
            r3 = r4.fu;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x00c1;
        L_0x00d2:
            r0 = r4.fv;
            if (r0 == 0) goto L_0x00ed;
        L_0x00d6:
            r0 = r4.fv;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00ed;
        L_0x00db:
            r0 = r1;
        L_0x00dc:
            r2 = r4.fv;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00ed;
        L_0x00e1:
            r2 = 9;
            r3 = r4.fv;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x00dc;
        L_0x00ed:
            r0 = r4.fw;
            if (r0 == 0) goto L_0x0107;
        L_0x00f1:
            r0 = r4.fw;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0107;
        L_0x00f6:
            r0 = r4.fw;
            r0 = r0.length;
            if (r1 >= r0) goto L_0x0107;
        L_0x00fb:
            r0 = 10;
            r2 = r4.fw;
            r2 = r2[r1];
            r5.i(r0, r2);
            r1 = r1 + 1;
            goto L_0x00f6;
        L_0x0107:
            super.a(r5);
            return;
            */
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return h(knVar);
        }

        public int c() {
            int i;
            int i2;
            int i3 = 0;
            int c = super.c();
            if (this.fn == null || this.fn.length <= 0) {
                i = c;
            } else {
                i = 0;
                i2 = 0;
                while (i < this.fn.length) {
                    i2 += ko.cX(this.fn[i]);
                    i++;
                }
                i = c + i2 + this.fn.length * 1;
            }
            if (this.fo != null && this.fo.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fo.length) {
                    c += ko.cX(this.fo[i2]);
                    i2++;
                }
                i = i + c + this.fo.length * 1;
            }
            if (this.fp != null && this.fp.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fp.length) {
                    c += ko.cX(this.fp[i2]);
                    i2++;
                }
                i = i + c + this.fp.length * 1;
            }
            if (this.fq != null && this.fq.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fq.length) {
                    c += ko.cX(this.fq[i2]);
                    i2++;
                }
                i = i + c + this.fq.length * 1;
            }
            if (this.fr != null && this.fr.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fr.length) {
                    c += ko.cX(this.fr[i2]);
                    i2++;
                }
                i = i + c + this.fr.length * 1;
            }
            if (this.fs != null && this.fs.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fs.length) {
                    c += ko.cX(this.fs[i2]);
                    i2++;
                }
                i = i + c + this.fs.length * 1;
            }
            if (this.ft != null && this.ft.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.ft.length) {
                    c += ko.cX(this.ft[i2]);
                    i2++;
                }
                i = i + c + this.ft.length * 1;
            }
            if (this.fu != null && this.fu.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fu.length) {
                    c += ko.cX(this.fu[i2]);
                    i2++;
                }
                i = i + c + this.fu.length * 1;
            }
            if (this.fv != null && this.fv.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fv.length) {
                    c += ko.cX(this.fv[i2]);
                    i2++;
                }
                i = i + c + this.fv.length * 1;
            }
            if (this.fw != null && this.fw.length > 0) {
                i2 = 0;
                while (i3 < this.fw.length) {
                    i2 += ko.cX(this.fw[i3]);
                    i3++;
                }
                i = i + i2 + this.fw.length * 1;
            }
            this.adY = i;
            return i;
        }

        public boolean equals(com.google.android.gms.internal.c.g o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.g)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.fn, o.fn) || !kr.equals(this.fo, o.fo) || !kr.equals(this.fp, o.fp) || !kr.equals(this.fq, o.fq) || !kr.equals(this.fr, o.fr) || !kr.equals(this.fs, o.fs) || !kr.equals(this.ft, o.ft) || !kr.equals(this.fu, o.fu) || !kr.equals(this.fv, o.fv) || !kr.equals(this.fw, o.fw)) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public com.google.android.gms.internal.c.g h(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                Object obj;
                int cR;
                Object obj2;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        b = kw.b(knVar, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                        mh = this.fn == null ? 0 : this.fn.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fn, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fn = obj;
                        break;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fn == null ? 0 : this.fn.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fn, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fn = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        b = kw.b(knVar, ApiEventType.API_MRAID_GET_ORIENTATION);
                        mh = this.fo == null ? 0 : this.fo.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fo, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fo = obj;
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fo == null ? 0 : this.fo.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fo, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fo = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE);
                        mh = this.fp == null ? 0 : this.fp.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fp, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fp = obj;
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fp == null ? 0 : this.fp.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fp, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fp = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_PLAY_AUDIO);
                        mh = this.fq == null ? 0 : this.fq.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fq, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fq = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fq == null ? 0 : this.fq.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fq, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fq = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_PLAY_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_PLAY_VIDEO);
                        mh = this.fr == null ? 0 : this.fr.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fr, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fr = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fr == null ? 0 : this.fr.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fr, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fr = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_HIDE_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_HIDE_VIDEO);
                        mh = this.fs == null ? 0 : this.fs.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fs, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fs = obj;
                        break;
                    case ApiEventType.API_MRAID_CLOSE_VIDEO:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fs == null ? 0 : this.fs.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fs, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fs = obj2;
                        knVar.cS(cR);
                        break;
                    case 56:
                        b = kw.b(knVar, 56);
                        mh = this.ft == null ? 0 : this.ft.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.ft, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.ft = obj;
                        break;
                    case 58:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.ft == null ? 0 : this.ft.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.ft, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.ft = obj2;
                        knVar.cS(cR);
                        break;
                    case TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD:
                        b = kw.b(knVar, TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD);
                        mh = this.fu == null ? 0 : this.fu.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fu, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fu = obj;
                        break;
                    case 66:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fu == null ? 0 : this.fu.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fu, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fu = obj2;
                        knVar.cS(cR);
                        break;
                    case 72:
                        b = kw.b(knVar, 72);
                        mh = this.fv == null ? 0 : this.fv.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fv, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fv = obj;
                        break;
                    case 74:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fv == null ? 0 : this.fv.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fv, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fv = obj2;
                        knVar.cS(cR);
                        break;
                    case VservConstants.THREAD_SLEEP:
                        b = kw.b(knVar, VservConstants.THREAD_SLEEP);
                        mh = this.fw == null ? 0 : this.fw.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fw, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fw = obj;
                        break;
                    case 82:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fw == null ? 0 : this.fw.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fw, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fw = obj2;
                        knVar.cS(cR);
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public int hashCode() {
            int hashCode = (((((((((((((((((((kr.hashCode(this.fn) + 527) * 31) + kr.hashCode(this.fo)) * 31) + kr.hashCode(this.fp)) * 31) + kr.hashCode(this.fq)) * 31) + kr.hashCode(this.fr)) * 31) + kr.hashCode(this.fs)) * 31) + kr.hashCode(this.ft)) * 31) + kr.hashCode(this.fu)) * 31) + kr.hashCode(this.fv)) * 31) + kr.hashCode(this.fw)) * 31;
            int hashCode2 = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode2 + hashCode;
        }

        public com.google.android.gms.internal.c.g m() {
            this.fn = kw.aea;
            this.fo = kw.aea;
            this.fp = kw.aea;
            this.fq = kw.aea;
            this.fr = kw.aea;
            this.fs = kw.aea;
            this.ft = kw.aea;
            this.fu = kw.aea;
            this.fv = kw.aea;
            this.fw = kw.aea;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }

    public static final class h extends kp<com.google.android.gms.internal.c.h> {
        public static final kq<com.google.android.gms.internal.d.a, com.google.android.gms.internal.c.h> fx;
        private static final com.google.android.gms.internal.c.h[] fy;
        public int[] fA;
        public int[] fB;
        public int fC;
        public int[] fD;
        public int fE;
        public int fF;
        public int[] fz;

        static {
            fx = kq.a(ApiEventType.API_MRAID_EXPAND, com.google.android.gms.internal.c.h.class, 810);
            fy = new com.google.android.gms.internal.c.h[0];
        }

        public h() {
            n();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.google.android.gms.internal.ko r5) throws java.io.IOException {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.c.h.a(com.google.android.gms.internal.ko):void");
            /*
            r4 = this;
            r1 = 0;
            r0 = r4.fz;
            if (r0 == 0) goto L_0x001b;
        L_0x0005:
            r0 = r4.fz;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x001b;
        L_0x000a:
            r0 = r1;
        L_0x000b:
            r2 = r4.fz;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x001b;
        L_0x0010:
            r2 = 1;
            r3 = r4.fz;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x000b;
        L_0x001b:
            r0 = r4.fA;
            if (r0 == 0) goto L_0x0035;
        L_0x001f:
            r0 = r4.fA;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0035;
        L_0x0024:
            r0 = r1;
        L_0x0025:
            r2 = r4.fA;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0035;
        L_0x002a:
            r2 = 2;
            r3 = r4.fA;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x0025;
        L_0x0035:
            r0 = r4.fB;
            if (r0 == 0) goto L_0x004f;
        L_0x0039:
            r0 = r4.fB;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x004f;
        L_0x003e:
            r0 = r1;
        L_0x003f:
            r2 = r4.fB;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x004f;
        L_0x0044:
            r2 = 3;
            r3 = r4.fB;
            r3 = r3[r0];
            r5.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x003f;
        L_0x004f:
            r0 = r4.fC;
            if (r0 == 0) goto L_0x0059;
        L_0x0053:
            r0 = 4;
            r2 = r4.fC;
            r5.i(r0, r2);
        L_0x0059:
            r0 = r4.fD;
            if (r0 == 0) goto L_0x0072;
        L_0x005d:
            r0 = r4.fD;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0072;
        L_0x0062:
            r0 = r4.fD;
            r0 = r0.length;
            if (r1 >= r0) goto L_0x0072;
        L_0x0067:
            r0 = 5;
            r2 = r4.fD;
            r2 = r2[r1];
            r5.i(r0, r2);
            r1 = r1 + 1;
            goto L_0x0062;
        L_0x0072:
            r0 = r4.fE;
            if (r0 == 0) goto L_0x007c;
        L_0x0076:
            r0 = 6;
            r1 = r4.fE;
            r5.i(r0, r1);
        L_0x007c:
            r0 = r4.fF;
            if (r0 == 0) goto L_0x0086;
        L_0x0080:
            r0 = 7;
            r1 = r4.fF;
            r5.i(r0, r1);
        L_0x0086:
            super.a(r5);
            return;
            */
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return i(knVar);
        }

        public int c() {
            int i;
            int i2;
            int i3 = 0;
            int c = super.c();
            if (this.fz == null || this.fz.length <= 0) {
                i = c;
            } else {
                i = 0;
                i2 = 0;
                while (i < this.fz.length) {
                    i2 += ko.cX(this.fz[i]);
                    i++;
                }
                i = c + i2 + this.fz.length * 1;
            }
            if (this.fA != null && this.fA.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fA.length) {
                    c += ko.cX(this.fA[i2]);
                    i2++;
                }
                i = i + c + this.fA.length * 1;
            }
            if (this.fB != null && this.fB.length > 0) {
                i2 = 0;
                c = 0;
                while (i2 < this.fB.length) {
                    c += ko.cX(this.fB[i2]);
                    i2++;
                }
                i = i + c + this.fB.length * 1;
            }
            if (this.fC != 0) {
                i += ko.j(MMAdView.TRANSITION_RANDOM, this.fC);
            }
            if (this.fD != null && this.fD.length > 0) {
                i2 = 0;
                while (i3 < this.fD.length) {
                    i2 += ko.cX(this.fD[i3]);
                    i3++;
                }
                i = i + i2 + this.fD.length * 1;
            }
            if (this.fE != 0) {
                i += ko.j(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, this.fE);
            }
            if (this.fF != 0) {
                i += ko.j(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, this.fF);
            }
            this.adY = i;
            return i;
        }

        public boolean equals(com.google.android.gms.internal.c.h o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.h)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.fz, o.fz) || !kr.equals(this.fA, o.fA) || !kr.equals(this.fB, o.fB) || this.fC != o.fC || !kr.equals(this.fD, o.fD) || this.fE != o.fE || this.fF != o.fF) {
                return false;
            }
            if (this.adU == null || this.adU.isEmpty()) {
                return o.adU == null || o.adU.isEmpty();
            } else {
                return this.adU.equals(o.adU);
            }
        }

        public int hashCode() {
            int hashCode = (((((((((((((kr.hashCode(this.fz) + 527) * 31) + kr.hashCode(this.fA)) * 31) + kr.hashCode(this.fB)) * 31) + this.fC) * 31) + kr.hashCode(this.fD)) * 31) + this.fE) * 31) + this.fF) * 31;
            int hashCode2 = (this.adU == null || this.adU.isEmpty()) ? 0 : this.adU.hashCode();
            return hashCode2 + hashCode;
        }

        public com.google.android.gms.internal.c.h i(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                Object obj;
                int cR;
                Object obj2;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        b = kw.b(knVar, ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES);
                        mh = this.fz == null ? 0 : this.fz.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fz, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fz = obj;
                        break;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fz == null ? 0 : this.fz.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fz, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fz = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                        b = kw.b(knVar, ApiEventType.API_MRAID_GET_ORIENTATION);
                        mh = this.fA == null ? 0 : this.fA.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fA, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fA = obj;
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fA == null ? 0 : this.fA.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fA, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fA = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE);
                        mh = this.fB == null ? 0 : this.fB.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fB, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fB = obj;
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fB == null ? 0 : this.fB.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fB, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fB = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_PLAY_AUDIO:
                        this.fC = knVar.mk();
                        break;
                    case ApiEventType.API_MRAID_PLAY_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_PLAY_VIDEO);
                        mh = this.fD == null ? 0 : this.fD.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fD, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = knVar.mk();
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = knVar.mk();
                        this.fD = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                        cR = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            knVar.mk();
                            mh++;
                        }
                        knVar.cT(b);
                        b = this.fD == null ? 0 : this.fD.length;
                        obj2 = new Object[(mh + b)];
                        if (b != 0) {
                            System.arraycopy(this.fD, 0, obj2, 0, b);
                        }
                        while (b < obj2.length) {
                            obj2[b] = knVar.mk();
                            b++;
                        }
                        this.fD = obj2;
                        knVar.cS(cR);
                        break;
                    case ApiEventType.API_MRAID_HIDE_VIDEO:
                        this.fE = knVar.mk();
                        break;
                    case 56:
                        this.fF = knVar.mk();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public com.google.android.gms.internal.c.h n() {
            this.fz = kw.aea;
            this.fA = kw.aea;
            this.fB = kw.aea;
            this.fC = 0;
            this.fD = kw.aea;
            this.fE = 0;
            this.fF = 0;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }

    public static final class i extends kp<com.google.android.gms.internal.c.i> {
        private static volatile com.google.android.gms.internal.c.i[] fG;
        public com.google.android.gms.internal.d.a fH;
        public com.google.android.gms.internal.c.d fI;
        public String name;

        public i() {
            p();
        }

        public static com.google.android.gms.internal.c.i[] o() {
            if (fG == null) {
                synchronized (kr.adX) {
                    if (fG == null) {
                        fG = new com.google.android.gms.internal.c.i[0];
                    }
                }
            }
            return fG;
        }

        public void a(ko koVar) throws IOException {
            if (!this.name.equals(Preconditions.EMPTY_ARGUMENTS)) {
                koVar.b(1, this.name);
            }
            if (this.fH != null) {
                koVar.a((int)MMAdView.TRANSITION_UP, this.fH);
            }
            if (this.fI != null) {
                koVar.a((int)MMAdView.TRANSITION_DOWN, this.fI);
            }
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return j(knVar);
        }

        public int c() {
            int c = super.c();
            if (!this.name.equals(Preconditions.EMPTY_ARGUMENTS)) {
                c += ko.g(1, this.name);
            }
            if (this.fH != null) {
                c += ko.b((int)MMAdView.TRANSITION_UP, this.fH);
            }
            if (this.fI != null) {
                c += ko.b((int)MMAdView.TRANSITION_DOWN, this.fI);
            }
            this.adY = c;
            return c;
        }

        public boolean equals(com.google.android.gms.internal.c.i o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.i)) {
                return false;
            }
            o = o;
            if (this.name == null) {
                if (o.name != null) {
                    return false;
                }
            } else if (!this.name.equals(o.name)) {
                return false;
            }
            if (this.fH == null) {
                if (o.fH != null) {
                    return false;
                }
            } else if (!this.fH.equals(o.fH)) {
                return false;
            }
            if (this.fI == null) {
                if (o.fI != null) {
                    return false;
                }
            } else if (!this.fI.equals(o.fI)) {
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
            int hashCode = ((this.fI == null ? 0 : this.fI.hashCode()) + (((this.fH == null ? 0 : this.fH.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + 527) * 31)) * 31)) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i = this.adU.hashCode();
            }
            return hashCode + i;
        }

        public com.google.android.gms.internal.c.i j(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        this.name = knVar.readString();
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        if (this.fH == null) {
                            this.fH = new com.google.android.gms.internal.d.a();
                        }
                        knVar.a(this.fH);
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        if (this.fI == null) {
                            this.fI = new com.google.android.gms.internal.c.d();
                        }
                        knVar.a(this.fI);
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public com.google.android.gms.internal.c.i p() {
            this.name = Preconditions.EMPTY_ARGUMENTS;
            this.fH = null;
            this.fI = null;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }

    public static final class j extends kp<com.google.android.gms.internal.c.j> {
        public com.google.android.gms.internal.c.i[] fJ;
        public com.google.android.gms.internal.c.f fK;
        public String fL;

        public j() {
            q();
        }

        public static com.google.android.gms.internal.c.j b(byte[] bArr) throws ks {
            return (com.google.android.gms.internal.c.j) kt.a(new com.google.android.gms.internal.c.j(), bArr);
        }

        public void a(ko koVar) throws IOException {
            if (this.fJ != null && this.fJ.length > 0) {
                int i = 0;
                while (i < this.fJ.length) {
                    kt ktVar = this.fJ[i];
                    if (ktVar != null) {
                        koVar.a(1, ktVar);
                    }
                    i++;
                }
            }
            if (this.fK != null) {
                koVar.a((int)MMAdView.TRANSITION_UP, this.fK);
            }
            if (!this.fL.equals(Preconditions.EMPTY_ARGUMENTS)) {
                koVar.b((int)MMAdView.TRANSITION_DOWN, this.fL);
            }
            super.a(koVar);
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return k(knVar);
        }

        public int c() {
            int c = super.c();
            if (this.fJ != null && this.fJ.length > 0) {
                int i = 0;
                while (i < this.fJ.length) {
                    kt ktVar = this.fJ[i];
                    if (ktVar != null) {
                        c += ko.b(1, ktVar);
                    }
                    i++;
                }
            }
            if (this.fK != null) {
                c += ko.b((int)MMAdView.TRANSITION_UP, this.fK);
            }
            if (!this.fL.equals(Preconditions.EMPTY_ARGUMENTS)) {
                c += ko.g(MMAdView.TRANSITION_DOWN, this.fL);
            }
            this.adY = c;
            return c;
        }

        public boolean equals(com.google.android.gms.internal.c.j o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.c.j)) {
                return false;
            }
            o = o;
            if (!kr.equals(this.fJ, o.fJ)) {
                return false;
            }
            if (this.fK == null) {
                if (o.fK != null) {
                    return false;
                }
            } else if (!this.fK.equals(o.fK)) {
                return false;
            }
            if (this.fL == null) {
                if (o.fL != null) {
                    return false;
                }
            } else if (!this.fL.equals(o.fL)) {
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
            int hashCode = ((this.fL == null ? 0 : this.fL.hashCode()) + (((this.fK == null ? 0 : this.fK.hashCode()) + ((kr.hashCode(this.fJ) + 527) * 31)) * 31)) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i = this.adU.hashCode();
            }
            return hashCode + i;
        }

        public com.google.android.gms.internal.c.j k(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                        int b = kw.b(knVar, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE);
                        mh = this.fJ == null ? 0 : this.fJ.length;
                        Object obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fJ, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.c.i();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.c.i();
                        knVar.a(obj[mh]);
                        this.fJ = obj;
                        break;
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        if (this.fK == null) {
                            this.fK = new com.google.android.gms.internal.c.f();
                        }
                        knVar.a(this.fK);
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        this.fL = knVar.readString();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public com.google.android.gms.internal.c.j q() {
            this.fJ = com.google.android.gms.internal.c.i.o();
            this.fK = null;
            this.fL = Preconditions.EMPTY_ARGUMENTS;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }
}