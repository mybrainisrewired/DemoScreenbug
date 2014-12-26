package com.clouds.object;

import java.io.Serializable;

public class NotificationInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String link;
    private String text;
    private String title;

    public NotificationInfo(String title, String text, String link) {
        this.title = title;
        this.text = text;
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }

    public String getText() {
        return this.text;
    }

    public String getTitle() {
        return this.title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return new StringBuilder("NotificationInfo [title=").append(this.title).append(", text=").append(this.text).append(", link=").append(this.link).append("]").toString();
    }
}