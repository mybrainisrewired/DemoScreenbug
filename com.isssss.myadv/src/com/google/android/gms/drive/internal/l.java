package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.CreateFileActivityBuilder;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.DriveApi.DriveIdResult;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Query;

public class l implements DriveApi {

    static class a implements ContentsResult {
        private final Contents EA;
        private final Status wJ;

        public a(Status status, Contents contents) {
            this.wJ = status;
            this.EA = contents;
        }

        public Contents getContents() {
            return this.EA;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    static class c implements DriveIdResult {
        private final DriveId Ew;
        private final Status wJ;

        public c(Status status, DriveId driveId) {
            this.wJ = status;
            this.Ew = driveId;
        }

        public DriveId getDriveId() {
            return this.Ew;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    static class e implements MetadataBufferResult {
        private final MetadataBuffer Ff;
        private final boolean Fg;
        private final Status wJ;

        public e(Status status, MetadataBuffer metadataBuffer, boolean z) {
            this.wJ = status;
            this.Ff = metadataBuffer;
            this.Fg = z;
        }

        public MetadataBuffer getMetadataBuffer() {
            return this.Ff;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    private static class b extends c {
        private final com.google.android.gms.common.api.a.d<DriveIdResult> wH;

        public b(com.google.android.gms.common.api.a.d<DriveIdResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnMetadataResponse onMetadataResponse) throws RemoteException {
            this.wH.b(new c(Status.Bv, new j(onMetadataResponse.fQ()).getDriveId()));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new c(status, null));
        }
    }

    private static class f extends c {
        private final com.google.android.gms.common.api.a.d<ContentsResult> wH;

        public f(com.google.android.gms.common.api.a.d<ContentsResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnContentsResponse onContentsResponse) throws RemoteException {
            this.wH.b(new a(Status.Bv, onContentsResponse.fI()));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new a(status, null));
        }
    }

    static class h extends c {
        private final com.google.android.gms.common.api.a.d<MetadataBufferResult> wH;

        public h(com.google.android.gms.common.api.a.d<MetadataBufferResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnListEntriesResponse onListEntriesResponse) throws RemoteException {
            this.wH.b(new e(Status.Bv, new MetadataBuffer(onListEntriesResponse.fN(), null), onListEntriesResponse.fO()));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new e(status, null, false));
        }
    }

    abstract class d extends m<DriveIdResult> {
        d() {
        }

        public /* synthetic */ Result d(Status status) {
            return n(status);
        }

        public DriveIdResult n(Status status) {
            return new c(status, null);
        }
    }

    abstract class g extends m<ContentsResult> {
        g() {
        }

        public /* synthetic */ Result d(Status status) {
            return o(status);
        }

        public ContentsResult o(Status status) {
            return new a(status, null);
        }
    }

    abstract class i extends m<MetadataBufferResult> {
        i() {
        }

        public /* synthetic */ Result d(Status status) {
            return p(status);
        }

        public MetadataBufferResult p(Status status) {
            return new e(status, null, false);
        }
    }

    static abstract class j extends m<Status> {
        j() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    abstract class l extends m<Status> {
        l() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    class AnonymousClass_1 extends i {
        final /* synthetic */ Query Fb;

        AnonymousClass_1(Query query) {
            this.Fb = query;
            super();
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new QueryRequest(this.Fb), new h(this));
        }
    }

    class AnonymousClass_3 extends j {
        final /* synthetic */ Contents Fd;

        AnonymousClass_3(Contents contents) {
            this.Fd = contents;
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new CloseContentsRequest(this.Fd, false), new al(this));
        }
    }

    class AnonymousClass_4 extends d {
        final /* synthetic */ String Fe;

        AnonymousClass_4(String str) {
            this.Fe = str;
            super();
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new GetMetadataRequest(DriveId.aw(this.Fe)), new b(this));
        }
    }

    static class k extends j {
        k(GoogleApiClient googleApiClient, Status status) {
            a(new com.google.android.gms.common.api.a.c(((n) googleApiClient.a(Drive.wx)).getLooper()));
            a(status);
        }

        protected void a(n nVar) {
        }
    }

    public PendingResult<Status> discardContents(GoogleApiClient apiClient, Contents contents) {
        return apiClient.b(new AnonymousClass_3(contents));
    }

    public PendingResult<DriveIdResult> fetchDriveId(GoogleApiClient apiClient, String resourceId) {
        return apiClient.a(new AnonymousClass_4(resourceId));
    }

    public DriveFolder getAppFolder(GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            DriveId fG = ((n) apiClient.a(Drive.wx)).fG();
            return fG != null ? new q(fG) : null;
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFile getFile(GoogleApiClient apiClient, DriveId id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be provided.");
        } else if (apiClient.isConnected()) {
            return new o(id);
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFolder getFolder(GoogleApiClient apiClient, DriveId id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must be provided.");
        } else if (apiClient.isConnected()) {
            return new q(id);
        } else {
            throw new IllegalStateException("Client must be connected");
        }
    }

    public DriveFolder getRootFolder(GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            return new q(((n) apiClient.a(Drive.wx)).fF());
        }
        throw new IllegalStateException("Client must be connected");
    }

    public PendingResult<ContentsResult> newContents(GoogleApiClient apiClient) {
        return apiClient.a(new g() {
            {
                super();
            }

            protected void a(n nVar) throws RemoteException {
                nVar.fE().a(new CreateContentsRequest(), new f(this));
            }
        });
    }

    public CreateFileActivityBuilder newCreateFileActivityBuilder() {
        return new CreateFileActivityBuilder();
    }

    public OpenFileActivityBuilder newOpenFileActivityBuilder() {
        return new OpenFileActivityBuilder();
    }

    public PendingResult<MetadataBufferResult> query(GoogleApiClient apiClient, Query query) {
        if (query != null) {
            return apiClient.a(new AnonymousClass_1(query));
        }
        throw new IllegalArgumentException("Query must be provided.");
    }

    public PendingResult<Status> requestSync(GoogleApiClient client) {
        return client.b(new l() {
            {
                super();
            }

            protected void a(n nVar) throws RemoteException {
                nVar.fE().a(new al(this));
            }
        });
    }
}