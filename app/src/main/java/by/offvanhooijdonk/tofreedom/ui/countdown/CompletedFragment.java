package by.offvanhooijdonk.tofreedom.ui.countdown;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.offvanhooijdonk.tofreedom.R;

public class CompletedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_completed, container, false); // TODO restore actionbar color to primary
    }
}
