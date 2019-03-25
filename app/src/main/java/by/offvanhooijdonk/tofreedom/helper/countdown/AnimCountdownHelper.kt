package by.offvanhooijdonk.tofreedom.helper.countdown

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.*

class AnimCountdownHelper(fadeOutListener: Animator.AnimatorListener?, fadeInListener: Animator.AnimatorListener?) {

    private val poolFadeOut = HashMap<Any, ObjectAnimator>()
    private val poolFadeIn = HashMap<Any, ObjectAnimator>()

    private val listFadeOut = ArrayList<Animator>()
    private val listFadeIn = ArrayList<Animator>()

    private val setFadeOut = AnimatorSet()
    private val setFadeIn = AnimatorSet()

    init {
        fadeInListener?.let { setFadeIn.addListener(it) }
        fadeOutListener?.let { setFadeOut.addListener(it) }

        setFadeIn.interpolator = AccelerateDecelerateInterpolator()
        setFadeOut.interpolator = AccelerateDecelerateInterpolator()
    }

    fun addView(v: View) {
        if (poolFadeOut[v] == null) {
            poolFadeOut[v] = ObjectAnimator.ofFloat(v, View.ALPHA, ALPHA_ON, ALPHA_OFF).setDuration(DURATION_DEFAULT)
        }

        if (poolFadeIn[v] == null) {
            poolFadeIn[v] = ObjectAnimator.ofFloat(v, View.ALPHA, ALPHA_OFF, ALPHA_ON).setDuration(DURATION_DEFAULT)
        }

        poolFadeOut[v]?.let { listFadeOut.add(it) }
        poolFadeIn[v]?.let { listFadeIn.add(it) }
    }

    @JvmOverloads
    fun animateFadeOut(fadeOutListener: Animator.AnimatorListener?, duration: Long = DURATION_DEFAULT) {
        if (fadeOutListener != null) {
            setFadeOut.addListener(fadeOutListener)
        }
        setFadeOut.playTogether(listFadeOut)
        setFadeOut.duration = duration
        setFadeOut.start()
    }

    fun animateFadeIn() {
        setFadeIn.playTogether(listFadeIn)
        setFadeIn.start()
    }

    fun clearAnimations() {
        listFadeIn.clear()
        listFadeOut.clear()
    }
}

const val DURATION_DEFAULT = 350L
const val DURATION_SHORT = 75L
const val ALPHA_ON = 1.0f
const val ALPHA_OFF = 0.0f

fun View.fadeAway() {
    ObjectAnimator.ofFloat(this, View.ALPHA, ALPHA_ON, ALPHA_OFF)
            .apply {
                duration = DURATION_DEFAULT
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                        alpha = ALPHA_ON
                    }
                })
            }.start()
}