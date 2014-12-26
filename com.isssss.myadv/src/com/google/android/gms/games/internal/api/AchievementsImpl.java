package com.google.android.gms.games.internal.api;

import android.content.Intent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.google.android.gms.games.achievement.Achievements.UpdateAchievementResult;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class AchievementsImpl implements Achievements {

    private static abstract class LoadImpl extends BaseGamesApiMethodImpl<LoadAchievementsResult> {

        class AnonymousClass_1 implements LoadAchievementsResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public AchievementBuffer getAchievements() {
                return new AchievementBuffer(DataHolder.empty(ApiEventType.API_MRAID_IS_VIEWABLE));
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private LoadImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return t(status);
        }

        public LoadAchievementsResult t(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    private static abstract class UpdateImpl extends BaseGamesApiMethodImpl<UpdateAchievementResult> {
        private final String wp;

        class AnonymousClass_1 implements UpdateAchievementResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public String getAchievementId() {
                return UpdateImpl.this.wp;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        public UpdateImpl(String id) {
            this.wp = id;
        }

        public /* synthetic */ Result d(Status status) {
            return u(status);
        }

        public UpdateAchievementResult u(Status status) {
            return new AnonymousClass_1(status);
        }
    }

    class AnonymousClass_10 extends LoadImpl {
        final /* synthetic */ boolean JQ;
        final /* synthetic */ String JS;
        final /* synthetic */ String JT;

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JS, this.JT, this.JQ);
        }
    }

    class AnonymousClass_1 extends LoadImpl {
        final /* synthetic */ boolean JQ;

        AnonymousClass_1(boolean z) {
            this.JQ = z;
            super();
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.JQ);
        }
    }

    class AnonymousClass_2 extends UpdateImpl {
        final /* synthetic */ String JU;

        AnonymousClass_2(String x0, String str) {
            this.JU = str;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(null, this.JU);
        }
    }

    class AnonymousClass_3 extends UpdateImpl {
        final /* synthetic */ String JU;

        AnonymousClass_3(String x0, String str) {
            this.JU = str;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JU);
        }
    }

    class AnonymousClass_4 extends UpdateImpl {
        final /* synthetic */ String JU;

        AnonymousClass_4(String x0, String str) {
            this.JU = str;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(null, this.JU);
        }
    }

    class AnonymousClass_5 extends UpdateImpl {
        final /* synthetic */ String JU;

        AnonymousClass_5(String x0, String str) {
            this.JU = str;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.JU);
        }
    }

    class AnonymousClass_6 extends UpdateImpl {
        final /* synthetic */ String JU;
        final /* synthetic */ int JV;

        AnonymousClass_6(String x0, String str, int i) {
            this.JU = str;
            this.JV = i;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(null, this.JU, this.JV);
        }
    }

    class AnonymousClass_7 extends UpdateImpl {
        final /* synthetic */ String JU;
        final /* synthetic */ int JV;

        AnonymousClass_7(String x0, String str, int i) {
            this.JU = str;
            this.JV = i;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.JU, this.JV);
        }
    }

    class AnonymousClass_8 extends UpdateImpl {
        final /* synthetic */ String JU;
        final /* synthetic */ int JV;

        AnonymousClass_8(String x0, String str, int i) {
            this.JU = str;
            this.JV = i;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(null, this.JU, this.JV);
        }
    }

    class AnonymousClass_9 extends UpdateImpl {
        final /* synthetic */ String JU;
        final /* synthetic */ int JV;

        AnonymousClass_9(String x0, String str, int i) {
            this.JU = str;
            this.JV = i;
            super(x0);
        }

        public void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.b(this, this.JU, this.JV);
        }
    }

    public Intent getAchievementsIntent(GoogleApiClient apiClient) {
        return Games.c(apiClient).gq();
    }

    public void increment(GoogleApiClient apiClient, String id, int numSteps) {
        apiClient.b(new AnonymousClass_6(id, id, numSteps));
    }

    public PendingResult<UpdateAchievementResult> incrementImmediate(GoogleApiClient apiClient, String id, int numSteps) {
        return apiClient.b(new AnonymousClass_7(id, id, numSteps));
    }

    public PendingResult<LoadAchievementsResult> load(GoogleApiClient apiClient, boolean forceReload) {
        return apiClient.a(new AnonymousClass_1(forceReload));
    }

    public void reveal(GoogleApiClient apiClient, String id) {
        apiClient.b(new AnonymousClass_2(id, id));
    }

    public PendingResult<UpdateAchievementResult> revealImmediate(GoogleApiClient apiClient, String id) {
        return apiClient.b(new AnonymousClass_3(id, id));
    }

    public void setSteps(GoogleApiClient apiClient, String id, int numSteps) {
        apiClient.b(new AnonymousClass_8(id, id, numSteps));
    }

    public PendingResult<UpdateAchievementResult> setStepsImmediate(GoogleApiClient apiClient, String id, int numSteps) {
        return apiClient.b(new AnonymousClass_9(id, id, numSteps));
    }

    public void unlock(GoogleApiClient apiClient, String id) {
        apiClient.b(new AnonymousClass_4(id, id));
    }

    public PendingResult<UpdateAchievementResult> unlockImmediate(GoogleApiClient apiClient, String id) {
        return apiClient.b(new AnonymousClass_5(id, id));
    }
}