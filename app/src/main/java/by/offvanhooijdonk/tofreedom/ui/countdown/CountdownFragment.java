package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CountdownFragment extends Fragment implements FreedomCountdownTimer.CountdownListener {
    private CountDownTimer countdownTimer;
    private CountdownBean countdown = new CountdownBean();
    private long freedomTime;
    private CountdownBean emptyCountdown = new CountdownBean();
    private AnimCountdownHelper animHelper;
    private StringBuilder builderTime = new StringBuilder();
    int prevTimeTextLength = 0;
    private Context ctx;

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
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_countdown, container, false);

        ctx = getActivity().getBaseContext();
        initViews(v);
        animHelper = new AnimCountdownHelper(new FadeOutListener(), new FadeInListener());

        DateFormatHelper.formatForCountdown(emptyCountdown, 0);
        initCountdown();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO  make special helper class for this
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar(); //  TODO check for Activity type - compat or not
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
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = this.getResources().getColor(R.color.md_blue_grey_700, null);
            } else {
                color = this.getResources().getColor(R.color.md_blue_grey_700);
            }
            window.setStatusBarColor(color);
        }

        root.setBackgroundColor(ctx.getResources().getColor(R.color.md_blue_grey_500));
    }

    @Override
    public void onResume() {
        super.onResume();

        long newFreedomTime = PrefHelper.getFreedomTime(ctx);
        if (freedomTime != newFreedomTime) {
            freedomTime = newFreedomTime;
            if (countdownTimer != null) {
                countdownTimer.cancel();
            }

            initCountdown();
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
        // check if text size changes substantially - then add fade animation to hide view resize
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
        handleFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelCountdown();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        cancelCountdown();
    }

    private void handleFinish() {
        // todo set MainView to the fragment
        if (getActivity() instanceof MainView) {
            ((MainView) getActivity()).onCountDownFinished();
        }
    }

    private void initCountdown() {
        freedomTime = PrefHelper.getFreedomTime(ctx);
        long timeDiff = freedomTime - System.currentTimeMillis();

        DateFormatHelper.formatForCountdown(countdown, timeDiff);
        initCountdownValue();
        drawInitialCountdown();
        countdownTimer = new FreedomCountdownTimer(freedomTime - System.currentTimeMillis(), this).start();
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

    private void initViews(View v) {
        txtYear = v.findViewById(R.id.txtYears);
        txtMonth = v.findViewById(R.id.txtMonths);
        txtDay = v.findViewById(R.id.txtDays);
        txtTime = v.findViewById(R.id.txtTime);
        txtLabelYear = v.findViewById(R.id.txtLabelYear);
        txtLabelMonth = v.findViewById(R.id.txtLabelMonth);
        txtLabelDay = v.findViewById(R.id.txtLabelDay);
        blockYear = v.findViewById(R.id.blockYear);
        blockMonthDay = v.findViewById(R.id.blockMonthDay);
        root = v.findViewById(R.id.root);
    }

    private void cancelCountdown() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
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
