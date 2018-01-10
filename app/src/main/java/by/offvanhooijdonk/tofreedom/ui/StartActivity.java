package by.offvanhooijdonk.tofreedom.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.ui.countdown.CountdownActivity;

public class StartActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView txtSetFreedomDate;
    private TextView txtSetFreedomTime;
    private TextView txtPickedTime;
    private Button btnContinue;

    private long freedomTime = PrefHelper.FREEDOM_TIME_DEFAULT;
    private boolean isDateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long freedomTime = PrefHelper.getFreedomTime(this);
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT) {
            navigateCountdownView();
        } else {
            init();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar timeNewValue = Calendar.getInstance();
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT) {
            timeNewValue.setTimeInMillis(freedomTime);
        } else {
            timeNewValue.set(Calendar.HOUR_OF_DAY, 12);
            timeNewValue.set(Calendar.MINUTE, 0);
            timeNewValue.set(Calendar.SECOND, 0);
            timeNewValue.set(Calendar.MILLISECOND, 0);
        }

        timeNewValue.set(Calendar.YEAR, year);
        timeNewValue.set(Calendar.MONTH, month);
        timeNewValue.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        freedomTime = timeNewValue.getTimeInMillis();
        updateFreedomDateView();

        isDateSet = true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar timeNewValue = Calendar.getInstance();
        timeNewValue.setTimeInMillis(freedomTime);

        timeNewValue.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeNewValue.set(Calendar.MINUTE, minute);

        freedomTime = timeNewValue.getTimeInMillis();
        updateFreedomDateView();
    }

    private void init() {
        setContentView(R.layout.activity_start);

        txtSetFreedomDate = (TextView) findViewById(R.id.txtSetFreedomDate);
        txtSetFreedomTime = (TextView) findViewById(R.id.txtSetFreedomTime);
        txtPickedTime = (TextView) findViewById(R.id.txtPickedTime);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        txtSetFreedomDate.setOnClickListener(v -> onFreedomDateClick());
        txtSetFreedomTime.setOnClickListener(v -> onFreedomDateTime());
        btnContinue.setOnClickListener(v -> {
            if (isDateSet && freedomTime > System.currentTimeMillis()) {
                PrefHelper.setFreedomTime(StartActivity.this, freedomTime);
                PrefHelper.setCountdownStartDate(StartActivity.this, System.currentTimeMillis());
                navigateCountdownView();
            } else {
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle(R.string.date_warning_title)
                        .setMessage(R.string.date_warning_msg)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        txtPickedTime.setVisibility(View.GONE);
    }

    private void onFreedomDateTime() {
        Calendar now = Calendar.getInstance();
        new TimePickerDialog(this, this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true).show();
    }

    private void onFreedomDateClick() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(now.getTimeInMillis());

        now.add(Calendar.YEAR, PrefHelper.MAX_YEARS_AHEAD);
        dialog.getDatePicker().setMaxDate(now.getTimeInMillis());

        dialog.show();
    }

    private void updateFreedomDateView() {
        if (freedomTime != PrefHelper.FREEDOM_TIME_DEFAULT) {
            txtPickedTime.setText(DateFormatHelper.formatForStart(freedomTime));
            txtPickedTime.setVisibility(View.VISIBLE);
        }
    }

    private void navigateCountdownView() {
        startActivity(new Intent(this, CountdownActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
