package mobi.vserv.android.ads;

public class VservController {
    VservAdController adController;

    public void resumeRefresh() {
        if (!this.adController.shouldRefresh) {
            this.adController.resumeRefresh();
        }
    }

    public void setRefresh(int value) {
        if (value <= 0) {
            this.adController.stopRefresh();
        } else if (value < 30) {
            this.adController.refreshRate = 30;
        } else {
            this.adController.refreshRate = value;
        }
    }

    public void setZone(String zoneId) {
        this.adController.zoneId = zoneId;
    }

    public void stopRefresh() {
        this.adController.stopRefresh();
    }
}