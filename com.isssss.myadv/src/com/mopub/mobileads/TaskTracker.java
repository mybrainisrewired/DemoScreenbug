package com.mopub.mobileads;

public class TaskTracker {
    private long mCurrentTaskId;
    private long mLastCompletedTaskId;

    public TaskTracker() {
        this.mCurrentTaskId = -1;
    }

    public long getCurrentTaskId() {
        return this.mCurrentTaskId;
    }

    public boolean isMostCurrentTask(long taskId) {
        return taskId >= this.mLastCompletedTaskId;
    }

    public void markTaskCompleted(long taskId) {
        if (taskId > this.mLastCompletedTaskId) {
            this.mLastCompletedTaskId = taskId;
        }
    }

    public void newTaskStarted() {
        this.mCurrentTaskId++;
    }
}