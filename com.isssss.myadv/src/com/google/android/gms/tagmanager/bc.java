package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.a;
import com.google.android.gms.internal.d;
import java.util.Locale;
import java.util.Map;

class bc extends aj {
    private static final String ID;

    static {
        ID = a.L.toString();
    }

    public bc() {
        super(ID, new String[0]);
    }

    public boolean jX() {
        return false;
    }

    public d.a x(Map<String, d.a> map) {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return dh.lT();
        }
        String language = locale.getLanguage();
        return language == null ? dh.lT() : dh.r(language.toLowerCase());
    }
}