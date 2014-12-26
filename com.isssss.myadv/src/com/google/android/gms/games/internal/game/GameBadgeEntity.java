package com.google.android.gms.games.internal.game;

import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;
import com.google.android.gms.internal.fo;

public final class GameBadgeEntity extends GamesDowngradeableSafeParcel implements GameBadge {
    public static final GameBadgeEntityCreator CREATOR;
    private String EB;
    private String HD;
    private Uri HF;
    private int LF;
    private final int xH;

    static final class GameBadgeEntityCreatorCompat extends GameBadgeEntityCreator {
        GameBadgeEntityCreatorCompat() {
        }

        public GameBadgeEntity ar(Parcel parcel) {
            if (GameBadgeEntity.c(GameBadgeEntity.eJ()) || GameBadgeEntity.al(GameBadgeEntity.class.getCanonicalName())) {
                return super.ar(parcel);
            }
            int readInt = parcel.readInt();
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            return new GameBadgeEntity(1, readInt, readString, readString2, readString3 == null ? null : Uri.parse(readString3));
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return ar(x0);
        }
    }

    static {
        CREATOR = new GameBadgeEntityCreatorCompat();
    }

    GameBadgeEntity(int versionCode, int type, String title, String description, Uri iconImageUri) {
        this.xH = versionCode;
        this.LF = type;
        this.EB = title;
        this.HD = description;
        this.HF = iconImageUri;
    }

    public GameBadgeEntity(GameBadge gameBadge) {
        this.xH = 1;
        this.LF = gameBadge.getType();
        this.EB = gameBadge.getTitle();
        this.HD = gameBadge.getDescription();
        this.HF = gameBadge.getIconImageUri();
    }

    static int a(GameBadge gameBadge) {
        return fo.hashCode(new Object[]{Integer.valueOf(gameBadge.getType()), gameBadge.getTitle(), gameBadge.getDescription(), gameBadge.getIconImageUri()});
    }

    static boolean a(GameBadge gameBadge, GameBadge gameBadge2) {
        if (!(gameBadge2 instanceof GameBadge)) {
            return false;
        }
        if (gameBadge == gameBadge2) {
            return true;
        }
        gameBadge2 = gameBadge2;
        return fo.equal(Integer.valueOf(gameBadge2.getType()), gameBadge.getTitle()) && fo.equal(gameBadge2.getDescription(), gameBadge.getIconImageUri());
    }

    static String b(GameBadge gameBadge) {
        return fo.e(gameBadge).a("Type", Integer.valueOf(gameBadge.getType())).a("Title", gameBadge.getTitle()).a("Description", gameBadge.getDescription()).a("IconImageUri", gameBadge.getIconImageUri()).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hh();
    }

    public String getDescription() {
        return this.HD;
    }

    public Uri getIconImageUri() {
        return this.HF;
    }

    public String getTitle() {
        return this.EB;
    }

    public int getType() {
        return this.LF;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int hashCode() {
        return a(this);
    }

    public GameBadge hh() {
        return this;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (eK()) {
            dest.writeInt(this.LF);
            dest.writeString(this.EB);
            dest.writeString(this.HD);
            dest.writeString(this.HF == null ? null : this.HF.toString());
        } else {
            GameBadgeEntityCreator.a(this, dest, flags);
        }
    }
}