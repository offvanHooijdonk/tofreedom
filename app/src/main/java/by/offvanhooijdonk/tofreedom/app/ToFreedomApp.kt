package by.offvanhooijdonk.tofreedom.app

import android.app.Application
import android.preference.PreferenceManager

import net.danlew.android.joda.JodaTimeAndroid
import net.time4j.android.ApplicationStarter

import by.offvanhooijdonk.tofreedom.R
import by.offvanhooijdonk.tofreedom.dao.AppDatabase

class ToFreedomApp : Application() {

    companion object {
        val LOG = "toFreedom"
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        PreferenceManager.setDefaultValues(this, R.xml.pref, false)
        JodaTimeAndroid.init(this)
        ApplicationStarter.initialize(this, true)

        appDatabase = AppDatabase.buildDatabase(applicationContext)
    }
}
