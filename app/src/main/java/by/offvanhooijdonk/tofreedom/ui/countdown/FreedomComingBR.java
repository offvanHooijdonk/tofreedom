package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import by.offvanhooijdonk.tofreedom.helper.NotificationHelper;

public class FreedomComingBR extends BroadcastReceiver {


    @Override
    public void onReceive(Context ctx, Intent intent) {
        NotificationHelper.showFreedomNotification(ctx);
    }

}
