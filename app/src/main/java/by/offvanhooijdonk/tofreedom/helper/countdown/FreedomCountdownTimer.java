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
        DateFormatHelper.formatCountdownForUpdate(currentCountdown, millisUntilFinished);

        if (!currentCountdown.equals(prevCountdown)) {
            diffCountdown.year =    pickChanges(currentCountdown.year, prevCountdown.year);
            diffCountdown.month =   pickChanges(currentCountdown.month, prevCountdown.year);
            diffCountdown.day =     pickChanges(currentCountdown.day, prevCountdown.year);
            diffCountdown.hour =    pickChanges(currentCountdown.hour, prevCountdown.year);
            diffCountdown.minute =  pickChanges(currentCountdown.minute, prevCountdown.year);
            diffCountdown.second =  pickChanges(currentCountdown.second, prevCountdown.second);
        }

        if (listener != null) {
            listener.onCountdownChange(diffCountdown);
        }

        copyCountDownValues(prevCountdown, currentCountdown);
    }

    private String pickChanges(String currValue, String prevValue) {
        return currValue == null || currValue.equals(prevValue) ? null : currValue;
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

    public static void copyCountDownValues(CountdownBean dest, CountdownBean source) {
        dest.year = source.year;
        dest.month = source.month;
        dest.day = source.day;
        dest.hour = source.hour;
        dest.minute = source.minute;
        dest.second = source.second;
    }

    public interface CountdownListener {
        void onCountdownChange(CountdownBean diffCountdown);
        void onFinish();
    }
}
