package by.offvanhooijdonk.tofreedom.ui.countdown

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper.getLocalDateTimeText
import by.offvanhooijdonk.tofreedom.helper.PrefHelper
import by.offvanhooijdonk.tofreedom.helper.colorize.ColorsHelper
import by.offvanhooijdonk.tofreedom.helper.countdown.*
import by.offvanhooijdonk.tofreedom.helper.surprise.SurpriseHelper
import kotlinx.android.synthetic.main.frag_countdown.*
import net.time4j.CalendarUnit

class CountdownFragment : Fragment() {
    private var countdownTimer: CountDownTimer? = null
    private val countdown = CountdownBean()
    private var freedomTime: Long = 0
    private val emptyCountdown = CountdownBean()
    private var animHelper: AnimCountdownHelper = AnimCountdownHelper(FadeOutListener(), FadeInListener())
    private val builderTime = StringBuilder()
    private var prevTimeTextLength = 0
    private var ctx: Context? = null
    private lateinit var surpriseHelper: SurpriseHelper

    companion object {
        const val EMPTY_STRING = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.frag_countdown, container, false)

        ctx = requireContext()

        DateFormatHelper.formatForCountdown(emptyCountdown, 0)
        initCountdown()

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.let { ColorsHelper.setupScreenColors(requireActivity(), it, root!!) }

        surpriseHelper = SurpriseHelper(requireContext(), requireActivity(), root, actionBar)
        Handler().postDelayed({ surpriseHelper.startRollingEvent() }, 100)
    }

    override fun onResume() {
        super.onResume()

        val newFreedomTime = PrefHelper.getFreedomTime(ctx)
        if (freedomTime != newFreedomTime) {
            freedomTime = newFreedomTime
            if (countdownTimer != null) {
                countdownTimer!!.cancel()
            }

            initCountdown()
        }
    }

    private fun onCountdownChange(diffCountdown: CountdownBean) {
        updateCountdownWithDiff(diffCountdown)

        countdown.year?.let {
            txtYears!!.setTime(it)
            txtLabelYear!!.text = getLocalDateTimeText(it, CalendarUnit.YEARS)
        }

        countdown.month?.let {
            txtMonths!!.setTime(it)
            txtLabelMonth!!.text = getLocalDateTimeText(it, CalendarUnit.MONTHS)
        }
        countdown.day?.let {
            txtDays!!.setTime(it)
            txtLabelDay!!.text = getLocalDateTimeText(it, CalendarUnit.DAYS)
        }

        val timeText = timeToString()
        //Log.i(ToFreedomApp.LOG, "Time to output: " + timeText);
        // check if text size changes substantially - then add fade animation to hide view resize
        val textDiff = prevTimeTextLength - timeText.length
        if (textDiff >= 2 || textDiff <= -2) {
            // TODO do some animation to smooth the process
            animHelper.addView(txtTime)
            animHelper.animateFadeOut(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    txtTime.setTime(timeText)
                }
            }, DURATION_SHORT)
        } else {
            txtTime.setTime(timeText)
        }
        prevTimeTextLength = timeText.length
    }

    private fun onCountdownElapsed() {
        handleFinish()
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelCountdown()
    }

    override fun onDetach() {
        super.onDetach()

        cancelCountdown()
    }

    private fun handleFinish() {
        // todo set MainView to the fragment
        if (activity is MainView) {
            (activity as MainView).onCountDownFinished()
        }
    }

    private fun initCountdown() {
        freedomTime = PrefHelper.getFreedomTime(ctx)
        val timeDiff = freedomTime - System.currentTimeMillis()

        DateFormatHelper.formatForCountdown(countdown, timeDiff)
        initCountdownValue()
        drawInitialCountdown()
        countdownTimer = FreedomCountdownTimer(freedomTime - System.currentTimeMillis(), ::onCountdownChange, ::onCountdownElapsed).start()
    }

    private fun initCountdownValue() {

        val isYearEmpty = countdown.year == emptyCountdown.year
        val isMonthEmpty = /*isYearEmpty && */countdown.month == emptyCountdown.month
        val isDayEmpty = /*isMonthEmpty && */countdown.day == emptyCountdown.day
        val isHourEmpty = /*isDayEmpty && */countdown.hour == emptyCountdown.hour
        val isMinuteEmpty = /*isHourEmpty && */countdown.minute == emptyCountdown.minute

        countdown.year = if (isYearEmpty) null else countdown.year
        countdown.month = if (isMonthEmpty) null else countdown.month
        countdown.day = if (isDayEmpty) null else countdown.day
        countdown.hour = if (isHourEmpty) null else countdown.hour
        countdown.minute = if (isMinuteEmpty) null else countdown.minute
    }

    private fun updateCountdownWithDiff(diffCountdown: CountdownBean) {
        if (countdown.year != null) {
            countdown.year = pickChanges(countdown.year, diffCountdown.year/*, emptyCountdown.year*/)
            if (countdown.year == null) blockYear.fadeAway()
        }

        if (countdown.month != null) {
            countdown.month = pickChanges(countdown.month, diffCountdown.month/*, emptyCountdown.month*/)
            if (countdown.month == null) {
                txtMonths.fadeAway()
                txtLabelMonth.fadeAway()
            }
        }

        if (countdown.day != null) {
            countdown.day = pickChanges(countdown.day, diffCountdown.day/*, emptyCountdown.day*/)
            if (countdown.day == null) {
                txtDays.fadeAway()
                txtLabelDay.fadeAway()
            }
        }

        if (countdown.month == null && countdown.day == null) {
            Handler().postDelayed({ blockMonthDay!!.visibility = View.GONE }, DURATION_DEFAULT)
        }
        countdown.hour = pickChanges(countdown.hour, diffCountdown.hour/*, emptyCountdown.hour*/)
        countdown.minute = pickChanges(countdown.minute, diffCountdown.minute/*, emptyCountdown.minute*/)

        // assume seconds always change
        countdown.second = diffCountdown.second
    }

    private fun pickChanges(currValue: String?, diffValue: String?/*, emptyValue: String*/): String? =
            diffValue?.takeIf { it != EMPTY_STRING } ?: currValue
    /*if (diffValue != null)
        if (diffValue == EMPTY_STRING) null else diffValue
    else
        currValue*/


    private fun timeToString(): String {
        builderTime.delete(0, builderTime.length)
        if (countdown.hour != null) {
            builderTime.append(countdown.hour).append(this.getString(R.string.time_delimiter))
        }
        if (countdown.minute != null) {
            builderTime.append(countdown.minute).append(this.getString(R.string.time_delimiter))
        }

        builderTime.append(countdown.second)

        return builderTime.toString()
    }

    /*private String getLocalDateTimeText(String value, CalendarUnit unit) {
        if (value != null && !value.isEmpty()) {
            int valueNum = Integer.valueOf(value);
            String unitText = PrettyTime.of(Locale.getDefault()).print(valueNum, unit, TextWidth.WIDE);
            try {
                unitText = unitText.substring(unitText.indexOf(value) + 2).intern();
            } catch (IndexOutOfBoundsException e) {
                Log.e(ToFreedomApp.LOG, "Error while substring date locale representation: " + unitText, e);
                return "";
            }
            return unitText;
        } else {
            // TODO handle properly
            return "";
        }
    }*/

    private fun drawInitialCountdown() {
        if (countdown.year == null) {
            blockYear!!.visibility = View.GONE
        } else {
            txtYears!!.setTime(countdown.year)
            blockYear!!.visibility = View.VISIBLE
        }

        if (countdown.month == null && countdown.day == null) {
            blockMonthDay!!.visibility = View.GONE
        } else {
            if (countdown.month == null) {
                txtMonths!!.visibility = View.GONE
                txtLabelMonth!!.visibility = View.GONE
            } else {
                txtMonths!!.setTime(countdown.month)
                txtMonths!!.visibility = View.VISIBLE
                txtLabelMonth!!.visibility = View.VISIBLE
            }

            if (countdown.day == null) {
                txtDays!!.visibility = View.GONE
                txtLabelDay!!.visibility = View.GONE
            } else {
                txtDays!!.setTime(countdown.day)
                txtDays!!.visibility = View.VISIBLE
                txtLabelDay!!.visibility = View.VISIBLE
            }
        }

        val timeText = timeToString()
        prevTimeTextLength = timeText.length
        txtTime!!.setTime(timeText)
    }

    private fun cancelCountdown() {
        if (countdownTimer != null) {
            countdownTimer!!.cancel()
        }
    }

    private inner class FadeOutListener : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)

            // TODO do all settings and hidings here
            //txtSecond.setText(diffCountdown.second);

            animHelper.animateFadeIn()
        }

        override fun onAnimationCancel(animation: Animator) {
            super.onAnimationCancel(animation)
            animHelper.clearAnimations()
        }
    }

    private inner class FadeInListener : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)

            animHelper.clearAnimations()
        }

        override fun onAnimationCancel(animation: Animator) {
            super.onAnimationCancel(animation)
            animHelper.clearAnimations()
        }
    }
}
