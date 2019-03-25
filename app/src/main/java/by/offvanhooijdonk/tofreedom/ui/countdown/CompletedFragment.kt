package by.offvanhooijdonk.tofreedom.ui.countdown

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import by.offvanhooijdonk.tofreedom.R

class CompletedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_completed, container, false) // TODO restore actionbar color to primary
    }
}
