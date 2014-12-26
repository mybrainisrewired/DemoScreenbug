package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

public class SearchViewCompat {
    private static final SearchViewCompatImpl IMPL;

    public static abstract class OnQueryTextListenerCompat {
        final Object mListener;

        public OnQueryTextListenerCompat() {
            this.mListener = IMPL.newOnQueryTextListener(this);
        }

        public boolean onQueryTextChange(String newText) {
            return false;
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    static interface SearchViewCompatImpl {
        Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat onQueryTextListenerCompat);

        View newSearchView(Context context);

        void setOnQueryTextListener(Object obj, Object obj2);
    }

    static class SearchViewCompatStubImpl implements SearchViewCompatImpl {
        SearchViewCompatStubImpl() {
        }

        public Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat listener) {
            return null;
        }

        public View newSearchView(Context context) {
            return null;
        }

        public void setOnQueryTextListener(Object searchView, Object listener) {
        }
    }

    static class SearchViewCompatHoneycombImpl extends SearchViewCompatStubImpl {

        class AnonymousClass_1 implements OnQueryTextListenerCompatBridge {
            final /* synthetic */ android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat val$listener;

            AnonymousClass_1(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat onQueryTextListenerCompat) {
                this.val$listener = onQueryTextListenerCompat;
            }

            public boolean onQueryTextChange(String newText) {
                return this.val$listener.onQueryTextChange(newText);
            }

            public boolean onQueryTextSubmit(String query) {
                return this.val$listener.onQueryTextSubmit(query);
            }
        }

        SearchViewCompatHoneycombImpl() {
        }

        public Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat listener) {
            return SearchViewCompatHoneycomb.newOnQueryTextListener(new AnonymousClass_1(listener));
        }

        public View newSearchView(Context context) {
            return SearchViewCompatHoneycomb.newSearchView(context);
        }

        public void setOnQueryTextListener(Object searchView, Object listener) {
            SearchViewCompatHoneycomb.setOnQueryTextListener(searchView, listener);
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            IMPL = new SearchViewCompatHoneycombImpl();
        } else {
            IMPL = new SearchViewCompatStubImpl();
        }
    }

    private SearchViewCompat(Context context) {
    }

    public static View newSearchView(Context context) {
        return IMPL.newSearchView(context);
    }

    public static void setOnQueryTextListener(View searchView, OnQueryTextListenerCompat listener) {
        IMPL.setOnQueryTextListener(searchView, listener.mListener);
    }
}