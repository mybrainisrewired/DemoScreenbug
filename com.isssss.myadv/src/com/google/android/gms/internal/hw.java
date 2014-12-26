package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.a.b;
import com.google.android.gms.panorama.Panorama;
import com.google.android.gms.panorama.PanoramaApi;
import com.google.android.gms.panorama.PanoramaApi.PanoramaResult;

public class hw implements PanoramaApi {

    private static abstract class a extends b<PanoramaResult, hx> {

        class AnonymousClass_1 implements PanoramaResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public Status getStatus() {
                return this.wz;
            }

            public Intent getViewerIntent() {
                return null;
            }
        }

        public a() {
            super(Panorama.wx);
        }

        public PanoramaResult X(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return X(status);
        }
    }

    class AnonymousClass_1 extends a {
        final /* synthetic */ Uri Tz;

        AnonymousClass_1(Uri uri) {
            this.Tz = uri;
        }

        protected void a(hx hxVar) {
            hxVar.a(this, this.Tz, false);
        }
    }

    class AnonymousClass_2 extends a {
        final /* synthetic */ Uri Tz;

        AnonymousClass_2(Uri uri) {
            this.Tz = uri;
        }

        protected void a(hx hxVar) {
            hxVar.a(this, this.Tz, true);
        }
    }

    public PendingResult<PanoramaResult> loadPanoramaInfo(GoogleApiClient client, Uri uri) {
        return client.a(new AnonymousClass_1(uri));
    }

    public PendingResult<PanoramaResult> loadPanoramaInfoAndGrantAccess(GoogleApiClient client, Uri uri) {
        return client.a(new AnonymousClass_2(uri));
    }
}