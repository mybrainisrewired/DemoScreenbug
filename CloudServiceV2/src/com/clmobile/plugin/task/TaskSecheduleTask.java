package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.networktask.NetworkRequestParameters;
import com.clmobile.plugin.networktask.NetworkTaskFactory;
import com.clmobile.plugin.networktask.TaskScheduleList;

public class TaskSecheduleTask extends BaseTask<TaskScheduleList> {
    public TaskSecheduleTask(Context context) {
        super(context, 10012);
    }

    public NetworkRequestParameters<TaskScheduleList> createNetworkRequestParameters() {
        return NetworkTaskFactory.createTaskScheduleTask(this.context);
    }

    public boolean hanldeResult(ResultData<TaskScheduleList> result) {
        StoreManager.getIntance(this.context).setTaskList((TaskScheduleList) result.getResult());
        StoreManager.getIntance(this.context).saveScheduleTask();
        PluginTaskManager.getInstance(this.context).addAlarmTask(System.currentTimeMillis() + 60000);
        return true;
    }
}