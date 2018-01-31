package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateFragment extends Fragment {

    private TextView txtGreeting;
    private View viewAnchor;
    private View viewStartCorner;
    private View viewEndCorner;

    private MediaPlayer player;
    private ParticlesHelper.Fireworks fireworksHelper = new ParticlesHelper.Fireworks();
    private ParticlesHelper.Confetti confettiHelper = new ParticlesHelper.Confetti();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_celebrate, container, false);

        viewStartCorner = v.findViewById(R.id.viewStartCorner);
        viewEndCorner = v.findViewById(R.id.viewEndCorner);
        viewAnchor = v.findViewById(R.id.viewAnchor);
        txtGreeting = v.findViewById(R.id.txtGreeting);

        fireworksHelper.initialize(getActivity());
        confettiHelper.initialize(getActivity());

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Animator greetingFadeIn = ObjectAnimator.ofFloat(txtGreeting, View.ALPHA, 0.0f, 1.0f).setDuration(4000);
        //greetingFadeIn.start();

        playMusic();

        // delay here to be able to measure the fragment view
        new Handler().postDelayed(this::startParticles, 7000);


    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (player != null) player.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (player != null) player.release();
    }

    private void startParticles() {
        fireworksHelper.setupMaxDimens(getView());
        fireworksHelper.runParticles(getActivity(), viewAnchor);

        long confDelay = fireworksHelper.getLastDelay();
        confettiHelper.runConfetti(getActivity(), viewStartCorner, viewEndCorner, confDelay);
    }

    private void playMusic() {
        player = MediaPlayer.create(getActivity(), R.raw.overture1812);
        player.setVolume(0.1f, 0.1f);
        player.seekTo(9500);

        ValueAnimator volumeAnim = ValueAnimator.ofFloat(0.1f, 1.0f).setDuration(5000);
        volumeAnim.setInterpolator(new AccelerateInterpolator(3.0f));
        volumeAnim.addUpdateListener(animation -> {
            Float val = (Float) animation.getAnimatedValue();
            player.setVolume(val, val);
            txtGreeting.setAlpha(val);
        });

        player.start();
        volumeAnim.start();
    }
}
