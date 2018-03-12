package by.offvanhooijdonk.tofreedom.helper.surprise;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;

public interface IEvent {
    void run();

    interface IEventBuilder {
        IEventBuilder context(Context context);

        IEventBuilder rootView(ViewGroup root);

        IEventBuilder activity(Activity activity);

        IEventBuilder actionBar(ActionBar actionBar);

        IEvent build();
    }
}
