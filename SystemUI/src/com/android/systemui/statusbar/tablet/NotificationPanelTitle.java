package com.android.systemui.statusbar.tablet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RelativeLayout;
import com.android.systemui.R;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;
import java.util.Iterator;

public class NotificationPanelTitle extends RelativeLayout implements OnClickListener {
    private ArrayList<View> buttons;
    private NotificationPanel mPanel;
    private View mSettingsButton;

    public NotificationPanelTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.buttons = new ArrayList();
        setOnClickListener(this);
    }

    public void onClick(View v) {
        if (this.mSettingsButton.isEnabled() && v == this) {
            this.mPanel.swapPanels();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        ArrayList arrayList = this.buttons;
        View findViewById = findViewById(R.id.settings_button);
        this.mSettingsButton = findViewById;
        arrayList.add(findViewById);
        this.buttons.add(findViewById(R.id.notification_button));
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (!super.onRequestSendAccessibilityEvent(child, event)) {
            return false;
        }
        AccessibilityEvent record = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(record);
        dispatchPopulateAccessibilityEvent(record);
        event.appendRecord(record);
        return true;
    }

    public boolean onTouchEvent(MotionEvent e) {
        boolean z = false;
        if (!this.mSettingsButton.isEnabled()) {
            return false;
        }
        switch (e.getAction()) {
            case CommandQueue.FLAG_EXCLUDE_NONE:
                setPressed(true);
                break;
            case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                if (isPressed()) {
                    playSoundEffect(0);
                    this.mPanel.swapPanels();
                    setPressed(false);
                }
                break;
            case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (x > 0 && x < getWidth() && y > 0 && y < getHeight()) {
                    z = true;
                }
                setPressed(z);
                break;
            case RecentsCallback.SWIPE_DOWN:
                setPressed(false);
                break;
        }
        return true;
    }

    public void setPanel(NotificationPanel p) {
        this.mPanel = p;
    }

    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        Iterator i$ = this.buttons.iterator();
        while (i$.hasNext()) {
            View button = (View) i$.next();
            if (button != null) {
                button.setPressed(pressed);
            }
        }
    }
}