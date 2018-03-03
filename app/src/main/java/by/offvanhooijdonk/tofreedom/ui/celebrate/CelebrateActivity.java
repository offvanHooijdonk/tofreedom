package by.offvanhooijdonk.tofreedom.ui.celebrate;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.DateFormatHelper;
import by.offvanhooijdonk.tofreedom.helper.PrefHelper;
import by.offvanhooijdonk.tofreedom.helper.celebrate.AchievementsHelper;
import by.offvanhooijdonk.tofreedom.helper.celebrate.MusicHelper;
import by.offvanhooijdonk.tofreedom.helper.celebrate.ParticlesHelper;

public class CelebrateActivity extends AppCompatActivity {
    private TextView txtGreeting;
    private View blockTimeElapsed;
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
    private AchievementsHelper achievementsHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);

        initViews();

        fabReplay.setOnClickListener(v -> {
            if (achievementsHelper != null) {
                achievementsHelper.dropToInitial();
            }
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

    private void initViews() {
        root = findViewById(R.id.root);
        viewStartCorner = findViewById(R.id.viewStartCorner);
        viewEndCorner = findViewById(R.id.viewEndCorner);
        viewAnchor = findViewById(R.id.viewAnchor);
        txtGreeting = findViewById(R.id.txtGreeting);
        blockTimeElapsed = findViewById(R.id.blockTimeElapsed);
        txtTimeElapsed = findViewById(R.id.txtTimeElapsed);
        txtStarredNumber = findViewById(R.id.txtStarredNumber);
        txtSorrowTimes = findViewById(R.id.txtSorrowTimes);
        txtHappyTimes = findViewById(R.id.txtHappyTimes);
        glGreeting = findViewById(R.id.glGreeting);
        fabReplay = findViewById(R.id.fabStopReplay);

        txtTimeElapsed.setText(getString(R.string.achiev_elapsed, DateFormatHelper.formatElapsed(
                PrefHelper.getCountdownStartDate(this), PrefHelper.getFreedomTime(this)
        )));
    }

    private void runCelebrations() {
        musicHelper = new MusicHelper(getApplicationContext());
        playMusic();
        int particleDelay = musicHelper.getPunchTime();

        fireworksHelper = new ParticlesHelper.Fireworks();
        confettiHelper = new ParticlesHelper.Confetti();
        prepareAchievements();

        new Handler().postDelayed(this::startParticles, particleDelay);
        new Handler().postDelayed(this::runAchievements, particleDelay); // different than the particles delay
    }

    private void prepareAchievements() {
        achievementsHelper = new AchievementsHelper.Builder()
                .moveView(glGreeting)
                .addAchievement(blockTimeElapsed)
                .addAchievement(txtStarredNumber)
                .addAchievement(txtSorrowTimes)
                .addAchievement(txtHappyTimes)
                .durationDefault()
                .delayBetweenDefault()
                .build();
    }

    private void runAchievements() {
        achievementsHelper.runAchievements();
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
