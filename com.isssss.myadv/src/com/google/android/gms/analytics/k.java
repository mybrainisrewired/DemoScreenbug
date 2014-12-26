package com.google.android.gms.analytics;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

abstract class k<T extends j> {
    Context mContext;
    a<T> sy;

    public static interface a<U extends j> {
        void a(String str, int i);

        void a(String str, String str2);

        void b(String str, String str2);

        void c(String str, boolean z);

        U cg();
    }

    public k(Context context, a<T> aVar) {
        this.mContext = context;
        this.sy = aVar;
    }

    private T a(XmlResourceParser xmlResourceParser) {
        String trim;
        try {
            xmlResourceParser.next();
            int eventType = xmlResourceParser.getEventType();
            while (eventType != 1) {
                if (xmlResourceParser.getEventType() == 2) {
                    String toLowerCase = xmlResourceParser.getName().toLowerCase();
                    if (toLowerCase.equals("screenname")) {
                        toLowerCase = xmlResourceParser.getAttributeValue(null, "name");
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(toLowerCase) || TextUtils.isEmpty(trim))) {
                            this.sy.a(toLowerCase, trim);
                        }
                    } else if (toLowerCase.equals("string")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, "name");
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || trim == null)) {
                            this.sy.b(attributeValue, trim);
                        }
                    } else if (toLowerCase.equals("bool")) {
                        attributeValue = xmlResourceParser.getAttributeValue(null, "name");
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(attributeValue) || TextUtils.isEmpty(trim))) {
                            try {
                                this.sy.c(attributeValue, Boolean.parseBoolean(trim));
                            } catch (NumberFormatException e) {
                                aa.w("Error parsing bool configuration value: " + trim);
                            }
                        }
                    } else if (toLowerCase.equals("integer")) {
                        toLowerCase = xmlResourceParser.getAttributeValue(null, "name");
                        trim = xmlResourceParser.nextText().trim();
                        if (!(TextUtils.isEmpty(toLowerCase) || TextUtils.isEmpty(trim))) {
                            try {
                                this.sy.a(toLowerCase, Integer.parseInt(trim));
                            } catch (NumberFormatException e2) {
                                aa.w("Error parsing int configuration value: " + trim);
                            }
                        }
                    }
                }
                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e3) {
            aa.w("Error parsing tracker configuration file: " + e3);
        } catch (IOException e4) {
            aa.w("Error parsing tracker configuration file: " + e4);
        }
        return this.sy.cg();
    }

    public T p(int i) {
        try {
            return a(this.mContext.getResources().getXml(i));
        } catch (NotFoundException e) {
            aa.z("inflate() called with unknown resourceId: " + e);
            return null;
        }
    }
}