package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.internal.e;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.Collection;

public final class ib implements People {

    private static abstract class a extends com.google.android.gms.plus.Plus.a<LoadPeopleResult> {

        class AnonymousClass_1 implements LoadPeopleResult {
            final /* synthetic */ Status wz;

            AnonymousClass_1(Status status) {
                this.wz = status;
            }

            public String getNextPageToken() {
                return null;
            }

            public PersonBuffer getPersonBuffer() {
                return null;
            }

            public Status getStatus() {
                return this.wz;
            }

            public void release() {
            }
        }

        private a() {
        }

        public LoadPeopleResult ab(Status status) {
            return new AnonymousClass_1(status);
        }

        public /* synthetic */ Result d(Status status) {
            return ab(status);
        }
    }

    class AnonymousClass_1 extends a {
        final /* synthetic */ int UD;
        final /* synthetic */ String Uw;

        AnonymousClass_1(int i, String str) {
            this.UD = i;
            this.Uw = str;
            super();
        }

        protected void a(e eVar) {
            a(eVar.a(this, this.UD, this.Uw));
        }
    }

    class AnonymousClass_2 extends a {
        final /* synthetic */ String Uw;

        AnonymousClass_2(String str) {
            this.Uw = str;
            super();
        }

        protected void a(e eVar) {
            a(eVar.o(this, this.Uw));
        }
    }

    class AnonymousClass_4 extends a {
        final /* synthetic */ Collection UF;

        AnonymousClass_4(Collection collection) {
            this.UF = collection;
            super();
        }

        protected void a(e eVar) {
            eVar.a(this, this.UF);
        }
    }

    class AnonymousClass_5 extends a {
        final /* synthetic */ String[] UG;

        AnonymousClass_5(String[] strArr) {
            this.UG = strArr;
            super();
        }

        protected void a(e eVar) {
            eVar.d(this, this.UG);
        }
    }

    public Person getCurrentPerson(GoogleApiClient googleApiClient) {
        return Plus.a(googleApiClient, Plus.wx).getCurrentPerson();
    }

    public PendingResult<LoadPeopleResult> load(GoogleApiClient googleApiClient, Collection<String> personIds) {
        return googleApiClient.a(new AnonymousClass_4(personIds));
    }

    public PendingResult<LoadPeopleResult> load(GoogleApiClient googleApiClient, String... personIds) {
        return googleApiClient.a(new AnonymousClass_5(personIds));
    }

    public PendingResult<LoadPeopleResult> loadConnected(GoogleApiClient googleApiClient) {
        return googleApiClient.a(new a() {
            {
                super();
            }

            protected void a(e eVar) {
                eVar.m(this);
            }
        });
    }

    public PendingResult<LoadPeopleResult> loadVisible(GoogleApiClient googleApiClient, int orderBy, String pageToken) {
        return googleApiClient.a(new AnonymousClass_1(orderBy, pageToken));
    }

    public PendingResult<LoadPeopleResult> loadVisible(GoogleApiClient googleApiClient, String pageToken) {
        return googleApiClient.a(new AnonymousClass_2(pageToken));
    }
}