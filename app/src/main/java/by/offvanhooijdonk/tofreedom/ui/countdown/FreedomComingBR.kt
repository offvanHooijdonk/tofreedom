package by.offvanhooijdonk.tofreedom.ui.countdown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import by.offvanhooijdonk.tofreedom.helper.NotificationHelper

class FreedomComingBR : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {
        NotificationHelper.showFreedomNotification(ctx)
    }
}
