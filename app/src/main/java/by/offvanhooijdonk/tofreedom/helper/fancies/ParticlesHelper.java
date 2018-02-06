package by.offvanhooijdonk.tofreedom.helper.fancies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import by.offvanhooijdonk.tofreedom.R;

public class ParticlesHelper {
    private static final int OVERLAP_DURATION_BASE = 2000;

    public static class Fireworks {
        private static final int PARTICLES_AMOUNT_BASE = /*25*/ 12;
        private static final int DELAY_BASE = 400;
        private static final int NUMBER_BASE = 14;
        private static final int DURATION_BASE = 440;
        private static int[] FIREWORKS_COLOR_RESOURCES;

        private int maxXParticle;
        private int maxYParticle;
        private int marginParticle;
        private int amount;
        private long lastDelay;
        private boolean isStopped = false;

        private Map<Long, ParticleBean> circleParticles;

        @SuppressLint("UseSparseArrays")
        public void initialize(Context ctx) {
            if (isStopped) return;

            FIREWORKS_COLOR_RESOURCES = ctx.getResources().getIntArray(R.array.firework_colors);
            circleParticles = new HashMap<>();
            amount = randomize(PARTICLES_AMOUNT_BASE, 0.2f);
            long delay = 0;

            for (int i = 0; i < amount; i++) {
                if (isStopped) return;
                delay += randomize(DELAY_BASE, 0.2f);

                ParticleBean bean = new ParticleBean(
                        getRandomParticleColor(),
                        randomize(NUMBER_BASE, 0.3f),
                        0.1f,
                        0.2f,
                        randomize(DURATION_BASE, 0.5f)
                );

                circleParticles.put(delay, bean);
                lastDelay = delay;
            }
        }

        public long getLastDelay() {
            return lastDelay;
        }

        public void runParticles(Activity activity, View v) {
            if (isStopped) return;
            for (Long delay : circleParticles.keySet()) {
                new Handler().postDelayed(() -> { // TODO try reuse ParticleSystem
                    if (isStopped) return;
                    changeParticleLocation(v);
                    ParticleBean bean = circleParticles.get(delay);
                    new ParticleSystem(activity, NUMBER_BASE * 2, generateColoredParticle(activity, bean.getDrawableColor()), DURATION_BASE * 2)
                            .setSpeedRange(bean.getSpeedFrom(), bean.getSpeedTo())
                            .setFadeOut(bean.getDuration())
                            .setScaleRange(bean.getScaleFrom(), bean.getScaleTo())
                            .oneShot(v, bean.getNumber(), new DecelerateInterpolator());
                }, delay);
            }
        }

        public void setupMaxDimens(View v) {
            if (v != null) {
                maxXParticle = v.getWidth();
                maxYParticle = (int) (v.getHeight() * .75f);
                marginParticle = (int) (Math.min(maxXParticle, maxYParticle) * 0.1f);
            }
        }

        public void stop() {
            isStopped = true;
        }

        public void clearState() {
            isStopped = false;
        }

        private Drawable generateColoredParticle(Context ctx, int drawableColor) { // TODO make a map of available
            Drawable particle = ctx.getDrawable(R.drawable.particle_round);
            DrawableCompat.setTint(particle, drawableColor);

            return particle;
        }

        private int getRandomParticleColor() {
            return getRandom(FIREWORKS_COLOR_RESOURCES);
        }

        private void changeParticleLocation(View v) {
            v.setX(generateParticleCoord(maxXParticle));
            v.setY(generateParticleCoord(maxYParticle));
        }

        private int generateParticleCoord(int max) {
            return (int) (Math.random() * (max - 2 * marginParticle)) + marginParticle;
        }
    }

    public static class Confetti {
        private static final int EMIT_TIME_BASE = 10000;
        private static final float SPEED_MIN = 0.05f;
        private static final float SPEED_MAX = 0.15f;
        private static final float ROTATION_MIN = 90f;
        private static final float ROTATION_MAX = 180f;
        private static int[] CONFETTI_COLOR_RESOURCES;

        private boolean isStopped = false;
        Set<ParticleSystem> emitters = new HashSet<>();

        public void initialize(Context ctx) {
            if (isStopped) return;
            CONFETTI_COLOR_RESOURCES = ctx.getResources().getIntArray(R.array.confetti_colors);
        }
// TODO implement a way to stop emitting
        public void runConfetti(Activity a, View viewStartCorner, View viewEndCorner, long initialDelay) {
            if (isStopped) return;
            boolean isLtR = a.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR;
            long confDelay = initialDelay - generateConfettiOverlapTime();
            runConfetti(a, viewEndCorner, isLtR, confDelay);

            confDelay += EMIT_TIME_BASE - generateConfettiOverlapTime();
            runConfetti(a, viewStartCorner, !isLtR, confDelay);
        }

        public void stop() {
            isStopped = true;

            for (ParticleSystem ps : emitters) {
                //ps.stopEmitting();
                ps.cancel();
            }
            emitters.clear();
        }

        public void clearState() {
            isStopped = false;
        }

        private void runConfetti(Activity a, View v, boolean toLeft, long delay) {
            int[] confettiColors = getTwoRandom(CONFETTI_COLOR_RESOURCES);
            int angleStart = toLeft ? 90 : 0;
            int angleEnd = toLeft ? 180 : 90;

            ParticleSystem confettiOne = prepareConfetti(a, confettiColors[0], angleStart, angleEnd);
            ParticleSystem confettiTwo = prepareConfetti(a, confettiColors[1], angleStart, angleEnd);
            new Handler().postDelayed(() -> {
                if (isStopped) return;
                int emitTime = randomize(EMIT_TIME_BASE, 0.1f);
                confettiOne.emit(v, 4, emitTime);
                confettiTwo.emit(v, 4, emitTime);
                emitters.add(confettiOne);
                emitters.add(confettiTwo);
            }, delay);
        }

        private ParticleSystem prepareConfetti(Activity a,int color, int angleStart, int angleEnd) {
            Drawable confDrawable = a.getDrawable(R.drawable.confetti_rect);
            DrawableCompat.setTint(confDrawable, color);
            return new ParticleSystem(a, 60, confDrawable, EMIT_TIME_BASE * 2)
                    .setSpeedModuleAndAngleRange(SPEED_MIN, SPEED_MAX, angleStart, angleEnd)
                    .setRotationSpeedRange(ROTATION_MIN, ROTATION_MAX)
                    .setAcceleration(0.00005f, 90);
        }

        private static long generateConfettiOverlapTime() {
            return randomize(OVERLAP_DURATION_BASE, 0.3f);
        }
    }

    private static int randomize(int base, float radius) {
        return (int) (base * (1 + (Math.random() - 0.5) * radius));
    }

    private static int getRandom(int[] array) {
        int index = randomInt(0, array.length);//(int) Math.floor(Math.random() * array.length);

        return array[index];
    }

    /**
     * @param start inclusive
     * @param end   exclusive
     * @return random int
     */
    private static int randomInt(int start, int end) {
        return (int) Math.floor(Math.random() * (end - start)) + start; // use Random nextInt ?
    }

    private static int[] getTwoRandom(int[] array) {
        if (array.length == 0) {
            return new int[]{0, 0};
        } else if (array.length == 1) {
            return new int[]{array[0], 0};
        } else {
            int index = randomInt(0, array.length);
            int colorOne = array[index];
            index += randomInt(1, array.length - 1);
            index = index >= array.length ? index - array.length : index; // if new index bigger, then length - make a lap over
            int colorTwo = array[index];

            return new int[]{colorOne, colorTwo};
        }
    }
}
