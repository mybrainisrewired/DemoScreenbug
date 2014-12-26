package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import com.google.ads.AdSize;
import java.util.ArrayList;

public class TransportMediator extends TransportController {
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;

    public TransportMediator(Activity activity, TransportPerformer callbacks) {
        this(activity, null, callbacks);
    }

    private TransportMediator(Context activity, View view, TransportPerformer callbacks) {
        this.mListeners = new ArrayList();
        this.mTransportKeyCallback = new TransportMediatorCallback() {
            public long getPlaybackPosition() {
                return TransportMediator.this.mCallbacks.onGetCurrentPosition();
            }

            public void handleAudioFocusChange(int focusChange) {
                TransportMediator.this.mCallbacks.onAudioFocusChange(focusChange);
            }

            public void handleKey(KeyEvent key) {
                key.dispatch(TransportMediator.this.mKeyEventCallback);
            }

            public void playbackPositionUpdate(long newPositionMs) {
                TransportMediator.this.mCallbacks.onSeekTo(newPositionMs);
            }
        };
        this.mKeyEventCallback = new Callback() {
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                return TransportMediator.isMediaKey(keyCode) ? TransportMediator.this.mCallbacks.onMediaButtonDown(keyCode, event) : false;
            }

            public boolean onKeyLongPress(int keyCode, KeyEvent event) {
                return false;
            }

            public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
                return false;
            }

            public boolean onKeyUp(int keyCode, KeyEvent event) {
                return TransportMediator.isMediaKey(keyCode) ? TransportMediator.this.mCallbacks.onMediaButtonUp(keyCode, event) : false;
            }
        };
        this.mContext = activity != null ? activity : view.getContext();
        this.mCallbacks = callbacks;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (activity != null) {
            view = activity.getWindow().getDecorView();
        }
        this.mView = view;
        this.mDispatcherState = KeyEventCompat.getKeyDispatcherState(this.mView);
        if (VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
        } else {
            this.mController = null;
        }
    }

    public TransportMediator(View view, TransportPerformer callbacks) {
        this(null, view, callbacks);
    }

    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener[] listeners = new TransportStateListener[this.mListeners.size()];
        this.mListeners.toArray(listeners);
        return listeners;
    }

    static boolean isMediaKey(int keyCode) {
        switch (keyCode) {
            case 79:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case AdSize.LARGE_AD_HEIGHT:
            case 91:
            case KEYCODE_MEDIA_PLAY:
            case KEYCODE_MEDIA_PAUSE:
            case KEYCODE_MEDIA_RECORD:
                return true;
            default:
                return false;
        }
    }

    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }

    private void reportPlayingChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            TransportStateListener[] arr$ = listeners;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                arr$[i$].onPlayingChanged(this);
                i$++;
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            TransportStateListener[] arr$ = listeners;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                arr$[i$].onTransportControlsChanged(this);
                i$++;
            }
        }
    }

    public void destroy() {
        this.mController.destroy();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return KeyEventCompat.dispatch(event, this.mKeyEventCallback, this.mDispatcherState, this);
    }

    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }

    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }

    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }

    public Object getRemoteControlClient() {
        return this.mController != null ? this.mController.getRemoteControlClient() : null;
    }

    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }

    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }

    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        pushControllerState();
        reportPlayingChanged();
    }

    public void refreshState() {
        pushControllerState();
        reportPlayingChanged();
        reportTransportControlsChanged();
    }

    public void registerStateListener(TransportStateListener listener) {
        this.mListeners.add(listener);
    }

    public void seekTo(long pos) {
        this.mCallbacks.onSeekTo(pos);
    }

    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        pushControllerState();
        reportPlayingChanged();
    }

    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        pushControllerState();
        reportPlayingChanged();
    }

    public void unregisterStateListener(TransportStateListener listener) {
        this.mListeners.remove(listener);
    }
}