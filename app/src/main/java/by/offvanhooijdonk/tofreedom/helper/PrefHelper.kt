package by.offvanhooijdonk.tofreedom.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import by.offvanhooijdonk.tofreedom.R

object PrefHelper {
    const val FREEDOM_TIME_DEFAULT = 0
    const val MAX_YEARS_AHEAD = 10

    fun getFreedomTime(ctx: Context): Long {
        return provideSharedPreferences(ctx).getLong(ctx.getString(R.string.pref_freedom_date_key), FREEDOM_TIME_DEFAULT.toLong())
    }

    fun setFreedomTime(ctx: Context, value: Long) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_freedom_date_key), value).apply()
    }

    fun dropFreedomTime(ctx: Context) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_freedom_date_key), FREEDOM_TIME_DEFAULT.toLong()).apply()
    }

    fun getCountdownStartDate(ctx: Context): Long {
        return provideSharedPreferences(ctx).getLong(ctx.getString(R.string.pref_cnt_started_date_key), FREEDOM_TIME_DEFAULT.toLong())
    }

    fun setCountdownStartDate(ctx: Context, value: Long) {
        provideSharedPreferences(ctx).edit().putLong(ctx.getString(R.string.pref_cnt_started_date_key), value).apply()
    }

    fun getCelebrateShown(ctx: Context): Boolean {
        return provideSharedPreferences(ctx).getBoolean(ctx.getString(R.string.pref_celebrate_shown), false)
    }

    fun setCelebrateShown(ctx: Context, value: Boolean) {
        provideSharedPreferences(ctx).edit().putBoolean(ctx.getString(R.string.pref_celebrate_shown), value).apply()
    }


    private fun provideSharedPreferences(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }
}
