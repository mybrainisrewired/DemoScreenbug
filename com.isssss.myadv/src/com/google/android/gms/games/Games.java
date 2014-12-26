package com.google.android.gms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.View;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Api.b;
import com.google.android.gms.common.api.Api.c;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.internal.api.AchievementsImpl;
import com.google.android.gms.games.internal.api.AclsImpl;
import com.google.android.gms.games.internal.api.GamesMetadataImpl;
import com.google.android.gms.games.internal.api.InvitationsImpl;
import com.google.android.gms.games.internal.api.LeaderboardsImpl;
import com.google.android.gms.games.internal.api.MultiplayerImpl;
import com.google.android.gms.games.internal.api.NotificationsImpl;
import com.google.android.gms.games.internal.api.PlayersImpl;
import com.google.android.gms.games.internal.api.RealTimeMultiplayerImpl;
import com.google.android.gms.games.internal.api.RequestsImpl;
import com.google.android.gms.games.internal.api.TurnBasedMultiplayerImpl;
import com.google.android.gms.games.internal.game.Acls;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.multiplayer.Invitations;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMultiplayer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.request.Requests;
import com.google.android.gms.internal.fc;
import com.google.android.gms.internal.fq;

public final class Games {
    public static final Api<GamesOptions> API;
    public static final Achievements Achievements;
    public static final String EXTRA_PLAYER_IDS = "players";
    public static final GamesMetadata GamesMetadata;
    public static final Scope HV;
    public static final Api<GamesOptions> HW;
    public static final Multiplayer HX;
    public static final Acls HY;
    public static final Invitations Invitations;
    public static final Leaderboards Leaderboards;
    public static final Notifications Notifications;
    public static final Players Players;
    public static final RealTimeMultiplayer RealTimeMultiplayer;
    public static final Requests Requests;
    public static final Scope SCOPE_GAMES;
    public static final TurnBasedMultiplayer TurnBasedMultiplayer;
    static final c<GamesClientImpl> wx;
    private static final b<GamesClientImpl, GamesOptions> wy;

    public static abstract class BaseGamesApiMethodImpl<R extends Result> extends a.b<R, GamesClientImpl> {
        public BaseGamesApiMethodImpl() {
            super(wx);
        }
    }

    public static final class GamesOptions implements Optional {
        final boolean HZ;
        final boolean Ia;
        final int Ib;
        final boolean Ic;
        final int Id;

        public static final class Builder {
            boolean HZ;
            boolean Ia;
            int Ib;
            boolean Ic;
            int Id;

            private Builder() {
                this.HZ = false;
                this.Ia = true;
                this.Ib = 17;
                this.Ic = false;
                this.Id = 4368;
            }

            public com.google.android.gms.games.Games.GamesOptions build() {
                return new com.google.android.gms.games.Games.GamesOptions(null);
            }

            public com.google.android.gms.games.Games.GamesOptions.Builder setSdkVariant(int variant) {
                this.Id = variant;
                return this;
            }

            public com.google.android.gms.games.Games.GamesOptions.Builder setShowConnectingPopup(boolean showConnectingPopup) {
                this.Ia = showConnectingPopup;
                this.Ib = 17;
                return this;
            }

            public com.google.android.gms.games.Games.GamesOptions.Builder setShowConnectingPopup(boolean showConnectingPopup, int gravity) {
                this.Ia = showConnectingPopup;
                this.Ib = gravity;
                return this;
            }
        }

        private GamesOptions() {
            this.HZ = false;
            this.Ia = true;
            this.Ib = 17;
            this.Ic = false;
            this.Id = 4368;
        }

        private GamesOptions(Builder builder) {
            this.HZ = builder.HZ;
            this.Ia = builder.Ia;
            this.Ib = builder.Ib;
            this.Ic = builder.Ic;
            this.Id = builder.Id;
        }

        public static Builder builder() {
            return new Builder();
        }
    }

    private static abstract class SignOutImpl extends com.google.android.gms.games.Games.BaseGamesApiMethodImpl<Status> {
        private SignOutImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    static {
        wx = new c();
        wy = new b<GamesClientImpl, GamesOptions>() {
            public GamesClientImpl a(Context context, Looper looper, fc fcVar, com.google.android.gms.games.Games.GamesOptions gamesOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
                if (gamesOptions == null) {
                    com.google.android.gms.games.Games.GamesOptions gamesOptions2 = gamesOptions;
                }
                return new GamesClientImpl(context, looper, fcVar.eG(), fcVar.eC(), connectionCallbacks, onConnectionFailedListener, fcVar.eF(), fcVar.eD(), fcVar.eH(), gamesOptions.HZ, gamesOptions.Ia, gamesOptions.Ib, gamesOptions.Ic, gamesOptions.Id);
            }

            public int getPriority() {
                return 1;
            }
        };
        SCOPE_GAMES = new Scope(Scopes.GAMES);
        API = new Api(wy, wx, new Scope[]{SCOPE_GAMES});
        HV = new Scope("https://www.googleapis.com/auth/games.firstparty");
        HW = new Api(wy, wx, new Scope[]{HV});
        GamesMetadata = new GamesMetadataImpl();
        Achievements = new AchievementsImpl();
        Leaderboards = new LeaderboardsImpl();
        Invitations = new InvitationsImpl();
        TurnBasedMultiplayer = new TurnBasedMultiplayerImpl();
        RealTimeMultiplayer = new RealTimeMultiplayerImpl();
        HX = new MultiplayerImpl();
        Players = new PlayersImpl();
        Notifications = new NotificationsImpl();
        Requests = new RequestsImpl();
        HY = new AclsImpl();
    }

    private Games() {
    }

    public static GamesClientImpl c(GoogleApiClient googleApiClient) {
        boolean z = true;
        fq.b(googleApiClient != null, (Object)"GoogleApiClient parameter is required.");
        fq.a(googleApiClient.isConnected(), "GoogleApiClient must be connected.");
        GamesClientImpl gamesClientImpl = (GamesClientImpl) googleApiClient.a(wx);
        if (gamesClientImpl == null) {
            z = false;
        }
        fq.a(z, "GoogleApiClient is not configured to use the Games Api. Pass Games.API into GoogleApiClient.Builder#addApi() to use this feature.");
        return gamesClientImpl;
    }

    public static String getAppId(GoogleApiClient apiClient) {
        return c(apiClient).gz();
    }

    public static String getCurrentAccountName(GoogleApiClient apiClient) {
        return c(apiClient).gl();
    }

    public static int getSdkVariant(GoogleApiClient apiClient) {
        return c(apiClient).gy();
    }

    public static Intent getSettingsIntent(GoogleApiClient apiClient) {
        return c(apiClient).gx();
    }

    public static void setGravityForPopups(GoogleApiClient apiClient, int gravity) {
        c(apiClient).aX(gravity);
    }

    public static void setViewForPopups(GoogleApiClient apiClient, View gamesContentView) {
        fq.f(gamesContentView);
        c(apiClient).f(gamesContentView);
    }

    public static PendingResult<Status> signOut(GoogleApiClient apiClient) {
        return apiClient.b(new SignOutImpl() {
            {
                super();
            }

            protected void a(GamesClientImpl gamesClientImpl) {
                gamesClientImpl.b(this);
            }
        });
    }
}