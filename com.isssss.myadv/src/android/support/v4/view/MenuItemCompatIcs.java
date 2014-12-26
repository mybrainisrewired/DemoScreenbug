package android.support.v4.view;

import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;

class MenuItemCompatIcs {

    static class OnActionExpandListenerWrapper implements OnActionExpandListener {
        private SupportActionExpandProxy mWrapped;

        public OnActionExpandListenerWrapper(SupportActionExpandProxy wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean onMenuItemActionCollapse(MenuItem item) {
            return this.mWrapped.onMenuItemActionCollapse(item);
        }

        public boolean onMenuItemActionExpand(MenuItem item) {
            return this.mWrapped.onMenuItemActionExpand(item);
        }
    }

    static interface SupportActionExpandProxy {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    MenuItemCompatIcs() {
    }

    public static boolean collapseActionView(MenuItem item) {
        return item.collapseActionView();
    }

    public static boolean expandActionView(MenuItem item) {
        return item.expandActionView();
    }

    public static boolean isActionViewExpanded(MenuItem item) {
        return item.isActionViewExpanded();
    }

    public static MenuItem setOnActionExpandListener(MenuItem item, SupportActionExpandProxy listener) {
        return item.setOnActionExpandListener(new OnActionExpandListenerWrapper(listener));
    }
}