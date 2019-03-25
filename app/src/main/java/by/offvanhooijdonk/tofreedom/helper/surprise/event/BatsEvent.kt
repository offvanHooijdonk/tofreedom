package by.offvanhooijdonk.tofreedom.helper.surprise.event

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.brushActionBar
import by.offvanhooijdonk.tofreedom.helper.colorize.ColorsHelper

class BatsEvent : BaseEvent() {

    companion object {
        private const val DURATION_SCALE: Long = 2200
        private const val DURATION_FADE_IN: Long = 1800
        private const val SCALE_START = 1.0f
        private const val SCALE_END = 8.0f
        private const val ROTATION_START = -15f
        private const val ROTATION_END = 15f
        private const val ALPHA_START = 0.0f
        private const val ALPHA_END = 0.8f
        private const val MAIN_INTERPOLATOR_FACTOR = 2.0f
    }

    override fun run() {
        val imgBats = insetBatsImage()

        val color = context.resources.getColor(R.color.night_back)
        brushActionBar(activity, actionBar, color, ColorsHelper.manipulateColor(color, 0.8f))// TODO animate backgr color change?
        root.setBackgroundColor(color)

        animateBatsImage(imgBats)
    }

    private fun insetBatsImage(): ImageView {
        val imgBats = ImageView(context)
        imgBats.setImageResource(R.drawable.ev_bats)
        imgBats.rotationY = 180f
        imgBats.alpha = ALPHA_START
        root.addView(imgBats, 0)
        imgBats.imageTintList = ColorStateList.valueOf(context.resources.getColor(R.color.event_bats))
        setupLayoutParams(imgBats)

        return imgBats
    }

    private fun animateBatsImage(imgBats: ImageView) {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(DURATION_SCALE)
        animator.interpolator = AccelerateInterpolator(MAIN_INTERPOLATOR_FACTOR)
        animator.addUpdateListener { a ->
            val value = a.animatedValue as Float
            val scale = calculateAnimValue(SCALE_START, SCALE_END, value)
            val rotation = calculateAnimValue(ROTATION_START, ROTATION_END, value)
            imgBats.scaleX = scale
            imgBats.scaleY = scale
            imgBats.rotation = rotation
        }
        animator.start()

        val animAlpha = ObjectAnimator.ofFloat(imgBats, View.ALPHA, ALPHA_START, ALPHA_END).setDuration(DURATION_FADE_IN)
        animAlpha.interpolator = AccelerateInterpolator(MAIN_INTERPOLATOR_FACTOR)
        animAlpha.start()

        Handler().postDelayed({
            val anim = ObjectAnimator.ofFloat(imgBats, View.ALPHA, ALPHA_END, ALPHA_START).setDuration(DURATION_SCALE - DURATION_FADE_IN)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    root.removeView(imgBats)
                }
            })
            anim.start()
        }, DURATION_FADE_IN)
    }

    private fun setupLayoutParams(v: View) {
        val lp = v.layoutParams as ConstraintLayout.LayoutParams

        lp.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

        v.layoutParams = lp
    }

    private fun calculateAnimValue(min: Float, max: Float, fraction: Float): Float {
        return min + (max - min) * fraction
    }

    class Builder : BaseEvent.BaseBuilder<BatsEvent>() {
        override var event: BatsEvent = BatsEvent()
    }
}
