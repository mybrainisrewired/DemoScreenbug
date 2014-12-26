package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;
import com.wmt.data.MediaObject;
import com.wmt.opengl.grid.ItemAnimation;
import com.wmt.util.MmInfo;

public class KeyEventCompat {
    static final KeyEventVersionImpl IMPL;

    static interface KeyEventVersionImpl {
        boolean metaStateHasModifiers(int i, int i2);

        boolean metaStateHasNoModifiers(int i);

        int normalizeMetaState(int i);
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

        public boolean metaStateHasModifiers(int metaState, int modifiers) {
            return metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(normalizeMetaState(metaState) & 247, modifiers, 1, MediaObject.SUPPORT_FULL_IMAGE, MediaObject.SUPPORT_PLAY), modifiers, ItemAnimation.CUR_Z, MmInfo.MMINFO_MEM_SIZE, MediaObject.SUPPORT_SETAS) == modifiers;
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
    }

    static class HoneycombKeyEventVersionImpl implements KeyEventVersionImpl {
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

    public static boolean hasModifiers(KeyEvent event, int modifiers) {
        return IMPL.metaStateHasModifiers(event.getMetaState(), modifiers);
    }

    public static boolean hasNoModifiers(KeyEvent event) {
        return IMPL.metaStateHasNoModifiers(event.getMetaState());
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
}