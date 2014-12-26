package com.google.android.gms.drive.internal;

import com.google.android.gms.internal.kn;
import com.google.android.gms.internal.ko;
import com.google.android.gms.internal.kp;
import com.google.android.gms.internal.ks;
import com.google.android.gms.internal.kt;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.Preconditions;
import java.io.IOException;

public final class y extends kp<y> {
    public String FC;
    public long FD;
    public long FE;
    public int versionCode;

    public y() {
        fH();
    }

    public static y g(byte[] bArr) throws ks {
        return (y) kt.a(new y(), bArr);
    }

    public void a(ko koVar) throws IOException {
        koVar.i(1, this.versionCode);
        koVar.b((int)MMAdView.TRANSITION_UP, this.FC);
        koVar.c((int)MMAdView.TRANSITION_DOWN, this.FD);
        koVar.c((int)MMAdView.TRANSITION_RANDOM, this.FE);
        super.a(koVar);
    }

    public /* synthetic */ kt b(kn knVar) throws IOException {
        return m(knVar);
    }

    public int c() {
        int c = super.c() + ko.j(1, this.versionCode) + ko.g(MMAdView.TRANSITION_UP, this.FC) + ko.e(MMAdView.TRANSITION_DOWN, this.FD) + ko.e(MMAdView.TRANSITION_RANDOM, this.FE);
        this.adY = c;
        return c;
    }

    public boolean equals(y o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof y)) {
            return false;
        }
        o = o;
        if (this.versionCode != o.versionCode) {
            return false;
        }
        if (this.FC == null) {
            if (o.FC != null) {
                return false;
            }
        } else if (!this.FC.equals(o.FC)) {
            return false;
        }
        if (this.FD != o.FD || this.FE != o.FE) {
            return false;
        }
        if (this.adU == null || this.adU.isEmpty()) {
            return o.adU == null || o.adU.isEmpty();
        } else {
            return this.adU.equals(o.adU);
        }
    }

    public y fH() {
        this.versionCode = 1;
        this.FC = Preconditions.EMPTY_ARGUMENTS;
        this.FD = -1;
        this.FE = -1;
        this.adU = null;
        this.adY = -1;
        return this;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((((((this.FC == null ? 0 : this.FC.hashCode()) + ((this.versionCode + 527) * 31)) * 31) + ((int) (this.FD ^ (this.FD >>> 32)))) * 31) + ((int) (this.FE ^ (this.FE >>> 32)))) * 31;
        if (!(this.adU == null || this.adU.isEmpty())) {
            i = this.adU.hashCode();
        }
        return hashCode + i;
    }

    public y m(kn knVar) throws IOException {
        while (true) {
            int mh = knVar.mh();
            switch (mh) {
                case MMAdView.TRANSITION_NONE:
                    return this;
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    this.versionCode = knVar.mk();
                    break;
                case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                    this.FC = knVar.readString();
                    break;
                case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                    this.FD = knVar.mm();
                    break;
                case ApiEventType.API_MRAID_PLAY_AUDIO:
                    this.FE = knVar.mm();
                    break;
                default:
                    if (!a(knVar, mh)) {
                        return this;
                    }
            }
        }
    }
}