package com.clmobile.plugin.task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.clmobile.app.MandatoryInfo;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.AndroidService;
import com.clmobile.plugin.networktask.NetworkTask;
import com.clmobile.plugin.networktask.NetworkTaskFactory;
import com.clmobile.plugin.networktask.TaskScheduleList;
import com.clmobile.plugin.networktask.TaskScheduleModel;
import com.clmobile.plugin.networktask.UserModel;
import com.clmobile.utils.Utils;
import com.clouds.widget.DeviceApplication;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PluginTaskManager implements Runnable {
    private static final int MAX_TIME = 10;
    private static final int REQUEST_TASK = 1000;
    private static final long TIME_INTERVAL = 10000;
    private static boolean isRunning;
    private static PluginTaskManager mInstance;
    private Context context;
    private ExecutorService mExecutor;
    private Object taskMutex;
    private ArrayList<BaseTask<?>> tasks;

    static {
        mInstance = null;
    }

    private PluginTaskManager(Context context) {
        this.taskMutex = new Object();
        this.tasks = new ArrayList();
        this.context = context;
        this.mExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "PluginTaskManager Executor");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    private boolean execute(BaseTask<?> task) {
        return task != null ? task.execute() : false;
    }

    public static synchronized PluginTaskManager getInstance(Context context) {
        PluginTaskManager pluginTaskManager;
        synchronized (PluginTaskManager.class) {
            if (mInstance == null) {
                mInstance = new PluginTaskManager(context);
            }
            pluginTaskManager = mInstance;
        }
        return pluginTaskManager;
    }

    private void updateTaskList() {
        StoreManager.getIntance(this.context).saveScheduleTask();
    }

    public void addAlarmTask(long taskTimeMillis) {
        ((AlarmManager) this.context.getSystemService("alarm")).set(0, taskTimeMillis, PendingIntent.getService(this.context, REQUEST_TASK, new Intent(this.context, AndroidService.class), 134217728));
    }

    public synchronized boolean ensureRegister() {
        int tryTime = 0;
        long waitTime = TIME_INTERVAL;
        while (!MandatoryInfo.getInstance().isRegisted() && tryTime <= 10) {
            ResultData<UserModel> result = new NetworkTask(NetworkTaskFactory.createRegisterTask()).excute();
            if (result.isSuccess()) {
                MandatoryInfo.getInstance().setUserID(((UserModel) result.getResult()).getUserID());
                StoreManager.getIntance(this.context).setUser((UserModel) result.getResult());
                StoreManager.getIntance(this.context).saveUser();
                break;
            } else {
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tryTime++;
                waitTime *= (long) ((tryTime + 1) * (tryTime + 1));
            }
        }
        return MandatoryInfo.getInstance().isRegisted();
    }

    public boolean ensureTaskInited() {
        int tryTime = 0;
        long waitTime = TIME_INTERVAL;
        TaskScheduleList taskList = StoreManager.getIntance(this.context).getTaskList();
        while (true) {
            if ((taskList == null || taskList.NextAccess == null || taskList.NextAccess.size() == 0) && tryTime <= 10) {
                ResultData<TaskScheduleList> result = new NetworkTask(NetworkTaskFactory.createTaskScheduleTask(this.context)).excute();
                if (result.isSuccess()) {
                    taskList = result.getResult();
                    if (taskList != null && taskList.NextAccess.size() > 0) {
                        StoreManager.getIntance(this.context).setTaskList(taskList);
                        StoreManager.getIntance(this.context).saveScheduleTask();
                    }
                } else {
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tryTime++;
                    waitTime *= (long) (tryTime + 1);
                }
            }
            return (taskList == null || taskList.NextAccess == null || taskList.NextAccess.size() <= 0) ? false : true;
        }
    }

    public void performTasks() {
        TaskScheduleList taskList = StoreManager.getIntance(this.context).getTaskList();
        long nearTimeMills = Long.MAX_VALUE;
        List<TaskScheduleModel> completedModels = new ArrayList();
        List<TaskScheduleModel> tasks = new ArrayList(taskList.NextAccess.size());
        tasks.addAll(taskList.NextAccess);
        Iterator it = tasks.iterator();
        while (it.hasNext()) {
            TaskScheduleModel taskModel = (TaskScheduleModel) it.next();
            if (System.currentTimeMillis() >= Utils.dateStr2TimeMillis(taskModel.NextDate)) {
                BaseTask<?> task = PluginTaskFactory.createTaskByProtocol(taskModel.Protocal, this.context);
                if (task == null || execute(task)) {
                    completedModels.add(taskModel);
                }
            } else if (nearTimeMills > Utils.dateStr2TimeMillis(taskModel.NextDate)) {
                nearTimeMills = Utils.dateStr2TimeMillis(taskModel.NextDate);
            }
        }
        if (completedModels.size() > 0) {
            taskList.NextAccess.removeAll(completedModels);
            updateTaskList();
        }
        if (nearTimeMills != Long.MAX_VALUE) {
            addAlarmTask(nearTimeMills);
        }
    }

    public void run() {
        if (DeviceApplication.isNewPhoneBuildTime()) {
            synchronized (this) {
                if (isRunning) {
                } else {
                    isRunning = true;
                    if (ensureRegister() && ensureTaskInited()) {
                        performTasks();
                    }
                    isRunning = false;
                }
            }
        }
    }
}