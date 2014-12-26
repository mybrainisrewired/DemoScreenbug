package com.google.android.gms.games.internal.game;

import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;

public final class GameBadgeRef extends b implements GameBadge {
    GameBadgeRef(DataHolder holder, int dataRow) {
        super(holder, dataRow);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return GameBadgeEntity.a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hh();
    }

    public String getDescription() {
        return getString("badge_description");
    }

    public Uri getIconImageUri() {
        return ah("badge_icon_image_uri");
    }

    public String getTitle() {
        return getString("badge_title");
    }

    public int getType() {
        return getInteger("badge_type");
    }

    public int hashCode() {
        return GameBadgeEntity.a(this);
    }

    public GameBadge hh() {
        return new GameBadgeEntity(this);
    }

    public String toString() {
        return GameBadgeEntity.b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((GameBadgeEntity) hh()).writeToParcel(dest, flags);
    }
}