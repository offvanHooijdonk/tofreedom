package by.offvanhooijdonk.tofreedom.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import by.offvanhooijdonk.tofreedom.R;

public class PrefHelper {
    public static final int FREEDOM_TIME_DEFAULT = 0;
    public static final int MAX_YEARS_AHEAD = 10;

    public static long getFreedomTime(Context ctx) {
        return provideSharedPreferences(ctx).getLong(ctx.getString(R.string.pref_freedom_date_key), FREEDOM_TIME_DEFAULT);
    }

    public static void setFreedomTime(Context ctx, long value) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_freedom_date_key), value).apply();
    }

    public static void dropFreedomTime(Context ctx) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_freedom_date_key), FREEDOM_TIME_DEFAULT).apply();
    }

    public static long getCountdownStartDate(Context ctx) {
        return provideSharedPreferences(ctx).getLong(ctx.getString(R.string.pref_cnt_started_date_key), FREEDOM_TIME_DEFAULT);
    }

    public static void setCountdownStartDate(Context ctx, long value) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_cnt_started_date_key), value).apply();
    }

    private static SharedPreferences provideSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
}
