package by.offvanhooijdonk.tofreedom.helper.celebrate;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

import by.offvanhooijdonk.tofreedom.R;

public class IconsAnimHelper {
    private static final int DELAY_IN_SERIES = 400;
    private static final int DELAY_BETWEEN_SERIES = 1200;
    private static final int[] ICONS_RES = new int[]{
            R.drawable.f_paw_24, R.drawable.f_star_border_24,
            R.drawable.f_butterfly, R.drawable.f_cat_border
    };
    private static int[] COLORS_RES = null;

    private Context ctx;
    private ViewGroup root;
    private int minNumber = 1;
    private int maxNumber = 3;
    private int seriesNumber = 3;
    private boolean withRotation = false;
    private int rotationMin = -45;
    private int rotationMax = 45;
    private Random random = new Random();

    private IconsAnimHelper(Context context) {
        this.ctx = context;

        COLORS_RES = ctx.getResources().getIntArray(R.array.fancy_colors);
    }

    public void runIcons() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        long delay = 0;
        for (int i = 0; i < seriesNumber; i++) {
            int num = new Random().nextInt(maxNumber - minNumber) + minNumber;
            int imageRes = getRandom(ICONS_RES);
            for (int j = 0; j < num; j++) {
                delay += j == 0 ? 0 : DELAY_IN_SERIES; //  TODO randomize
                new Handler().postDelayed(() -> {
                    View v = setupIcon(inflater, imageRes);
                    root.addView(v);
                    ObjectAnimator.ofFloat(v, View.ALPHA, 0.0f, 1.0f).start();
                }, delay);
            }
            delay += DELAY_BETWEEN_SERIES;
        }
    }

    private View setupIcon(LayoutInflater inflater, int imageResource) {
        ImageView v = (ImageView) inflater.inflate(R.layout.icon_fancy, root, false);
        v.setImageResource(imageResource);
        v.setImageTintList(ColorStateList.valueOf(getRandom(COLORS_RES)));

        v.setX(random.nextInt((int) (root.getWidth() * 0.8)) + (int) (root.getWidth() * 0.1));
        v.setY((int) (root.getHeight() * 0.5) + random.nextInt((int) (root.getHeight() * 0.3)));

        if (withRotation) {
            v.setRotation(random.nextInt(rotationMax - rotationMin) + rotationMin);
        }

        return v;
    }

    private int getRandom(int[] array) {
        int index = random.nextInt(array.length);
        return array[index];
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
