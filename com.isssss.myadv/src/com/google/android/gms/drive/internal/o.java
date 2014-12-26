package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi.ContentsResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFile.DownloadProgressListener;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;

public class o extends r implements DriveFile {

    private static class c extends c {
        private final DownloadProgressListener Ft;
        private final com.google.android.gms.common.api.a.d<ContentsResult> wH;

        public c(com.google.android.gms.common.api.a.d<ContentsResult> dVar, DownloadProgressListener downloadProgressListener) {
            this.wH = dVar;
            this.Ft = downloadProgressListener;
        }

        public void a(OnContentsResponse onContentsResponse) throws RemoteException {
            this.wH.b(new a(Status.Bv, onContentsResponse.fI()));
        }

        public void a(OnDownloadProgressResponse onDownloadProgressResponse) throws RemoteException {
            if (this.Ft != null) {
                this.Ft.onProgress(onDownloadProgressResponse.fJ(), onDownloadProgressResponse.fK());
            }
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new a(status, null));
        }
    }

    private abstract class a extends m<Status> {
        private a() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    private abstract class b extends m<Status> {
        private b() {
        }

        public /* synthetic */ Result d(Status status) {
            return f(status);
        }

        public Status f(Status status) {
            return status;
        }
    }

    private abstract class d extends m<ContentsResult> {
        private d() {
        }

        public /* synthetic */ Result d(Status status) {
            return o(status);
        }

        public ContentsResult o(Status status) {
            return new a(status, null);
        }
    }

    class AnonymousClass_1 extends d {
        final /* synthetic */ int Fp;
        final /* synthetic */ DownloadProgressListener Fq;

        AnonymousClass_1(int i, DownloadProgressListener downloadProgressListener) {
            this.Fp = i;
            this.Fq = downloadProgressListener;
            super(null);
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new OpenContentsRequest(o.this.getDriveId(), this.Fp), new c(this, this.Fq));
        }
    }

    class AnonymousClass_2 extends b {
        final /* synthetic */ Contents Fd;

        AnonymousClass_2(Contents contents) {
            this.Fd = contents;
            super(null);
        }

        protected void a(n nVar) throws RemoteException {
            this.Fd.close();
            nVar.fE().a(new CloseContentsRequest(this.Fd, true), new al(this));
        }
    }

    class AnonymousClass_3 extends a {
        final /* synthetic */ Contents Fd;
        final /* synthetic */ MetadataChangeSet Fs;

        AnonymousClass_3(Contents contents, MetadataChangeSet metadataChangeSet) {
            this.Fd = contents;
            this.Fs = metadataChangeSet;
            super(null);
        }

        protected void a(n nVar) throws RemoteException {
            this.Fd.close();
            nVar.fE().a(new CloseContentsAndUpdateMetadataRequest(o.this.Ew, this.Fs.fD(), this.Fd), new al(this));
        }
    }

    public o(DriveId driveId) {
        super(driveId);
    }

    public PendingResult<Status> commitAndCloseContents(GoogleApiClient apiClient, Contents contents) {
        if (contents != null) {
            return apiClient.b(new AnonymousClass_2(contents));
        }
        throw new IllegalArgumentException("Contents must be provided.");
    }

    public PendingResult<Status> commitAndCloseContents(GoogleApiClient apiClient, Contents contents, MetadataChangeSet changeSet) {
        if (contents != null) {
            return apiClient.b(new AnonymousClass_3(contents, changeSet));
        }
        throw new IllegalArgumentException("Contents must be provided.");
    }

    public PendingResult<Status> discardContents(GoogleApiClient apiClient, Contents contents) {
        return Drive.DriveApi.discardContents(apiClient, contents);
    }

    public PendingResult<ContentsResult> openContents(GoogleApiClient apiClient, int mode, DownloadProgressListener listener) {
        if (mode == 268435456 || mode == 536870912 || mode == 805306368) {
            return apiClient.a(new AnonymousClass_1(mode, listener));
        }
        throw new IllegalArgumentException("Invalid mode provided.");
    }
}