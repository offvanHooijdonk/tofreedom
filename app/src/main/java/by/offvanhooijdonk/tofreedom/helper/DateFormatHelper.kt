package by.offvanhooijdonk.tofreedom.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import net.time4j.CalendarUnit
import net.time4j.ClockUnit
import net.time4j.IsoUnit
import net.time4j.PrettyTime
import net.time4j.format.TextWidth

import org.joda.time.Period

import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

import by.offvanhooijdonk.tofreedom.app.ToFreedomApp
import by.offvanhooijdonk.tofreedom.helper.countdown.CountdownBean

@SuppressLint("SimpleDateFormat")
object DateFormatHelper {
    private const val SECOND_MAX = "59"

    private val MINUTE_FORMAT = SimpleDateFormat("mm")
    private val SECOND_FORMAT = SimpleDateFormat("ss")


    private val START_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL)
    private val START_TIME_FORMAT_24 = SimpleDateFormat("HH:mm")
    private val START_TIME_FORMAT = SimpleDateFormat("hh:mm a")

    private val SHARE_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val SHARE_TIME_FORMAT_24 = START_TIME_FORMAT_24
    private val SHARE_TIME_FORMAT = START_TIME_FORMAT

    private val FORMATTED_DATE = Date()

    fun formatCountdownForUpdate(countdownBean: CountdownBean, timeMillis: Long) {
        FORMATTED_DATE.time = timeMillis

        val seconds = SECOND_FORMAT.format(FORMATTED_DATE).intern()

        if (seconds == SECOND_MAX) {
            formatForCountdown(countdownBean, timeMillis)
        } else {
            countdownBean.second = seconds
        }
    }

    fun formatForCountdown(countdownBean: CountdownBean, timeMillis: Long) {
        FORMATTED_DATE.time = timeMillis
        val currentTime = System.currentTimeMillis()
        formatForCountdown(countdownBean, currentTime, currentTime + timeMillis)
    }

    private fun formatForCountdown(countdownBean: CountdownBean, timeFrom: Long, timeTo: Long) {
        val period = Period(timeFrom, timeTo)

        countdownBean.year = period.years.toString().intern()
        countdownBean.month = period.months.toString().intern()
        countdownBean.day = (period.days + 7 * period.weeks).toString().intern()
        countdownBean.hour = period.hours.toString().intern()
        countdownBean.minute = MINUTE_FORMAT.format(FORMATTED_DATE).intern()
        countdownBean.second = SECOND_FORMAT.format(FORMATTED_DATE).intern()
    }

    fun formatForStart(timeMillis: Long, is24Hours: Boolean): String {
        val date = Date(timeMillis)
        var formatted = if (is24Hours) START_TIME_FORMAT_24.format(date) else START_TIME_FORMAT.format(date)
        if (!isDateDefault(timeMillis)) {
            formatted = START_DATE_FORMAT.format(date) + " " + formatted
        }

        return formatted
    }

    fun formatTime(ctx: Context, timeMillis: Long): String {
        val is24Hour = android.text.format.DateFormat.is24HourFormat(ctx)
        return (if (is24Hour) START_TIME_FORMAT_24 else START_TIME_FORMAT).format(Date(timeMillis))
    }

    fun formatForShare(timeMillis: Long, is24Hours: Boolean): String {
        val date = Date(timeMillis)
        var formatted = if (is24Hours) SHARE_TIME_FORMAT_24.format(date) else SHARE_TIME_FORMAT.format(date)
        formatted = SHARE_DATE_FORMAT.format(date) + " " + formatted


        return formatted
    }

    fun formatElapsed(timeFrom: Long, timeTo: Long): String {
        val totalMinutes = (timeTo - timeFrom) / (60 * 1000)
        return DecimalFormat.getNumberInstance().format(totalMinutes) + " " + getLocalDateTimeText(totalMinutes.toString(), ClockUnit.MINUTES)
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

    fun getLocalDateTimeText(value: String?, unit: IsoUnit): String {
        if (value != null && !value.isEmpty()) {
            val valueNum = Integer.valueOf(value)
            var unitText = if (unit is CalendarUnit)
                PrettyTime.of(Locale.getDefault()).print(valueNum.toLong(), unit, TextWidth.WIDE)
            else
                PrettyTime.of(Locale.getDefault()).print(valueNum.toLong(), unit as ClockUnit, TextWidth.WIDE)
            try {
                unitText = unitText.substring(value.length + 1).intern() // TODO rtl support?
            } catch (e: IndexOutOfBoundsException) {
                Log.e(ToFreedomApp.LOG, "Error while substring date locale representation: $unitText", e)
                return ""
            }

            return unitText
        } else {
            // TODO handle properly
            return ""
        }
    }

    /**
     *
     * @return `true` if _date_ differs from the default
     */
    private fun isDateDefault(timeInMillis: Long): Boolean {
        val defaultDate = Calendar.getInstance().apply { this.timeInMillis = 0 }
        val date = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }

        return defaultDate.get(Calendar.YEAR) == date.get(Calendar.YEAR)
                && defaultDate.get(Calendar.MONTH) == date.get(Calendar.MONTH)
                && defaultDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
    }
}
