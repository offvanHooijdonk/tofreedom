package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import by.offvanhooijdonk.tofreedom.R;

public class FreedomComingBR extends BroadcastReceiver {
    private static final int ID_FREEDOM_COMING = 0;
    private static final String CHANNEL_ID = "BreakFree";

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i("break-free", "Notification preparing...");
        Notification.Builder builder = new Notification.Builder(ctx) // TODO move Notifications to a separate Helper class
                .setContentTitle("Break Free")
                .setContentText("Your freedom soon approaches!")
                .setSmallIcon(R.drawable.ic_broken_chain)
                .setContentIntent(prepareCountdownIntent(ctx))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setupChannel(ctx, builder);

        getNotificationManager(ctx).notify(ID_FREEDOM_COMING, builder.build());
        Log.i("break-free", "Notification shown.");
    }

    public static void removeNotification(Context ctx) {
        getNotificationManager(ctx).cancel(ID_FREEDOM_COMING);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void setupChannel(Context ctx, Notification.Builder builder) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Break Free application", NotificationManager.IMPORTANCE_HIGH);
        getNotificationManager(ctx).createNotificationChannel(channel);
        builder.setChannelId(CHANNEL_ID);
    }

    private PendingIntent prepareCountdownIntent(Context ctx) {
        return PendingIntent.getActivity(ctx, 0, new Intent(ctx, CountdownActivity.class), 0);
    }

    private static NotificationManager getNotificationManager(Context ctx) {
        return (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
