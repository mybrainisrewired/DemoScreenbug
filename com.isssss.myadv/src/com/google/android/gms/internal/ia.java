package com.google.android.gms.internal;

import android.net.Uri;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Moments;
import com.google.android.gms.plus.Moments.LoadMomentsResult;
import com.google.android.gms.plus.internal.e;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;

public final class ia implements Moments {

    private static abstract class a extends com.google.android.gms.plus.Plus.a<LoadMomentsResult> {

        class AnonymousClass_1 implements LoadMomentsResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public MomentBuffer getMomentBuffer() {
                return null;
            }

            public String getNextPageToken() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }

            public String getUpdated() {
                return null;
            }

            public void release() {
            }
        }

        private a() {
        }

        public LoadMomentsResult aa(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return aa(status);
        }
    }

    private static abstract class b extends com.google.android.gms.plus.Plus.a<Status> {
        private b() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    private static abstract class c extends com.google.android.gms.plus.Plus.a<Status> {
        private c() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    class AnonymousClass_2 extends a {
        final /* synthetic */ int Ks;
        final /* synthetic */ String Uw;
        final /* synthetic */ Uri Ux;
        final /* synthetic */ String Uy;
        final /* synthetic */ String Uz;

        AnonymousClass_2(int i, String str, Uri uri, String str2, String str3) {
            this.Ks = i;
            this.Uw = str;
            this.Ux = uri;
            this.Uy = str2;
            this.Uz = str3;
            super();
        }

        protected void a(e eVar) {
            eVar.a(this, this.Ks, this.Uw, this.Ux, this.Uy, this.Uz);
        }
    }

    class AnonymousClass_3 extends c {
        final /* synthetic */ Moment UA;

        AnonymousClass_3(Moment moment) {
            this.UA = moment;
            super();
        }

        protected void a(e eVar) {
            eVar.a(this, this.UA);
        }
    }

    class AnonymousClass_4 extends b {
        final /* synthetic */ String UB;

        AnonymousClass_4(String str) {
            this.UB = str;
            super();
        }

        protected void a(e eVar) {
            eVar.removeMoment(this.UB);
            a(Status.Bv);
        }
    }

    public PendingResult<LoadMomentsResult> load(GoogleApiClient googleApiClient) {
        return googleApiClient.a(new a() {
            {
                super();
            }

            protected void a(e eVar) {
                eVar.l(this);
            }
        });
    }

    public PendingResult<LoadMomentsResult> load(GoogleApiClient googleApiClient, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        return googleApiClient.a(new AnonymousClass_2(maxResults, pageToken, targetUrl, type, userId));
    }

    public PendingResult<Status> remove(GoogleApiClient googleApiClient, String momentId) {
        return googleApiClient.b(new AnonymousClass_4(momentId));
    }

    public PendingResult<Status> write(GoogleApiClient googleApiClient, Moment moment) {
        return googleApiClient.b(new AnonymousClass_3(moment));
    }
}