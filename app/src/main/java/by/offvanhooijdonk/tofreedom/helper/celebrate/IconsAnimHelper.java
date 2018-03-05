package by.offvanhooijdonk.tofreedom.helper.celebrate;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;

import by.offvanhooijdonk.tofreedom.R;

import static by.offvanhooijdonk.tofreedom.helper.RandomHelper.randomize;

public class IconsAnimHelper {
    private static final int DELAY_IN_SERIES = 200;// TODO move to builder
    private static final int DEFAULT_DELAY_BETWEEN_SERIES = 2800;
    private static final int DEFAULT_ICON_LIFE_SPAN = 500;
    private static final int FADE_IN_DURATION = 300;
    private static final int FADE_OUT_DURATION = 200;

    private static final int[] ICONS_RES = new int[]{
            R.drawable.f_paw_24, R.drawable.f_star_border_24,
            R.drawable.f_butterfly, R.drawable.f_cat_border,
            R.drawable.f_unicorn_baby, R.drawable.f_unicorn,
            R.drawable.f_flower_cam, R.drawable.f_flower_rose,
            R.drawable.f_puppy
    };
    private static int[] COLORS_RES = null;

    private Context ctx;
    private ViewGroup root;
    private int minNumber = 1;
    private int maxNumber = 3;
    private int seriesNumber = 3;
    private boolean withRotation = false;
    private int rotationMin = -30;
    private int rotationMax = 30;
    private int iconLifeSpan = DEFAULT_ICON_LIFE_SPAN;
    private int delayBetweenSeriesStart = DEFAULT_DELAY_BETWEEN_SERIES;
    private Random random = new Random();

    private IconsAnimHelper(Context context) {
        this.ctx = context;

        COLORS_RES = ctx.getResources().getIntArray(R.array.fancy_colors);
    }

    public void runIcons() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        int yAnchor = (int) (root.getHeight() * .75);
        int xAnchor = root.getWidth() / 2;

        long delay = 0;
        for (int i = 0; i < seriesNumber; i++) {
            int num = new Random().nextInt(maxNumber - minNumber + 1) + minNumber;
            int imageRes = getRandom(ICONS_RES);

            for (int j = 0; j < num; j++) {
                delay += j == 0 ? 0 : randomize(DELAY_IN_SERIES, .3f); //  TODO randomize
                int x = randomize(xAnchor, 1f);
                int y = randomize(yAnchor, 0.3f);
                View v = setupIcon(inflater, imageRes, x, y);
                new Handler().postDelayed(() -> {
                    root.addView(v);
                    ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f).setDuration(FADE_IN_DURATION).start();
                }, delay);

                new Handler().postDelayed(() -> {
                    ObjectAnimator.ofFloat(v, View.ALPHA, 1f, 0f).setDuration(FADE_OUT_DURATION).start();
                }, delay + num * DELAY_IN_SERIES + iconLifeSpan);
            }
            delay += randomize(delayBetweenSeriesStart, .6f);
        }

    }

    private View setupIcon(LayoutInflater inflater, int imageResource, int x, int y) {
        ImageView v = (ImageView) inflater.inflate(R.layout.icon_fancy, root, false);
        v.setImageResource(imageResource);
        v.setImageTintList(ColorStateList.valueOf(getRandom(COLORS_RES)));

        v.setX(x);
        v.setY(y);

        if (withRotation) {
            v.setRotation(random.nextInt(rotationMax - rotationMin) + rotationMin);
        }

        return v;
    }

    private int getRandom(int[] array) { // TODO move to helper
        int index = random.nextInt(array.length);
        return array[index];
    }

    private <T> T getRandom(List<T> array) {
        int index = random.nextInt(array.size());
        return array.get(index);
    }

    private int getHPosition(int totalSpace, int number, int index) {
        Log.i("break-free", "fancy position " + index);
        return (int) (totalSpace * ((float) index / (number - 1)));
    }

    public void dropToInitial() {
        View v;
        while ((v = root.findViewWithTag(ctx.getString(R.string.tag_fancy_icon))) != null) root.removeView(v);
    }

    public static class Builder {
        private IconsAnimHelper helperObject;

        public Builder(Context context, ViewGroup root) {
            helperObject = new IconsAnimHelper(context);
            helperObject.root = root;
        }

        public Builder rotation(int degreesMin, int degreesMax) {
            helperObject.rotationMin = degreesMin;
            helperObject.rotationMax = degreesMax;
            return this;
        }

        public Builder withRotation(boolean withRotation) {
            helperObject.withRotation = withRotation;
            return this;
        }

        public Builder iconLifeSpan(int iconLifeSpanMillis) {
            helperObject.iconLifeSpan = iconLifeSpanMillis;
            return this;
        }

        public Builder timeBetweenSeriesStart(int timeDelayMillis) {
            helperObject.delayBetweenSeriesStart = timeDelayMillis;
            return this;
        }

        public Builder minMaxIcons(int min, int max) {
            helperObject.minNumber = min;
            helperObject.maxNumber = max;
            return this;
        }

        public Builder seriesNumber(int number) {
            helperObject.seriesNumber = number;
            return this;
        }

        public IconsAnimHelper build() {
            return helperObject;
        }
    }
}
