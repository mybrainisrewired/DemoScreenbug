package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ga.a;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.inmobi.commons.analytics.db.AnalyticsEvent;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.isssss.myadv.dao.BannerInfoTable;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class ic extends ga implements SafeParcelable, ItemScope {
    public static final id CREATOR;
    private static final HashMap<String, a<?, ?>> UI;
    private String HD;
    private double NX;
    private double NY;
    private String Rd;
    private final Set<Integer> UJ;
    private ic UK;
    private List<String> UL;
    private ic UM;
    private String UN;
    private String UO;
    private String UP;
    private List<ic> UQ;
    private int UR;
    private List<ic> US;
    private ic UT;
    private List<ic> UU;
    private String UV;
    private String UW;
    private ic UX;
    private String UY;
    private String UZ;
    private String VA;
    private String VB;
    private String VC;
    private String VD;
    private List<ic> Va;
    private String Vb;
    private String Vc;
    private String Vd;
    private String Ve;
    private String Vf;
    private String Vg;
    private String Vh;
    private String Vi;
    private ic Vj;
    private String Vk;
    private String Vl;
    private String Vm;
    private ic Vn;
    private ic Vo;
    private ic Vp;
    private List<ic> Vq;
    private String Vr;
    private String Vs;
    private String Vt;
    private String Vu;
    private ic Vv;
    private String Vw;
    private String Vx;
    private String Vy;
    private ic Vz;
    private String lY;
    private String mName;
    private String ro;
    private String wp;
    private final int xH;

    static {
        CREATOR = new id();
        UI = new HashMap();
        UI.put("about", a.a("about", MMAdView.TRANSITION_UP, ic.class));
        UI.put("additionalName", a.k("additionalName", MMAdView.TRANSITION_DOWN));
        UI.put("address", a.a("address", MMAdView.TRANSITION_RANDOM, ic.class));
        UI.put("addressCountry", a.j("addressCountry", ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
        UI.put("addressLocality", a.j("addressLocality", ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
        UI.put("addressRegion", a.j("addressRegion", ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
        UI.put("associated_media", a.b("associated_media", ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES, ic.class));
        UI.put("attendeeCount", a.g("attendeeCount", ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
        UI.put("attendees", a.b("attendees", ApiEventType.API_MRAID_USE_CUSTOM_CLOSE, ic.class));
        UI.put("audio", a.a("audio", ApiEventType.API_MRAID_EXPAND, ic.class));
        UI.put("author", a.b("author", ApiEventType.API_MRAID_RESIZE, ic.class));
        UI.put("bestRating", a.j("bestRating", ApiEventType.API_MRAID_CLOSE));
        UI.put("birthDate", a.j("birthDate", ApiEventType.API_MRAID_IS_VIEWABLE));
        UI.put("byArtist", a.a("byArtist", ApiEventType.API_MRAID_GET_PLACEMENT_TYPE, ic.class));
        UI.put("caption", a.j("caption", ApiEventType.API_MRAID_GET_ORIENTATION));
        UI.put("contentSize", a.j("contentSize", ApiEventType.API_MRAID_GET_SCREEN_SIZE));
        UI.put("contentUrl", a.j("contentUrl", ApiEventType.API_MRAID_GET_CURRENT_POSITION));
        UI.put("contributor", a.b("contributor", Encoder.LINE_GROUPS, ic.class));
        UI.put("dateCreated", a.j("dateCreated", ApiEventType.API_MRAID_GET_MAX_SIZE));
        UI.put("dateModified", a.j("dateModified", ApiEventType.API_MRAID_POST_TO_SOCIAL));
        UI.put("datePublished", a.j("datePublished", ApiEventType.API_MRAID_SUPPORTS));
        UI.put(BannerInfoTable.COLUMN_DESCRIPTION, a.j(BannerInfoTable.COLUMN_DESCRIPTION, ApiEventType.API_MRAID_STORE_PICTURE));
        UI.put("duration", a.j("duration", ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
        UI.put("embedUrl", a.j("embedUrl", ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
        UI.put("endDate", a.j("endDate", ApiEventType.API_MRAID_VIBRATE));
        UI.put("familyName", a.j("familyName", ApiEventType.API_MRAID_SEND_SMS));
        UI.put(MMRequest.KEY_GENDER, a.j(MMRequest.KEY_GENDER, ApiEventType.API_MRAID_SEND_MAIL));
        UI.put(Event.INTENT_MAPS, a.a(Event.INTENT_MAPS, ApiEventType.API_MRAID_MAKE_CALL, ic.class));
        UI.put("givenName", a.j("givenName", ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT));
        UI.put(MMLayout.KEY_HEIGHT, a.j(MMLayout.KEY_HEIGHT, ApiEventType.API_MRAID_ASYNC_PING));
        UI.put(AnalyticsEvent.EVENT_ID, a.j(AnalyticsEvent.EVENT_ID, ApiEventType.API_MRAID_PLAY_AUDIO));
        UI.put("image", a.j("image", ApiEventType.API_MRAID_MUTE_AUDIO));
        UI.put("inAlbum", a.a("inAlbum", ApiEventType.API_MRAID_UNMUTE_AUDIO, ic.class));
        UI.put("latitude", a.h("latitude", ApiEventType.API_MRAID_SET_AUDIO_VOLUME));
        UI.put("location", a.a("location", ApiEventType.API_MRAID_GET_AUDIO_VOLUME, ic.class));
        UI.put("longitude", a.h("longitude", ApiEventType.API_MRAID_SEEK_AUDIO));
        UI.put("name", a.j("name", ApiEventType.API_MRAID_PAUSE_AUDIO));
        UI.put("partOfTVSeries", a.a("partOfTVSeries", ApiEventType.API_MRAID_PLAY_VIDEO, ic.class));
        UI.put("performers", a.b("performers", ApiEventType.API_MRAID_MUTE_VIDEO, ic.class));
        UI.put("playerType", a.j("playerType", ApiEventType.API_MRAID_UNMUTE_VIDEO));
        UI.put("postOfficeBoxNumber", a.j("postOfficeBoxNumber", ApiEventType.API_MRAID_IS_VIDEO_MUTED));
        UI.put("postalCode", a.j("postalCode", ApiEventType.API_MRAID_SET_VIDEO_VOLUME));
        UI.put("ratingValue", a.j("ratingValue", ApiEventType.API_MRAID_GET_VIDEO_VOLUME));
        UI.put("reviewRating", a.a("reviewRating", ApiEventType.API_MRAID_SEEK_VIDEO, ic.class));
        UI.put("startDate", a.j("startDate", ApiEventType.API_MRAID_PAUSE_VIDEO));
        UI.put("streetAddress", a.j("streetAddress", ApiEventType.API_MRAID_HIDE_VIDEO));
        UI.put("text", a.j("text", ApiEventType.API_MRAID_SHOW_VIDEO));
        UI.put("thumbnail", a.a("thumbnail", ApiEventType.API_MRAID_CLOSE_VIDEO, ic.class));
        UI.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, a.j(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_THUMBNAIL_URL, ApiEventType.API_MRAID_GET_MIC_INTENSITY));
        UI.put("tickerSymbol", a.j("tickerSymbol", 52));
        UI.put(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, a.j(AnalyticsSQLiteHelper.EVENT_LIST_TYPE, 53));
        UI.put(PlusShare.KEY_CALL_TO_ACTION_URL, a.j(PlusShare.KEY_CALL_TO_ACTION_URL, 54));
        UI.put(MMLayout.KEY_WIDTH, a.j(MMLayout.KEY_WIDTH, 55));
        UI.put("worstRating", a.j("worstRating", 56));
    }

    public ic() {
        this.xH = 1;
        this.UJ = new HashSet();
    }

    ic(Set<Integer> set, int i, ic icVar, List<String> list, ic icVar2, String str, String str2, String str3, List<ic> list2, int i2, List<ic> list3, ic icVar3, List<ic> list4, String str4, String str5, ic icVar4, String str6, String str7, String str8, List<ic> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, ic icVar5, String str18, String str19, String str20, String str21, ic icVar6, double d, ic icVar7, double d2, String str22, ic icVar8, List<ic> list6, String str23, String str24, String str25, String str26, ic icVar9, String str27, String str28, String str29, ic icVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.UJ = set;
        this.xH = i;
        this.UK = icVar;
        this.UL = list;
        this.UM = icVar2;
        this.UN = str;
        this.UO = str2;
        this.UP = str3;
        this.UQ = list2;
        this.UR = i2;
        this.US = list3;
        this.UT = icVar3;
        this.UU = list4;
        this.UV = str4;
        this.UW = str5;
        this.UX = icVar4;
        this.UY = str6;
        this.UZ = str7;
        this.lY = str8;
        this.Va = list5;
        this.Vb = str9;
        this.Vc = str10;
        this.Vd = str11;
        this.HD = str12;
        this.Ve = str13;
        this.Vf = str14;
        this.Vg = str15;
        this.Vh = str16;
        this.Vi = str17;
        this.Vj = icVar5;
        this.Vk = str18;
        this.Vl = str19;
        this.wp = str20;
        this.Vm = str21;
        this.Vn = icVar6;
        this.NX = d;
        this.Vo = icVar7;
        this.NY = d2;
        this.mName = str22;
        this.Vp = icVar8;
        this.Vq = list6;
        this.Vr = str23;
        this.Vs = str24;
        this.Vt = str25;
        this.Vu = str26;
        this.Vv = icVar9;
        this.Vw = str27;
        this.Vx = str28;
        this.Vy = str29;
        this.Vz = icVar10;
        this.VA = str30;
        this.VB = str31;
        this.Rd = str32;
        this.ro = str33;
        this.VC = str34;
        this.VD = str35;
    }

    public ic(Set<Integer> set, ic icVar, List<String> list, ic icVar2, String str, String str2, String str3, List<ic> list2, int i, List<ic> list3, ic icVar3, List<ic> list4, String str4, String str5, ic icVar4, String str6, String str7, String str8, List<ic> list5, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, ic icVar5, String str18, String str19, String str20, String str21, ic icVar6, double d, ic icVar7, double d2, String str22, ic icVar8, List<ic> list6, String str23, String str24, String str25, String str26, ic icVar9, String str27, String str28, String str29, ic icVar10, String str30, String str31, String str32, String str33, String str34, String str35) {
        this.UJ = set;
        this.xH = 1;
        this.UK = icVar;
        this.UL = list;
        this.UM = icVar2;
        this.UN = str;
        this.UO = str2;
        this.UP = str3;
        this.UQ = list2;
        this.UR = i;
        this.US = list3;
        this.UT = icVar3;
        this.UU = list4;
        this.UV = str4;
        this.UW = str5;
        this.UX = icVar4;
        this.UY = str6;
        this.UZ = str7;
        this.lY = str8;
        this.Va = list5;
        this.Vb = str9;
        this.Vc = str10;
        this.Vd = str11;
        this.HD = str12;
        this.Ve = str13;
        this.Vf = str14;
        this.Vg = str15;
        this.Vh = str16;
        this.Vi = str17;
        this.Vj = icVar5;
        this.Vk = str18;
        this.Vl = str19;
        this.wp = str20;
        this.Vm = str21;
        this.Vn = icVar6;
        this.NX = d;
        this.Vo = icVar7;
        this.NY = d2;
        this.mName = str22;
        this.Vp = icVar8;
        this.Vq = list6;
        this.Vr = str23;
        this.Vs = str24;
        this.Vt = str25;
        this.Vu = str26;
        this.Vv = icVar9;
        this.Vw = str27;
        this.Vx = str28;
        this.Vy = str29;
        this.Vz = icVar10;
        this.VA = str30;
        this.VB = str31;
        this.Rd = str32;
        this.ro = str33;
        this.VC = str34;
        this.VD = str35;
    }

    protected boolean a(a aVar) {
        return this.UJ.contains(Integer.valueOf(aVar.ff()));
    }

    protected Object aq(String str) {
        return null;
    }

    protected boolean ar(String str) {
        return false;
    }

    protected Object b(a aVar) {
        switch (aVar.ff()) {
            case MMAdView.TRANSITION_UP:
                return this.UK;
            case MMAdView.TRANSITION_DOWN:
                return this.UL;
            case MMAdView.TRANSITION_RANDOM:
                return this.UM;
            case ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES:
                return this.UN;
            case ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES:
                return this.UO;
            case ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES:
                return this.UP;
            case ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES:
                return this.UQ;
            case ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES:
                return Integer.valueOf(this.UR);
            case ApiEventType.API_MRAID_USE_CUSTOM_CLOSE:
                return this.US;
            case ApiEventType.API_MRAID_EXPAND:
                return this.UT;
            case ApiEventType.API_MRAID_RESIZE:
                return this.UU;
            case ApiEventType.API_MRAID_CLOSE:
                return this.UV;
            case ApiEventType.API_MRAID_IS_VIEWABLE:
                return this.UW;
            case ApiEventType.API_MRAID_GET_PLACEMENT_TYPE:
                return this.UX;
            case ApiEventType.API_MRAID_GET_ORIENTATION:
                return this.UY;
            case ApiEventType.API_MRAID_GET_SCREEN_SIZE:
                return this.UZ;
            case ApiEventType.API_MRAID_GET_CURRENT_POSITION:
                return this.lY;
            case Encoder.LINE_GROUPS:
                return this.Va;
            case ApiEventType.API_MRAID_GET_MAX_SIZE:
                return this.Vb;
            case ApiEventType.API_MRAID_POST_TO_SOCIAL:
                return this.Vc;
            case ApiEventType.API_MRAID_SUPPORTS:
                return this.Vd;
            case ApiEventType.API_MRAID_STORE_PICTURE:
                return this.HD;
            case ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE:
                return this.Ve;
            case ApiEventType.API_MRAID_GET_GALLERY_IMAGE:
                return this.Vf;
            case ApiEventType.API_MRAID_VIBRATE:
                return this.Vg;
            case ApiEventType.API_MRAID_SEND_SMS:
                return this.Vh;
            case ApiEventType.API_MRAID_SEND_MAIL:
                return this.Vi;
            case ApiEventType.API_MRAID_MAKE_CALL:
                return this.Vj;
            case ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT:
                return this.Vk;
            case ApiEventType.API_MRAID_ASYNC_PING:
                return this.Vl;
            case ApiEventType.API_MRAID_PLAY_AUDIO:
                return this.wp;
            case ApiEventType.API_MRAID_MUTE_AUDIO:
                return this.Vm;
            case ApiEventType.API_MRAID_UNMUTE_AUDIO:
                return this.Vn;
            case ApiEventType.API_MRAID_SET_AUDIO_VOLUME:
                return Double.valueOf(this.NX);
            case ApiEventType.API_MRAID_GET_AUDIO_VOLUME:
                return this.Vo;
            case ApiEventType.API_MRAID_SEEK_AUDIO:
                return Double.valueOf(this.NY);
            case ApiEventType.API_MRAID_PAUSE_AUDIO:
                return this.mName;
            case ApiEventType.API_MRAID_PLAY_VIDEO:
                return this.Vp;
            case ApiEventType.API_MRAID_MUTE_VIDEO:
                return this.Vq;
            case ApiEventType.API_MRAID_UNMUTE_VIDEO:
                return this.Vr;
            case ApiEventType.API_MRAID_IS_VIDEO_MUTED:
                return this.Vs;
            case ApiEventType.API_MRAID_SET_VIDEO_VOLUME:
                return this.Vt;
            case ApiEventType.API_MRAID_GET_VIDEO_VOLUME:
                return this.Vu;
            case ApiEventType.API_MRAID_SEEK_VIDEO:
                return this.Vv;
            case ApiEventType.API_MRAID_PAUSE_VIDEO:
                return this.Vw;
            case ApiEventType.API_MRAID_HIDE_VIDEO:
                return this.Vx;
            case ApiEventType.API_MRAID_SHOW_VIDEO:
                return this.Vy;
            case ApiEventType.API_MRAID_CLOSE_VIDEO:
                return this.Vz;
            case ApiEventType.API_MRAID_GET_MIC_INTENSITY:
                return this.VA;
            case 52:
                return this.VB;
            case 53:
                return this.Rd;
            case 54:
                return this.ro;
            case 55:
                return this.VC;
            case 56:
                return this.VD;
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.ff());
        }
    }

    public int describeContents() {
        id idVar = CREATOR;
        return 0;
    }

    public HashMap<String, a<?, ?>> eY() {
        return UI;
    }

    public boolean equals(ic obj) {
        if (!(obj instanceof ic)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        obj = obj;
        Iterator it = UI.values().iterator();
        while (it.hasNext()) {
            a aVar = (a) it.next();
            if (a(aVar)) {
                if (!obj.a(aVar)) {
                    return false;
                }
                if (!b(aVar).equals(obj.b(aVar))) {
                    return false;
                }
            } else if (obj.a(aVar)) {
                return false;
            }
        }
        return true;
    }

    public /* synthetic */ Object freeze() {
        return jq();
    }

    public ItemScope getAbout() {
        return this.UK;
    }

    public List<String> getAdditionalName() {
        return this.UL;
    }

    public ItemScope getAddress() {
        return this.UM;
    }

    public String getAddressCountry() {
        return this.UN;
    }

    public String getAddressLocality() {
        return this.UO;
    }

    public String getAddressRegion() {
        return this.UP;
    }

    public List<ItemScope> getAssociated_media() {
        return (ArrayList) this.UQ;
    }

    public int getAttendeeCount() {
        return this.UR;
    }

    public List<ItemScope> getAttendees() {
        return (ArrayList) this.US;
    }

    public ItemScope getAudio() {
        return this.UT;
    }

    public List<ItemScope> getAuthor() {
        return (ArrayList) this.UU;
    }

    public String getBestRating() {
        return this.UV;
    }

    public String getBirthDate() {
        return this.UW;
    }

    public ItemScope getByArtist() {
        return this.UX;
    }

    public String getCaption() {
        return this.UY;
    }

    public String getContentSize() {
        return this.UZ;
    }

    public String getContentUrl() {
        return this.lY;
    }

    public List<ItemScope> getContributor() {
        return (ArrayList) this.Va;
    }

    public String getDateCreated() {
        return this.Vb;
    }

    public String getDateModified() {
        return this.Vc;
    }

    public String getDatePublished() {
        return this.Vd;
    }

    public String getDescription() {
        return this.HD;
    }

    public String getDuration() {
        return this.Ve;
    }

    public String getEmbedUrl() {
        return this.Vf;
    }

    public String getEndDate() {
        return this.Vg;
    }

    public String getFamilyName() {
        return this.Vh;
    }

    public String getGender() {
        return this.Vi;
    }

    public ItemScope getGeo() {
        return this.Vj;
    }

    public String getGivenName() {
        return this.Vk;
    }

    public String getHeight() {
        return this.Vl;
    }

    public String getId() {
        return this.wp;
    }

    public String getImage() {
        return this.Vm;
    }

    public ItemScope getInAlbum() {
        return this.Vn;
    }

    public double getLatitude() {
        return this.NX;
    }

    public ItemScope getLocation() {
        return this.Vo;
    }

    public double getLongitude() {
        return this.NY;
    }

    public String getName() {
        return this.mName;
    }

    public ItemScope getPartOfTVSeries() {
        return this.Vp;
    }

    public List<ItemScope> getPerformers() {
        return (ArrayList) this.Vq;
    }

    public String getPlayerType() {
        return this.Vr;
    }

    public String getPostOfficeBoxNumber() {
        return this.Vs;
    }

    public String getPostalCode() {
        return this.Vt;
    }

    public String getRatingValue() {
        return this.Vu;
    }

    public ItemScope getReviewRating() {
        return this.Vv;
    }

    public String getStartDate() {
        return this.Vw;
    }

    public String getStreetAddress() {
        return this.Vx;
    }

    public String getText() {
        return this.Vy;
    }

    public ItemScope getThumbnail() {
        return this.Vz;
    }

    public String getThumbnailUrl() {
        return this.VA;
    }

    public String getTickerSymbol() {
        return this.VB;
    }

    public String getType() {
        return this.Rd;
    }

    public String getUrl() {
        return this.ro;
    }

    int getVersionCode() {
        return this.xH;
    }

    public String getWidth() {
        return this.VC;
    }

    public String getWorstRating() {
        return this.VD;
    }

    public boolean hasAbout() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_UP));
    }

    public boolean hasAdditionalName() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_DOWN));
    }

    public boolean hasAddress() {
        return this.UJ.contains(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
    }

    public boolean hasAddressCountry() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
    }

    public boolean hasAddressLocality() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
    }

    public boolean hasAddressRegion() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
    }

    public boolean hasAssociated_media() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
    }

    public boolean hasAttendeeCount() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
    }

    public boolean hasAttendees() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
    }

    public boolean hasAudio() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_EXPAND));
    }

    public boolean hasAuthor() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_RESIZE));
    }

    public boolean hasBestRating() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_CLOSE));
    }

    public boolean hasBirthDate() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
    }

    public boolean hasByArtist() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
    }

    public boolean hasCaption() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION));
    }

    public boolean hasContentSize() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_SCREEN_SIZE));
    }

    public boolean hasContentUrl() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION));
    }

    public boolean hasContributor() {
        return this.UJ.contains(Integer.valueOf(Encoder.LINE_GROUPS));
    }

    public boolean hasDateCreated() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE));
    }

    public boolean hasDateModified() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
    }

    public boolean hasDatePublished() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS));
    }

    public boolean hasDescription() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE));
    }

    public boolean hasDuration() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
    }

    public boolean hasEmbedUrl() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
    }

    public boolean hasEndDate() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE));
    }

    public boolean hasFamilyName() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
    }

    public boolean hasGender() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL));
    }

    public boolean hasGeo() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL));
    }

    public boolean hasGivenName() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT));
    }

    public boolean hasHeight() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_ASYNC_PING));
    }

    public boolean hasId() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_PLAY_AUDIO));
    }

    public boolean hasImage() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_MUTE_AUDIO));
    }

    public boolean hasInAlbum() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_AUDIO));
    }

    public boolean hasLatitude() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_AUDIO_VOLUME));
    }

    public boolean hasLocation() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_AUDIO_VOLUME));
    }

    public boolean hasLongitude() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEEK_AUDIO));
    }

    public boolean hasName() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_AUDIO));
    }

    public boolean hasPartOfTVSeries() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_PLAY_VIDEO));
    }

    public boolean hasPerformers() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_MUTE_VIDEO));
    }

    public boolean hasPlayerType() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_VIDEO));
    }

    public boolean hasPostOfficeBoxNumber() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_IS_VIDEO_MUTED));
    }

    public boolean hasPostalCode() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SET_VIDEO_VOLUME));
    }

    public boolean hasRatingValue() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_VIDEO_VOLUME));
    }

    public boolean hasReviewRating() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SEEK_VIDEO));
    }

    public boolean hasStartDate() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_VIDEO));
    }

    public boolean hasStreetAddress() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_HIDE_VIDEO));
    }

    public boolean hasText() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_SHOW_VIDEO));
    }

    public boolean hasThumbnail() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_CLOSE_VIDEO));
    }

    public boolean hasThumbnailUrl() {
        return this.UJ.contains(Integer.valueOf(ApiEventType.API_MRAID_GET_MIC_INTENSITY));
    }

    public boolean hasTickerSymbol() {
        return this.UJ.contains(Integer.valueOf(52));
    }

    public boolean hasType() {
        return this.UJ.contains(Integer.valueOf(53));
    }

    public boolean hasUrl() {
        return this.UJ.contains(Integer.valueOf(54));
    }

    public boolean hasWidth() {
        return this.UJ.contains(Integer.valueOf(55));
    }

    public boolean hasWorstRating() {
        return this.UJ.contains(Integer.valueOf(56));
    }

    public int hashCode() {
        Iterator it = UI.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            int hashCode;
            a aVar = (a) it.next();
            if (a(aVar)) {
                hashCode = b(aVar).hashCode() + i + aVar.ff();
            } else {
                hashCode = i;
            }
            i = hashCode;
        }
        return i;
    }

    public boolean isDataValid() {
        return true;
    }

    Set<Integer> ja() {
        return this.UJ;
    }

    ic jb() {
        return this.UK;
    }

    ic jc() {
        return this.UM;
    }

    List<ic> jd() {
        return this.UQ;
    }

    List<ic> je() {
        return this.US;
    }

    ic jf() {
        return this.UT;
    }

    List<ic> jg() {
        return this.UU;
    }

    ic jh() {
        return this.UX;
    }

    List<ic> ji() {
        return this.Va;
    }

    ic jj() {
        return this.Vj;
    }

    ic jk() {
        return this.Vn;
    }

    ic jl() {
        return this.Vo;
    }

    ic jm() {
        return this.Vp;
    }

    List<ic> jn() {
        return this.Vq;
    }

    ic jo() {
        return this.Vv;
    }

    ic jp() {
        return this.Vz;
    }

    public ic jq() {
        return this;
    }

    public void writeToParcel(Parcel out, int flags) {
        id idVar = CREATOR;
        id.a(this, out, flags);
    }
}