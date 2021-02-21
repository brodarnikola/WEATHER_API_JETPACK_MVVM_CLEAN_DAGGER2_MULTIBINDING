package com.vjezba.weatherapi.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vjezba.weatherapi.databinding.FragmentServiceMusicExampleBinding
import com.vjezba.weatherapi.services.ServiceMusic
import kotlinx.android.synthetic.main.activity_weather.*


class ServiceMusicExampleFragment : Fragment() {

    lateinit var binding: FragmentServiceMusicExampleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentServiceMusicExampleBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.tvToolbarTitle?.text = "SERVICE MUSIC EXAMPLE"

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {

        binding.btnStartMusicService.setOnClickListener {
            activity?.startService(Intent(requireContext(), ServiceMusic::class.java))
        }

        binding.btnStopMusicService.setOnClickListener {
            activity?.stopService(Intent(requireContext(), ServiceMusic::class.java))
        }
    }

}