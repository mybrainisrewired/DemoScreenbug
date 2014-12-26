package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
    private static final AccessibilityNodeProviderImpl IMPL;
    private final Object mProvider;

    static interface AccessibilityNodeProviderImpl {
        Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat);
    }

    static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl {
        AccessibilityNodeProviderStubImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
            return null;
        }
    }

    static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl {

        class AnonymousClass_1 implements AccessibilityNodeInfoBridge {
            final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

            AnonymousClass_1(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
                this.val$compat = accessibilityNodeProviderCompat;
            }

            public Object createAccessibilityNodeInfo(int virtualViewId) {
                AccessibilityNodeInfoCompat compatInfo = this.val$compat.createAccessibilityNodeInfo(virtualViewId);
                return compatInfo == null ? null : compatInfo.getInfo();
            }

            public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
                List<AccessibilityNodeInfoCompat> compatInfos = this.val$compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                List<Object> infos = new ArrayList();
                int infoCount = compatInfos.size();
                int i = 0;
                while (i < infoCount) {
                    infos.add(((AccessibilityNodeInfoCompat) compatInfos.get(i)).getInfo());
                    i++;
                }
                return infos;
            }

            public boolean performAction(int virtualViewId, int action, Bundle arguments) {
                return this.val$compat.performAction(virtualViewId, action, arguments);
            }
        }

        AccessibilityNodeProviderJellyBeanImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AnonymousClass_1(compat));
        }
    }

    static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl {

        class AnonymousClass_1 implements AccessibilityNodeInfoBridge {
            final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

            AnonymousClass_1(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
                this.val$compat = accessibilityNodeProviderCompat;
            }

            public Object createAccessibilityNodeInfo(int virtualViewId) {
                AccessibilityNodeInfoCompat compatInfo = this.val$compat.createAccessibilityNodeInfo(virtualViewId);
                return compatInfo == null ? null : compatInfo.getInfo();
            }

            public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
                List<AccessibilityNodeInfoCompat> compatInfos = this.val$compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                List<Object> infos = new ArrayList();
                int infoCount = compatInfos.size();
                int i = 0;
                while (i < infoCount) {
                    infos.add(((AccessibilityNodeInfoCompat) compatInfos.get(i)).getInfo());
                    i++;
                }
                return infos;
            }

            public Object findFocus(int focus) {
                AccessibilityNodeInfoCompat compatInfo = this.val$compat.findFocus(focus);
                return compatInfo == null ? null : compatInfo.getInfo();
            }

            public boolean performAction(int virtualViewId, int action, Bundle arguments) {
                return this.val$compat.performAction(virtualViewId, action, arguments);
            }
        }

        AccessibilityNodeProviderKitKatImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AnonymousClass_1(compat));
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            IMPL = new AccessibilityNodeProviderKitKatImpl();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new AccessibilityNodeProviderJellyBeanImpl();
        } else {
            IMPL = new AccessibilityNodeProviderStubImpl();
        }
    }

    public AccessibilityNodeProviderCompat() {
        this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
    }

    public AccessibilityNodeProviderCompat(Object provider) {
        this.mProvider = provider;
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
        return null;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
        return null;
    }

    public AccessibilityNodeInfoCompat findFocus(int focus) {
        return null;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        return false;
    }
}