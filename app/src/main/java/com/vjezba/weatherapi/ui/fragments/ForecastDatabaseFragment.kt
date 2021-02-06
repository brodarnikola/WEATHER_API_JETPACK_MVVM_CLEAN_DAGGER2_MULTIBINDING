package com.vjezba.weatherapi.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.weatherapi.databinding.FragmentForecastDatabaseBinding
import com.vjezba.weatherapi.ui.adapters.ForecastAdapter
import com.vjezba.weatherapi.ui.adapters.ForecastDatabaseAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_forecast.*

@AndroidEntryPoint
class ForecastDatabaseFragment : Fragment() {

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var forecastDatabaseAdapter: ForecastDatabaseAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    lateinit var binding: FragmentForecastDatabaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentForecastDatabaseBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.tvToolbarTitle?.text = "FORECAST DATABASE ROOM "

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initializeUi()

        forecastViewModel.forecastList.observe(this@ForecastDatabaseFragment, Observer { items ->
            Log.d(ContentValues.TAG, "Data is: ${items.forecastList.joinToString { "-" }}")
            progressBar.visibility = View.GONE

            forecastDatabaseAdapter.updateDevices(items.forecastList.toMutableList())
        })

        forecastViewModel.getWeatherFromLocalStorage()
    }

    private fun initializeUi() {

        weatherLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        forecastDatabaseAdapter = ForecastDatabaseAdapter( mutableListOf() )

        forecast_list.apply {
            layoutManager = weatherLayoutManager
            adapter = forecastDatabaseAdapter
        }
        forecast_list.adapter = forecastDatabaseAdapter
    }

}