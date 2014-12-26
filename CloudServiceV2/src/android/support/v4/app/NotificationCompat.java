package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.Type;

public class NotificationCompat {
    public static final int FLAG_HIGH_PRIORITY = 128;
    private static final NotificationCompatImpl IMPL;
    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MAX = 2;
    public static final int PRIORITY_MIN = -2;

    public static class Action {
        public PendingIntent actionIntent;
        public int icon;
        public CharSequence title;

        public Action(int icon_, CharSequence title_, PendingIntent intent_) {
            this.icon = icon_;
            this.title = title_;
            this.actionIntent = intent_;
        }
    }

    public static class Builder {
        ArrayList<android.support.v4.app.NotificationCompat.Action> mActions;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        Context mContext;
        PendingIntent mFullScreenIntent;
        Bitmap mLargeIcon;
        Notification mNotification;
        int mNumber;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        android.support.v4.app.NotificationCompat.Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        boolean mUseChronometer;

        public Builder(Context context) {
            this.mActions = new ArrayList();
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

        public Notification build() {
            return IMPL.build(this);
        }

        @Deprecated
        public Notification getNotification() {
            return IMPL.build(this);
        }

        public android.support.v4.app.NotificationCompat.Builder setAutoCancel(boolean autoCancel) {
            setFlag(Segment.TOKENS_PER_SEGMENT, autoCancel);
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

        public android.support.v4.app.NotificationCompat.Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mFullScreenIntent = intent;
            setFlag(FLAG_HIGH_PRIORITY, highPriority);
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

        public android.support.v4.app.NotificationCompat.Builder setNumber(int number) {
            this.mNumber = number;
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setOngoing(boolean ongoing) {
            setFlag(PRIORITY_MAX, ongoing);
            return this;
        }

        public android.support.v4.app.NotificationCompat.Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(Type.DOUBLE, onlyAlertOnce);
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

    static interface NotificationCompatImpl {
        Notification build(android.support.v4.app.NotificationCompat.Builder builder);
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

    public static class BigPictureStyle extends android.support.v4.app.NotificationCompat.Style {
        Bitmap mPicture;

        public BigPictureStyle(android.support.v4.app.NotificationCompat.Builder builder) {
            setBuilder(builder);
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
    }

    static class NotificationCompatImplHoneycomb implements NotificationCompatImpl {
        NotificationCompatImplHoneycomb() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            return NotificationCompatHoneycomb.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon);
        }
    }

    static class NotificationCompatImplIceCreamSandwich implements NotificationCompatImpl {
        NotificationCompatImplIceCreamSandwich() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            return NotificationCompatIceCreamSandwich.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
        }
    }

    static class NotificationCompatImplJellybean implements NotificationCompatImpl {
        NotificationCompatImplJellybean() {
        }

        public Notification build(android.support.v4.app.NotificationCompat.Builder b) {
            NotificationCompatJellybean jbBuilder = new NotificationCompatJellybean(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText);
            Iterator i$ = b.mActions.iterator();
            while (i$.hasNext()) {
                android.support.v4.app.NotificationCompat.Action action = (android.support.v4.app.NotificationCompat.Action) i$.next();
                jbBuilder.addAction(action.icon, action.title, action.actionIntent);
            }
            if (b.mStyle != null) {
                if (b.mStyle instanceof android.support.v4.app.NotificationCompat.BigTextStyle) {
                    android.support.v4.app.NotificationCompat.BigTextStyle style = (android.support.v4.app.NotificationCompat.BigTextStyle) b.mStyle;
                    jbBuilder.addBigTextStyle(style.mBigContentTitle, style.mSummaryTextSet, style.mSummaryText, style.mBigText);
                } else if (b.mStyle instanceof android.support.v4.app.NotificationCompat.InboxStyle) {
                    android.support.v4.app.NotificationCompat.InboxStyle style2 = (android.support.v4.app.NotificationCompat.InboxStyle) b.mStyle;
                    jbBuilder.addInboxStyle(style2.mBigContentTitle, style2.mSummaryTextSet, style2.mSummaryText, style2.mTexts);
                } else if (b.mStyle instanceof android.support.v4.app.NotificationCompat.BigPictureStyle) {
                    android.support.v4.app.NotificationCompat.BigPictureStyle style3 = (android.support.v4.app.NotificationCompat.BigPictureStyle) b.mStyle;
                    jbBuilder.addBigPictureStyle(style3.mBigContentTitle, style3.mSummaryTextSet, style3.mSummaryText, style3.mPicture);
                }
            }
            return jbBuilder.build();
        }
    }

    static {
        if (VERSION.SDK_INT >= 16) {
            IMPL = new NotificationCompatImplJellybean();
        } else if (VERSION.SDK_INT >= 13) {
            IMPL = new NotificationCompatImplIceCreamSandwich();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new NotificationCompatImplHoneycomb();
        } else {
            IMPL = new NotificationCompatImplBase();
        }
    }
}