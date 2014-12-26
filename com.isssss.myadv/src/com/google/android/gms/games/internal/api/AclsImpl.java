package com.google.android.gms.games.internal.api;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games.BaseGamesApiMethodImpl;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.games.internal.game.Acls;
import com.google.android.gms.games.internal.game.Acls.LoadAclResult;

public final class AclsImpl implements Acls {

    static class AnonymousClass_1 implements LoadAclResult {
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

    private static abstract class LoadNotifyAclImpl extends BaseGamesApiMethodImpl<LoadAclResult> {
        private LoadNotifyAclImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return x(status);
        }

        public LoadAclResult x(Status status) {
            return AclsImpl.v(status);
        }
    }

    private static abstract class UpdateNotifyAclImpl extends BaseGamesApiMethodImpl<Status> {
        private UpdateNotifyAclImpl() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    class AnonymousClass_2 extends LoadNotifyAclImpl {
        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.i(this);
        }
    }

    class AnonymousClass_3 extends UpdateNotifyAclImpl {
        final /* synthetic */ String JY;

        protected void a(GamesClientImpl gamesClientImpl) {
            gamesClientImpl.m(this, this.JY);
        }
    }

    private static LoadAclResult v(Status status) {
        return new AnonymousClass_1(status);
    }
}