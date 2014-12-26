package com.google.android.gms.games.internal.api;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.d;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameBuffer;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.GamesMetadata;
import com.google.android.gms.games.GamesMetadata.LoadExtendedGamesResult;
import com.google.android.gms.games.GamesMetadata.LoadGameInstancesResult;
import com.google.android.gms.games.GamesMetadata.LoadGameSearchSuggestionsResult;
import com.google.android.gms.games.GamesMetadata.LoadGamesResult;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class GamesMetadataImpl implements GamesMetadata {

    private static abstract class LoadExtendedGamesImpl extends BaseGamesApiMethodImpl<LoadExtendedGamesResult> {

        class AnonymousClass_1 implements LoadExtendedGamesResult {
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

        private LoadExtendedGamesImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return y(status);
        }

        public LoadExtendedGamesResult y(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    private static abstract class LoadGameInstancesImpl extends BaseGamesApiMethodImpl<LoadGameInstancesResult> {

        class AnonymousClass_1 implements LoadGameInstancesResult {
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

        private LoadGameInstancesImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return z(status);
        }

        public LoadGameInstancesResult z(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    private static abstract class LoadGameSearchSuggestionsImpl extends BaseGamesApiMethodImpl<LoadGameSearchSuggestionsResult> {

        class AnonymousClass_1 implements LoadGameSearchSuggestionsResult {
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

        private LoadGameSearchSuggestionsImpl() {
        }

        public LoadGameSearchSuggestionsResult A(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return A(status);
        }
    }

    private static abstract class LoadGamesImpl extends BaseGamesApiMethodImpl<LoadGamesResult> {

        class AnonymousClass_1 implements LoadGamesResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public GameBuffer getGames() {
                return new GameBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadGamesImpl() {
        }

        public LoadGamesResult B(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return B(status);
        }
    }

    class AnonymousClass_10 extends LoadExtendedGamesImpl {
        final /* synthetic */ String Ka;
        final /* synthetic */ int Kb;
        final /* synthetic */ boolean Kc;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Ka, this.Kb, false, true, false, this.Kc);
        }
    }

    class AnonymousClass_11 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String Ka;
        final /* synthetic */ int Kb;
        final /* synthetic */ boolean Kc;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Ka, this.Kb, true, false, this.JQ, this.Kc);
        }
    }

    class AnonymousClass_12 extends LoadExtendedGamesImpl {
        final /* synthetic */ String Ka;
        final /* synthetic */ int Kb;
        final /* synthetic */ boolean Kc;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Ka, this.Kb, true, true, false, this.Kc);
        }
    }

    class AnonymousClass_13 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;
        final /* synthetic */ String Kd;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.Kd, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_14 extends LoadExtendedGamesImpl {
        final /* synthetic */ int Kb;
        final /* synthetic */ String Kd;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.Kd, this.Kb, true, false);
        }
    }

    class AnonymousClass_15 extends LoadGameSearchSuggestionsImpl {
        final /* synthetic */ String Kd;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.k(this, this.Kd);
        }
    }

    class AnonymousClass_2 extends LoadExtendedGamesImpl {
        final /* synthetic */ String JT;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.i(this, this.JT);
        }
    }

    class AnonymousClass_3 extends LoadGameInstancesImpl {
        final /* synthetic */ String JT;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.j(this, this.JT);
        }
    }

    class AnonymousClass_4 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, null, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_5 extends LoadExtendedGamesImpl {
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, null, this.Kb, true, false);
        }
    }

    class AnonymousClass_6 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JS;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JS, this.Kb, false, this.JQ);
        }
    }

    class AnonymousClass_7 extends LoadExtendedGamesImpl {
        final /* synthetic */ String JS;
        final /* synthetic */ int Kb;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JS, this.Kb, true, false);
        }
    }

    class AnonymousClass_8 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ int Kb;
        final /* synthetic */ int Ke;
        final /* synthetic */ boolean Kf;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Kb, this.Ke, this.Kf, this.JQ);
        }
    }

    class AnonymousClass_9 extends LoadExtendedGamesImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String Ka;
        final /* synthetic */ int Kb;
        final /* synthetic */ boolean Kc;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a((d)this, this.Ka, this.Kb, false, false, this.JQ, this.Kc);
        }
    }

    public Game getCurrentGame(GoogleApiClient apiClient) {
        return Games.c(apiClient).go();
    }

    public PendingResult<LoadGamesResult> loadGame(GoogleApiClient apiClient) {
        return apiClient.a(new LoadGamesImpl() {
            {
                super();
            }

            protected void a(GamesClientImpl gamesClientImpl) {
                gamesClientImpl.g(this);
            }
        });
    }
}