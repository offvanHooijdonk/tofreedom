package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.ui.AboutActivity;
import by.offvanhooijdonk.tofreedom.ui.StartActivity;
import by.offvanhooijdonk.tofreedom.ui.fancies.CelebrateActivity;
import by.offvanhooijdonk.tofreedom.ui.pref.PreferenceActivity;

public class CountdownActivity extends AppCompatActivity implements MainView {
    private static final String STACK_NAME_CELEBRATE_SCREEN = "celebrate_screen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        long freedomTime = PrefHelper.getFreedomTime(this);
        if (freedomTime == PrefHelper.FREEDOM_TIME_DEFAULT) {
            // todo show message that date not set, or go to Start Activity?
            goToStartActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        long freedomTime = PrefHelper.getFreedomTime(this);

        if (System.currentTimeMillis() >= freedomTime) {
            if (PrefHelper.getCelebrateShown(this)) {
                goToComplete();
            } else {
                goToCelebrate();
            }
        } else {
            goToCountdown();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
            case R.id.action_drop_time:
                startDropConfirmDialog();
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_celebrate: {
                PrefHelper.setCelebrateShown(this, false);
                goToCelebrate();
            }
            break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void goToCountdown() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.blockContainer, new CountdownFragment())
                .commit();
    }

    private void goToComplete() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.blockContainer, new CompletedFragment())
                .commit();
    }

    private void goToCelebrate() {
        startActivity(new Intent(this, CelebrateActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        PrefHelper.setCelebrateShown(this, true);
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

        goToStartActivity();
    }

    private void goToStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCountDownFinished() {
        goToCelebrate();
    }


}
