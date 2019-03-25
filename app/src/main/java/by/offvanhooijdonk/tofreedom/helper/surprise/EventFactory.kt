package by.offvanhooijdonk.tofreedom.helper.surprise

import java.util.ArrayList
import java.util.Random

import by.offvanhooijdonk.tofreedom.helper.surprise.event.BatsEvent
import by.offvanhooijdonk.tofreedom.helper.surprise.event.ScreamEvent

object EventFactory {
    private var events: MutableList<IEvent.IEventBuilder>? = null

    val randomEventBuilder: IEvent.IEventBuilder
        get() {
            if (events == null) {
                initEventsList()
            }

            val index = Random().nextInt(events!!.size)
            return events!![index]
        }

    private fun initEventsList() {
        events = ArrayList<IEventBuilder>()
        events!!.add(BatsEvent.Builder())
        events!!.add(ScreamEvent.Builder())
    }

}
