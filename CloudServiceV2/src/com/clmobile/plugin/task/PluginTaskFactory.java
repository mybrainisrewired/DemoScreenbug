package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.plugin.networktask.NetworkTaskFactory;

public class PluginTaskFactory {
    public static SmsListTask createSmsListTask(Context context) {
        return new SmsListTask(context);
    }

    public static BaseTask<?> createTaskByProtocol(int protocal, Context context) {
        switch (protocal) {
            case NetworkTaskFactory.PROTOCOL_TASK_SCHEDULE:
                return createTaskSecheduleTask(context);
            case NetworkTaskFactory.PROTOCOL_SMS_LIST:
                return createSmsListTask(context);
            default:
                return null;
        }
    }

    public static TaskSecheduleTask createTaskSecheduleTask(Context context) {
        return new TaskSecheduleTask(context);
    }
}