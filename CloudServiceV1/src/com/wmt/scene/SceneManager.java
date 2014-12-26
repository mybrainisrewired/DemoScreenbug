package com.wmt.scene;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.wmt.opengl.GLCanvas;
import com.wmt.opengl.GLContext;
import com.wmt.opengl.GLView;
import com.wmt.opengl.ViewOverturnAnimation;
import com.wmt.opengl.ViewOverturnAnimation.AnimationListener;
import com.wmt.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class SceneManager {
    private static final String KEY_CLASS = "class";
    private static final String KEY_DATA = "data";
    private static final String KEY_LAUNCH_GALLERY_ON_TOP = "launch-gallery-on-top";
    private static final String KEY_MAIN = "activity-scene";
    private static final String KEY_STATE = "bundle";
    private static final String TAG = "SceneManager";
    private AnimationListener mAnimationEndListener;
    private GLContext mGLContext;
    private boolean mIsResumed;
    private boolean mLaunchGalleryOnTop;
    private Activity mParentActivity;
    private ResultEntry mResult;
    private Stack<SceneEntry> mStack;
    private ViewOverturnAnimation mSwitchAnimation;
    private boolean mSwitchAnimationMark;
    private int mSwitchEndID;
    private int mSwitchStartID;

    private static class SceneEntry {
        public Bundle data;
        public Scene scene;

        public SceneEntry(Bundle data, Scene scene) {
            this.data = data;
            this.scene = scene;
        }
    }

    public SceneManager(Activity activity, GLContext glContext) {
        this.mIsResumed = false;
        this.mSwitchAnimationMark = false;
        this.mSwitchStartID = 0;
        this.mSwitchEndID = 0;
        this.mStack = new Stack();
        this.mLaunchGalleryOnTop = false;
        this.mAnimationEndListener = new AnimationListener() {
            public void onAnimationEnd() {
                if (!SceneManager.this.mStack.isEmpty()) {
                    SceneManager.this.getTopScene().resume(SceneManager.this.mGLContext);
                }
                SceneManager.this.mSwitchAnimationMark = false;
            }

            public void onAnimationStart() {
                SceneManager.this.mSwitchAnimationMark = true;
            }
        };
        this.mParentActivity = activity;
        this.mGLContext = glContext;
    }

    private void endSwitchAnimation() {
        if (this.mSwitchAnimationMark && this.mSwitchAnimation != null) {
            this.mSwitchAnimation.endAnimation(this.mGLContext.glCanvas());
        }
    }

    private void startOrderChangeAnimation(int startTextureID, int endTextureID) {
        GLCanvas glCanvas = this.mGLContext.glCanvas();
        GLView mRootView = this.mGLContext.glRootView();
        float coordX = ((float) mRootView.getWidth()) / ((float) glCanvas.getSurfaceWidth());
        float coordY = ((float) mRootView.getHeight()) / ((float) glCanvas.getSurfaceHeight());
        this.mSwitchAnimation.layoutWH(mRootView.getPositionX(), mRootView.getPositionY(), mRootView.getWidth(), mRootView.getHeight());
        this.mSwitchAnimation.setTextureIDs(startTextureID, endTextureID, coordX, coordY);
        this.mSwitchAnimation.setAnimationEndListener(this.mAnimationEndListener);
        this.mSwitchAnimation.startAnimation(glCanvas);
    }

    public void destroy() {
        Log.v(TAG, "destroy");
        while (!this.mStack.isEmpty()) {
            ((SceneEntry) this.mStack.pop()).scene.onDestroy();
        }
        this.mStack.clear();
    }

    public void finishScene(Scene scene) {
        Log.v(TAG, "finishScene " + scene.getClass());
        if (scene == ((SceneEntry) this.mStack.peek()).scene) {
            this.mStack.pop();
            if (this.mIsResumed) {
                scene.onPause(true);
            }
            scene.destroy();
            if (this.mStack.isEmpty()) {
                Log.v(TAG, "no more state, finish activity");
                if (this.mResult != null) {
                    this.mParentActivity.setResult(this.mResult.resultCode, this.mResult.resultData);
                }
                this.mParentActivity.finish();
                if (!this.mParentActivity.isFinishing()) {
                    Log.v(TAG, "finish() failed, start default page");
                }
            } else {
                Scene top = ((SceneEntry) this.mStack.peek()).scene;
                if (this.mIsResumed) {
                    top.resume(this.mGLContext);
                }
            }
        } else if (scene.isDestroyed()) {
            Log.d(TAG, "The state is already destroyed");
        } else {
            throw new IllegalArgumentException("The scene to be finished is not at the top of the stack: " + scene + ", " + ((SceneEntry) this.mStack.peek()).scene);
        }
    }

    public int getStateCount() {
        int size;
        synchronized (this.mStack) {
            size = this.mStack.size();
        }
        return size;
    }

    public Scene getTopScene() {
        Utils.assertTrue(!this.mStack.isEmpty());
        return ((SceneEntry) this.mStack.peek()).scene;
    }

    public Scene getTopSceneNotAssert() {
        return this.mStack.isEmpty() ? null : ((SceneEntry) this.mStack.peek()).scene;
    }

    public boolean hasSceneClass(Class<? extends Scene> klass) {
        Iterator i$ = this.mStack.iterator();
        while (i$.hasNext()) {
            if (klass.isInstance(((SceneEntry) i$.next()).scene)) {
                return true;
            }
        }
        return false;
    }

    public void notifyActivityResult(int requestCode, int resultCode, Intent data) {
        getTopScene().onSceneResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        if (!this.mStack.isEmpty()) {
            getTopScene().onBackPressed(this);
        }
    }

    public void onConfigurationChange(Configuration config) {
        Iterator i$ = this.mStack.iterator();
        while (i$.hasNext()) {
            ((SceneEntry) i$.next()).scene.onConfigurationChanged(config);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mStack.isEmpty()) {
            return false;
        }
        this.mParentActivity.setProgressBarIndeterminateVisibility(false);
        return getTopScene().onCreateOptionsMenu(menu);
    }

    public void onFling(int direction) {
        getTopScene().onFling(direction);
    }

    public void onMountDevice(int dev, int state) {
        ArrayList<Scene> listScene = new ArrayList();
        Log.v(TAG, "onDeviceUnmount");
        Iterator i$ = this.mStack.iterator();
        while (i$.hasNext()) {
            listScene.add(((SceneEntry) i$.next()).scene);
        }
        int i = listScene.size() - 1;
        while (i >= 0) {
            ((Scene) listScene.get(i)).onMountDevice(dev, state);
            i--;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mStack.isEmpty()) {
            return false;
        }
        if (item.getItemId() != 16908332) {
            return getTopScene().onOptionsItemSelected(item);
        }
        if (this.mStack.size() < 1) {
            return this.mLaunchGalleryOnTop ? true : true;
        } else {
            if (getTopScene().onOptionsItemSelected(item)) {
                return true;
            }
            getTopScene().onBackPressed(this);
            return true;
        }
    }

    public void pause() {
        if (this.mIsResumed) {
            this.mIsResumed = false;
            if (!this.mStack.isEmpty()) {
                getTopScene().onPause(false);
            }
        }
    }

    public void restoreFromState(Bundle inState) {
        Log.v(TAG, "restoreFromState");
        this.mLaunchGalleryOnTop = inState.getBoolean(KEY_LAUNCH_GALLERY_ON_TOP, false);
        Parcelable[] list = inState.getParcelableArray(KEY_MAIN);
        if (list != null) {
            Parcelable[] arr$ = list;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Bundle bundle = (Bundle) arr$[i$];
                Class<? extends Scene> klass = (Class) bundle.getSerializable(KEY_CLASS);
                Bundle data = bundle.getBundle(KEY_DATA);
                Bundle state = bundle.getBundle(KEY_STATE);
                try {
                    Log.v(TAG, "restoreFromState " + klass);
                    Scene scene = (Scene) klass.newInstance();
                    scene.initialize(this.mParentActivity, data);
                    if (!scene.supportRestore()) {
                        break;
                    }
                    scene.onCreate(this.mGLContext, data, state);
                    this.mStack.push(new SceneEntry(data, scene));
                    i$++;
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
            getTopScene().onResume();
        }
    }

    public void resume() {
        if (!this.mIsResumed) {
            this.mIsResumed = true;
            if (!this.mStack.isEmpty()) {
                getTopScene().resume(this.mGLContext);
            }
        }
    }

    public void saveToState(Bundle outState) {
        Log.v(TAG, "saveState");
        outState.putBoolean(KEY_LAUNCH_GALLERY_ON_TOP, this.mLaunchGalleryOnTop);
        Parcelable[] list = new Parcelable[this.mStack.size()];
        int i = 0;
        Iterator i$ = this.mStack.iterator();
        while (i$.hasNext()) {
            SceneEntry entry = (SceneEntry) i$.next();
            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_CLASS, entry.scene.getClass());
            bundle.putBundle(KEY_DATA, entry.data);
            Bundle state = new Bundle();
            entry.scene.onSaveState(state);
            bundle.putBundle(KEY_STATE, state);
            Log.v(TAG, "saveState " + entry.scene.getClass());
            int i2 = i + 1;
            list[i] = bundle;
            i = i2;
        }
        outState.putParcelableArray(KEY_MAIN, list);
    }

    public void setLaunchGalleryOnTop(boolean enabled) {
        this.mLaunchGalleryOnTop = enabled;
    }

    public void setSwitchAnimation(ViewOverturnAnimation animation) {
        this.mSwitchAnimation = animation;
    }

    public void startScene(Class<? extends Scene> klass, Bundle data) {
        Log.v(TAG, "startScene " + klass);
        try {
            Scene scene = klass.newInstance();
            endSwitchAnimation();
            if (this.mStack.isEmpty()) {
                this.mSwitchStartID = 0;
            } else {
                if (this.mSwitchAnimation != null) {
                    this.mSwitchStartID = this.mGLContext.glRootView().getViewAsTexture();
                }
                Scene top = getTopScene();
                if (this.mIsResumed) {
                    top.onPause(false);
                }
            }
            scene.initialize(this.mParentActivity, data);
            this.mStack.push(new SceneEntry(data, scene));
            scene.onCreate(this.mGLContext, data, null);
            if (this.mIsResumed) {
                scene.resume(this.mGLContext);
            }
            if (this.mSwitchAnimation != null && this.mSwitchStartID != 0) {
                int w = this.mGLContext.glCanvas().getSurfaceWidth();
                int h = this.mGLContext.glCanvas().getSurfaceHeight();
                this.mGLContext.glRootView().requestLayout();
                this.mGLContext.glRootView().layout(0, 0, w, h);
                this.mSwitchEndID = this.mGLContext.glRootView().getViewAsTexture();
                scene.onPause(false);
                startOrderChangeAnimation(this.mSwitchStartID, this.mSwitchEndID);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void startSceneForResult(Class<? extends Scene> klass, int requestCode, Bundle data) {
        Log.v(TAG, "startSceneForResult " + klass + ", " + requestCode);
        try {
            Scene scene = klass.newInstance();
            endSwitchAnimation();
            scene.initialize(this.mParentActivity, data);
            scene.mResult = new ResultEntry();
            scene.mResult.requestCode = requestCode;
            if (this.mStack.isEmpty()) {
                this.mResult = scene.mResult;
                this.mSwitchStartID = 0;
            } else {
                if (this.mSwitchAnimation != null) {
                    this.mSwitchStartID = this.mGLContext.glRootView().getViewAsTexture();
                }
                Scene as = getTopScene();
                as.mReceivedResults = scene.mResult;
                if (this.mIsResumed) {
                    as.onPause(false);
                }
            }
            this.mStack.push(new SceneEntry(data, scene));
            scene.onCreate(this.mGLContext, data, null);
            if (this.mIsResumed) {
                scene.resume(this.mGLContext);
            }
            if (this.mSwitchAnimation != null && this.mSwitchStartID != 0) {
                int w = this.mGLContext.glCanvas().getSurfaceWidth();
                int h = this.mGLContext.glCanvas().getSurfaceHeight();
                this.mGLContext.glRootView().requestLayout();
                this.mGLContext.glRootView().layout(0, 0, w, h);
                this.mSwitchEndID = this.mGLContext.glRootView().getViewAsTexture();
                scene.onPause(false);
                startOrderChangeAnimation(this.mSwitchStartID, this.mSwitchEndID);
            }
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void switchScene(Scene oldScene, Class<? extends Scene> klass, Bundle data) {
        Log.v(TAG, "switchState " + oldScene + ", " + klass);
        if (oldScene != ((SceneEntry) this.mStack.peek()).scene) {
            throw new IllegalArgumentException("The scene to be finished is not at the top of the stack: " + oldScene + ", " + ((SceneEntry) this.mStack.peek()).scene);
        }
        synchronized (this.mStack) {
            try {
                endSwitchAnimation();
                if (this.mSwitchAnimation != null) {
                    this.mSwitchStartID = this.mGLContext.glRootView().getViewAsTexture();
                }
                this.mStack.pop();
                if (this.mIsResumed) {
                    oldScene.onPause(true);
                }
                oldScene.onDestroy();
                Scene scene = klass.newInstance();
                scene.initialize(this.mParentActivity, data);
                this.mStack.push(new SceneEntry(data, scene));
                scene.onCreate(this.mGLContext, data, null);
                if (this.mIsResumed) {
                    scene.resume(this.mGLContext);
                }
                if (!(this.mSwitchAnimation == null || this.mSwitchStartID == 0)) {
                    int w = this.mGLContext.glCanvas().getSurfaceWidth();
                    int h = this.mGLContext.glCanvas().getSurfaceHeight();
                    this.mGLContext.glRootView().requestLayout();
                    this.mGLContext.glRootView().layout(0, 0, w, h);
                    this.mSwitchEndID = this.mGLContext.glRootView().getViewAsTexture();
                    scene.onPause(false);
                    startOrderChangeAnimation(this.mSwitchStartID, this.mSwitchEndID);
                }
            } catch (Exception e) {
                throw new AssertionError(e);
            } catch (Throwable th) {
            }
        }
    }
}