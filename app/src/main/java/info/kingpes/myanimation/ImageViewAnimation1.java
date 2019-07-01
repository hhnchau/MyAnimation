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

public class ImageViewAnimation1 extends android.support.v7.widget.AppCompatImageView {
    private static final int DURATION = 100;
    private static final float SCALE = 0.9f;
    private static final float ALPHA = 0.5f;
    private boolean isAnimation = true;
    AnimatorSet downSet;
    AnimatorSet upSet;

    public ImageViewAnimation1(Context context) {
        super(context);
    }

    public ImageViewAnimation1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewAnimation1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDownAnimation() {
        isAnimation = false;
        ObjectAnimator alphaDownAnimator = ObjectAnimator.ofFloat(this, "alpha", ALPHA);
        ObjectAnimator scaleXDownAnimator = ObjectAnimator.ofFloat(this, "scaleX", SCALE);
        ObjectAnimator scaleYDownAnimator = ObjectAnimator.ofFloat(this, "scaleY", SCALE);
        downSet = new AnimatorSet();
        downSet.setDuration(DURATION);
        downSet.setInterpolator(new AccelerateInterpolator());
        downSet.playTogether(alphaDownAnimator, scaleXDownAnimator, scaleYDownAnimator);
        downSet.cancel();
        downSet.start();
    }

    public void setUpAnimation() {
        isAnimation = true;
        ObjectAnimator alphaUpAnimator = ObjectAnimator.ofFloat(this, "alpha", 1.0f);
        ObjectAnimator scaleXUpAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1.0f);
        ObjectAnimator scaleYUpAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1.0f);
        upSet = new AnimatorSet();
        upSet.setDuration(DURATION);
        upSet.setInterpolator(new FastOutSlowInInterpolator());
        upSet.playTogether(alphaUpAnimator, scaleXUpAnimator, scaleYUpAnimator);
        upSet.cancel();
        upSet.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        animation(isAnimation);
        return super.onTouchEvent(event);
    }


    private void animation(boolean isAnimation) {
        if (isAnimation) {
            setDownAnimation();
        } else {
            setUpAnimation();
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

