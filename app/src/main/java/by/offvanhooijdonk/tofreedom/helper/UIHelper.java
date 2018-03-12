package by.offvanhooijdonk.tofreedom.helper;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

public class UIHelper {
    public static void brushActionBar(Activity activity, ActionBar actionBar, int color, int colorDark) {
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }

        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorDark);
    }
}
