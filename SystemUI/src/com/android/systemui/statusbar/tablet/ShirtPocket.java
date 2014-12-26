package com.android.systemui.statusbar.tablet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.widget.ImageView;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;

public class ShirtPocket extends ImageView {
    private static final boolean DEBUG = false;
    private static final String TAG = "StatusBar/ShirtPocket";
    ObjectAnimator mAnimHide;
    ObjectAnimator mAnimShow;
    private ClipData mClipping;
    private ImageView mPreviewIcon;

    class AnonymousClass_1 extends DragShadowBuilder {
        final /* synthetic */ Bitmap val$icon;

        AnonymousClass_1(View x0, Bitmap bitmap) {
            this.val$icon = bitmap;
            super(x0);
        }

        public void onDrawShadow(Canvas canvas) {
            canvas.drawBitmap(this.val$icon, 0.0f, 0.0f, new Paint());
        }

        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            shadowSize.set(this.val$icon.getWidth(), this.val$icon.getHeight());
            shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
        }
    }

    public static class DropZone extends View {
        ShirtPocket mPocket;

        public DropZone(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        private void hide(boolean animate) {
            AnimatorListenerAdapter onEnd = new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator _a) {
                    com.android.systemui.statusbar.tablet.ShirtPocket.DropZone.this.setTranslationY((float) (com.android.systemui.statusbar.tablet.ShirtPocket.DropZone.this.getHeight() + 2));
                    com.android.systemui.statusbar.tablet.ShirtPocket.DropZone.this.setAlpha(0.0f);
                }
            };
            if (animate) {
                Animator a = ObjectAnimator.ofFloat(this, "alpha", new float[]{getAlpha(), 0.0f});
                a.addListener(onEnd);
                a.start();
            } else {
                onEnd.onAnimationEnd(null);
            }
        }

        private void show(boolean animate) {
            setTranslationY(0.0f);
            if (animate) {
                setAlpha(0.0f);
                ObjectAnimator.ofFloat(this, "alpha", new float[]{0.0f, 1.0f}).start();
            } else {
                setAlpha(1.0f);
            }
        }

        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (this.mPocket.holding()) {
                show(DEBUG);
            } else {
                hide(DEBUG);
            }
        }

        public boolean onDragEvent(DragEvent event) {
            switch (event.getAction()) {
                case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                    show(true);
                    break;
                case RecentsCallback.SWIPE_DOWN:
                    this.mPocket.stash(event.getClipData());
                    break;
                case CommandQueue.FLAG_EXCLUDE_NOTIFICATION_PANEL:
                    hide(true);
                    break;
            }
            return true;
        }

        public void setPocket(ShirtPocket p) {
            this.mPocket = p;
        }
    }

    public ShirtPocket(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mClipping = null;
    }

    private void stash(ClipData clipping) {
        this.mClipping = clipping;
        if (this.mClipping != null) {
            setVisibility(0);
            Bitmap icon = this.mClipping.getIcon();
            if (icon != null) {
                setImageBitmap(icon);
            } else if (this.mClipping.getItemCount() <= 0) {
            }
        } else {
            setVisibility(CommandQueue.FLAG_EXCLUDE_INPUT_METHODS_PANEL);
        }
    }

    public boolean holding() {
        return this.mClipping != null ? true : DEBUG;
    }

    protected void onAttachedToWindow() {
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() != 0) {
            return DEBUG;
        }
        ClipData clip = this.mClipping;
        if (clip == null) {
            return DEBUG;
        }
        DragShadowBuilder shadow;
        Bitmap icon = clip.getIcon();
        if (icon != null) {
            shadow = new AnonymousClass_1(this, icon);
        } else {
            shadow = new DragShadowBuilder(this);
        }
        startDrag(clip, shadow, null, 0);
        stash(null);
        return true;
    }
}