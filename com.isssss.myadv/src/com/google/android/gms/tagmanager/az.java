package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.b;
import com.google.android.gms.plus.PlusShare;
import com.millennialmedia.android.MMAdView;
import com.mopub.common.MoPubBrowser;
import com.mopub.common.Preconditions;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class az extends aj {
    private static final String ID;
    private static final String XQ;
    private static final String Ym;
    private static final String Yn;
    private static final String Yo;

    static /* synthetic */ class AnonymousClass_1 {
        static final /* synthetic */ int[] Yp;

        static {
            Yp = new int[a.values().length];
            try {
                Yp[a.Yr.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                Yp[a.Ys.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            Yp[a.Yq.ordinal()] = 3;
        }
    }

    private enum a {
        NONE,
        URL,
        BACKSLASH;

        static {
            Yq = new a("NONE", 0);
            Yr = new a(MoPubBrowser.DESTINATION_URL_KEY, 1);
            Ys = new a("BACKSLASH", 2);
            Yt = new a[]{Yq, Yr, Ys};
        }
    }

    static {
        ID = com.google.android.gms.internal.a.ac.toString();
        XQ = b.bi.toString();
        Ym = b.cL.toString();
        Yn = b.cO.toString();
        Yo = b.co.toString();
    }

    public az() {
        super(ID, new String[]{XQ});
    }

    private String a(String str, a aVar, Set<Character> set) {
        switch (AnonymousClass_1.Yp[aVar.ordinal()]) {
            case MMAdView.TRANSITION_FADE:
                try {
                    return dk.cd(str);
                } catch (UnsupportedEncodingException e) {
                    bh.b("Joiner: unsupported encoding", e);
                    return str;
                }
            case MMAdView.TRANSITION_UP:
                String replace = str.replace("\\", "\\\\");
                Iterator it = set.iterator();
                String str2 = replace;
                while (it.hasNext()) {
                    CharSequence toString = ((Character) it.next()).toString();
                    str2 = str2.replace(toString, "\\" + toString);
                }
                return str2;
            default:
                return str;
        }
    }

    private void a(StringBuilder stringBuilder, String str, a aVar, Set<Character> set) {
        stringBuilder.append(a(str, aVar, set));
    }

    private void a(Set<Character> set, String str) {
        int i = 0;
        while (i < str.length()) {
            set.add(Character.valueOf(str.charAt(i)));
            i++;
        }
    }

    public boolean jX() {
        return true;
    }

    public com.google.android.gms.internal.d.a x(Map<String, com.google.android.gms.internal.d.a> map) {
        com.google.android.gms.internal.d.a aVar = (com.google.android.gms.internal.d.a) map.get(XQ);
        if (aVar == null) {
            return dh.lT();
        }
        a aVar2;
        Set set;
        com.google.android.gms.internal.d.a aVar3 = (com.google.android.gms.internal.d.a) map.get(Ym);
        String j = aVar3 != null ? dh.j(aVar3) : Preconditions.EMPTY_ARGUMENTS;
        aVar3 = (com.google.android.gms.internal.d.a) map.get(Yn);
        String j2 = aVar3 != null ? dh.j(aVar3) : "=";
        a aVar4 = a.Yq;
        aVar3 = (com.google.android.gms.internal.d.a) map.get(Yo);
        if (aVar3 != null) {
            String j3 = dh.j(aVar3);
            if (PlusShare.KEY_CALL_TO_ACTION_URL.equals(j3)) {
                aVar2 = a.Yr;
                set = null;
            } else if ("backslash".equals(j3)) {
                aVar2 = a.Ys;
                set = new HashSet();
                a(set, j);
                a(set, j2);
                set.remove(Character.valueOf('\\'));
            } else {
                bh.w("Joiner: unsupported escape type: " + j3);
                return dh.lT();
            }
        } else {
            set = null;
            aVar2 = aVar4;
        }
        StringBuilder stringBuilder = new StringBuilder();
        switch (aVar.type) {
            case MMAdView.TRANSITION_UP:
                boolean z = 1;
                com.google.android.gms.internal.d.a[] aVarArr = aVar.fO;
                int length = aVarArr.length;
                int i = 0;
                while (i < length) {
                    com.google.android.gms.internal.d.a aVar5 = aVarArr[i];
                    if (i == 0) {
                        stringBuilder.append(j);
                    }
                    a(stringBuilder, dh.j(aVar5), aVar2, set);
                    i++;
                    z = false;
                }
                break;
            case MMAdView.TRANSITION_DOWN:
                int i2 = 0;
                while (i2 < aVar.fP.length) {
                    if (i2 > 0) {
                        stringBuilder.append(j);
                    }
                    String j4 = dh.j(aVar.fP[i2]);
                    String j5 = dh.j(aVar.fQ[i2]);
                    a(stringBuilder, j4, aVar2, set);
                    stringBuilder.append(j2);
                    a(stringBuilder, j5, aVar2, set);
                    i2++;
                }
                break;
            default:
                a(stringBuilder, dh.j(aVar), aVar2, set);
                break;
        }
        return dh.r(stringBuilder.toString());
    }
}