package by.offvanhooijdonk.tofreedom.helper.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimCountdownHelper {
    public static final int DURATION_DEFAULT = 350;
    public static final int DURATION_SHORT = 75;
    private static final float ALPHA_ON = 1.0f;
    private static final float ALPHA_OFF = 0.0f;

    private Map<Object, ObjectAnimator> poolFadeOut = new HashMap<>();
    private Map<Object, ObjectAnimator> poolFadeIn = new HashMap<>();

    private List<Animator> listFadeOut = new ArrayList<>();
    private List<Animator> listFadeIn = new ArrayList<>();

    private AnimatorSet setFadeOut = new AnimatorSet();
    private AnimatorSet setFadeIn = new AnimatorSet();

    public AnimCountdownHelper(Animator.AnimatorListener fadeOutListener, Animator.AnimatorListener fadeInListener) {
        if (fadeOutListener != null) {
            setFadeOut.addListener(fadeOutListener);
        }
        if (fadeInListener != null) {
            setFadeIn.addListener(fadeInListener);
        }

        setFadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        setFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void addView(View v) {
        if (poolFadeOut.get(v) == null) {
            poolFadeOut.put(v, ObjectAnimator.ofFloat(v, View.ALPHA, ALPHA_ON, ALPHA_OFF).setDuration(DURATION_DEFAULT));
        }

        if (poolFadeIn.get(v) == null) {
            poolFadeIn.put(v, ObjectAnimator.ofFloat(v, View.ALPHA, ALPHA_OFF, ALPHA_ON).setDuration(DURATION_DEFAULT));
        }

        listFadeOut.add(poolFadeOut.get(v));
        listFadeIn.add(poolFadeIn.get(v));
    }

    public void animateFadeOut(Animator.AnimatorListener fadeOutListener) {
        animateFadeOut(fadeOutListener, DURATION_DEFAULT);
    }

    public void animateFadeOut(Animator.AnimatorListener fadeOutListener, int duration) {
        if (fadeOutListener != null) {
            setFadeOut.addListener(fadeOutListener);
        }
        setFadeOut.playTogether(listFadeOut);
        setFadeOut.setDuration(duration);
        setFadeOut.start();
    }

    public void animateFadeIn() {
        setFadeIn.playTogether(listFadeIn);
        setFadeIn.start();
    }

    public static void fadeAway(View v) {
        Animator animator = ObjectAnimator.ofFloat(v, View.ALPHA, ALPHA_ON, ALPHA_OFF).setDuration(DURATION_DEFAULT);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                v.setVisibility(View.GONE);
                v.setAlpha(ALPHA_ON);
            }
        });

        animator.start();
    }

    public void clearAnimations() {
        listFadeIn.clear();
        listFadeOut.clear();
    }

}
