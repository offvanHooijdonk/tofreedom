package by.offvanhooijdonk.tofreedom.ui.stories;

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
import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;
import by.offvanhooijdonk.tofreedom.helper.ResHelper;
import by.offvanhooijdonk.tofreedom.model.StoryModel;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddStoryDialog extends DialogFragment {
    private EditText editStoryText;
    private Button btnSend;
    private Spinner spMood;

    private StoryModel.Type storyType;
    private StorySaveListener listener;

    public static AddStoryDialog getInstance(@NonNull StoryModel.Type storyType, @Nullable StorySaveListener l) {
        AddStoryDialog dialog = new AddStoryDialog();
        dialog.setStoryType(storyType);
        dialog.setListener(l);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.AppBaseTheme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_story, container, false);

        editStoryText = v.findViewById(R.id.editStoryText);
        spMood = v.findViewById(R.id.spMood);
        btnSend = v.findViewById(R.id.btnSend);

        if (storyType.equals(StoryModel.Type.FEEL_TODAY)) {
            spMood.setAdapter(new MoodSpinnerAdapter(getActivity()));
            spMood.setSelection(2);
            editStoryText.setHint(R.string.feel_today_text_hint);
        } else {
            spMood.setVisibility(View.INVISIBLE);
            editStoryText.setHint(R.string.future_plans_text_hint);
        }

        btnSend.setOnClickListener(view -> {
            saveStory();
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        setCancelable(true);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        new Handler().postDelayed(() -> {
            editStoryText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            editStoryText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 50);

    }

    public void setStoryType(StoryModel.Type storyType) {
        this.storyType = storyType;
    }

    public void setListener(StorySaveListener listener) {
        this.listener = listener;
    }

    private void saveStory() {
        StoryModel model = new StoryModel();
        if (storyType.equals(StoryModel.Type.FEEL_TODAY)) {
            model.setMoodOption(spMood.getSelectedItemPosition());
        }
        model.setText(editStoryText.getText().toString());
        model.setDateCreated(System.currentTimeMillis());
        model.setType(storyType.getIndex());

        Maybe.fromAction(() -> ToFreedomApp.getAppDatabase().storyDao().save(model))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                }, th -> {
                }, () -> {
                    //Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onStorySaved(model);
                    }
                    dismiss();
                }); // TODO th
    }

    public interface StorySaveListener {
        void onStorySaved(StoryModel model);
    }

    private static class MoodSpinnerAdapter extends BaseAdapter {
        private Context ctx;

        public MoodSpinnerAdapter(@NonNull Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return ResHelper.moodResources.length;
        }

        @Override
        public Object getItem(int position) {
            return ResHelper.moodResources[position];
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

            ((ImageView) v).setImageResource(ResHelper.moodResources[position]);

            return v;
        }
    }
}
