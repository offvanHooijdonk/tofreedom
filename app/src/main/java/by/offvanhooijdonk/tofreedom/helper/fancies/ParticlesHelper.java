package by.offvanhooijdonk.tofreedom.helper.fancies;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

import java.util.HashMap;
import java.util.Map;

import by.offvanhooijdonk.tofreedom.R;

public class ParticlesHelper {

    public static class Fireworks {
        private static final int PARTICLES_AMOUNT_BASE = /*25*/ 12;
        private static final int DELAY_BASE = 400;
        private static final int NUMBER_BASE = 14;
        private static final int DURATION_BASE = 440;
        private static final int[] PARTICLE_RESOURCES = new int[]{R.drawable.particle_round_red, R.drawable.particle_round_blue, R.drawable.particle_round_green};

        private int maxXParticle;
        private int maxYParticle;
        private int marginParticle;
        private int amount;
        private long lastDelay;

        private Map<Long, ParticleBean> circleParticles;

        @SuppressLint("UseSparseArrays")
        public void initialize() {
            circleParticles = new HashMap<>();
            amount = randomize(PARTICLES_AMOUNT_BASE, 0.2f);
            long delay = 0;

            for (int i = 0; i < amount; i++) {
                delay += randomize(DELAY_BASE, 0.2f);

                ParticleBean bean = new ParticleBean(
                        getRandomParticleDrawable(),
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
            for (Long delay : circleParticles.keySet()) {
                new Handler().postDelayed(() -> { // TODO try reuse ParticleSystem
                    changeParticleLocation(v);
                    ParticleBean bean = circleParticles.get(delay);
                    new ParticleSystem(activity, NUMBER_BASE * 2, bean.getResDrawable(), DURATION_BASE * 2)
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

        private int getRandomParticleDrawable() {
            int index = (int) Math.floor(Math.random() * PARTICLE_RESOURCES.length);
            return PARTICLE_RESOURCES[index];
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
        public void runConfetti(Activity a, View v, boolean toLeft) { // TODO support rtl, use enum at least
            int angleStart = toLeft ? 90 : 0;
            int angleEnd = toLeft ? 180 : 90;
            Drawable confBlue = a.getDrawable(R.drawable.confetti_rect);
            DrawableCompat.setTint(confBlue, a.getResources().getColor(R.color.md_blue_300));
            new ParticleSystem(a, 60, /*R.drawable.confetti_rect*/confBlue, 10000)
                    .setSpeedModuleAndAngleRange(0.05f, 0.15f, angleStart, angleEnd)
                    .setRotationSpeed(144)
                    .setAcceleration(0.00005f, 90)
                    .emit(v, 4, 10000);

            Drawable confRed = a.getDrawable(R.drawable.confetti_rect);
            DrawableCompat.setTint(confRed, a.getResources().getColor(R.color.md_red_300));
            new ParticleSystem(a, 60, /*R.drawable.confetti_rect*/confRed, 10000)
                    .setSpeedModuleAndAngleRange(0.05f, 0.15f, angleStart, angleEnd)
                    .setRotationSpeed(144)
                    .setAcceleration(0.00005f, 90)
                    .emit(v, 4, 10000);
        }

        public long getDuration() {
            return 10000;
        }
    }

    private static int randomize(int base, float radius) {
        return (int) (base * (1 + (Math.random() - 0.5) * radius));
    }
}
