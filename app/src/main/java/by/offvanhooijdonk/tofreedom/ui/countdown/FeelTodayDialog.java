package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import by.offvanhooijdonk.tofreedom.R;

public class FeelTodayDialog extends DialogFragment {
    private EditText editStoryText;
    private /*ImageButton*/ Button btnSend;
    private Spinner spMood;
    //private IBinder windowToken;

    public static FeelTodayDialog getInstance() {
        return new FeelTodayDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.AppBaseTheme_SearchDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_story, container, false);

        editStoryText = v.findViewById(R.id.editStoryText);
        btnSend = v.findViewById(R.id.btnSend);
        spMood = v.findViewById(R.id.spMood);

        spMood.setAdapter(new MoodSpinnerAdapter(getActivity()));

        //showKeyboard(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        setCancelable(true);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        /*new Handler().post(() -> {
            windowToken = editStoryText.getWindowToken();
        });*/
        new Handler().postDelayed(() -> {
            editStoryText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            editStoryText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 50);

    }

    private static class MoodSpinnerAdapter extends BaseAdapter {
        private static final int[] moodResources = {
                R.drawable.ic_mood_very_satisfied_24, R.drawable.ic_mood_satisfied_24, R.drawable.ic_mood_neutral_24,
                R.drawable.ic_mood_dissatisfied_24, R.drawable.ic_mood_very_dissatisfied_24
        };
        private Context ctx;

        public MoodSpinnerAdapter(@NonNull Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return moodResources.length;
        }

        @Override
        public Object getItem(int position) {
            return moodResources[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = LayoutInflater.from(ctx).inflate(R.layout.item_mood, parent, false);
            }

            ((ImageView) v).setImageResource(moodResources[position]);

            return v;
        }
    }
}
