package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ga.a;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class ie extends ga implements SafeParcelable, Moment {
    public static final if_ CREATOR;
    private static final HashMap<String, a<?, ?>> UI;
    private String Rd;
    private final Set<Integer> UJ;
    private ic VE;
    private ic VF;
    private String Vw;
    private String wp;
    private final int xH;

    static {
        CREATOR = new if_();
        UI = new HashMap();
        UI.put(AnalyticsEvent.EVENT_ID, a.j(AnalyticsEvent.EVENT_ID, MMAdView.TRANSITION_UP));
        UI.put("result", a.a("result", MMAdView.TRANSITION_RANDOM, ic.class));
        UI.put("startDate", a.j("startDate", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        UI.put("target", a.a("target", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, ic.class));
        UI.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, a.j(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
    }

    public ie() {
        this.xH = 1;
        this.UJ = new HashSet();
    }

    ie(Set<Integer> set, int i, String str, ic icVar, String str2, ic icVar2, String str3) {
        this.UJ = set;
        this.xH = i;
        this.wp = str;
        this.VE = icVar;
        this.Vw = str2;
        this.VF = icVar2;
        this.Rd = str3;
    }

    public ie(Set<Integer> set, String str, ic icVar, String str2, ic icVar2, String str3) {
        this.UJ = set;
        this.xH = 1;
        this.wp = str;
        this.VE = icVar;
        this.Vw = str2;
        this.VF = icVar2;
        this.Rd = str3;
    }

    protected boolean a(a aVar) {
        return this.UJ.contains(Integer.valueOf(aVar.ff()));
    }

    protected Object aq(String str) {
        return null;
    }

    protected boolean ar(String str) {
        return false;
    }

    protected Object b(a aVar) {
        switch (aVar.ff()) {
            case MMAdView.TRANSITION_UP:
                return this.wp;
            case MMAdView.TRANSITION_RANDOM:
                return this.VE;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return this.Vw;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return this.VF;
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return this.Rd;
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
        }
    }

    public int describeContents() {
        if_ com_google_android_gms_internal_if = CREATOR;
        return 0;
    }

    public HashMap<String, a<?, ?>> eY() {
        return UI;
    }

    public boolean equals(ie obj) {
        if (!(obj instanceof ie)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        obj = obj;
        Iterator it = UI.values().iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (a(aVar)) {
                if (!obj.a(aVar)) {
                    return false;
                }
                if (!b(aVar).equals(obj.b(aVar))) {
                    return false;
                }
            } else if (obj.a(aVar)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return jt();
    }

    public String getId() {
        return this.wp;
    }

    public ItemScope getResult() {
        return this.VE;
    }

    public String getStartDate() {
        return this.Vw;
    }

    public ItemScope getTarget() {
        return this.VF;
    }

    public String getType() {
        return this.Rd;
    }

    int getVersionCode() {
        return this.xH;
    }

    public boolean hasId() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
    }

    public boolean hasResult() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
    }

    public boolean hasStartDate() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
    }

    public boolean hasTarget() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
    }

    public boolean hasType() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
    }

    public int hashCode() {
        Iterator it = UI.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            int hashCode;
            a aVar = (a) it.next();
            if (a(aVar)) {
                hashCode = b(aVar).hashCode() + i + aVar.ff();
            } else {
                hashCode = i;
            }
            i = hashCode;
        }
        return i;
    }

    public boolean isDataValid() {
        return true;
    }

    Set<Integer> ja() {
        return this.UJ;
    }

    ic jr() {
        return this.VE;
    }

    ic js() {
        return this.VF;
    }

    public ie jt() {
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        if_ com_google_android_gms_internal_if = CREATOR;
        if_.a(this, out, flags);
    }
}