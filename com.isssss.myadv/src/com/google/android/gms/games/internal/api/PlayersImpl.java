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
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.Players;
import com.google.android.gms.games.Players.LoadExtendedPlayersResult;
import com.google.android.gms.games.Players.LoadOwnerCoverPhotoUrisResult;
import com.google.android.gms.games.Players.LoadPlayersResult;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class PlayersImpl implements Players {

    private static abstract class LoadExtendedPlayersImpl extends BaseGamesApiMethodImpl<LoadExtendedPlayersResult> {

        class AnonymousClass_1 implements LoadExtendedPlayersResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadExtendedPlayersImpl() {
        }

        public LoadExtendedPlayersResult K(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return K(status);
        }
    }

    private static abstract class LoadOwnerCoverPhotoUrisImpl extends BaseGamesApiMethodImpl<LoadOwnerCoverPhotoUrisResult> {

        class AnonymousClass_1 implements LoadOwnerCoverPhotoUrisResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private LoadOwnerCoverPhotoUrisImpl() {
        }

        public LoadOwnerCoverPhotoUrisResult L(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return L(status);
        }
    }

    private static abstract class LoadPlayersImpl extends BaseGamesApiMethodImpl<LoadPlayersResult> {

        class AnonymousClass_1 implements LoadPlayersResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public PlayerBuffer getPlayers() {
                return new PlayerBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadPlayersImpl() {
        }

        public LoadPlayersResult M(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return M(status);
        }
    }

    class AnonymousClass_10 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_11 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.Kb, true, false);
        }
    }

    class AnonymousClass_12 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_13 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.Kb, true, false);
        }
    }

    class AnonymousClass_14 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_15 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.Kb, true, false);
        }
    }

    class AnonymousClass_16 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;
        final /* synthetic */ String Kd;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.Kd, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_17 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;
        final /* synthetic */ String Kd;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.d(this, this.Kd, this.Kb, true, false);
        }
    }

    class AnonymousClass_18 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;
        final /* synthetic */ int KJ;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JT, this.KJ, this.JQ);
        }
    }

    class AnonymousClass_19 extends LoadExtendedPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.e(this, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_1 extends LoadPlayersImpl {
        final /* synthetic */ String JS;

        AnonymousClass_1(String str) {
            this.JS = str;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JS);
        }
    }

    class AnonymousClass_20 extends LoadExtendedPlayersImpl {
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.e(this, this.Kb, true, false);
        }
    }

    class AnonymousClass_21 extends LoadOwnerCoverPhotoUrisImpl {
        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.h(this);
        }
    }

    class AnonymousClass_2 extends LoadPlayersImpl {
        final /* synthetic */ String[] KK;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.KK);
        }
    }

    class AnonymousClass_3 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        AnonymousClass_3(int i, boolean z) {
            this.Kb = i;
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_4 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;

        AnonymousClass_4(int i) {
            this.Kb = i;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.Kb, true, false);
        }
    }

    class AnonymousClass_5 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        AnonymousClass_5(int i, boolean z) {
            this.Kb = i;
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, "playedWith", this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_6 extends LoadPlayersImpl {
        final /* synthetic */ int Kb;

        AnonymousClass_6(int i) {
            this.Kb = i;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, "playedWith", this.Kb, true, false);
        }
    }

    class AnonymousClass_7 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JT;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, "playedWith", this.JT, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_8 extends LoadPlayersImpl {
        final /* synthetic */ String JT;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, "playedWith", this.JT, this.Kb, true, false);
        }
    }

    class AnonymousClass_9 extends LoadPlayersImpl {
        final /* synthetic */ boolean JQ;

        AnonymousClass_9(boolean z) {
            this.JQ = z;
            super();
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JQ);
        }
    }

    public Player getCurrentPlayer(GoogleApiClient apiClient) {
        return Games.c(apiClient).gn();
    }

    public String getCurrentPlayerId(GoogleApiClient apiClient) {
        return Games.c(apiClient).gm();
    }

    public Intent getPlayerSearchIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gw();
    }

    public PendingResult<LoadPlayersResult> loadConnectedPlayers(GoogleApiClient apiClient, boolean forceReload) {
        return apiClient.a(new AnonymousClass_9(forceReload));
    }

    public PendingResult<LoadPlayersResult> loadInvitablePlayers(GoogleApiClient apiClient, int pageSize, boolean forceReload) {
        return apiClient.a(new AnonymousClass_3(pageSize, forceReload));
    }

    public PendingResult<LoadPlayersResult> loadMoreInvitablePlayers(GoogleApiClient apiClient, int pageSize) {
        return apiClient.a(new AnonymousClass_4(pageSize));
    }

    public PendingResult<LoadPlayersResult> loadMoreRecentlyPlayedWithPlayers(GoogleApiClient apiClient, int pageSize) {
        return apiClient.a(new AnonymousClass_6(pageSize));
    }

    public PendingResult<LoadPlayersResult> loadPlayer(GoogleApiClient apiClient, String playerId) {
        return apiClient.a(new AnonymousClass_1(playerId));
    }

    public PendingResult<LoadPlayersResult> loadRecentlyPlayedWithPlayers(GoogleApiClient apiClient, int pageSize, boolean forceReload) {
        return apiClient.a(new AnonymousClass_5(pageSize, forceReload));
    }
}