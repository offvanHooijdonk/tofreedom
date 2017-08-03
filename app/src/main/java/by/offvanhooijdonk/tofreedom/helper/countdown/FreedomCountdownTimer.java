package by.offvanhooijdonk.tofreedom.helper.countdown;

import android.os.CountDownTimer;

import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;

/**
 * Created by Yahor_Fralou on 8/3/2017 12:05 PM.
 */

public class FreedomCountdownTimer extends CountDownTimer {
    private static final int UPDATE_INTERVAL = 1000;

    private CountdownListener listener;
    private CountdownBean prevCountdown = new CountdownBean();
    private CountdownBean currentCountdown = new CountdownBean();
    private CountdownBean diffCountdown = new CountdownBean();
    private long millisInFuture;

    public FreedomCountdownTimer(long millisInFuture, CountdownListener l) {
        super(millisInFuture, UPDATE_INTERVAL);

        this.millisInFuture = millisInFuture;
        this.listener = l;

        updateCountdown(prevCountdown, millisInFuture - System.currentTimeMillis());
    }

    @Override
    public void onTick(long millisUntilFinished) {
        updateCountdown(currentCountdown, millisUntilFinished);

        if (!currentCountdown.equals(prevCountdown)) {
            diffCountdown.year =    currentCountdown.year.equals(prevCountdown.year) ? null :   currentCountdown.year;
            diffCountdown.month =   currentCountdown.month.equals(prevCountdown.month) ? null : currentCountdown.month;
            diffCountdown.day =     currentCountdown.day.equals(prevCountdown.day) ? null :     currentCountdown.day;
            diffCountdown.hour =    currentCountdown.hour.equals(prevCountdown.hour) ? null :   currentCountdown.hour;
            diffCountdown.minute =  currentCountdown.minute.equals(prevCountdown.minute) ? null : currentCountdown.minute;
            diffCountdown.second =  currentCountdown.second.equals(prevCountdown.second) ? null : currentCountdown.second;
        }

        if (listener != null) {
            listener.onCountdownChange(diffCountdown);
        }

        copyCurrentToPrevious();
    }

    @Override
    public void onFinish() {
        if (listener != null) {
            listener.onFinish();
        }
    }

    private void updateCountdown(CountdownBean cnt, long timeMillis) {
        DateFormatHelper.formatForCountdown(cnt, timeMillis);
    }

    private void copyCurrentToPrevious() {
        prevCountdown.year = currentCountdown.year;
        prevCountdown.month = currentCountdown.month;
        prevCountdown.day = currentCountdown.day;
        prevCountdown.hour = currentCountdown.hour;
        prevCountdown.minute = currentCountdown.minute;
        prevCountdown.second = currentCountdown.second;
    }

    public interface CountdownListener {
        void onCountdownChange(CountdownBean diffCountdown);
        void onFinish();
    }
}
