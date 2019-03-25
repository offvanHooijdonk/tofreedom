package by.offvanhooijdonk.tofreedom.helper.surprise.event

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.brushActionBar
import by.offvanhooijdonk.tofreedom.helper.getColorValue
import by.offvanhooijdonk.tofreedom.helper.randomize

class ScreamEvent : BaseEvent() {

    companion object {
        private const val ROTATION_MIN = -10f
        private const val ROTATION_MAX = 20f
        private const val ICONS_NUMBER = 5
        private const val REPEAT_NUMBER = 3
        private const val DELAY_BETWEEN = 1_750L
        private const val DURATION_SINGLE = 600L
    }

    private var toStartSide = true

    override fun run() {
        val colorBack = context.getColorValue(R.color.event_scream_back)
        val colorBackDark = context.getColorValue(R.color.event_scream_back_dark)
        brushActionBar(activity, actionBar, colorBack, colorBackDark)
        root.setBackgroundColor(colorBack)

        var delay = 0
        for (i in 0 until ICONS_NUMBER) {
            Handler().postDelayed({
                val img = initScreamImage()
                animateScreamNod(img)
            }, delay.toLong())
            delay += DELAY_BETWEEN.toInt()
        }
    }

    private fun initScreamImage(): ImageView {
        val imgScream = ImageView(context)
        imgScream.setImageResource(R.drawable.ev_scream)
        imgScream.imageTintList = ColorStateList.valueOf(context.getColorValue(R.color.event_scream))
        root.addView(imgScream, 0)
        setupLayoutParams(imgScream)

        return imgScream
    }

    private fun animateScreamNod(v: View) {
        val size = context.resources.getDimensionPixelOffset(R.dimen.scream_size).toFloat()
        v.pivotX = size / 2
        v.pivotY = size / 1.5f
        val animator = ValueAnimator.ofInt(0, 1, 0).setDuration(DURATION_SINGLE)
        animator.addUpdateListener { a ->
            val value = a.animatedValue as Int
            v.rotation = value * (ROTATION_MAX - ROTATION_MIN) + ROTATION_MIN
        }
        animator.repeatCount = REPEAT_NUMBER
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                root.removeView(v)
            }
        })
        animator.start()
    }

    private fun setupLayoutParams(v: View) {
        val lp = v.layoutParams as ConstraintLayout.LayoutParams
        val size = context.resources.getDimensionPixelOffset(R.dimen.scream_size).toFloat()
        lp.width = size.toInt()//ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.height = size.toInt()//ConstraintLayout.LayoutParams.WRAP_CONTENT;
        lp.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        lp.leftMargin = randomize(root.width / 3 * if (toStartSide) 1 else 2, 0.4f)
        v.rotationY = (if (toStartSide) 0 else 180).toFloat()
        toStartSide = !toStartSide
        lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        lp.bottomMargin = randomize(300, 0.3f) // TODO to resources

        v.layoutParams = lp
    }

    class Builder : BaseEvent.BaseBuilder<ScreamEvent>() {
        override var event = ScreamEvent()
    }
}
