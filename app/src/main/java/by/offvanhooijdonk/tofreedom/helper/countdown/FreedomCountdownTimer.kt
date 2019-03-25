package by.offvanhooijdonk.tofreedom.helper.countdown

import android.os.CountDownTimer

import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper

/**
 * Created by Yahor_Fralou on 8/3/2017 12:05 PM.
 */

class FreedomCountdownTimer(millisInFuture: Long,
                            private val onChange: (diff: CountdownBean) -> Unit,
                            private val onElapsed: () -> Unit) : CountDownTimer(millisInFuture, UPDATE_INTERVAL) {

    private var prevCountdown = CountdownBean()
    private val currentCountdown = CountdownBean()
    private val diffCountdown = CountdownBean()

    companion object {
        private const val UPDATE_INTERVAL = 1000L
    }

    init {
        updateCountdown(prevCountdown, millisInFuture)
    }

    override fun onTick(millisUntilFinished: Long) {
        //Log.i(ToFreedomApp.LOG, "Millis Until Finished: " + millisUntilFinished);
        DateFormatHelper.formatCountdownForUpdate(currentCountdown, millisUntilFinished)
        //Log.i(ToFreedomApp.LOG, "Current Countdown" + currentCountdown.toString());

        if (currentCountdown != prevCountdown) {
            diffCountdown.year = pickChanges(currentCountdown.year, prevCountdown.year)
            diffCountdown.month = pickChanges(currentCountdown.month, prevCountdown.month)
            diffCountdown.day = pickChanges(currentCountdown.day, prevCountdown.day)
            diffCountdown.hour = pickChanges(currentCountdown.hour, prevCountdown.hour)
            diffCountdown.minute = pickChanges(currentCountdown.minute, prevCountdown.minute)
            diffCountdown.second = currentCountdown.second
        }

        onChange(diffCountdown)

        prevCountdown = currentCountdown.copy()
    }

    private fun pickChanges(currValue: String?, prevValue: String?): String? {
        return if (currValue == null || currValue == prevValue) null else currValue
    }

    override fun onFinish() {
        onElapsed()
    }

    private fun updateCountdown(cnt: CountdownBean, timeMillis: Long) {
        DateFormatHelper.formatForCountdown(cnt, timeMillis)
    }
}