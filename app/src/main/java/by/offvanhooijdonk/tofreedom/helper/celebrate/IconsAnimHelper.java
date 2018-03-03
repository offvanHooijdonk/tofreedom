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
    private static final int DELAY_IN_SERIES = 250;
    private static final int DELAY_BETWEEN_SERIES = 2800;
    private static final int ICON_LIFE_SPAN = 1500;// TODO make editable within Builder

    private static final int[] ICONS_RES = new int[]{
            R.drawable.f_paw_24, R.drawable.f_star_border_24,
            R.drawable.f_butterfly, R.drawable.f_cat_border,
            R.drawable.f_dog, R.drawable.f_unicorn
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
                    ObjectAnimator.ofFloat(v, View.ALPHA, 0f, 1f).start();
                }, delay);

                new Handler().postDelayed(() -> {
                    ObjectAnimator.ofFloat(v, View.ALPHA, 1f, 0f).start();
                }, delay + num * DELAY_IN_SERIES);
            }
            delay += randomize(DELAY_BETWEEN_SERIES, .6f);
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

    private int getRandom(int[] array) {
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
