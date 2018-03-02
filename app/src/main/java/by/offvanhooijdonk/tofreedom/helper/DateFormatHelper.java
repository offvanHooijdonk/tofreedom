package by.offvanhooijdonk.tofreedom.helper;

import android.annotation.SuppressLint;
import android.util.Log;

import net.time4j.CalendarUnit;
import net.time4j.ClockUnit;
import net.time4j.IsoUnit;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import org.joda.time.Period;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;
import by.offvanhooijdonk.tofreedom.helper.countdown.CountdownBean;

@SuppressLint("SimpleDateFormat")
public class DateFormatHelper {
    private static final String SECOND_MAX = "59";

    private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("mm");
    private static final SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("ss");
    
    
    private static final DateFormat START_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
    private static final DateFormat START_TIME_FORMAT_24 = new SimpleDateFormat("HH:mm");
    private static final DateFormat START_TIME_FORMAT = new SimpleDateFormat("hh:mm a");

    private static final DateFormat SHARE_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private static final DateFormat SHARE_TIME_FORMAT_24 = START_TIME_FORMAT_24;
    private static final DateFormat SHARE_TIME_FORMAT = START_TIME_FORMAT;

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
        long currentTime = System.currentTimeMillis();
        formatForCountdown(countdownBean, currentTime, currentTime + timeMillis);
    }

    public static void formatForCountdown(CountdownBean countdownBean, long timeFrom, long timeTo) {
        Period period = new Period(timeFrom, timeTo);

        countdownBean.year = String.valueOf(period.getYears()).intern();
        countdownBean.month = String.valueOf(period.getMonths()).intern();
        countdownBean.day = String.valueOf(period.getDays() + 7 * period.getWeeks()).intern();
        countdownBean.hour = String.valueOf(period.getHours()).intern();
        countdownBean.minute = MINUTE_FORMAT.format(FORMATTED_DATE).intern();
        countdownBean.second = SECOND_FORMAT.format(FORMATTED_DATE).intern();
    }

    public static String formatForStart(long timeMillis, boolean is24Hours) {
        Date date = new Date(timeMillis);
        String formatted = is24Hours ? START_TIME_FORMAT_24.format(date) : START_TIME_FORMAT.format(date);
        if (!isDateDefault(timeMillis)) {
            formatted = START_DATE_FORMAT.format(date) + " " + formatted;
        }

        return formatted;
    }

    public static String formatForShare(long timeMillis, boolean is24Hours) {
        Date date = new Date(timeMillis);
        String formatted = is24Hours ? SHARE_TIME_FORMAT_24.format(date) : SHARE_TIME_FORMAT.format(date);
        formatted = SHARE_DATE_FORMAT.format(date) + " " + formatted;


        return formatted;
    }

    public static String formatElapsed(long timeFrom, long timeTo) {
        long totalMinutes = (timeTo - timeFrom) / (60 * 1000);
        return DecimalFormat.getNumberInstance().format(totalMinutes) + " " + getLocalDateTimeText(String.valueOf(totalMinutes), ClockUnit.MINUTES);
        /*CountdownBean bean = new CountdownBean();
        formatForCountdown(bean, timeFrom, timeTo);

        return getLocalDateTimeText(bean.year, CalendarUnit.YEARS) +
                getLocalDateTimeText(bean.month, CalendarUnit.MONTHS) +
                getLocalDateTimeText(bean.day, CalendarUnit.DAYS) +
                getLocalDateTimeText(bean.hour, ClockUnit.HOURS) +
                getLocalDateTimeText(bean.minute, ClockUnit.MINUTES);*/
    }

    /*private static String getLocalDateTimeText(String value, IsoUnit unit) {
        if (value != null && !value.isEmpty()) {
            int valueNum = Integer.valueOf(value);
            return unit instanceof CalendarUnit ? PrettyTime.of(Locale.getDefault()).print(valueNum, (CalendarUnit) unit, TextWidth.WIDE) :
                    PrettyTime.of(Locale.getDefault()).print(valueNum, (ClockUnit) unit, TextWidth.WIDE);
        } else {
            // TODO handle properly
            return "";
        }
    }*/

    public static String getLocalDateTimeText(String value, IsoUnit unit) {
        if (value != null && !value.isEmpty()) {
            int valueNum = Integer.valueOf(value);
            String unitText = unit instanceof CalendarUnit ? PrettyTime.of(Locale.getDefault()).print(valueNum, (CalendarUnit) unit, TextWidth.WIDE) :
                    PrettyTime.of(Locale.getDefault()).print(valueNum, (ClockUnit) unit, TextWidth.WIDE);
            try {
                unitText = unitText.substring(value.length() + 1).intern(); // TODO rtl support?
            } catch (IndexOutOfBoundsException e) {
                Log.e(ToFreedomApp.LOG, "Error while substring date locale representation: " + unitText, e);
                return "";
            }
            return unitText;
        } else {
            // TODO handle properly
            return "";
        }
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
