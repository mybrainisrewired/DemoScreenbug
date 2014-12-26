package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.AgeRange;
import com.google.android.gms.plus.model.people.Person.Cover;
import com.google.android.gms.plus.model.people.Person.Cover.CoverInfo;
import com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto;
import com.google.android.gms.plus.model.people.Person.Image;
import com.google.android.gms.plus.model.people.Person.Name;
import com.google.android.gms.plus.model.people.Person.Organizations;
import com.google.android.gms.plus.model.people.Person.PlacesLived;
import com.google.android.gms.plus.model.people.Person.Urls;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.BannerInfoTable;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ih extends ga implements SafeParcelable, Person {
    public static final ii CREATOR;
    private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
    private String HA;
    private final Set<Integer> UJ;
    private String VH;
    private a VI;
    private String VJ;
    private String VK;
    private int VL;
    private b VM;
    private String VN;
    private c VO;
    private boolean VP;
    private String VQ;
    private d VR;
    private String VS;
    private int VT;
    private List<f> VU;
    private List<g> VV;
    private int VW;
    private int VX;
    private String VY;
    private List<h> VZ;
    private boolean Wa;
    private int lZ;
    private String ro;
    private String wp;
    private final int xH;

    public static class e {
        public static int bi(String str) {
            if (str.equals("person")) {
                return 0;
            }
            if (str.equals("page")) {
                return 1;
            }
            throw new IllegalArgumentException("Unknown objectType string: " + str);
        }
    }

    public static final class a extends ga implements SafeParcelable, AgeRange {
        public static final ij CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private final Set<Integer> UJ;
        private int Wb;
        private int Wc;
        private final int xH;

        static {
            CREATOR = new ij();
            UI = new HashMap();
            UI.put("max", com.google.android.gms.internal.ga.a.g("max", MMAdView.TRANSITION_UP));
            UI.put("min", com.google.android.gms.internal.ga.a.g("min", MMAdView.TRANSITION_DOWN));
        }

        public a() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        a(Set<Integer> set, int i, int i2, int i3) {
            this.UJ = set;
            this.xH = i;
            this.Wb = i2;
            this.Wc = i3;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return Integer.valueOf(this.Wb);
                case MMAdView.TRANSITION_DOWN:
                    return Integer.valueOf(this.Wc);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            ij ijVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.a obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.a)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jD();
        }

        public int getMax() {
            return this.Wb;
        }

        public int getMin() {
            return this.Wc;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasMax() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public boolean hasMin() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        public com.google.android.gms.internal.ih.a jD() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            ij ijVar = CREATOR;
            ij.a(this, out, flags);
        }
    }

    public static final class b extends ga implements SafeParcelable, Cover {
        public static final ik CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private final Set<Integer> UJ;
        private a Wd;
        private b We;
        private int Wf;
        private final int xH;

        public static final class a extends ga implements SafeParcelable, CoverInfo {
            public static final il CREATOR;
            private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
            private final Set<Integer> UJ;
            private int Wg;
            private int Wh;
            private final int xH;

            static {
                CREATOR = new il();
                UI = new HashMap();
                UI.put("leftImageOffset", com.google.android.gms.internal.ga.a.g("leftImageOffset", MMAdView.TRANSITION_UP));
                UI.put("topImageOffset", com.google.android.gms.internal.ga.a.g("topImageOffset", MMAdView.TRANSITION_DOWN));
            }

            public a() {
                this.xH = 1;
                this.UJ = new HashSet();
            }

            a(Set<Integer> set, int i, int i2, int i3) {
                this.UJ = set;
                this.xH = i;
                this.Wg = i2;
                this.Wh = i3;
            }

            protected boolean a(com.google.android.gms.internal.ga.a aVar) {
                return this.UJ.contains(Integer.valueOf(aVar.ff()));
            }

            protected Object aq(String str) {
                return null;
            }

            protected boolean ar(String str) {
                return false;
            }

            protected Object b(com.google.android.gms.internal.ga.a aVar) {
                switch (aVar.ff()) {
                    case MMAdView.TRANSITION_UP:
                        return Integer.valueOf(this.Wg);
                    case MMAdView.TRANSITION_DOWN:
                        return Integer.valueOf(this.Wh);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
                }
            }

            public int describeContents() {
                il ilVar = CREATOR;
                return 0;
            }

            public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
                return UI;
            }

            public boolean equals(com.google.android.gms.internal.ih.b.a obj) {
                if (!(obj instanceof com.google.android.gms.internal.ih.b.a)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                obj = obj;
                Iterator it = UI.values().iterator();
                while (it.hasNext()) {
                    com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
                return jH();
            }

            public int getLeftImageOffset() {
                return this.Wg;
            }

            public int getTopImageOffset() {
                return this.Wh;
            }

            int getVersionCode() {
                return this.xH;
            }

            public boolean hasLeftImageOffset() {
                return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
            }

            public boolean hasTopImageOffset() {
                return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
            }

            public int hashCode() {
                Iterator it = UI.values().iterator();
                int i = 0;
                while (it.hasNext()) {
                    int hashCode;
                    com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

            public com.google.android.gms.internal.ih.b.a jH() {
                return this;
            }

            Set<Integer> ja() {
                return this.UJ;
            }

            public void writeToParcel(Parcel out, int flags) {
                il ilVar = CREATOR;
                il.a(this, out, flags);
            }
        }

        public static final class b extends ga implements SafeParcelable, CoverPhoto {
            public static final im CREATOR;
            private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
            private final Set<Integer> UJ;
            private int kr;
            private int ks;
            private String ro;
            private final int xH;

            static {
                CREATOR = new im();
                UI = new HashMap();
                UI.put(MMLayout.KEY_HEIGHT, com.google.android.gms.internal.ga.a.g(MMLayout.KEY_HEIGHT, MMAdView.TRANSITION_UP));
                UI.put(PlusShare.KEY_CALL_TO_ACTION_URL, com.google.android.gms.internal.ga.a.j(PlusShare.KEY_CALL_TO_ACTION_URL, MMAdView.TRANSITION_DOWN));
                UI.put(MMLayout.KEY_WIDTH, com.google.android.gms.internal.ga.a.g(MMLayout.KEY_WIDTH, MMAdView.TRANSITION_RANDOM));
            }

            public b() {
                this.xH = 1;
                this.UJ = new HashSet();
            }

            b(Set<Integer> set, int i, int i2, String str, int i3) {
                this.UJ = set;
                this.xH = i;
                this.ks = i2;
                this.ro = str;
                this.kr = i3;
            }

            protected boolean a(com.google.android.gms.internal.ga.a aVar) {
                return this.UJ.contains(Integer.valueOf(aVar.ff()));
            }

            protected Object aq(String str) {
                return null;
            }

            protected boolean ar(String str) {
                return false;
            }

            protected Object b(com.google.android.gms.internal.ga.a aVar) {
                switch (aVar.ff()) {
                    case MMAdView.TRANSITION_UP:
                        return Integer.valueOf(this.ks);
                    case MMAdView.TRANSITION_DOWN:
                        return this.ro;
                    case MMAdView.TRANSITION_RANDOM:
                        return Integer.valueOf(this.kr);
                    default:
                        throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
                }
            }

            public int describeContents() {
                im imVar = CREATOR;
                return 0;
            }

            public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
                return UI;
            }

            public boolean equals(com.google.android.gms.internal.ih.b.b obj) {
                if (!(obj instanceof com.google.android.gms.internal.ih.b.b)) {
                    return false;
                }
                if (this == obj) {
                    return true;
                }
                obj = obj;
                Iterator it = UI.values().iterator();
                while (it.hasNext()) {
                    com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
                return jI();
            }

            public int getHeight() {
                return this.ks;
            }

            public String getUrl() {
                return this.ro;
            }

            int getVersionCode() {
                return this.xH;
            }

            public int getWidth() {
                return this.kr;
            }

            public boolean hasHeight() {
                return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
            }

            public boolean hasUrl() {
                return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
            }

            public boolean hasWidth() {
                return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
            }

            public int hashCode() {
                Iterator it = UI.values().iterator();
                int i = 0;
                while (it.hasNext()) {
                    int hashCode;
                    com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

            public com.google.android.gms.internal.ih.b.b jI() {
                return this;
            }

            Set<Integer> ja() {
                return this.UJ;
            }

            public void writeToParcel(Parcel out, int flags) {
                im imVar = CREATOR;
                im.a(this, out, flags);
            }
        }

        static {
            CREATOR = new ik();
            UI = new HashMap();
            UI.put("coverInfo", com.google.android.gms.internal.ga.a.a("coverInfo", MMAdView.TRANSITION_UP, a.class));
            UI.put("coverPhoto", com.google.android.gms.internal.ga.a.a("coverPhoto", MMAdView.TRANSITION_DOWN, b.class));
            UI.put("layout", com.google.android.gms.internal.ga.a.a("layout", MMAdView.TRANSITION_RANDOM, new fx().f("banner", 0), false));
        }

        public b() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        b(Set<Integer> set, int i, a aVar, b bVar, int i2) {
            this.UJ = set;
            this.xH = i;
            this.Wd = aVar;
            this.We = bVar;
            this.Wf = i2;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return this.Wd;
                case MMAdView.TRANSITION_DOWN:
                    return this.We;
                case MMAdView.TRANSITION_RANDOM:
                    return Integer.valueOf(this.Wf);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            ik ikVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.b obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.b)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jG();
        }

        public CoverInfo getCoverInfo() {
            return this.Wd;
        }

        public CoverPhoto getCoverPhoto() {
            return this.We;
        }

        public int getLayout() {
            return this.Wf;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasCoverInfo() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public boolean hasCoverPhoto() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
        }

        public boolean hasLayout() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        a jE() {
            return this.Wd;
        }

        b jF() {
            return this.We;
        }

        public com.google.android.gms.internal.ih.b jG() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            ik ikVar = CREATOR;
            ik.a(this, out, flags);
        }
    }

    public static final class c extends ga implements SafeParcelable, Image {
        public static final in CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private final Set<Integer> UJ;
        private String ro;
        private final int xH;

        static {
            CREATOR = new in();
            UI = new HashMap();
            UI.put(PlusShare.KEY_CALL_TO_ACTION_URL, com.google.android.gms.internal.ga.a.j(PlusShare.KEY_CALL_TO_ACTION_URL, MMAdView.TRANSITION_UP));
        }

        public c() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        public c(String str) {
            this.UJ = new HashSet();
            this.xH = 1;
            this.ro = str;
            this.UJ.add(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        c(Set<Integer> set, int i, String str) {
            this.UJ = set;
            this.xH = i;
            this.ro = str;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return this.ro;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            in inVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.c obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.c)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jJ();
        }

        public String getUrl() {
            return this.ro;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasUrl() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        public com.google.android.gms.internal.ih.c jJ() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            in inVar = CREATOR;
            in.a(this, out, flags);
        }
    }

    public static final class d extends ga implements SafeParcelable, Name {
        public static final io CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private final Set<Integer> UJ;
        private String Vh;
        private String Vk;
        private String Wi;
        private String Wj;
        private String Wk;
        private String Wl;
        private final int xH;

        static {
            CREATOR = new io();
            UI = new HashMap();
            UI.put("familyName", com.google.android.gms.internal.ga.a.j("familyName", MMAdView.TRANSITION_UP));
            UI.put("formatted", com.google.android.gms.internal.ga.a.j("formatted", MMAdView.TRANSITION_DOWN));
            UI.put("givenName", com.google.android.gms.internal.ga.a.j("givenName", MMAdView.TRANSITION_RANDOM));
            UI.put("honorificPrefix", com.google.android.gms.internal.ga.a.j("honorificPrefix", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
            UI.put("honorificSuffix", com.google.android.gms.internal.ga.a.j("honorificSuffix", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
            UI.put("middleName", com.google.android.gms.internal.ga.a.j("middleName", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
        }

        public d() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        d(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, String str6) {
            this.UJ = set;
            this.xH = i;
            this.Vh = str;
            this.Wi = str2;
            this.Vk = str3;
            this.Wj = str4;
            this.Wk = str5;
            this.Wl = str6;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return this.Vh;
                case MMAdView.TRANSITION_DOWN:
                    return this.Wi;
                case MMAdView.TRANSITION_RANDOM:
                    return this.Vk;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    return this.Wj;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    return this.Wk;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    return this.Wl;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            io ioVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.d obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.d)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jK();
        }

        public String getFamilyName() {
            return this.Vh;
        }

        public String getFormatted() {
            return this.Wi;
        }

        public String getGivenName() {
            return this.Vk;
        }

        public String getHonorificPrefix() {
            return this.Wj;
        }

        public String getHonorificSuffix() {
            return this.Wk;
        }

        public String getMiddleName() {
            return this.Wl;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasFamilyName() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public boolean hasFormatted() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
        }

        public boolean hasGivenName() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
        }

        public boolean hasHonorificPrefix() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        }

        public boolean hasHonorificSuffix() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        }

        public boolean hasMiddleName() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        public com.google.android.gms.internal.ih.d jK() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            io ioVar = CREATOR;
            io.a(this, out, flags);
        }
    }

    public static final class f extends ga implements SafeParcelable, Organizations {
        public static final ip CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private String EB;
        private String HD;
        private int LF;
        private final Set<Integer> UJ;
        private String Vg;
        private String Vw;
        private String Wm;
        private String Wn;
        private boolean Wo;
        private String mName;
        private final int xH;

        static {
            CREATOR = new ip();
            UI = new HashMap();
            UI.put("department", com.google.android.gms.internal.ga.a.j("department", MMAdView.TRANSITION_UP));
            UI.put(BannerInfoTable.COLUMN_DESCRIPTION, com.google.android.gms.internal.ga.a.j(BannerInfoTable.COLUMN_DESCRIPTION, MMAdView.TRANSITION_DOWN));
            UI.put("endDate", com.google.android.gms.internal.ga.a.j("endDate", MMAdView.TRANSITION_RANDOM));
            UI.put("location", com.google.android.gms.internal.ga.a.j("location", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
            UI.put("name", com.google.android.gms.internal.ga.a.j("name", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
            UI.put("primary", com.google.android.gms.internal.ga.a.i("primary", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
            UI.put("startDate", com.google.android.gms.internal.ga.a.j("startDate", ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
            UI.put(BannerInfoTable.COLUMN_TITLE, com.google.android.gms.internal.ga.a.j(BannerInfoTable.COLUMN_TITLE, ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
            UI.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, com.google.android.gms.internal.ga.a.a(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, new fx().f("work", 0).f("school", 1), false));
        }

        public f() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        f(Set<Integer> set, int i, String str, String str2, String str3, String str4, String str5, boolean z, String str6, String str7, int i2) {
            this.UJ = set;
            this.xH = i;
            this.Wm = str;
            this.HD = str2;
            this.Vg = str3;
            this.Wn = str4;
            this.mName = str5;
            this.Wo = z;
            this.Vw = str6;
            this.EB = str7;
            this.LF = i2;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return this.Wm;
                case MMAdView.TRANSITION_DOWN:
                    return this.HD;
                case MMAdView.TRANSITION_RANDOM:
                    return this.Vg;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    return this.Wn;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    return this.mName;
                case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                    return Boolean.valueOf(this.Wo);
                case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                    return this.Vw;
                case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                    return this.EB;
                case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                    return Integer.valueOf(this.LF);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            ip ipVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.f obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.f)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jL();
        }

        public String getDepartment() {
            return this.Wm;
        }

        public String getDescription() {
            return this.HD;
        }

        public String getEndDate() {
            return this.Vg;
        }

        public String getLocation() {
            return this.Wn;
        }

        public String getName() {
            return this.mName;
        }

        public String getStartDate() {
            return this.Vw;
        }

        public String getTitle() {
            return this.EB;
        }

        public int getType() {
            return this.LF;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasDepartment() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public boolean hasDescription() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
        }

        public boolean hasEndDate() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
        }

        public boolean hasLocation() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        }

        public boolean hasName() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        }

        public boolean hasPrimary() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
        }

        public boolean hasStartDate() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
        }

        public boolean hasTitle() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
        }

        public boolean hasType() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        public boolean isPrimary() {
            return this.Wo;
        }

        public com.google.android.gms.internal.ih.f jL() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            ip ipVar = CREATOR;
            ip.a(this, out, flags);
        }
    }

    public static final class g extends ga implements SafeParcelable, PlacesLived {
        public static final iq CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private final Set<Integer> UJ;
        private boolean Wo;
        private String mValue;
        private final int xH;

        static {
            CREATOR = new iq();
            UI = new HashMap();
            UI.put("primary", com.google.android.gms.internal.ga.a.i("primary", MMAdView.TRANSITION_UP));
            UI.put("value", com.google.android.gms.internal.ga.a.j("value", MMAdView.TRANSITION_DOWN));
        }

        public g() {
            this.xH = 1;
            this.UJ = new HashSet();
        }

        g(Set<Integer> set, int i, boolean z, String str) {
            this.UJ = set;
            this.xH = i;
            this.Wo = z;
            this.mValue = str;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_UP:
                    return Boolean.valueOf(this.Wo);
                case MMAdView.TRANSITION_DOWN:
                    return this.mValue;
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            iq iqVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.g obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.g)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jM();
        }

        public String getValue() {
            return this.mValue;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasPrimary() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
        }

        public boolean hasValue() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        public boolean isPrimary() {
            return this.Wo;
        }

        public com.google.android.gms.internal.ih.g jM() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            iq iqVar = CREATOR;
            iq.a(this, out, flags);
        }
    }

    public static final class h extends ga implements SafeParcelable, Urls {
        public static final ir CREATOR;
        private static final HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> UI;
        private int LF;
        private final Set<Integer> UJ;
        private String Wp;
        private final int Wq;
        private String mValue;
        private final int xH;

        static {
            CREATOR = new ir();
            UI = new HashMap();
            UI.put(PlusShare.KEY_CALL_TO_ACTION_LABEL, com.google.android.gms.internal.ga.a.j(PlusShare.KEY_CALL_TO_ACTION_LABEL, ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
            UI.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, com.google.android.gms.internal.ga.a.a(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES, new fx().f("home", 0).f("work", 1).f("blog", MMAdView.TRANSITION_UP).f(Scopes.PROFILE, MMAdView.TRANSITION_DOWN).f(MMRequest.MARITAL_OTHER, MMAdView.TRANSITION_RANDOM).f("otherProfile", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES).f("contributor", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES).f("website", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES), false));
            UI.put("value", com.google.android.gms.internal.ga.a.j("value", MMAdView.TRANSITION_RANDOM));
        }

        public h() {
            this.Wq = 4;
            this.xH = 2;
            this.UJ = new HashSet();
        }

        h(Set<Integer> set, int i, String str, int i2, String str2, int i3) {
            this.Wq = 4;
            this.UJ = set;
            this.xH = i;
            this.Wp = str;
            this.LF = i2;
            this.mValue = str2;
        }

        protected boolean a(com.google.android.gms.internal.ga.a aVar) {
            return this.UJ.contains(Integer.valueOf(aVar.ff()));
        }

        protected Object aq(String str) {
            return null;
        }

        protected boolean ar(String str) {
            return false;
        }

        protected Object b(com.google.android.gms.internal.ga.a aVar) {
            switch (aVar.ff()) {
                case MMAdView.TRANSITION_RANDOM:
                    return this.mValue;
                case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                    return this.Wp;
                case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                    return Integer.valueOf(this.LF);
                default:
                    throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
            }
        }

        public int describeContents() {
            ir irVar = CREATOR;
            return 0;
        }

        public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
            return UI;
        }

        public boolean equals(com.google.android.gms.internal.ih.h obj) {
            if (!(obj instanceof com.google.android.gms.internal.ih.h)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            obj = obj;
            Iterator it = UI.values().iterator();
            while (it.hasNext()) {
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
            return jO();
        }

        public String getLabel() {
            return this.Wp;
        }

        public int getType() {
            return this.LF;
        }

        public String getValue() {
            return this.mValue;
        }

        int getVersionCode() {
            return this.xH;
        }

        public boolean hasLabel() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        }

        public boolean hasType() {
            return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        }

        public boolean hasValue() {
            return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
        }

        public int hashCode() {
            Iterator it = UI.values().iterator();
            int i = 0;
            while (it.hasNext()) {
                int hashCode;
                com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

        @Deprecated
        public int jN() {
            return MMAdView.TRANSITION_RANDOM;
        }

        public com.google.android.gms.internal.ih.h jO() {
            return this;
        }

        Set<Integer> ja() {
            return this.UJ;
        }

        public void writeToParcel(Parcel out, int flags) {
            ir irVar = CREATOR;
            ir.a(this, out, flags);
        }
    }

    static {
        CREATOR = new ii();
        UI = new HashMap();
        UI.put("aboutMe", com.google.android.gms.internal.ga.a.j("aboutMe", MMAdView.TRANSITION_UP));
        UI.put("ageRange", com.google.android.gms.internal.ga.a.a("ageRange", MMAdView.TRANSITION_DOWN, a.class));
        UI.put("birthday", com.google.android.gms.internal.ga.a.j("birthday", MMAdView.TRANSITION_RANDOM));
        UI.put("braggingRights", com.google.android.gms.internal.ga.a.j("braggingRights", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        UI.put("circledByCount", com.google.android.gms.internal.ga.a.g("circledByCount", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        UI.put("cover", com.google.android.gms.internal.ga.a.a("cover", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES, b.class));
        UI.put("currentLocation", com.google.android.gms.internal.ga.a.j("currentLocation", ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
        UI.put("displayName", com.google.android.gms.internal.ga.a.j("displayName", ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
        UI.put(MMRequest.KEY_GENDER, com.google.android.gms.internal.ga.a.a(MMRequest.KEY_GENDER, ApiEventType.API_MRAID_RESIZE, new fx().f(MMRequest.GENDER_MALE, 0).f(MMRequest.GENDER_FEMALE, 1).f(MMRequest.MARITAL_OTHER, MMAdView.TRANSITION_UP), false));
        UI.put(AnalyticsEvent.EVENT_ID, com.google.android.gms.internal.ga.a.j(AnalyticsEvent.EVENT_ID, ApiEventType.API_MRAID_IS_VIEWABLE));
        UI.put("image", com.google.android.gms.internal.ga.a.a("image", ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, c.class));
        UI.put("isPlusUser", com.google.android.gms.internal.ga.a.i("isPlusUser", ApiEventType.API_MRAID_GET_ORIENTATION));
        UI.put("language", com.google.android.gms.internal.ga.a.j("language", ApiEventType.API_MRAID_GET_CURRENT_POSITION));
        UI.put("name", com.google.android.gms.internal.ga.a.a("name", Encoder.LINE_GROUPS, d.class));
        UI.put("nickname", com.google.android.gms.internal.ga.a.j("nickname", ApiEventType.API_MRAID_GET_MAX_SIZE));
        UI.put("objectType", com.google.android.gms.internal.ga.a.a("objectType", ApiEventType.API_MRAID_POST_TO_SOCIAL, new fx().f("person", 0).f("page", 1), false));
        UI.put("organizations", com.google.android.gms.internal.ga.a.b("organizations", ApiEventType.API_MRAID_SUPPORTS, f.class));
        UI.put("placesLived", com.google.android.gms.internal.ga.a.b("placesLived", ApiEventType.API_MRAID_STORE_PICTURE, g.class));
        UI.put("plusOneCount", com.google.android.gms.internal.ga.a.g("plusOneCount", ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
        UI.put("relationshipStatus", com.google.android.gms.internal.ga.a.a("relationshipStatus", ApiEventType.API_MRAID_GET_GALLERY_IMAGE, new fx().f(MMRequest.MARITAL_SINGLE, 0).f("in_a_relationship", 1).f(MMRequest.MARITAL_ENGAGED, MMAdView.TRANSITION_UP).f(MMRequest.MARITAL_MARRIED, MMAdView.TRANSITION_DOWN).f("its_complicated", MMAdView.TRANSITION_RANDOM).f("open_relationship", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES).f("widowed", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES).f("in_domestic_partnership", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES).f("in_civil_union", ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES), false));
        UI.put("tagline", com.google.android.gms.internal.ga.a.j("tagline", ApiEventType.API_MRAID_VIBRATE));
        UI.put(PlusShare.KEY_CALL_TO_ACTION_URL, com.google.android.gms.internal.ga.a.j(PlusShare.KEY_CALL_TO_ACTION_URL, ApiEventType.API_MRAID_SEND_SMS));
        UI.put("urls", com.google.android.gms.internal.ga.a.b("urls", ApiEventType.API_MRAID_SEND_MAIL, h.class));
        UI.put("verified", com.google.android.gms.internal.ga.a.i("verified", ApiEventType.API_MRAID_MAKE_CALL));
    }

    public ih() {
        this.xH = 2;
        this.UJ = new HashSet();
    }

    public ih(String str, String str2, c cVar, int i, String str3) {
        this.xH = 2;
        this.UJ = new HashSet();
        this.HA = str;
        this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
        this.wp = str2;
        this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
        this.VO = cVar;
        this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
        this.VT = i;
        this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
        this.ro = str3;
        this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
    }

    ih(Set<Integer> set, int i, String str, a aVar, String str2, String str3, int i2, b bVar, String str4, String str5, int i3, String str6, c cVar, boolean z, String str7, d dVar, String str8, int i4, List<f> list, List<g> list2, int i5, int i6, String str9, String str10, List<h> list3, boolean z2) {
        this.UJ = set;
        this.xH = i;
        this.VH = str;
        this.VI = aVar;
        this.VJ = str2;
        this.VK = str3;
        this.VL = i2;
        this.VM = bVar;
        this.VN = str4;
        this.HA = str5;
        this.lZ = i3;
        this.wp = str6;
        this.VO = cVar;
        this.VP = z;
        this.VQ = str7;
        this.VR = dVar;
        this.VS = str8;
        this.VT = i4;
        this.VU = list;
        this.VV = list2;
        this.VW = i5;
        this.VX = i6;
        this.VY = str9;
        this.ro = str10;
        this.VZ = list3;
        this.Wa = z2;
    }

    public static ih i(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        ih aN = CREATOR.aN(obtain);
        obtain.recycle();
        return aN;
    }

    protected boolean a(com.google.android.gms.internal.ga.a aVar) {
        return this.UJ.contains(Integer.valueOf(aVar.ff()));
    }

    protected Object aq(String str) {
        return null;
    }

    protected boolean ar(String str) {
        return false;
    }

    protected Object b(com.google.android.gms.internal.ga.a aVar) {
        switch (aVar.ff()) {
            case MMAdView.TRANSITION_UP:
                return this.VH;
            case MMAdView.TRANSITION_DOWN:
                return this.VI;
            case MMAdView.TRANSITION_RANDOM:
                return this.VJ;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return this.VK;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return Integer.valueOf(this.VL);
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return this.VM;
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return this.VN;
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                return this.HA;
            case ApiEventType.API_MRAID_RESIZE:
                return Integer.valueOf(this.lZ);
            case ApiEventType.API_MRAID_IS_VIEWABLE:
                return this.wp;
            case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                return this.VO;
            case ApiEventType.API_MRAID_GET_ORIENTATION:
                return Boolean.valueOf(this.VP);
            case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                return this.VQ;
            case Encoder.LINE_GROUPS:
                return this.VR;
            case ApiEventType.API_MRAID_GET_MAX_SIZE:
                return this.VS;
            case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                return Integer.valueOf(this.VT);
            case ApiEventType.API_MRAID_SUPPORTS:
                return this.VU;
            case ApiEventType.API_MRAID_STORE_PICTURE:
                return this.VV;
            case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                return Integer.valueOf(this.VW);
            case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                return Integer.valueOf(this.VX);
            case ApiEventType.API_MRAID_VIBRATE:
                return this.VY;
            case ApiEventType.API_MRAID_SEND_SMS:
                return this.ro;
            case ApiEventType.API_MRAID_SEND_MAIL:
                return this.VZ;
            case ApiEventType.API_MRAID_MAKE_CALL:
                return Boolean.valueOf(this.Wa);
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
        }
    }

    public int describeContents() {
        ii iiVar = CREATOR;
        return 0;
    }

    public HashMap<String, com.google.android.gms.internal.ga.a<?, ?>> eY() {
        return UI;
    }

    public boolean equals(ih obj) {
        if (!(obj instanceof ih)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        obj = obj;
        Iterator it = UI.values().iterator();
        while (it.hasNext()) {
            com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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
        return jC();
    }

    public String getAboutMe() {
        return this.VH;
    }

    public AgeRange getAgeRange() {
        return this.VI;
    }

    public String getBirthday() {
        return this.VJ;
    }

    public String getBraggingRights() {
        return this.VK;
    }

    public int getCircledByCount() {
        return this.VL;
    }

    public Cover getCover() {
        return this.VM;
    }

    public String getCurrentLocation() {
        return this.VN;
    }

    public String getDisplayName() {
        return this.HA;
    }

    public int getGender() {
        return this.lZ;
    }

    public String getId() {
        return this.wp;
    }

    public Image getImage() {
        return this.VO;
    }

    public String getLanguage() {
        return this.VQ;
    }

    public Name getName() {
        return this.VR;
    }

    public String getNickname() {
        return this.VS;
    }

    public int getObjectType() {
        return this.VT;
    }

    public List<Organizations> getOrganizations() {
        return (ArrayList) this.VU;
    }

    public List<PlacesLived> getPlacesLived() {
        return (ArrayList) this.VV;
    }

    public int getPlusOneCount() {
        return this.VW;
    }

    public int getRelationshipStatus() {
        return this.VX;
    }

    public String getTagline() {
        return this.VY;
    }

    public String getUrl() {
        return this.ro;
    }

    public List<Urls> getUrls() {
        return (ArrayList) this.VZ;
    }

    int getVersionCode() {
        return this.xH;
    }

    public boolean hasAboutMe() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
    }

    public boolean hasAgeRange() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
    }

    public boolean hasBirthday() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
    }

    public boolean hasBraggingRights() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
    }

    public boolean hasCircledByCount() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
    }

    public boolean hasCover() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
    }

    public boolean hasCurrentLocation() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
    }

    public boolean hasDisplayName() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
    }

    public boolean hasGender() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_RESIZE));
    }

    public boolean hasId() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
    }

    public boolean hasImage() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
    }

    public boolean hasIsPlusUser() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION));
    }

    public boolean hasLanguage() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION));
    }

    public boolean hasName() {
        return this.UJ.contains(Integer.valueOf(Encoder.LINE_GROUPS));
    }

    public boolean hasNickname() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE));
    }

    public boolean hasObjectType() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
    }

    public boolean hasOrganizations() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS));
    }

    public boolean hasPlacesLived() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE));
    }

    public boolean hasPlusOneCount() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
    }

    public boolean hasRelationshipStatus() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
    }

    public boolean hasTagline() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE));
    }

    public boolean hasUrl() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
    }

    public boolean hasUrls() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL));
    }

    public boolean hasVerified() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL));
    }

    public int hashCode() {
        Iterator it = UI.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            int hashCode;
            com.google.android.gms.internal.ga.a aVar = (com.google.android.gms.internal.ga.a) it.next();
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

    public boolean isPlusUser() {
        return this.VP;
    }

    public boolean isVerified() {
        return this.Wa;
    }

    List<g> jA() {
        return this.VV;
    }

    List<h> jB() {
        return this.VZ;
    }

    public ih jC() {
        return this;
    }

    Set<Integer> ja() {
        return this.UJ;
    }

    a jv() {
        return this.VI;
    }

    b jw() {
        return this.VM;
    }

    c jx() {
        return this.VO;
    }

    d jy() {
        return this.VR;
    }

    List<f> jz() {
        return this.VU;
    }

    public void writeToParcel(Parcel out, int flags) {
        ii iiVar = CREATOR;
        ii.a(this, out, flags);
    }
}