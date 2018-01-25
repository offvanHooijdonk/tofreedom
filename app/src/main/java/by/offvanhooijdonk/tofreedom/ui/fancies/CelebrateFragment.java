package by.offvanhooijdonk.tofreedom.ui.fancies;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import by.offvanhooijdonk.tofreedom.R;
import by.offvanhooijdonk.tofreedom.helper.fancies.ParticlesHelper;

public class CelebrateFragment extends Fragment {

    private TextView txtGreeting;
    private View viewAnchor;

    private ParticlesHelper particlesHelper = new ParticlesHelper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_celebrate, container, false);

        viewAnchor = v.findViewById(R.id.viewAnchor);
        txtGreeting = v.findViewById(R.id.txtGreeting);
        txtGreeting.setOnClickListener(v1 -> tryParticle(viewAnchor));

        particlesHelper.initialize();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        new Handler().postDelayed(() -> {
            particlesHelper.setupMaxDimens(getView());
            particlesHelper.runParticles(getActivity(), viewAnchor);
        }, 100);
    }

    private void tryParticle(View v) {
        //particleSystem.setSpeedRange(.1f, .2f).setFadeOut(350).setScaleRange(1f, 2f).oneShot(v, 20, new DecelerateInterpolator());
        //setupMaxDimens();

        new Handler().postDelayed(() -> {
            //changeParticleLocation(v);
            new ParticleSystem(getActivity(), 10, R.drawable.particle_round_red, 1000).setSpeedRange(.1f, .2f).setFadeOut(350).setScaleRange(1f, 2f)
                    .oneShot(v, 10, new DecelerateInterpolator());
        }, 100);
        new Handler().postDelayed(() -> {
            //changeParticleLocation(v);
            new ParticleSystem(getActivity(), 10, R.drawable.particle_round_blue, 1000).setSpeedRange(.1f, .2f).setFadeOut(350).setScaleRange(1f, 2f)
                    .oneShot(v, 10, new DecelerateInterpolator());
        }, 500);
        new Handler().postDelayed(() -> {
            //changeParticleLocation(v);
            new ParticleSystem(getActivity(), 10, R.drawable.particle_round_red, 1000).setSpeedRange(.1f, .2f).setFadeOut(350).setScaleRange(1f, 2f)
                    .oneShot(v, 10, new DecelerateInterpolator());
        }, 1000);
        new Handler().postDelayed(() -> {
            //changeParticleLocation(v);
            new ParticleSystem(getActivity(), 10, R.drawable.particle_round_green, 1000).setSpeedRange(.1f, .2f).setFadeOut(350).setScaleRange(1f, 2f)
                    .oneShot(v, 10, new DecelerateInterpolator());
        }, 1400);

    }

}
