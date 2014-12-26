package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameRef;
import java.util.ArrayList;

public final class LeaderboardRef extends b implements Leaderboard {
    private final int LE;
    private final Game LS;

    LeaderboardRef(DataHolder holder, int dataRow, int numChildren) {
        super(holder, dataRow);
        this.LE = numChildren;
        this.LS = new GameRef(holder, dataRow);
    }

    public boolean equals(Object obj) {
        return LeaderboardEntity.a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hC();
    }

    public String getDisplayName() {
        return getString("name");
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        a("name", dataOut);
    }

    public Game getGame() {
        return this.LS;
    }

    public Uri getIconImageUri() {
        return ah("board_icon_image_uri");
    }

    public String getIconImageUrl() {
        return getString("board_icon_image_url");
    }

    public String getLeaderboardId() {
        return getString("external_leaderboard_id");
    }

    public int getScoreOrder() {
        return getInteger("score_order");
    }

    public ArrayList<LeaderboardVariant> getVariants() {
        ArrayList<LeaderboardVariant> arrayList = new ArrayList(this.LE);
        int i = 0;
        while (i < this.LE) {
            arrayList.add(new LeaderboardVariantRef(this.BB, this.BD + i));
            i++;
        }
        return arrayList;
    }

    public Leaderboard hC() {
        return new LeaderboardEntity(this);
    }

    public int hashCode() {
        return LeaderboardEntity.a(this);
    }

    public String toString() {
        return LeaderboardEntity.b(this);
    }
}