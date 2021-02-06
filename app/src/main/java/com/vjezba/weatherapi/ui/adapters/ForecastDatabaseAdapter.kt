package com.vjezba.weatherapi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.domain.model.ForecastData
import com.vjezba.weatherapi.databinding.ForecastDatabaseListBinding

class ForecastDatabaseAdapter(
    var forecastResultList: MutableList<ForecastData>
) : RecyclerView.Adapter<ForecastDatabaseAdapter.WeatherViewHolder>() {

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindItem( forecastResultList[position] )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            ForecastDatabaseListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class WeatherViewHolder( private val binding: ForecastDatabaseListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem( item: ForecastData) {

            binding.apply {
                val finalTemp = "Temp: " + item.main.temp
                item.main.temp = finalTemp
                forecast = item
                executePendingBindings()
                tvMax.text = "Temp max: " + item.main.tempMax
                tvFeelsLike.text = "Feels like: " + item.main.feelsLike
                tvWind.text = "Wind Speed: " + item.wind.speed

                tvDate.text = "Date and time: " + item.dateAndTime

                if( item.weather.isNotEmpty() ) {
                    tvDescription.visibility = View.VISIBLE
                    tvDescription.text = "Description: " + item.weather[0].description
                }
                else
                    tvDescription.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int {
        return forecastResultList.size
    }


    fun updateDevices(updatedDevices: MutableList<ForecastData>) {
        forecastResultList.addAll(updatedDevices)
        notifyItemRangeInserted(forecastResultList.size, updatedDevices.size)
    }


}