package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.app.ToFreedomApp;
import by.offvanhooijdonk.tofreedom.helper.fancies.MusicHelper;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateFragment extends Fragment {

    private TextView txtGreeting;
    private View viewAnchor;
    private View viewStartCorner;
    private View viewEndCorner;

    private ParticlesHelper.Fireworks fireworksHelper;
    private ParticlesHelper.Confetti confettiHelper;
    private MusicHelper musicHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_celebrate, container, false);

        viewStartCorner = v.findViewById(R.id.viewStartCorner);
        viewEndCorner = v.findViewById(R.id.viewEndCorner);
        viewAnchor = v.findViewById(R.id.viewAnchor);
        txtGreeting = v.findViewById(R.id.txtGreeting);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseAll();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(ToFreedomApp.LOG, "onStart");

        musicHelper = new MusicHelper(getActivity().getApplicationContext());
        playMusic();
        int particleDelay = musicHelper.getPunchTime();

        new Handler().postDelayed(this::startParticles, particleDelay);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        releaseAll();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releaseAll();
    }

    private void startParticles() {
        fireworksHelper = new ParticlesHelper.Fireworks();
        confettiHelper = new ParticlesHelper.Confetti();

        fireworksHelper.initialize(getActivity());
        confettiHelper.initialize(getActivity());

        fireworksHelper.setupMaxDimens(getView());
        fireworksHelper.runParticles(getActivity(), viewAnchor);

        long confDelay = fireworksHelper.getLastDelay();
        confettiHelper.runConfetti(getActivity(), viewStartCorner, viewEndCorner, confDelay);
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
            fireworksHelper = null;
        }

        if (confettiHelper != null) {
            confettiHelper.stop();
            confettiHelper = null;
        }
    }
}
