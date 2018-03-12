package by.offvanhooijdonk.tofreedom.helper.surprise.event;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;

import by.offvanhooijdonk.tofreedom.helper.surprise.IEvent;

public abstract class BaseEvent implements IEvent {
    private Context ctx;
    private ViewGroup root;
    private ActionBar actionBar;
    private Activity activity;

    public Context getContext() {
        return ctx;
    }

    public void setContext(Context ctx) {
        this.ctx = ctx;
    }

    public ViewGroup getRoot() {
        return root;
    }

    public void setRoot(ViewGroup root) {
        this.root = root;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public static abstract class BaseBuilder<T extends BaseEvent> implements IEventBuilder {
        private T event;

        protected void setEvent(T event) {
            this.event = event;
        }

        protected T getEvent() {
            return event;
        }

        @Override
        public BaseBuilder<T> context(Context context) {
            getEvent().setContext(context);
            return this;
        }

        @Override
        public BaseBuilder<T> rootView(ViewGroup root) {
            getEvent().setRoot(root);
            return this;
        }

        @Override
        public BaseBuilder<T> activity(Activity activity) {
            getEvent().setActivity(activity);
            return this;
        }

        @Override
        public BaseBuilder<T> actionBar(ActionBar actionBar) {
            getEvent().setActionBar(actionBar);
            return this;
        }

        @Override
        public T build() {
            return getEvent();
        }
    }
}
