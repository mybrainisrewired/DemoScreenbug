package com.android.systemui;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.power.PowerUI;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class SystemUIService extends Service {
    static final String TAG = "SystemUIService";
    final Object[] SERVICES;
    SystemUI[] mServices;

    public SystemUIService() {
        this.SERVICES = new Object[]{Integer.valueOf(0), PowerUI.class, RingtonePlayer.class};
    }

    private Class chooseClass(Object o) {
        if (o instanceof Integer) {
            try {
                return getClassLoader().loadClass(getString(((Integer) o).intValue()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (o instanceof Class) {
            return (Class) o;
        } else {
            throw new RuntimeException("Unknown system ui service: " + o);
        }
    }

    private boolean hasOTAServer() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.clouds.server", 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    private void showDemoOverlay() {
        TextView tv = new TextView(this);
        tv.setText("Demo");
        tv.setTextSize(180.0f);
        tv.setGravity(17);
        tv.setBackgroundColor(0);
        tv.setTextColor(-65536);
        LayoutParams lp = new LayoutParams();
        lp.type = 2006;
        lp.width = -1;
        lp.height = -2;
        lp.gravity = 17;
        lp.format = tv.getBackground().getOpacity();
        lp.flags = 24;
        ((WindowManager) getSystemService("window")).addView(tv, lp);
    }

    protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        SystemUI[] arr$;
        int len$;
        int i$;
        SystemUI ui;
        if (args == null || args.length == 0) {
            arr$ = this.mServices;
            len$ = arr$.length;
            i$ = 0;
            while (i$ < len$) {
                ui = arr$[i$];
                pw.println("dumping service: " + ui.getClass().getName());
                ui.dump(fd, pw, args);
                i$++;
            }
        } else {
            String svc = args[0];
            arr$ = this.mServices;
            len$ = arr$.length;
            i$ = 0;
            while (i$ < len$) {
                ui = arr$[i$];
                if (ui.getClass().getName().endsWith(svc)) {
                    ui.dump(fd, pw, args);
                }
                i$++;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        SystemUI[] arr$ = this.mServices;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            arr$[i$].onConfigurationChanged(newConfig);
            i$++;
        }
    }

    public void onCreate() {
        IWindowManager wm = Stub.asInterface(ServiceManager.getService("window"));
        try {
            this.SERVICES[0] = Integer.valueOf(wm.hasSystemNavBar() ? R.string.config_systemBarComponent : R.string.config_statusBarComponent);
        } catch (RemoteException e) {
            Slog.w(TAG, "Failing checking whether status bar can hide", e);
        }
        int N = this.SERVICES.length;
        this.mServices = new SystemUI[N];
        int i = 0;
        while (i < N) {
            Class cl = chooseClass(this.SERVICES[i]);
            Slog.d(TAG, "loading: " + cl);
            try {
                this.mServices[i] = (SystemUI) cl.newInstance();
                this.mServices[i].mContext = this;
                Slog.d(TAG, "running: " + this.mServices[i]);
                this.mServices[i].start();
                i++;
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InstantiationException e3) {
                throw new RuntimeException(e3);
            }
        }
        if (!hasOTAServer()) {
            showDemoOverlay();
        }
    }
}