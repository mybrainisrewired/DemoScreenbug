package com.google.android.gms.internal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.cast.Cast;
import com.millennialmedia.android.MMAdView;
import java.util.HashMap;
import java.util.Map;

public final class ba {
    public static final bb mG;
    public static final bb mH;
    public static final bb mI;
    public static final bb mJ;
    public static final bb mK;
    public static final bb mL;
    public static final bb mM;
    public static final bb mN;
    public static final bb mO;

    static {
        mG = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
            }
        };
        mH = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                String str = (String) map.get("urls");
                if (TextUtils.isEmpty(str)) {
                    dw.z("URLs missing in canOpenURLs GMSG.");
                } else {
                    String[] split = str.split(",");
                    Map hashMap = new HashMap();
                    PackageManager packageManager = dzVar.getContext().getPackageManager();
                    int length = split.length;
                    int i = 0;
                    while (i < length) {
                        String str2 = split[i];
                        String[] split2 = str2.split(";", MMAdView.TRANSITION_UP);
                        String trim = split2[0].trim();
                        hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(trim)), Cast.MAX_MESSAGE_LENGTH) != null));
                        i++;
                    }
                    dzVar.a("openableURLs", hashMap);
                }
            }
        };
        mI = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                String str = (String) map.get("u");
                if (str == null) {
                    dw.z("URL missing from click GMSG.");
                } else {
                    Uri a;
                    Uri parse = Uri.parse(str);
                    try {
                        l bJ = dzVar.bJ();
                        if (bJ != null && bJ.a(parse)) {
                            a = bJ.a(parse, dzVar.getContext());
                            new du(dzVar.getContext(), dzVar.bK().rq, a.toString()).start();
                        }
                    } catch (m e) {
                        dw.z("Unable to append parameter to URL: " + str);
                    }
                    a = parse;
                    new du(dzVar.getContext(), dzVar.bK().rq, a.toString()).start();
                }
            }
        };
        mJ = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                cc bH = dzVar.bH();
                if (bH == null) {
                    dw.z("A GMSG tried to close something that wasn't an overlay.");
                } else {
                    bH.close();
                }
            }
        };
        mK = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                cc bH = dzVar.bH();
                if (bH == null) {
                    dw.z("A GMSG tried to use a custom close button on something that wasn't an overlay.");
                } else {
                    bH.i("1".equals(map.get("custom_close")));
                }
            }
        };
        mL = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                String str = (String) map.get("u");
                if (str == null) {
                    dw.z("URL missing from httpTrack GMSG.");
                } else {
                    new du(dzVar.getContext(), dzVar.bK().rq, str).start();
                }
            }
        };
        mM = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                dw.x("Received log message: " + ((String) map.get("string")));
            }
        };
        mN = new bb() {
            public void b(dz dzVar, Map<String, String> map) {
                String str = (String) map.get("ty");
                String str2 = (String) map.get("td");
                try {
                    int parseInt = Integer.parseInt((String) map.get("tx"));
                    int parseInt2 = Integer.parseInt(str);
                    int parseInt3 = Integer.parseInt(str2);
                    l bJ = dzVar.bJ();
                    if (bJ != null) {
                        bJ.y().a(parseInt, parseInt2, parseInt3);
                    }
                } catch (NumberFormatException e) {
                    dw.z("Could not parse touch parameters from gmsg.");
                }
            }
        };
        mO = new be();
    }
}