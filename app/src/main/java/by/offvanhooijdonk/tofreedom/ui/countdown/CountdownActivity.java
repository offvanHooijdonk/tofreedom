package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.helper.anim.AnimCountdownHelper;
import by.offvanhooijdonk.tofreedom.helper.countdown.CountdownBean;
import by.offvanhooijdonk.tofreedom.helper.countdown.FreedomCountdownTimer;
import by.offvanhooijdonk.tofreedom.ui.StartActivity;
import by.offvanhooijdonk.tofreedom.ui.pref.PreferenceActivity;

public class CountdownActivity extends AppCompatActivity implements FreedomCountdownTimer.CountdownListener {
    private CountDownTimer countdownTimer;
    private CountdownBean countdown = new CountdownBean();
    private long freedomTime;
    private CountdownBean emptyCountdown = new CountdownBean();
    private CountdownBean diffCountdown = new CountdownBean();

    private TextView txtYear;
    private TextView txtMonth;
    private TextView txtDay;
    private TextView txtHour;
    private TextView txtMinute;
    private TextView txtSecond;
    private TextView txtDelimiter1;
    private TextView txtDelimiter2;
    private TextView txtLabelMonth;
    private TextView txtLabelDay;
    private View blockYear;
    private View blockMonthDay;
    private View blockTime;

    private AnimCountdownHelper animHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        DateFormatHelper.formatForCountdown(emptyCountdown, 0);

        animHelper = new AnimCountdownHelper(new FadeOutListener(), new FadeInListener());

        initViews();

        initCountdown();
    }

    @Override
    protected void onResume() {
        super.onResume();

        long newFreedomTime = PrefHelper.getFreedomTime(this);
        if (freedomTime != newFreedomTime) {
            freedomTime = newFreedomTime;
            if (countdownTimer != null) {
                countdownTimer.cancel();
            }

            initCountdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }

    @Override
    public void onCountdownChange(CountdownBean newDiffCountdown) {
        copyCountdown(this.diffCountdown, newDiffCountdown);

        /*if (diffCountdown.year != null) {
            txtYear.setText(diffCountdown.year);
        }
        if (diffCountdown.month != null) {
            txtMonth.setText(diffCountdown.month);
        }
        if (diffCountdown.day != null) {
            txtDay.setText(diffCountdown.day);
        }
        if (diffCountdown.hour != null) {
            txtHour.setText(diffCountdown.hour);
        }
        if (diffCountdown.minute != null) {
            txtMinute.setText(diffCountdown.minute);
        }*/

        animHelper.addView(txtSecond);
        animHelper.animateFadeOut();
    }

    @Override
    public void onFinish() {
        // todo WIN FREEDOM HAPPINESS
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            startActivity(new Intent(this, PreferenceActivity.class));
        } else if (itemId == R.id.action_drop_time) {
            startDropConfirmDialog();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initCountdown() {
        freedomTime = PrefHelper.getFreedomTime(this);
        if (freedomTime == PrefHelper.FREEDOM_TIME_DEFAULT) {
            // todo show message that date not set, or go to Start Activity?

            return;
        }

        long timeDiff = freedomTime - System.currentTimeMillis();
        if (timeDiff <= 0) {
            // todo WIN FREEDOM HAPPINESS - for the first time. Other times - just PEACE
            Toast.makeText(this, "HOOORAAY!!!", Toast.LENGTH_LONG).show();

            return;
        }

        updateCountdownBean(timeDiff);
        drawInitialCountdown();
        countdownTimer = new FreedomCountdownTimer(freedomTime - System.currentTimeMillis(), this).start();
    }

    private void initViews() {
        txtYear = (TextView) findViewById(R.id.txtYears);
        txtMonth = (TextView) findViewById(R.id.txtMonths);
        txtDay = (TextView) findViewById(R.id.txtDays);
        txtHour = (TextView) findViewById(R.id.txtHours);
        txtMinute = (TextView) findViewById(R.id.txtMinutes);
        txtSecond = (TextView) findViewById(R.id.txtSeconds);
        txtDelimiter1 = (TextView) findViewById(R.id.txtDelimiter1);
        txtDelimiter2 = (TextView) findViewById(R.id.txtDelimiter2);
        txtLabelMonth = (TextView) findViewById(R.id.txtLabelMonth);
        txtLabelDay = (TextView) findViewById(R.id.txtLabelDay);
        blockYear = findViewById(R.id.blockYear);
        blockMonthDay = findViewById(R.id.blockMonthDay);
        blockTime = findViewById(R.id.blockTime);
    }

    private void startDropConfirmDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.confirm_drop_title)
                .setMessage(R.string.confirm_drop_message)
                .setPositiveButton(R.string.dialog_btn_positive, (dialog, which) -> dropTime())
                .setNegativeButton(R.string.dialog_btn_negative, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void dropTime() {
        PrefHelper.dropFreedomTime(this);

        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updateCountdownBean(long diffTimeMillis) {
        DateFormatHelper.formatForCountdown(diffCountdown, diffTimeMillis);

        boolean isYearEmpty = diffCountdown.year.equals(emptyCountdown.year);
        boolean isMonthEmpty = isYearEmpty &&   diffCountdown.month.equals(emptyCountdown.month);
        boolean isDayEmpty = isMonthEmpty &&    diffCountdown.day.equals(emptyCountdown.day);
        boolean isHourEmpty = isDayEmpty &&     diffCountdown.hour.equals(emptyCountdown.hour);
        boolean isMinuteEmpty = isHourEmpty &&  diffCountdown.minute.equals(emptyCountdown.minute);

        countdown.year = isYearEmpty ? null : diffCountdown.year;
        countdown.month = isMonthEmpty ? null : diffCountdown.month;
        countdown.day = isDayEmpty ? null : diffCountdown.day;
        countdown.hour = isHourEmpty ? null : diffCountdown.hour;
        countdown.minute = isMinuteEmpty ? null : diffCountdown.minute;
        countdown.second = diffCountdown.second;
    }

    private void copyCountdown(CountdownBean dest, CountdownBean source) {
        dest.year = source.year;
        dest.month = source.month;
        dest.day = source.day;
        dest.hour = source.hour;
        dest.minute = source.minute;
        dest.second = source.second;
    }

    private void drawInitialCountdown() {
        if (countdown.year == null) {
            blockYear.setVisibility(View.GONE);
        } else {
            txtYear.setText(countdown.year);
            blockYear.setVisibility(View.VISIBLE);
        }

        if (countdown.month == null && countdown.day == null) {
            blockMonthDay.setVisibility(View.GONE);
        } else {
            if (countdown.month == null) {
                txtMonth.setVisibility(View.GONE);
                txtLabelMonth.setVisibility(View.GONE);
            } else {
                txtMonth.setText(countdown.month);
                txtMonth.setVisibility(View.VISIBLE);
                txtLabelMonth.setVisibility(View.VISIBLE);
            }

            if (countdown.day == null) {
                txtDay.setVisibility(View.GONE);
                txtLabelDay.setVisibility(View.GONE);
            } else {
                txtDay.setText(countdown.day);
                txtDay.setVisibility(View.VISIBLE);
                txtLabelDay.setVisibility(View.VISIBLE);
            }
        }

        if (countdown.hour == null) {
            txtHour.setVisibility(View.GONE);
            txtDelimiter1.setVisibility(View.GONE);
        } else {
            txtHour.setText(countdown.hour);
            txtHour.setVisibility(View.VISIBLE);
            txtDelimiter1.setVisibility(View.VISIBLE);
        }

        if (countdown.minute == null) {
            txtMinute.setVisibility(View.GONE);
            txtDelimiter2.setVisibility(View.GONE);
        } else {
            txtMinute.setText(countdown.minute);
            txtMinute.setVisibility(View.VISIBLE);
            txtDelimiter2.setVisibility(View.VISIBLE);
        }

        txtSecond.setText(countdown.second);
    }

    private class FadeOutListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            // TODO do all settings and hidings here
            txtSecond.setText(diffCountdown.second);

            animHelper.animateFadeIn();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            animHelper.clearAnimations();
        }
    }

    private class FadeInListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            animHelper.clearAnimations();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            animHelper.clearAnimations();
        }
    }
}
