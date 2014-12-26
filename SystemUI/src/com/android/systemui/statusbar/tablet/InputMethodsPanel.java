package com.android.systemui.statusbar.tablet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.R;
import com.android.systemui.statusbar.CommandQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InputMethodsPanel extends LinearLayout implements StatusBarPanel, OnClickListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "InputMethodsPanel";
    private boolean mAttached;
    private final BroadcastReceiver mBroadcastReceiver;
    private View mConfigureImeShortcut;
    private Context mContext;
    private final TreeMap<InputMethodInfo, List<InputMethodSubtype>> mEnabledInputMethodAndSubtypesCache;
    private String mEnabledInputMethodAndSubtypesCacheStr;
    private boolean mHardKeyboardAvailable;
    private boolean mHardKeyboardEnabled;
    private OnHardKeyboardEnabledChangeListener mHardKeyboardEnabledChangeListener;
    private LinearLayout mHardKeyboardSection;
    private Switch mHardKeyboardSwitch;
    private final InputMethodManager mImm;
    private LinearLayout mInputMethodMenuList;
    private InputMethodButton mInputMethodSwitchButton;
    private final IntentFilter mIntentFilter;
    private String mLastSystemLocaleString;
    private boolean mPackageChanged;
    private PackageManager mPackageManager;
    private final HashMap<View, Pair<InputMethodInfo, InputMethodSubtype>> mRadioViewAndImiMap;
    private IBinder mToken;

    class AnonymousClass_2 implements OnClickListener {
        final /* synthetic */ InputMethodInfo val$imi;
        final /* synthetic */ String val$settingsActivity;

        AnonymousClass_2(InputMethodInfo inputMethodInfo, String str) {
            this.val$imi = inputMethodInfo;
            this.val$settingsActivity = str;
        }

        public void onClick(View arg0) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClassName(this.val$imi.getPackageName(), this.val$settingsActivity);
            intent.setFlags(337641472);
            InputMethodsPanel.this.startActivity(intent);
            InputMethodsPanel.this.closePanel(true);
        }
    }

    private class InputMethodComparator implements Comparator<InputMethodInfo> {
        private InputMethodComparator() {
        }

        public int compare(InputMethodInfo imi1, InputMethodInfo imi2) {
            if (imi2 == null) {
                return 0;
            }
            if (imi1 == null) {
                return 1;
            }
            if (InputMethodsPanel.this.mPackageManager == null) {
                return imi1.getId().compareTo(imi2.getId());
            }
            return (imi1.loadLabel(InputMethodsPanel.this.mPackageManager) + "/" + imi1.getId()).toString().compareTo((imi2.loadLabel(InputMethodsPanel.this.mPackageManager) + "/" + imi2.getId()).toString());
        }
    }

    public static interface OnHardKeyboardEnabledChangeListener {
        void onHardKeyboardEnabledChange(boolean z);
    }

    public InputMethodsPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputMethodsPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                InputMethodsPanel.this.onPackageChanged();
            }
        };
        this.mIntentFilter = new IntentFilter();
        this.mRadioViewAndImiMap = new HashMap();
        this.mEnabledInputMethodAndSubtypesCache = new TreeMap(new InputMethodComparator(null));
        this.mAttached = false;
        this.mPackageChanged = false;
        this.mContext = context;
        this.mImm = (InputMethodManager) context.getSystemService("input_method");
        this.mIntentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        this.mIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        this.mIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        this.mIntentFilter.addDataScheme("package");
    }

    private View createInputMethodItem(InputMethodInfo imi, InputMethodSubtype subtype) {
        CharSequence subtypeName;
        if (subtype == null || subtype.overridesImplicitlyEnabledSubtype()) {
            subtypeName = null;
        } else {
            subtypeName = getSubtypeName(imi, subtype);
        }
        CharSequence imiName = getIMIName(imi);
        Drawable icon = getSubtypeIcon(imi, subtype);
        View view = View.inflate(this.mContext, R.layout.system_bar_input_methods_item, null);
        ImageView subtypeIcon = (ImageView) view.findViewById(R.id.item_icon);
        TextView itemTitle = (TextView) view.findViewById(R.id.item_title);
        TextView itemSubtitle = (TextView) view.findViewById(R.id.item_subtitle);
        ImageView settingsIcon = (ImageView) view.findViewById(R.id.item_settings_icon);
        View subtypeView = view.findViewById(R.id.item_subtype);
        if (subtypeName == null) {
            itemTitle.setText(imiName);
            itemSubtitle.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        } else {
            itemTitle.setText(subtypeName);
            itemSubtitle.setVisibility(0);
            itemSubtitle.setText(imiName);
        }
        subtypeIcon.setImageDrawable(icon);
        subtypeIcon.setContentDescription(itemTitle.getText());
        String settingsActivity = imi.getSettingsActivity();
        if (TextUtils.isEmpty(settingsActivity)) {
            view.findViewById(R.id.item_vertical_separator).setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
            settingsIcon.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        } else {
            settingsIcon.setOnClickListener(new AnonymousClass_2(imi, settingsActivity));
        }
        this.mRadioViewAndImiMap.put(subtypeView, new Pair(imi, subtype));
        subtypeView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Pair<InputMethodInfo, InputMethodSubtype> imiAndSubtype = InputMethodsPanel.this.updateRadioButtonsByView(v);
                InputMethodsPanel.this.closePanel(DEBUG);
                InputMethodsPanel.this.setInputMethodAndSubtype((InputMethodInfo) imiAndSubtype.first, (InputMethodSubtype) imiAndSubtype.second);
            }
        });
        return view;
    }

    private InputMethodInfo getCurrentInputMethodInfo() {
        String curInputMethodId = Secure.getString(getContext().getContentResolver(), "default_input_method");
        Iterator i$ = this.mEnabledInputMethodAndSubtypesCache.keySet().iterator();
        while (i$.hasNext()) {
            InputMethodInfo imi = (InputMethodInfo) i$.next();
            if (imi.getId().equals(curInputMethodId)) {
                return imi;
            }
        }
        i$ = getEnabledInputMethodAndSubtypeList().keySet().iterator();
        while (i$.hasNext()) {
            imi = i$.next();
            if (imi.getId().equals(curInputMethodId)) {
                return imi;
            }
        }
        return null;
    }

    private TreeMap<InputMethodInfo, List<InputMethodSubtype>> getEnabledInputMethodAndSubtypeList() {
        String newEnabledIMIs = Secure.getString(this.mContext.getContentResolver(), "enabled_input_methods");
        String currentSystemLocaleString = this.mContext.getResources().getConfiguration().locale.toString();
        if (!(TextUtils.equals(this.mEnabledInputMethodAndSubtypesCacheStr, newEnabledIMIs) && TextUtils.equals(this.mLastSystemLocaleString, currentSystemLocaleString) && !this.mPackageChanged)) {
            this.mEnabledInputMethodAndSubtypesCache.clear();
            Iterator i$ = this.mImm.getEnabledInputMethodList().iterator();
            while (i$.hasNext()) {
                InputMethodInfo imi = (InputMethodInfo) i$.next();
                this.mEnabledInputMethodAndSubtypesCache.put(imi, this.mImm.getEnabledInputMethodSubtypeList(imi, true));
            }
            this.mEnabledInputMethodAndSubtypesCacheStr = newEnabledIMIs;
            this.mPackageChanged = false;
            this.mLastSystemLocaleString = currentSystemLocaleString;
        }
        return this.mEnabledInputMethodAndSubtypesCache;
    }

    private CharSequence getIMIName(InputMethodInfo imi) {
        return imi == null ? null : imi.loadLabel(this.mPackageManager);
    }

    private Drawable getSubtypeIcon(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (imi != null) {
            if (subtype != null) {
                return this.mPackageManager.getDrawable(imi.getPackageName(), subtype.getIconResId(), imi.getServiceInfo().applicationInfo);
            }
            if (imi.getSubtypeCount() > 0) {
                return this.mPackageManager.getDrawable(imi.getPackageName(), imi.getSubtypeAt(0).getIconResId(), imi.getServiceInfo().applicationInfo);
            }
            try {
                return this.mPackageManager.getApplicationInfo(imi.getPackageName(), 0).loadIcon(this.mPackageManager);
            } catch (NameNotFoundException e) {
                Log.w(TAG, "IME can't be found: " + imi.getPackageName());
            }
        }
        return null;
    }

    private CharSequence getSubtypeName(InputMethodInfo imi, InputMethodSubtype subtype) {
        return (imi == null || subtype == null) ? null : subtype.getDisplayName(this.mContext, imi.getPackageName(), imi.getServiceInfo().applicationInfo);
    }

    private void onPackageChanged() {
        this.mPackageChanged = true;
    }

    private void setInputMethodAndSubtype(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (this.mToken != null) {
            this.mImm.setInputMethodAndSubtype(this.mToken, imi.getId(), subtype);
        } else {
            Log.w(TAG, "IME Token is not set yet.");
        }
    }

    private void showConfigureInputMethods() {
        Intent intent = new Intent("android.settings.INPUT_METHOD_SETTINGS");
        intent.setFlags(337641472);
        startActivity(intent);
    }

    private void startActivity(Intent intent) {
        this.mContext.startActivity(intent);
    }

    private void updateHardKeyboardEnabled() {
        if (this.mHardKeyboardAvailable) {
            boolean checked = this.mHardKeyboardSwitch.isChecked();
            if (this.mHardKeyboardEnabled != checked) {
                this.mHardKeyboardEnabled = checked;
                if (this.mHardKeyboardEnabledChangeListener != null) {
                    this.mHardKeyboardEnabledChangeListener.onHardKeyboardEnabledChange(checked);
                }
            }
        }
    }

    private void updateHardKeyboardSection() {
        if (this.mHardKeyboardAvailable) {
            this.mHardKeyboardSection.setVisibility(0);
            if (this.mHardKeyboardSwitch.isChecked() != this.mHardKeyboardEnabled) {
                this.mHardKeyboardSwitch.setChecked(this.mHardKeyboardEnabled);
                updateHardKeyboardEnabled();
            }
        } else {
            this.mHardKeyboardSection.setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
    }

    private void updateRadioButtons() {
        updateRadioButtonsByImiAndSubtype(getCurrentInputMethodInfo(), this.mImm.getCurrentInputMethodSubtype());
    }

    private void updateRadioButtonsByImiAndSubtype(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (imi != null) {
            Iterator i$ = this.mRadioViewAndImiMap.keySet().iterator();
            while (i$.hasNext()) {
                View radioView = (View) i$.next();
                RadioButton subtypeRadioButton = (RadioButton) radioView.findViewById(R.id.item_radio);
                if (subtypeRadioButton == null) {
                    Log.w(TAG, "RadioButton was not found in the selected subtype view");
                    return;
                } else {
                    Pair<InputMethodInfo, InputMethodSubtype> imiAndSubtype = (Pair) this.mRadioViewAndImiMap.get(radioView);
                    if (((InputMethodInfo) imiAndSubtype.first).getId().equals(imi.getId()) && (imiAndSubtype.second == null || ((InputMethodSubtype) imiAndSubtype.second).equals(subtype))) {
                        subtypeRadioButton.setChecked(true);
                    } else {
                        subtypeRadioButton.setChecked(DEBUG);
                    }
                }
            }
        }
    }

    private Pair<InputMethodInfo, InputMethodSubtype> updateRadioButtonsByView(View selectedView) {
        Pair<InputMethodInfo, InputMethodSubtype> selectedImiAndSubtype = null;
        if (this.mRadioViewAndImiMap.containsKey(selectedView)) {
            Iterator i$ = this.mRadioViewAndImiMap.keySet().iterator();
            while (i$.hasNext()) {
                View radioView = (View) i$.next();
                RadioButton subtypeRadioButton = (RadioButton) radioView.findViewById(R.id.item_radio);
                if (subtypeRadioButton == null) {
                    Log.w(TAG, "RadioButton was not found in the selected subtype view");
                    return null;
                } else if (radioView == selectedView) {
                    selectedImiAndSubtype = (Pair) this.mRadioViewAndImiMap.get(radioView);
                    subtypeRadioButton.setChecked(true);
                } else {
                    subtypeRadioButton.setChecked(DEBUG);
                }
            }
        }
        return selectedImiAndSubtype;
    }

    private void updateUiElements() {
        updateHardKeyboardSection();
        this.mInputMethodMenuList.removeAllViews();
        this.mRadioViewAndImiMap.clear();
        this.mPackageManager = this.mContext.getPackageManager();
        Map<InputMethodInfo, List<InputMethodSubtype>> enabledIMIs = getEnabledInputMethodAndSubtypeList();
        Iterator it = enabledIMIs.keySet().iterator();
        while (it.hasNext()) {
            InputMethodInfo imi = (InputMethodInfo) it.next();
            List<InputMethodSubtype> subtypes = (List) enabledIMIs.get(imi);
            if (subtypes == null || subtypes.size() == 0) {
                this.mInputMethodMenuList.addView(createInputMethodItem(imi, null));
            } else {
                Iterator i$ = subtypes.iterator();
                while (i$.hasNext()) {
                    this.mInputMethodMenuList.addView(createInputMethodItem(imi, (InputMethodSubtype) i$.next()));
                }
            }
        }
        updateRadioButtons();
    }

    public void closePanel(boolean closeKeyboard) {
        setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        if (this.mInputMethodSwitchButton != null) {
            this.mInputMethodSwitchButton.setIconImage(R.drawable.ic_sysbar_ime);
        }
        if (closeKeyboard) {
            this.mImm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        return (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) ? true : super.dispatchHoverEvent(event);
    }

    public boolean isInContentArea(int x, int y) {
        return DEBUG;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            getContext().registerReceiver(this.mBroadcastReceiver, this.mIntentFilter);
            this.mAttached = true;
        }
    }

    public void onClick(View view) {
        if (view == this.mConfigureImeShortcut) {
            showConfigureInputMethods();
            closePanel(true);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            getContext().unregisterReceiver(this.mBroadcastReceiver);
            this.mAttached = false;
        }
    }

    public void onFinishInflate() {
        this.mInputMethodMenuList = (LinearLayout) findViewById(R.id.input_method_menu_list);
        this.mHardKeyboardSection = (LinearLayout) findViewById(R.id.hard_keyboard_section);
        this.mHardKeyboardSwitch = (Switch) findViewById(R.id.hard_keyboard_switch);
        this.mConfigureImeShortcut = findViewById(R.id.ime_settings_shortcut);
        this.mConfigureImeShortcut.setOnClickListener(this);
        updateUiElements();
    }

    public void openPanel() {
        setVisibility(0);
        updateUiElements();
        if (this.mInputMethodSwitchButton != null) {
            this.mInputMethodSwitchButton.setIconImage(R.drawable.ic_sysbar_ime_pressed);
        }
    }

    public void setHardKeyboardEnabledChangeListener(OnHardKeyboardEnabledChangeListener listener) {
        this.mHardKeyboardEnabledChangeListener = listener;
    }

    public void setHardKeyboardStatus(boolean available, boolean enabled) {
        if (this.mHardKeyboardAvailable != available || this.mHardKeyboardEnabled != enabled) {
            this.mHardKeyboardAvailable = available;
            this.mHardKeyboardEnabled = enabled;
            updateHardKeyboardSection();
        }
    }

    public void setImeSwitchButton(InputMethodButton imb) {
        this.mInputMethodSwitchButton = imb;
    }

    public void setImeToken(IBinder token) {
        this.mToken = token;
    }
}