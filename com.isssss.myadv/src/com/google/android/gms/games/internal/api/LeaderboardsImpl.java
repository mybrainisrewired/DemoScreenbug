package com.google.android.gms.games.internal.api;

import android.content.Intent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LeaderboardMetadataResult;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadPlayerScoreResult;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.google.android.gms.games.leaderboard.Leaderboards.SubmitScoreResult;
import com.google.android.gms.games.leaderboard.ScoreSubmissionData;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class LeaderboardsImpl implements Leaderboards {

    private static abstract class LoadMetadataImpl extends BaseGamesApiMethodImpl<LeaderboardMetadataResult> {

        class AnonymousClass_1 implements LeaderboardMetadataResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public LeaderboardBuffer getLeaderboards() {
                return new LeaderboardBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadMetadataImpl() {
        }

        public LeaderboardMetadataResult D(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return D(status);
        }
    }

    private static abstract class LoadPlayerScoreImpl extends BaseGamesApiMethodImpl<LoadPlayerScoreResult> {

        class AnonymousClass_1 implements LoadPlayerScoreResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public LeaderboardScore getScore() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private LoadPlayerScoreImpl() {
        }

        public LoadPlayerScoreResult E(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return E(status);
        }
    }

    private static abstract class LoadScoresImpl extends BaseGamesApiMethodImpl<LoadScoresResult> {

        class AnonymousClass_1 implements LoadScoresResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Leaderboard getLeaderboard() {
                return null;
            }

            public LeaderboardScoreBuffer getScores() {
                return new LeaderboardScoreBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadScoresImpl() {
        }

        public LoadScoresResult F(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return F(status);
        }
    }

    protected static abstract class SubmitScoreImpl extends BaseGamesApiMethodImpl<SubmitScoreResult> {

        class AnonymousClass_1 implements SubmitScoreResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public ScoreSubmissionData getScoreData() {
                return new ScoreSubmissionData(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        protected SubmitScoreImpl() {
        }

        public SubmitScoreResult G(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return G(status);
        }
    }

    class AnonymousClass_10 extends LoadScoresImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;
        final /* synthetic */ String Kp;
        final /* synthetic */ int Kq;
        final /* synthetic */ int Kr;
        final /* synthetic */ int Ks;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.JT, this.Kp, this.Kq, this.Kr, this.Ks, this.JQ);
        }
    }

    class AnonymousClass_11 extends LoadScoresImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;
        final /* synthetic */ String Kp;
        final /* synthetic */ int Kq;
        final /* synthetic */ int Kr;
        final /* synthetic */ int Ks;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JT, this.Kp, this.Kq, this.Kr, this.Ks, this.JQ);
        }
    }

    class AnonymousClass_1 extends LoadMetadataImpl {
        final /* synthetic */ boolean JQ;

        AnonymousClass_1(boolean z) {
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JQ);
        }
    }

    class AnonymousClass_2 extends LoadMetadataImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String Kp;

        AnonymousClass_2(String str, boolean z) {
            this.Kp = str;
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Kp, this.JQ);
        }
    }

    class AnonymousClass_3 extends LoadPlayerScoreImpl {
        final /* synthetic */ String Kp;
        final /* synthetic */ int Kq;
        final /* synthetic */ int Kr;

        AnonymousClass_3(String str, int i, int i2) {
            this.Kp = str;
            this.Kq = i;
            this.Kr = i2;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, null, this.Kp, this.Kq, this.Kr);
        }
    }

    class AnonymousClass_4 extends LoadScoresImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String Kp;
        final /* synthetic */ int Kq;
        final /* synthetic */ int Kr;
        final /* synthetic */ int Ks;

        AnonymousClass_4(String str, int i, int i2, int i3, boolean z) {
            this.Kp = str;
            this.Kq = i;
            this.Kr = i2;
            this.Ks = i3;
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Kp, this.Kq, this.Kr, this.Ks, this.JQ);
        }
    }

    class AnonymousClass_5 extends LoadScoresImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String Kp;
        final /* synthetic */ int Kq;
        final /* synthetic */ int Kr;
        final /* synthetic */ int Ks;

        AnonymousClass_5(String str, int i, int i2, int i3, boolean z) {
            this.Kp = str;
            this.Kq = i;
            this.Kr = i2;
            this.Ks = i3;
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.Kp, this.Kq, this.Kr, this.Ks, this.JQ);
        }
    }

    class AnonymousClass_6 extends LoadScoresImpl {
        final /* synthetic */ int Ks;
        final /* synthetic */ LeaderboardScoreBuffer Kt;
        final /* synthetic */ int Ku;

        AnonymousClass_6(LeaderboardScoreBuffer leaderboardScoreBuffer, int i, int i2) {
            this.Kt = leaderboardScoreBuffer;
            this.Ks = i;
            this.Ku = i2;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Kt, this.Ks, this.Ku);
        }
    }

    class AnonymousClass_7 extends SubmitScoreImpl {
        final /* synthetic */ String Kp;
        final /* synthetic */ long Kv;
        final /* synthetic */ String Kw;

        AnonymousClass_7(String str, long j, String str2) {
            this.Kp = str;
            this.Kv = j;
            this.Kw = str2;
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Kp, this.Kv, this.Kw);
        }
    }

    class AnonymousClass_8 extends LoadMetadataImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JT, this.JQ);
        }
    }

    class AnonymousClass_9 extends LoadMetadataImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;
        final /* synthetic */ String Kp;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JT, this.Kp, this.JQ);
        }
    }

    public Intent getAllLeaderboardsIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gp();
    }

    public Intent getLeaderboardIntent(GoogleApiClient apiClient, String leaderboardId) {
        return Games.c(apiClient).aA(leaderboardId);
    }

    public PendingResult<LoadPlayerScoreResult> loadCurrentPlayerLeaderboardScore(GoogleApiClient apiClient, String leaderboardId, int span, int leaderboardCollection) {
        return apiClient.a(new AnonymousClass_3(leaderboardId, span, leaderboardCollection));
    }

    public PendingResult<LeaderboardMetadataResult> loadLeaderboardMetadata(GoogleApiClient apiClient, String leaderboardId, boolean forceReload) {
        return apiClient.a(new AnonymousClass_2(leaderboardId, forceReload));
    }

    public PendingResult<LeaderboardMetadataResult> loadLeaderboardMetadata(GoogleApiClient apiClient, boolean forceReload) {
        return apiClient.a(new AnonymousClass_1(forceReload));
    }

    public PendingResult<LoadScoresResult> loadMoreScores(GoogleApiClient apiClient, LeaderboardScoreBuffer buffer, int maxResults, int pageDirection) {
        return apiClient.a(new AnonymousClass_6(buffer, maxResults, pageDirection));
    }

    public PendingResult<LoadScoresResult> loadPlayerCenteredScores(GoogleApiClient apiClient, String leaderboardId, int span, int leaderboardCollection, int maxResults) {
        return loadPlayerCenteredScores(apiClient, leaderboardId, span, leaderboardCollection, maxResults, false);
    }

    public PendingResult<LoadScoresResult> loadPlayerCenteredScores(GoogleApiClient apiClient, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        return apiClient.a(new AnonymousClass_5(leaderboardId, span, leaderboardCollection, maxResults, forceReload));
    }

    public PendingResult<LoadScoresResult> loadTopScores(GoogleApiClient apiClient, String leaderboardId, int span, int leaderboardCollection, int maxResults) {
        return loadTopScores(apiClient, leaderboardId, span, leaderboardCollection, maxResults, false);
    }

    public PendingResult<LoadScoresResult> loadTopScores(GoogleApiClient apiClient, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        return apiClient.a(new AnonymousClass_4(leaderboardId, span, leaderboardCollection, maxResults, forceReload));
    }

    public void submitScore(GoogleApiClient apiClient, String leaderboardId, long score) {
        submitScore(apiClient, leaderboardId, score, null);
    }

    public void submitScore(GoogleApiClient apiClient, String leaderboardId, long score, String scoreTag) {
        Games.c(apiClient).a(null, leaderboardId, score, scoreTag);
    }

    public PendingResult<SubmitScoreResult> submitScoreImmediate(GoogleApiClient apiClient, String leaderboardId, long score) {
        return submitScoreImmediate(apiClient, leaderboardId, score, null);
    }

    public PendingResult<SubmitScoreResult> submitScoreImmediate(GoogleApiClient apiClient, String leaderboardId, long score, String scoreTag) {
        return apiClient.b(new AnonymousClass_7(leaderboardId, score, scoreTag));
    }
}