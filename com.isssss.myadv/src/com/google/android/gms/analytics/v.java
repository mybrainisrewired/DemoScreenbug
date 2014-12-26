package com.google.android.gms.analytics;

import android.content.Context;

class v extends k<w> {

    private static class a implements com.google.android.gms.analytics.k.a<w> {
        private final w uU;

        public a() {
            this.uU = new w();
        }

        public void a(String str, int i) {
            if ("ga_dispatchPeriod".equals(str)) {
                this.uU.uW = i;
            } else {
                aa.z("int configuration name not recognized:  " + str);
            }
        }

        public void a(String str, String str2) {
        }

        public void b(String str, String str2) {
            if ("ga_appName".equals(str)) {
                this.uU.so = str2;
            } else if ("ga_appVersion".equals(str)) {
                this.uU.sp = str2;
            } else if ("ga_logLevel".equals(str)) {
                this.uU.uV = str2;
            } else {
                aa.z("string configuration name not recognized:  " + str);
            }
        }

        public void c(String str, boolean z) {
            if ("ga_dryRun".equals(str)) {
                this.uU.uX = z ? 1 : 0;
            } else {
                aa.z("bool configuration name not recognized:  " + str);
            }
        }

        public w cB() {
            return this.uU;
        }

        public /* synthetic */ j cg() {
            return cB();
        }
    }

    public v(Context context) {
        super(context, new a());
    }
}