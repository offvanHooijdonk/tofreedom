package by.offvanhooijdonk.tofreedom.helper;

import android.content.Context;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.model.StoryModel;

public class ResHelper {
    public static final int[] moodResources = {
            R.drawable.ic_mood_very_satisfied_24, R.drawable.ic_mood_satisfied_24, R.drawable.ic_mood_neutral_24,
            R.drawable.ic_mood_dissatisfied_24, R.drawable.ic_mood_very_dissatisfied_24
    };

    public static String getMoodText(Context ctx, StoryModel.Mood mood) {
        return mood.getIndex() - 1 <= moodResources.length ? ctx.getResources().getStringArray(R.array.mood_texts)[mood.getIndex()]
                : ctx.getResources().getStringArray(R.array.mood_texts)[StoryModel.Mood.NEUTRAL.getIndex()];
    }
}
