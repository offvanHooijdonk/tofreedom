package by.offvanhooijdonk.tofreedom.helper.surprise

import java.util.ArrayList
import java.util.Random

import by.offvanhooijdonk.tofreedom.helper.surprise.event.BatsEvent
import by.offvanhooijdonk.tofreedom.helper.surprise.event.ScreamEvent

object EventFactory {
    private var events = mutableListOf<IEvent.IEventBuilder>(
            BatsEvent.Builder(),
            ScreamEvent.Builder()
    )

    val randomEventBuilder: IEvent.IEventBuilder
        get() {
            val index = Random().nextInt(events.size)
            return events[index]
        }

}
