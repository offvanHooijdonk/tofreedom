package by.offvanhooijdonk.tofreedom.app;

import android.app.Application;
import android.preference.PreferenceManager;

import net.danlew.android.joda.JodaTimeAndroid;
import net.time4j.android.ApplicationStarter;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.dao.AppDatabase;

public class ToFreedomApp extends Application {
    public static final String LOG = "toFreedom";
    public static AppDatabase APP_DB;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
        JodaTimeAndroid.init(this);
        ApplicationStarter.initialize(this, true);

        APP_DB = AppDatabase.buildDatabase(getApplicationContext());
    }

    public static AppDatabase getAppDatabase() {
        return APP_DB;
    }
}
