package com.google.android.gms.drive.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveFolder.DriveFileResult;
import com.google.android.gms.drive.DriveFolder.DriveFolderResult;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.Query.Builder;
import com.google.android.gms.drive.query.SearchableField;

public class q extends r implements DriveFolder {

    private static class d implements DriveFileResult {
        private final DriveFile Fv;
        private final Status wJ;

        public d(Status status, DriveFile driveFile) {
            this.wJ = status;
            this.Fv = driveFile;
        }

        public DriveFile getDriveFile() {
            return this.Fv;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    private static class e implements DriveFolderResult {
        private final DriveFolder Fw;
        private final Status wJ;

        public e(Status status, DriveFolder driveFolder) {
            this.wJ = status;
            this.Fw = driveFolder;
        }

        public DriveFolder getDriveFolder() {
            return this.Fw;
        }

        public Status getStatus() {
            return this.wJ;
        }
    }

    private static class a extends c {
        private final com.google.android.gms.common.api.a.d<DriveFileResult> wH;

        public a(com.google.android.gms.common.api.a.d<DriveFileResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnDriveIdResponse onDriveIdResponse) throws RemoteException {
            this.wH.b(new d(Status.Bv, new o(onDriveIdResponse.getDriveId())));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new d(status, null));
        }
    }

    private static class b extends c {
        private final com.google.android.gms.common.api.a.d<DriveFolderResult> wH;

        public b(com.google.android.gms.common.api.a.d<DriveFolderResult> dVar) {
            this.wH = dVar;
        }

        public void a(OnDriveIdResponse onDriveIdResponse) throws RemoteException {
            this.wH.b(new e(Status.Bv, new q(onDriveIdResponse.getDriveId())));
        }

        public void m(Status status) throws RemoteException {
            this.wH.b(new e(status, null));
        }
    }

    class AnonymousClass_1 extends m<DriveFileResult> {
        final /* synthetic */ Contents Fd;
        final /* synthetic */ MetadataChangeSet Fs;

        AnonymousClass_1(Contents contents, MetadataChangeSet metadataChangeSet) {
            this.Fd = contents;
            this.Fs = metadataChangeSet;
        }

        protected void a(n nVar) throws RemoteException {
            this.Fd.close();
            nVar.fE().a(new CreateFileRequest(q.this.getDriveId(), this.Fs.fD(), this.Fd), new a(this));
        }

        public /* synthetic */ Result d(Status status) {
            return q(status);
        }

        public DriveFileResult q(Status status) {
            return new d(status, null);
        }
    }

    private abstract class c extends m<DriveFolderResult> {
        private c() {
        }

        public /* synthetic */ Result d(Status status) {
            return r(status);
        }

        public DriveFolderResult r(Status status) {
            return new e(status, null);
        }
    }

    class AnonymousClass_2 extends c {
        final /* synthetic */ MetadataChangeSet Fs;

        AnonymousClass_2(MetadataChangeSet metadataChangeSet) {
            this.Fs = metadataChangeSet;
            super(null);
        }

        protected void a(n nVar) throws RemoteException {
            nVar.fE().a(new CreateFolderRequest(q.this.getDriveId(), this.Fs.fD()), new b(this));
        }
    }

    public q(DriveId driveId) {
        super(driveId);
    }

    public PendingResult<DriveFileResult> createFile(GoogleApiClient apiClient, MetadataChangeSet changeSet, Contents contents) {
        if (changeSet == null) {
            throw new IllegalArgumentException("MetatadataChangeSet must be provided.");
        } else if (contents == null) {
            throw new IllegalArgumentException("Contents must be provided.");
        } else if (!DriveFolder.MIME_TYPE.equals(changeSet.getMimeType())) {
            return apiClient.b(new AnonymousClass_1(contents, changeSet));
        } else {
            throw new IllegalArgumentException("May not create folders (mimetype: application/vnd.google-apps.folder) using this method. Use DriveFolder.createFolder() instead.");
        }
    }

    public PendingResult<DriveFolderResult> createFolder(GoogleApiClient apiClient, MetadataChangeSet changeSet) {
        if (changeSet == null) {
            throw new IllegalArgumentException("MetatadataChangeSet must be provided.");
        } else if (changeSet.getMimeType() == null || changeSet.getMimeType().equals(DriveFolder.MIME_TYPE)) {
            return apiClient.b(new AnonymousClass_2(changeSet));
        } else {
            throw new IllegalArgumentException("The mimetype must be of type application/vnd.google-apps.folder");
        }
    }

    public PendingResult<MetadataBufferResult> listChildren(GoogleApiClient apiClient) {
        return queryChildren(apiClient, null);
    }

    public PendingResult<MetadataBufferResult> queryChildren(GoogleApiClient apiClient, Query query) {
        Builder addFilter = new Builder().addFilter(Filters.in(SearchableField.PARENTS, getDriveId()));
        if (query != null) {
            if (query.getFilter() != null) {
                addFilter.addFilter(query.getFilter());
            }
            addFilter.setPageToken(query.getPageToken());
            addFilter.a(query.fV());
        }
        return new l().query(apiClient, addFilter.build());
    }
}