package com.wmt.util;

import java.util.ArrayList;
import java.util.List;

public final class WorkThread extends Thread {
    public static final int BOTH_SIDES = 0;
    public static final int FIFO = 1;
    public static final int LIFO = 2;
    private WorkReq mCurReq;
    private int mDispatchMode;
    private List<WorkReq> mList;
    private int mListSize;
    private boolean mStop;
    private volatile boolean mWaiting;

    public WorkThread() {
        super("wmt.WorkThread");
        this.mStop = false;
        this.mList = new ArrayList();
        this.mCurReq = null;
        this.mWaiting = false;
        this.mDispatchMode = 0;
        this.mListSize = 20;
    }

    public void addReq(WorkReq req) {
        synchronized (this.mList) {
            this.mList.add(BOTH_SIDES, req);
            if (this.mWaiting) {
                this.mList.notify();
            }
        }
    }

    public void addReqWithLimit(WorkReq req) {
        synchronized (this.mList) {
            if (this.mList.size() > this.mListSize) {
                WorkReq reqRemoved = null;
                if (this.mDispatchMode == 0) {
                    if (this.mList.size() % 2 == 0) {
                        reqRemoved = this.mList.remove(this.mList.size() - 1);
                    } else {
                        reqRemoved = this.mList.remove(BOTH_SIDES);
                    }
                } else if (this.mDispatchMode == 1) {
                    reqRemoved = this.mList.remove(BOTH_SIDES);
                } else if (this.mDispatchMode == 2) {
                    reqRemoved = this.mList.remove(this.mList.size() - 1);
                }
                reqRemoved.cancel();
            }
            this.mList.add(BOTH_SIDES, req);
            if (this.mWaiting) {
                this.mList.notify();
            }
        }
    }

    public boolean cancelReq(WorkReq req) {
        synchronized (this.mList) {
            if (this.mCurReq == null || req != this.mCurReq) {
                int i = this.mList.size() - 1;
                while (i >= 0) {
                    if (((WorkReq) this.mList.get(i)) == req) {
                        req.cancel();
                        this.mList.remove(i);
                        return true;
                    } else {
                        i--;
                    }
                }
                return false;
            } else {
                this.mCurReq.cancel();
                return true;
            }
        }
    }

    public void cancelReqs(Match4Req m) {
        synchronized (this.mList) {
            if (this.mCurReq != null && m.matchs(this.mCurReq)) {
                this.mCurReq.cancel();
            }
            int i = this.mList.size() - 1;
            while (i >= 0) {
                WorkReq req = (WorkReq) this.mList.get(i);
                if (m.matchs(req)) {
                    req.cancel();
                    this.mList.remove(i);
                }
                i--;
            }
        }
    }

    public void cancelReqsList() {
        synchronized (this.mList) {
            int i = this.mList.size() - 1;
            while (i >= 0) {
                ((WorkReq) this.mList.remove(i)).cancel();
                i--;
            }
            if (this.mWaiting) {
                this.mList.notify();
            }
        }
    }

    public void exit() {
        synchronized (this.mList) {
            this.mStop = true;
            if (this.mCurReq != null) {
                this.mCurReq.cancel();
            }
            this.mList.clear();
            if (this.mWaiting) {
                this.mList.notify();
            }
        }
    }

    public WorkReq getDuplicateWorkReq(Match4Req m) {
        synchronized (this.mList) {
            WorkReq req1;
            if (this.mCurReq == null || !m.matchs(this.mCurReq)) {
                int i = BOTH_SIDES;
                while (i < this.mList.size()) {
                    req1 = (WorkReq) this.mList.get(i);
                    if (m.matchs(req1)) {
                        return req1;
                    } else {
                        i++;
                    }
                }
                return null;
            } else {
                req1 = this.mCurReq;
                return req1;
            }
        }
    }

    public boolean isDuplicateWorking(Match4Req m) {
        synchronized (this.mList) {
            if (this.mCurReq == null || !m.matchs(this.mCurReq)) {
                int i = BOTH_SIDES;
                while (i < this.mList.size()) {
                    if (m.matchs((WorkReq) this.mList.get(i))) {
                        return true;
                    } else {
                        i++;
                    }
                }
                return false;
            } else {
                return true;
            }
        }
    }

    public void run() {
        while (!this.mStop) {
            synchronized (this.mList) {
                while (!this.mStop && this.mList.isEmpty()) {
                    this.mWaiting = true;
                    try {
                        this.mList.wait();
                    } catch (InterruptedException e) {
                    }
                    this.mWaiting = false;
                }
                if (!this.mStop) {
                    if (this.mDispatchMode == 0) {
                        if (this.mList.size() % 2 == 0) {
                            this.mCurReq = (WorkReq) this.mList.remove(BOTH_SIDES);
                        } else {
                            this.mCurReq = (WorkReq) this.mList.remove(this.mList.size() - 1);
                        }
                    } else if (this.mDispatchMode == 1) {
                        this.mCurReq = (WorkReq) this.mList.remove(this.mList.size() - 1);
                    } else if (this.mDispatchMode == 2) {
                        this.mCurReq = (WorkReq) this.mList.remove(BOTH_SIDES);
                    }
                }
            }
            if (this.mCurReq != null) {
                this.mCurReq.execute();
                synchronized (this.mList) {
                    this.mCurReq = null;
                }
            }
        }
        synchronized (this.mList) {
            this.mList.clear();
        }
    }

    public void setDispatchMode(int mode) {
        this.mDispatchMode = mode;
    }

    public void setReqListSize(int size) {
        this.mListSize = size;
    }
}