package com.clouds.util;

import android.util.Log;
import com.clouds.debug.SystemDebug;
import com.wmt.wmtserver.WmtServer;
import com.wmt.wmtserver.WmtServer.OnFinishCallback;

public class ScriptCmd4wmt8850 implements OnFinishCallback {
    private static final String TAG;
    private static ScriptCmd4wmt8850 cb;
    public static int isSucceed;

    static {
        TAG = ScriptCmd4wmt8850.class.getSimpleName();
        isSucceed = 0;
        cb = null;
    }

    public static synchronized int ExecWmtScriptCmd(String wmtShFilePath) {
        int i;
        synchronized (ScriptCmd4wmt8850.class) {
            SystemDebug.e(TAG, new StringBuilder("isSucceed: ").append(isSucceed).toString());
            SystemDebug.e(TAG, new StringBuilder("wmtShFilePath: ").append(wmtShFilePath).toString());
            WmtServer.wmtShCmd(wmtShFilePath, getScriptCmd4wmt8850());
            i = isSucceed;
        }
        return i;
    }

    public static ScriptCmd4wmt8850 getScriptCmd4wmt8850() {
        if (cb == null) {
            cb = new ScriptCmd4wmt8850();
        }
        return cb;
    }

    public void onFinish(int ret, int cmdId) {
        if (ret == 0) {
            isSucceed = 1;
            Log.d(TAG, "onFinish wmtShCmd return true!");
        } else {
            isSucceed = 1;
            Log.d(TAG, "onFinish wmtShCmd return false!");
        }
        SystemDebug.e(TAG, new StringBuilder("isSucceed: ").append(isSucceed).toString());
    }
}