package com.google.android.gms.games.achievement;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.b;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerRef;
import com.google.android.gms.internal.fb;
import com.google.android.gms.internal.fo;
import com.google.android.gms.internal.fo.a;
import com.inmobi.commons.analytics.db.AnalyticsSQLiteHelper;
import com.isssss.myadv.dao.BannerInfoTable;

public final class AchievementRef extends b implements Achievement {
    AchievementRef(DataHolder holder, int dataRow) {
        super(holder, dataRow);
    }

    public String getAchievementId() {
        return getString("external_achievement_id");
    }

    public int getCurrentSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        return getInteger("current_steps");
    }

    public String getDescription() {
        return getString(BannerInfoTable.COLUMN_DESCRIPTION);
    }

    public void getDescription(CharArrayBuffer dataOut) {
        a(BannerInfoTable.COLUMN_DESCRIPTION, dataOut);
    }

    public String getFormattedCurrentSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        return getString("formatted_current_steps");
    }

    public void getFormattedCurrentSteps(CharArrayBuffer dataOut) {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        a("formatted_current_steps", dataOut);
    }

    public String getFormattedTotalSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        return getString("formatted_total_steps");
    }

    public void getFormattedTotalSteps(CharArrayBuffer dataOut) {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        a("formatted_total_steps", dataOut);
    }

    public long getLastUpdatedTimestamp() {
        return getLong("last_updated_timestamp");
    }

    public String getName() {
        return getString("name");
    }

    public void getName(CharArrayBuffer dataOut) {
        a("name", dataOut);
    }

    public Player getPlayer() {
        return new PlayerRef(this.BB, this.BD);
    }

    public Uri getRevealedImageUri() {
        return ah("revealed_icon_image_uri");
    }

    public String getRevealedImageUrl() {
        return getString("revealed_icon_image_url");
    }

    public int getState() {
        return getInteger("state");
    }

    public int getTotalSteps() {
        boolean z = true;
        if (getType() != 1) {
            z = false;
        }
        fb.x(z);
        return getInteger("total_steps");
    }

    public int getType() {
        return getInteger(AnalyticsSQLiteHelper.EVENT_LIST_TYPE);
    }

    public Uri getUnlockedImageUri() {
        return ah("unlocked_icon_image_uri");
    }

    public String getUnlockedImageUrl() {
        return getString("unlocked_icon_image_url");
    }

    public String toString() {
        a a = fo.e(this).a("AchievementId", getAchievementId()).a("Type", Integer.valueOf(getType())).a("Name", getName()).a("Description", getDescription()).a("UnlockedImageUri", getUnlockedImageUri()).a("UnlockedImageUrl", getUnlockedImageUrl()).a("RevealedImageUri", getRevealedImageUri()).a("RevealedImageUrl", getRevealedImageUrl()).a("Player", getPlayer()).a("LastUpdatedTimeStamp", Long.valueOf(getLastUpdatedTimestamp()));
        if (getType() == 1) {
            a.a("CurrentSteps", Integer.valueOf(getCurrentSteps()));
            a.a("TotalSteps", Integer.valueOf(getTotalSteps()));
        }
        return a.toString();
    }
}