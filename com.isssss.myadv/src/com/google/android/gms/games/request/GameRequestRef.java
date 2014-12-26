package com.google.android.gms.games.request;

import android.os.Parcel;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameRef;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerRef;
import com.inmobi.androidsdk.IMBrowserActivity;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import java.util.ArrayList;
import java.util.List;

public final class GameRequestRef extends b implements GameRequest {
    private final int LE;

    public GameRequestRef(DataHolder holder, int dataRow, int numChildren) {
        super(holder, dataRow);
        this.LE = numChildren;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return GameRequestEntity.a(this, obj);
    }

    public GameRequest freeze() {
        return new GameRequestEntity(this);
    }

    public long getCreationTimestamp() {
        return getLong("creation_timestamp");
    }

    public byte[] getData() {
        return getByteArray(IMBrowserActivity.EXPANDDATA);
    }

    public long getExpirationTimestamp() {
        return getLong("expiration_timestamp");
    }

    public Game getGame() {
        return new GameRef(this.BB, this.BD);
    }

    public int getRecipientStatus(String playerId) {
        int i = this.BD;
        while (i < this.BD + this.LE) {
            int G = this.BB.G(i);
            if (this.BB.getString("recipient_external_player_id", i, G).equals(playerId)) {
                return this.BB.getInteger("recipient_status", i, G);
            }
            i++;
        }
        return -1;
    }

    public List<Player> getRecipients() {
        List arrayList = new ArrayList(this.LE);
        int i = 0;
        while (i < this.LE) {
            arrayList.add(new PlayerRef(this.BB, this.BD + i, "recipient_"));
            i++;
        }
        return arrayList;
    }

    public String getRequestId() {
        return getString("external_request_id");
    }

    public Player getSender() {
        return new PlayerRef(this.BB, this.BD, "sender_");
    }

    public int getStatus() {
        return getInteger("status");
    }

    public int getType() {
        return getInteger(AnalyticsSQLiteHelper.EVENT_LIST_TYPE);
    }

    public int hashCode() {
        return GameRequestEntity.a(this);
    }

    public boolean isConsumed(String playerId) {
        return getRecipientStatus(playerId) == 1;
    }

    public String toString() {
        return GameRequestEntity.c(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ((GameRequestEntity) freeze()).writeToParcel(dest, flags);
    }
}