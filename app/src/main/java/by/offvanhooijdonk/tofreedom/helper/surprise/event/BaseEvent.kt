package by.offvanhooijdonk.tofreedom.helper.surprise.event

import android.app.Activity
import android.content.Context
import android.support.v7.app.ActionBar
import android.view.ViewGroup

import by.offvanhooijdonk.tofreedom.helper.surprise.IEvent

abstract class BaseEvent : IEvent {
    lateinit var context: Context
    lateinit var root: ViewGroup
    lateinit var actionBar: ActionBar
    lateinit var activity: Activity

    abstract class BaseBuilder<T : BaseEvent> : IEvent.IEventBuilder {
        protected abstract var event: T

        override fun context(context: Context): BaseBuilder<T> {
            event.context = context
            return this
        }

        override fun rootView(root: ViewGroup): BaseBuilder<T> {
            event.root = root
            return this
        }

        override fun activity(activity: Activity): BaseBuilder<T> {
            event.activity = activity
            return this
        }

        override fun actionBar(actionBar: ActionBar): BaseBuilder<T> {
            event.actionBar = actionBar
            return this
        }

        override fun build(): T {
            return event
        }
    }
}
