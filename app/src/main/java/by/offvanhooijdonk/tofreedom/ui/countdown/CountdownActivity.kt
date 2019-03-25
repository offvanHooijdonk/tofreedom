package by.offvanhooijdonk.tofreedom.ui.countdown

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.helper.PrefHelper
import by.offvanhooijdonk.tofreedom.ui.AboutActivity
import by.offvanhooijdonk.tofreedom.ui.StartActivity
import by.offvanhooijdonk.tofreedom.ui.celebrate.CelebrateActivity
import by.offvanhooijdonk.tofreedom.ui.pref.PreferenceActivity

class CountdownActivity : AppCompatActivity(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)

        val freedomTime = PrefHelper.getFreedomTime(this)
        if (freedomTime == PrefHelper.FREEDOM_TIME_DEFAULT.toLong()) {
            // todo show message that date not set, or go to Start Activity?
            goToStartActivity()
        }
    }

    override fun onResume() {
        super.onResume()

        val freedomTime = PrefHelper.getFreedomTime(this)

        if (System.currentTimeMillis() >= freedomTime) {
            if (PrefHelper.getCelebrateShown(this)) {
                goToComplete()
            } else {
                goToCelebrate()
            }
        } else {
            goToCountdown()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val itemId = item.itemId
        when (itemId) {
            R.id.action_settings -> startActivity(Intent(this, PreferenceActivity::class.java))
            R.id.action_drop_time -> startDropConfirmDialog()
            R.id.action_about -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.action_celebrate -> {
                PrefHelper.setCelebrateShown(this, false)
                goToCelebrate()
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun goToCountdown() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.blockContainer, CountdownFragment())
                .commit()
    }

    private fun goToComplete() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.blockContainer, CompletedFragment())
                .commit()
    }

    private fun goToCelebrate() {
        startActivity(Intent(this, CelebrateActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        PrefHelper.setCelebrateShown(this, true)
    }

    private fun startDropConfirmDialog() {
        AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.confirm_drop_title)
                .setMessage(R.string.confirm_drop_message)
                .setPositiveButton(R.string.dialog_btn_positive) { dialog, which -> dropTime() }
                .setNegativeButton(R.string.dialog_btn_negative) { dialog, which -> dialog.dismiss() }
                .show()
    }

    private fun dropTime() {
        PrefHelper.dropFreedomTime(this)
        PrefHelper.setCelebrateShown(this, false)

        goToStartActivity()
    }

    private fun goToStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onCountDownFinished() {
        goToCelebrate()
    }


}
