package com.wmt.frameworkbridge.video;

import android.view.View;

public interface ControllerOverlay {

    public static interface Listener {
        int getBufferPercent();

        int getCurrentPosition();

        int getDuration();

        void onHidden();

        void onPlayPause();

        void onReplay();

        void onSeekEnd(int i);

        void onSeekMove(int i);

        void onSeekStart();

        void onShown();

        void startPreviousNext(boolean z);
    }

    public enum State {
        PLAYING,
        PAUSED,
        ENDED,
        ERROR,
        LOADING
    }

    State getCurrentState();

    View getView();

    void hide();

    void setCanReplay(boolean z);

    void setListener(Listener listener);

    void show();

    void showLoading(boolean z, int i);

    void updateState(State state);
}