package info.kingpes.myanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

public class ImageViewAnimation extends android.support.v7.widget.AppCompatImageView {
    private static final int DURATION = 100;
    private static final float SCALE = 0.9f;
    private static final float ALPHA = 0.5f;
    private boolean isAnimation = false;
    private boolean pressed = false, released = true;
    private long touchTime;
    AnimatorSet downSet;
    AnimatorSet upSet;

    public ImageViewAnimation(Context context) {
        super(context);
    }

    public ImageViewAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void setAnimation() {
        ObjectAnimator alphaDownAnimator = ObjectAnimator.ofFloat(this, "alpha", ALPHA);
        ObjectAnimator alphaUpAnimator = ObjectAnimator.ofFloat(this, "alpha", 1.0f);
        ObjectAnimator scaleXDownAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE);
        ObjectAnimator scaleXUpAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1.0f);
        ObjectAnimator scaleYDownAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE);
        ObjectAnimator scaleYUpAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1.0f);

        downSet = new AnimatorSet();
        downSet.setDuration(DURATION);
        downSet.setInterpolator(new AccelerateInterpolator());
        downSet.playTogether(alphaDownAnimator, scaleXDownAnimator, scaleYDownAnimator);

        upSet = new AnimatorSet();
        upSet.setDuration(DURATION);
        upSet.setInterpolator(new FastOutSlowInInterpolator());
        upSet.playTogether(alphaUpAnimator, scaleXUpAnimator, scaleYUpAnimator);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchTime = System.currentTimeMillis();
                if (!pressed) {
                    effect(true);
                    pressed = true;
                    released = false;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!released) {
                    effect(false);
                    released = true;
                    pressed = false;
                }
                return false;
            case MotionEvent.ACTION_UP:
                if (!released) {
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

    private void effect(boolean press) {
        if (!isAnimation) {
            setAnimation();
            isAnimation = true;
        }
        if (press) {
            if (upSet != null)
                upSet.removeAllListeners();
            downSet.cancel();
            downSet.start();
        } else {
            long diffTime = System.currentTimeMillis() - touchTime;
            if (diffTime < DURATION) {
                upSet.setStartDelay(DURATION - diffTime);
            }
            upSet.cancel();
            upSet.start();
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        doSomething();
        return true;
    }

    private void doSomething() {
        Toast.makeText(getContext(), "did something", Toast.LENGTH_SHORT).show();
    }
}

