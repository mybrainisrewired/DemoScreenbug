package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.internal.GamesDowngradeableSafeParcel;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.gm;

public final class PlayerEntity extends GamesDowngradeableSafeParcel implements Player {
    public static final Creator<PlayerEntity> CREATOR;
    private final String HA;
    private final Uri HF;
    private final Uri HG;
    private final String HQ;
    private final String HR;
    private final String Ie;
    private final long If;
    private final int Ig;
    private final long Ih;
    private final int xH;

    static final class PlayerEntityCreatorCompat extends PlayerEntityCreator {
        PlayerEntityCreatorCompat() {
        }

        public PlayerEntity ao(Parcel parcel) {
            if (PlayerEntity.c(PlayerEntity.eJ()) || PlayerEntity.al(PlayerEntity.class.getCanonicalName())) {
                return super.ao(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            return new PlayerEntity(4, readString, readString2, readString3 == null ? null : Uri.parse(readString3), readString4 == null ? null : Uri.parse(readString4), parcel.readLong(), -1, -1, null, null);
        }

        public /* synthetic */ Object createFromParcel(Parcel x0) {
            return ao(x0);
        }
    }

    static {
        CREATOR = new PlayerEntityCreatorCompat();
    }

    PlayerEntity(int versionCode, String playerId, String displayName, Uri iconImageUri, Uri hiResImageUri, long retrievedTimestamp, int isInCircles, long lastPlayedWithTimestamp, String iconImageUrl, String hiResImageUrl) {
        this.xH = versionCode;
        this.Ie = playerId;
        this.HA = displayName;
        this.HF = iconImageUri;
        this.HQ = iconImageUrl;
        this.HG = hiResImageUri;
        this.HR = hiResImageUrl;
        this.If = retrievedTimestamp;
        this.Ig = isInCircles;
        this.Ih = lastPlayedWithTimestamp;
    }

    public PlayerEntity(Player player) {
        this.xH = 4;
        this.Ie = player.getPlayerId();
        this.HA = player.getDisplayName();
        this.HF = player.getIconImageUri();
        this.HQ = player.getIconImageUrl();
        this.HG = player.getHiResImageUri();
        this.HR = player.getHiResImageUrl();
        this.If = player.getRetrievedTimestamp();
        this.Ig = player.gh();
        this.Ih = player.getLastPlayedWithTimestamp();
        fb.d(this.Ie);
        fb.d(this.HA);
        fb.x(this.If > 0);
    }

    static int a(Player player) {
        return fo.hashCode(new Object[]{player.getPlayerId(), player.getDisplayName(), player.getIconImageUri(), player.getHiResImageUri(), Long.valueOf(player.getRetrievedTimestamp())});
    }

    static boolean a(Player player, Player player2) {
        if (!(player2 instanceof Player)) {
            return false;
        }
        if (player == player2) {
            return true;
        }
        player2 = player2;
        return fo.equal(player2.getPlayerId(), player.getPlayerId()) && fo.equal(player2.getDisplayName(), player.getDisplayName()) && fo.equal(player2.getIconImageUri(), player.getIconImageUri()) && fo.equal(player2.getHiResImageUri(), player.getHiResImageUri()) && fo.equal(Long.valueOf(player2.getRetrievedTimestamp()), Long.valueOf(player.getRetrievedTimestamp()));
    }

    static String b(Player player) {
        return fo.e(player).a("PlayerId", player.getPlayerId()).a("DisplayName", player.getDisplayName()).a("IconImageUri", player.getIconImageUri()).a("IconImageUrl", player.getIconImageUrl()).a("HiResImageUri", player.getHiResImageUri()).a("HiResImageUrl", player.getHiResImageUrl()).a("RetrievedTimestamp", Long.valueOf(player.getRetrievedTimestamp())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public Player freeze() {
        return this;
    }

    public String getDisplayName() {
        return this.HA;
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        gm.b(this.HA, dataOut);
    }

    public Uri getHiResImageUri() {
        return this.HG;
    }

    public String getHiResImageUrl() {
        return this.HR;
    }

    public Uri getIconImageUri() {
        return this.HF;
    }

    public String getIconImageUrl() {
        return this.HQ;
    }

    public long getLastPlayedWithTimestamp() {
        return this.Ih;
    }

    public String getPlayerId() {
        return this.Ie;
    }

    public long getRetrievedTimestamp() {
        return this.If;
    }

    public int getVersionCode() {
        return this.xH;
    }

    public int gh() {
        return this.Ig;
    }

    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    public boolean hasIconImage() {
        return getIconImageUri() != null;
    }

    public int hashCode() {
        return a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return b(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        String str = null;
        if (eK()) {
            dest.writeString(this.Ie);
            dest.writeString(this.HA);
            dest.writeString(this.HF == null ? null : this.HF.toString());
            if (this.HG != null) {
                str = this.HG.toString();
            }
            dest.writeString(str);
            dest.writeLong(this.If);
        } else {
            PlayerEntityCreator.a(this, dest, flags);
        }
    }
}