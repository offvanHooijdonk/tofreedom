package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bydavy.morpher.DigitalClockView;

import net.time4j.CalendarUnit;
import net.time4j.PrettyTime;
import net.time4j.format.TextWidth;

import java.util.Locale;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.helper.anim.AnimCountdownHelper;
import by.offvanhooijdonk.tofreedom.helper.countdown.CountdownBean;
import by.offvanhooijdonk.tofreedom.helper.countdown.FreedomCountdownTimer;
import by.offvanhooijdonk.tofreedom.ui.StartActivity;
import by.offvanhooijdonk.tofreedom.ui.fances.CelebrateFragment;
import by.offvanhooijdonk.tofreedom.ui.pref.PreferenceActivity;

public class CountdownActivity extends AppCompatActivity implements FreedomCountdownTimer.CountdownListener {
    private CountDownTimer countdownTimer;
    private CountdownBean countdown = new CountdownBean();
    private long freedomTime;
    private CountdownBean emptyCountdown = new CountdownBean();

    private DigitalClockView txtYear;
    private DigitalClockView txtMonth;
    private DigitalClockView txtDay;
    private DigitalClockView txtTime;
    private TextView txtLabelYear;
    private TextView txtLabelMonth;
    private TextView txtLabelDay;
    private View blockYear;
    private View blockMonthDay;
    private View blockTime;
    private View blockContainer;
    private View blockCountdown;

    private AnimCountdownHelper animHelper;
    private StringBuilder builderTime = new StringBuilder();
    int prevTimeTextLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        // TODO  make special helper class for this
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.md_blue_grey_500, null)));
            } else {
                actionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.md_blue_grey_500)));
            }
            /*actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);*/
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = this.getResources().getColor(R.color.md_blue_grey_700, null);
            } else {
                color = this.getResources().getColor(R.color.md_blue_grey_700);
            }
            window.setStatusBarColor(color);
        }

        // ------------

        DateFormatHelper.formatForCountdown(emptyCountdown, 0);

        animHelper = new AnimCountdownHelper(new FadeOutListener(), new FadeInListener());

        initViews();

        //goToCelebrate();
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
    public void onCountdownChange(CountdownBean diffCountdown) {
        updateCountdownWithDiff(diffCountdown);

        if (countdown.year != null) {
            txtYear.setTime(countdown.year);
            txtLabelYear.setText(getLocalCalendarUnitText(countdown.year, CalendarUnit.YEARS));
        }
        if (countdown.month != null) {
            txtMonth.setTime(countdown.month);
            txtLabelMonth.setText(getLocalCalendarUnitText(countdown.month, CalendarUnit.MONTHS));
        }
        if (countdown.day != null) {
            txtDay.setTime(countdown.day);
            txtLabelDay.setText(getLocalCalendarUnitText(countdown.day, CalendarUnit.DAYS));
        }

        String timeText = timeToString();
        //Log.i(ToFreedomApp.LOG, "Time to output: " + timeText);
        int textDiff = prevTimeTextLength - timeText.length();
        if (textDiff >= 2 || textDiff <= -2) {
            // TODO do some animation to smooth the process
            animHelper.addView(txtTime);
            animHelper.animateFadeOut(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    txtTime.setTime(timeText);
                }
            }, AnimCountdownHelper.DURATION_SHORT);
        } else {
            txtTime.setTime(timeText);
        }
        prevTimeTextLength = timeText.length();
    }

    @Override
    public void onFinish() {
        goToCelebrate();
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

    private void goToCelebrate() {
        if (!PrefHelper.getCelebrateShown(this)) {
            blockCountdown.setVisibility(View.GONE);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.blockContainer, new CelebrateFragment())
                    .commit();
            blockContainer.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(blockContainer, View.ALPHA, 0f, 1f).setDuration(500).start();

            PrefHelper.setCelebrateShown(this, true);
        } else {
            // TODO show screen with info that countdown finished
            // tmp workflow
            blockCountdown.setVisibility(View.GONE);
            new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("The Time is Here!")
                    .setMessage("Your countdown is over.")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
        }
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
            goToCelebrate();

            return;
        }

        DateFormatHelper.formatForCountdown(countdown, timeDiff);
        initCountdownValue();
        drawInitialCountdown();
        blockCountdown.setVisibility(View.VISIBLE);
        countdownTimer = new FreedomCountdownTimer(freedomTime - System.currentTimeMillis(), this).start();
    }

    private void initViews() {
        txtYear = findViewById(R.id.txtYears);
        txtMonth = findViewById(R.id.txtMonths);
        txtDay = findViewById(R.id.txtDays);
        txtTime = findViewById(R.id.txtTime);
        txtLabelYear = findViewById(R.id.txtLabelYear);
        txtLabelMonth = findViewById(R.id.txtLabelMonth);
        txtLabelDay = findViewById(R.id.txtLabelDay);
        blockYear = findViewById(R.id.blockYear);
        blockMonthDay = findViewById(R.id.blockMonthDay);
        blockContainer = findViewById(R.id.blockContainer);
        blockCountdown = findViewById(R.id.blockCountdown);
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
        PrefHelper.setCelebrateShown(this, false);

        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void initCountdownValue() {

        boolean isYearEmpty = countdown.year.equals(emptyCountdown.year);
        boolean isMonthEmpty = /*isYearEmpty && */countdown.month.equals(emptyCountdown.month);
        boolean isDayEmpty = /*isMonthEmpty && */countdown.day.equals(emptyCountdown.day);
        boolean isHourEmpty = /*isDayEmpty && */countdown.hour.equals(emptyCountdown.hour);
        boolean isMinuteEmpty = /*isHourEmpty && */countdown.minute.equals(emptyCountdown.minute);

        countdown.year = isYearEmpty ? null : countdown.year;
        countdown.month = isMonthEmpty ? null : countdown.month;
        countdown.day = isDayEmpty ? null : countdown.day;
        countdown.hour = isHourEmpty ? null : countdown.hour;
        countdown.minute = isMinuteEmpty ? null : countdown.minute;
    }

    private void updateCountdownWithDiff(CountdownBean diffCountdown) {
        if (countdown.year != null) {
            countdown.year = pickChanges(countdown.year, diffCountdown.year, emptyCountdown.year);
            if (countdown.year == null) AnimCountdownHelper.fadeAway(blockYear);
        }

        if (countdown.month != null) {
            countdown.month = pickChanges(countdown.month, diffCountdown.month, emptyCountdown.month);
            if (countdown.month == null) {
                AnimCountdownHelper.fadeAway(txtMonth);
                AnimCountdownHelper.fadeAway(txtLabelMonth);
            }
        }

        if (countdown.day != null) {
            countdown.day = pickChanges(countdown.day, diffCountdown.day, emptyCountdown.day);
            if (countdown.day == null) {
                AnimCountdownHelper.fadeAway(txtDay);
                AnimCountdownHelper.fadeAway(txtLabelDay);
            }
        }

        if (countdown.month == null && countdown.day == null) {
            new Handler().postDelayed(() -> blockMonthDay.setVisibility(View.GONE), AnimCountdownHelper.DURATION_DEFAULT);
        }
        countdown.hour = pickChanges(countdown.hour, diffCountdown.hour, emptyCountdown.hour);
        countdown.minute = pickChanges(countdown.minute, diffCountdown.minute, emptyCountdown.minute);

        // assume seconds always change
        countdown.second = diffCountdown.second;
    }

    private String pickChanges(String currValue, String diffValue, String emptyValue) {
        return diffValue != null ?
                (diffValue.equals(emptyValue) ? null : diffValue)
                : currValue;
    }

    private String getLocalCalendarUnitText(String value, CalendarUnit unit) {
        if (value != null && !value.isEmpty()) {
            int valueNum = Integer.valueOf(value);
            String unitText = PrettyTime.of(Locale.getDefault()).print(valueNum, unit, TextWidth.WIDE);
            try {
                unitText = unitText.substring(unitText.indexOf(value) + 2).intern();
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

    private String timeToString() {
        builderTime.delete(0, builderTime.length());
        if (countdown.hour != null) {
            builderTime.append(countdown.hour).append(this.getString(R.string.time_delimiter));
        }
        if (countdown.minute != null) {
            builderTime.append(countdown.minute).append(this.getString(R.string.time_delimiter));
        }

        builderTime.append(countdown.second);

        return builderTime.toString();
    }

    private void drawInitialCountdown() {
        if (countdown.year == null) {
            blockYear.setVisibility(View.GONE);
        } else {
            txtYear.setTime(countdown.year);
            blockYear.setVisibility(View.VISIBLE);
        }

        if (countdown.month == null && countdown.day == null) {
            blockMonthDay.setVisibility(View.GONE);
        } else {
            if (countdown.month == null) {
                txtMonth.setVisibility(View.GONE);
                txtLabelMonth.setVisibility(View.GONE);
            } else {
                txtMonth.setTime(countdown.month);
                txtMonth.setVisibility(View.VISIBLE);
                txtLabelMonth.setVisibility(View.VISIBLE);
            }

            if (countdown.day == null) {
                txtDay.setVisibility(View.GONE);
                txtLabelDay.setVisibility(View.GONE);
            } else {
                txtDay.setTime(countdown.day);
                txtDay.setVisibility(View.VISIBLE);
                txtLabelDay.setVisibility(View.VISIBLE);
            }
        }

        String timeText = timeToString();
        prevTimeTextLength = timeText.length();
        txtTime.setTime(timeText);
    }

    private class FadeOutListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            // TODO do all settings and hidings here
            //txtSecond.setText(diffCountdown.second);

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
