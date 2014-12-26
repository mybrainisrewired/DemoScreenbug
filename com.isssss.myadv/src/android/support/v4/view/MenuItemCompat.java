package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MenuItemCompat {
    static final MenuVersionImpl IMPL;
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    static interface MenuVersionImpl {
        boolean collapseActionView(MenuItem menuItem);

        boolean expandActionView(MenuItem menuItem);

        View getActionView(MenuItem menuItem);

        boolean isActionViewExpanded(MenuItem menuItem);

        MenuItem setActionView(MenuItem menuItem, int i);

        MenuItem setActionView(MenuItem menuItem, View view);

        MenuItem setOnActionExpandListener(MenuItem menuItem, android.support.v4.view.MenuItemCompat.OnActionExpandListener onActionExpandListener);

        void setShowAsAction(MenuItem menuItem, int i);
    }

    public static interface OnActionExpandListener {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    static class BaseMenuVersionImpl implements MenuVersionImpl {
        BaseMenuVersionImpl() {
        }

        public boolean collapseActionView(MenuItem item) {
            return false;
        }

        public boolean expandActionView(MenuItem item) {
            return false;
        }

        public View getActionView(MenuItem item) {
            return null;
        }

        public boolean isActionViewExpanded(MenuItem item) {
            return false;
        }

        public MenuItem setActionView(MenuItem item, int resId) {
            return item;
        }

        public MenuItem setActionView(MenuItem item, View view) {
            return item;
        }

        public MenuItem setOnActionExpandListener(MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            return item;
        }

        public void setShowAsAction(MenuItem item, int actionEnum) {
        }
    }

    static class HoneycombMenuVersionImpl implements MenuVersionImpl {
        HoneycombMenuVersionImpl() {
        }

        public boolean collapseActionView(MenuItem item) {
            return false;
        }

        public boolean expandActionView(MenuItem item) {
            return false;
        }

        public View getActionView(MenuItem item) {
            return MenuItemCompatHoneycomb.getActionView(item);
        }

        public boolean isActionViewExpanded(MenuItem item) {
            return false;
        }

        public MenuItem setActionView(MenuItem item, int resId) {
            return MenuItemCompatHoneycomb.setActionView(item, resId);
        }

        public MenuItem setActionView(MenuItem item, View view) {
            return MenuItemCompatHoneycomb.setActionView(item, view);
        }

        public MenuItem setOnActionExpandListener(MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            return item;
        }

        public void setShowAsAction(MenuItem item, int actionEnum) {
            MenuItemCompatHoneycomb.setShowAsAction(item, actionEnum);
        }
    }

    static class IcsMenuVersionImpl extends HoneycombMenuVersionImpl {

        class AnonymousClass_1 implements SupportActionExpandProxy {
            final /* synthetic */ android.support.v4.view.MenuItemCompat.OnActionExpandListener val$listener;

            AnonymousClass_1(android.support.v4.view.MenuItemCompat.OnActionExpandListener onActionExpandListener) {
                this.val$listener = onActionExpandListener;
            }

            public boolean onMenuItemActionCollapse(MenuItem item) {
                return this.val$listener.onMenuItemActionCollapse(item);
            }

            public boolean onMenuItemActionExpand(MenuItem item) {
                return this.val$listener.onMenuItemActionExpand(item);
            }
        }

        IcsMenuVersionImpl() {
        }

        public boolean collapseActionView(MenuItem item) {
            return MenuItemCompatIcs.collapseActionView(item);
        }

        public boolean expandActionView(MenuItem item) {
            return MenuItemCompatIcs.expandActionView(item);
        }

        public boolean isActionViewExpanded(MenuItem item) {
            return MenuItemCompatIcs.isActionViewExpanded(item);
        }

        public MenuItem setOnActionExpandListener(MenuItem item, android.support.v4.view.MenuItemCompat.OnActionExpandListener listener) {
            return listener == null ? MenuItemCompatIcs.setOnActionExpandListener(item, null) : MenuItemCompatIcs.setOnActionExpandListener(item, new AnonymousClass_1(listener));
        }
    }

    static {
        int version = VERSION.SDK_INT;
        if (version >= 14) {
            IMPL = new IcsMenuVersionImpl();
        } else if (version >= 11) {
            IMPL = new HoneycombMenuVersionImpl();
        } else {
            IMPL = new BaseMenuVersionImpl();
        }
    }

    public static boolean collapseActionView(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).collapseActionView() : IMPL.collapseActionView(item);
    }

    public static boolean expandActionView(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).expandActionView() : IMPL.expandActionView(item);
    }

    public static ActionProvider getActionProvider(MenuItem item) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).getSupportActionProvider();
        }
        Log.w(TAG, "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    public static View getActionView(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).getActionView() : IMPL.getActionView(item);
    }

    public static boolean isActionViewExpanded(MenuItem item) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).isActionViewExpanded() : IMPL.isActionViewExpanded(item);
    }

    public static MenuItem setActionProvider(MenuItem item, ActionProvider provider) {
        if (item instanceof SupportMenuItem) {
            return ((SupportMenuItem) item).setSupportActionProvider(provider);
        }
        Log.w(TAG, "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return item;
    }

    public static MenuItem setActionView(MenuItem item, int resId) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).setActionView(resId) : IMPL.setActionView(item, resId);
    }

    public static MenuItem setActionView(MenuItem item, View view) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).setActionView(view) : IMPL.setActionView(item, view);
    }

    public static MenuItem setOnActionExpandListener(MenuItem item, OnActionExpandListener listener) {
        return item instanceof SupportMenuItem ? ((SupportMenuItem) item).setSupportOnActionExpandListener(listener) : IMPL.setOnActionExpandListener(item, listener);
    }

    public static void setShowAsAction(MenuItem item, int actionEnum) {
        if (item instanceof SupportMenuItem) {
            ((SupportMenuItem) item).setShowAsAction(actionEnum);
        } else {
            IMPL.setShowAsAction(item, actionEnum);
        }
    }
}