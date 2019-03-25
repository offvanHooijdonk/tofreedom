package by.offvanhooijdonk.tofreedom.helper.surprise

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.support.v7.app.ActionBar
import android.view.ViewGroup

class SurpriseHelper(private val ctx: Context, private val activity: Activity,
                     private val root: ViewGroup, private val actionBar: ActionBar?) {

    fun startRollingEvent() {
        startRollForEvent()
    }

    private fun pickAndRunEvent() {
        val builder = EventFactory.randomEventBuilder

        val event = builder
                .apply { actionBar?.let { actionBar(it) }}
                .activity(activity)
                .context(ctx)
                .rootView(root)
                .build()

        event.run()
    }

    private fun startRollForEvent() {
        val handler = Handler()
        val roll = object : Runnable {
            override fun run() {
                if (rollForEvent()) {
                    pickAndRunEvent()
                } else {
                    handler.postDelayed(this, 30 * 1000L)
                }
            }
        }
        handler.post(roll)
    }

    private fun rollForEvent(): Boolean {
        return Math.random() < FACTOR_EVENT
    }

    companion object {
        private const val FACTOR_EVENT = 0.2
    }
}
