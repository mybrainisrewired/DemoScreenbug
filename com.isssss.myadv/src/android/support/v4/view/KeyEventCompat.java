package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.media.TransportMediator;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;

public class KeyEventCompat {
    static final KeyEventVersionImpl IMPL;

    static interface KeyEventVersionImpl {
        boolean dispatch(KeyEvent keyEvent, Callback callback, Object obj, Object obj2);

        Object getKeyDispatcherState(View view);

        boolean isTracking(KeyEvent keyEvent);

        boolean metaStateHasModifiers(int i, int i2);

        boolean metaStateHasNoModifiers(int i);

        int normalizeMetaState(int i);

        void startTracking(KeyEvent keyEvent);
    }

    static class BaseKeyEventVersionImpl implements KeyEventVersionImpl {
        private static final int META_ALL_MASK = 247;
        private static final int META_MODIFIER_MASK = 247;

        BaseKeyEventVersionImpl() {
        }

        private static int metaStateFilterDirectionalModifiers(int metaState, int modifiers, int basic, int left, int right) {
            boolean wantBasic;
            boolean wantLeftOrRight = true;
            if ((modifiers & basic) != 0) {
                wantBasic = true;
            } else {
                wantBasic = false;
            }
            int directional = left | right;
            if ((modifiers & directional) == 0) {
                wantLeftOrRight = false;
            }
            if (!wantBasic) {
                return wantLeftOrRight ? metaState & (basic ^ -1) : metaState;
            } else {
                if (!wantLeftOrRight) {
                    return metaState & (directional ^ -1);
                }
                throw new IllegalArgumentException("bad arguments");
            }
        }

        public boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
            return event.dispatch(receiver);
        }

        public Object getKeyDispatcherState(View view) {
            return null;
        }

        public boolean isTracking(KeyEvent event) {
            return false;
        }

        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(metaState) & 247, modifiers, 1, TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD, TransportMediator.FLAG_KEY_MEDIA_NEXT), modifiers, MMAdView.TRANSITION_UP, ApiEventType.API_MRAID_GET_ORIENTATION, ApiEventType.API_MRAID_PLAY_AUDIO) == modifiers;
        }

        public boolean metaStateHasNoModifiers(int metaState) {
            return (normalizeMetaState(metaState) & 247) == 0;
        }

        public int normalizeMetaState(int metaState) {
            if ((metaState & 192) != 0) {
                metaState |= 1;
            }
            if ((metaState & 48) != 0) {
                metaState |= 2;
            }
            return metaState & 247;
        }

        public void startTracking(KeyEvent event) {
        }
    }

    static class EclairKeyEventVersionImpl extends BaseKeyEventVersionImpl {
        EclairKeyEventVersionImpl() {
        }

        public boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
            return KeyEventCompatEclair.dispatch(event, receiver, state, target);
        }

        public Object getKeyDispatcherState(View view) {
            return KeyEventCompatEclair.getKeyDispatcherState(view);
        }

        public boolean isTracking(KeyEvent event) {
            return KeyEventCompatEclair.isTracking(event);
        }

        public void startTracking(KeyEvent event) {
            KeyEventCompatEclair.startTracking(event);
        }
    }

    static class HoneycombKeyEventVersionImpl extends EclairKeyEventVersionImpl {
        HoneycombKeyEventVersionImpl() {
        }

        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return KeyEventCompatHoneycomb.metaStateHasModifiers(metaState, modifiers);
        }

        public boolean metaStateHasNoModifiers(int metaState) {
            return KeyEventCompatHoneycomb.metaStateHasNoModifiers(metaState);
        }

        public int normalizeMetaState(int metaState) {
            return KeyEventCompatHoneycomb.normalizeMetaState(metaState);
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            IMPL = new HoneycombKeyEventVersionImpl();
        } else {
            IMPL = new BaseKeyEventVersionImpl();
        }
    }

    public static boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
        return IMPL.dispatch(event, receiver, state, target);
    }

    public static Object getKeyDispatcherState(View view) {
        return IMPL.getKeyDispatcherState(view);
    }

    public static boolean hasModifiers(KeyEvent event, int modifiers) {
        return IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
    }

    public static boolean hasNoModifiers(KeyEvent event) {
        return IMPL.metaStateHasNoModifiers(event.getMetaState());
    }

    public static boolean isTracking(KeyEvent event) {
        return IMPL.isTracking(event);
    }

    public static boolean metaStateHasModifiers(int metaState, int modifiers) {
        return IMPL.metaStateHasModifiers(metaState, modifiers);
    }

    public static boolean metaStateHasNoModifiers(int metaState) {
        return IMPL.metaStateHasNoModifiers(metaState);
    }

    public static int normalizeMetaState(int metaState) {
        return IMPL.normalizeMetaState(metaState);
    }

    public static void startTracking(KeyEvent event) {
        IMPL.startTracking(event);
    }
}