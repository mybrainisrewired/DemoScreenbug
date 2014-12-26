package com.isssss.myadv.model;

public class TaskApp {
    private static TaskApp mInstance;
    private String packageName;

    public static synchronized TaskApp getInstance() {
        TaskApp taskApp;
        synchronized (TaskApp.class) {
            if (mInstance == null) {
                mInstance = new TaskApp();
            }
            taskApp = mInstance;
        }
        return taskApp;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}