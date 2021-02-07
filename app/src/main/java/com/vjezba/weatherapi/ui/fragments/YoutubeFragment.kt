package com.vjezba.weatherapi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.databinding.FragmentYoutubeBinding
import com.vjezba.weatherapi.ui.adapters.ForecastAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*

@AndroidEntryPoint
class YoutubeFragment : Fragment() {

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var forecastAdapter: ForecastAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    lateinit var binding: FragmentYoutubeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.tvToolbarTitle?.text = "FORECAST"

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initializeUi()

//        forecastViewModel.forecastList.observe(this@YoutubeFragment, Observer { items ->
//            Log.d(ContentValues.TAG, "Data is: ${items.forecastList.joinToString { "-" }}")
//            progressBar.visibility = View.GONE
//
//            forecastAdapter.updateDevices(items.forecastList.toMutableList())
//        })
//
//        forecastViewModel.getForecastFromNetwork(cityName)
    }

    private fun initializeUi() {

        //tvForecast.text = "City name: ${cityName}. Forecast for next 5 days: "

        weatherLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        forecastAdapter = ForecastAdapter( mutableListOf() )

//        binding.forecastList.apply {
//            layoutManager = weatherLayoutManager
//            adapter = forecastAdapter
//        }
//        binding.forecastList.adapter = forecastAdapter

        binding.btnRoomOldWeatherData.setOnClickListener {
//            val direction =
//                ForecastFragmentDirections.forecastFragmentToForecastDatabaseFragment( cityName = cityName )
//            findNavController().navigate(direction)
        }
    }

}