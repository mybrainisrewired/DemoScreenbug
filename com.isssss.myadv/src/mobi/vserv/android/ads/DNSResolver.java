package mobi.vserv.android.ads;

import java.net.InetAddress;
import java.net.UnknownHostException;

// compiled from: VservAdController.java
class DNSResolver implements Runnable {
    private String domain;
    private InetAddress inetAddr;

    public DNSResolver(String domain) {
        this.domain = domain;
    }

    public synchronized InetAddress get() {
        return this.inetAddr;
    }

    public void run() {
        try {
            set(InetAddress.getByName(this.domain));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public synchronized void set(InetAddress inetAddr) {
        this.inetAddr = inetAddr;
    }
}