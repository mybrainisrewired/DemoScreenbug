package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.internal.constants.TimeSpan;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fo.a;
import com.google.android.gms.internal.fq;
import java.util.HashMap;

public final class ScoreSubmissionData {
    private static final String[] LN;
    private int Ah;
    private String Ie;
    private String LP;
    private HashMap<Integer, Result> Mt;

    public static final class Result {
        public final String formattedScore;
        public final boolean newBest;
        public final long rawScore;
        public final String scoreTag;

        public Result(long rawScore, String formattedScore, String scoreTag, boolean newBest) {
            this.rawScore = rawScore;
            this.formattedScore = formattedScore;
            this.scoreTag = scoreTag;
            this.newBest = newBest;
        }

        public String toString() {
            return fo.e(this).a("RawScore", Long.valueOf(this.rawScore)).a("FormattedScore", this.formattedScore).a("ScoreTag", this.scoreTag).a("NewBest", Boolean.valueOf(this.newBest)).toString();
        }
    }

    static {
        LN = new String[]{"leaderboardId", "playerId", "timeSpan", "hasResult", "rawScore", "formattedScore", "newBest", "scoreTag"};
    }

    public ScoreSubmissionData(DataHolder dataHolder) {
        this.Ah = dataHolder.getStatusCode();
        this.Mt = new HashMap();
        int count = dataHolder.getCount();
        fq.z(count == 3);
        int i = 0;
        while (i < count) {
            int G = dataHolder.G(i);
            if (i == 0) {
                this.LP = dataHolder.getString("leaderboardId", i, G);
                this.Ie = dataHolder.getString("playerId", i, G);
            }
            if (dataHolder.getBoolean("hasResult", i, G)) {
                a(new Result(dataHolder.getLong("rawScore", i, G), dataHolder.getString("formattedScore", i, G), dataHolder.getString("scoreTag", i, G), dataHolder.getBoolean("newBest", i, G)), dataHolder.getInteger("timeSpan", i, G));
            }
            i++;
        }
    }

    private void a(Result result, int i) {
        this.Mt.put(Integer.valueOf(i), result);
    }

    public String getLeaderboardId() {
        return this.LP;
    }

    public String getPlayerId() {
        return this.Ie;
    }

    public Result getScoreResult(int timeSpan) {
        return (Result) this.Mt.get(Integer.valueOf(timeSpan));
    }

    public String toString() {
        a a = fo.e(this).a("PlayerId", this.Ie).a("StatusCode", Integer.valueOf(this.Ah));
        int i = 0;
        while (i < 3) {
            Result result = (Result) this.Mt.get(Integer.valueOf(i));
            a.a("TimesSpan", TimeSpan.bd(i));
            a.a("Result", result == null ? "null" : result.toString());
            i++;
        }
        return a.toString();
    }
}