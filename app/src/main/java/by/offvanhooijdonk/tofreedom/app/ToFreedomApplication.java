package by.offvanhooijdonk.tofreedom.app;

import android.app.Application;
import android.preference.PreferenceManager;

import by.offvanhooijdonk.tofreedom.R;

/**
 * Created by Yahor_Fralou on 8/2/2017 3:32 PM.
 */

public class ToFreedomApplication extends Application {
    public static final String LOG = "toFreedom";

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }
}
