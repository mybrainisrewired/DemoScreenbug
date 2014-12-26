package com.clmobile.utils;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ScriptUtils {
    private static final String SCRIPT_FILE = "script.sh";

    public static boolean hasRootAccess() {
        try {
            if (runRootCommand(null, new StringBuilder()) == 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int runRootCommand(java.lang.String r8_command, java.lang.StringBuilder r9_res) {
        throw new UnsupportedOperationException("Method not decompiled: com.clmobile.utils.ScriptUtils.runRootCommand(java.lang.String, java.lang.StringBuilder):int");
        /*
        r1 = -1;
        r5 = 0;
        r3 = 0;
        r6 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x0047 }
        r7 = "su";
        r5 = r6.exec(r7);	 Catch:{ Exception -> 0x0047 }
        r4 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x0047 }
        r6 = r5.getOutputStream();	 Catch:{ Exception -> 0x0047 }
        r4.<init>(r6);	 Catch:{ Exception -> 0x0047 }
        if (r8 == 0) goto L_0x002e;
    L_0x0018:
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r7 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r6.<init>(r7);	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r7 = "\n";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r4.writeBytes(r6);	 Catch:{ Exception -> 0x0081, all -> 0x007c }
    L_0x002e:
        r6 = "exit\n";
        r4.writeBytes(r6);	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r4.flush();	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        r1 = r5.waitFor();	 Catch:{ Exception -> 0x0081, all -> 0x007c }
        if (r4 == 0) goto L_0x003f;
    L_0x003c:
        r4.close();	 Catch:{ Exception -> 0x0078 }
    L_0x003f:
        if (r5 == 0) goto L_0x0044;
    L_0x0041:
        r5.destroy();	 Catch:{ Exception -> 0x0078 }
    L_0x0044:
        r3 = r4;
        r2 = r1;
    L_0x0046:
        return r2;
    L_0x0047:
        r0 = move-exception;
    L_0x0048:
        if (r9 == 0) goto L_0x0060;
    L_0x004a:
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006c }
        r7 = "Unexpected error - Here is what I know: ";
        r6.<init>(r7);	 Catch:{ all -> 0x006c }
        r7 = r0.getMessage();	 Catch:{ all -> 0x006c }
        r6 = r6.append(r7);	 Catch:{ all -> 0x006c }
        r6 = r6.toString();	 Catch:{ all -> 0x006c }
        r9.append(r6);	 Catch:{ all -> 0x006c }
    L_0x0060:
        if (r3 == 0) goto L_0x0065;
    L_0x0062:
        r3.close();	 Catch:{ Exception -> 0x007f }
    L_0x0065:
        if (r5 == 0) goto L_0x006a;
    L_0x0067:
        r5.destroy();	 Catch:{ Exception -> 0x007f }
    L_0x006a:
        r2 = r1;
        goto L_0x0046;
    L_0x006c:
        r6 = move-exception;
    L_0x006d:
        if (r3 == 0) goto L_0x0072;
    L_0x006f:
        r3.close();	 Catch:{ Exception -> 0x007a }
    L_0x0072:
        if (r5 == 0) goto L_0x0077;
    L_0x0074:
        r5.destroy();	 Catch:{ Exception -> 0x007a }
    L_0x0077:
        throw r6;
    L_0x0078:
        r6 = move-exception;
        goto L_0x0044;
    L_0x007a:
        r7 = move-exception;
        goto L_0x0077;
    L_0x007c:
        r6 = move-exception;
        r3 = r4;
        goto L_0x006d;
    L_0x007f:
        r6 = move-exception;
        goto L_0x006a;
    L_0x0081:
        r0 = move-exception;
        r3 = r4;
        goto L_0x0048;
        */
    }

    public static int runScriptAsRoot(Context ctx, String script, StringBuilder res) {
        File file = new File(ctx.getDir("bin", 0), SCRIPT_FILE);
        int exitcode = 0;
        Process exec = null;
        try {
            file.createNewFile();
            String abspath = file.getAbsolutePath();
            Runtime.getRuntime().exec(new StringBuilder("chmod 777 ").append(abspath).toString()).waitFor();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            if (new File("/system/bin/sh").exists()) {
                out.write("#!/system/bin/sh\n");
            }
            out.write(script);
            if (!script.endsWith("\n")) {
                out.write("\n");
            }
            out.write("exit\n");
            out.flush();
            out.close();
            exec = Runtime.getRuntime().exec(new StringBuilder("su -c ").append(abspath).toString());
            InputStreamReader r = new InputStreamReader(exec.getInputStream());
            char[] buf = new char[1024];
            while (true) {
                int read = r.read(buf);
                if (read == -1) {
                    r = new InputStreamReader(exec.getErrorStream());
                    while (true) {
                        read = r.read(buf);
                        if (read == -1) {
                            if (exec != null) {
                                exitcode = exec.waitFor();
                            }
                            if (exec != null) {
                                exec.destroy();
                            }
                            return exitcode;
                        } else if (res != null) {
                            res.append(buf, 0, read);
                        }
                    }
                } else if (res != null) {
                    res.append(buf, 0, read);
                }
            }
        } catch (InterruptedException e) {
            if (res != null) {
                res.append("\nOperation timed-out");
            }
            if (exec != null) {
                exec.destroy();
            }
        } catch (Exception e2) {
            ex = e2;
            if (res != null) {
                Exception ex2;
                res.append(new StringBuilder("\n").append(ex2).toString());
            }
            if (exec != null) {
                exec.destroy();
            }
        }
    }
}