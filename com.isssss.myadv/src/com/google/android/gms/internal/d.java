package com.google.android.gms.internal;

import android.support.v4.media.TransportMediator;
import com.google.ads.AdSize;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;
import mobi.vserv.android.ads.VservConstants;

public interface d {

    public static final class a extends kp<com.google.android.gms.internal.d.a> {
        private static volatile com.google.android.gms.internal.d.a[] fM;
        public String fN;
        public com.google.android.gms.internal.d.a[] fO;
        public com.google.android.gms.internal.d.a[] fP;
        public com.google.android.gms.internal.d.a[] fQ;
        public String fR;
        public String fS;
        public long fT;
        public boolean fU;
        public com.google.android.gms.internal.d.a[] fV;
        public int[] fW;
        public boolean fX;
        public int type;

        public a() {
            s();
        }

        public static com.google.android.gms.internal.d.a[] r() {
            if (fM == null) {
                synchronized (kr.adX) {
                    if (fM == null) {
                        fM = new com.google.android.gms.internal.d.a[0];
                    }
                }
            }
            return fM;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(com.google.android.gms.internal.ko r7) throws java.io.IOException {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.d.a.a(com.google.android.gms.internal.ko):void");
            /*
            r6 = this;
            r1 = 0;
            r0 = 1;
            r2 = r6.type;
            r7.i(r0, r2);
            r0 = r6.fN;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x0017;
        L_0x0011:
            r0 = 2;
            r2 = r6.fN;
            r7.b(r0, r2);
        L_0x0017:
            r0 = r6.fO;
            if (r0 == 0) goto L_0x0033;
        L_0x001b:
            r0 = r6.fO;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x0033;
        L_0x0020:
            r0 = r1;
        L_0x0021:
            r2 = r6.fO;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x0033;
        L_0x0026:
            r2 = r6.fO;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0030;
        L_0x002c:
            r3 = 3;
            r7.a(r3, r2);
        L_0x0030:
            r0 = r0 + 1;
            goto L_0x0021;
        L_0x0033:
            r0 = r6.fP;
            if (r0 == 0) goto L_0x004f;
        L_0x0037:
            r0 = r6.fP;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x004f;
        L_0x003c:
            r0 = r1;
        L_0x003d:
            r2 = r6.fP;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x004f;
        L_0x0042:
            r2 = r6.fP;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x004c;
        L_0x0048:
            r3 = 4;
            r7.a(r3, r2);
        L_0x004c:
            r0 = r0 + 1;
            goto L_0x003d;
        L_0x004f:
            r0 = r6.fQ;
            if (r0 == 0) goto L_0x006b;
        L_0x0053:
            r0 = r6.fQ;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x006b;
        L_0x0058:
            r0 = r1;
        L_0x0059:
            r2 = r6.fQ;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x006b;
        L_0x005e:
            r2 = r6.fQ;
            r2 = r2[r0];
            if (r2 == 0) goto L_0x0068;
        L_0x0064:
            r3 = 5;
            r7.a(r3, r2);
        L_0x0068:
            r0 = r0 + 1;
            goto L_0x0059;
        L_0x006b:
            r0 = r6.fR;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x007b;
        L_0x0075:
            r0 = 6;
            r2 = r6.fR;
            r7.b(r0, r2);
        L_0x007b:
            r0 = r6.fS;
            r2 = "";
            r0 = r0.equals(r2);
            if (r0 != 0) goto L_0x008b;
        L_0x0085:
            r0 = 7;
            r2 = r6.fS;
            r7.b(r0, r2);
        L_0x008b:
            r2 = r6.fT;
            r4 = 0;
            r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r0 == 0) goto L_0x009a;
        L_0x0093:
            r0 = 8;
            r2 = r6.fT;
            r7.b(r0, r2);
        L_0x009a:
            r0 = r6.fX;
            if (r0 == 0) goto L_0x00a5;
        L_0x009e:
            r0 = 9;
            r2 = r6.fX;
            r7.a(r0, r2);
        L_0x00a5:
            r0 = r6.fW;
            if (r0 == 0) goto L_0x00c0;
        L_0x00a9:
            r0 = r6.fW;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00c0;
        L_0x00ae:
            r0 = r1;
        L_0x00af:
            r2 = r6.fW;
            r2 = r2.length;
            if (r0 >= r2) goto L_0x00c0;
        L_0x00b4:
            r2 = 10;
            r3 = r6.fW;
            r3 = r3[r0];
            r7.i(r2, r3);
            r0 = r0 + 1;
            goto L_0x00af;
        L_0x00c0:
            r0 = r6.fV;
            if (r0 == 0) goto L_0x00dc;
        L_0x00c4:
            r0 = r6.fV;
            r0 = r0.length;
            if (r0 <= 0) goto L_0x00dc;
        L_0x00c9:
            r0 = r6.fV;
            r0 = r0.length;
            if (r1 >= r0) goto L_0x00dc;
        L_0x00ce:
            r0 = r6.fV;
            r0 = r0[r1];
            if (r0 == 0) goto L_0x00d9;
        L_0x00d4:
            r2 = 11;
            r7.a(r2, r0);
        L_0x00d9:
            r1 = r1 + 1;
            goto L_0x00c9;
        L_0x00dc:
            r0 = r6.fU;
            if (r0 == 0) goto L_0x00e7;
        L_0x00e0:
            r0 = 12;
            r1 = r6.fU;
            r7.a(r0, r1);
        L_0x00e7:
            super.a(r7);
            return;
            */
        }

        public /* synthetic */ kt b(kn knVar) throws IOException {
            return l(knVar);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int c() {
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.d.a.c():int");
            /*
            r6 = this;
            r1 = 0;
            r0 = super.c();
            r2 = 1;
            r3 = r6.type;
            r2 = com.google.android.gms.internal.ko.j(r2, r3);
            r0 = r0 + r2;
            r2 = r6.fN;
            r3 = "";
            r2 = r2.equals(r3);
            if (r2 != 0) goto L_0x001f;
        L_0x0017:
            r2 = 2;
            r3 = r6.fN;
            r2 = com.google.android.gms.internal.ko.g(r2, r3);
            r0 = r0 + r2;
        L_0x001f:
            r2 = r6.fO;
            if (r2 == 0) goto L_0x003f;
        L_0x0023:
            r2 = r6.fO;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x003f;
        L_0x0028:
            r2 = r0;
            r0 = r1;
        L_0x002a:
            r3 = r6.fO;
            r3 = r3.length;
            if (r0 >= r3) goto L_0x003e;
        L_0x002f:
            r3 = r6.fO;
            r3 = r3[r0];
            if (r3 == 0) goto L_0x003b;
        L_0x0035:
            r4 = 3;
            r3 = com.google.android.gms.internal.ko.b(r4, r3);
            r2 = r2 + r3;
        L_0x003b:
            r0 = r0 + 1;
            goto L_0x002a;
        L_0x003e:
            r0 = r2;
        L_0x003f:
            r2 = r6.fP;
            if (r2 == 0) goto L_0x005f;
        L_0x0043:
            r2 = r6.fP;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x005f;
        L_0x0048:
            r2 = r0;
            r0 = r1;
        L_0x004a:
            r3 = r6.fP;
            r3 = r3.length;
            if (r0 >= r3) goto L_0x005e;
        L_0x004f:
            r3 = r6.fP;
            r3 = r3[r0];
            if (r3 == 0) goto L_0x005b;
        L_0x0055:
            r4 = 4;
            r3 = com.google.android.gms.internal.ko.b(r4, r3);
            r2 = r2 + r3;
        L_0x005b:
            r0 = r0 + 1;
            goto L_0x004a;
        L_0x005e:
            r0 = r2;
        L_0x005f:
            r2 = r6.fQ;
            if (r2 == 0) goto L_0x007f;
        L_0x0063:
            r2 = r6.fQ;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x007f;
        L_0x0068:
            r2 = r0;
            r0 = r1;
        L_0x006a:
            r3 = r6.fQ;
            r3 = r3.length;
            if (r0 >= r3) goto L_0x007e;
        L_0x006f:
            r3 = r6.fQ;
            r3 = r3[r0];
            if (r3 == 0) goto L_0x007b;
        L_0x0075:
            r4 = 5;
            r3 = com.google.android.gms.internal.ko.b(r4, r3);
            r2 = r2 + r3;
        L_0x007b:
            r0 = r0 + 1;
            goto L_0x006a;
        L_0x007e:
            r0 = r2;
        L_0x007f:
            r2 = r6.fR;
            r3 = "";
            r2 = r2.equals(r3);
            if (r2 != 0) goto L_0x0091;
        L_0x0089:
            r2 = 6;
            r3 = r6.fR;
            r2 = com.google.android.gms.internal.ko.g(r2, r3);
            r0 = r0 + r2;
        L_0x0091:
            r2 = r6.fS;
            r3 = "";
            r2 = r2.equals(r3);
            if (r2 != 0) goto L_0x00a3;
        L_0x009b:
            r2 = 7;
            r3 = r6.fS;
            r2 = com.google.android.gms.internal.ko.g(r2, r3);
            r0 = r0 + r2;
        L_0x00a3:
            r2 = r6.fT;
            r4 = 0;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 == 0) goto L_0x00b4;
        L_0x00ab:
            r2 = 8;
            r3 = r6.fT;
            r2 = com.google.android.gms.internal.ko.d(r2, r3);
            r0 = r0 + r2;
        L_0x00b4:
            r2 = r6.fX;
            if (r2 == 0) goto L_0x00c1;
        L_0x00b8:
            r2 = 9;
            r3 = r6.fX;
            r2 = com.google.android.gms.internal.ko.b(r2, r3);
            r0 = r0 + r2;
        L_0x00c1:
            r2 = r6.fW;
            if (r2 == 0) goto L_0x00e4;
        L_0x00c5:
            r2 = r6.fW;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x00e4;
        L_0x00ca:
            r2 = r1;
            r3 = r1;
        L_0x00cc:
            r4 = r6.fW;
            r4 = r4.length;
            if (r2 >= r4) goto L_0x00dd;
        L_0x00d1:
            r4 = r6.fW;
            r4 = r4[r2];
            r4 = com.google.android.gms.internal.ko.cX(r4);
            r3 = r3 + r4;
            r2 = r2 + 1;
            goto L_0x00cc;
        L_0x00dd:
            r0 = r0 + r3;
            r2 = r6.fW;
            r2 = r2.length;
            r2 = r2 * 1;
            r0 = r0 + r2;
        L_0x00e4:
            r2 = r6.fV;
            if (r2 == 0) goto L_0x0102;
        L_0x00e8:
            r2 = r6.fV;
            r2 = r2.length;
            if (r2 <= 0) goto L_0x0102;
        L_0x00ed:
            r2 = r6.fV;
            r2 = r2.length;
            if (r1 >= r2) goto L_0x0102;
        L_0x00f2:
            r2 = r6.fV;
            r2 = r2[r1];
            if (r2 == 0) goto L_0x00ff;
        L_0x00f8:
            r3 = 11;
            r2 = com.google.android.gms.internal.ko.b(r3, r2);
            r0 = r0 + r2;
        L_0x00ff:
            r1 = r1 + 1;
            goto L_0x00ed;
        L_0x0102:
            r1 = r6.fU;
            if (r1 == 0) goto L_0x010f;
        L_0x0106:
            r1 = 12;
            r2 = r6.fU;
            r1 = com.google.android.gms.internal.ko.b(r1, r2);
            r0 = r0 + r1;
        L_0x010f:
            r6.adY = r0;
            return r0;
            */
        }

        public boolean equals(com.google.android.gms.internal.d.a o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.google.android.gms.internal.d.a)) {
                return false;
            }
            o = o;
            if (this.type != o.type) {
                return false;
            }
            if (this.fN == null) {
                if (o.fN != null) {
                    return false;
                }
            } else if (!this.fN.equals(o.fN)) {
                return false;
            }
            if (!kr.equals(this.fO, o.fO) || !kr.equals(this.fP, o.fP) || !kr.equals(this.fQ, o.fQ)) {
                return false;
            }
            if (this.fR == null) {
                if (o.fR != null) {
                    return false;
                }
            } else if (!this.fR.equals(o.fR)) {
                return false;
            }
            if (this.fS == null) {
                if (o.fS != null) {
                    return false;
                }
            } else if (!this.fS.equals(o.fS)) {
                return false;
            }
            if (this.fT != o.fT || this.fU != o.fU || !kr.equals(this.fV, o.fV) || !kr.equals(this.fW, o.fW) || this.fX != o.fX) {
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
            int i2 = 0;
            int hashCode = ((((((this.fU ? 1231 : 1237) + (((((this.fS == null ? 0 : this.fS.hashCode()) + (((this.fR == null ? 0 : this.fR.hashCode()) + (((((((((this.fN == null ? 0 : this.fN.hashCode()) + ((this.type + 527) * 31)) * 31) + kr.hashCode(this.fO)) * 31) + kr.hashCode(this.fP)) * 31) + kr.hashCode(this.fQ)) * 31)) * 31)) * 31) + ((int) (this.fT ^ (this.fT >>> 32)))) * 31)) * 31) + kr.hashCode(this.fV)) * 31) + kr.hashCode(this.fW)) * 31;
            if (!this.fX) {
                i = 1237;
            }
            hashCode = (hashCode + i) * 31;
            if (!(this.adU == null || this.adU.isEmpty())) {
                i2 = this.adU.hashCode();
            }
            return hashCode + i2;
        }

        public com.google.android.gms.internal.d.a l(kn knVar) throws IOException {
            while (true) {
                int mh = knVar.mh();
                int b;
                Object obj;
                int i;
                switch (mh) {
                    case MMAdView.TRANSITION_NONE:
                        return this;
                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                        mh = knVar.mk();
                        switch (mh) {
                            case MMAdView.TRANSITION_FADE:
                            case MMAdView.TRANSITION_UP:
                            case MMAdView.TRANSITION_DOWN:
                            case MMAdView.TRANSITION_RANDOM:
                            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                                this.type = mh;
                                break;
                            default:
                                break;
                        }
                    case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                        this.fN = knVar.readString();
                        break;
                    case ApiEventType.API_MRAID_VIBRATE:
                        b = kw.b(knVar, ApiEventType.API_MRAID_VIBRATE);
                        mh = this.fO == null ? 0 : this.fO.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fO, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.fO = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_UNMUTE_AUDIO);
                        mh = this.fP == null ? 0 : this.fP.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fP, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.fP = obj;
                        break;
                    case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                        b = kw.b(knVar, ApiEventType.API_MRAID_UNMUTE_VIDEO);
                        mh = this.fQ == null ? 0 : this.fQ.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fQ, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.fQ = obj;
                        break;
                    case ApiEventType.API_MRAID_CLOSE_VIDEO:
                        this.fR = knVar.readString();
                        break;
                    case 58:
                        this.fS = knVar.readString();
                        break;
                    case TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD:
                        this.fT = knVar.mj();
                        break;
                    case 72:
                        this.fX = knVar.ml();
                        break;
                    case VservConstants.THREAD_SLEEP:
                        int b2 = kw.b(knVar, VservConstants.THREAD_SLEEP);
                        Object obj2 = new Object[b2];
                        i = 0;
                        b = 0;
                        while (i < b2) {
                            if (i != 0) {
                                knVar.mh();
                            }
                            int mk = knVar.mk();
                            switch (mk) {
                                case MMAdView.TRANSITION_FADE:
                                case MMAdView.TRANSITION_UP:
                                case MMAdView.TRANSITION_DOWN:
                                case MMAdView.TRANSITION_RANDOM:
                                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                                case ApiEventType.API_MRAID_EXPAND:
                                case ApiEventType.API_MRAID_RESIZE:
                                case ApiEventType.API_MRAID_CLOSE:
                                case ApiEventType.API_MRAID_IS_VIEWABLE:
                                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                                case ApiEventType.API_MRAID_GET_ORIENTATION:
                                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                                    mh = b + 1;
                                    obj2[b] = mk;
                                    break;
                                default:
                                    mh = b;
                                    break;
                            }
                            i++;
                            b = mh;
                        }
                        if (b != 0) {
                            mh = this.fW == null ? 0 : this.fW.length;
                            if (mh == 0 && b == obj2.length) {
                                this.fW = obj2;
                            } else {
                                Object obj3 = new Object[(mh + b)];
                                if (mh != 0) {
                                    System.arraycopy(this.fW, 0, obj3, 0, mh);
                                }
                                System.arraycopy(obj2, 0, obj3, mh, b);
                                this.fW = obj3;
                            }
                        }
                        break;
                    case 82:
                        i = knVar.cR(knVar.mn());
                        b = knVar.getPosition();
                        mh = 0;
                        while (knVar.ms() > 0) {
                            switch (knVar.mk()) {
                                case MMAdView.TRANSITION_FADE:
                                case MMAdView.TRANSITION_UP:
                                case MMAdView.TRANSITION_DOWN:
                                case MMAdView.TRANSITION_RANDOM:
                                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                                case ApiEventType.API_MRAID_EXPAND:
                                case ApiEventType.API_MRAID_RESIZE:
                                case ApiEventType.API_MRAID_CLOSE:
                                case ApiEventType.API_MRAID_IS_VIEWABLE:
                                case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                                case ApiEventType.API_MRAID_GET_ORIENTATION:
                                case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                                    mh++;
                                    break;
                                default:
                                    break;
                            }
                        }
                        if (mh != 0) {
                            knVar.cT(b);
                            b = this.fW == null ? 0 : this.fW.length;
                            Object obj4 = new Object[(mh + b)];
                            if (b != 0) {
                                System.arraycopy(this.fW, 0, obj4, 0, b);
                            }
                            while (knVar.ms() > 0) {
                                int mk2 = knVar.mk();
                                switch (mk2) {
                                    case MMAdView.TRANSITION_FADE:
                                    case MMAdView.TRANSITION_UP:
                                    case MMAdView.TRANSITION_DOWN:
                                    case MMAdView.TRANSITION_RANDOM:
                                    case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                                    case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                                    case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                                    case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                                    case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                                    case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                                    case ApiEventType.API_MRAID_EXPAND:
                                    case ApiEventType.API_MRAID_RESIZE:
                                    case ApiEventType.API_MRAID_CLOSE:
                                    case ApiEventType.API_MRAID_IS_VIEWABLE:
                                    case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                                    case ApiEventType.API_MRAID_GET_ORIENTATION:
                                    case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                                        mh = b + 1;
                                        obj4[b] = mk2;
                                        b = mh;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            this.fW = obj4;
                        }
                        knVar.cS(i);
                        break;
                    case AdSize.LARGE_AD_HEIGHT:
                        b = kw.b(knVar, AdSize.LARGE_AD_HEIGHT);
                        mh = this.fV == null ? 0 : this.fV.length;
                        obj = new Object[(b + mh)];
                        if (mh != 0) {
                            System.arraycopy(this.fV, 0, obj, 0, mh);
                        }
                        while (mh < obj.length - 1) {
                            obj[mh] = new com.google.android.gms.internal.d.a();
                            knVar.a(obj[mh]);
                            knVar.mh();
                            mh++;
                        }
                        obj[mh] = new com.google.android.gms.internal.d.a();
                        knVar.a(obj[mh]);
                        this.fV = obj;
                        break;
                    case 96:
                        this.fU = knVar.ml();
                        break;
                    default:
                        if (!a(knVar, mh)) {
                            return this;
                        }
                }
            }
        }

        public com.google.android.gms.internal.d.a s() {
            this.type = 1;
            this.fN = Preconditions.EMPTY_ARGUMENTS;
            this.fO = r();
            this.fP = r();
            this.fQ = r();
            this.fR = Preconditions.EMPTY_ARGUMENTS;
            this.fS = Preconditions.EMPTY_ARGUMENTS;
            this.fT = 0;
            this.fU = false;
            this.fV = r();
            this.fW = kw.aea;
            this.fX = false;
            this.adU = null;
            this.adY = -1;
            return this;
        }
    }
}