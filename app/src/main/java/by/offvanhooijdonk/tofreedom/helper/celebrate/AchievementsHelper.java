package by.offvanhooijdonk.tofreedom.helper.celebrate;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class AchievementsHelper {
    private static final int DURATION_DEFAULT = 18000;
    private static final int DELAY_ACHIEVEMENTS_DEFAULT = 2300;
    private static final int REVEAL_DURATION_DEFAULT = 500;
    private static final float GUIDELINE_START = 0.5f;
    private static final float GUIDELINE_FINISH = 0.2f; // TODO setup in Builder

    private View viewToMove;
    private List<View> listRevealViews;
    private int duration = DURATION_DEFAULT;
    private Integer delayAchievements;

    public AchievementsHelper() {
        listRevealViews = new ArrayList<>();
    }

    public void runAchievements() {
        moveView();

        revealViews();
    }

    public void dropToInitial() {
        if (viewToMove != null) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewToMove.getLayoutParams();
            lp.guidePercent = GUIDELINE_START;
            viewToMove.setLayoutParams(lp);
        }

        for (View v : listRevealViews) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void moveView() {
        if (viewToMove != null) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewToMove.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(GUIDELINE_START, GUIDELINE_FINISH).setDuration(duration);
            animator.setInterpolator(new DecelerateInterpolator(1.5f));
            animator.addUpdateListener(animation -> {
                lp.guidePercent = (Float) animation.getAnimatedValue();
                viewToMove.setLayoutParams(lp);
            });
            animator.start();
        }
    }

    private void revealViews() {
        int delay = delayAchievements;
        for (View v : listRevealViews) {
            new Handler().postDelayed(() -> {
                v.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(v, View.ALPHA, 0.0f, 1.0f).setDuration(REVEAL_DURATION_DEFAULT).start();
            }, delay);
            delay += delayAchievements;
        }
    }

    public static class Builder {
        private AchievementsHelper helperObject;

        public Builder() {
            helperObject = new AchievementsHelper();
        }

        public Builder moveView(View viewToMove) {
            helperObject.viewToMove = viewToMove;
            return this;
        }

        public Builder addAchievement(View v) {
            helperObject.listRevealViews.add(v);
            return this;
        }

        public Builder duration(int duration) {
            helperObject.duration = duration;
            return this;
        }

        public Builder durationDefault() {
            helperObject.duration = DURATION_DEFAULT;
            return this;
        }

        public Builder delayBetweenAchievements(int delay) {
            helperObject.delayAchievements = delay;
            return this;
        }

        public Builder delayBetweenDefault() {
            helperObject.delayAchievements = DELAY_ACHIEVEMENTS_DEFAULT;
            return this;
        }

        public AchievementsHelper build() {
            return helperObject;
        }
    }
}
