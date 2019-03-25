package by.offvanhooijdonk.tofreedom.helper

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.v7.app.ActionBar
import android.view.WindowManager

fun Context.getColorValue(@ColorRes colorRes: Int) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(colorRes)
    } else {
        resources.getColor(colorRes)
    }

fun brushActionBar(activity: Activity, actionBar: ActionBar?, color: Int, colorDark: Int) {
    actionBar?.setBackgroundDrawable(ColorDrawable(color))

    val window = activity.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = colorDark
}