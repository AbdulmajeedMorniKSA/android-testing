package com.example.android.architecture.blueprints.todoapp.majeettest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentMajeedDesBinding
import com.example.android.architecture.blueprints.todoapp.util.NotificationHelper
import kotlinx.android.synthetic.main.fragment_majeed_des.*

class MajeedFragmentDes : Fragment() {

    private val args: MajeedFragmentDesArgs by navArgs()

    private lateinit var viewDataBinding: FragmentMajeedDesBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_majeed_des, container, false)
        viewDataBinding = FragmentMajeedDesBinding.bind(view)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTest.text = args.testInfoArgs
        notify.setOnClickListener {
            NotificationHelper.sendNotification(requireContext())
        }
    }
}
