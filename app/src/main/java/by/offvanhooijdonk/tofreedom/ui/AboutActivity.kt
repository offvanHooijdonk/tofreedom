package by.offvanhooijdonk.tofreedom.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import by.offvanhooijdonk.tofreedom.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if (itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}
