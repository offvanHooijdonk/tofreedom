package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.fancies.MusicHelper;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateActivity extends AppCompatActivity {
    private static final float GUIDELINE_START = 0.5f;
    private static final float GUIDELINE_FINISH = 0.2f;

    private TextView txtGreeting;
    private TextView txtTimeElapsed;
    private TextView txtStarredNumber;
    private TextView txtSorrowTimes;
    private TextView txtHappyTimes;
    private Guideline glGreeting;
    private View viewAnchor;
    private View viewStartCorner;
    private View viewEndCorner;
    private View root;
    private FloatingActionButton fabReplay;

    private ParticlesHelper.Fireworks fireworksHelper;
    private ParticlesHelper.Confetti confettiHelper;
    private MusicHelper musicHelper;
    private List<View> achievementViews;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);

        root = findViewById(R.id.root);
        viewStartCorner = findViewById(R.id.viewStartCorner);
        viewEndCorner = findViewById(R.id.viewEndCorner);
        viewAnchor = findViewById(R.id.viewAnchor);
        txtGreeting = findViewById(R.id.txtGreeting);
        txtTimeElapsed = findViewById(R.id.txtTimeElapsed);
        txtStarredNumber = findViewById(R.id.txtStarredNumber);
        txtSorrowTimes = findViewById(R.id.txtSorrowTimes);
        txtHappyTimes = findViewById(R.id.txtHappyTimes);
        glGreeting = findViewById(R.id.glGreeting);
        fabReplay = findViewById(R.id.fabStopReplay);

        achievementViews = Arrays.asList(txtTimeElapsed, txtStarredNumber, txtSorrowTimes, txtHappyTimes);

        fabReplay.setOnClickListener(v -> {
            fabReplay.hide();
            runCelebrations();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        runCelebrations();
    }

    @Override
    protected void onStop() {
        super.onStop();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        releaseAll();
    }

    private void runCelebrations() {
        musicHelper = new MusicHelper(getApplicationContext());
        playMusic();
        int particleDelay = musicHelper.getPunchTime();

        fireworksHelper = new ParticlesHelper.Fireworks();
        confettiHelper = new ParticlesHelper.Confetti();
        new Handler().postDelayed(this::startParticles, particleDelay);
        new Handler().postDelayed(this::runRetrospective, particleDelay); // different them the particles delay
    }

    private void runRetrospective() {
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) glGreeting.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofFloat(GUIDELINE_START, GUIDELINE_FINISH).setDuration(18000);
        animator.setInterpolator(new DecelerateInterpolator(1.5f));
        animator.addUpdateListener(animation -> {
            lp.guidePercent = (Float) animation.getAnimatedValue();
            glGreeting.setLayoutParams(lp);
        });

        int delay = 1500;
        for (View v : achievementViews) {
            new Handler().postDelayed(() -> {
                v.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(v, View.ALPHA, 0.0f, 1.0f).setDuration(500).start();
            }, delay);
            delay += 1500;
        }

        animator.start();
    }

    private void startParticles() {
        fireworksHelper.initialize(getApplicationContext());
        confettiHelper.initialize(getApplicationContext());

        fireworksHelper.setupMaxDimens(root);
        fireworksHelper.runParticles(this, viewAnchor);

        long confDelay = fireworksHelper.getLastDelay();
        confettiHelper.runConfetti(this, viewStartCorner, viewEndCorner, confDelay);
    }

    private void playMusic() {
        musicHelper.play(animation -> {
            Float value = (Float) animation.getAnimatedValue();
            txtGreeting.setAlpha(value);
        }, animation -> {
            if (Float.compare(animation.getAnimatedFraction(), 1.0f) == 0) {
                fabReplay.show();
            }
        });
    }

    private void releaseAll() {
        if (musicHelper != null) {
            musicHelper.releasePlayer();
        }

        if (fireworksHelper != null) {
            fireworksHelper.stop();
        }

        if (confettiHelper != null) {
            confettiHelper.stop();
        }
    }
}
