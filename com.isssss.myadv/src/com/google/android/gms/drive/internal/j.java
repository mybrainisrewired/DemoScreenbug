package com.google.android.gms.drive.internal;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.metadata.MetadataField;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;

public final class j extends Metadata {
    private final MetadataBundle ED;

    public j(MetadataBundle metadataBundle) {
        this.ED = metadataBundle;
    }

    protected <T> T a(MetadataField metadataField) {
        return this.ED.a(metadataField);
    }

    public Metadata fB() {
        return new j(MetadataBundle.a(this.ED));
    }

    public /* synthetic */ Object freeze() {
        return fB();
    }

    public boolean isDataValid() {
        return this.ED != null;
    }

    public String toString() {
        return "Metadata [mImpl=" + this.ED + "]";
    }
}