package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.fancies.MusicHelper;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateActivity extends AppCompatActivity {
    private TextView txtGreeting;
    private View viewAnchor;
    private View viewStartCorner;
    private View viewEndCorner;
    private View root;

    private ParticlesHelper.Fireworks fireworksHelper;
    private ParticlesHelper.Confetti confettiHelper;
    private MusicHelper musicHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celebrate);

        root = findViewById(R.id.root);
        viewStartCorner = findViewById(R.id.viewStartCorner);
        viewEndCorner = findViewById(R.id.viewEndCorner);
        viewAnchor = findViewById(R.id.viewAnchor);
        txtGreeting = findViewById(R.id.txtGreeting);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        musicHelper = new MusicHelper(getApplicationContext());
        playMusic();
        int particleDelay = musicHelper.getPunchTime();

        fireworksHelper = new ParticlesHelper.Fireworks();
        confettiHelper = new ParticlesHelper.Confetti();
        new Handler().postDelayed(this::startParticles, particleDelay);
    }

    @Override
    protected void onStop() {
        super.onStop();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        releaseAll();
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
