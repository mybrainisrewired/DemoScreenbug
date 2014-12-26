package com.google.android.gms.internal;

import android.text.TextUtils;
import com.google.ads.AdSize;
import com.google.android.gms.common.images.WebImage;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ew {
    private static final String[] Ac;
    private static final String Ad;
    private static final er zb;

    static {
        zb = new er("MetadataUtils");
        Ac = new String[]{"Z", "+hh", "+hhmm", "+hh:mm"};
        Ad = "yyyyMMdd'T'HHmmss" + Ac[0];
    }

    public static String a(Calendar calendar) {
        if (calendar == null) {
            zb.b("Calendar object cannot be null", new Object[0]);
            return null;
        } else {
            String str = Ad;
            if (calendar.get(ApiEventType.API_MRAID_EXPAND) == 0 && calendar.get(ApiEventType.API_MRAID_RESIZE) == 0 && calendar.get(ApiEventType.API_MRAID_CLOSE) == 0) {
                str = "yyyyMMdd";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
            simpleDateFormat.setTimeZone(calendar.getTimeZone());
            str = simpleDateFormat.format(calendar.getTime());
            return str.endsWith("+0000") ? str.replace("+0000", Ac[0]) : str;
        }
    }

    public static void a(List<WebImage> list, JSONObject jSONObject) {
        try {
            list.clear();
            JSONArray jSONArray = jSONObject.getJSONArray("images");
            int length = jSONArray.length();
            int i = 0;
            while (i < length) {
                try {
                    list.add(new WebImage(jSONArray.getJSONObject(i)));
                } catch (IllegalArgumentException e) {
                }
                i++;
            }
        } catch (JSONException e2) {
        }
    }

    public static void a(JSONObject jSONObject, List<WebImage> list) {
        if (list != null && !list.isEmpty()) {
            JSONArray jSONArray = new JSONArray();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                jSONArray.put(((WebImage) it.next()).dB());
            }
            try {
                jSONObject.put("images", jSONArray);
            } catch (JSONException e) {
            }
        }
    }

    public static Calendar ac(String str) {
        if (TextUtils.isEmpty(str)) {
            zb.b("Input string is empty or null", new Object[0]);
            return null;
        } else {
            String ad = ad(str);
            if (TextUtils.isEmpty(ad)) {
                zb.b("Invalid date format", new Object[0]);
                return null;
            } else {
                String ae = ae(str);
                String str2 = "yyyyMMdd";
                if (!TextUtils.isEmpty(ae)) {
                    ad = ad + "T" + ae;
                    str2 = ae.length() == "HHmmss".length() ? "yyyyMMdd'T'HHmmss" : Ad;
                }
                Calendar instance = GregorianCalendar.getInstance();
                try {
                    instance.setTime(new SimpleDateFormat(str2).parse(ad));
                    return instance;
                } catch (ParseException e) {
                    ParseException parseException = e;
                    zb.b("Error parsing string: %s", new Object[]{parseException.getMessage()});
                    return null;
                }
            }
        }
    }

    private static String ad(String str) {
        if (TextUtils.isEmpty(str)) {
            zb.b("Input string is empty or null", new Object[0]);
            return null;
        } else {
            try {
                return str.substring(0, "yyyyMMdd".length());
            } catch (IndexOutOfBoundsException e) {
                IndexOutOfBoundsException indexOutOfBoundsException = e;
                zb.c("Error extracting the date: %s", new Object[]{indexOutOfBoundsException.getMessage()});
                return null;
            }
        }
    }

    private static String ae(String str) {
        if (TextUtils.isEmpty(str)) {
            zb.b("string is empty or null", new Object[0]);
            return null;
        } else {
            int indexOf = str.indexOf(84);
            int i = indexOf + 1;
            if (indexOf != "yyyyMMdd".length()) {
                zb.b("T delimeter is not found", new Object[0]);
                return null;
            } else {
                try {
                    String substring = str.substring(i);
                    if (substring.length() == "HHmmss".length()) {
                        return substring;
                    }
                    switch (substring.charAt("HHmmss".length())) {
                        case ApiEventType.API_MRAID_IS_VIDEO_MUTED:
                        case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                            return af(substring) ? substring.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2") : null;
                        case AdSize.LARGE_AD_HEIGHT:
                            return substring.length() == "HHmmss".length() + Ac[0].length() ? substring.substring(0, substring.length() - 1) + "+0000" : null;
                        default:
                            return null;
                    }
                } catch (IndexOutOfBoundsException e) {
                    IndexOutOfBoundsException indexOutOfBoundsException = e;
                    zb.b("Error extracting the time substring: %s", new Object[]{indexOutOfBoundsException.getMessage()});
                    return null;
                }
            }
        }
    }

    private static boolean af(String str) {
        int length = str.length();
        int length2 = "HHmmss".length();
        return length == Ac[1].length() + length2 || length == Ac[2].length() + length2 || length == length2 + Ac[3].length();
    }
}