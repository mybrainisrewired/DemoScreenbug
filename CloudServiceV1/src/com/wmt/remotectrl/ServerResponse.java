package com.wmt.remotectrl;

import com.wmt.remotectrl.EventPacket.OnSocketMsgListener;
import java.net.DatagramSocket;

public class ServerResponse extends Thread {
    private static String TAG;
    OnSocketMsgListener mSml;
    DatagramSocket mSocket;

    static {
        TAG = "ServerResponse";
    }

    public ServerResponse(DatagramSocket socket, OnSocketMsgListener srl) {
        this.mSocket = socket;
        this.mSml = srl;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void listenResponses() throws java.io.IOException {
        throw new UnsupportedOperationException("Method not decompiled: com.wmt.remotectrl.ServerResponse.listenResponses():void");
        /*
        r10 = this;
    L_0x0000:
        r7 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r1 = new byte[r7];	 Catch:{ Exception -> 0x0055 }
        r5 = new java.net.DatagramPacket;	 Catch:{ Exception -> 0x0055 }
        r7 = r1.length;	 Catch:{ Exception -> 0x0055 }
        r5.<init>(r1, r7);	 Catch:{ Exception -> 0x0055 }
        r7 = r10.mSocket;	 Catch:{ Exception -> 0x0055 }
        r7.receive(r5);	 Catch:{ Exception -> 0x0055 }
        r6 = new java.lang.String;	 Catch:{ Exception -> 0x0055 }
        r7 = r5.getData();	 Catch:{ Exception -> 0x0055 }
        r8 = 0;
        r9 = r5.getLength();	 Catch:{ Exception -> 0x0055 }
        r6.<init>(r7, r8, r9);	 Catch:{ Exception -> 0x0055 }
        r7 = com.wmt.remotectrl.EventPacket.SERVER_PREFIX;	 Catch:{ Exception -> 0x0055 }
        r7 = r6.startsWith(r7);	 Catch:{ Exception -> 0x0055 }
        if (r7 == 0) goto L_0x0000;
    L_0x0025:
        r3 = r5.getSocketAddress();	 Catch:{ Exception -> 0x0055 }
        r3 = (java.net.InetSocketAddress) r3;	 Catch:{ Exception -> 0x0055 }
        r0 = r3.getAddress();	 Catch:{ Exception -> 0x0055 }
        r4 = r0.getHostAddress();	 Catch:{ Exception -> 0x0055 }
        r7 = TAG;	 Catch:{ Exception -> 0x0055 }
        r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0055 }
        r8.<init>();	 Catch:{ Exception -> 0x0055 }
        r9 = "server ip is: ";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x0055 }
        r8 = r8.append(r4);	 Catch:{ Exception -> 0x0055 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x0055 }
        android.util.Log.d(r7, r8);	 Catch:{ Exception -> 0x0055 }
        r7 = r10.mSml;	 Catch:{ Exception -> 0x0055 }
        r8 = "Server";
        r9 = 10;
        r7.receivedResponse(r4, r8, r9);	 Catch:{ Exception -> 0x0055 }
        goto L_0x0000;
    L_0x0055:
        r2 = move-exception;
        r2.printStackTrace();
        r7 = r10.mSml;
        r7.onMySocketError();
        r7 = r10.mSocket;
        r7 = r7.isClosed();
        if (r7 != 0) goto L_0x006b;
    L_0x0066:
        r7 = r10.mSocket;
        r7.close();
    L_0x006b:
        return;
        */
    }

    public void run() {
        try {
            listenResponses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}