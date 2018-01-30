package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateFragment extends Fragment {

    private TextView txtGreeting;
    private View viewAnchor;
    private View viewStartCorner;
    private View viewEndCorner;

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

        new Handler().postDelayed(() -> { // delay here to be able to measure the fragment view
            fireworksHelper.setupMaxDimens(getView());
            fireworksHelper.runParticles(getActivity(), viewAnchor);
        }, 100);

        long confDelay = fireworksHelper.getLastDelay();
        confettiHelper.runConfetti(getActivity(), viewStartCorner, viewEndCorner, confDelay);
    }

}
