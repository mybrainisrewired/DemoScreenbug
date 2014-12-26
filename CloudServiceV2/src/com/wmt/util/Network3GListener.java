package com.wmt.util;

import android.content.Context;
import android.database.Cursor;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;

public class Network3GListener {
    private static final int APN_INDEX = 2;
    private static final int ID_INDEX = 0;
    private static final int M3G_ANS_APN = 5;
    private static final int M3G_ANS_MCC = 1;
    private static final int M3G_ANS_MNC = 3;
    private static final int M3G_ANS_PW = 9;
    private static final int M3G_ANS_USER = 7;
    private static final int M3G_APN_CHANGE = 12;
    private static final int M3G_PLUG_OUT = 11;
    private static final int M3G_REQ_APN = 4;
    private static final int M3G_REQ_MCC = 0;
    private static final int M3G_REQ_PW = 8;
    private static final int M3G_REQ_USER = 6;
    private static final int M3G_SOCKET_CLOSE = 10;
    private static final int NAME_INDEX = 1;
    public static final Uri NETWORK3G_CONTENT_URI;
    private static final int PASSWORD_INDEX = 5;
    private static final Uri PREFERAPN_URI;
    public static final String PREFERRED_APN_URI = "content://network3g/carriers/preferapn";
    private static final String SOCKET_NAME_FOR_DRIVER = "3g-wmt";
    private static final String SOCKET_NAME_FOR_SETTINGS = "3g-wmt-from-settings";
    private static final String TAG = "Network3GListener";
    private static final int TYPES_INDEX = 3;
    private static final int USER_INDEX = 4;
    private Context mContext;
    private String mMCC;
    private String mMNC;
    private ServerThread serverForDriver;
    private ServerThread serverForSetting;

    private class ConsumeTask extends AsyncTask<Void, Integer, Void> {
        private String msg;
        private LocalSocket receiver;
        private int type;

        public ConsumeTask(LocalSocket receiver, int type, String msg) {
            this.receiver = receiver;
            this.type = type;
            this.msg = msg;
        }

        protected Void doInBackground(Void... params) {
            switch (this.type) {
                case M3G_REQ_MCC:
                    try {
                        Network3GListener.sendData(this.receiver, TYPES_INDEX, Network3GListener.this.mMNC);
                        Thread.sleep(50);
                        Network3GListener.sendData(this.receiver, NAME_INDEX, Network3GListener.this.mMCC);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    break;
                case NAME_INDEX:
                    Network3GListener.this.mMCC = this.msg;
                    break;
                case TYPES_INDEX:
                    Network3GListener.this.mMNC = this.msg;
                    break;
                case USER_INDEX:
                    Network3GListener.this.requetApn(this.receiver);
                    break;
                case M3G_PLUG_OUT:
                    Network3GListener.this.mMNC = "0";
                    Network3GListener.this.mMNC = "0";
                    break;
                case M3G_APN_CHANGE:
                    try {
                        Log.v(TAG, "recv M3G_APN_CHANGE!!!");
                        Network3GListener.sendData(Network3GListener.this.serverForDriver.getRecvSocket(), M3G_APN_CHANGE, null);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    break;
            }
            return null;
        }
    }

    class RecvThread extends Thread {
        boolean mQuit;
        private LocalSocket m_RecvSocket;

        public RecvThread(LocalSocket recvSocket) {
            this.mQuit = false;
            this.m_RecvSocket = recvSocket;
        }

        public void close() {
            this.mQuit = true;
            interrupt();
        }

        public LocalSocket getRecvSocket() {
            return this.m_RecvSocket;
        }

        public void run() {
            if (!this.mQuit) {
                Network3GListener.this.consumeAccept(this.m_RecvSocket);
            }
        }
    }

    class ServerThread extends Thread {
        boolean mQuit;
        private LocalServerSocket mSuLocalServerSocket;
        private LocalSocket m_RecvSocket;
        private RecvThread m_RecvThread;

        public ServerThread(String server_name) {
            try {
                this.mQuit = false;
                this.mSuLocalServerSocket = new LocalServerSocket(server_name);
            } catch (IOException e) {
                IOException e2 = e;
                this.mQuit = true;
                e2.printStackTrace();
            }
        }

        public void close() {
            this.mQuit = true;
            interrupt();
        }

        public LocalSocket getRecvSocket() {
            return this.m_RecvSocket;
        }

        public void run() {
            while (!this.mQuit) {
                try {
                    this.m_RecvSocket = this.mSuLocalServerSocket.accept();
                    if (this.m_RecvThread != null && this.m_RecvThread.isAlive()) {
                        this.m_RecvThread.close();
                    }
                    this.m_RecvThread = new RecvThread(this.m_RecvSocket);
                    this.m_RecvThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static {
        NETWORK3G_CONTENT_URI = Uri.parse("content://network3g/carriers");
        PREFERAPN_URI = Uri.parse(PREFERRED_APN_URI);
    }

    public Network3GListener(Context context) {
        this.mMCC = "0";
        this.mMNC = "0";
        this.mContext = context;
        this.serverForSetting = new ServerThread(SOCKET_NAME_FOR_SETTINGS);
        this.serverForDriver = new ServerThread(SOCKET_NAME_FOR_DRIVER);
        this.serverForSetting.start();
        this.serverForDriver.start();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void consumeAccept(android.net.LocalSocket r14_recv) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.Network3GListener.consumeAccept(android.net.LocalSocket):void");
        /*
        r13 = this;
        r12 = -1;
        r3 = 0;
        if (r14 == 0) goto L_0x0027;
    L_0x0004:
        r6 = r14.getInputStream();	 Catch:{ IOException -> 0x0038 }
        r8 = r6.read();	 Catch:{ IOException -> 0x0038 }
        r4 = 0;
        r2 = -1;
        r0 = -1;
        r9 = -1;
        r7 = 0;
        r1 = r0;
    L_0x0012:
        if (r8 == r12) goto L_0x0057;
    L_0x0014:
        if (r4 != 0) goto L_0x0028;
    L_0x0016:
        r9 = r8;
        r0 = r1;
    L_0x0018:
        r4 = r4 + 1;
        if (r2 == r12) goto L_0x004b;
    L_0x001c:
        if (r0 == r12) goto L_0x004b;
    L_0x001e:
        if (r0 != r2) goto L_0x004b;
    L_0x0020:
        r10 = 10;
        if (r9 != r10) goto L_0x003d;
    L_0x0024:
        r6.close();	 Catch:{ IOException -> 0x0038 }
    L_0x0027:
        return;
    L_0x0028:
        r10 = 1;
        if (r4 != r10) goto L_0x0032;
    L_0x002b:
        r2 = (byte) r8;	 Catch:{ IOException -> 0x0038 }
        r0 = 0;
        if (r2 <= 0) goto L_0x0018;
    L_0x002f:
        r3 = new byte[r2];	 Catch:{ IOException -> 0x0038 }
        goto L_0x0018;
    L_0x0032:
        r0 = r1 + 1;
        r10 = (byte) r8;	 Catch:{ IOException -> 0x0038 }
        r3[r1] = r10;	 Catch:{ IOException -> 0x0038 }
        goto L_0x0018;
    L_0x0038:
        r5 = move-exception;
        r5.printStackTrace();
        goto L_0x0027;
    L_0x003d:
        if (r2 <= 0) goto L_0x0051;
    L_0x003f:
        r10 = new java.lang.String;	 Catch:{ IOException -> 0x0038 }
        r11 = 0;
        r10.<init>(r3, r11, r2);	 Catch:{ IOException -> 0x0038 }
        r13.consumeData(r14, r9, r10);	 Catch:{ IOException -> 0x0038 }
    L_0x0048:
        r2 = -1;
        r0 = -1;
        r4 = 0;
    L_0x004b:
        r8 = r6.read();	 Catch:{ IOException -> 0x0038 }
        r1 = r0;
        goto L_0x0012;
    L_0x0051:
        r10 = "";
        r13.consumeData(r14, r9, r10);	 Catch:{ IOException -> 0x0038 }
        goto L_0x0048;
    L_0x0057:
        r0 = r1;
        goto L_0x0024;
        */
    }

    private void consumeData(LocalSocket recv, int type, String msg) {
        new ConsumeTask(recv, type, msg).execute(new Void[0]);
    }

    private String getSelectedApnKey() {
        String key = null;
        Cursor cursor = this.mContext.getContentResolver().query(PREFERAPN_URI, new String[]{"_id"}, null, null, "name ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            key = cursor.getString(M3G_REQ_MCC);
        }
        cursor.close();
        return key;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void requetApn(android.net.LocalSocket r19_sender) {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.util.Network3GListener.requetApn(android.net.LocalSocket):void");
        /*
        r18 = this;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "mcc=\"";
        r2 = r2.append(r3);
        r0 = r18;
        r3 = r0.mMCC;
        r2 = r2.append(r3);
        r3 = "\"";
        r2 = r2.append(r3);
        r3 = "and mnc=\"";
        r2 = r2.append(r3);
        r0 = r18;
        r3 = r0.mMNC;
        r2 = r2.append(r3);
        r3 = "\"";
        r2 = r2.append(r3);
        r5 = r2.toString();
        r0 = r18;
        r2 = r0.mMCC;
        if (r2 == 0) goto L_0x0067;
    L_0x0037:
        r0 = r18;
        r2 = r0.mMNC;
        if (r2 == 0) goto L_0x0067;
    L_0x003d:
        r2 = "Network3GListener";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "query mcc = ";
        r3 = r3.append(r4);
        r0 = r18;
        r4 = r0.mMCC;
        r3 = r3.append(r4);
        r4 = "; mnc = ";
        r3 = r3.append(r4);
        r0 = r18;
        r4 = r0.mMNC;
        r3 = r3.append(r4);
        r3 = r3.toString();
        android.util.Log.v(r2, r3);
    L_0x0067:
        r0 = r18;
        r2 = r0.mContext;
        r2 = r2.getContentResolver();
        r3 = NETWORK3G_CONTENT_URI;
        r4 = 6;
        r4 = new java.lang.String[r4];
        r6 = 0;
        r7 = "_id";
        r4[r6] = r7;
        r6 = 1;
        r7 = "name";
        r4[r6] = r7;
        r6 = 2;
        r7 = "apn";
        r4[r6] = r7;
        r6 = 3;
        r7 = "type";
        r4[r6] = r7;
        r6 = 4;
        r7 = "user";
        r4[r6] = r7;
        r6 = 5;
        r7 = "password";
        r4[r6] = r7;
        r6 = 0;
        r7 = "name ASC";
        r9 = r2.query(r3, r4, r5, r6, r7);
        r15 = r18.getSelectedApnKey();
        r9.moveToFirst();
    L_0x00a0:
        r2 = r9.isAfterLast();
        if (r2 != 0) goto L_0x00ff;
    L_0x00a6:
        r2 = 0;
        r11 = r9.getString(r2);
        r2 = 1;
        r12 = r9.getString(r2);
        r2 = 2;
        r8 = r9.getString(r2);
        r2 = 3;
        r16 = r9.getString(r2);
        r2 = 4;
        r17 = r9.getString(r2);
        r2 = 5;
        r13 = r9.getString(r2);
        if (r16 == 0) goto L_0x00d0;
    L_0x00c6:
        r2 = "mms";
        r0 = r16;
        r2 = r0.equals(r2);
        if (r2 != 0) goto L_0x0103;
    L_0x00d0:
        r14 = 1;
    L_0x00d1:
        if (r14 == 0) goto L_0x0130;
    L_0x00d3:
        if (r15 == 0) goto L_0x0105;
    L_0x00d5:
        r2 = r15.equals(r11);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        if (r2 == 0) goto L_0x0105;
    L_0x00db:
        r2 = 5;
        r0 = r19;
        sendData(r0, r2, r8);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 7;
        r0 = r19;
        r1 = r17;
        sendData(r0, r2, r1);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 9;
        r0 = r19;
        sendData(r0, r2, r13);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
    L_0x00ff:
        r9.close();
        return;
    L_0x0103:
        r14 = 0;
        goto L_0x00d1;
    L_0x0105:
        if (r15 != 0) goto L_0x0130;
    L_0x0107:
        r2 = 5;
        r0 = r19;
        sendData(r0, r2, r8);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 7;
        r0 = r19;
        r1 = r17;
        sendData(r0, r2, r1);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 9;
        r0 = r19;
        sendData(r0, r2, r13);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        r2 = 50;
        java.lang.Thread.sleep(r2);	 Catch:{ IOException -> 0x012c, InterruptedException -> 0x0135 }
        goto L_0x00ff;
    L_0x012c:
        r10 = move-exception;
        r10.printStackTrace();
    L_0x0130:
        r9.moveToNext();
        goto L_0x00a0;
    L_0x0135:
        r10 = move-exception;
        r10.printStackTrace();
        goto L_0x0130;
        */
    }

    public static void sendData(LocalSocket sender, int type, String message) throws IOException {
        if (sender == null) {
            return;
        }
        if (message != null) {
            byte[] msgByte = message.getBytes();
            byte[] sendData = new byte[(msgByte.length + 2)];
            sendData[0] = (byte) type;
            sendData[1] = (byte) msgByte.length;
            int i = M3G_REQ_MCC;
            while (i < msgByte.length) {
                sendData[i + 2] = msgByte[i];
                i++;
            }
            sender.getOutputStream().write(sendData);
            sender.getOutputStream().flush();
        } else {
            sender.getOutputStream().write(new byte[]{(byte) type, (byte) 0});
            sender.getOutputStream().flush();
        }
    }
}