package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AttributeSet;
import android.util.Slog;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerImpl;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.IStatusBarService.Stub;
import com.android.systemui.R;
import com.android.systemui.statusbar.BaseStatusBar;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DelegateViewHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class NavigationBarView extends LinearLayout {
    static final boolean ANIMATE_HIDE_TRANSITION = false;
    static final boolean DEBUG = false;
    static final boolean DEBUG_DEADZONE = false;
    static final int MSG_CHECK_INVALID_LAYOUT = 8686;
    static final boolean NAVBAR_ALWAYS_AT_RIGHT = true;
    static final String TAG = "PhoneStatusBar/NavigationBarView";
    static final boolean WORKAROUND_INVALID_LAYOUT = true;
    private Drawable mBackAltIcon;
    private Drawable mBackAltLandIcon;
    private Drawable mBackIcon;
    private Drawable mBackLandIcon;
    protected IStatusBarService mBarService;
    int mBarSize;
    View mCurrentView;
    private DelegateViewHelper mDelegateHelper;
    int mDisabledFlags;
    final Display mDisplay;
    private H mHandler;
    boolean mHidden;
    OnTouchListener mLightsOutListener;
    boolean mLowProfile;
    int mNavigationIconHints;
    View[] mRotatedViews;
    boolean mShowMenu;
    boolean mVertical;

    class AnonymousClass_2 extends AnimatorListenerAdapter {
        final /* synthetic */ View val$lowLights;

        AnonymousClass_2(View view) {
            this.val$lowLights = view;
        }

        public void onAnimationEnd(Animator _a) {
            this.val$lowLights.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
    }

    private class H extends Handler {
        private H() {
        }

        public void handleMessage(Message m) {
            switch (m.what) {
                case MSG_CHECK_INVALID_LAYOUT:
                    String how = "" + m.obj;
                    int w = NavigationBarView.this.getWidth();
                    int h = NavigationBarView.this.getHeight();
                    int vw = NavigationBarView.this.mCurrentView.getWidth();
                    if (h != NavigationBarView.this.mCurrentView.getHeight() || w != vw) {
                        Slog.w(TAG, String.format("*** Invalid layout in navigation bar (%s this=%dx%d cur=%dx%d)", new Object[]{how, Integer.valueOf(w), Integer.valueOf(h), Integer.valueOf(vw), Integer.valueOf(vh)}));
                        NavigationBarView.this.requestLayout();
                    }
                default:
                    break;
            }
        }
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCurrentView = null;
        this.mRotatedViews = new View[4];
        this.mDisabledFlags = 0;
        this.mNavigationIconHints = 0;
        this.mHandler = new H(null);
        this.mLightsOutListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == 0) {
                    NavigationBarView.this.setLowProfile(DEBUG_DEADZONE, DEBUG_DEADZONE, DEBUG_DEADZONE);
                    try {
                        NavigationBarView.this.mBarService.setSystemUiVisibility(0, 1);
                    } catch (RemoteException e) {
                    }
                }
                return DEBUG_DEADZONE;
            }
        };
        this.mHidden = false;
        this.mDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.mBarService = Stub.asInterface(ServiceManager.getService("statusbar"));
        Resources res = this.mContext.getResources();
        this.mBarSize = res.getDimensionPixelSize(R.dimen.navigation_bar_size);
        this.mVertical = false;
        this.mShowMenu = false;
        this.mDelegateHelper = new DelegateViewHelper(this);
        this.mBackIcon = res.getDrawable(R.drawable.ic_sysbar_back);
        this.mBackLandIcon = res.getDrawable(R.drawable.ic_sysbar_back_land);
        this.mBackAltIcon = res.getDrawable(R.drawable.ic_sysbar_back_ime);
        this.mBackAltLandIcon = res.getDrawable(R.drawable.ic_sysbar_back_ime);
    }

    private String getResourceName(int resId) {
        if (resId == 0) {
            return "(null)";
        }
        try {
            return this.mContext.getResources().getResourceName(resId);
        } catch (NotFoundException e) {
            return "(unknown)";
        }
    }

    private void postCheckForInvalidLayout(String how) {
        this.mHandler.obtainMessage(MSG_CHECK_INVALID_LAYOUT, 0, 0, how).sendToTarget();
    }

    private static String visibilityToString(int vis) {
        switch (vis) {
            case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                return "INVISIBLE";
            case CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL:
                return "GONE";
            default:
                return "VISIBLE";
        }
    }

    public void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("NavigationBarView {");
        Rect r = new Rect();
        pw.println(String.format("      this: " + PhoneStatusBar.viewInfo(this) + " " + visibilityToString(getVisibility()), new Object[0]));
        getWindowVisibleDisplayFrame(r);
        boolean offscreen = (r.right > this.mDisplay.getRawWidth() || r.bottom > this.mDisplay.getRawHeight()) ? WORKAROUND_INVALID_LAYOUT : DEBUG_DEADZONE;
        pw.println("      window: " + r.toShortString() + " " + visibilityToString(getWindowVisibility()) + (offscreen ? " OFFSCREEN!" : ""));
        pw.println(String.format("      mCurrentView: id=%s (%dx%d) %s", new Object[]{getResourceName(this.mCurrentView.getId()), Integer.valueOf(this.mCurrentView.getWidth()), Integer.valueOf(this.mCurrentView.getHeight()), visibilityToString(this.mCurrentView.getVisibility())}));
        String str = "      disabled=0x%08x vertical=%s hidden=%s low=%s menu=%s";
        Object[] objArr = new Object[5];
        objArr[0] = Integer.valueOf(this.mDisabledFlags);
        objArr[1] = this.mVertical ? "true" : "false";
        objArr[2] = this.mHidden ? "true" : "false";
        objArr[3] = this.mLowProfile ? "true" : "false";
        objArr[4] = this.mShowMenu ? "true" : "false";
        pw.println(String.format(str, objArr));
        View back = getBackButton();
        View home = getHomeButton();
        View recent = getRecentsButton();
        View menu = getMenuButton();
        pw.println("      back: " + PhoneStatusBar.viewInfo(back) + " " + visibilityToString(back.getVisibility()));
        pw.println("      home: " + PhoneStatusBar.viewInfo(home) + " " + visibilityToString(home.getVisibility()));
        pw.println("      rcnt: " + PhoneStatusBar.viewInfo(recent) + " " + visibilityToString(recent.getVisibility()));
        pw.println("      menu: " + PhoneStatusBar.viewInfo(menu) + " " + visibilityToString(menu.getVisibility()));
        pw.println("    }");
    }

    public View getBackButton() {
        return this.mCurrentView.findViewById(R.id.back);
    }

    public View getHomeButton() {
        return this.mCurrentView.findViewById(R.id.home);
    }

    public View getMenuButton() {
        return this.mCurrentView.findViewById(R.id.menu);
    }

    public View getRecentsButton() {
        return this.mCurrentView.findViewById(R.id.recent_apps);
    }

    public void onFinishInflate() {
        View[] viewArr = this.mRotatedViews;
        View[] viewArr2 = this.mRotatedViews;
        View findViewById = findViewById(R.id.rot0);
        viewArr2[2] = findViewById;
        viewArr[0] = findViewById;
        this.mRotatedViews[1] = findViewById(R.id.rot90);
        this.mRotatedViews[3] = findViewById(R.id.rot90);
        this.mCurrentView = this.mRotatedViews[0];
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mDelegateHelper.onInterceptTouchEvent(event);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mDelegateHelper.setInitialTouchRegion(new View[]{getHomeButton(), getBackButton(), getRecentsButton()});
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        boolean newVertical = (w <= 0 || h <= w) ? DEBUG_DEADZONE : WORKAROUND_INVALID_LAYOUT;
        if (newVertical != this.mVertical) {
            this.mVertical = newVertical;
            reorient();
        }
        postCheckForInvalidLayout("sizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDelegateHelper != null) {
            this.mDelegateHelper.onInterceptTouchEvent(event);
        }
        return WORKAROUND_INVALID_LAYOUT;
    }

    public void reorient() {
        int rot = this.mDisplay.getRotation();
        int i = 0;
        while (i < 4) {
            this.mRotatedViews[i].setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            i++;
        }
        this.mCurrentView = this.mRotatedViews[rot];
        this.mCurrentView.setVisibility(0);
        setLowProfile(this.mLowProfile, DEBUG_DEADZONE, WORKAROUND_INVALID_LAYOUT);
        setDisabledFlags(this.mDisabledFlags, WORKAROUND_INVALID_LAYOUT);
        setMenuVisibility(this.mShowMenu, WORKAROUND_INVALID_LAYOUT);
        setNavigationIconHints(this.mNavigationIconHints, WORKAROUND_INVALID_LAYOUT);
    }

    public void setBar(BaseStatusBar phoneStatusBar) {
        this.mDelegateHelper.setBar(phoneStatusBar);
    }

    public void setDelegateView(View view) {
        this.mDelegateHelper.setDelegateView(view);
    }

    public void setDisabledFlags(int disabledFlags) {
        setDisabledFlags(disabledFlags, DEBUG_DEADZONE);
    }

    public void setDisabledFlags(int disabledFlags, boolean force) {
        int i = CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL;
        boolean z = WORKAROUND_INVALID_LAYOUT;
        if (force || this.mDisabledFlags != disabledFlags) {
            boolean disableHome;
            boolean disableRecent;
            boolean disableBack;
            int i2;
            this.mDisabledFlags = disabledFlags;
            if ((2097152 & disabledFlags) != 0) {
                disableHome = true;
            } else {
                disableHome = false;
            }
            if ((16777216 & disabledFlags) != 0) {
                disableRecent = true;
            } else {
                disableRecent = false;
            }
            if ((4194304 & disabledFlags) != 0) {
                disableBack = true;
            } else {
                disableBack = false;
            }
            if (!(disableHome && disableRecent && disableBack)) {
                z = false;
            }
            setSlippery(z);
            View backButton = getBackButton();
            if (disableBack) {
                i2 = 4;
            } else {
                i2 = 0;
            }
            backButton.setVisibility(i2);
            backButton = getHomeButton();
            if (disableHome) {
                i2 = 4;
            } else {
                i2 = 0;
            }
            backButton.setVisibility(i2);
            View recentsButton = getRecentsButton();
            if (!disableRecent) {
                i = 0;
            }
            recentsButton.setVisibility(i);
        }
    }

    public void setHidden(boolean hide) {
        if (hide != this.mHidden) {
            this.mHidden = hide;
            Slog.d(TAG, (hide ? "HIDING" : "SHOWING") + " navigation bar");
            setLowProfile(DEBUG_DEADZONE);
        }
    }

    public void setLowProfile(boolean lightsOut) {
        setLowProfile(lightsOut, WORKAROUND_INVALID_LAYOUT, DEBUG_DEADZONE);
    }

    public void setLowProfile(boolean lightsOut, boolean animate, boolean force) {
        long j = 250;
        float f = 1.0f;
        if (force || lightsOut != this.mLowProfile) {
            this.mLowProfile = lightsOut;
            View navButtons = this.mCurrentView.findViewById(R.id.nav_buttons);
            View lowLights = this.mCurrentView.findViewById(R.id.lights_out);
            navButtons.animate().cancel();
            lowLights.animate().cancel();
            if (animate) {
                AnimatorListener animatorListener;
                navButtons.animate().alpha(lightsOut ? 0.0f : 1.0f).setDuration(lightsOut ? 750 : 250).start();
                lowLights.setOnTouchListener(this.mLightsOutListener);
                if (lowLights.getVisibility() == 8) {
                    lowLights.setAlpha(0.0f);
                    lowLights.setVisibility(0);
                }
                ViewPropertyAnimator animate2 = lowLights.animate();
                if (!lightsOut) {
                    f = 0.0f;
                }
                animate2 = animate2.alpha(f);
                if (lightsOut) {
                    j = 750;
                }
                ViewPropertyAnimator interpolator = animate2.setDuration(j).setInterpolator(new AccelerateInterpolator(2.0f));
                if (lightsOut) {
                    animatorListener = null;
                } else {
                    animatorListener = new AnonymousClass_2(lowLights);
                }
                interpolator.setListener(animatorListener).start();
            } else {
                float f2;
                int i;
                if (lightsOut) {
                    f2 = 0.0f;
                } else {
                    f2 = 1.0f;
                }
                navButtons.setAlpha(f2);
                if (!lightsOut) {
                    f = 0.0f;
                }
                lowLights.setAlpha(f);
                if (lightsOut) {
                    i = 0;
                } else {
                    i = 8;
                }
                lowLights.setVisibility(i);
            }
        }
    }

    public void setMenuVisibility(boolean show) {
        setMenuVisibility(show, DEBUG_DEADZONE);
    }

    public void setMenuVisibility(boolean show, boolean force) {
        if (force || this.mShowMenu != show) {
            this.mShowMenu = show;
            getMenuButton().setVisibility(this.mShowMenu ? 0 : CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL);
        }
    }

    public void setNavigationIconHints(int hints) {
        setNavigationIconHints(hints, DEBUG_DEADZONE);
    }

    public void setNavigationIconHints(int hints, boolean force) {
        float f = 0.5f;
        if (force || hints != this.mNavigationIconHints) {
            float f2;
            this.mNavigationIconHints = hints;
            getBackButton().setAlpha((hints & 1) != 0 ? 0.5f : 1.0f);
            View homeButton = getHomeButton();
            if ((hints & 2) != 0) {
                f2 = 0.5f;
            } else {
                f2 = 1.0f;
            }
            homeButton.setAlpha(f2);
            View recentsButton = getRecentsButton();
            if ((hints & 4) == 0) {
                f = 1.0f;
            }
            recentsButton.setAlpha(f);
            ImageView imageView = (ImageView) getBackButton();
            Drawable drawable = (hints & 8) != 0 ? this.mVertical ? this.mBackAltLandIcon : this.mBackAltIcon : this.mVertical ? this.mBackLandIcon : this.mBackIcon;
            imageView.setImageDrawable(drawable);
        }
    }

    public void setSlippery(boolean newSlippery) {
        LayoutParams lp = (LayoutParams) getLayoutParams();
        if (lp != null) {
            boolean oldSlippery = (lp.flags & 67108864) != 0 ? WORKAROUND_INVALID_LAYOUT : DEBUG_DEADZONE;
            if (!oldSlippery && newSlippery) {
                lp.flags |= 67108864;
            } else if (oldSlippery && !newSlippery) {
                lp.flags &= -67108865;
            } else {
                return;
            }
            WindowManagerImpl.getDefault().updateViewLayout(this, lp);
        }
    }
}