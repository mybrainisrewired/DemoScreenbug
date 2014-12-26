package android.support.v4.widget;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

public class SearchViewCompat {
    private static final SearchViewCompatImpl IMPL;

    public static abstract class OnCloseListenerCompat {
        final Object mListener;

        public OnCloseListenerCompat() {
            this.mListener = IMPL.newOnCloseListener(this);
        }

        public boolean onClose() {
            return false;
        }
    }

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
        CharSequence getQuery(View view);

        boolean isIconified(View view);

        boolean isQueryRefinementEnabled(View view);

        boolean isSubmitButtonEnabled(View view);

        Object newOnCloseListener(android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat onCloseListenerCompat);

        Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat onQueryTextListenerCompat);

        View newSearchView(Context context);

        void setIconified(View view, boolean z);

        void setImeOptions(View view, int i);

        void setInputType(View view, int i);

        void setMaxWidth(View view, int i);

        void setOnCloseListener(Object obj, Object obj2);

        void setOnQueryTextListener(Object obj, Object obj2);

        void setQuery(View view, CharSequence charSequence, boolean z);

        void setQueryHint(View view, CharSequence charSequence);

        void setQueryRefinementEnabled(View view, boolean z);

        void setSearchableInfo(View view, ComponentName componentName);

        void setSubmitButtonEnabled(View view, boolean z);
    }

    static class SearchViewCompatStubImpl implements SearchViewCompatImpl {
        SearchViewCompatStubImpl() {
        }

        public CharSequence getQuery(View searchView) {
            return null;
        }

        public boolean isIconified(View searchView) {
            return true;
        }

        public boolean isQueryRefinementEnabled(View searchView) {
            return false;
        }

        public boolean isSubmitButtonEnabled(View searchView) {
            return false;
        }

        public Object newOnCloseListener(android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat listener) {
            return null;
        }

        public Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat listener) {
            return null;
        }

        public View newSearchView(Context context) {
            return null;
        }

        public void setIconified(View searchView, boolean iconify) {
        }

        public void setImeOptions(View searchView, int imeOptions) {
        }

        public void setInputType(View searchView, int inputType) {
        }

        public void setMaxWidth(View searchView, int maxpixels) {
        }

        public void setOnCloseListener(Object searchView, Object listener) {
        }

        public void setOnQueryTextListener(Object searchView, Object listener) {
        }

        public void setQuery(View searchView, CharSequence query, boolean submit) {
        }

        public void setQueryHint(View searchView, CharSequence hint) {
        }

        public void setQueryRefinementEnabled(View searchView, boolean enable) {
        }

        public void setSearchableInfo(View searchView, ComponentName searchableComponent) {
        }

        public void setSubmitButtonEnabled(View searchView, boolean enabled) {
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

        class AnonymousClass_2 implements OnCloseListenerCompatBridge {
            final /* synthetic */ android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat val$listener;

            AnonymousClass_2(android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat onCloseListenerCompat) {
                this.val$listener = onCloseListenerCompat;
            }

            public boolean onClose() {
                return this.val$listener.onClose();
            }
        }

        SearchViewCompatHoneycombImpl() {
        }

        public CharSequence getQuery(View searchView) {
            return SearchViewCompatHoneycomb.getQuery(searchView);
        }

        public boolean isIconified(View searchView) {
            return SearchViewCompatHoneycomb.isIconified(searchView);
        }

        public boolean isQueryRefinementEnabled(View searchView) {
            return SearchViewCompatHoneycomb.isQueryRefinementEnabled(searchView);
        }

        public boolean isSubmitButtonEnabled(View searchView) {
            return SearchViewCompatHoneycomb.isSubmitButtonEnabled(searchView);
        }

        public Object newOnCloseListener(android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat listener) {
            return SearchViewCompatHoneycomb.newOnCloseListener(new AnonymousClass_2(listener));
        }

        public Object newOnQueryTextListener(android.support.v4.widget.SearchViewCompat.OnQueryTextListenerCompat listener) {
            return SearchViewCompatHoneycomb.newOnQueryTextListener(new AnonymousClass_1(listener));
        }

        public View newSearchView(Context context) {
            return SearchViewCompatHoneycomb.newSearchView(context);
        }

        public void setIconified(View searchView, boolean iconify) {
            SearchViewCompatHoneycomb.setIconified(searchView, iconify);
        }

        public void setMaxWidth(View searchView, int maxpixels) {
            SearchViewCompatHoneycomb.setMaxWidth(searchView, maxpixels);
        }

        public void setOnCloseListener(Object searchView, Object listener) {
            SearchViewCompatHoneycomb.setOnCloseListener(searchView, listener);
        }

        public void setOnQueryTextListener(Object searchView, Object listener) {
            SearchViewCompatHoneycomb.setOnQueryTextListener(searchView, listener);
        }

        public void setQuery(View searchView, CharSequence query, boolean submit) {
            SearchViewCompatHoneycomb.setQuery(searchView, query, submit);
        }

        public void setQueryHint(View searchView, CharSequence hint) {
            SearchViewCompatHoneycomb.setQueryHint(searchView, hint);
        }

        public void setQueryRefinementEnabled(View searchView, boolean enable) {
            SearchViewCompatHoneycomb.setQueryRefinementEnabled(searchView, enable);
        }

        public void setSearchableInfo(View searchView, ComponentName searchableComponent) {
            SearchViewCompatHoneycomb.setSearchableInfo(searchView, searchableComponent);
        }

        public void setSubmitButtonEnabled(View searchView, boolean enabled) {
            SearchViewCompatHoneycomb.setSubmitButtonEnabled(searchView, enabled);
        }
    }

    static class SearchViewCompatIcsImpl extends SearchViewCompatHoneycombImpl {
        SearchViewCompatIcsImpl() {
        }

        public View newSearchView(Context context) {
            return SearchViewCompatIcs.newSearchView(context);
        }

        public void setImeOptions(View searchView, int imeOptions) {
            SearchViewCompatIcs.setImeOptions(searchView, imeOptions);
        }

        public void setInputType(View searchView, int inputType) {
            SearchViewCompatIcs.setInputType(searchView, inputType);
        }
    }

    static {
        if (VERSION.SDK_INT >= 14) {
            IMPL = new SearchViewCompatIcsImpl();
        } else if (VERSION.SDK_INT >= 11) {
            IMPL = new SearchViewCompatHoneycombImpl();
        } else {
            IMPL = new SearchViewCompatStubImpl();
        }
    }

    private SearchViewCompat(Context context) {
    }

    public static CharSequence getQuery(View searchView) {
        return IMPL.getQuery(searchView);
    }

    public static boolean isIconified(View searchView) {
        return IMPL.isIconified(searchView);
    }

    public static boolean isQueryRefinementEnabled(View searchView) {
        return IMPL.isQueryRefinementEnabled(searchView);
    }

    public static boolean isSubmitButtonEnabled(View searchView) {
        return IMPL.isSubmitButtonEnabled(searchView);
    }

    public static View newSearchView(Context context) {
        return IMPL.newSearchView(context);
    }

    public static void setIconified(View searchView, boolean iconify) {
        IMPL.setIconified(searchView, iconify);
    }

    public static void setImeOptions(View searchView, int imeOptions) {
        IMPL.setImeOptions(searchView, imeOptions);
    }

    public static void setInputType(View searchView, int inputType) {
        IMPL.setInputType(searchView, inputType);
    }

    public static void setMaxWidth(View searchView, int maxpixels) {
        IMPL.setMaxWidth(searchView, maxpixels);
    }

    public static void setOnCloseListener(View searchView, OnCloseListenerCompat listener) {
        IMPL.setOnCloseListener(searchView, listener.mListener);
    }

    public static void setOnQueryTextListener(View searchView, OnQueryTextListenerCompat listener) {
        IMPL.setOnQueryTextListener(searchView, listener.mListener);
    }

    public static void setQuery(View searchView, CharSequence query, boolean submit) {
        IMPL.setQuery(searchView, query, submit);
    }

    public static void setQueryHint(View searchView, CharSequence hint) {
        IMPL.setQueryHint(searchView, hint);
    }

    public static void setQueryRefinementEnabled(View searchView, boolean enable) {
        IMPL.setQueryRefinementEnabled(searchView, enable);
    }

    public static void setSearchableInfo(View searchView, ComponentName searchableComponent) {
        IMPL.setSearchableInfo(searchView, searchableComponent);
    }

    public static void setSubmitButtonEnabled(View searchView, boolean enabled) {
        IMPL.setSubmitButtonEnabled(searchView, enabled);
    }
}