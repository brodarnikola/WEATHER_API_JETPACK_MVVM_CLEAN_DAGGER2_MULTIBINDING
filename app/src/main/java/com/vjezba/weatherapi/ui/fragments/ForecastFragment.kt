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
import com.vjezba.weatherapi.databinding.FragmentForecastBinding
import com.vjezba.weatherapi.ui.adapters.WeatherAdapter
import com.vjezba.weatherapi.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_forecast.*

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    var cityName = ""

    val forecastViewModel: ForecastViewModel by viewModels()

    private lateinit var weatherAdapter: WeatherAdapter
    var weatherLayoutManager: LinearLayoutManager? = null

    lateinit var binding: FragmentForecastBinding

    private val args: ForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentForecastBinding.inflate(inflater, container, false)
        context ?: return binding.root

        cityName = args.cityName

        activity?.tvToolbarTitle?.text = "FORECAST"

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initializeUi()

        forecastViewModel.forecastList.observe(this@ForecastFragment, Observer { items ->
            Log.d(ContentValues.TAG, "Data is: ${items.forecastList.joinToString { "-" }}")
            progressBar.visibility = View.GONE

            weatherAdapter.updateDevices(items.forecastList.toMutableList())
        })

        forecastViewModel.getForecastFromNetwork(cityName)
    }

    private fun initializeUi() {

        weatherLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        weatherAdapter = WeatherAdapter( mutableListOf() )

        binding.forecastList.apply {
            layoutManager = weatherLayoutManager
            adapter = weatherAdapter
        }
        binding.forecastList.adapter = weatherAdapter

        binding.btnRoomOldWeatherData.setOnClickListener {
//            val intent = Intent(this, ForecastDatabaseFragment::class.java)
//            startActivity(intent)
//            finish()
        }
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
////                val intent = Intent( this, WeatherActivity::class.java )
////                startActivity(intent)
////                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}