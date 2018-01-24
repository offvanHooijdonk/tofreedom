package by.offvanhooijdonk.tofreedom.ui.stories;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.ResHelper;
import by.offvanhooijdonk.tofreedom.helper.ShareHelper;
import by.offvanhooijdonk.tofreedom.model.StoryModel;

public class StoryDetailsDialog extends DialogFragment {
    private static final int MAX_TEXT_SIZE_SP = 48;
    private ImageView imgMood;
    private TextView txtStoryText;
    private TextView txtDate;
    private View root;
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

        root = v.findViewById(R.id.root);
        imgMood = v.findViewById(R.id.imgMood);
        txtStoryText = v.findViewById(R.id.txtStoryText);
        txtDate = v.findViewById(R.id.txtDateCreated);
        fabShare = v.findViewById(R.id.fabShare);

        setupSwipeDown();
        if (storyModel.getType() == StoryModel.Type.FEEL_TODAY.getIndex()) {
            imgMood.setImageResource(ResHelper.moodResources[storyModel.getMoodOption()]);
        } else {
            imgMood.setImageResource(R.drawable.ic_plans_24);
        }
        txtDate.setText(DateFormatHelper.formatForStart(storyModel.getDateCreated(), DateFormat.is24HourFormat(getActivity())));
        txtStoryText.setText(storyModel.getText());
        sizeTextByLength(txtStoryText, MAX_TEXT_SIZE_SP);

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
        WindowManager.LayoutParams wParams = getDialog().getWindow().getAttributes();
        wParams.y = (int) (metrics.heightPixels * 0.25f);
        getDialog().getWindow().setAttributes(wParams);
        //getDialog().getWindow().setGravity(Gravity.BOTTOM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getDialog().getWindow().setClipToOutline(false);
        }

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

    private void sizeTextByLength(TextView tv, int maxSizeSp) {
        float startSizeSp = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        ;
        int sizeDelta = 4 * Math.max((tv.getText().length() - 16) / 12, 0);
        float newSizeSp = Math.max(startSizeSp, maxSizeSp - sizeDelta);

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSizeSp);
    }

    private void setupSwipeDown() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset < 0.7f) {
                    dismiss();
                }
            }
        });
    }

}
