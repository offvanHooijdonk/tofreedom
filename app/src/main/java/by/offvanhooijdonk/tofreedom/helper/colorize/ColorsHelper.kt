package by.offvanhooijdonk.tofreedom.helper.colorize

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v7.app.ActionBar
import android.view.View
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.PrefHelper
import by.offvanhooijdonk.tofreedom.helper.brushActionBar

object ColorsHelper {
    private const val COLOR_DARK_FACTOR = 0.8f
    private var BACK_COLORS: IntArray? = null

    fun setupScreenColors(activity: Activity, actionBar: ActionBar, root: View) {
        val ctx = activity.baseContext
        val timeStart = PrefHelper.getCountdownStartDate(ctx)
        val timeFreedom = PrefHelper.getFreedomTime(ctx)
        val colorsNum = getBackColors(ctx).size
        val timeElapsedPerCent = (System.currentTimeMillis() - timeStart).toDouble() / (timeFreedom - timeStart)
        val indexColor = Math.floor(colorsNum * timeElapsedPerCent).toInt()
        val color = getBackColors(ctx)[indexColor]
        val colorDark = manipulateColor(color, COLOR_DARK_FACTOR)

        brushActionBar(activity, actionBar, color, colorDark)
        root.setBackgroundColor(color)
    }

    fun manipulateColor(color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255))
    }

    private fun getBackColors(ctx: Context): IntArray {
        if (BACK_COLORS == null) {
            BACK_COLORS = ctx.resources.getIntArray(R.array.back_colors)
        }

        return BACK_COLORS!!
    }

}
