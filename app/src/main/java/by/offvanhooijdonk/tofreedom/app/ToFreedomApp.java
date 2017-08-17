package by.offvanhooijdonk.tofreedom.app;

import android.app.Application;
import android.preference.PreferenceManager;

import net.time4j.android.ApplicationStarter;

import by.offvanhooijdonk.tofreedom.R;

public class ToFreedomApp extends Application {
    public static final String LOG = "toFreedom";

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
        ApplicationStarter.initialize(this, true);
    }
}
