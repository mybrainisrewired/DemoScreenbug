package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.DriveResource.MetadataResult;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.DriveEvent.Listener;

public class r implements DriveResource {
    protected final DriveId Ew;

    private static class e implements MetadataResult {
        private final Metadata Fy;
        private final Status wJ;

        public e(Status status, Metadata metadata) {
            this.wJ = status;
            this.Fy = metadata;
        }

        public Metadata getMetadata() {
            return this.Fy;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    private static class b extends c {
        private final com.google.android.gms.common.api.a.d<MetadataBufferResult> wH;

        public b(com.google.android.gms.common.api.a.d<MetadataBufferResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnListParentsResponse onListParentsResponse) throws RemoteException {
            this.wH.b(new e(Status.Bv, new MetadataBuffer(onListParentsResponse.fP(), null), false));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new e(status, null, false));
        }
    }

    private static class d extends c {
        private final com.google.android.gms.common.api.a.d<MetadataResult> wH;

        public d(com.google.android.gms.common.api.a.d<MetadataResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnMetadataResponse onMetadataResponse) throws RemoteException {
            this.wH.b(new e(Status.Bv, new j(onMetadataResponse.fQ())));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new e(status, null));
        }
    }

    private abstract class a extends m<MetadataResult> {
        private a() {
        }

        public /* synthetic */ Result d(Status status) {
            return s(status);
        }

        public MetadataResult s(Status status) {
            return new e(status, null);
        }
    }

    private abstract class c extends m<MetadataBufferResult> {
        private c() {
        }

        public /* synthetic */ Result d(Status status) {
            return p(status);
        }

        public MetadataBufferResult p(Status status) {
            return new e(status, null, false);
        }
    }

    private abstract class f extends m<MetadataResult> {
        private f() {
        }

        public /* synthetic */ Result d(Status status) {
            return s(status);
        }

        public MetadataResult s(Status status) {
            return new e(status, null);
        }
    }

    class AnonymousClass_3 extends f {
        final /* synthetic */ MetadataChangeSet Fs;

        AnonymousClass_3(MetadataChangeSet metadataChangeSet) {
            this.Fs = metadataChangeSet;
            super(null);
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new UpdateMetadataRequest(r.this.Ew, this.Fs.fD()), new d(this));
        }
    }

    protected r(DriveId driveId) {
        this.Ew = driveId;
    }

    public PendingResult<Status> addChangeListener(GoogleApiClient apiClient, Listener<ChangeEvent> listener) {
        return ((n) apiClient.a(Drive.wx)).a(apiClient, this.Ew, 1, listener);
    }

    public DriveId getDriveId() {
        return this.Ew;
    }

    public PendingResult<MetadataResult> getMetadata(GoogleApiClient apiClient) {
        return apiClient.a(new a() {
            {
                super(null);
            }

            protected void a(n nVar) throws RemoteException {
                nVar.fE().a(new GetMetadataRequest(r.this.Ew), new d(this));
            }
        });
    }

    public PendingResult<MetadataBufferResult> listParents(GoogleApiClient apiClient) {
        return apiClient.a(new c() {
            {
                super(null);
            }

            protected void a(n nVar) throws RemoteException {
                nVar.fE().a(new ListParentsRequest(r.this.Ew), new b(this));
            }
        });
    }

    public PendingResult<Status> removeChangeListener(GoogleApiClient apiClient, Listener<ChangeEvent> listener) {
        return ((n) apiClient.a(Drive.wx)).b(apiClient, this.Ew, 1, listener);
    }

    public PendingResult<MetadataResult> updateMetadata(GoogleApiClient apiClient, MetadataChangeSet changeSet) {
        if (changeSet != null) {
            return apiClient.b(new AnonymousClass_3(changeSet));
        }
        throw new IllegalArgumentException("ChangeSet must be provided.");
    }
}