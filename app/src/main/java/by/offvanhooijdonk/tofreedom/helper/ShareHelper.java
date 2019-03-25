package by.offvanhooijdonk.tofreedom.helper;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.model.StoryModel;

public class ShareHelper {

    public static void shareStory(Context ctx, StoryModel model) {
        String date = DateFormatHelper.INSTANCE.formatForShare(model.getDateCreated(), DateFormat.is24HourFormat(ctx));
        String text;
        if (model.getType() == StoryModel.Type.FEEL_TODAY.getIndex()) {
            text = ctx.getString(R.string.share_feel_today_macro,
                    date,
                    ResHelper.getMoodText(ctx, StoryModel.Mood.Companion.fromIndex(model.getMoodOption())),
                    model.getText());
        } else {
            text = ctx.getString(R.string.share_future_plan_macro,
                    date,
                    model.getText());
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        ctx.startActivity(Intent.createChooser(shareIntent, ctx.getString(R.string.share_title)));
    }

}
