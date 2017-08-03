package by.offvanhooijdonk.tofreedom.ui.pref;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;

public class DateTimePreference extends Preference implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final int DEFAULT_VALUE = 0;
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.FULL);
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public DateTimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTimePreference(Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View v = super.onCreateView(parent);
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.pref_date_time, parent, false);
        }

        long currentValue = getPersistedLong(DEFAULT_VALUE);
        if (currentValue != DEFAULT_VALUE) {
            updateSummary(currentValue);
        }

        v.setOnClickListener(view -> startDateDialog());

        return v;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar datePicked = getValueAsCalendar();
        datePicked.set(year, month, dayOfMonth);

        saveValue(datePicked.getTimeInMillis());

        startTimeDialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = getValueAsCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        saveValue(calendar.getTimeInMillis());
    }

    private void saveValue(long timeMillis) {
        persistLong(timeMillis);

        updateSummary(timeMillis);
    }

    private void updateSummary(long timeMillis) {
        String dateTime = DATE_FORMAT.format(new Date(timeMillis)) + " " + TIME_FORMAT.format(new Date(timeMillis));
        setSummary(dateTime);
    }

    private void startDateDialog() {
        Calendar dateForDialog = getValueAsCalendar();

        DatePickerDialog dialog = new DatePickerDialog(getContext(), DateTimePreference.this,
                dateForDialog.get(Calendar.YEAR),
                dateForDialog.get(Calendar.MONTH),
                dateForDialog.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());

        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, PrefHelper.MAX_YEARS_AHEAD);
        dialog.getDatePicker().setMaxDate(now.getTimeInMillis());

        dialog.show();
    }


    private void startTimeDialog() {
        Calendar timeForDialog = getValueAsCalendar();

        new TimePickerDialog(getContext(), DateTimePreference.this,
                timeForDialog.get(Calendar.HOUR_OF_DAY),
                timeForDialog.get(Calendar.MINUTE),
                true).show();
    }

    private Calendar getValueAsCalendar() {
        Calendar calendar = Calendar.getInstance();

        long currentValue = getPersistedLong(DEFAULT_VALUE);
        if (currentValue != DEFAULT_VALUE) {
            calendar.setTimeInMillis(currentValue);
        }

        return calendar;
    }

}
