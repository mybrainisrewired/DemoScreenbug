package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.TextSwitcher;
import android.widget.TextView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.statusbar.StatusBarNotification;
import com.android.internal.util.CharSequences;
import com.android.systemui.R;
import com.android.systemui.statusbar.StatusBarIconView;
import java.util.ArrayList;

public abstract class Ticker {
    private static final int TICKER_SEGMENT_DELAY = 3000;
    private Runnable mAdvanceTicker;
    private Context mContext;
    private Handler mHandler;
    private float mIconScale;
    private ImageSwitcher mIconSwitcher;
    private TextPaint mPaint;
    private ArrayList<Segment> mSegments;
    private TextSwitcher mTextSwitcher;
    private View mTickerView;

    private final class Segment {
        int current;
        boolean first;
        Drawable icon;
        int next;
        StatusBarNotification notification;
        CharSequence text;

        Segment(StatusBarNotification n, Drawable icon, CharSequence text) {
            this.notification = n;
            this.icon = icon;
            this.text = text;
            int index = 0;
            int len = text.length();
            while (index < len && !TextUtils.isGraphic(text.charAt(index))) {
                index++;
            }
            this.current = index;
            this.next = index;
            this.first = true;
        }

        CharSequence advance() {
            this.first = false;
            int index = this.next;
            int len = this.text.length();
            while (index < len && !TextUtils.isGraphic(this.text.charAt(index))) {
                index++;
            }
            if (index >= len) {
                return null;
            }
            CharSequence substr = this.text.subSequence(index, this.text.length());
            StaticLayout l = getLayout(substr);
            int lineCount = l.getLineCount();
            int i = 0;
            while (i < lineCount) {
                int start = l.getLineStart(i);
                int end = l.getLineEnd(i);
                if (i == lineCount - 1) {
                    this.next = len;
                } else {
                    this.next = l.getLineStart(i + 1) + index;
                }
                CharSequence result = rtrim(substr, start, end);
                if (result != null) {
                    this.current = index + start;
                    return result;
                } else {
                    i++;
                }
            }
            this.current = len;
            return null;
        }

        StaticLayout getLayout(CharSequence substr) {
            return new StaticLayout(substr, Ticker.this.mPaint, Ticker.this.mTextSwitcher.getWidth() - Ticker.this.mTextSwitcher.getPaddingLeft() - Ticker.this.mTextSwitcher.getPaddingRight(), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        }

        CharSequence getText() {
            if (this.current > this.text.length()) {
                return null;
            }
            CharSequence substr = this.text.subSequence(this.current, this.text.length());
            StaticLayout l = getLayout(substr);
            int lineCount = l.getLineCount();
            if (lineCount > 0) {
                int start = l.getLineStart(0);
                int end = l.getLineEnd(0);
                this.next = this.current + end;
                return rtrim(substr, start, end);
            } else {
                throw new RuntimeException("lineCount=" + lineCount + " current=" + this.current + " text=" + this.text);
            }
        }

        CharSequence rtrim(CharSequence substr, int start, int end) {
            while (end > start && !TextUtils.isGraphic(substr.charAt(end - 1))) {
                end--;
            }
            return end > start ? substr.subSequence(start, end) : null;
        }
    }

    public Ticker(Context context, View sb) {
        this.mHandler = new Handler();
        this.mSegments = new ArrayList();
        this.mAdvanceTicker = new Runnable() {
            public void run() {
                while (Ticker.this.mSegments.size() > 0) {
                    Segment seg = (Segment) Ticker.this.mSegments.get(0);
                    if (seg.first) {
                        Ticker.this.mIconSwitcher.setImageDrawable(seg.icon);
                    }
                    CharSequence text = seg.advance();
                    if (text != null) {
                        Ticker.this.mTextSwitcher.setText(text);
                        Ticker.this.scheduleAdvance();
                        break;
                    } else {
                        Ticker.this.mSegments.remove(0);
                    }
                }
                if (Ticker.this.mSegments.size() == 0) {
                    Ticker.this.tickerDone();
                }
            }
        };
        this.mContext = context;
        Resources res = context.getResources();
        this.mIconScale = ((float) res.getDimensionPixelSize(R.dimen.status_bar_icon_drawing_size)) / ((float) res.getDimensionPixelSize(R.dimen.status_bar_icon_size));
        this.mTickerView = sb.findViewById(R.id.ticker);
        this.mIconSwitcher = (ImageSwitcher) sb.findViewById(R.id.tickerIcon);
        this.mIconSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, 17432625));
        this.mIconSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, 17432626));
        this.mIconSwitcher.setScaleX(this.mIconScale);
        this.mIconSwitcher.setScaleY(this.mIconScale);
        this.mTextSwitcher = (TextSwitcher) sb.findViewById(R.id.tickerText);
        this.mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(context, 17432625));
        this.mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(context, 17432626));
        this.mPaint = ((TextView) this.mTextSwitcher.getChildAt(0)).getPaint();
    }

    private void scheduleAdvance() {
        this.mHandler.postDelayed(this.mAdvanceTicker, 3000);
    }

    public void addEntry(StatusBarNotification n) {
        Segment seg;
        int initialCount = this.mSegments.size();
        if (initialCount > 0) {
            seg = (Segment) this.mSegments.get(0);
            if (n.pkg.equals(seg.notification.pkg) && n.notification.icon == seg.notification.notification.icon && n.notification.iconLevel == seg.notification.notification.iconLevel && CharSequences.equals(seg.notification.notification.tickerText, n.notification.tickerText)) {
                return;
            }
        }
        Segment newSegment = new Segment(n, StatusBarIconView.getIcon(this.mContext, new StatusBarIcon(n.pkg, n.notification.icon, n.notification.iconLevel, 0, n.notification.tickerText)), n.notification.tickerText);
        int i = 0;
        while (i < this.mSegments.size()) {
            seg = this.mSegments.get(i);
            if (n.id == seg.notification.id && n.pkg.equals(seg.notification.pkg)) {
                int i2 = i - 1;
                this.mSegments.remove(i);
                i = i2;
            }
            i++;
        }
        this.mSegments.add(newSegment);
        if (initialCount == 0 && this.mSegments.size() > 0) {
            seg = this.mSegments.get(0);
            seg.first = false;
            this.mIconSwitcher.setAnimateFirstView(false);
            this.mIconSwitcher.reset();
            this.mIconSwitcher.setImageDrawable(seg.icon);
            this.mTextSwitcher.setAnimateFirstView(false);
            this.mTextSwitcher.reset();
            this.mTextSwitcher.setText(seg.getText());
            tickerStarting();
            scheduleAdvance();
        }
    }

    public void halt() {
        this.mHandler.removeCallbacks(this.mAdvanceTicker);
        this.mSegments.clear();
        tickerHalting();
    }

    public void reflowText() {
        if (this.mSegments.size() > 0) {
            this.mTextSwitcher.setCurrentText(((Segment) this.mSegments.get(0)).getText());
        }
    }

    public void removeEntry(StatusBarNotification n) {
        int i = this.mSegments.size() - 1;
        while (i >= 0) {
            Segment seg = (Segment) this.mSegments.get(i);
            if (n.id == seg.notification.id && n.pkg.equals(seg.notification.pkg)) {
                this.mSegments.remove(i);
            }
            i--;
        }
    }

    public abstract void tickerDone();

    public abstract void tickerHalting();

    public abstract void tickerStarting();
}