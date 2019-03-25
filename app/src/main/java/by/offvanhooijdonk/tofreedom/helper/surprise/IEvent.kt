package by.offvanhooijdonk.tofreedom.helper.surprise

import android.app.Activity
import android.content.Context
import android.support.v7.app.ActionBar
import android.view.ViewGroup

interface IEvent {
    fun run()

    interface IEventBuilder {
        fun context(context: Context): IEventBuilder

        fun rootView(root: ViewGroup): IEventBuilder

        fun activity(activity: Activity): IEventBuilder

        fun actionBar(actionBar: ActionBar): IEventBuilder

        fun build(): IEvent
    }
}
