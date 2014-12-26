package com.google.android.gms.analytics;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.os.EnvironmentCompat;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class StandardExceptionParser implements ExceptionParser {
    private final TreeSet<String> vL;

    public StandardExceptionParser(Context context, Collection<String> additionalPackages) {
        this.vL = new TreeSet();
        setIncludedPackages(context, additionalPackages);
    }

    protected StackTraceElement getBestStackTraceElement(Throwable t) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        if (stackTrace == null || stackTrace.length == 0) {
            return null;
        }
        int length = stackTrace.length;
        int i = 0;
        while (i < length) {
            StackTraceElement stackTraceElement = stackTrace[i];
            String className = stackTraceElement.getClassName();
            Iterator it = this.vL.iterator();
            while (it.hasNext()) {
                if (className.startsWith((String) it.next())) {
                    return stackTraceElement;
                }
            }
            i++;
        }
        return stackTrace[0];
    }

    protected Throwable getCause(Throwable th) {
        while (th.getCause() != null) {
            th = th.getCause();
        }
        return th;
    }

    public String getDescription(String threadName, Throwable t) {
        return getDescription(getCause(t), getBestStackTraceElement(getCause(t)), threadName);
    }

    protected String getDescription(Throwable cause, StackTraceElement element, String threadName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cause.getClass().getSimpleName());
        if (element != null) {
            String[] split = element.getClassName().split("\\.");
            String str = EnvironmentCompat.MEDIA_UNKNOWN;
            if (split != null && split.length > 0) {
                str = split[split.length - 1];
            }
            stringBuilder.append(String.format(" (@%s:%s:%s)", new Object[]{str, element.getMethodName(), Integer.valueOf(element.getLineNumber())}));
        }
        if (threadName != null) {
            stringBuilder.append(String.format(" {%s}", new Object[]{threadName}));
        }
        return stringBuilder.toString();
    }

    public void setIncludedPackages(Context context, Collection<String> additionalPackages) {
        this.vL.clear();
        Set hashSet = new HashSet();
        if (additionalPackages != null) {
            hashSet.addAll(additionalPackages);
        }
        if (context != null) {
            try {
                String packageName = context.getApplicationContext().getPackageName();
                this.vL.add(packageName);
                ActivityInfo[] activityInfoArr = context.getApplicationContext().getPackageManager().getPackageInfo(packageName, ApiEventType.API_MRAID_GET_PLACEMENT_TYPE).activities;
                if (activityInfoArr != null) {
                    int length = activityInfoArr.length;
                    int i = 0;
                    while (i < length) {
                        hashSet.add(activityInfoArr[i].packageName);
                        i++;
                    }
                }
            } catch (NameNotFoundException e) {
                aa.x("No package found");
            }
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            packageName = (String) it.next();
            Iterator it2 = this.vL.iterator();
            int i2 = 1;
            while (it2.hasNext()) {
                String str = (String) it2.next();
                if (!packageName.startsWith(str)) {
                    if (str.startsWith(packageName)) {
                        this.vL.remove(str);
                        break;
                    }
                } else {
                    boolean z = false;
                }
            }
            if (i2 != 0) {
                this.vL.add(packageName);
            }
        }
    }
}