package com.google.android.gms.games.internal.api;

import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.Notifications;
import com.google.android.gms.games.Notifications.ContactSettingLoadResult;
import com.google.android.gms.games.Notifications.GameMuteStatusChangeResult;
import com.google.android.gms.games.Notifications.GameMuteStatusLoadResult;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;

public final class NotificationsImpl implements Notifications {

    class AnonymousClass_1 extends BaseGamesApiMethodImpl<GameMuteStatusChangeResult> {
        final /* synthetic */ String KB;

        class AnonymousClass_1 implements GameMuteStatusChangeResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        public GameMuteStatusChangeResult H(Status status) {
            return new AnonymousClass_1(status);
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.KB, true);
        }

        public /* synthetic */ Result d(Status status) {
            return H(status);
        }
    }

    class AnonymousClass_2 extends BaseGamesApiMethodImpl<GameMuteStatusChangeResult> {
        final /* synthetic */ String KB;

        class AnonymousClass_1 implements GameMuteStatusChangeResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        public GameMuteStatusChangeResult H(Status status) {
            return new AnonymousClass_1(status);
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.c(this, this.KB, false);
        }

        public /* synthetic */ Result d(Status status) {
            return H(status);
        }
    }

    class AnonymousClass_3 extends BaseGamesApiMethodImpl<GameMuteStatusLoadResult> {
        final /* synthetic */ String KB;

        class AnonymousClass_1 implements GameMuteStatusLoadResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        public GameMuteStatusLoadResult I(Status status) {
            return new AnonymousClass_1(status);
        }

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.n(this, this.KB);
        }

        public /* synthetic */ Result d(Status status) {
            return I(status);
        }
    }

    private static abstract class ContactSettingLoadImpl extends BaseGamesApiMethodImpl<ContactSettingLoadResult> {

        class AnonymousClass_1 implements ContactSettingLoadResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }
        }

        private ContactSettingLoadImpl() {
        }

        public ContactSettingLoadResult J(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return J(status);
        }
    }

    private static abstract class ContactSettingUpdateImpl extends BaseGamesApiMethodImpl<Status> {
        private ContactSettingUpdateImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    class AnonymousClass_4 extends ContactSettingLoadImpl {
        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.j(this);
        }
    }

    class AnonymousClass_5 extends ContactSettingUpdateImpl {
        final /* synthetic */ boolean KF;
        final /* synthetic */ Bundle KG;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.a(this, this.KF, this.KG);
        }
    }

    public void clear(GoogleApiClient apiClient, int notificationTypes) {
        Games.c(apiClient).aY(notificationTypes);
    }

    public void clearAll(GoogleApiClient apiClient) {
        clear(apiClient, ApiEventType.API_MRAID_SET_EXPAND_PROPERTIES);
    }
}