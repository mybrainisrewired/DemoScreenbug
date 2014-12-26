package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
    public static final int DEFAULT_ALL = -1;
    public static final int DEFAULT_LIGHTS = 4;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final String EXTRA_INFO_TEXT = "android.infoText";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";
    public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
    public static final String EXTRA_PEOPLE = "android.people";
    public static final String EXTRA_PICTURE = "android.picture";
    public static final String EXTRA_PROGRESS = "android.progress";
    public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
    public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
    public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
    public static final String EXTRA_SMALL_ICON = "android.icon";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_TEXT_LINES = "android.textLines";
    public static final String EXTRA_TITLE = "android.title";
    public static final String EXTRA_TITLE_BIG = "android.title.big";
    public static final int FLAG_AUTO_CANCEL = 16;
    public static final int FLAG_FOREGROUND_SERVICE = 64;
    public static final int FLAG_GROUP_SUMMARY = 512;
    public static final int FLAG_HIGH_PRIORITY = 128;
    public static final int FLAG_INSISTENT = 4;
    public static final int FLAG_LOCAL_ONLY = 256;
    public static final int FLAG_NO_CLEAR = 32;
    public static final int FLAG_ONGOING_EVENT = 2;
    public static final int FLAG_ONLY_ALERT_ONCE = 8;
    public static final int FLAG_SHOW_LIGHTS = 1;
    private static final NotificationCompatImpl IMPL;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;
    public static final int STREAM_DEFAULT = -1;

    public static class Builder {
        ArrayList<android.support.v4.app.NotificationCompat.Action> mActions;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        String mGroupKey;
        boolean mGroupSummary;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        Notification mNotification;
        int mNumber;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        String mSortKey;
        android.support.v4.app.NotificationCompat.Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        boolean mUseChronometer;

        public Builder(Context context) {
            this.mActions = new ArrayList();
            this.mLocalOnly = false;
            this.mNotification = new Notification();
            this.mContext = context;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
        }

        private void setFlag(int mask, boolean value) {
            Notification notification;
            if (value) {
                notification = this.mNotification;
                notification.flags |= mask;
            } else {
                notification = this.mNotification;
                notification.flags &= mask ^ -1;
            }
        }

        public android.support.v4.app.NotificationCompat.Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new android.support.v4.app.NotificationCompat.Action(icon, title, intent));
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder addAction(android.support.v4.app.NotificationCompat.Action action) {
            this.mActions.add(action);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder addExtras(Bundle extras) {
            if (extras != null) {
                if (this.mExtras == null) {
                    this.mExtras = new Bundle(extras);
                } else {
                    this.mExtras.putAll(extras);
                }
            }
            return this;
        }

        public Notification build() {
            return IMPL.build(this);
        }

        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Extender extender) {
            extender.extend(this);
            return this;
        }

        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }

        @Deprecated
        public Notification getNotification() {
            return IMPL.build(this);
        }

        public android.support.v4.app.NotificationCompat.Builder setAutoCancel(boolean autoCancel) {
            setFlag(FLAG_AUTO_CANCEL, autoCancel);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setContent(RemoteViews views) {
            this.mNotification.contentView = views;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setContentInfo(CharSequence info) {
            this.mContentInfo = info;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setContentText(CharSequence text) {
            this.mContentText = text;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setContentTitle(CharSequence title) {
            this.mContentTitle = title;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setDefaults(int defaults) {
            this.mNotification.defaults = defaults;
            if ((defaults & 4) != 0) {
                Notification notification = this.mNotification;
                notification.flags |= 1;
            }
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setDeleteIntent(PendingIntent intent) {
            this.mNotification.deleteIntent = intent;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mFullScreenIntent = intent;
            setFlag(FLAG_HIGH_PRIORITY, highPriority);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setGroup(String groupKey) {
            this.mGroupKey = groupKey;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setGroupSummary(boolean isGroupSummary) {
            this.mGroupSummary = isGroupSummary;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setLargeIcon(Bitmap icon) {
            this.mLargeIcon = icon;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setLights(int argb, int onMs, int offMs) {
            boolean showLights;
            int i = PRIORITY_HIGH;
            this.mNotification.ledARGB = argb;
            this.mNotification.ledOnMS = onMs;
            this.mNotification.ledOffMS = offMs;
            if (this.mNotification.ledOnMS == 0 || this.mNotification.ledOffMS == 0) {
                showLights = false;
            } else {
                showLights = true;
            }
            Notification notification = this.mNotification;
            int i2 = this.mNotification.flags & -2;
            if (!showLights) {
                i = 0;
            }
            notification.flags = i | i2;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setLocalOnly(boolean b) {
            this.mLocalOnly = b;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setNumber(int number) {
            this.mNumber = number;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setOngoing(boolean ongoing) {
            setFlag(PRIORITY_MAX, ongoing);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setPriority(int pri) {
            this.mPriority = pri;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mProgressMax = max;
            this.mProgress = progress;
            this.mProgressIndeterminate = indeterminate;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSmallIcon(int icon) {
            this.mNotification.icon = icon;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSmallIcon(int icon, int level) {
            this.mNotification.icon = icon;
            this.mNotification.iconLevel = level;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSortKey(String sortKey) {
            this.mSortKey = sortKey;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSound(Uri sound) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = -1;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSound(Uri sound, int streamType) {
            this.mNotification.sound = sound;
            this.mNotification.audioStreamType = streamType;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setStyle(android.support.v4.app.NotificationCompat.Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                if (this.mStyle != null) {
                    this.mStyle.setBuilder(this);
                }
            }
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setSubText(CharSequence text) {
            this.mSubText = text;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setTicker(CharSequence tickerText) {
            this.mNotification.tickerText = tickerText;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setTicker(CharSequence tickerText, RemoteViews views) {
            this.mNotification.tickerText = tickerText;
            this.mTickerView = views;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setUsesChronometer(boolean b) {
            this.mUseChronometer = b;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setVibrate(long[] pattern) {
            this.mNotification.vibrate = pattern;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setWhen(long when) {
            this.mNotification.when = when;
            return this;
        }
    }

    public static interface Extender {
        android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Builder builder);
    }

    static interface NotificationCompatImpl {
        Notification build(android.support.v4.app.NotificationCompat.Builder builder);

        android.support.v4.app.NotificationCompat.Action getAction(Notification notification, int i);

        int getActionCount(Notification notification);

        android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> arrayList);

        Bundle getExtras(Notification notification);

        String getGroup(Notification notification);

        boolean getLocalOnly(Notification notification);

        ArrayList<Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actionArr);

        String getSortKey(Notification notification);

        boolean isGroupSummary(Notification notification);
    }

    public static abstract class Style {
        CharSequence mBigContentTitle;
        android.support.v4.app.NotificationCompat.Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet;

        public Style() {
            this.mSummaryTextSet = false;
        }

        public Notification build() {
            return this.mBuilder != null ? this.mBuilder.build() : null;
        }

        public void setBuilder(android.support.v4.app.NotificationCompat.Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                if (this.mBuilder != null) {
                    this.mBuilder.setStyle(this);
                }
            }
        }
    }

    public static class Action extends android.support.v4.app.NotificationCompatBase.Action {
        public static final Factory FACTORY;
        public PendingIntent actionIntent;
        public int icon;
        private final Bundle mExtras;
        private final RemoteInput[] mRemoteInputs;
        public CharSequence title;

        public static final class Builder {
            private final Bundle mExtras;
            private final int mIcon;
            private final PendingIntent mIntent;
            private ArrayList<RemoteInput> mRemoteInputs;
            private final CharSequence mTitle;

            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle());
            }

            private Builder(int icon, CharSequence title, PendingIntent intent, Bundle extras) {
                this.mIcon = icon;
                this.mTitle = title;
                this.mIntent = intent;
                this.mExtras = extras;
            }

            public Builder(android.support.v4.app.NotificationCompat.Action action) {
                this(action.icon, action.title, action.actionIntent, new Bundle(action.mExtras));
            }

            public android.support.v4.app.NotificationCompat.Action.Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public android.support.v4.app.NotificationCompat.Action.Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList();
                }
                this.mRemoteInputs.add(remoteInput);
                return this;
            }

            public android.support.v4.app.NotificationCompat.Action build() {
                RemoteInput[] remoteInputs;
                if (this.mRemoteInputs != null) {
                    remoteInputs = (RemoteInput[]) this.mRemoteInputs.toArray(new RemoteInput[this.mRemoteInputs.size()]);
                } else {
                    remoteInputs = null;
                }
                return new android.support.v4.app.NotificationCompat.Action(this.mTitle, this.mIntent, this.mExtras, remoteInputs, null);
            }

            public android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Extender extender) {
                extender.extend(this);
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }
        }

        public static interface Extender {
            android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Builder builder);
        }

        public static final class WearableExtender implements android.support.v4.app.NotificationCompat.Action.Extender {
            private static final int DEFAULT_FLAGS = 1;
            private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
            private static final int FLAG_AVAILABLE_OFFLINE = 1;
            private static final String KEY_FLAGS = "flags";
            private int mFlags;

            public WearableExtender() {
                this.mFlags = 1;
            }

            public WearableExtender(android.support.v4.app.NotificationCompat.Action action) {
                this.mFlags = 1;
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    this.mFlags = wearableBundle.getInt(KEY_FLAGS, FLAG_AVAILABLE_OFFLINE);
                }
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= mask ^ -1;
                }
            }

            public android.support.v4.app.NotificationCompat.Action.WearableExtender clone() {
                android.support.v4.app.NotificationCompat.Action.WearableExtender that = new android.support.v4.app.NotificationCompat.Action.WearableExtender();
                that.mFlags = this.mFlags;
                return that;
            }

            public android.support.v4.app.NotificationCompat.Action.Builder extend(android.support.v4.app.NotificationCompat.Action.Builder builder) {
                Bundle wearableBundle = new Bundle();
                if (this.mFlags != 1) {
                    wearableBundle.putInt(KEY_FLAGS, this.mFlags);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            public android.support.v4.app.NotificationCompat.Action.WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(FLAG_AVAILABLE_OFFLINE, availableOffline);
                return this;
            }
        }

        static {
            FACTORY = new Factory() {
                public android.support.v4.app.NotificationCompat.Action build(int icon, CharSequence title, PendingIntent actionIntent, Bundle extras, RemoteInput[] remoteInputs) {
                    return new android.support.v4.app.NotificationCompat.Action(title, actionIntent, extras, (RemoteInput[]) remoteInputs, null);
                }

                public android.support.v4.app.NotificationCompat.Action[] newArray(int length) {
                    return new android.support.v4.app.NotificationCompat.Action[length];
                }
            };
        }

        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(icon, title, intent, new Bundle(), null);
        }

        private Action(int icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs) {
            this.icon = icon;
            this.title = title;
            this.actionIntent = intent;
            if (extras == null) {
                extras = new Bundle();
            }
            this.mExtras = extras;
            this.mRemoteInputs = remoteInputs;
        }

        protected PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        protected int getIcon() {
            return this.icon;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        protected CharSequence getTitle() {
            return this.title;
        }
    }

    public static class BigPictureStyle extends android.support.v4.app.NotificationCompat.Style {
        Bitmap mBigLargeIcon;
        boolean mBigLargeIconSet;
        Bitmap mPicture;

        public BigPictureStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        public android.support.v4.app.NotificationCompat.BigPictureStyle bigLargeIcon(Bitmap b) {
            this.mBigLargeIcon = b;
            this.mBigLargeIconSet = true;
            return this;
        }

        public android.support.v4.app.NotificationCompat.BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        public android.support.v4.app.NotificationCompat.BigPictureStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
            return this;
        }

        public android.support.v4.app.NotificationCompat.BigPictureStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class BigTextStyle extends android.support.v4.app.NotificationCompat.Style {
        CharSequence mBigText;

        public BigTextStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        public android.support.v4.app.NotificationCompat.BigTextStyle bigText(CharSequence cs) {
            this.mBigText = cs;
            return this;
        }

        public android.support.v4.app.NotificationCompat.BigTextStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
            return this;
        }

        public android.support.v4.app.NotificationCompat.BigTextStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    public static class InboxStyle extends android.support.v4.app.NotificationCompat.Style {
        ArrayList<CharSequence> mTexts;

        public InboxStyle() {
            this.mTexts = new ArrayList();
        }

        public InboxStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            this.mTexts = new ArrayList();
            setBuilder(builder);
        }

        public android.support.v4.app.NotificationCompat.InboxStyle addLine(CharSequence cs) {
            this.mTexts.add(cs);
            return this;
        }

        public android.support.v4.app.NotificationCompat.InboxStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = title;
            return this;
        }

        public android.support.v4.app.NotificationCompat.InboxStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = cs;
            this.mSummaryTextSet = true;
            return this;
        }
    }

    static class NotificationCompatImplBase implements NotificationCompatImpl {
        NotificationCompatImplBase() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            Notification result = b.mNotification;
            result.setLatestEventInfo(b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent);
            if (b.mPriority > 0) {
                result.flags |= 128;
            }
            return result;
        }

        public android.support.v4.app.NotificationCompat.Action getAction(Notification n, int actionIndex) {
            return null;
        }

        public int getActionCount(Notification n) {
            return PRIORITY_DEFAULT;
        }

        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
            return null;
        }

        public Bundle getExtras(Notification n) {
            return null;
        }

        public String getGroup(Notification n) {
            return null;
        }

        public boolean getLocalOnly(Notification n) {
            return false;
        }

        public ArrayList<Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return null;
        }

        public String getSortKey(Notification n) {
            return null;
        }

        public boolean isGroupSummary(Notification n) {
            return false;
        }
    }

    public static final class WearableExtender implements android.support.v4.app.NotificationCompat.Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
        private static final int DEFAULT_FLAGS = 1;
        private static final int DEFAULT_GRAVITY = 80;
        private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
        private static final int FLAG_HINT_HIDE_ICON = 2;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
        private static final int FLAG_START_SCROLL_BOTTOM = 8;
        private static final String KEY_ACTIONS = "actions";
        private static final String KEY_BACKGROUND = "background";
        private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
        private static final String KEY_CONTENT_ICON = "contentIcon";
        private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
        private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
        private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
        private static final String KEY_DISPLAY_INTENT = "displayIntent";
        private static final String KEY_FLAGS = "flags";
        private static final String KEY_GRAVITY = "gravity";
        private static final String KEY_PAGES = "pages";
        public static final int SIZE_DEFAULT = 0;
        public static final int SIZE_FULL_SCREEN = 5;
        public static final int SIZE_LARGE = 4;
        public static final int SIZE_MEDIUM = 3;
        public static final int SIZE_SMALL = 2;
        public static final int SIZE_XSMALL = 1;
        public static final int UNSET_ACTION_INDEX = -1;
        private ArrayList<android.support.v4.app.NotificationCompat.Action> mActions;
        private Bitmap mBackground;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private ArrayList<Notification> mPages;

        public WearableExtender() {
            this.mActions = new ArrayList();
            this.mFlags = 1;
            this.mPages = new ArrayList();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
        }

        public WearableExtender(Notification notif) {
            this.mActions = new ArrayList();
            this.mFlags = 1;
            this.mPages = new ArrayList();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
            Bundle extras = NotificationCompat.getExtras(notif);
            Bundle wearableBundle = extras != null ? extras.getBundle(EXTRA_WEARABLE_EXTENSIONS) : null;
            if (wearableBundle != null) {
                android.support.v4.app.NotificationCompat.Action[] actions = IMPL.getActionsFromParcelableArrayList(wearableBundle.getParcelableArrayList(KEY_ACTIONS));
                if (actions != null) {
                    Collections.addAll(this.mActions, actions);
                }
                this.mFlags = wearableBundle.getInt(KEY_FLAGS, SIZE_XSMALL);
                this.mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] pages = NotificationCompat.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(this.mPages, pages);
                }
                this.mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, DEFAULT_CONTENT_ICON_GRAVITY);
                this.mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, UNSET_ACTION_INDEX);
                this.mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, SIZE_DEFAULT);
                this.mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = wearableBundle.getInt(KEY_GRAVITY, DEFAULT_GRAVITY);
            }
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= mask ^ -1;
            }
        }

        public android.support.v4.app.NotificationCompat.WearableExtender addAction(android.support.v4.app.NotificationCompat.Action action) {
            this.mActions.add(action);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender addActions(List<android.support.v4.app.NotificationCompat.Action> actions) {
            this.mActions.addAll(actions);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender addPages(List<Notification> pages) {
            this.mPages.addAll(pages);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender clone() {
            android.support.v4.app.NotificationCompat.WearableExtender that = new android.support.v4.app.NotificationCompat.WearableExtender();
            that.mActions = new ArrayList(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            return that;
        }

        public android.support.v4.app.NotificationCompat.Builder extend(android.support.v4.app.NotificationCompat.Builder builder) {
            Bundle wearableBundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                wearableBundle.putParcelableArrayList(KEY_ACTIONS, IMPL.getParcelableArrayListForActions((android.support.v4.app.NotificationCompat.Action[]) this.mActions.toArray(new android.support.v4.app.NotificationCompat.Action[this.mActions.size()])));
            }
            if (this.mFlags != 1) {
                wearableBundle.putInt(KEY_FLAGS, this.mFlags);
            }
            if (this.mDisplayIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, this.mDisplayIntent);
            }
            if (!this.mPages.isEmpty()) {
                wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) this.mPages.toArray(new Notification[this.mPages.size()]));
            }
            if (this.mBackground != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, this.mBackground);
            }
            if (this.mContentIcon != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, this.mContentIcon);
            }
            if (this.mContentIconGravity != 8388613) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, this.mContentIconGravity);
            }
            if (this.mContentActionIndex != -1) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, this.mContentActionIndex);
            }
            if (this.mCustomSizePreset != 0) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, this.mCustomSizePreset);
            }
            if (this.mCustomContentHeight != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, this.mCustomContentHeight);
            }
            if (this.mGravity != 80) {
                wearableBundle.putInt(KEY_GRAVITY, this.mGravity);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        public List<android.support.v4.app.NotificationCompat.Action> getActions() {
            return this.mActions;
        }

        public Bitmap getBackground() {
            return this.mBackground;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        public int getContentIcon() {
            return this.mContentIcon;
        }

        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        public int getGravity() {
            return this.mGravity;
        }

        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        public List<Notification> getPages() {
            return this.mPages;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(SIZE_XSMALL, contentIntentAvailableOffline);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(SIZE_SMALL, hintHideIcon);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(SIZE_LARGE, hintShowBackgroundOnly);
            return this;
        }

        public android.support.v4.app.NotificationCompat.WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(FLAG_START_SCROLL_BOTTOM, startScrollBottom);
            return this;
        }
    }

    static class NotificationCompatImplGingerbread extends NotificationCompatImplBase {
        NotificationCompatImplGingerbread() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            Notification result = b.mNotification;
            result.setLatestEventInfo(b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent);
            result = NotificationCompatGingerbread.add(result, b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent, b.mFullScreenIntent);
            if (b.mPriority > 0) {
                result.flags |= 128;
            }
            return result;
        }
    }

    static class NotificationCompatImplHoneycomb extends NotificationCompatImplBase {
        NotificationCompatImplHoneycomb() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            return NotificationCompatHoneycomb.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon);
        }
    }

    static class NotificationCompatImplIceCreamSandwich extends NotificationCompatImplBase {
        NotificationCompatImplIceCreamSandwich() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            return NotificationCompatIceCreamSandwich.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
        }
    }

    static class NotificationCompatImplJellybean extends NotificationCompatImplBase {
        NotificationCompatImplJellybean() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            android.support.v4.app.NotificationCompatJellybean.Builder builder = new android.support.v4.app.NotificationCompatJellybean.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
            NotificationCompat.addActionsToBuilder(builder, b.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            return builder.build();
        }

        public android.support.v4.app.NotificationCompat.Action getAction(Notification n, int actionIndex) {
            return (android.support.v4.app.NotificationCompat.Action) NotificationCompatJellybean.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
        }

        public int getActionCount(Notification n) {
            return NotificationCompatJellybean.getActionCount(n);
        }

        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
            return (android.support.v4.app.NotificationCompat.Action[]) NotificationCompatJellybean.getActionsFromParcelableArrayList(parcelables, android.support.v4.app.NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
        }

        public Bundle getExtras(Notification n) {
            return NotificationCompatJellybean.getExtras(n);
        }

        public String getGroup(Notification n) {
            return NotificationCompatJellybean.getGroup(n);
        }

        public boolean getLocalOnly(Notification n) {
            return NotificationCompatJellybean.getLocalOnly(n);
        }

        public ArrayList<Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return NotificationCompatJellybean.getParcelableArrayListForActions(actions);
        }

        public String getSortKey(Notification n) {
            return NotificationCompatJellybean.getSortKey(n);
        }

        public boolean isGroupSummary(Notification n) {
            return NotificationCompatJellybean.isGroupSummary(n);
        }
    }

    static class NotificationCompatImplKitKat extends NotificationCompatImplJellybean {
        NotificationCompatImplKitKat() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            android.support.v4.app.NotificationCompatKitKat.Builder builder = new android.support.v4.app.NotificationCompatKitKat.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
            NotificationCompat.addActionsToBuilder(builder, b.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            return builder.build();
        }

        public android.support.v4.app.NotificationCompat.Action getAction(Notification n, int actionIndex) {
            return (android.support.v4.app.NotificationCompat.Action) NotificationCompatKitKat.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
        }

        public int getActionCount(Notification n) {
            return NotificationCompatKitKat.getActionCount(n);
        }

        public Bundle getExtras(Notification n) {
            return NotificationCompatKitKat.getExtras(n);
        }

        public String getGroup(Notification n) {
            return NotificationCompatKitKat.getGroup(n);
        }

        public boolean getLocalOnly(Notification n) {
            return NotificationCompatKitKat.getLocalOnly(n);
        }

        public String getSortKey(Notification n) {
            return NotificationCompatKitKat.getSortKey(n);
        }

        public boolean isGroupSummary(Notification n) {
            return NotificationCompatKitKat.isGroupSummary(n);
        }
    }

    static class NotificationCompatImplApi20 extends NotificationCompatImplKitKat {
        NotificationCompatImplApi20() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            android.support.v4.app.NotificationCompatApi20.Builder builder = new android.support.v4.app.NotificationCompatApi20.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
            NotificationCompat.addActionsToBuilder(builder, b.mActions);
            NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
            return builder.build();
        }

        public android.support.v4.app.NotificationCompat.Action getAction(Notification n, int actionIndex) {
            return (android.support.v4.app.NotificationCompat.Action) NotificationCompatApi20.getAction(n, actionIndex, android.support.v4.app.NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
        }

        public android.support.v4.app.NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
            return (android.support.v4.app.NotificationCompat.Action[]) NotificationCompatApi20.getActionsFromParcelableArrayList(parcelables, android.support.v4.app.NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
        }

        public String getGroup(Notification n) {
            return NotificationCompatApi20.getGroup(n);
        }

        public boolean getLocalOnly(Notification n) {
            return NotificationCompatApi20.getLocalOnly(n);
        }

        public ArrayList<Parcelable> getParcelableArrayListForActions(android.support.v4.app.NotificationCompat.Action[] actions) {
            return NotificationCompatApi20.getParcelableArrayListForActions(actions);
        }

        public String getSortKey(Notification n) {
            return NotificationCompatApi20.getSortKey(n);
        }

        public boolean isGroupSummary(Notification n) {
            return NotificationCompatApi20.isGroupSummary(n);
        }
    }

    static {
        if (VERSION.SDK_INT >= 20) {
            IMPL = new NotificationCompatImplApi20();
        } else if (VERSION.SDK_INT >= 19) {
            IMPL = new NotificationCompatImplKitKat();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new NotificationCompatImplJellybean();
        } else if (VERSION.SDK_INT >= 14) {
            IMPL = new NotificationCompatImplIceCreamSandwich();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new NotificationCompatImplHoneycomb();
        } else if (VERSION.SDK_INT >= 9) {
            IMPL = new NotificationCompatImplGingerbread();
        } else {
            IMPL = new NotificationCompatImplBase();
        }
    }

    private static void addActionsToBuilder(NotificationBuilderWithActions builder, ArrayList<Action> actions) {
        Iterator i$ = actions.iterator();
        while (i$.hasNext()) {
            builder.addAction((Action) i$.next());
        }
    }

    private static void addStyleToBuilderJellybean(NotificationBuilderWithBuilderAccessor builder, Style style) {
        if (style == null) {
            return;
        }
        if (style instanceof BigTextStyle) {
            BigTextStyle bigTextStyle = (BigTextStyle) style;
            NotificationCompatJellybean.addBigTextStyle(builder, bigTextStyle.mBigContentTitle, bigTextStyle.mSummaryTextSet, bigTextStyle.mSummaryText, bigTextStyle.mBigText);
        } else if (style instanceof InboxStyle) {
            InboxStyle inboxStyle = (InboxStyle) style;
            NotificationCompatJellybean.addInboxStyle(builder, inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
        } else if (style instanceof BigPictureStyle) {
            BigPictureStyle bigPictureStyle = (BigPictureStyle) style;
            NotificationCompatJellybean.addBigPictureStyle(builder, bigPictureStyle.mBigContentTitle, bigPictureStyle.mSummaryTextSet, bigPictureStyle.mSummaryText, bigPictureStyle.mPicture, bigPictureStyle.mBigLargeIcon, bigPictureStyle.mBigLargeIconSet);
        }
    }

    public static Action getAction(Notification notif, int actionIndex) {
        return IMPL.getAction(notif, actionIndex);
    }

    public static int getActionCount(Notification notif) {
        return IMPL.getActionCount(notif);
    }

    public static Bundle getExtras(Notification notif) {
        return IMPL.getExtras(notif);
    }

    public static String getGroup(Notification notif) {
        return IMPL.getGroup(notif);
    }

    public static boolean getLocalOnly(Notification notif) {
        return IMPL.getLocalOnly(notif);
    }

    private static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if (array instanceof Notification[] || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = new Notification[array.length];
        int i = PRIORITY_DEFAULT;
        while (i < array.length) {
            typedArray[i] = (Notification) array[i];
            i++;
        }
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    public static String getSortKey(Notification notif) {
        return IMPL.getSortKey(notif);
    }

    public static boolean isGroupSummary(Notification notif) {
        return IMPL.isGroupSummary(notif);
    }
}