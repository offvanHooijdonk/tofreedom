package by.offvanhooijdonk.tofreedom.helper;

import android.content.Context;
import android.content.Intent;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.model.StoryModel;

public class ShareHelper {

    public static void shareStory(Context ctx, StoryModel model) {
        String date = DateFormatHelper.formatForStart(model.getDateCreated());
        String text = ctx.getString(R.string.share_story_macro,
                date,
                ResHelper.getMoodText(ctx, StoryModel.Mood.fromIndex(model.getMoodOption())),
                model.getText());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        ctx.startActivity(Intent.createChooser(shareIntent, ctx.getString(R.string.share_title)));
    }

}
