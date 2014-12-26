package com.google.android.gms.tagmanager;

import android.net.Uri;
import com.mopub.common.Preconditions;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

class cd {
    private static cd YP;
    private volatile String WJ;
    private volatile a YQ;
    private volatile String YR;
    private volatile String YS;

    enum a {
        NONE,
        CONTAINER,
        CONTAINER_DEBUG;

        static {
            YT = new a("NONE", 0);
            YU = new a("CONTAINER", 1);
            YV = new a("CONTAINER_DEBUG", 2);
            YW = new a[]{YT, YU, YV};
        }
    }

    cd() {
        clear();
    }

    private String bI(String str) {
        return str.split("&")[0].split("=")[1];
    }

    private String h(Uri uri) {
        return uri.getQuery().replace("&gtm_debug=x", Preconditions.EMPTY_ARGUMENTS);
    }

    static cd kT() {
        cd cdVar;
        synchronized (cd.class) {
            if (YP == null) {
                YP = new cd();
            }
            cdVar = YP;
        }
        return cdVar;
    }

    void clear() {
        this.YQ = a.YT;
        this.YR = null;
        this.WJ = null;
        this.YS = null;
    }

    synchronized boolean g(Uri uri) {
        boolean z = true;
        synchronized (this) {
            try {
                String decode = URLDecoder.decode(uri.toString(), "UTF-8");
                if (decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_auth=\\S+&gtm_preview=\\d+(&gtm_debug=x)?$")) {
                    bh.y("Container preview url: " + decode);
                    if (decode.matches(".*?&gtm_debug=x$")) {
                        this.YQ = a.YV;
                    } else {
                        this.YQ = a.YU;
                    }
                    this.YS = h(uri);
                    if (this.YQ == a.YU || this.YQ == a.YV) {
                        this.YR = "/r?" + this.YS;
                    }
                    this.WJ = bI(this.YS);
                } else {
                    if (!decode.matches("^tagmanager.c.\\S+:\\/\\/preview\\/p\\?id=\\S+&gtm_preview=$")) {
                        bh.z("Invalid preview uri: " + decode);
                        z = false;
                    } else if (bI(uri.getQuery()).equals(this.WJ)) {
                        bh.y("Exit preview mode for container: " + this.WJ);
                        this.YQ = a.YT;
                        this.YR = null;
                    } else {
                        z = false;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                z = false;
            }
        }
        return z;
    }

    String getContainerId() {
        return this.WJ;
    }

    a kU() {
        return this.YQ;
    }

    String kV() {
        return this.YR;
    }
}