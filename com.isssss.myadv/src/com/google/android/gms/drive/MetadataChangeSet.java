package com.google.android.gms.drive;

import com.google.android.gms.drive.metadata.internal.MetadataBundle;
import com.google.android.gms.internal.gs;
import com.google.android.gms.internal.gt;
import java.util.Date;

public final class MetadataChangeSet {
    private final MetadataBundle EP;

    public static class Builder {
        private final MetadataBundle EP;

        public Builder() {
            this.EP = MetadataBundle.fT();
        }

        public MetadataChangeSet build() {
            return new MetadataChangeSet(null);
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setDescription(String description) {
            this.EP.b(gs.FT, description);
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setIndexableText(String text) {
            this.EP.b(gs.FY, text);
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setLastViewedByMeDate(Date date) {
            this.EP.b(gt.Gt, date);
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setMimeType(String mimeType) {
            this.EP.b(gs.Gh, mimeType);
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setPinned(boolean pinned) {
            this.EP.b(gs.Gc, Boolean.valueOf(pinned));
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setStarred(boolean starred) {
            this.EP.b(gs.Gm, Boolean.valueOf(starred));
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setTitle(String title) {
            this.EP.b(gs.Go, title);
            return this;
        }

        public com.google.android.gms.drive.MetadataChangeSet.Builder setViewed(boolean viewed) {
            this.EP.b(gs.Gg, Boolean.valueOf(viewed));
            return this;
        }
    }

    private MetadataChangeSet(MetadataBundle bag) {
        this.EP = MetadataBundle.a(bag);
    }

    public MetadataBundle fD() {
        return this.EP;
    }

    public String getDescription() {
        return (String) this.EP.a(gs.FT);
    }

    public String getIndexableText() {
        return (String) this.EP.a(gs.FY);
    }

    public Date getLastViewedByMeDate() {
        return (Date) this.EP.a(gt.Gt);
    }

    public String getMimeType() {
        return (String) this.EP.a(gs.Gh);
    }

    public String getTitle() {
        return (String) this.EP.a(gs.Go);
    }

    public Boolean isPinned() {
        return (Boolean) this.EP.a(gs.Gc);
    }

    public Boolean isStarred() {
        return (Boolean) this.EP.a(gs.Gm);
    }

    public Boolean isViewed() {
        return (Boolean) this.EP.a(gs.Gg);
    }
}