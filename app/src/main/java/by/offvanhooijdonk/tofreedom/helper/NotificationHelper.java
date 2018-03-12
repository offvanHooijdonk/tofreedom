package by.offvanhooijdonk.tofreedom.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import by.offvanhooijdonk.tofreedom.ui.countdown.FreedomComingBR;

public class NotificationHelper { // TODO rename

    public static void setupFinishingNotification(Context ctx) {
        cancelNotification(ctx);

        long notifyTime = getTimeBeforeFreedom(ctx);
        long currTime = System.currentTimeMillis();
        notifyTime = currTime > notifyTime ? nextMinute(currTime) : notifyTime; // if notify time in past - use current time next minute
        Log.i("break-free", "Notify at " + new Date(notifyTime).toString());

        getAlarmManager(ctx).setExact(
                AlarmManager.RTC_WAKEUP,
                notifyTime,
                prepareNotificationPI(ctx));
    }

    private static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
    }

    private static void cancelNotification(Context ctx) {
        getAlarmManager(ctx).cancel(prepareNotificationPI(ctx));
        Log.i("break-free", "Notification cancelled.");
    }

    private static PendingIntent prepareNotificationPI(Context ctx) {
        Intent intent = new Intent(ctx, FreedomComingBR.class);
        return PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static long getTimeBeforeFreedom(Context ctx) {
        long timeFreedom = PrefHelper.getFreedomTime(ctx);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeFreedom);
        calendar.add(Calendar.MINUTE, -5); // TODO settings

        return calendar.getTimeInMillis();
    }

    private static long nextMinute(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }
}
