package android.support.v4.app;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;

class NotificationCompatBase {

    public static abstract class Action {

        public static interface Factory {
            android.support.v4.app.NotificationCompatBase.Action build(int i, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInput[] remoteInputArr);

            android.support.v4.app.NotificationCompatBase.Action[] newArray(int i);
        }

        protected abstract PendingIntent getActionIntent();

        protected abstract Bundle getExtras();

        protected abstract int getIcon();

        protected abstract RemoteInput[] getRemoteInputs();

        protected abstract CharSequence getTitle();
    }

    NotificationCompatBase() {
    }
}