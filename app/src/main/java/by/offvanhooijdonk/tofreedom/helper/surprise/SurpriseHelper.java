package by.offvanhooijdonk.tofreedom.helper.surprise;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;

public class SurpriseHelper {
    private static final double FACTOR_EVENT = 0.2;
    /*private static final double FACTOR_EVENT = 1;*/

    private ViewGroup root;
    private Context ctx;
    private Activity activity;
    private ActionBar actionBar;

    public SurpriseHelper(Context context, Activity activity, ViewGroup root, ActionBar actionBar) {
        this.ctx = context;
        this.activity = activity;
        this.root = root;
        this.actionBar = actionBar;
    }

    public void startRollingEvent() {
        startRollForEvent();
    }
    

    private void pickAndRunEvent() {
        IEvent.IEventBuilder builder = EventFactory.getRandomEventBuilder();
        /*BatsEvent batsEvent = new BatsEvent.Builder()*/
        IEvent event = builder
                .actionBar(actionBar)
                .activity(activity)
                .context(ctx)
                .rootView(root)
                .build();

        event.run();
    }

    private void startRollForEvent() {
        Handler handler = new Handler();
        Runnable roll = new Runnable() {
            @Override
            public void run() {
                if (rollForEvent()) {
                    pickAndRunEvent();
                } else {
                    handler.postDelayed(this, 30 * 1000);
                }
            }
        };
        handler.post(roll);
    }

    private boolean rollForEvent() {
        return Math.random() < FACTOR_EVENT;
    }
}
