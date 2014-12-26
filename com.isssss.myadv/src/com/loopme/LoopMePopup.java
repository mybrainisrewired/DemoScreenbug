package com.loopme;

import android.content.Intent;
import com.google.android.gms.drive.DriveFile;
import com.unity3d.player.UnityPlayer;

final class LoopMePopup {
    private static final String LOG_TAG;

    class AnonymousClass_1 implements Runnable {
        private final /* synthetic */ BaseLoopMe val$baseLoopme;

        AnonymousClass_1(BaseLoopMe baseLoopMe) {
            this.val$baseLoopme = baseLoopMe;
        }

        public void run() {
            LoopMePopup.this.startAdListActivity(this.val$baseLoopme);
        }
    }

    static {
        LOG_TAG = LoopMePopup.class.getSimpleName();
    }

    public LoopMePopup(BaseLoopMe baseLoopme) {
        if (baseLoopme == null) {
            Utilities.log(LOG_TAG, "Wrong parameter", LogLevel.ERROR);
        } else if (Utilities.isUnityProject()) {
            UnityPlayer.currentActivity.runOnUiThread(new AnonymousClass_1(baseLoopme));
        } else {
            startAdListActivity(baseLoopme);
        }
    }

    private void startAdListActivity(BaseLoopMe baseLoopme) {
        Utilities.log(LOG_TAG, "Starting Ad List Activity", LogLevel.DEBUG);
        BaseLoopMeHolder.put(baseLoopme);
        Intent intent = new Intent(baseLoopme.getActivity(), LoopMeAdListActivity.class);
        intent.addFlags(DriveFile.MODE_WRITE_ONLY);
        baseLoopme.getActivity().startActivity(intent);
    }
}