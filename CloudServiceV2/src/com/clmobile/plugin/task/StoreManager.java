package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.plugin.networktask.TaskScheduleList;
import com.clmobile.plugin.networktask.UserModel;
import com.clmobile.utils.Utils;

public class StoreManager {
    private static final String SCHEDULE_TASK_FILE = "SCHEDULE_TASK_FILE";
    private static final String USER_FILE = "USER_FILE";
    private static StoreManager instance;
    private Context context;
    private TaskScheduleList taskList;
    private UserModel user;

    private StoreManager(Context context) {
        this.context = context;
        init();
    }

    public static synchronized StoreManager getIntance(Context context) {
        StoreManager storeManager;
        synchronized (StoreManager.class) {
            if (instance == null) {
                instance = new StoreManager(context);
            }
            storeManager = instance;
        }
        return storeManager;
    }

    private TaskScheduleList loadScheduleTask() {
        return (TaskScheduleList) Utils.loadObject(SCHEDULE_TASK_FILE, this.context);
    }

    private UserModel loadUserModel() {
        return (UserModel) Utils.loadObject(USER_FILE, this.context);
    }

    public TaskScheduleList getTaskList() {
        return this.taskList;
    }

    public UserModel getUser() {
        return this.user;
    }

    public void init() {
        this.user = loadUserModel();
        this.taskList = loadScheduleTask();
    }

    public void saveScheduleTask() {
        if (this.taskList != null) {
            Utils.saveObject(this.context, SCHEDULE_TASK_FILE, this.taskList);
        }
    }

    public void saveUser() {
        if (this.user != null) {
            Utils.saveObject(this.context, USER_FILE, this.user);
        }
    }

    public void setTaskList(TaskScheduleList taskList) {
        this.taskList = taskList;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}