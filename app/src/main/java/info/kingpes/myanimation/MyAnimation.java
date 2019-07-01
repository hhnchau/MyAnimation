package info.kingpes.myanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.lang.ref.WeakReference;


public abstract class MyAnimation implements View.OnTouchListener, View.OnClickListener {
    private int duration = 100;
    private float scale = 0.9f;
    private float alpha = 0.4f;
    private long touchTime;

    private AnimatorSet downSet, upSet;
    private AnimatorListenerAdapter animationListener;

    private WeakReference<View> mView;
    private boolean isAnimation = false;
    private boolean pressed = false, released = true;
    private Rect rect;

    public MyAnimation() {

    }

    public MyAnimation(int duration, float alpha, float scale) {
        this.duration = duration;
        this.alpha = alpha;
        this.scale = scale;
    }

    private void createAnimators() {
        ObjectAnimator alphaDownAnimator = ObjectAnimator.ofFloat(mView.get(), "alpha", alpha);
        ObjectAnimator alphaUpAnimator = ObjectAnimator.ofFloat(mView.get(), "alpha", 1.0f);
        ObjectAnimator scaleXDownAnimator = ObjectAnimator.ofFloat(mView.get(), "scaleX", scale);
        ObjectAnimator scaleXUpAnimator = ObjectAnimator.ofFloat(mView.get(), "scaleX", 1.0f);
        ObjectAnimator scaleYDownAnimator = ObjectAnimator.ofFloat(mView.get(), "scaleY", scale);
        ObjectAnimator scaleYUpAnimator = ObjectAnimator.ofFloat(mView.get(), "scaleY", 1.0f);

        downSet = new AnimatorSet();
        downSet.setDuration(duration);
        downSet.setInterpolator(new AccelerateInterpolator());
        downSet.playTogether(alphaDownAnimator, scaleXDownAnimator, scaleYDownAnimator);

        upSet = new AnimatorSet();
        upSet.setDuration(duration);
        upSet.setInterpolator(new FastOutSlowInInterpolator());
        upSet.playTogether(alphaUpAnimator, scaleXUpAnimator, scaleYUpAnimator);

        animationListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onClick(mView.get());
            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mView == null)
            mView = new WeakReference<>(v);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setRect();
                touchTime = System.currentTimeMillis();
                if (!pressed) {
                    effect(true);
                    pressed = true;
                    released = false;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!released) {
                    setRect();
                    if (!insideView(event)) {
                        effect(false);
                        released = true;
                        pressed = false;
                    }
                }
                return false;
            case MotionEvent.ACTION_UP:
                if (!released) {
                    setRect();
                    if (insideView(event)) {
                        upSet.addListener(animationListener);
                    }
                    effect(false);
                    released = true;
                    pressed = false;
                }
                return false;
            case MotionEvent.ACTION_CANCEL:
                effect(false);
                released = true;
                pressed = false;
                return false;
            default:
                return false;
        }
    }

    private boolean insideView(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        return x >= getRect().left && x <= getRect().right && y <= getRect().bottom && y >= getRect().top;
    }

    private void setRect() {
        if (rect == null) {
            rect = new Rect();
            mView.get().getGlobalVisibleRect(rect);
        }
    }

    private Rect getRect() {
        return rect;
    }

    private void effect(boolean press) {

        if (!isAnimation) {
            createAnimators();
            isAnimation = true;
        }

        if (press) {
            if (upSet != null)
                upSet.removeAllListeners();
            downSet.cancel();
            downSet.start();
        } else {
            long diffTime = System.currentTimeMillis() - touchTime;
            if (diffTime < duration) {
                upSet.setStartDelay(duration - diffTime);
            }
            upSet.cancel();
            upSet.start();
        }
    }
}
