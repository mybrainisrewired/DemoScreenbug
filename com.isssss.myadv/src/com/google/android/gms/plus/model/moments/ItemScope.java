package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.ic;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ItemScope extends Freezable<ItemScope> {

    public static class Builder {
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

        public Builder() {
            this.UJ = new HashSet();
        }

        public ItemScope build() {
            return new ic(this.UJ, this.UK, this.UL, this.UM, this.UN, this.UO, this.UP, this.UQ, this.UR, this.US, this.UT, this.UU, this.UV, this.UW, this.UX, this.UY, this.UZ, this.lY, this.Va, this.Vb, this.Vc, this.Vd, this.HD, this.Ve, this.Vf, this.Vg, this.Vh, this.Vi, this.Vj, this.Vk, this.Vl, this.wp, this.Vm, this.Vn, this.NX, this.Vo, this.NY, this.mName, this.Vp, this.Vq, this.Vr, this.Vs, this.Vt, this.Vu, this.Vv, this.Vw, this.Vx, this.Vy, this.Vz, this.VA, this.VB, this.Rd, this.ro, this.VC, this.VD);
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAbout(ItemScope about) {
            this.UK = (ic) about;
            this.UJ.add(Integer.valueOf(MMAdView.TRANSITION_UP));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAdditionalName(List<String> additionalName) {
            this.UL = additionalName;
            this.UJ.add(Integer.valueOf(MMAdView.TRANSITION_DOWN));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAddress(ItemScope address) {
            this.UM = (ic) address;
            this.UJ.add(Integer.valueOf(MMAdView.TRANSITION_RANDOM));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAddressCountry(String addressCountry) {
            this.UN = addressCountry;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAddressLocality(String addressLocality) {
            this.UO = addressLocality;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_RESIZE_PROPERTIES));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAddressRegion(String addressRegion) {
            this.UP = addressRegion;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAssociated_media(List<ItemScope> associated_media) {
            this.UQ = associated_media;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_ORIENTATION_PROPERTIES));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAttendeeCount(int attendeeCount) {
            this.UR = attendeeCount;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_RESIZE_PROPERTIES));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAttendees(List<ItemScope> attendees) {
            this.US = attendees;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_USE_CUSTOM_CLOSE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAudio(ItemScope audio) {
            this.UT = (ic) audio;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_EXPAND));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setAuthor(List<ItemScope> author) {
            this.UU = author;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_RESIZE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setBestRating(String bestRating) {
            this.UV = bestRating;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_CLOSE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setBirthDate(String birthDate) {
            this.UW = birthDate;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIEWABLE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setByArtist(ItemScope byArtist) {
            this.UX = (ic) byArtist;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_PLACEMENT_TYPE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setCaption(String caption) {
            this.UY = caption;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_ORIENTATION));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setContentSize(String contentSize) {
            this.UZ = contentSize;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_SCREEN_SIZE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setContentUrl(String contentUrl) {
            this.lY = contentUrl;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_CURRENT_POSITION));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setContributor(List<ItemScope> contributor) {
            this.Va = contributor;
            this.UJ.add(Integer.valueOf(Encoder.LINE_GROUPS));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setDateCreated(String dateCreated) {
            this.Vb = dateCreated;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_MAX_SIZE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setDateModified(String dateModified) {
            this.Vc = dateModified;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_POST_TO_SOCIAL));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setDatePublished(String datePublished) {
            this.Vd = datePublished;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SUPPORTS));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setDescription(String description) {
            this.HD = description;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_STORE_PICTURE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setDuration(String duration) {
            this.Ve = duration;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_TAKE_CAMERA_PICTURE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setEmbedUrl(String embedUrl) {
            this.Vf = embedUrl;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_GALLERY_IMAGE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setEndDate(String endDate) {
            this.Vg = endDate;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_VIBRATE));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setFamilyName(String familyName) {
            this.Vh = familyName;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_SMS));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setGender(String gender) {
            this.Vi = gender;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SEND_MAIL));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setGeo(ItemScope geo) {
            this.Vj = (ic) geo;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_MAKE_CALL));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setGivenName(String givenName) {
            this.Vk = givenName;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_CREATE_CALENDAR_EVENT));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setHeight(String height) {
            this.Vl = height;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_ASYNC_PING));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setId(String id) {
            this.wp = id;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_PLAY_AUDIO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setImage(String image) {
            this.Vm = image;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_MUTE_AUDIO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setInAlbum(ItemScope inAlbum) {
            this.Vn = (ic) inAlbum;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_AUDIO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setLatitude(double latitude) {
            this.NX = latitude;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_AUDIO_VOLUME));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setLocation(ItemScope location) {
            this.Vo = (ic) location;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_AUDIO_VOLUME));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setLongitude(double longitude) {
            this.NY = longitude;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SEEK_AUDIO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setName(String name) {
            this.mName = name;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_AUDIO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setPartOfTVSeries(ItemScope partOfTVSeries) {
            this.Vp = (ic) partOfTVSeries;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_PLAY_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setPerformers(List<ItemScope> performers) {
            this.Vq = performers;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_MUTE_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setPlayerType(String playerType) {
            this.Vr = playerType;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_UNMUTE_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setPostOfficeBoxNumber(String postOfficeBoxNumber) {
            this.Vs = postOfficeBoxNumber;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_IS_VIDEO_MUTED));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setPostalCode(String postalCode) {
            this.Vt = postalCode;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SET_VIDEO_VOLUME));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setRatingValue(String ratingValue) {
            this.Vu = ratingValue;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_VIDEO_VOLUME));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setReviewRating(ItemScope reviewRating) {
            this.Vv = (ic) reviewRating;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SEEK_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setStartDate(String startDate) {
            this.Vw = startDate;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_PAUSE_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setStreetAddress(String streetAddress) {
            this.Vx = streetAddress;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_HIDE_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setText(String text) {
            this.Vy = text;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_SHOW_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setThumbnail(ItemScope thumbnail) {
            this.Vz = (ic) thumbnail;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_CLOSE_VIDEO));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setThumbnailUrl(String thumbnailUrl) {
            this.VA = thumbnailUrl;
            this.UJ.add(Integer.valueOf(ApiEventType.API_MRAID_GET_MIC_INTENSITY));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setTickerSymbol(String tickerSymbol) {
            this.VB = tickerSymbol;
            this.UJ.add(Integer.valueOf(52));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setType(String type) {
            this.Rd = type;
            this.UJ.add(Integer.valueOf(53));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setUrl(String url) {
            this.ro = url;
            this.UJ.add(Integer.valueOf(54));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setWidth(String width) {
            this.VC = width;
            this.UJ.add(Integer.valueOf(55));
            return this;
        }

        public com.google.android.gms.plus.model.moments.ItemScope.Builder setWorstRating(String worstRating) {
            this.VD = worstRating;
            this.UJ.add(Integer.valueOf(56));
            return this;
        }
    }

    ItemScope getAbout();

    List<String> getAdditionalName();

    ItemScope getAddress();

    String getAddressCountry();

    String getAddressLocality();

    String getAddressRegion();

    List<ItemScope> getAssociated_media();

    int getAttendeeCount();

    List<ItemScope> getAttendees();

    ItemScope getAudio();

    List<ItemScope> getAuthor();

    String getBestRating();

    String getBirthDate();

    ItemScope getByArtist();

    String getCaption();

    String getContentSize();

    String getContentUrl();

    List<ItemScope> getContributor();

    String getDateCreated();

    String getDateModified();

    String getDatePublished();

    String getDescription();

    String getDuration();

    String getEmbedUrl();

    String getEndDate();

    String getFamilyName();

    String getGender();

    ItemScope getGeo();

    String getGivenName();

    String getHeight();

    String getId();

    String getImage();

    ItemScope getInAlbum();

    double getLatitude();

    ItemScope getLocation();

    double getLongitude();

    String getName();

    ItemScope getPartOfTVSeries();

    List<ItemScope> getPerformers();

    String getPlayerType();

    String getPostOfficeBoxNumber();

    String getPostalCode();

    String getRatingValue();

    ItemScope getReviewRating();

    String getStartDate();

    String getStreetAddress();

    String getText();

    ItemScope getThumbnail();

    String getThumbnailUrl();

    String getTickerSymbol();

    String getType();

    String getUrl();

    String getWidth();

    String getWorstRating();

    boolean hasAbout();

    boolean hasAdditionalName();

    boolean hasAddress();

    boolean hasAddressCountry();

    boolean hasAddressLocality();

    boolean hasAddressRegion();

    boolean hasAssociated_media();

    boolean hasAttendeeCount();

    boolean hasAttendees();

    boolean hasAudio();

    boolean hasAuthor();

    boolean hasBestRating();

    boolean hasBirthDate();

    boolean hasByArtist();

    boolean hasCaption();

    boolean hasContentSize();

    boolean hasContentUrl();

    boolean hasContributor();

    boolean hasDateCreated();

    boolean hasDateModified();

    boolean hasDatePublished();

    boolean hasDescription();

    boolean hasDuration();

    boolean hasEmbedUrl();

    boolean hasEndDate();

    boolean hasFamilyName();

    boolean hasGender();

    boolean hasGeo();

    boolean hasGivenName();

    boolean hasHeight();

    boolean hasId();

    boolean hasImage();

    boolean hasInAlbum();

    boolean hasLatitude();

    boolean hasLocation();

    boolean hasLongitude();

    boolean hasName();

    boolean hasPartOfTVSeries();

    boolean hasPerformers();

    boolean hasPlayerType();

    boolean hasPostOfficeBoxNumber();

    boolean hasPostalCode();

    boolean hasRatingValue();

    boolean hasReviewRating();

    boolean hasStartDate();

    boolean hasStreetAddress();

    boolean hasText();

    boolean hasThumbnail();

    boolean hasThumbnailUrl();

    boolean hasTickerSymbol();

    boolean hasType();

    boolean hasUrl();

    boolean hasWidth();

    boolean hasWorstRating();
}