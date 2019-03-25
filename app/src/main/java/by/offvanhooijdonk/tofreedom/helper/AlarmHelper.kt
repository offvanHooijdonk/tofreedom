package by.offvanhooijdonk.tofreedom.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

import java.util.Calendar

import by.offvanhooijdonk.tofreedom.ui.countdown.FreedomComingBR

object AlarmHelper { // TODO rename

    fun setupFinishingNotification(ctx: Context) {
        cancelNotification(ctx)

        var notifyTime = getTimeBeforeFreedom(ctx)
        val currTime = System.currentTimeMillis()
        notifyTime = if (currTime > notifyTime) nextMinute(currTime) else notifyTime // if notify time in past - use current time next minute

        getAlarmManager(ctx).setExact(
                AlarmManager.RTC_WAKEUP,
                notifyTime,
                prepareNotificationPI(ctx))
    }

    private fun getAlarmManager(ctx: Context): AlarmManager {
        return ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun cancelNotification(ctx: Context) {
        getAlarmManager(ctx).cancel(prepareNotificationPI(ctx))
        Log.i("break-free", "Notification cancelled.")
    }

    private fun prepareNotificationPI(ctx: Context): PendingIntent {
        val intent = Intent(ctx, FreedomComingBR::class.java)
        return PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getTimeBeforeFreedom(ctx: Context): Long {
        val timeFreedom = PrefHelper.getFreedomTime(ctx)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeFreedom
        calendar.add(Calendar.MINUTE, -5) // TODO settings

        return calendar.timeInMillis
    }

    private fun nextMinute(time: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.add(Calendar.MINUTE, 1)
        calendar.set(Calendar.SECOND, 0)

        return calendar.timeInMillis
    }
}
