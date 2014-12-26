package com.android.systemui;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.recent.RecentsCallback;
import com.android.systemui.statusbar.CommandQueue;
import java.util.Random;

public class BeanBag extends Activity {
    static final boolean DEBUG = false;
    private Board mBoard;

    public static class Board extends FrameLayout {
        static int[] BEANS;
        static int[] COLORS;
        static float LUCKY;
        static int MAX_RADIUS;
        static float MAX_SCALE;
        static float MIN_SCALE;
        static int NUM_BEANS;
        static Random sRNG;
        private int boardHeight;
        private int boardWidth;
        TimeAnimator mAnim;

        public class Bean extends ImageView {
            public static final float VMAX = 1000.0f;
            public static final float VMIN = 100.0f;
            public float a;
            public boolean grabbed;
            public long grabtime;
            public float grabx;
            private float grabx_offset;
            public float graby;
            private float graby_offset;
            public int h;
            public float r;
            public float va;
            public float vx;
            public float vy;
            public int w;
            public float x;
            public float y;
            public float z;

            public Bean(Context context, AttributeSet as) {
                super(context, as);
            }

            private void pickBean() {
                int beanId = com.android.systemui.BeanBag.Board.pickInt(BEANS);
                if (com.android.systemui.BeanBag.Board.randfrange(0.0f, 1.0f) <= LUCKY) {
                    beanId = R.drawable.jandycane;
                }
                BitmapDrawable bean = (BitmapDrawable) getContext().getResources().getDrawable(beanId);
                Bitmap beanBits = bean.getBitmap();
                this.h = beanBits.getHeight();
                this.w = beanBits.getWidth();
                setImageDrawable(bean);
                Paint pt = new Paint();
                int color = com.android.systemui.BeanBag.Board.pickInt(COLORS);
                float[] M = new ColorMatrix().getArray();
                M[0] = ((float) ((16711680 & color) >> 16)) / 255.0f;
                M[5] = ((float) ((65280 & color) >> 8)) / 255.0f;
                M[10] = ((float) (color & 255)) / 255.0f;
                pt.setColorFilter(new ColorMatrixColorFilter(M));
                if (beanId == 2130837571) {
                    pt = null;
                }
                setLayerType(CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL, pt);
            }

            public boolean onTouchEvent(MotionEvent e) {
                switch (e.getAction()) {
                    case CommandQueue.FLAG_EXCLUDE_NONE:
                        this.grabbed = true;
                        this.grabx_offset = e.getRawX() - this.x;
                        this.graby_offset = e.getRawY() - this.y;
                        this.va = 0.0f;
                        this.grabx = e.getRawX() - this.grabx_offset;
                        this.graby = e.getRawY() - this.graby_offset;
                        this.grabtime = e.getEventTime();
                        break;
                    case CommandQueue.FLAG_EXCLUDE_SEARCH_PANEL:
                    case RecentsCallback.SWIPE_DOWN:
                        this.grabbed = false;
                        float a = ((float) com.android.systemui.BeanBag.Board.randsign()) * com.android.systemui.BeanBag.Board.clamp(com.android.systemui.BeanBag.Board.mag(this.vx, this.vy) * 0.33f, 0.0f, 1080.0f);
                        this.va = com.android.systemui.BeanBag.Board.randfrange(0.5f * a, a);
                        break;
                    case CommandQueue.FLAG_EXCLUDE_RECENTS_PANEL:
                        this.grabx = e.getRawX() - this.grabx_offset;
                        this.graby = e.getRawY() - this.graby_offset;
                        this.grabtime = e.getEventTime();
                        break;
                }
                return true;
            }

            public float overlap(com.android.systemui.BeanBag.Board.Bean other) {
                return com.android.systemui.BeanBag.Board.mag(this.x - other.x, this.y - other.y) - this.r - other.r;
            }

            public void reset() {
                float f = 0.0f;
                pickBean();
                float scale = com.android.systemui.BeanBag.Board.lerp(MIN_SCALE, MAX_SCALE, this.z);
                setScaleX(scale);
                setScaleY(scale);
                this.r = (0.3f * ((float) Math.max(this.h, this.w))) * scale;
                this.a = com.android.systemui.BeanBag.Board.randfrange(0.0f, 360.0f);
                this.va = com.android.systemui.BeanBag.Board.randfrange(-30.0f, 30.0f);
                this.vx = com.android.systemui.BeanBag.Board.randfrange(-40.0f, 40.0f) * this.z;
                this.vy = com.android.systemui.BeanBag.Board.randfrange(-40.0f, 40.0f) * this.z;
                float boardh = (float) com.android.systemui.BeanBag.Board.this.boardHeight;
                float boardw = (float) com.android.systemui.BeanBag.Board.this.boardWidth;
                float f2;
                if (com.android.systemui.BeanBag.Board.flip()) {
                    this.x = this.vx < 0.0f ? this.r * 2.0f + boardw : (-this.r) * 4.0f;
                    float randfrange = com.android.systemui.BeanBag.Board.randfrange(0.0f, boardh - (this.r * 3.0f)) * 0.5f;
                    if (this.vy < 0.0f) {
                        f2 = boardh * 0.5f;
                    } else {
                        f2 = 0.0f;
                    }
                    this.y = f2 + randfrange;
                } else {
                    this.y = this.vy < 0.0f ? this.r * 2.0f + boardh : (-this.r) * 4.0f;
                    f2 = com.android.systemui.BeanBag.Board.randfrange(0.0f, boardw - (this.r * 3.0f)) * 0.5f;
                    if (this.vx < 0.0f) {
                        f = boardw * 0.5f;
                    }
                    this.x = f2 + f;
                }
            }

            public String toString() {
                return String.format("<bean (%.1f, %.1f) (%d x %d)>", new Object[]{Float.valueOf(getX()), Float.valueOf(getY()), Integer.valueOf(getWidth()), Integer.valueOf(getHeight())});
            }

            public void update(float dt) {
                if (this.grabbed) {
                    this.vx = this.vx * 0.75f + ((this.grabx - this.x) / dt) * 0.25f;
                    this.x = this.grabx;
                    this.vy = this.vy * 0.75f + ((this.graby - this.y) / dt) * 0.25f;
                    this.y = this.graby;
                } else {
                    this.x += this.vx * dt;
                    this.y += this.vy * dt;
                    this.a += this.va * dt;
                }
            }
        }

        static {
            sRNG = new Random();
            NUM_BEANS = 40;
            MIN_SCALE = 0.2f;
            MAX_SCALE = 1.0f;
            LUCKY = 0.001f;
            MAX_RADIUS = (int) (576.0f * MAX_SCALE);
            BEANS = new int[]{2130837591, 2130837591, 2130837591, 2130837591, 2130837592, 2130837592, 2130837593, 2130837593, 2130837594};
            COLORS = new int[]{-16724992, -3407872, -16777012, -256, -32768, -16724737, -65408, -8388353, -32640, -8355585, -5193520, -2236963, -13421773};
        }

        public Board(Context context, AttributeSet as) {
            super(context, as);
            setSystemUiVisibility(1);
            setWillNotDraw(true);
        }

        static float clamp(float x, float a, float b) {
            if (x < a) {
                return a;
            }
            return x > b ? b : x;
        }

        static float dot(float x1, float y1, float x2, float y2) {
            return x1 * x2 + y1 + y2;
        }

        static boolean flip() {
            return sRNG.nextBoolean();
        }

        static float lerp(float a, float b, float f) {
            return (b - a) * f + a;
        }

        static float mag(float x, float y) {
            return (float) Math.sqrt((double) (x * x + y * y));
        }

        static <E> E pick(E[] array) {
            return array.length == 0 ? null : array[sRNG.nextInt(array.length)];
        }

        static int pickInt(int[] array) {
            return array.length == 0 ? 0 : array[sRNG.nextInt(array.length)];
        }

        static float randfrange(float a, float b) {
            return lerp(a, b, sRNG.nextFloat());
        }

        static int randsign() {
            return sRNG.nextBoolean() ? 1 : -1;
        }

        private void reset() {
            removeAllViews();
            LayoutParams wrap = new LayoutParams(-2, -2);
            int i = 0;
            while (i < NUM_BEANS) {
                Bean nv = new Bean(getContext(), null);
                addView(nv, wrap);
                nv.z = ((float) i) / ((float) NUM_BEANS);
                nv.z *= nv.z;
                nv.reset();
                nv.x = randfrange(0.0f, (float) this.boardWidth);
                nv.y = randfrange(0.0f, (float) this.boardHeight);
                i++;
            }
            if (this.mAnim != null) {
                this.mAnim.cancel();
            }
            this.mAnim = new TimeAnimator();
            this.mAnim.setTimeListener(new TimeListener() {
                private long lastPrint;

                {
                    this.lastPrint = 0;
                }

                public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                    int i = 0;
                    while (i < com.android.systemui.BeanBag.Board.this.getChildCount()) {
                        View v = com.android.systemui.BeanBag.Board.this.getChildAt(i);
                        if (v instanceof com.android.systemui.BeanBag.Board.Bean) {
                            com.android.systemui.BeanBag.Board.Bean nv = (com.android.systemui.BeanBag.Board.Bean) v;
                            nv.update(((float) deltaTime) / 1000.0f);
                            int j = i + 1;
                            while (j < com.android.systemui.BeanBag.Board.this.getChildCount()) {
                                View v2 = com.android.systemui.BeanBag.Board.this.getChildAt(j);
                                if (v2 instanceof com.android.systemui.BeanBag.Board.Bean) {
                                    nv.overlap((com.android.systemui.BeanBag.Board.Bean) v2);
                                }
                                j++;
                            }
                            nv.setRotation(nv.a);
                            nv.setX(nv.x - nv.getPivotX());
                            nv.setY(nv.y - nv.getPivotY());
                            if (nv.x < ((float) (-MAX_RADIUS)) || nv.x > ((float) (com.android.systemui.BeanBag.Board.this.boardWidth + MAX_RADIUS)) || nv.y < ((float) (-MAX_RADIUS)) || nv.y > ((float) (com.android.systemui.BeanBag.Board.this.boardHeight + MAX_RADIUS))) {
                                nv.reset();
                            }
                        }
                        i++;
                    }
                }
            });
        }

        public boolean isOpaque() {
            return false;
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            stopAnimation();
        }

        public void onDraw(Canvas c) {
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.boardWidth = w;
            this.boardHeight = h;
        }

        public void startAnimation() {
            stopAnimation();
            if (this.mAnim == null) {
                post(new Runnable() {
                    public void run() {
                        com.android.systemui.BeanBag.Board.this.reset();
                        com.android.systemui.BeanBag.Board.this.startAnimation();
                    }
                });
            } else {
                this.mAnim.start();
            }
        }

        public void stopAnimation() {
            if (this.mAnim != null) {
                this.mAnim.cancel();
            }
        }
    }

    public void onPause() {
        super.onPause();
        this.mBoard.stopAnimation();
    }

    public void onResume() {
        super.onResume();
        this.mBoard.startAnimation();
    }

    public void onStart() {
        super.onStart();
        getWindow().addFlags(524289);
        this.mBoard = new Board(this, null);
        setContentView(this.mBoard);
    }
}