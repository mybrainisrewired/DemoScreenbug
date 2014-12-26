package com.android.systemui.statusbar.tablet;

import android.content.Context;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import com.android.systemui.R;
import com.android.systemui.statusbar.CommandQueue;
import java.util.List;

public class InputMethodButton extends ImageView {
    private static final boolean DEBUG = false;
    private static final int ID_IME_BUTTON_VISIBILITY_ALWAYS_HIDE = 2;
    private static final int ID_IME_BUTTON_VISIBILITY_ALWAYS_SHOW = 1;
    private static final int ID_IME_BUTTON_VISIBILITY_AUTO = 0;
    private static final String TAG = "StatusBar/InputMethodButton";
    private static final String TAG_TRY_SUPPRESSING_IME_SWITCHER = "TrySuppressingImeSwitcher";
    private boolean mHardKeyboardAvailable;
    private ImageView mIcon;
    private final int mId;
    private final InputMethodManager mImm;
    private boolean mScreenLocked;
    private boolean mShowButton;
    private IBinder mToken;

    public InputMethodButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mShowButton = false;
        this.mScreenLocked = false;
        this.mId = getId();
        this.mImm = (InputMethodManager) context.getSystemService("input_method");
    }

    private int loadInputMethodSelectorVisibility() {
        return Secure.getInt(getContext().getContentResolver(), "input_method_selector_visibility", ID_IME_BUTTON_VISIBILITY_AUTO);
    }

    private boolean needsToShowIMEButton() {
        if (!this.mShowButton || this.mScreenLocked) {
            return false;
        }
        if (this.mHardKeyboardAvailable) {
            return true;
        }
        switch (loadInputMethodSelectorVisibility()) {
            case ID_IME_BUTTON_VISIBILITY_AUTO:
                return needsToShowIMEButtonWhenVisibilityAuto();
            case ID_IME_BUTTON_VISIBILITY_ALWAYS_SHOW:
                return true;
            case ID_IME_BUTTON_VISIBILITY_ALWAYS_HIDE:
                return false;
            default:
                return false;
        }
    }

    private boolean needsToShowIMEButtonWhenVisibilityAuto() {
        List<InputMethodInfo> imis = this.mImm.getEnabledInputMethodList();
        int N = imis.size();
        if (N > 2) {
            return true;
        }
        if (N < 1) {
            return DEBUG;
        }
        int nonAuxCount = ID_IME_BUTTON_VISIBILITY_AUTO;
        int auxCount = ID_IME_BUTTON_VISIBILITY_AUTO;
        InputMethodSubtype nonAuxSubtype = null;
        InputMethodSubtype auxSubtype = null;
        int i = ID_IME_BUTTON_VISIBILITY_AUTO;
        while (i < N) {
            List<InputMethodSubtype> subtypes = this.mImm.getEnabledInputMethodSubtypeList((InputMethodInfo) imis.get(i), true);
            int subtypeCount = subtypes.size();
            if (subtypeCount == 0) {
                nonAuxCount++;
            } else {
                int j = ID_IME_BUTTON_VISIBILITY_AUTO;
                while (j < subtypeCount) {
                    InputMethodSubtype subtype = (InputMethodSubtype) subtypes.get(j);
                    if (subtype.isAuxiliary()) {
                        auxCount++;
                        auxSubtype = subtype;
                    } else {
                        nonAuxCount++;
                        nonAuxSubtype = subtype;
                    }
                    j++;
                }
            }
            i++;
        }
        if (nonAuxCount > 1 || auxCount > 1) {
            return true;
        }
        if (nonAuxCount != 1 || auxCount != 1) {
            return DEBUG;
        }
        if (!(nonAuxSubtype == null || auxSubtype == null)) {
            if ((nonAuxSubtype.getLocale().equals(auxSubtype.getLocale()) || auxSubtype.overridesImplicitlyEnabledSubtype() || nonAuxSubtype.overridesImplicitlyEnabledSubtype()) && nonAuxSubtype.containsExtraValueKey(TAG_TRY_SUPPRESSING_IME_SWITCHER)) {
                return DEBUG;
            }
        }
        return true;
    }

    private void refreshStatusIcon() {
        if (this.mIcon != null) {
            if (needsToShowIMEButton()) {
                setVisibility(ID_IME_BUTTON_VISIBILITY_AUTO);
                this.mIcon.setImageResource(R.drawable.ic_sysbar_ime);
            } else {
                setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            }
        }
    }

    protected void onAttachedToWindow() {
        this.mIcon = (ImageView) findViewById(this.mId);
        refreshStatusIcon();
    }

    public void setHardKeyboardStatus(boolean available) {
        if (this.mHardKeyboardAvailable != available) {
            this.mHardKeyboardAvailable = available;
            refreshStatusIcon();
        }
    }

    public void setIconImage(int resId) {
        if (this.mIcon != null) {
            this.mIcon.setImageResource(resId);
        }
    }

    public void setImeWindowStatus(IBinder token, boolean showButton) {
        this.mToken = token;
        this.mShowButton = showButton;
        refreshStatusIcon();
    }

    public void setScreenLocked(boolean locked) {
        this.mScreenLocked = locked;
        refreshStatusIcon();
    }
}