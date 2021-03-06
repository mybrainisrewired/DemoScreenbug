package com.google.android.gms.analytics;

import android.content.Context;

class ai extends k<aj> {

    private static class a implements com.google.android.gms.analytics.k.a<aj> {
        private final aj wg;

        public a() {
            this.wg = new aj();
        }

        public void a(String str, int i) {
            if ("ga_sessionTimeout".equals(str)) {
                this.wg.wj = i;
            } else {
                aa.z("int configuration name not recognized:  " + str);
            }
        }

        public void a(String str, String str2) {
            this.wg.wn.put(str, str2);
        }

        public void b(String str, String str2) {
            if ("ga_trackingId".equals(str)) {
                this.wg.wh = str2;
            } else if ("ga_sampleFrequency".equals(str)) {
                try {
                    this.wg.wi = Double.parseDouble(str2);
                } catch (NumberFormatException e) {
                    aa.w("Error parsing ga_sampleFrequency value: " + str2);
                }
            } else {
                aa.z("string configuration name not recognized:  " + str);
            }
        }

        public void c(String str, boolean z) {
            int i = 1;
            aj ajVar;
            if ("ga_autoActivityTracking".equals(str)) {
                ajVar = this.wg;
                if (!z) {
                    i = 0;
                }
                ajVar.wk = i;
            } else if ("ga_anonymizeIp".equals(str)) {
                ajVar = this.wg;
                if (!z) {
                    i = 0;
                }
                ajVar.wl = i;
            } else if ("ga_reportUncaughtExceptions".equals(str)) {
                ajVar = this.wg;
                if (!z) {
                    i = 0;
                }
                ajVar.wm = i;
            } else {
                aa.z("bool configuration name not recognized:  " + str);
            }
        }

        public /* synthetic */ j cg() {
            return di();
        }

        public aj di() {
            return this.wg;
        }
    }

    public ai(Context context) {
        super(context, new a());
    }
}