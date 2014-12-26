package com.google.android.gms.internal;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import com.inmobi.commons.analytics.iat.impl.AdTrackerConstants;
import com.isssss.myadv.dao.AdvConfigTable;
import com.isssss.myadv.dao.BannerConfigTable;
import com.millennialmedia.android.MMAdView;
import java.util.Map;
import mobi.vserv.org.ormma.view.OrmmaView;

public final class be implements bb {
    private static int a(DisplayMetrics displayMetrics, Map<String, String> map, String str, int i) {
        String str2 = (String) map.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return dv.a(displayMetrics, Integer.parseInt(str2));
        } catch (NumberFormatException e) {
            dw.z("Could not parse " + str + " in a video GMSG: " + str2);
            return i;
        }
    }

    public void b(dz dzVar, Map<String, String> map) {
        String str = (String) map.get(OrmmaView.ACTION_KEY);
        if (str == null) {
            dw.z("Action missing from video GMSG.");
        } else {
            cc bH = dzVar.bH();
            if (bH == null) {
                dw.z("Could not get ad overlay for a video GMSG.");
            } else {
                boolean equalsIgnoreCase = "new".equalsIgnoreCase(str);
                boolean equalsIgnoreCase2 = BannerConfigTable.COLUMN_POSITION.equalsIgnoreCase(str);
                DisplayMetrics displayMetrics;
                int a;
                if (equalsIgnoreCase || equalsIgnoreCase2) {
                    displayMetrics = dzVar.getContext().getResources().getDisplayMetrics();
                    a = a(displayMetrics, map, "x", 0);
                    int a2 = a(displayMetrics, map, "y", 0);
                    int a3 = a(displayMetrics, map, "w", -1);
                    int a4 = a(displayMetrics, map, "h", -1);
                    if (equalsIgnoreCase && bH.aK() == null) {
                        bH.c(a, a2, a3, a4);
                    } else {
                        bH.b(a, a2, a3, a4);
                    }
                } else {
                    cg aK = bH.aK();
                    if (aK == null) {
                        cg.a(dzVar, "no_video_view", null);
                    } else if ("click".equalsIgnoreCase(str)) {
                        displayMetrics = dzVar.getContext().getResources().getDisplayMetrics();
                        int a5 = a(displayMetrics, map, "x", 0);
                        a = a(displayMetrics, map, "y", 0);
                        long uptimeMillis = SystemClock.uptimeMillis();
                        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, (float) a5, (float) a, 0);
                        aK.b(obtain);
                        obtain.recycle();
                    } else if ("controls".equalsIgnoreCase(str)) {
                        str = (String) map.get("enabled");
                        if (str == null) {
                            dw.z("Enabled parameter missing from controls video GMSG.");
                        } else {
                            aK.k(Boolean.parseBoolean(str));
                        }
                    } else if ("currentTime".equalsIgnoreCase(str)) {
                        str = (String) map.get("time");
                        if (str == null) {
                            dw.z("Time parameter missing from currentTime video GMSG.");
                        } else {
                            try {
                                aK.seekTo((int) (Float.parseFloat(str) * 1000.0f));
                            } catch (NumberFormatException e) {
                                dw.z("Could not parse time parameter from currentTime video GMSG: " + str);
                            }
                        }
                    } else if ("hide".equalsIgnoreCase(str)) {
                        aK.setVisibility(MMAdView.TRANSITION_RANDOM);
                    } else if ("load".equalsIgnoreCase(str)) {
                        aK.aU();
                    } else if ("pause".equalsIgnoreCase(str)) {
                        aK.pause();
                    } else if ("play".equalsIgnoreCase(str)) {
                        aK.play();
                    } else if (AdvConfigTable.COLUMN_SHOW.equalsIgnoreCase(str)) {
                        aK.setVisibility(0);
                    } else if (AdTrackerConstants.SOURCE.equalsIgnoreCase(str)) {
                        aK.o((String) map.get(AdTrackerConstants.SOURCE));
                    } else {
                        dw.z("Unknown video action: " + str);
                    }
                }
            }
        }
    }
}