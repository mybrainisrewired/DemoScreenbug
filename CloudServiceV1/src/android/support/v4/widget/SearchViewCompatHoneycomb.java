package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb {

    static class AnonymousClass_1 implements OnQueryTextListener {
        final /* synthetic */ OnQueryTextListenerCompatBridge val$listener;

        AnonymousClass_1(OnQueryTextListenerCompatBridge onQueryTextListenerCompatBridge) {
            this.val$listener = onQueryTextListenerCompatBridge;
        }

        public boolean onQueryTextChange(String newText) {
            return this.val$listener.onQueryTextChange(newText);
        }

        public boolean onQueryTextSubmit(String query) {
            return this.val$listener.onQueryTextSubmit(query);
        }
    }

    static interface OnQueryTextListenerCompatBridge {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    SearchViewCompatHoneycomb() {
    }

    public static Object newOnQueryTextListener(OnQueryTextListenerCompatBridge listener) {
        return new AnonymousClass_1(listener);
    }

    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    public static void setOnQueryTextListener(Object searchView, Object listener) {
        ((SearchView) searchView).setOnQueryTextListener((OnQueryTextListener) listener);
    }
}