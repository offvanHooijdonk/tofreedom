package by.offvanhooijdonk.tofreedom.helper.surprise;

import android.os.Handler;
import android.view.ViewGroup;

public class SurpriseHelper {
    private static final double FACTOR_EVENT = 0.01;

    private ViewGroup root;

    public void init(ViewGroup root) {
        this.root = root;
        // TODO init

        startRollForEvent();
    }

    private void pickAndRunEvent() {

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
