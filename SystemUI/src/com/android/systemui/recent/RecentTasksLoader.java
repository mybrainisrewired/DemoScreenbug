package com.android.systemui.recent;

import android.app.ActivityManager;
import android.app.ActivityManager.TaskThumbnails;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Process;
import com.android.systemui.R;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RecentTasksLoader {
    static final boolean DEBUG = false;
    private static final int DISPLAY_TASKS = 20;
    private static final int MAX_TASKS = 21;
    static final String TAG = "RecentTasksLoader";
    private Context mContext;
    private Bitmap mDefaultIconBackground;
    private Bitmap mDefaultThumbnailBackground;
    private final Handler mHandler;
    private int mIconDpi;
    private int mNumTasksInFirstScreenful;
    private RecentsPanelView mRecentsPanel;
    private AsyncTask<Void, ArrayList<TaskDescription>, Void> mTaskLoader;
    private AsyncTask<Void, TaskDescription, Void> mThumbnailLoader;

    class AnonymousClass_1 extends AsyncTask<Void, ArrayList<TaskDescription>, Void> {
        final /* synthetic */ LinkedBlockingQueue val$tasksWaitingForThumbnails;

        AnonymousClass_1(LinkedBlockingQueue linkedBlockingQueue) {
            this.val$tasksWaitingForThumbnails = linkedBlockingQueue;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected java.lang.Void doInBackground(java.lang.Void... r21_params) {
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.recent.RecentTasksLoader.AnonymousClass_1.doInBackground(java.lang.Void[]):java.lang.Void");
            /*
            r20 = this;
            r1 = android.os.Process.myTid();
            r15 = android.os.Process.getThreadPriority(r1);
            r1 = 10;
            android.os.Process.setThreadPriority(r1);
            r0 = r20;
            r1 = com.android.systemui.recent.RecentTasksLoader.this;
            r1 = r1.mContext;
            r16 = r1.getPackageManager();
            r0 = r20;
            r1 = com.android.systemui.recent.RecentTasksLoader.this;
            r1 = r1.mContext;
            r2 = "activity";
            r8 = r1.getSystemService(r2);
            r8 = (android.app.ActivityManager) r8;
            r1 = 21;
            r2 = 2;
            r18 = r8.getRecentTasks(r1, r2);
            r14 = r18.size();
            r1 = new android.content.Intent;
            r2 = "android.intent.action.MAIN";
            r1.<init>(r2);
            r2 = "android.intent.category.HOME";
            r1 = r1.addCategory(r2);
            r2 = 0;
            r0 = r16;
            r7 = r1.resolveActivityInfo(r0, r2);
            r10 = 1;
            r19 = new java.util.ArrayList;
            r19.<init>();
            r9 = 1;
            r11 = 1;
            r12 = 0;
        L_0x0051:
            if (r11 >= r14) goto L_0x005d;
        L_0x0053:
            r1 = 21;
            if (r12 >= r1) goto L_0x005d;
        L_0x0057:
            r1 = r20.isCancelled();
            if (r1 == 0) goto L_0x0091;
        L_0x005d:
            r1 = r20.isCancelled();
            if (r1 != 0) goto L_0x0080;
        L_0x0063:
            r1 = 1;
            r1 = new java.util.ArrayList[r1];
            r2 = 0;
            r1[r2] = r19;
            r0 = r20;
            r0.publishProgress(r1);
            if (r10 == 0) goto L_0x0080;
        L_0x0070:
            r1 = 1;
            r1 = new java.util.ArrayList[r1];
            r2 = 0;
            r3 = new java.util.ArrayList;
            r3.<init>();
            r1[r2] = r3;
            r0 = r20;
            r0.publishProgress(r1);
        L_0x0080:
            r0 = r20;
            r1 = r0.val$tasksWaitingForThumbnails;	 Catch:{ InterruptedException -> 0x00ea }
            r2 = new com.android.systemui.recent.TaskDescription;	 Catch:{ InterruptedException -> 0x00ea }
            r2.<init>();	 Catch:{ InterruptedException -> 0x00ea }
            r1.put(r2);	 Catch:{ InterruptedException -> 0x00ea }
            android.os.Process.setThreadPriority(r15);
            r1 = 0;
            return r1;
        L_0x0091:
            r0 = r18;
            r17 = r0.get(r11);
            r17 = (android.app.ActivityManager.RecentTaskInfo) r17;
            r0 = r20;
            r1 = com.android.systemui.recent.RecentTasksLoader.this;
            r0 = r17;
            r2 = r0.id;
            r0 = r17;
            r3 = r0.persistentId;
            r0 = r17;
            r4 = r0.baseIntent;
            r0 = r17;
            r5 = r0.origActivity;
            r0 = r17;
            r6 = r0.description;
            r13 = r1.createTaskDescription(r2, r3, r4, r5, r6, r7);
            if (r13 == 0) goto L_0x00e6;
        L_0x00b7:
            r0 = r20;
            r1 = r0.val$tasksWaitingForThumbnails;	 Catch:{ InterruptedException -> 0x00ec }
            r1.put(r13);	 Catch:{ InterruptedException -> 0x00ec }
            r0 = r19;
            r0.add(r13);
            if (r10 == 0) goto L_0x00e4;
        L_0x00c5:
            r1 = r19.size();
            r0 = r20;
            r2 = com.android.systemui.recent.RecentTasksLoader.this;
            r2 = r2.mNumTasksInFirstScreenful;
            if (r1 != r2) goto L_0x00e4;
        L_0x00d3:
            r1 = 1;
            r1 = new java.util.ArrayList[r1];
            r2 = 0;
            r1[r2] = r19;
            r0 = r20;
            r0.publishProgress(r1);
            r19 = new java.util.ArrayList;
            r19.<init>();
            r10 = 0;
        L_0x00e4:
            r12 = r12 + 1;
        L_0x00e6:
            r11 = r11 + 1;
            goto L_0x0051;
        L_0x00ea:
            r1 = move-exception;
            goto L_0x0080;
        L_0x00ec:
            r1 = move-exception;
            goto L_0x00b7;
            */
        }

        protected void onProgressUpdate(ArrayList<TaskDescription>... values) {
            if (!isCancelled()) {
                RecentTasksLoader.this.mRecentsPanel.onTasksLoaded(values[0]);
            }
        }
    }

    class AnonymousClass_2 extends AsyncTask<Void, TaskDescription, Void> {
        final /* synthetic */ BlockingQueue val$tasksWaitingForThumbnails;

        AnonymousClass_2(BlockingQueue blockingQueue) {
            this.val$tasksWaitingForThumbnails = blockingQueue;
        }

        protected Void doInBackground(Void... params) {
            int origPri = Process.getThreadPriority(Process.myTid());
            Process.setThreadPriority(10);
            while (!isCancelled()) {
                TaskDescription td = null;
                while (td == null) {
                    try {
                        td = (TaskDescription) this.val$tasksWaitingForThumbnails.take();
                    } catch (InterruptedException e) {
                    }
                }
                if (td.isNull()) {
                    break;
                }
                RecentTasksLoader.this.loadThumbnailAndIcon(td);
                synchronized (td) {
                    publishProgress(new TaskDescription[]{td});
                }
            }
            Process.setThreadPriority(origPri);
            return null;
        }

        protected void onProgressUpdate(TaskDescription... values) {
            if (!isCancelled()) {
                RecentTasksLoader.this.mRecentsPanel.onTaskThumbnailLoaded(values[0]);
            }
        }
    }

    public RecentTasksLoader(Context context) {
        this.mContext = context;
        Resources res = context.getResources();
        if (res.getBoolean(R.bool.config_recents_interface_for_tablets)) {
            this.mIconDpi = ((ActivityManager) context.getSystemService("activity")).getLauncherLargeIconDensity();
        } else {
            this.mIconDpi = res.getDisplayMetrics().densityDpi;
        }
        int iconSize = (this.mIconDpi * res.getDimensionPixelSize(17104896)) / res.getDisplayMetrics().densityDpi;
        this.mDefaultIconBackground = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
        int thumbnailWidth = res.getDimensionPixelSize(17104898);
        int thumbnailHeight = res.getDimensionPixelSize(17104897);
        int color = res.getColor(R.drawable.status_bar_recents_app_thumbnail_background);
        this.mDefaultThumbnailBackground = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, Config.ARGB_8888);
        new Canvas(this.mDefaultThumbnailBackground).drawColor(color);
        ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
        this.mHandler = new Handler();
    }

    private Drawable getFullResIcon(ResolveInfo info, PackageManager packageManager) {
        try {
            Resources resources = packageManager.getResourcesForApplication(info.activityInfo.applicationInfo);
        } catch (NameNotFoundException e) {
            resources = null;
        }
        if (resources != null) {
            int iconId = info.activityInfo.getIconResource();
            if (iconId != 0) {
                return getFullResIcon(resources, iconId);
            }
        }
        return getFullResDefaultActivityIcon();
    }

    private void loadThumbnailsAndIconsInBackground(BlockingQueue<TaskDescription> tasksWaitingForThumbnails) {
        this.mThumbnailLoader = new AnonymousClass_2(tasksWaitingForThumbnails);
        this.mThumbnailLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void cancelLoadingThumbnailsAndIcons() {
        if (this.mTaskLoader != null) {
            this.mTaskLoader.cancel(DEBUG);
            this.mTaskLoader = null;
        }
        if (this.mThumbnailLoader != null) {
            this.mThumbnailLoader.cancel(DEBUG);
            this.mThumbnailLoader = null;
        }
    }

    TaskDescription createTaskDescription(int taskId, int persistentTaskId, Intent baseIntent, ComponentName origActivity, CharSequence description, ActivityInfo homeInfo) {
        Intent intent = new Intent(baseIntent);
        if (origActivity != null) {
            intent.setComponent(origActivity);
        }
        PackageManager pm = this.mContext.getPackageManager();
        if (homeInfo == null) {
            homeInfo = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME").resolveActivityInfo(pm, 0);
        }
        if (homeInfo != null && homeInfo.packageName.equals(intent.getComponent().getPackageName()) && homeInfo.name.equals(intent.getComponent().getClassName())) {
            return null;
        }
        intent.setFlags((intent.getFlags() & -2097153) | 268435456);
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
        if (resolveInfo != null) {
            ActivityInfo info = resolveInfo.activityInfo;
            String title = info.loadLabel(pm).toString();
            if (title != null && title.length() > 0) {
                TaskDescription item = new TaskDescription(taskId, persistentTaskId, resolveInfo, baseIntent, info.packageName, description);
                item.setLabel(title);
                return item;
            }
        }
        return null;
    }

    public Bitmap getDefaultIcon() {
        return this.mDefaultIconBackground;
    }

    public Bitmap getDefaultThumbnail() {
        return this.mDefaultThumbnailBackground;
    }

    Drawable getFullResDefaultActivityIcon() {
        return getFullResIcon(Resources.getSystem(), 17629184);
    }

    Drawable getFullResIcon(Resources resources, int iconId) {
        try {
            return resources.getDrawableForDensity(iconId, this.mIconDpi);
        } catch (NotFoundException e) {
            return getFullResDefaultActivityIcon();
        }
    }

    public void loadTasksInBackground() {
        cancelLoadingThumbnailsAndIcons();
        LinkedBlockingQueue<TaskDescription> tasksWaitingForThumbnails = new LinkedBlockingQueue();
        this.mTaskLoader = new AnonymousClass_1(tasksWaitingForThumbnails);
        this.mTaskLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        loadThumbnailsAndIconsInBackground(tasksWaitingForThumbnails);
    }

    void loadThumbnailAndIcon(TaskDescription td) {
        ActivityManager am = (ActivityManager) this.mContext.getSystemService("activity");
        PackageManager pm = this.mContext.getPackageManager();
        TaskThumbnails thumbs = am.getTaskThumbnails(td.persistentTaskId);
        Drawable icon = getFullResIcon(td.resolveInfo, pm);
        synchronized (td) {
            if (thumbs != null) {
                if (thumbs.mainThumbnail != null) {
                    td.setThumbnail(thumbs.mainThumbnail);
                    if (icon != null) {
                        td.setIcon(icon);
                    }
                    td.setLoaded(true);
                }
            }
            td.setThumbnail(this.mDefaultThumbnailBackground);
            if (icon != null) {
                td.setIcon(icon);
            }
            td.setLoaded(true);
        }
    }

    public void setRecentsPanel(RecentsPanelView recentsPanel) {
        this.mRecentsPanel = recentsPanel;
        this.mNumTasksInFirstScreenful = this.mRecentsPanel.numItemsInOneScreenful();
    }
}