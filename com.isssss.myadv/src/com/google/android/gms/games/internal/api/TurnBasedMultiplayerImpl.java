package com.google.android.gms.games.internal.api;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.multiplayer.ParticipantResult;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.CancelMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LeaveMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchesResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import java.util.List;

public final class TurnBasedMultiplayerImpl implements TurnBasedMultiplayer {

    private static abstract class CancelMatchImpl extends BaseGamesApiMethodImpl<CancelMatchResult> {
        private final String wp;

        class AnonymousClass_1 implements CancelMatchResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public String getMatchId() {
                return CancelMatchImpl.this.wp;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        public CancelMatchImpl(String id) {
            this.wp = id;
        }

        public CancelMatchResult R(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return R(status);
        }
    }

    private static abstract class InitiateMatchImpl extends BaseGamesApiMethodImpl<InitiateMatchResult> {

        class AnonymousClass_1 implements InitiateMatchResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public TurnBasedMatch getMatch() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private InitiateMatchImpl() {
        }

        public InitiateMatchResult S(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return S(status);
        }
    }

    private static abstract class LeaveMatchImpl extends BaseGamesApiMethodImpl<LeaveMatchResult> {

        class AnonymousClass_1 implements LeaveMatchResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public TurnBasedMatch getMatch() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private LeaveMatchImpl() {
        }

        public LeaveMatchResult T(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return T(status);
        }
    }

    private static abstract class LoadMatchImpl extends BaseGamesApiMethodImpl<LoadMatchResult> {

        class AnonymousClass_1 implements LoadMatchResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public TurnBasedMatch getMatch() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private LoadMatchImpl() {
        }

        public LoadMatchResult U(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return U(status);
        }
    }

    private static abstract class LoadMatchesImpl extends BaseGamesApiMethodImpl<LoadMatchesResult> {

        class AnonymousClass_1 implements LoadMatchesResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public LoadMatchesResponse getMatches() {
                return new LoadMatchesResponse(new Bundle());
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadMatchesImpl() {
        }

        public LoadMatchesResult V(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return V(status);
        }
    }

    private static abstract class UpdateMatchImpl extends BaseGamesApiMethodImpl<UpdateMatchResult> {

        class AnonymousClass_1 implements UpdateMatchResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public TurnBasedMatch getMatch() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private UpdateMatchImpl() {
        }

        public UpdateMatchResult W(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return W(status);
        }
    }

    class AnonymousClass_10 extends LoadMatchImpl {
        final /* synthetic */ String Ld;

        AnonymousClass_10(String str) {
            this.Ld = str;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.h(this, this.Ld);
        }
    }

    class AnonymousClass_11 extends InitiateMatchImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ String Ld;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JT, this.Ld);
        }
    }

    class AnonymousClass_12 extends InitiateMatchImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ String Ld;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.JT, this.Ld);
        }
    }

    class AnonymousClass_13 extends LoadMatchesImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ int Le;
        final /* synthetic */ int[] Lf;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JT, this.Le, this.Lf);
        }
    }

    class AnonymousClass_1 extends InitiateMatchImpl {
        final /* synthetic */ TurnBasedMatchConfig Lb;

        AnonymousClass_1(TurnBasedMatchConfig turnBasedMatchConfig) {
            this.Lb = turnBasedMatchConfig;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Lb);
        }
    }

    class AnonymousClass_2 extends InitiateMatchImpl {
        final /* synthetic */ String Ld;

        AnonymousClass_2(String str) {
            this.Ld = str;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.Ld);
        }
    }

    class AnonymousClass_3 extends InitiateMatchImpl {
        final /* synthetic */ String Km;

        AnonymousClass_3(String str) {
            this.Km = str;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.e(this, this.Km);
        }
    }

    class AnonymousClass_4 extends UpdateMatchImpl {
        final /* synthetic */ String Ld;
        final /* synthetic */ byte[] Lg;
        final /* synthetic */ String Lh;
        final /* synthetic */ ParticipantResult[] Li;

        AnonymousClass_4(String str, byte[] bArr, String str2, ParticipantResult[] participantResultArr) {
            this.Ld = str;
            this.Lg = bArr;
            this.Lh = str2;
            this.Li = participantResultArr;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Ld, this.Lg, this.Lh, this.Li);
        }
    }

    class AnonymousClass_5 extends UpdateMatchImpl {
        final /* synthetic */ String Ld;
        final /* synthetic */ byte[] Lg;
        final /* synthetic */ ParticipantResult[] Li;

        AnonymousClass_5(String str, byte[] bArr, ParticipantResult[] participantResultArr) {
            this.Ld = str;
            this.Lg = bArr;
            this.Li = participantResultArr;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Ld, this.Lg, this.Li);
        }
    }

    class AnonymousClass_6 extends LeaveMatchImpl {
        final /* synthetic */ String Ld;

        AnonymousClass_6(String str) {
            this.Ld = str;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.f(this, this.Ld);
        }
    }

    class AnonymousClass_7 extends LeaveMatchImpl {
        final /* synthetic */ String Ld;
        final /* synthetic */ String Lh;

        AnonymousClass_7(String str, String str2) {
            this.Ld = str;
            this.Lh = str2;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Ld, this.Lh);
        }
    }

    class AnonymousClass_8 extends CancelMatchImpl {
        final /* synthetic */ String Ld;

        AnonymousClass_8(String x0, String str) {
            this.Ld = str;
            super(x0);
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.g(this, this.Ld);
        }
    }

    class AnonymousClass_9 extends LoadMatchesImpl {
        final /* synthetic */ int Le;
        final /* synthetic */ int[] Lf;

        AnonymousClass_9(int i, int[] iArr) {
            this.Le = i;
            this.Lf = iArr;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Le, this.Lf);
        }
    }

    public PendingResult<InitiateMatchResult> acceptInvitation(GoogleApiClient apiClient, String invitationId) {
        return apiClient.b(new AnonymousClass_3(invitationId));
    }

    public PendingResult<CancelMatchResult> cancelMatch(GoogleApiClient apiClient, String matchId) {
        return apiClient.b(new AnonymousClass_8(matchId, matchId));
    }

    public PendingResult<InitiateMatchResult> createMatch(GoogleApiClient apiClient, TurnBasedMatchConfig config) {
        return apiClient.b(new AnonymousClass_1(config));
    }

    public void declineInvitation(GoogleApiClient apiClient, String invitationId) {
        Games.c(apiClient).m(invitationId, 1);
    }

    public void dismissInvitation(GoogleApiClient apiClient, String invitationId) {
        Games.c(apiClient).l(invitationId, 1);
    }

    public void dismissMatch(GoogleApiClient apiClient, String matchId) {
        Games.c(apiClient).aB(matchId);
    }

    public PendingResult<UpdateMatchResult> finishMatch(GoogleApiClient apiClient, String matchId) {
        return finishMatch(apiClient, matchId, null, (ParticipantResult[]) null);
    }

    public PendingResult<UpdateMatchResult> finishMatch(GoogleApiClient apiClient, String matchId, byte[] matchData, List<ParticipantResult> results) {
        return finishMatch(apiClient, matchId, matchData, results == null ? null : (ParticipantResult[]) results.toArray(new ParticipantResult[results.size()]));
    }

    public PendingResult<UpdateMatchResult> finishMatch(GoogleApiClient apiClient, String matchId, byte[] matchData, ParticipantResult... results) {
        return apiClient.b(new AnonymousClass_5(matchId, matchData, results));
    }

    public Intent getInboxIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gr();
    }

    public int getMaxMatchDataSize(GoogleApiClient apiClient) {
        return Games.c(apiClient).gA();
    }

    public Intent getSelectOpponentsIntent(GoogleApiClient apiClient, int minPlayers, int maxPlayers) {
        return Games.c(apiClient).a(minPlayers, maxPlayers, true);
    }

    public Intent getSelectOpponentsIntent(GoogleApiClient apiClient, int minPlayers, int maxPlayers, boolean allowAutomatch) {
        return Games.c(apiClient).a(minPlayers, maxPlayers, allowAutomatch);
    }

    public PendingResult<LeaveMatchResult> leaveMatch(GoogleApiClient apiClient, String matchId) {
        return apiClient.b(new AnonymousClass_6(matchId));
    }

    public PendingResult<LeaveMatchResult> leaveMatchDuringTurn(GoogleApiClient apiClient, String matchId, String pendingParticipantId) {
        return apiClient.b(new AnonymousClass_7(matchId, pendingParticipantId));
    }

    public PendingResult<LoadMatchResult> loadMatch(GoogleApiClient apiClient, String matchId) {
        return apiClient.a(new AnonymousClass_10(matchId));
    }

    public PendingResult<LoadMatchesResult> loadMatchesByStatus(GoogleApiClient apiClient, int invitationSortOrder, int[] matchTurnStatuses) {
        return apiClient.a(new AnonymousClass_9(invitationSortOrder, matchTurnStatuses));
    }

    public PendingResult<LoadMatchesResult> loadMatchesByStatus(GoogleApiClient apiClient, int[] matchTurnStatuses) {
        return loadMatchesByStatus(apiClient, 0, matchTurnStatuses);
    }

    public void registerMatchUpdateListener(GoogleApiClient apiClient, OnTurnBasedMatchUpdateReceivedListener listener) {
        Games.c(apiClient).a(listener);
    }

    public PendingResult<InitiateMatchResult> rematch(GoogleApiClient apiClient, String matchId) {
        return apiClient.b(new AnonymousClass_2(matchId));
    }

    public PendingResult<UpdateMatchResult> takeTurn(GoogleApiClient apiClient, String matchId, byte[] matchData, String pendingParticipantId) {
        return takeTurn(apiClient, matchId, matchData, pendingParticipantId, (ParticipantResult[]) 0);
    }

    public PendingResult<UpdateMatchResult> takeTurn(GoogleApiClient apiClient, String matchId, byte[] matchData, String pendingParticipantId, List<ParticipantResult> results) {
        return takeTurn(apiClient, matchId, matchData, pendingParticipantId, results == null ? null : (ParticipantResult[]) results.toArray(new ParticipantResult[results.size()]));
    }

    public PendingResult<UpdateMatchResult> takeTurn(GoogleApiClient apiClient, String matchId, byte[] matchData, String pendingParticipantId, ParticipantResult... results) {
        return apiClient.b(new AnonymousClass_4(matchId, matchData, pendingParticipantId, results));
    }

    public void unregisterMatchUpdateListener(GoogleApiClient apiClient) {
        Games.c(apiClient).gu();
    }
}