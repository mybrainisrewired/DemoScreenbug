package com.google.android.gms.cast;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.images.WebImage;
import com.google.android.gms.internal.ew;
import com.isssss.myadv.dao.BannerInfoTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaMetadata {
    public static final String KEY_ALBUM_ARTIST = "com.google.android.gms.cast.metadata.ALBUM_ARTIST";
    public static final String KEY_ALBUM_TITLE = "com.google.android.gms.cast.metadata.ALBUM_TITLE";
    public static final String KEY_ARTIST = "com.google.android.gms.cast.metadata.ARTIST";
    public static final String KEY_BROADCAST_DATE = "com.google.android.gms.cast.metadata.BROADCAST_DATE";
    public static final String KEY_COMPOSER = "com.google.android.gms.cast.metadata.COMPOSER";
    public static final String KEY_CREATION_DATE = "com.google.android.gms.cast.metadata.CREATION_DATE";
    public static final String KEY_DISC_NUMBER = "com.google.android.gms.cast.metadata.DISC_NUMBER";
    public static final String KEY_EPISODE_NUMBER = "com.google.android.gms.cast.metadata.EPISODE_NUMBER";
    public static final String KEY_HEIGHT = "com.google.android.gms.cast.metadata.HEIGHT";
    public static final String KEY_LOCATION_LATITUDE = "com.google.android.gms.cast.metadata.LOCATION_LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "com.google.android.gms.cast.metadata.LOCATION_LONGITUDE";
    public static final String KEY_LOCATION_NAME = "com.google.android.gms.cast.metadata.LOCATION_NAME";
    public static final String KEY_RELEASE_DATE = "com.google.android.gms.cast.metadata.RELEASE_DATE";
    public static final String KEY_SEASON_NUMBER = "com.google.android.gms.cast.metadata.SEASON_NUMBER";
    public static final String KEY_SERIES_TITLE = "com.google.android.gms.cast.metadata.SERIES_TITLE";
    public static final String KEY_STUDIO = "com.google.android.gms.cast.metadata.STUDIO";
    public static final String KEY_SUBTITLE = "com.google.android.gms.cast.metadata.SUBTITLE";
    public static final String KEY_TITLE = "com.google.android.gms.cast.metadata.TITLE";
    public static final String KEY_TRACK_NUMBER = "com.google.android.gms.cast.metadata.TRACK_NUMBER";
    public static final String KEY_WIDTH = "com.google.android.gms.cast.metadata.WIDTH";
    public static final int MEDIA_TYPE_GENERIC = 0;
    public static final int MEDIA_TYPE_MOVIE = 1;
    public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
    public static final int MEDIA_TYPE_PHOTO = 4;
    public static final int MEDIA_TYPE_TV_SHOW = 2;
    public static final int MEDIA_TYPE_USER = 100;
    private static final String[] yp;
    private static final a yq;
    private final List<WebImage> xJ;
    private final Bundle yr;
    private int ys;

    private static class a {
        private final Map<String, String> yt;
        private final Map<String, String> yu;
        private final Map<String, Integer> yv;

        public a() {
            this.yt = new HashMap();
            this.yu = new HashMap();
            this.yv = new HashMap();
        }

        public String R(String str) {
            return (String) this.yt.get(str);
        }

        public String S(String str) {
            return (String) this.yu.get(str);
        }

        public int T(String str) {
            Integer num = (Integer) this.yv.get(str);
            return num != null ? num.intValue() : MEDIA_TYPE_GENERIC;
        }

        public a a(String str, String str2, int i) {
            this.yt.put(str, str2);
            this.yu.put(str2, str);
            this.yv.put(str, Integer.valueOf(i));
            return this;
        }
    }

    static {
        yp = new String[]{null, "String", "int", "double", "ISO-8601 date String"};
        yq = new a().a(KEY_CREATION_DATE, "creationDateTime", MEDIA_TYPE_PHOTO).a(KEY_RELEASE_DATE, "releaseDate", MEDIA_TYPE_PHOTO).a(KEY_BROADCAST_DATE, "originalAirdate", MEDIA_TYPE_PHOTO).a(KEY_TITLE, BannerInfoTable.COLUMN_TITLE, MEDIA_TYPE_MOVIE).a(KEY_SUBTITLE, "subtitle", MEDIA_TYPE_MOVIE).a(KEY_ARTIST, "artist", MEDIA_TYPE_MOVIE).a(KEY_ALBUM_ARTIST, "albumArtist", MEDIA_TYPE_MOVIE).a(KEY_ALBUM_TITLE, "albumName", MEDIA_TYPE_MOVIE).a(KEY_COMPOSER, "composer", MEDIA_TYPE_MOVIE).a(KEY_DISC_NUMBER, "discNumber", MEDIA_TYPE_TV_SHOW).a(KEY_TRACK_NUMBER, "trackNumber", MEDIA_TYPE_TV_SHOW).a(KEY_SEASON_NUMBER, "season", MEDIA_TYPE_TV_SHOW).a(KEY_EPISODE_NUMBER, "episode", MEDIA_TYPE_TV_SHOW).a(KEY_SERIES_TITLE, "seriesTitle", MEDIA_TYPE_MOVIE).a(KEY_STUDIO, "studio", MEDIA_TYPE_MOVIE).a(KEY_WIDTH, MMLayout.KEY_WIDTH, MEDIA_TYPE_TV_SHOW).a(KEY_HEIGHT, MMLayout.KEY_HEIGHT, MEDIA_TYPE_TV_SHOW).a(KEY_LOCATION_NAME, "location", MEDIA_TYPE_MOVIE).a(KEY_LOCATION_LATITUDE, "latitude", MEDIA_TYPE_MUSIC_TRACK).a(KEY_LOCATION_LONGITUDE, "longitude", MEDIA_TYPE_MUSIC_TRACK);
    }

    public MediaMetadata() {
        this(0);
    }

    public MediaMetadata(int mediaType) {
        this.xJ = new ArrayList();
        this.yr = new Bundle();
        this.ys = mediaType;
    }

    private void a(JSONObject jSONObject, String... strArr) {
        try {
            int length = strArr.length;
            int i = MEDIA_TYPE_GENERIC;
            while (i < length) {
                String str = strArr[i];
                if (this.yr.containsKey(str)) {
                    switch (yq.T(str)) {
                        case MEDIA_TYPE_MOVIE:
                        case MEDIA_TYPE_PHOTO:
                            jSONObject.put(yq.R(str), this.yr.getString(str));
                            break;
                        case MEDIA_TYPE_TV_SHOW:
                            jSONObject.put(yq.R(str), this.yr.getInt(str));
                            break;
                        case MEDIA_TYPE_MUSIC_TRACK:
                            jSONObject.put(yq.R(str), this.yr.getDouble(str));
                            break;
                        default:
                            break;
                    }
                }
                i++;
            }
            Iterator it = this.yr.keySet().iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                if (!str2.startsWith("com.google.")) {
                    Object obj = this.yr.get(str2);
                    if (obj instanceof String) {
                        jSONObject.put(str2, obj);
                    } else if (obj instanceof Integer) {
                        jSONObject.put(str2, obj);
                    } else if (obj instanceof Double) {
                        jSONObject.put(str2, obj);
                    }
                }
            }
        } catch (JSONException e) {
        }
    }

    private boolean a(Bundle bundle, Bundle bundle2) {
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Object obj = bundle.get(str);
            Object obj2 = bundle2.get(str);
            if (obj instanceof Bundle && obj2 instanceof Bundle && !a((Bundle) obj, (Bundle) obj2)) {
                return false;
            }
            if (obj == null) {
                return false;
            }
            if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    private void b(JSONObject jSONObject, String... strArr) {
        Set hashSet = new HashSet(Arrays.asList(strArr));
        try {
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String str = (String) keys.next();
                if (!"metadataType".equals(str)) {
                    String S = yq.S(str);
                    if (S == null) {
                        Object obj = jSONObject.get(str);
                        if (obj instanceof String) {
                            this.yr.putString(str, (String) obj);
                        } else if (obj instanceof Integer) {
                            this.yr.putInt(str, ((Integer) obj).intValue());
                        } else if (obj instanceof Double) {
                            this.yr.putDouble(str, ((Double) obj).doubleValue());
                        }
                    } else if (hashSet.contains(S)) {
                        try {
                            Object obj2 = jSONObject.get(str);
                            if (obj2 != null) {
                                switch (yq.T(S)) {
                                    case MEDIA_TYPE_MOVIE:
                                        if (obj2 instanceof String) {
                                            this.yr.putString(S, (String) obj2);
                                        }
                                        break;
                                    case MEDIA_TYPE_TV_SHOW:
                                        if (obj2 instanceof Integer) {
                                            this.yr.putInt(S, ((Integer) obj2).intValue());
                                        }
                                        break;
                                    case MEDIA_TYPE_MUSIC_TRACK:
                                        if (obj2 instanceof Double) {
                                            this.yr.putDouble(S, ((Double) obj2).doubleValue());
                                        }
                                        break;
                                    case MEDIA_TYPE_PHOTO:
                                        if (obj2 instanceof String && ew.ac((String) obj2) != null) {
                                            this.yr.putString(S, (String) obj2);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            }
        } catch (JSONException e2) {
        }
    }

    private void d(String str, int i) throws IllegalArgumentException {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("null and empty keys are not allowed");
        }
        int T = yq.T(str);
        if (T != i && T != 0) {
            throw new IllegalArgumentException("Value for " + str + " must be a " + yp[i]);
        }
    }

    public void addImage(WebImage image) {
        this.xJ.add(image);
    }

    public void c(JSONObject jSONObject) {
        clear();
        this.ys = 0;
        try {
            this.ys = jSONObject.getInt("metadataType");
        } catch (JSONException e) {
        }
        ew.a(this.xJ, jSONObject);
        switch (this.ys) {
            case MEDIA_TYPE_GENERIC:
                b(jSONObject, new String[]{KEY_TITLE, KEY_ARTIST, KEY_SUBTITLE, KEY_RELEASE_DATE});
            case MEDIA_TYPE_MOVIE:
                b(jSONObject, new String[]{KEY_TITLE, KEY_STUDIO, KEY_SUBTITLE, KEY_RELEASE_DATE});
            case MEDIA_TYPE_TV_SHOW:
                b(jSONObject, new String[]{KEY_TITLE, KEY_SERIES_TITLE, KEY_SEASON_NUMBER, KEY_EPISODE_NUMBER, KEY_BROADCAST_DATE});
            case MEDIA_TYPE_MUSIC_TRACK:
                b(jSONObject, new String[]{KEY_TITLE, KEY_ALBUM_TITLE, KEY_ARTIST, KEY_ALBUM_ARTIST, KEY_COMPOSER, KEY_TRACK_NUMBER, KEY_DISC_NUMBER, KEY_RELEASE_DATE});
            case MEDIA_TYPE_PHOTO:
                b(jSONObject, new String[]{KEY_TITLE, KEY_ARTIST, KEY_LOCATION_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_WIDTH, KEY_HEIGHT, KEY_CREATION_DATE});
            default:
                b(jSONObject, new String[0]);
        }
    }

    public void clear() {
        this.yr.clear();
        this.xJ.clear();
    }

    public void clearImages() {
        this.xJ.clear();
    }

    public boolean containsKey(String key) {
        return this.yr.containsKey(key);
    }

    public JSONObject dB() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("metadataType", this.ys);
        } catch (JSONException e) {
        }
        ew.a(jSONObject, this.xJ);
        switch (this.ys) {
            case MEDIA_TYPE_GENERIC:
                a(jSONObject, new String[]{KEY_TITLE, KEY_ARTIST, KEY_SUBTITLE, KEY_RELEASE_DATE});
                break;
            case MEDIA_TYPE_MOVIE:
                a(jSONObject, new String[]{KEY_TITLE, KEY_STUDIO, KEY_SUBTITLE, KEY_RELEASE_DATE});
                break;
            case MEDIA_TYPE_TV_SHOW:
                a(jSONObject, new String[]{KEY_TITLE, KEY_SERIES_TITLE, KEY_SEASON_NUMBER, KEY_EPISODE_NUMBER, KEY_BROADCAST_DATE});
                break;
            case MEDIA_TYPE_MUSIC_TRACK:
                a(jSONObject, new String[]{KEY_TITLE, KEY_ARTIST, KEY_ALBUM_TITLE, KEY_ALBUM_ARTIST, KEY_COMPOSER, KEY_TRACK_NUMBER, KEY_DISC_NUMBER, KEY_RELEASE_DATE});
                break;
            case MEDIA_TYPE_PHOTO:
                a(jSONObject, new String[]{KEY_TITLE, KEY_ARTIST, KEY_LOCATION_NAME, KEY_LOCATION_LATITUDE, KEY_LOCATION_LONGITUDE, KEY_WIDTH, KEY_HEIGHT, KEY_CREATION_DATE});
                break;
            default:
                a(jSONObject, new String[0]);
                break;
        }
        return jSONObject;
    }

    public boolean equals(MediaMetadata other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MediaMetadata)) {
            return false;
        }
        other = other;
        return a(this.yr, other.yr) && this.xJ.equals(other.xJ);
    }

    public Calendar getDate(String key) {
        d(key, MEDIA_TYPE_PHOTO);
        String string = this.yr.getString(key);
        return string != null ? ew.ac(string) : null;
    }

    public String getDateAsString(String key) {
        d(key, MEDIA_TYPE_PHOTO);
        return this.yr.getString(key);
    }

    public double getDouble(String key) {
        d(key, MEDIA_TYPE_MUSIC_TRACK);
        return this.yr.getDouble(key);
    }

    public List<WebImage> getImages() {
        return this.xJ;
    }

    public int getInt(String key) {
        d(key, MEDIA_TYPE_TV_SHOW);
        return this.yr.getInt(key);
    }

    public int getMediaType() {
        return this.ys;
    }

    public String getString(String key) {
        d(key, MEDIA_TYPE_MOVIE);
        return this.yr.getString(key);
    }

    public boolean hasImages() {
        return (this.xJ == null || this.xJ.isEmpty()) ? false : true;
    }

    public int hashCode() {
        Iterator it = this.yr.keySet().iterator();
        int i = 17;
        while (it.hasNext()) {
            i *= 31;
            i = this.yr.get((String) it.next()).hashCode() + i;
        }
        return i * 31 + this.xJ.hashCode();
    }

    public Set<String> keySet() {
        return this.yr.keySet();
    }

    public void putDate(String key, Calendar value) {
        d(key, MEDIA_TYPE_PHOTO);
        this.yr.putString(key, ew.a(value));
    }

    public void putDouble(String key, double value) {
        d(key, MEDIA_TYPE_MUSIC_TRACK);
        this.yr.putDouble(key, value);
    }

    public void putInt(String key, int value) {
        d(key, MEDIA_TYPE_TV_SHOW);
        this.yr.putInt(key, value);
    }

    public void putString(String key, String value) {
        d(key, MEDIA_TYPE_MOVIE);
        this.yr.putString(key, value);
    }
}