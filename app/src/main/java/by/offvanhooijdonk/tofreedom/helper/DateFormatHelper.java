package by.offvanhooijdonk.tofreedom.helper;

import android.annotation.SuppressLint;

import org.joda.time.Period;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import by.offvanhooijdonk.tofreedom.helper.countdown.CountdownBean;

@SuppressLint("SimpleDateFormat")
public class DateFormatHelper {
    private static final String SECOND_MAX = "59";

    private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("mm");
    private static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("ss");
    
    
    private static final DateFormat START_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
    private static final DateFormat START_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private static final Date FORMATTED_DATE = new Date();

    public static void formatCountdownForUpdate(CountdownBean countdownBean, long timeMillis) {
        FORMATTED_DATE.setTime(timeMillis);

        String seconds = SECOND_FORMAT.format(FORMATTED_DATE).intern();

        if (seconds.equals(SECOND_MAX)) {
            formatForCountdown(countdownBean, timeMillis);
        } else {
            countdownBean.second = seconds;
        }
    }

    public static void formatForCountdown(CountdownBean countdownBean, long timeMillis) {
        FORMATTED_DATE.setTime(timeMillis);
        Period period = new Period(0, timeMillis);

        countdownBean.year = String.valueOf(period.getYears()).intern();
        countdownBean.month = String.valueOf(period.getMonths()).intern();
        countdownBean.day = String.valueOf(period.getDays() + 7 * period.getWeeks()).intern();
        countdownBean.hour = String.valueOf(period.getHours()).intern();
        countdownBean.minute = MINUTE_FORMAT.format(FORMATTED_DATE).intern();
        countdownBean.second = SECOND_FORMAT.format(FORMATTED_DATE).intern();
    }

    public static String formatForStart(long timeMillis) {
        String formatted = START_TIME_FORMAT.format(new Date(timeMillis));
        if (!isDateDefault(timeMillis)) {
            formatted = START_DATE_FORMAT.format(new Date(timeMillis)) + " " + formatted;
        }

        return formatted;
    }

    /**
     *
     * @return <code>true</code> if _date_ differs from the default
     */
    private static boolean isDateDefault(long timeInMillis) {
        Calendar defaultDate = Calendar.getInstance();
        defaultDate.setTimeInMillis(0);

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timeInMillis);

        if (defaultDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && defaultDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                && defaultDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else {
            return false;
        }
    }
}
