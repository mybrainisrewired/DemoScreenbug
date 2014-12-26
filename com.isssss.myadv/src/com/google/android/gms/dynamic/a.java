package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.inmobi.commons.internal.ApiStatCollector.ApiEventType;
import com.millennialmedia.android.MMAdView;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class a<T extends LifecycleDelegate> {
    private T Hj;
    private Bundle Hk;
    private LinkedList<a> Hl;
    private final f<T> Hm;

    static class AnonymousClass_5 implements OnClickListener {
        final /* synthetic */ int Hu;
        final /* synthetic */ Context pB;

        AnonymousClass_5(Context context, int i) {
            this.pB = context;
            this.Hu = i;
        }

        public void onClick(View v) {
            this.pB.startActivity(GooglePlayServicesUtil.b(this.pB, this.Hu));
        }
    }

    private static interface a {
        void b(LifecycleDelegate lifecycleDelegate);

        int getState();
    }

    class AnonymousClass_2 implements a {
        final /* synthetic */ Activity Ho;
        final /* synthetic */ Bundle Hp;
        final /* synthetic */ Bundle Hq;

        AnonymousClass_2(Activity activity, Bundle bundle, Bundle bundle2) {
            this.Ho = activity;
            this.Hp = bundle;
            this.Hq = bundle2;
        }

        public void b(LifecycleDelegate lifecycleDelegate) {
            a.this.Hj.onInflate(this.Ho, this.Hp, this.Hq);
        }

        public int getState() {
            return 0;
        }
    }

    class AnonymousClass_3 implements a {
        final /* synthetic */ Bundle Hq;

        AnonymousClass_3(Bundle bundle) {
            this.Hq = bundle;
        }

        public void b(LifecycleDelegate lifecycleDelegate) {
            a.this.Hj.onCreate(this.Hq);
        }

        public int getState() {
            return 1;
        }
    }

    class AnonymousClass_4 implements a {
        final /* synthetic */ Bundle Hq;
        final /* synthetic */ FrameLayout Hr;
        final /* synthetic */ LayoutInflater Hs;
        final /* synthetic */ ViewGroup Ht;

        AnonymousClass_4(FrameLayout frameLayout, LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            this.Hr = frameLayout;
            this.Hs = layoutInflater;
            this.Ht = viewGroup;
            this.Hq = bundle;
        }

        public void b(LifecycleDelegate lifecycleDelegate) {
            this.Hr.removeAllViews();
            this.Hr.addView(a.this.Hj.onCreateView(this.Hs, this.Ht, this.Hq));
        }

        public int getState() {
            return MMAdView.TRANSITION_UP;
        }
    }

    public a() {
        this.Hm = new f<T>() {
            public void a(LifecycleDelegate lifecycleDelegate) {
                a.this.Hj = lifecycleDelegate;
                Iterator it = a.this.Hl.iterator();
                while (it.hasNext()) {
                    ((a) it.next()).b(a.this.Hj);
                }
                a.this.Hl.clear();
                a.this.Hk = null;
            }
        };
    }

    private void a(Bundle bundle, a aVar) {
        if (this.Hj != null) {
            aVar.b(this.Hj);
        } else {
            if (this.Hl == null) {
                this.Hl = new LinkedList();
            }
            this.Hl.add(aVar);
            if (bundle != null) {
                if (this.Hk == null) {
                    this.Hk = (Bundle) bundle.clone();
                } else {
                    this.Hk.putAll(bundle);
                }
            }
            a(this.Hm);
        }
    }

    private void aR(int i) {
        while (!this.Hl.isEmpty() && ((a) this.Hl.getLast()).getState() >= i) {
            this.Hl.removeLast();
        }
    }

    public static void b(FrameLayout frameLayout) {
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        CharSequence c = GooglePlayServicesUtil.c(context, isGooglePlayServicesAvailable);
        CharSequence d = GooglePlayServicesUtil.d(context, isGooglePlayServicesAvailable);
        View linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        View textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(c);
        linearLayout.addView(textView);
        if (d != null) {
            View button = new Button(context);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(d);
            linearLayout.addView(button);
            button.setOnClickListener(new AnonymousClass_5(context, isGooglePlayServicesAvailable));
        }
    }

    protected void a(FrameLayout frameLayout) {
        b(frameLayout);
    }

    protected abstract void a(f<T> fVar);

    public T fW() {
        return this.Hj;
    }

    public void onCreate(Bundle savedInstanceState) {
        a(savedInstanceState, new AnonymousClass_3(savedInstanceState));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        a(savedInstanceState, new AnonymousClass_4(frameLayout, inflater, container, savedInstanceState));
        if (this.Hj == null) {
            a(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.Hj != null) {
            this.Hj.onDestroy();
        } else {
            aR(1);
        }
    }

    public void onDestroyView() {
        if (this.Hj != null) {
            this.Hj.onDestroyView();
        } else {
            aR(MMAdView.TRANSITION_UP);
        }
    }

    public void onInflate(Activity activity, Bundle attrs, Bundle savedInstanceState) {
        a(savedInstanceState, new AnonymousClass_2(activity, attrs, savedInstanceState));
    }

    public void onLowMemory() {
        if (this.Hj != null) {
            this.Hj.onLowMemory();
        }
    }

    public void onPause() {
        if (this.Hj != null) {
            this.Hj.onPause();
        } else {
            aR(ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES);
        }
    }

    public void onResume() {
        a(null, new a() {
            public void b(LifecycleDelegate lifecycleDelegate) {
                a.this.Hj.onResume();
            }

            public int getState() {
                return ApiEventType.API_MRAID_GET_ORIENTATION_PROPERTIES;
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        if (this.Hj != null) {
            this.Hj.onSaveInstanceState(outState);
        } else if (this.Hk != null) {
            outState.putAll(this.Hk);
        }
    }

    public void onStart() {
        a(null, new a() {
            public void b(LifecycleDelegate lifecycleDelegate) {
                a.this.Hj.onStart();
            }

            public int getState() {
                return MMAdView.TRANSITION_RANDOM;
            }
        });
    }

    public void onStop() {
        if (this.Hj != null) {
            this.Hj.onStop();
        } else {
            aR(MMAdView.TRANSITION_RANDOM);
        }
    }
}