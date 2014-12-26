package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.gm;
import java.util.ArrayList;

public final class LeaderboardEntity implements Leaderboard {
    private final String HA;
    private final Uri HF;
    private final String HQ;
    private final String LP;
    private final int LQ;
    private final ArrayList<LeaderboardVariantEntity> LR;
    private final Game LS;

    public LeaderboardEntity(Leaderboard leaderboard) {
        this.LP = leaderboard.getLeaderboardId();
        this.HA = leaderboard.getDisplayName();
        this.HF = leaderboard.getIconImageUri();
        this.HQ = leaderboard.getIconImageUrl();
        this.LQ = leaderboard.getScoreOrder();
        Game game = leaderboard.getGame();
        this.LS = game == null ? null : new GameEntity(game);
        ArrayList variants = leaderboard.getVariants();
        int size = variants.size();
        this.LR = new ArrayList(size);
        int i = 0;
        while (i < size) {
            this.LR.add((LeaderboardVariantEntity) ((LeaderboardVariant) variants.get(i)).freeze());
            i++;
        }
    }

    static int a(Leaderboard leaderboard) {
        return fo.hashCode(new Object[]{leaderboard.getLeaderboardId(), leaderboard.getDisplayName(), leaderboard.getIconImageUri(), Integer.valueOf(leaderboard.getScoreOrder()), leaderboard.getVariants()});
    }

    static boolean a(Leaderboard leaderboard, Leaderboard leaderboard2) {
        if (!(leaderboard2 instanceof Leaderboard)) {
            return false;
        }
        if (leaderboard == leaderboard2) {
            return true;
        }
        leaderboard2 = leaderboard2;
        return fo.equal(leaderboard2.getLeaderboardId(), leaderboard.getLeaderboardId()) && fo.equal(leaderboard2.getDisplayName(), leaderboard.getDisplayName()) && fo.equal(leaderboard2.getIconImageUri(), leaderboard.getIconImageUri()) && fo.equal(Integer.valueOf(leaderboard2.getScoreOrder()), Integer.valueOf(leaderboard.getScoreOrder())) && fo.equal(leaderboard2.getVariants(), leaderboard.getVariants());
    }

    static String b(Leaderboard leaderboard) {
        return fo.e(leaderboard).a("LeaderboardId", leaderboard.getLeaderboardId()).a("DisplayName", leaderboard.getDisplayName()).a("IconImageUri", leaderboard.getIconImageUri()).a("IconImageUrl", leaderboard.getIconImageUrl()).a("ScoreOrder", Integer.valueOf(leaderboard.getScoreOrder())).a("Variants", leaderboard.getVariants()).toString();
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hC();
    }

    public String getDisplayName() {
        return this.HA;
    }

    public void getDisplayName(CharArrayBuffer dataOut) {
        gm.b(this.HA, dataOut);
    }

    public Game getGame() {
        return this.LS;
    }

    public Uri getIconImageUri() {
        return this.HF;
    }

    public String getIconImageUrl() {
        return this.HQ;
    }

    public String getLeaderboardId() {
        return this.LP;
    }

    public int getScoreOrder() {
        return this.LQ;
    }

    public ArrayList<LeaderboardVariant> getVariants() {
        return new ArrayList(this.LR);
    }

    public Leaderboard hC() {
        return this;
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
}