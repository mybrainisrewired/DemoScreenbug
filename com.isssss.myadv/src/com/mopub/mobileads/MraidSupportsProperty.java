package com.mopub.mobileads;

// compiled from: MraidProperty.java
class MraidSupportsProperty extends MraidProperty {
    private boolean calendar;
    private boolean inlineVideo;
    private boolean sms;
    private boolean storePicture;
    private boolean tel;

    MraidSupportsProperty() {
    }

    public String toJsonPair() {
        return new StringBuilder("supports: {sms: ").append(String.valueOf(this.sms)).append(", ").append("tel: ").append(String.valueOf(this.tel)).append(", ").append("calendar: ").append(String.valueOf(this.calendar)).append(", ").append("storePicture: ").append(String.valueOf(this.storePicture)).append(", ").append("inlineVideo: ").append(String.valueOf(this.inlineVideo)).append("}").toString();
    }

    public MraidSupportsProperty withCalendar(boolean value) {
        this.calendar = value;
        return this;
    }

    public MraidSupportsProperty withInlineVideo(boolean value) {
        this.inlineVideo = value;
        return this;
    }

    public MraidSupportsProperty withSms(boolean value) {
        this.sms = value;
        return this;
    }

    public MraidSupportsProperty withStorePicture(boolean value) {
        this.storePicture = value;
        return this;
    }

    public MraidSupportsProperty withTel(boolean value) {
        this.tel = value;
        return this;
    }
}