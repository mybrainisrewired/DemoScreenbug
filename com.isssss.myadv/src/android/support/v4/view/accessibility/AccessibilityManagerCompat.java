package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

public class AccessibilityManagerCompat {
    private static final AccessibilityManagerVersionImpl IMPL;

    static interface AccessibilityManagerVersionImpl {
        boolean addAccessibilityStateChangeListener(AccessibilityManager accessibilityManager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat accessibilityStateChangeListenerCompat);

        List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager accessibilityManager, int i);

        List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager accessibilityManager);

        boolean isTouchExplorationEnabled(AccessibilityManager accessibilityManager);

        Object newAccessiblityStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat accessibilityStateChangeListenerCompat);

        boolean removeAccessibilityStateChangeListener(AccessibilityManager accessibilityManager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat accessibilityStateChangeListenerCompat);
    }

    public static abstract class AccessibilityStateChangeListenerCompat {
        final Object mListener;

        public AccessibilityStateChangeListenerCompat() {
            this.mListener = IMPL.newAccessiblityStateChangeListener(this);
        }

        public abstract void onAccessibilityStateChanged(boolean z);
    }

    static class AccessibilityManagerStubImpl implements AccessibilityManagerVersionImpl {
        AccessibilityManagerStubImpl() {
        }

        public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return false;
        }

        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
            return Collections.emptyList();
        }

        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
            return Collections.emptyList();
        }

        public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
            return false;
        }

        public Object newAccessiblityStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return null;
        }

        public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return false;
        }
    }

    static class AccessibilityManagerIcsImpl extends AccessibilityManagerStubImpl {

        class AnonymousClass_1 implements AccessibilityStateChangeListenerBridge {
            final /* synthetic */ android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat val$listener;

            AnonymousClass_1(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat accessibilityStateChangeListenerCompat) {
                this.val$listener = accessibilityStateChangeListenerCompat;
            }

            public void onAccessibilityStateChanged(boolean enabled) {
                this.val$listener.onAccessibilityStateChanged(enabled);
            }
        }

        AccessibilityManagerIcsImpl() {
        }

        public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(manager, listener.mListener);
        }

        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
            return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
        }

        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
            return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(manager);
        }

        public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
            return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(manager);
        }

        public Object newAccessiblityStateChangeListener(android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return AccessibilityManagerCompatIcs.newAccessibilityStateChangeListener(new AnonymousClass_1(listener));
        }

        public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
            return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(manager, listener.mListener);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityManagerIcsImpl();
        } else {
            IMPL = new AccessibilityManagerStubImpl();
        }
    }

    public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerCompat listener) {
        return IMPL.addAccessibilityStateChangeListener(manager, listener);
    }

    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
        return IMPL.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
    }

    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
        return IMPL.getInstalledAccessibilityServiceList(manager);
    }

    public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
        return IMPL.isTouchExplorationEnabled(manager);
    }

    public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerCompat listener) {
        return IMPL.removeAccessibilityStateChangeListener(manager, listener);
    }
}