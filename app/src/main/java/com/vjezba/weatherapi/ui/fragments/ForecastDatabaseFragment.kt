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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.domain.ResultState
import com.vjezba.domain.model.Forecast
import com.vjezba.domain.model.ForecastData
import com.vjezba.weatherapi.databinding.FragmentForecastDatabaseBinding
import com.vjezba.weatherapi.ui.adapters.ForecastDatabaseAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_forecast_database.*

@AndroidEntryPoint
class ForecastDatabaseFragment : Fragment() {

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var forecastDatabaseAdapter: ForecastDatabaseAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    lateinit var binding: FragmentForecastDatabaseBinding

    private val args: ForecastDatabaseFragmentArgs by navArgs()

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

            when ( items ) {
                is ResultState.Success -> {
                    successUpdateUi(items)
                }
                is ResultState.Error -> {
                    printOutExceptionInsideLog(items)
                }
            }
        })

        forecastViewModel.getWeatherFromLocalStorage()
    }

    private fun successUpdateUi(items: ResultState.Success<*>) {
        val forecastRoomDatabaseList = items.data as Forecast
        Log.d(ContentValues.TAG, "Data is: ${forecastRoomDatabaseList.forecastList.joinToString { "-" }}")
        progressBar.visibility = View.GONE
        forecastDatabaseAdapter.updateDevices(forecastRoomDatabaseList.forecastList.toMutableList())
    }

    private fun printOutExceptionInsideLog(items: ResultState.Error) {
        val exceptionForecast = items.exception
        Log.d(ContentValues.TAG, "Exception inside forecast fragment is: ${   exceptionForecast}")
    }

    private fun initializeUi() {

        tvForecast.text = "City name: ${args.cityName}. Forecast from room, database: "

        weatherLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        forecastDatabaseAdapter = ForecastDatabaseAdapter( mutableListOf() )

        forecast_list.apply {
            layoutManager = weatherLayoutManager
            adapter = forecastDatabaseAdapter
        }
        forecast_list.adapter = forecastDatabaseAdapter
    }

}