package com.google.android.gms.drive;

import com.google.android.gms.drive.metadata.MetadataField;
import com.google.android.gms.drive.metadata.internal.MetadataBundle;

public final class b extends Metadata {
    private final MetadataBundle ED;

    public b(MetadataBundle metadataBundle) {
        this.ED = metadataBundle;
    }

    protected <T> T a(MetadataField metadataField) {
        return this.ED.a(metadataField);
    }

    public Metadata fB() {
        return new b(MetadataBundle.a(this.ED));
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