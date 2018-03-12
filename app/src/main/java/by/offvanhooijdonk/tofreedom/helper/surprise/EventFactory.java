package by.offvanhooijdonk.tofreedom.helper.surprise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.offvanhooijdonk.tofreedom.helper.surprise.event.BatsEvent;
import by.offvanhooijdonk.tofreedom.helper.surprise.event.ScreamEvent;

public class EventFactory {
    private static List<IEvent.IEventBuilder> events;

    public static IEvent.IEventBuilder getRandomEventBuilder() {
        if (events == null) {
            initEventsList();
        }

        int index = new Random().nextInt(events.size());
        return events.get(index);
    }

    private static void initEventsList() {
        events = new ArrayList<>();
        events.add(new BatsEvent.Builder());
        events.add(new ScreamEvent.Builder());
    }

}
