package by.offvanhooijdonk.tofreedom.helper.surprise.event;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.RandomHelper;
import by.offvanhooijdonk.tofreedom.helper.UIHelper;

public class ScreamEvent extends BaseEvent {
    private static final float ROTATION_MIN = -10;
    private static final float ROTATION_MAX = 20;
    private static final int ICONS_NUMBER = 5;
    private static final int REPEAT_NUMBER = 3;
    private static final long DELAY_BETWEEN = 1750;
    private static final long DURATION_SINGLE = 600;

    boolean toStartSide = true;

    @Override
    public void run() {
        int colorBack = getContext().getResources().getColor(R.color.event_scream_back);
        int colorBackDark = getContext().getResources().getColor(R.color.event_scream_back_dark);
        UIHelper.brushActionBar(getActivity(), getActionBar(), colorBack, colorBackDark);
        getRoot().setBackgroundColor(colorBack);

        int delay = 0;
        for (int i = 0; i < ICONS_NUMBER; i++) {
            new Handler().postDelayed(() -> {
                ImageView img = initScreamImage();
                animateScreamNod(img);
            }, delay);
            delay += DELAY_BETWEEN;
        }
    }

    private ImageView initScreamImage() {
        ImageView imgScream = new ImageView(getContext());
        imgScream.setImageResource(R.drawable.ev_scream);
        imgScream.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.event_scream)));
        getRoot().addView(imgScream, 0);
        setupLayoutParams(imgScream);

        return imgScream;
    }

    private void animateScreamNod(View v) {
        float size = getContext().getResources().getDimensionPixelOffset(R.dimen.scream_size);
        v.setPivotX(size / 2);
        v.setPivotY(size / 1.5f);
        ValueAnimator animator = ValueAnimator.ofInt(0, 1, 0).setDuration(DURATION_SINGLE);
        animator.addUpdateListener(a -> {
            int value = (int) a.getAnimatedValue();
            v.setRotation(value * (ROTATION_MAX - ROTATION_MIN) + ROTATION_MIN);
        });
        animator.setRepeatCount(REPEAT_NUMBER);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getRoot().removeView(v);
            }
        });
        animator.start();
    }

    private void setupLayoutParams(View v) {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) v.getLayoutParams();
        float size = getContext().getResources().getDimensionPixelOffset(R.dimen.scream_size);
        lp.width = (int) size;//ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.height = (int) size;//ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.leftMargin = RandomHelper.randomize((getRoot().getWidth() / 3) * (toStartSide ? 1 : 2), 0.4f);
        v.setRotationY(toStartSide ? 0 : 180);
        toStartSide = !toStartSide;
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        lp.bottomMargin = RandomHelper.randomize(300, 0.3f); // TODO to resources

        v.setLayoutParams(lp);
    }

    public static class Builder extends BaseBuilder<ScreamEvent> {
        public Builder() {
            setEvent(new ScreamEvent());
        }
    }
}
