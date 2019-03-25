package by.offvanhooijdonk.tofreedom.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.format.DateFormat
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.AlarmHelper
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper
import by.offvanhooijdonk.tofreedom.helper.PrefHelper
import by.offvanhooijdonk.tofreedom.ui.countdown.CountdownActivity
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*

class StartActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var freedomTime = PrefHelper.FREEDOM_TIME_DEFAULT.toLong()
    private var isDateSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val freedomTime = PrefHelper.getFreedomTime(this)
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT.toLong()) {
            navigateCountdownView()
        } else {
            init()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val timeNewValue = Calendar.getInstance()
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT.toLong()) {
            timeNewValue.timeInMillis = freedomTime
        } else {
            timeNewValue.set(Calendar.HOUR_OF_DAY, 12)
            timeNewValue.set(Calendar.MINUTE, 0)
            timeNewValue.set(Calendar.SECOND, 0)
            timeNewValue.set(Calendar.MILLISECOND, 0)
        }

        timeNewValue.set(Calendar.YEAR, year)
        timeNewValue.set(Calendar.MONTH, month)
        timeNewValue.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        freedomTime = timeNewValue.timeInMillis
        updateFreedomDateView()

        isDateSet = true
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val timeNewValue = Calendar.getInstance()
        timeNewValue.timeInMillis = freedomTime

        timeNewValue.set(Calendar.HOUR_OF_DAY, hourOfDay)
        timeNewValue.set(Calendar.MINUTE, minute)

        freedomTime = timeNewValue.timeInMillis
        updateFreedomDateView()
    }

    private fun init() {
        setContentView(R.layout.activity_start)

        txtSetFreedomDate!!.setOnClickListener { v -> onFreedomDateClick() }
        txtSetFreedomTime!!.setOnClickListener { v -> onFreedomDateTime() }
        btnContinue!!.setOnClickListener { v ->
            if (isDateSet && freedomTime > System.currentTimeMillis()) {
                PrefHelper.setFreedomTime(this@StartActivity, freedomTime)
                PrefHelper.setCountdownStartDate(this@StartActivity, System.currentTimeMillis())
                AlarmHelper.setupFinishingNotification(this@StartActivity)
                navigateCountdownView()
            } else {
                AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle(R.string.date_warning_title)
                        .setMessage(R.string.date_warning_msg)
                        .setPositiveButton(android.R.string.ok) { dialog, which -> dialog.dismiss() }
                        .show()
            }
        }

        txtPickedTime!!.visibility = View.GONE
    }

    private fun onFreedomDateTime() {
        val now = Calendar.getInstance()
        TimePickerDialog(this, this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true).show()
    }

    private fun onFreedomDateClick() {
        val now = Calendar.getInstance()
        val dialog = DatePickerDialog(this, this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.minDate = now.timeInMillis

        now.add(Calendar.YEAR, PrefHelper.MAX_YEARS_AHEAD)
        dialog.datePicker.maxDate = now.timeInMillis

        dialog.show()
    }

    private fun updateFreedomDateView() {
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT.toLong()) {
            txtPickedTime!!.text = DateFormatHelper.formatForStart(freedomTime, DateFormat.is24HourFormat(this))
            txtPickedTime!!.visibility = View.VISIBLE
        }
    }

    private fun navigateCountdownView() {
        startActivity(Intent(this, CountdownActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
