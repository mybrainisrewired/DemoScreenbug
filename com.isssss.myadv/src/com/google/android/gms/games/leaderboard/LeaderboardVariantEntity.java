package com.google.android.gms.games.leaderboard;

import com.google.android.gms.games.internal.constants.LeaderboardCollection;
import com.google.android.gms.games.internal.constants.TimeSpan;
import com.google.android.gms.internal.fo;

public final class LeaderboardVariantEntity implements LeaderboardVariant {
    private final int Mh;
    private final int Mi;
    private final boolean Mj;
    private final long Mk;
    private final String Ml;
    private final long Mm;
    private final String Mn;
    private final String Mo;
    private final long Mp;
    private final String Mq;
    private final String Mr;
    private final String Ms;

    public LeaderboardVariantEntity(LeaderboardVariant variant) {
        this.Mh = variant.getTimeSpan();
        this.Mi = variant.getCollection();
        this.Mj = variant.hasPlayerInfo();
        this.Mk = variant.getRawPlayerScore();
        this.Ml = variant.getDisplayPlayerScore();
        this.Mm = variant.getPlayerRank();
        this.Mn = variant.getDisplayPlayerRank();
        this.Mo = variant.getPlayerScoreTag();
        this.Mp = variant.getNumScores();
        this.Mq = variant.hG();
        this.Mr = variant.hH();
        this.Ms = variant.hI();
    }

    static int a(LeaderboardVariant leaderboardVariant) {
        return fo.hashCode(new Object[]{Integer.valueOf(leaderboardVariant.getTimeSpan()), Integer.valueOf(leaderboardVariant.getCollection()), Boolean.valueOf(leaderboardVariant.hasPlayerInfo()), Long.valueOf(leaderboardVariant.getRawPlayerScore()), leaderboardVariant.getDisplayPlayerScore(), Long.valueOf(leaderboardVariant.getPlayerRank()), leaderboardVariant.getDisplayPlayerRank(), Long.valueOf(leaderboardVariant.getNumScores()), leaderboardVariant.hG(), leaderboardVariant.hI(), leaderboardVariant.hH()});
    }

    static boolean a(LeaderboardVariant leaderboardVariant, LeaderboardVariant leaderboardVariant2) {
        if (!(leaderboardVariant2 instanceof LeaderboardVariant)) {
            return false;
        }
        if (leaderboardVariant == leaderboardVariant2) {
            return true;
        }
        leaderboardVariant2 = leaderboardVariant2;
        return fo.equal(Integer.valueOf(leaderboardVariant2.getTimeSpan()), Integer.valueOf(leaderboardVariant.getTimeSpan())) && fo.equal(Integer.valueOf(leaderboardVariant2.getCollection()), Integer.valueOf(leaderboardVariant.getCollection())) && fo.equal(Boolean.valueOf(leaderboardVariant2.hasPlayerInfo()), Boolean.valueOf(leaderboardVariant.hasPlayerInfo())) && fo.equal(Long.valueOf(leaderboardVariant2.getRawPlayerScore()), Long.valueOf(leaderboardVariant.getRawPlayerScore())) && fo.equal(leaderboardVariant2.getDisplayPlayerScore(), leaderboardVariant.getDisplayPlayerScore()) && fo.equal(Long.valueOf(leaderboardVariant2.getPlayerRank()), Long.valueOf(leaderboardVariant.getPlayerRank())) && fo.equal(leaderboardVariant2.getDisplayPlayerRank(), leaderboardVariant.getDisplayPlayerRank()) && fo.equal(Long.valueOf(leaderboardVariant2.getNumScores()), Long.valueOf(leaderboardVariant.getNumScores())) && fo.equal(leaderboardVariant2.hG(), leaderboardVariant.hG()) && fo.equal(leaderboardVariant2.hI(), leaderboardVariant.hI()) && fo.equal(leaderboardVariant2.hH(), leaderboardVariant.hH());
    }

    static String b(LeaderboardVariant leaderboardVariant) {
        return fo.e(leaderboardVariant).a("TimeSpan", TimeSpan.bd(leaderboardVariant.getTimeSpan())).a("Collection", LeaderboardCollection.bd(leaderboardVariant.getCollection())).a("RawPlayerScore", leaderboardVariant.hasPlayerInfo() ? Long.valueOf(leaderboardVariant.getRawPlayerScore()) : "none").a("DisplayPlayerScore", leaderboardVariant.hasPlayerInfo() ? leaderboardVariant.getDisplayPlayerScore() : "none").a("PlayerRank", leaderboardVariant.hasPlayerInfo() ? Long.valueOf(leaderboardVariant.getPlayerRank()) : "none").a("DisplayPlayerRank", leaderboardVariant.hasPlayerInfo() ? leaderboardVariant.getDisplayPlayerRank() : "none").a("NumScores", Long.valueOf(leaderboardVariant.getNumScores())).a("TopPageNextToken", leaderboardVariant.hG()).a("WindowPageNextToken", leaderboardVariant.hI()).a("WindowPagePrevToken", leaderboardVariant.hH()).toString();
    }

    public boolean equals(Object obj) {
        return a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return hJ();
    }

    public int getCollection() {
        return this.Mi;
    }

    public String getDisplayPlayerRank() {
        return this.Mn;
    }

    public String getDisplayPlayerScore() {
        return this.Ml;
    }

    public long getNumScores() {
        return this.Mp;
    }

    public long getPlayerRank() {
        return this.Mm;
    }

    public String getPlayerScoreTag() {
        return this.Mo;
    }

    public long getRawPlayerScore() {
        return this.Mk;
    }

    public int getTimeSpan() {
        return this.Mh;
    }

    public String hG() {
        return this.Mq;
    }

    public String hH() {
        return this.Mr;
    }

    public String hI() {
        return this.Ms;
    }

    public LeaderboardVariant hJ() {
        return this;
    }

    public boolean hasPlayerInfo() {
        return this.Mj;
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