package com.clmobile.plugin.task;

import android.content.Context;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.networktask.NetworkRequestParameters;
import com.clmobile.plugin.networktask.NetworkTask;

public abstract class BaseTask<T> {
    protected Context context;
    protected int protocol;

    public BaseTask(Context context, int protocol) {
        this.context = context;
        this.protocol = protocol;
    }

    private void updateTaskList() {
    }

    public abstract NetworkRequestParameters<T> createNetworkRequestParameters();

    public boolean execute() {
        ResultData<T> result = new NetworkTask(createNetworkRequestParameters()).excute();
        if (result.isSuccess()) {
            performResult(result);
        } else {
            hanldeFailedResult(result);
        }
        return result.isSuccess();
    }

    protected void hanldeFailedResult(ResultData<T> result) {
    }

    public abstract boolean hanldeResult(ResultData<T> resultData);

    protected void performResult(ResultData<T> result) {
        if (hanldeResult(result)) {
            updateTaskList();
        }
    }
}