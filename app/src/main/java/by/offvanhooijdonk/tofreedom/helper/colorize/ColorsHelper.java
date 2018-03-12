package by.offvanhooijdonk.tofreedom.helper.colorize;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.view.View;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.helper.UIHelper;

public class ColorsHelper {
    private static final float COLOR_DARK_FACTOR = 0.8f;
    private static int[] BACK_COLORS;

    public static void setupScreenColors(Activity activity, ActionBar actionBar, View root) {
        Context ctx = activity.getBaseContext();
        long timeStart = PrefHelper.getCountdownStartDate(ctx);
        long timeFreedom = PrefHelper.getFreedomTime(ctx);
        int colorsNum = getBackColors(ctx).length;
        double timeElapsedPerCent = (double) (System.currentTimeMillis() - timeStart) / (timeFreedom - timeStart);
        int indexColor = (int) Math.floor(colorsNum * timeElapsedPerCent);
        int color = getBackColors(ctx)[indexColor];
        int colorDark = manipulateColor(color, COLOR_DARK_FACTOR);

        UIHelper.brushActionBar(activity, actionBar, color, colorDark);
        root.setBackgroundColor(color);
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

    private static int[] getBackColors(Context ctx) {
        if (BACK_COLORS == null) {
            BACK_COLORS = ctx.getResources().getIntArray(R.array.back_colors);
        }

        return BACK_COLORS;
    }

}
