package by.offvanhooijdonk.tofreedom.helper.surprise.event;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.UIHelper;
import by.offvanhooijdonk.tofreedom.helper.colorize.ColorsHelper;

public class BatsEvent extends BaseEvent {
    private static final long DURATION_SCALE = 2200;
    private static final long DURATION_FADE_IN = 1800;
    private static final float SCALE_START = 1.0f;
    private static final float SCALE_END = 8.0f;
    private static final float ROTATION_START = -15;
    private static final float ROTATION_END = 15;
    private static final float ALPHA_START = 0.0f;
    private static final float ALPHA_END = 0.8f;
    private static final float MAIN_INTERPOLATOR_FACTOR = 2.0f;

    @Override
    public void run() {
        ImageView imgBats = insetBatsImage();

        int color = getContext().getResources().getColor(R.color.night_back);
        UIHelper.brushActionBar(getActivity(), getActionBar(), color, ColorsHelper.manipulateColor(color, 0.8f));// TODO animate backgr color change?
        getRoot().setBackgroundColor(color);

        animateBatsImage(imgBats);
    }

    private ImageView insetBatsImage() {
        ImageView imgBats = new ImageView(getContext());
        imgBats.setImageResource(R.drawable.ev_bats);
        imgBats.setRotationY(180);
        imgBats.setAlpha(ALPHA_START);
        getRoot().addView(imgBats, 0);
        imgBats.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.event_bats)));
        setupLayoutParams(imgBats);

        return imgBats;
    }

    private void animateBatsImage(ImageView imgBats) {
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(DURATION_SCALE);
        animator.setInterpolator(new AccelerateInterpolator(MAIN_INTERPOLATOR_FACTOR));
        animator.addUpdateListener(a -> { //TODO use view.animate() 
            float value = (float) a.getAnimatedValue();
            float scale = calculateAnimValue(SCALE_START, SCALE_END, value);
            float rotation = calculateAnimValue(ROTATION_START, ROTATION_END, value);
            imgBats.setScaleX(scale);
            imgBats.setScaleY(scale);
            imgBats.setRotation(rotation);
        });
        animator.start();

        ObjectAnimator animAlpha = ObjectAnimator.ofFloat(imgBats, View.ALPHA, ALPHA_START, ALPHA_END).setDuration(DURATION_FADE_IN);
        animAlpha.setInterpolator(new AccelerateInterpolator(MAIN_INTERPOLATOR_FACTOR));
        animAlpha.start();

        new Handler().postDelayed(() -> {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(imgBats, View.ALPHA, ALPHA_END, ALPHA_START).setDuration(DURATION_SCALE - DURATION_FADE_IN);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getRoot().removeView(imgBats);
                        }
                    });
                    anim.start();
                }
                , DURATION_FADE_IN);
    }

    private void setupLayoutParams(View v) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) v.getLayoutParams();

        lp.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;

        v.setLayoutParams(lp);
    }

    private float calculateAnimValue(float min, float max, float fraction) {
        return min + (max - min) * fraction;
    }

    public static class Builder extends BaseBuilder<BatsEvent> {
        public Builder() {
            setEvent(new BatsEvent());
        }
    }
}
