package by.offvanhooijdonk.tofreedom.ui.stories;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.ResHelper;
import by.offvanhooijdonk.tofreedom.helper.ShareHelper;
import by.offvanhooijdonk.tofreedom.model.StoryModel;

public class StoryDetailsDialog extends DialogFragment {
    private ImageView imgMood;
    private TextView txtStoryText;
    private TextView txtDate;
    private FloatingActionButton fabShare;

    private StoryModel storyModel;

    public static StoryDetailsDialog getInstance(StoryModel model) {
        StoryDetailsDialog dialog = new StoryDetailsDialog();
        dialog.setStoryModel(model);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME, R.style.AppBaseTheme_Dialog_StoryDetails);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_story_details, container, false);

        imgMood = v.findViewById(R.id.imgMood);
        txtStoryText = v.findViewById(R.id.txtStoryText);
        txtDate = v.findViewById(R.id.txtDateCreated);
        fabShare = v.findViewById(R.id.fabShare);

        imgMood.setImageResource(ResHelper.moodResources[storyModel.getMoodOption()]);
        txtDate.setText(DateFormatHelper.formatForStart(storyModel.getDateCreated()));
        txtStoryText.setText(storyModel.getText());

        fabShare.setOnClickListener(btn -> {
            ShareHelper.shareStory(getActivity(), storyModel);
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getDialog().getWindow().setLayout(metrics.widthPixels, (int) (metrics.heightPixels * 0.75f));
        getDialog().getWindow().setGravity(Gravity.BOTTOM);

        setCancelable(true);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        fabShare.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(() -> {
            fabShare.show();
        }, getActivity().getResources().getInteger(android.R.integer.config_mediumAnimTime) + 100);
    }

    public void setStoryModel(StoryModel model) {
        storyModel = model;
    }

}
