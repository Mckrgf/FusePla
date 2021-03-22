package com.supcon.mes.middleware.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.supcon.mes.middleware.R;

public class AnimationUtil {

    private ValueAnimator valueAnimator;
    private UpdateListener updateListener;
    private EndListener endListener;
    /**
     * 动画延迟时间
     */
    private long duration;
    
    private float start;
    private float end;
    private Interpolator interpolator = new LinearInterpolator();

    public AnimationUtil() {
        // 默认动画时常1s
        duration = 1000;
        start = 0.0f;
        end = 1.0f;
        // 匀速的插值器
        interpolator = new LinearInterpolator();
    }
    
    
    /**
     * 设置动画延迟时间
     * @param timeLength
     */
    public void setDuration(int timeLength) {
        duration = timeLength;
    }
    
    /**
     * 设置属性动画
     * @param start
     * @param end
     * @param duration
     */
    public void setValueAnimator(float start, float end, long duration) {
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void startAnimator() {
        if (valueAnimator != null) {
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(valueAnimator -> {

            if (updateListener == null) {
                return;
            }

            float cur = (float) valueAnimator.getAnimatedValue();
            updateListener.progress(cur);
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (endListener == null) {
                    return;
                }
                endListener.endUpdate(animator);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        valueAnimator.start();
    }

    public void addUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void addEndListner(EndListener endListener) {
        this.endListener = endListener;
    }

    public interface EndListener {
        void endUpdate(Animator animator);
    }

    public interface UpdateListener {
        void progress(float progress);
    }



    public static void setDefaultAnimation(Activity activity, int animationResId){
        activity.getWindow().setWindowAnimations(animationResId);
    }

    public static void setActivityDefaultAnimation(Activity activity){
        activity.getWindow().setWindowAnimations(R.style.activityAnimation);
    }

    public static void setBottomInAnimation(Activity activity){
        activity.getWindow().setWindowAnimations(R.style.settingAnimation);
    }
}
