package by.offvanhooijdonk.tofreedom.ui.pref

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.AlarmHelper
import by.offvanhooijdonk.tofreedom.helper.PrefHelper

class FreedomDateTimePreference : Preference, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private val valueAsCalendar: Calendar
        get() {
            val calendar = Calendar.getInstance()

            val currentValue = getPersistedLong(DEFAULT_VALUE.toLong())
            if (currentValue != DEFAULT_VALUE.toLong()) {
                calendar.timeInMillis = currentValue
            }

            return calendar
        }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onCreateView(parent: ViewGroup): View {
        var v: View? = super.onCreateView(parent)
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.pref_date_time, parent, false)
        }

        val currentValue = getPersistedLong(DEFAULT_VALUE.toLong())
        if (currentValue != DEFAULT_VALUE.toLong()) {
            updateSummary(currentValue)
        }

        v!!.setOnClickListener { startDateDialog() }

        return v
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val datePicked = valueAsCalendar
        datePicked.set(year, month, dayOfMonth)

        saveValue(datePicked.timeInMillis)

        startTimeDialog()
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val calendar = valueAsCalendar
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        // TODO add check that the date and time is in the future!

        PrefHelper.setCelebrateShown(context, false)
        saveValue(calendar.timeInMillis)
    }

    private fun saveValue(timeMillis: Long) {
        persistLong(timeMillis)
        AlarmHelper.setupFinishingNotification(context)

        updateSummary(timeMillis)
    }

    private fun updateSummary(timeMillis: Long) {
        val dateTime = DATE_FORMAT.format(Date(timeMillis)) + " " + TIME_FORMAT.format(Date(timeMillis))
        summary = dateTime
    }

    private fun startDateDialog() {
        val dateForDialog = valueAsCalendar

        val dialog = DatePickerDialog(context, this@FreedomDateTimePreference,
                dateForDialog.get(Calendar.YEAR),
                dateForDialog.get(Calendar.MONTH),
                dateForDialog.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.minDate = Date().time

        val now = Calendar.getInstance()
        now.add(Calendar.YEAR, PrefHelper.MAX_YEARS_AHEAD) // TODO move this to resources?
        dialog.datePicker.maxDate = now.timeInMillis

        dialog.show()
    }


    private fun startTimeDialog() {
        val timeForDialog = valueAsCalendar

        TimePickerDialog(context, this@FreedomDateTimePreference,
                timeForDialog.get(Calendar.HOUR_OF_DAY),
                timeForDialog.get(Calendar.MINUTE),
                true).show()
    }

    companion object {
        const val DEFAULT_VALUE = 0
        private val DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL)
        @SuppressLint("SimpleDateFormat")
        private val TIME_FORMAT = SimpleDateFormat("HH:mm") // TODO get Locale-dependent time
    }

}
