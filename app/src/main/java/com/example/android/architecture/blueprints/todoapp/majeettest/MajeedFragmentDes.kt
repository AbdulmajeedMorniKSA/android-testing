package com.example.android.architecture.blueprints.todoapp.majeettest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.R
import kotlinx.android.synthetic.main.fragment_majeed_des.*

class MajeedFragmentDes : Fragment() {

    private val args: MajeedFragmentDesArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_majeed_des, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTest.text = args.testInfoArgs
    }
}
