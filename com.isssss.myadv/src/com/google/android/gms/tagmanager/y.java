package com.google.android.gms.tagmanager;

import android.content.Context;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

class y implements aq {
    private static y XM;
    private static final Object sf;
    private String XN;
    private String XO;
    private ar XP;
    private cf Xa;

    static {
        sf = new Object();
    }

    private y(Context context) {
        this(as.H(context), new cv());
    }

    y(ar arVar, cf cfVar) {
        this.XP = arVar;
        this.Xa = cfVar;
    }

    public static aq F(Context context) {
        aq aqVar;
        synchronized (sf) {
            if (XM == null) {
                XM = new y(context);
            }
            aqVar = XM;
        }
        return aqVar;
    }

    public boolean bz(String str) {
        if (this.Xa.cS()) {
            if (!(this.XN == null || this.XO == null)) {
                try {
                    str = this.XN + "?" + this.XO + "=" + URLEncoder.encode(str, "UTF-8");
                    bh.y("Sending wrapped url hit: " + str);
                } catch (UnsupportedEncodingException e) {
                    bh.c("Error wrapping URL for testing.", e);
                    return false;
                }
            }
            this.XP.bC(str);
            return true;
        } else {
            bh.z("Too many urls sent too quickly with the TagManagerSender, rate limiting invoked.");
            return false;
        }
    }
}